package com.example.Backend.dto.request.invoice;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceDetailCreateRequest {

    LocalDateTime receiptDate;
    int rentalHours;
}
