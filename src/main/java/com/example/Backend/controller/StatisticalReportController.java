package com.example.Backend.controller;

import com.example.Backend.dto.response.MotorbikeDTO;
import com.example.Backend.entity.Motorbike;
import com.example.Backend.service.StatisticalReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/StatisticalReport")
public class StatisticalReportController {

    @Autowired
    private StatisticalReportService statisticalReportService;

    @GetMapping("/quantity/{date}")
    public ResponseEntity<?> quantityMotorbikeRentInDay(@PathVariable("date") String dateString) {
        try {
            LocalDate date = LocalDate.parse(dateString);
            int quantity = statisticalReportService.quantityMotorbikeRentedInDay(date);
            return ResponseEntity.ok(quantity);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format!");
        }
    }

    @GetMapping("/listMotorbikeRentedInDay")
    public ResponseEntity<List<MotorbikeDTO>> listMotorbikeRented() {
        List<MotorbikeDTO> motorbikes = statisticalReportService.listMotorbikeRentedInDay();
        return ResponseEntity.ok(motorbikes);
    }

    @GetMapping("/revenue/{date}")
    public ResponseEntity<?> revenueInDay(@PathVariable("date") String dateString) {
        try {
            LocalDate date = LocalDate.parse(dateString);
            BigDecimal revenue = statisticalReportService.revenueInDay(date);
            return ResponseEntity.ok(revenue);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format!");
        }
    }
}
