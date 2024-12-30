package com.example.Backend.repository;

import com.example.Backend.entity.Location;
import com.example.Backend.entity.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleTypeRepository extends JpaRepository<VehicleType, String> {

    Optional<VehicleType> findByTypeName(String typeName);
}
