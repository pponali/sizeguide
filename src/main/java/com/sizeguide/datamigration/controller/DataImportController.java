package com.sizeguide.datamigration.controller;

import com.sizeguide.datamigration.service.DataImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/datamigration")
@RequiredArgsConstructor
public class DataImportController {

    private final DataImportService dataImportService;

    @PostMapping("/start")
    public ResponseEntity<String> startImport() {
        dataImportService.startImport();
        return ResponseEntity.ok("Data import job started successfully");
    }
}
