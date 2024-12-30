package com.example.Backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoicedetail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoicedetailid")
    long invoiceDetailId;

    @Column(name = "invoiceid")
    long invoiceId;

    @Column(name = "fullname")
    String fullName;

    @Column(name = "motorbikename")
    String motorbikeName;

    @Column(name = "receiptdate")
    LocalDateTime receiptDate;

    @Column(name = "paymentdate")
    LocalDateTime paymentDate;

    @Column(name = "reantalprice")
    BigDecimal rentalPrice;

    @Column(name = "rentalhours")
    int rentalHours;

    @Column(name = "totalamout")
    BigDecimal totalAmount;

    @Column(name = "notes")
    String notes;
}
