package com.example.Backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "vehicletype")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VehicleType {

    @Id
    @Column(name = "typeid", length = 30)
    String typeId;

    @Column(name = "typename", length = 50)
    String typeName;

    @Column(name = "baserentalprice")
    BigDecimal baseRentalPrice;

    @Column(name = "description")
    String description;

    @Column(name = "rating")
    Integer rating;
}
