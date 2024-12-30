package com.example.Backend.dto.request.motorbike;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MotorbikeRentRequest {

    String motorbikeName;
    String locationName;
    int count;
}
