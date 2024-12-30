package com.example.Backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "location")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Location {

    @Id
    @Column(name = "locationid", length = 30)
    String locationId;

    @Column(name = "locationname", length = 50)
    String locationName;

    @Column(name = "address", length = 200)
    String address;
}
