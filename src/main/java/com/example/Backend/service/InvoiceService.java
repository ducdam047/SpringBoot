package com.example.Backend.service;

import com.example.Backend.dto.request.invoice.InvoiceCreateRequest;
import com.example.Backend.dto.request.invoice.InvoiceCreateRequest1;
import com.example.Backend.dto.request.invoice.InvoiceDetailCreateRequest;
import com.example.Backend.dto.response.InvoiceDTO;
import com.example.Backend.dto.response.MotorbikeDTO;
import com.example.Backend.enums.InvoiceStatus;
import com.example.Backend.entity.Invoice;
import com.example.Backend.entity.InvoiceDetail;
import com.example.Backend.entity.Motorbike;
import com.example.Backend.entity.User;
import com.example.Backend.enums.MotorbikeStatus;
import com.example.Backend.exception.AppException;
import com.example.Backend.exception.ErrorCode;
import com.example.Backend.repository.InvoiceDetailRepository;
import com.example.Backend.repository.InvoiceRepository;
import com.example.Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    @Autowired
    private MotorbikeService motorbikeService;

    @Autowired
    private InvoiceDetailService invoiceDetailService;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceDetailRepository invoiceDetailRepository;

    @Autowired
    private UserRepository userRepository;

    private InvoiceDTO mapToInvoiceDTO(Invoice invoice) {
        return new InvoiceDTO(
                invoice.getInvoiceId(),
                invoice.getUserId(),
                invoice.getMotorbikeIds(),
                invoice.getCreateDate(),
                invoice.getStatus(),
                invoice.getCount()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Invoice> getAllInvoice() {
        return invoiceRepository.findAll();
    }

    public List<Invoice> getInvoice(long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof Jwt jwt) {
            String email = jwt.getClaimAsString("sub");
            User currentUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            userId = currentUser.getId();
            return invoiceRepository.findByUserId(userId);
        }
        throw new AppException(ErrorCode.UNAUTHORIZED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<InvoiceDTO> getInvoiceByStatus(String status) {
        List<Invoice> invoices = invoiceRepository.findByStatus(status);
        if(invoices.isEmpty())
            throw new AppException(ErrorCode.INVOICE_NOT_FOUND);

        return invoices.stream().map(this::mapToInvoiceDTO).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('USER')")
    public Invoice createInvoice(InvoiceCreateRequest invoiceRequest, InvoiceDetailCreateRequest detailRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof Jwt jwt) {
            String email = jwt.getClaimAsString("sub");
            User currentUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

            List<Motorbike> motorbikeList = motorbikeService.getMotorbikeByMotorbikeNameAndLocationNameEntity(invoiceRequest.getMotorbikeRequest().getMotorbikeName(), invoiceRequest.getMotorbikeRequest().getLocationName(), invoiceRequest.getMotorbikeRequest().getCount());
            for(Motorbike motorbike : motorbikeList)
                motorbike.setStatus(MotorbikeStatus.RENTED.name());

            List<MotorbikeDTO> motorbikes = motorbikeService.getMotorbikeRent(invoiceRequest.getMotorbikeRequest().getMotorbikeName(), invoiceRequest.getMotorbikeRequest().getLocationName(), invoiceRequest.getMotorbikeRequest().getCount());
            String listMotorbikeId = motorbikes.stream()
                    .map(MotorbikeDTO::getMotorbikeId)
                    .collect(Collectors.joining(","));

            Invoice invoice = Invoice.builder()
                    .userId(currentUser.getId())
                    .createDate(LocalDateTime.now())
                    .status(InvoiceStatus.UNPAID.name())
                    .count(invoiceRequest.getMotorbikeRequest().getCount())
                    .motorbikeIds(listMotorbikeId)
                    .build();

            Invoice savedInvoice = invoiceRepository.save(invoice);
            invoiceDetailService.createInvoiceDetail(detailRequest, savedInvoice);
            return savedInvoice;
        }
        throw new AppException(ErrorCode.UNAUTHORIZED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteInvoice(long invoiceId) {
        if(!invoiceRepository.existsById(invoiceId))
            throw new RuntimeException("Invoice not found!");

        InvoiceDetail invoiceDetail = invoiceDetailRepository.findByInvoiceId(invoiceId);

        invoiceDetailRepository.delete(invoiceDetail);
        invoiceRepository.deleteById(invoiceId);
    }

    public Invoice createInvoice1(InvoiceCreateRequest1 request1, InvoiceDetailCreateRequest detailRequest) {
        String motorbikeId = request1.getMotorbikeId();
        Motorbike motorbike = motorbikeService.getMotorbike(motorbikeId);
        if(motorbike.getStatus().equals("RENTED"))
            throw new RuntimeException("Xe đã được thuê");
        motorbike.setStatus(MotorbikeStatus.RENTED.name());

        Invoice invoice = Invoice.builder()
                .userId(0)
                .createDate(LocalDateTime.now())
                .status(InvoiceStatus.UNPAID.name())
                .count(1)
                .motorbikeIds(motorbikeId)
                .build();

        Invoice savedInvoice = invoiceRepository.save(invoice);
        invoiceDetailService.createInvoiceDetail1(detailRequest, savedInvoice);
        return savedInvoice;
    }
}
