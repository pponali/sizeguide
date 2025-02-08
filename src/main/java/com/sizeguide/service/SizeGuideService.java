package com.sizeguide.service;

import com.sizeguide.domain.SizeGuide;
import com.sizeguide.dto.SizeGuideTabularWsDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface SizeGuideService {
    String uploadSizeGuideExcel(MultipartFile file);
    Optional<SizeGuideTabularWsDTO> getSizeGuide(String sizeGuideId);
    void validateSizeGuide(String batchId);
    String downloadTemplate();
    
    // Additional methods needed by ValidationJob
    List<SizeGuide> findByValidationStatus(SizeGuide.ValidationStatus status);
    List<SizeGuide> findBySizeGuideId(String sizeGuideId);
    SizeGuide save(SizeGuide sizeGuide);
}
