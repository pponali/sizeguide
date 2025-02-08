package com.sizeguide.datamigration.batch;

import com.sizeguide.domain.SizeGuide;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class SizeGuideProcessor implements ItemProcessor<SizeGuide, SizeGuide> {

    @Override
    public SizeGuide process(SizeGuide sizeGuide) {
        log.debug("Processing size guide with ID: {}", sizeGuide.getSizeGuideId());
        
        if (sizeGuide.getValidationStatus() == null) {
            log.debug("Setting default validation status for size guide: {}", sizeGuide.getSizeGuideId());
            sizeGuide.setValidationStatus(SizeGuide.ValidationStatus.SUCCESS);
        }
        
        log.debug("Completed processing size guide: {}", sizeGuide.getSizeGuideId());
        return sizeGuide;
    }
}
