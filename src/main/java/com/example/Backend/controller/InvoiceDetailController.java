package com.example.Backend.controller;

import com.example.Backend.entity.InvoiceDetail;
import com.example.Backend.service.InvoiceDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/invoiceDetail")
public class InvoiceDetailController {

    @Autowired
    private InvoiceDetailService invoiceDetailService;

    @GetMapping()
    public ResponseEntity<List<InvoiceDetail>> getAllInvoiceDetail() {
        List<InvoiceDetail> invoiceDetails = invoiceDetailService.getAllInvoiceDetail();
        return ResponseEntity.ok(invoiceDetails);
    }

    @GetMapping("/{invoiceId}")
    public ResponseEntity<InvoiceDetail> getInvoiceDetail(@PathVariable long invoiceId) {
        InvoiceDetail invoiceDetail = invoiceDetailService.getInvoiceDetail(invoiceId);
        return ResponseEntity.ok(invoiceDetail);
    }
}
