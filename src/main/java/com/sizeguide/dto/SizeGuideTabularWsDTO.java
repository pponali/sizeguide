package com.sizeguide.dto;

import lombok.Data;

@Data
public class SizeGuideTabularWsDTO {
    private String type = "sizeGuideTabularWsDTO";
    private String status;
    private boolean garmentSizePresent;
    private Integer imagePosition;
    private String imageURL;
    private SizeGuideTabularWsData sizeGuideTabularWsData;
}
