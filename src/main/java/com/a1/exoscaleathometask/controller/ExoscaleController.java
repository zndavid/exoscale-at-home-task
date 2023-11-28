package com.a1.exoscaleathometask.controller;

import com.a1.exoscaleathometask.service.ExoscaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v2")
@RequiredArgsConstructor
public class ExoscaleController {
    private final ExoscaleService exoscaleService;

    @PostMapping("/instance")
    public ResponseEntity<Object> createInstance(@RequestBody String instanceRequest) {
        Object response = exoscaleService.createExoscaleInstance(instanceRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/template")
    public ResponseEntity<Object> getAllTemplates() {
        Object response = exoscaleService.getAllTemplates();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/instance-types")
    public ResponseEntity<Object> getAllInstanceTypes() {
        Object response = exoscaleService.getAllInstanceTypes();
        return ResponseEntity.ok(response);
    }

}
