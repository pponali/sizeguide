package com.sizeguide.dto;

import lombok.Data;
import java.util.List;

@Data
public class UnitData {
    private String displaytext;
    private String unit;
    private List<SizeGuideData> sizeGuideList;
}
