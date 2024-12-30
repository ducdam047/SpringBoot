package com.example.Backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "motorbike")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Motorbike {

    @Id
    @Column(name = "motorbikeid", length = 10)
    String motorbikeId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "typeid")
    VehicleType vehicleType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "locationid")
    Location location;

    @Column(name = "licenseplate")
    String licensePlate;

    @Column(name = "motorbikename")
    String motorbikeName;

    @Column(name = "rentalprice")
    BigDecimal rentalPrice;

    @Column(name = "status")
    String status;

    @Column(name = "imageurl")
    String imageUrl;
}
