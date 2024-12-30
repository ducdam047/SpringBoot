package com.example.Backend.dto.request.motorbike;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MotorbikeUpdateRequest {

    String locationName;
    String motorbikeName;
    BigDecimal rentalPrice;
    String imageUrl;
}
