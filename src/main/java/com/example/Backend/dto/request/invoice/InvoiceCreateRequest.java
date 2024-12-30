package com.example.Backend.dto.request.invoice;

import com.example.Backend.dto.request.motorbike.MotorbikeRentRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceCreateRequest {

    MotorbikeRentRequest motorbikeRequest;
}
