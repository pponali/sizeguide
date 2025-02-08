package com.sizeguide.mapper;

import com.sizeguide.domain.SizeGuide;
import com.sizeguide.domain.SizeGuideDimension;
import com.sizeguide.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

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

        // Group by unit
        Map<String, List<SizeGuideDimension>> byUnit = dimensions.stream()
                .collect(Collectors.groupingBy(SizeGuideDimension::getUnit));

        for (Map.Entry<String, List<SizeGuideDimension>> unitEntry : byUnit.entrySet()) {
            String unit = unitEntry.getKey();
            List<SizeGuideDimension> unitDimensions = unitEntry.getValue();

            UnitData unitData = new UnitData();
            unitData.setUnit(unit);
            unitData.setDisplaytext(unit.equals("Cms") ? "Cm" : "In");

            // Group by size
            Map<String, List<SizeGuideDimension>> bySize = unitDimensions.stream()
                    .collect(Collectors.groupingBy(SizeGuideDimension::getSize));

            List<SizeGuideData> sizeGuideList = bySize.entrySet().stream()
                    .map(sizeEntry -> {
                        SizeGuideData sizeGuideData = new SizeGuideData();
                        sizeGuideData.setDimensionSize(sizeEntry.getKey());

                        List<DimensionData> dimensionList = sizeEntry.getValue().stream()
                                .map(dim -> {
                                    DimensionData dimensionData = new DimensionData();
                                    dimensionData.setDimension(dim.getDimension());
                                    dimensionData.setDimensionValue(dim.getValue());
                                    return dimensionData;
                                })
                                .collect(Collectors.toList());

                        sizeGuideData.setDimensionList(dimensionList);
                        return sizeGuideData;
                    })
                    .collect(Collectors.toList());

            unitData.setSizeGuideList(sizeGuideList);
            unitList.add(unitData);
        }

        data.setUnitList(unitList);
        return data;
    }
}
