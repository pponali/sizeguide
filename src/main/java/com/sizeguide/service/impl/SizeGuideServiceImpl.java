package com.sizeguide.service.impl;

import com.sizeguide.domain.SizeGuide;
import com.sizeguide.domain.SizeGuideDimension;
import com.sizeguide.dto.SizeGuideTabularWsDTO;
import com.sizeguide.mapper.SizeGuideMapper;
import com.sizeguide.repository.SizeGuideRepository;
import com.sizeguide.service.EmailService;
import com.sizeguide.service.S3Service;
import com.sizeguide.service.SizeGuideService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SizeGuideServiceImpl implements SizeGuideService {

    private final SizeGuideRepository sizeGuideRepository;
    private final MongoTemplate mongoTemplate;
    private final SizeGuideMapper sizeGuideMapper;
    private final EmailService emailService;
    private final S3Service s3Service;

    @Override
    public String uploadSizeGuideExcel(MultipartFile file) {
        try {
            String batchId = UUID.randomUUID().toString();
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            // Process each row
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header

                SizeGuide sg = new SizeGuide();
                sg.setSizeGuideId(batchId + "_" + row.getRowNum());
                sg.setDimensions(new ArrayList<>());
                sg.setCreatedAt(LocalDateTime.now());
                sg.setModifiedAt(LocalDateTime.now());
                sg.setValidationStatus(SizeGuide.ValidationStatus.PENDING);

                // Create dimension for each cell
                for (Cell cell : row) {
                    SizeGuideDimension dimension = new SizeGuideDimension();
                    dimension.setDimension(sheet.getRow(0).getCell(cell.getColumnIndex()).getStringCellValue());
                    dimension.setUnit("Cms");
                    dimension.setSize(row.getCell(0).getStringCellValue());
                    dimension.setValue(cell.getStringCellValue());
                    sg.getDimensions().add(dimension);
                }

                sizeGuideRepository.save(sg);
            }

            return batchId;
        } catch (IOException e) {
            throw new RuntimeException("Failed to process Excel file", e);
        }
    }

    @Override
    public Optional<SizeGuideTabularWsDTO> getSizeGuide(String sizeGuideId) {
        return sizeGuideRepository.findBySizeGuideId(sizeGuideId)
                .map(sizeGuideMapper::toDto);
    }

    @Override
    public void validateSizeGuide(String batchId) {
        // Validation logic will be handled by the ValidationJob
        Query query = Query.query(Criteria.where("sizeGuideId").regex("^" + batchId));
        List<SizeGuide> guides = mongoTemplate.find(query, SizeGuide.class);
        guides.forEach(guide -> guide.setValidationStatus(SizeGuide.ValidationStatus.PENDING));
        sizeGuideRepository.saveAll(guides);
    }

    @Override
    public String downloadTemplate() {
        return s3Service.downloadTemplate("templates/sizeguide_template.xlsx").toString();
    }

    @Override
    public List<SizeGuide> findByValidationStatus(SizeGuide.ValidationStatus status) {
        return sizeGuideRepository.findByValidationStatus(status);
    }

    @Override
    public List<SizeGuide> findBySizeGuideId(String sizeGuideId) {
        return sizeGuideRepository.findAllBySizeGuideId(sizeGuideId);
    }

    @Override
    public SizeGuide save(SizeGuide sizeGuide) {
        return sizeGuideRepository.save(sizeGuide);
    }
}
