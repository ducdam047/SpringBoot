package com.example.Backend.repository;

import com.example.Backend.entity.Motorbike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MotorbikeRepository extends JpaRepository<Motorbike, String> {

    List<Motorbike> findByMotorbikeNameContainingAndLocation_LocationNameContainingAndStatus(String motorbikeName, String locationName, String status);
    List<Motorbike> findByVehicleType_TypeId(String typeId);
    List<Motorbike> findByVehicleType_TypeName(String typeName);
    List<Motorbike> findByLocation_LocationId(String locationId);
    List<Motorbike> findByLocation_LocationName(String locationName);
    List<Motorbike> findByMotorbikeNameContaining(String motorbikeName);
    List<Motorbike> findByStatus(String status);
}
