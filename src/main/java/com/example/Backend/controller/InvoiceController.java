package com.example.Backend.controller;

import com.example.Backend.dto.request.invoice.*;
import com.example.Backend.dto.request.motorbike.MotorbikeRentRequest;
import com.example.Backend.dto.response.InvoiceDTO;
import com.example.Backend.entity.Invoice;
import com.example.Backend.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping()
    public ResponseEntity<List<Invoice>> getAllInvoice() {
        List<Invoice> invoices = invoiceService.getAllInvoice();
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Invoice>> getInvoice(@PathVariable long userId) {
        List<Invoice> invoices = invoiceService.getInvoice(userId);
        return ResponseEntity.ok(invoices);
    }

    @PostMapping("/createInvoice")
    public ResponseEntity<Invoice> createInvoice(@RequestBody InvoiceRequest request) {
        Invoice createInvoice = invoiceService.createInvoice(
                request.getInvoiceCreateRequest(),
                request.getDetailCreateRequest());
        return ResponseEntity.status(201).body(createInvoice);
    }

    @PostMapping("/createInvoiceTest")
    public ResponseEntity<Invoice> createInvoiceTest(
            @ModelAttribute MotorbikeRentRequest motorbikeRentRequest,
            @ModelAttribute InvoiceDetailCreateRequest detailRequest) {
        InvoiceCreateRequest invoiceCreateRequest = new InvoiceCreateRequest(motorbikeRentRequest);
        Invoice createInvoice = invoiceService.createInvoice(
                invoiceCreateRequest,
                detailRequest);
        return ResponseEntity.status(201).body(createInvoice);
    }

    @DeleteMapping("/deleteInvoice/{invoiceId}")
    String deleteInvoice(@PathVariable long invoiceId) {
        invoiceService.deleteInvoice(invoiceId);
        return "Invoice has been deleted";
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<InvoiceDTO>> getInvoiceByStatus(@PathVariable String status) {
        List<InvoiceDTO> invoices = invoiceService.getInvoiceByStatus(status);
        return ResponseEntity.ok(invoices);
    }

    @PostMapping("createInvoice1")
    public ResponseEntity<Invoice> createInvoice1(@RequestBody InvoiceRequest1 request1) {
        Invoice createInvoice = invoiceService.createInvoice1(
                request1.getInvoiceCreateRequest1(),
                request1.getDetailCreateRequest());
        return ResponseEntity.status(201).body(createInvoice);
    }
}
