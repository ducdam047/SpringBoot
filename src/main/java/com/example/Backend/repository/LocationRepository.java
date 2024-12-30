package com.example.Backend.repository;

import com.example.Backend.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, String> {

    Optional<Location> findByLocationName(String locationName);
}
