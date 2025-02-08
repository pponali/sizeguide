package com.sizeguide.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "size_guides")
public class SizeGuide {
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String sizeGuideId;
    
    private String categoryCode;
    private String brand;
    private String fitType;
    private String imageUrl;
    private Integer imagePosition;
    private String garmentSize;
    private List<SizeGuideDimension> dimensions;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long catalogVersion;
    private String productSource;
    private ValidationStatus validationStatus;
    private String validationMessage;
    
    public enum ValidationStatus {
        PENDING,
        IN_PROGRESS,
        VALIDATED,
        FAILED
    }
}
