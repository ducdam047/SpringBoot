package com.example.Backend.service;

import com.example.Backend.dto.request.authentication.AuthenticationRequest;
import com.example.Backend.dto.request.authentication.TokenValidationRequest;
import com.example.Backend.dto.response.AuthenticationResponse;
import com.example.Backend.dto.response.TokenValidationResponse;
import com.example.Backend.entity.User;
import com.example.Backend.exception.AppException;
import com.example.Backend.exception.ErrorCode;
import com.example.Backend.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    public TokenValidationResponse tokenValid(TokenValidationRequest request) throws JOSEException, ParseException {
        var token = request.getToken();

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified =  signedJWT.verify(verifier);

        return TokenValidationResponse.builder()
                .valid(verified && expiryTime.after(new Date()))
                .build();
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        Date now = new Date();
        Date expiration = new Date(now.getTime() + 60 * 60 * 1000);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("ducdam.com")
                .issueTime(now)
                .expirationTime(expiration)
                .claim("scope", buildScope(user))
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());

        JWSObject object = new JWSObject(header, payload);

        try {
            object.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return object.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        if(StringUtils.isEmpty(user.getRole()))
            return "";
        return user.getRole();
    }
}
