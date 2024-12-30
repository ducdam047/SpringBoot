package com.example.Backend.dto.request.invoice;

import com.example.Backend.dto.request.invoice.InvoiceCreateRequest;
import com.example.Backend.dto.request.invoice.InvoiceDetailCreateRequest;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceRequest {

    InvoiceCreateRequest invoiceCreateRequest;
    InvoiceDetailCreateRequest detailCreateRequest;
}
