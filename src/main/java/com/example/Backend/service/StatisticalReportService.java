package com.example.Backend.service;

import com.example.Backend.dto.response.MotorbikeDTO;
import com.example.Backend.entity.Invoice;
import com.example.Backend.entity.InvoiceDetail;
import com.example.Backend.entity.Motorbike;
import com.example.Backend.exception.AppException;
import com.example.Backend.exception.ErrorCode;
import com.example.Backend.repository.InvoiceDetailRepository;
import com.example.Backend.repository.InvoiceRepository;
import com.example.Backend.repository.MotorbikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticalReportService {

    @Autowired
    private MotorbikeService motorbikeService;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceDetailRepository invoiceDetailRepository;

    @Autowired
    private MotorbikeRepository motorbikeRepository;

    private LocalDate datePointer;

    public List<Invoice> invoiceInDay(LocalDate date) {
        LocalDateTime startDay = date.atStartOfDay();
        LocalDateTime endDay = date.plusDays(1).atStartOfDay();
        return invoiceRepository.findInvoicesByDate(startDay, endDay);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public int quantityMotorbikeRentedInDay(LocalDate date) {
        this.datePointer = date;
        List<Invoice> invoices = invoiceInDay(date);
        return invoices.stream()
                .mapToInt(Invoice::getCount)
                .sum();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<MotorbikeDTO> listMotorbikeRentedInDay() {
        List<Invoice> invoices = invoiceInDay(datePointer);
        List<MotorbikeDTO> motorbikes = new ArrayList<>();

        for(Invoice invoice : invoices) {
            String motorbikeIds = invoice.getMotorbikeIds();
            String[] motorbikeIdArray = motorbikeIds.split(",");

            for(String id : motorbikeIdArray) {
                String motorbikeId = id.trim();
                Motorbike motorbike = motorbikeRepository.findById(motorbikeId)
                        .orElseThrow(() -> new AppException(ErrorCode.MOTORBIKE_NOT_FOUND));
                MotorbikeDTO motorbikeDTO = motorbikeService.mapToMotorbikeDTO(motorbike);
                motorbikes.add(motorbikeDTO);
            }
        }

        return motorbikes;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public BigDecimal revenueInDay(LocalDate date) {
        List<Invoice> invoices = invoiceInDay(date);
        BigDecimal totalRevenue = BigDecimal.ZERO;

        for(Invoice invoice : invoices) {
            InvoiceDetail invoiceDetails = invoiceDetailRepository.findByInvoiceId(invoice.getInvoiceId());
            if(invoiceDetails!=null)
                totalRevenue = totalRevenue.add(invoiceDetails.getTotalAmount());
        }

        return totalRevenue;
    }
}
