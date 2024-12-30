package com.example.Backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "invoice")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoiceid")
    long invoiceId;

    @Column(name = "id")
    long userId;

    @Column(name = "motorbikeid")
    String motorbikeIds;

    @Column(name = "createdate")
    LocalDateTime createDate;

    @Column(name = "status")
    String status;

    @Column(name = "count")
    int count;
}
