package com.example.Backend.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {

    USER_EXISTED(409, "Account already exists", HttpStatus.CONFLICT),
    USER_NOT_EXISTED(404, "Account not found", HttpStatus.NOT_FOUND),
    PASSWORD_NOT_CONFIRM(4001, "Password do not match", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(401, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(403, "You do not have permission", HttpStatus.FORBIDDEN),
    MOTORBIKE_NOT_FOUND(404, "Motorbike not found", HttpStatus.NOT_FOUND),
    MOTORBIKE_UNAVAILABLE(404, "Motorbike unavailable", HttpStatus.NOT_FOUND),
    MOTORBIKE_COUNT_EXCEEDED(400, "Request count exceeded available motorbike", HttpStatus.BAD_REQUEST),
    VEHICLE_TYPE_NOT_FOUND(404, "Vehicle type not found", HttpStatus.NOT_FOUND),
    LOCATION_NOT_FOUND(404, "Location not found", HttpStatus.NOT_FOUND),
    INVOICE_NOT_FOUND(404, "Invoice not found", HttpStatus.NOT_FOUND);

    private int code;
    private String message;
    private HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

}
