package com.sizeguide.controller;

import com.sizeguide.dto.SizeGuideTabularWsDTO;
import com.sizeguide.service.SizeGuideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/sizeguides")
@RequiredArgsConstructor
@Tag(name = "Size Guide API", description = "API endpoints for managing size guides")
public class SizeGuideController {

    @Autowired
    private SizeGuideService sizeGuideService;

    @PostMapping("/upload")
    @Operation(summary = "Upload size guide excel file", description = "Upload and process size guide data from excel file")
    public ResponseEntity<String> uploadSizeGuide(
            @Parameter(description = "Excel file containing size guide data")
            @RequestParam("file") MultipartFile file) {
        String batchId = sizeGuideService.uploadSizeGuideExcel(file);
        return ResponseEntity.ok(batchId);
    }

    @GetMapping("/{sizeGuideId}")
    @Operation(summary = "Get size guide by ID", description = "Retrieve size guide data by size guide ID")
    public ResponseEntity<SizeGuideTabularWsDTO> getSizeGuide(
            @Parameter(description = "Size guide ID")
            @PathVariable String sizeGuideId) {
        return sizeGuideService.getSizeGuide(sizeGuideId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/template")
    @Operation(summary = "Download template", description = "Download size guide excel template")
    public ResponseEntity<String> downloadTemplate() {
        String templateUrl = sizeGuideService.downloadTemplate();
        return ResponseEntity.ok(templateUrl);
    }
}
