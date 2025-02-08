package com.sizeguide.mapper;

import com.sizeguide.domain.SizeGuide;
import com.sizeguide.domain.SizeGuideDimension;
import com.sizeguide.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface SizeGuideMapper {
    @Mapping(target = "type", constant = "sizeGuideTabularWsDTO")
    @Mapping(target = "status", expression = "java(sizeGuide.getValidationStatus().name())")
    @Mapping(target = "imageURL", source = "imageUrl")
    @Mapping(target = "sizeGuideTabularWsData", source = "dimensions", qualifiedByName = "dimensionsToData")
    @Mapping(target = "garmentSizePresent", expression = "java(sizeGuide.getGarmentSize() != null)")
    SizeGuideTabularWsDTO toDto(SizeGuide sizeGuide);

    @Named("dimensionsToData")
    default SizeGuideTabularWsData dimensionsToData(List<SizeGuideDimension> dimensions) {
        if (dimensions == null || dimensions.isEmpty()) {
            return null;
        }

        SizeGuideTabularWsData data = new SizeGuideTabularWsData();
        List<UnitData> unitList = new ArrayList<>();

        // Create CM unit data
        UnitData cmData = new UnitData();
        cmData.setUnit("Cm");
        cmData.setDisplaytext("Cm");

        // Create Inch unit data
        UnitData inData = new UnitData();
        inData.setUnit("In");
        inData.setDisplaytext("In");

        // Group by size
        Map<String, List<SizeGuideDimension>> bySize = dimensions.stream()
                .collect(Collectors.groupingBy(SizeGuideDimension::getSize));

        // Process dimensions for both units
        List<SizeGuideData> cmSizeGuideList = new ArrayList<>();
        List<SizeGuideData> inSizeGuideList = new ArrayList<>();

        for (Map.Entry<String, List<SizeGuideDimension>> sizeEntry : bySize.entrySet()) {
            SizeGuideData cmSizeGuide = new SizeGuideData();
            SizeGuideData inSizeGuide = new SizeGuideData();
            
            cmSizeGuide.setDimensionSize(sizeEntry.getKey());
            inSizeGuide.setDimensionSize(sizeEntry.getKey());

            List<DimensionData> cmDimensionList = new ArrayList<>();
            List<DimensionData> inDimensionList = new ArrayList<>();

            for (SizeGuideDimension dim : sizeEntry.getValue()) {
                // CM dimension
                DimensionData cmDim = new DimensionData();
                cmDim.setDimension(dim.getDimension());
                cmDim.setDimensionValue(dim.getValue());
                cmDimensionList.add(cmDim);

                // Inch dimension
                DimensionData inDim = new DimensionData();
                inDim.setDimension(dim.getDimension());
                // Convert CM to Inches (divide by 2.54)
                double inches = Double.parseDouble(dim.getValue()) / 2.54;
                inDim.setDimensionValue(String.valueOf(Math.round(inches)));
                inDimensionList.add(inDim);
            }

            cmSizeGuide.setDimensionList(cmDimensionList);
            inSizeGuide.setDimensionList(inDimensionList);

            cmSizeGuideList.add(cmSizeGuide);
            inSizeGuideList.add(inSizeGuide);
        }

        cmData.setSizeGuideList(cmSizeGuideList);
        inData.setSizeGuideList(inSizeGuideList);

        unitList.add(cmData);
        unitList.add(inData);
        data.setUnitList(unitList);
        
        return data;
    }
}
