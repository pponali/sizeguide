package com.sizeguide.job;

import com.sizeguide.domain.SizeGuide;
import com.sizeguide.domain.SizeGuideDimension;
import com.sizeguide.service.EmailService;
import com.sizeguide.service.SizeGuideService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidationJob implements Job {

    private final SizeGuideService sizeGuideService;
    private final EmailService emailService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Starting validation job");
        try {
            List<SizeGuide> pendingGuides = sizeGuideService.findByValidationStatus(SizeGuide.ValidationStatus.PENDING);
            
            if (pendingGuides.isEmpty()) {
                log.info("No pending size guides found for validation");
                return;
            }

            for (SizeGuide sg : pendingGuides) {
                sg.setValidationStatus(SizeGuide.ValidationStatus.IN_PROGRESS);
                sizeGuideService.save(sg);
                
                log.info("Processing size guide: {}", sg.getSizeGuideId());
                ValidationErrors errors = validateSizeGuide(sg);
                
                if (errors.isEmpty()) {
                    sg.setValidationStatus(SizeGuide.ValidationStatus.VALIDATED);
                    sg.setValidationMessage("Validation successful");
                } else {
                    sg.setValidationStatus(SizeGuide.ValidationStatus.FAILED);
                    sg.setValidationMessage(formatValidationErrors(errors));
                }
                
                sizeGuideService.save(sg);
                emailService.sendValidationResultEmail(sg.getSizeGuideId(), errors);
            }
        } catch (Exception e) {
            log.error("Error in validation job", e);
            throw new JobExecutionException(e);
        }
    }

    private ValidationErrors validateSizeGuide(SizeGuide guide) {
        ValidationErrors errors = new ValidationErrors();
        
        // Check for duplicate size guide IDs
        if (isDuplicateSizeGuideId(guide)) {
            errors.addError("Duplicate size guide ID: " + guide.getSizeGuideId());
        }
        
        // Check for duplicate dimensions
        if (hasDuplicateDimensions(guide)) {
            errors.addError("Duplicate dimensions found in size guide: " + guide.getSizeGuideId());
        }
        
        // Check for empty dimensions
        if (hasEmptyDimensions(guide)) {
            errors.addError("Empty dimensions found");
        }
        
        // Check for unit consistency
        validateUnitConsistency(guide, errors);
        
        // Check for size value format
        validateSizeValueFormat(guide, errors);
        
        return errors;
    }

    private boolean isDuplicateSizeGuideId(SizeGuide guide) {
        List<SizeGuide> existing = sizeGuideService.findBySizeGuideId(guide.getSizeGuideId());
        return existing.size() > 1;
    }

    private boolean hasDuplicateDimensions(SizeGuide guide) {
        List<String> dimensions = guide.getDimensions().stream()
                .map(SizeGuideDimension::getDimension)
                .collect(Collectors.toList());
        return dimensions.size() != dimensions.stream().distinct().count();
    }

    private boolean hasEmptyDimensions(SizeGuide guide) {
        return guide.getDimensions().stream()
                .anyMatch(d -> d.getDimension() == null || d.getDimension().trim().isEmpty());
    }

    private void validateUnitConsistency(SizeGuide guide, ValidationErrors errors) {
        Map<String, List<SizeGuideDimension>> dimensionsByUnit = guide.getDimensions().stream()
                .collect(Collectors.groupingBy(SizeGuideDimension::getUnit));
        
        if (dimensionsByUnit.size() > 1) {
            errors.addError("Inconsistent units found: " + String.join(", ", dimensionsByUnit.keySet()));
        }
    }

    private void validateSizeValueFormat(SizeGuide guide, ValidationErrors errors) {
        for (SizeGuideDimension dim : guide.getDimensions()) {
            if (!isValidNumberFormat(dim.getValue())) {
                errors.addError("Invalid dimension value format: " + dim.getValue());
            }
        }
    }

    private boolean isValidNumberFormat(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String formatValidationErrors(ValidationErrors errors) {
        return String.join("\n", errors.getErrors());
    }

    private static class ValidationErrors {
        private final List<String> errors = new ArrayList<>();

        public void addError(String error) {
            errors.add(error);
        }

        public boolean isEmpty() {
            return errors.isEmpty();
        }

        public List<String> getErrors() {
            return errors;
        }
    }
}
