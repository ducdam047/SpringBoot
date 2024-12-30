package com.example.Backend.service;

import com.example.Backend.dto.request.invoice.InvoiceDetailCreateRequest;
import com.example.Backend.entity.Invoice;
import com.example.Backend.entity.InvoiceDetail;
import com.example.Backend.entity.Motorbike;
import com.example.Backend.entity.User;
import com.example.Backend.repository.InvoiceDetailRepository;
import com.example.Backend.repository.MotorbikeRepository;
import com.example.Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class InvoiceDetailService {

    @Autowired
    private InvoiceDetailRepository invoiceDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MotorbikeRepository motorbikeRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public List<InvoiceDetail> getAllInvoiceDetail() {
        return invoiceDetailRepository.findAll();
    }

    public InvoiceDetail getInvoiceDetail(long invoiceId) {
        return invoiceDetailRepository.findByInvoiceId(invoiceId);
    }

    public InvoiceDetail createInvoiceDetail(InvoiceDetailCreateRequest request, Invoice savedInvoice) {
        String userId = String.valueOf(savedInvoice.getUserId());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found from Invoice!"));
        String fullName = user.getFullName();

        String[] motorbikeIds = savedInvoice.getMotorbikeIds().split(",");
        String motorbikeId = motorbikeIds[0];

        Motorbike motorbike = motorbikeRepository.findById(motorbikeId)
                .orElseThrow(() -> new RuntimeException("Motorbike not found form Invoice!"));
        BigDecimal rentalPrice = motorbike.getRentalPrice();
        int rentalHours = request.getRentalHours();
        int count = savedInvoice.getCount();
        BigDecimal totalAmount = rentalPrice.multiply(BigDecimal.valueOf((long) rentalHours * count));

        LocalDateTime receiptDate = request.getReceiptDate();
        LocalDateTime paymentDate = receiptDate.plusHours(rentalHours);

        InvoiceDetail invoiceDetail = InvoiceDetail.builder()
                .invoiceId(savedInvoice.getInvoiceId())
                .fullName(fullName)
                .motorbikeName(motorbike.getMotorbikeName())
                .receiptDate(receiptDate)
                .paymentDate(paymentDate)
                .rentalPrice(rentalPrice)
                .rentalHours(rentalHours)
                .totalAmount(totalAmount)
                .notes("Thank you for renting our motorbike!")
                .build();

        return invoiceDetailRepository.save(invoiceDetail);
    }

    public InvoiceDetail createInvoiceDetail1(InvoiceDetailCreateRequest request, Invoice savedInvoice) {
        String motorbikeId = savedInvoice.getMotorbikeIds();
        Motorbike motorbike = motorbikeRepository.findById(motorbikeId)
                .orElseThrow(() -> new RuntimeException("Motorbike not found form Invoice!"));

        BigDecimal rentalPrice = motorbike.getRentalPrice();
        int rentalHours = request.getRentalHours();
        BigDecimal totalAmount = rentalPrice.multiply(BigDecimal.valueOf((long) rentalHours));

        LocalDateTime receiptDate = request.getReceiptDate();
        LocalDateTime paymentDate = receiptDate.plusHours(rentalHours);

        InvoiceDetail invoiceDetail = InvoiceDetail.builder()
                .invoiceId(savedInvoice.getInvoiceId())
                .fullName("Khách hàng")
                .motorbikeName(motorbike.getMotorbikeName())
                .receiptDate(receiptDate)
                .paymentDate(paymentDate)
                .rentalHours(rentalHours)
                .totalAmount(totalAmount)
                .notes("Thank you for renting our motorbike!")
                .build();

        return invoiceDetailRepository.save(invoiceDetail);
    }
}
