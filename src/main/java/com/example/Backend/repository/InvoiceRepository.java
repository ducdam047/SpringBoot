package com.example.Backend.repository;

import com.example.Backend.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByUserId(long userId);

    List<Invoice> findByStatus(String status);

    @Query("SELECT i FROM Invoice i WHERE i.createDate >= :startDate AND i.createDate < :endDate")
    List<Invoice> findInvoicesByDate(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
