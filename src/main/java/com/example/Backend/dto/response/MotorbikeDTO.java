package com.example.Backend.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MotorbikeDTO {

    String motorbikeId;
    String typeName;
    String locationName;
    String licensePlate;
    String motorbikeName;
    BigDecimal rentalPrice;
    String status;
    String imageUrl;
}
