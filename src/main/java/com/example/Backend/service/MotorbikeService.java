package com.example.Backend.service;

import com.example.Backend.enums.MotorbikeStatus;
import com.example.Backend.dto.request.motorbike.MotorbikeAddRequest;
import com.example.Backend.dto.request.motorbike.MotorbikeUpdateRequest;
import com.example.Backend.dto.response.MotorbikeDTO;
import com.example.Backend.entity.Location;
import com.example.Backend.entity.Motorbike;
import com.example.Backend.entity.VehicleType;
import com.example.Backend.exception.AppException;
import com.example.Backend.exception.ErrorCode;
import com.example.Backend.repository.LocationRepository;
import com.example.Backend.repository.MotorbikeRepository;
import com.example.Backend.repository.VehicleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MotorbikeService {

    @Autowired
    private MotorbikeRepository motorbikeRepository;

    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    public MotorbikeDTO mapToMotorbikeDTO(Motorbike motorbike) {
        return new MotorbikeDTO(
                motorbike.getMotorbikeId(),
                motorbike.getVehicleType().getTypeName(),
                motorbike.getLocation().getLocationName(),
                motorbike.getLicensePlate(),
                motorbike.getMotorbikeName(),
                motorbike.getRentalPrice(),
                motorbike.getStatus(),
                motorbike.getImageUrl()
        );
    }

    private List<MotorbikeDTO> findMotorbikes(Function<MotorbikeRepository, List<Motorbike>> finder) {
        List<Motorbike> motorbikes = finder.apply(motorbikeRepository);
        if(motorbikes.isEmpty())
            throw new AppException(ErrorCode.MOTORBIKE_NOT_FOUND);
        return motorbikes.stream().map(this::mapToMotorbikeDTO).collect(Collectors.toList());
    }

    public List<MotorbikeDTO> getAllMotorbike() {
        return findMotorbikes(MotorbikeRepository::findAll);
    }

    public Motorbike getMotorbike(String motorbikeId) {
        return motorbikeRepository.findById(motorbikeId)
                .orElseThrow((() -> new RuntimeException("Motorbike not found!")));
    }

    public List<MotorbikeDTO> getMotorbikeByVehicleTypeId(String typeId) {
        return findMotorbikes(repo -> repo.findByVehicleType_TypeId(typeId));
    }

    public List<MotorbikeDTO> getMotorbikeByVehicleTypeName(String typeName) {
        return findMotorbikes(repo -> repo.findByVehicleType_TypeName(typeName));
    }

    public List<MotorbikeDTO> getMotorbikeByLocationId(String locationId) {
        return findMotorbikes(repo -> repo.findByLocation_LocationId(locationId));
    }

    public List<MotorbikeDTO> getMotorbikeByLocationName(String locationName) {
        return findMotorbikes(repo -> repo.findByLocation_LocationName(locationName));
    }

    public List<MotorbikeDTO> getMotorbikeByName(String motorbikeName) {
        return findMotorbikes(repo -> repo.findByMotorbikeNameContaining(motorbikeName));
    }

    public int countMotorbikeCanRent(String motorbikeName, String locationName) {
        List<MotorbikeDTO> motorbikes = findMotorbikes(repo -> repo.findByMotorbikeNameContainingAndLocation_LocationNameContainingAndStatus(motorbikeName, locationName, MotorbikeStatus.AVAILABLE.name()));
        return motorbikes.size();
    }

    public List<MotorbikeDTO> getMotorbikeRent(String motorbikeName, String locationName, int count) {
        List<MotorbikeDTO> motorbikes = findMotorbikes(repo -> repo.findByMotorbikeNameContainingAndLocation_LocationNameContainingAndStatus(motorbikeName, locationName, MotorbikeStatus.AVAILABLE.name()));

        if(count>motorbikes.size())
            throw new AppException(ErrorCode.MOTORBIKE_COUNT_EXCEEDED);
        if(count>0 && count<motorbikes.size())
            motorbikes = motorbikes.subList(0, count);

        return motorbikes;
    }

    public List<Motorbike> getMotorbikeByMotorbikeNameAndLocationNameEntity(String motorbikeName, String locationName, int count) {
        List<Motorbike> motorbikes = motorbikeRepository.findByMotorbikeNameContainingAndLocation_LocationNameContainingAndStatus(motorbikeName, locationName, MotorbikeStatus.AVAILABLE.name());
        if(count>motorbikes.size())
            throw new AppException(ErrorCode.MOTORBIKE_COUNT_EXCEEDED);
        if(count>0 && count<motorbikes.size())
            motorbikes = motorbikes.subList(0, count);
        return motorbikes;
    }

    public List<MotorbikeDTO> getMotorbikeByStatus(String status) {
        return findMotorbikes(repo -> repo.findByStatus(status));
    }

    public String getAddressByMotorbikeId(String motorbikeId) {
        Motorbike motorbike = motorbikeRepository.findById(motorbikeId)
                .orElseThrow(() -> new AppException(ErrorCode.MOTORBIKE_NOT_FOUND));
        Location location = motorbike.getLocation();
        return location.getAddress();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public MotorbikeDTO addMotorbike(MotorbikeAddRequest request, MultipartFile imageFile) throws IOException {
        String imageUrl = cloudinaryService.uploadImage(imageFile);
        request.setImageUrl(imageUrl);

        VehicleType vehicleType = vehicleTypeRepository.findByTypeName(request.getTypeName())
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_TYPE_NOT_FOUND));
        Location location = locationRepository.findByLocationName(request.getLocationName())
                .orElseThrow(() -> new AppException(ErrorCode.LOCATION_NOT_FOUND));

        Motorbike motorbike = Motorbike.builder()
                .motorbikeId(request.getMotorbikeId())
                .vehicleType(vehicleType)
                .location(location)
                .licensePlate(request.getLicensePlate())
                .motorbikeName(request.getMotorbikeName())
                .rentalPrice(request.getRentalPrice())
                .status(MotorbikeStatus.AVAILABLE.name())
                .imageUrl(request.getImageUrl())
                .build();

        motorbikeRepository.save(motorbike);

        return mapToMotorbikeDTO(motorbike);
    }

    public MotorbikeDTO updateMotorbike(String motorbikeId, @RequestBody MotorbikeUpdateRequest request) {
        Motorbike motorbike = getMotorbike(motorbikeId);
        Location location = motorbike.getLocation();
        if(location!=null)
            location.setLocationName(request.getLocationName());

        motorbike.setMotorbikeName(request.getMotorbikeName());
        motorbike.setRentalPrice(request.getRentalPrice());
        motorbike.setImageUrl(request.getImageUrl());

        motorbikeRepository.save(motorbike);

        return mapToMotorbikeDTO(motorbike);
    }

    public void deleteMotorbike(String motorbikeId) {
        if(!motorbikeRepository.existsById(motorbikeId))
            throw new RuntimeException("Motorbike not found!");

        motorbikeRepository.deleteById(motorbikeId);
    }

    public MotorbikeDTO addMotorbikeNoImage(MotorbikeAddRequest request) {
        VehicleType vehicleType = vehicleTypeRepository.findByTypeName(request.getTypeName())
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_TYPE_NOT_FOUND));
        Location location = locationRepository.findByLocationName(request.getLocationName())
                .orElseThrow(() -> new AppException(ErrorCode.LOCATION_NOT_FOUND));

        Motorbike motorbike = Motorbike.builder()
                .motorbikeId(request.getMotorbikeId())
                .vehicleType(vehicleType)
                .location(location)
                .licensePlate(request.getLicensePlate())
                .motorbikeName(request.getMotorbikeName())
                .rentalPrice(request.getRentalPrice())
                .status(MotorbikeStatus.AVAILABLE.name())
                .imageUrl(request.getImageUrl())
                .build();

        motorbikeRepository.save(motorbike);

        return mapToMotorbikeDTO(motorbike);
    }
}
