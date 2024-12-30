package com.example.Backend.dto.request.invoice;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceRequest1 {

    InvoiceCreateRequest1 invoiceCreateRequest1;
    InvoiceDetailCreateRequest detailCreateRequest;
}
