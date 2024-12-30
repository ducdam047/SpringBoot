package com.example.Backend.controller;

import com.example.Backend.dto.request.motorbike.MotorbikeAddRequest;
import com.example.Backend.dto.request.motorbike.MotorbikeRentRequest;
import com.example.Backend.dto.request.motorbike.MotorbikeUpdateRequest;
import com.example.Backend.dto.response.ApiResponse;
import com.example.Backend.dto.response.MotorbikeCanRentResponse;
import com.example.Backend.dto.response.MotorbikeDTO;
import com.example.Backend.entity.Motorbike;
import com.example.Backend.enums.MotorbikeStatus;
import com.example.Backend.service.MotorbikeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/motorbikes")
public class MotorbikeController {

    @Autowired
    private MotorbikeService motorbikeService;

    @GetMapping()
    public ResponseEntity<List<MotorbikeDTO>> getAllMotorbike() {
        List<MotorbikeDTO> motorbikes = motorbikeService.getAllMotorbike();
        return ResponseEntity.ok(motorbikes);
    }

    @GetMapping("/{motorbikeId}")
    public ResponseEntity<Motorbike> getMotorbike(@PathVariable String motorbikeId) {
        Motorbike motorbike = motorbikeService.getMotorbike(motorbikeId);
        return ResponseEntity.ok(motorbike);
    }

    @GetMapping("/typeId/{typeId}")
    public ResponseEntity<List<MotorbikeDTO>> getMotorbikeByVehicleTypeId(@PathVariable String typeId) {
        List<MotorbikeDTO> motorbikes = motorbikeService.getMotorbikeByVehicleTypeId(typeId);
        return ResponseEntity.ok(motorbikes);
    }

    @GetMapping("/typeName/{typeName}")
    public ResponseEntity<List<MotorbikeDTO>> getMotorbikeByVehicleTypeName(@PathVariable String typeName) {
        List<MotorbikeDTO> motorbikes = motorbikeService.getMotorbikeByVehicleTypeName(typeName);
        return ResponseEntity.ok(motorbikes);
    }

    @GetMapping("/locationId/{locationId}")
    public ResponseEntity<List<MotorbikeDTO>> getMotorbikeByLocationId(@PathVariable String locationId) {
        List<MotorbikeDTO> motorbikes = motorbikeService.getMotorbikeByLocationId(locationId);
        return ResponseEntity.ok(motorbikes);
    }

    @GetMapping("/locationName/{locationName}")
    public ResponseEntity<List<MotorbikeDTO>> getMotorbikeByLocationName(@PathVariable String locationName) {
        List<MotorbikeDTO> motorbikes = motorbikeService.getMotorbikeByLocationName(locationName);
        return ResponseEntity.ok(motorbikes);
    }

    @GetMapping("/name/{motorbikeName}")
    public ResponseEntity<List<MotorbikeDTO>> getMotorbikeByName(@PathVariable String motorbikeName) {
        List<MotorbikeDTO> motorbikes = motorbikeService.getMotorbikeByName(motorbikeName);
        return ResponseEntity.ok(motorbikes);
    }

    @PostMapping("/countMotorbikeCanRent")
    public ResponseEntity<MotorbikeCanRentResponse> getCountMotorbikeCanRent(@RequestBody MotorbikeRentRequest request) {
        int count = motorbikeService.countMotorbikeCanRent(request.getMotorbikeName(), request.getLocationName());
        MotorbikeCanRentResponse rentResponse = new MotorbikeCanRentResponse(count);
        return ResponseEntity.ok(rentResponse);
    }

    @PostMapping("/motorbikeName-locationName")
    public ResponseEntity<List<MotorbikeDTO>> getMotorbikeRent(@RequestBody MotorbikeRentRequest request) {
        List<MotorbikeDTO> motorbikes = motorbikeService.getMotorbikeRent(request.getMotorbikeName(), request.getLocationName(), request.getCount());
        return ResponseEntity.ok(motorbikes);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<MotorbikeDTO>> getMotorbikeByStatus(@PathVariable String status) {
        List<MotorbikeDTO> motorbikeDTOS = motorbikeService.getMotorbikeByStatus(status);
        return ResponseEntity.ok(motorbikeDTOS);
    }

    @GetMapping("/address/{motorbikeId}")
    public ResponseEntity<String> getAddressByMotorbikeId(@PathVariable String motorbikeId) {
        String address = motorbikeService.getAddressByMotorbikeId(motorbikeId);
        return ResponseEntity.ok(address);
    }

    @PostMapping("/addMotorbike")
    public ApiResponse<MotorbikeDTO> addMotorbike(
            @RequestParam("motorbikeData") String motorbikeData,
            @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        MotorbikeAddRequest request = new ObjectMapper().readValue(motorbikeData, MotorbikeAddRequest.class);

        ApiResponse<MotorbikeDTO> apiResponse = new ApiResponse<>();
        apiResponse.setCode(201);
        apiResponse.setMessage("Add motorbike successfully");
        apiResponse.setResult(motorbikeService.addMotorbike(request, imageFile));
        return apiResponse;
    }

    @PutMapping("/updateMotorbike/{motorbikeId}")
    MotorbikeDTO updateMotorbike(@PathVariable("motorbikeId") String motorbikeId, @RequestBody MotorbikeUpdateRequest request) {
        return motorbikeService.updateMotorbike(motorbikeId, request);
    }

    @DeleteMapping("/deleteMotorbike/{motorbikeId}")
    String deleteMotorbike(@PathVariable("motorbikeId") String motorbikeId) {
        motorbikeService.deleteMotorbike(motorbikeId);
        return "Motorbike has been deleted";
    }

    @PostMapping("/addMotorbike1")
    public ApiResponse<MotorbikeDTO> addMotorbikeNoImage(@RequestBody MotorbikeAddRequest request) {
        ApiResponse<MotorbikeDTO> apiResponse = new ApiResponse<>();
        apiResponse.setCode(201);
        apiResponse.setMessage("Add motorbike successfully");
        apiResponse.setResult(motorbikeService.addMotorbikeNoImage(request));
        return apiResponse;
    }
}
