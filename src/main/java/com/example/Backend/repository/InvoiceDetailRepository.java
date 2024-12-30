package com.example.Backend.repository;

import com.example.Backend.entity.InvoiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, Long> {

    InvoiceDetail findByInvoiceId(long invoiceId);
}
