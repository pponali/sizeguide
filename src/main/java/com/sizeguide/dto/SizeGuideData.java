package com.sizeguide.dto;

import lombok.Data;
import java.util.List;

@Data
public class SizeGuideData {
    private String dimensionSize;
    private List<DimensionData> dimensionList;
}
