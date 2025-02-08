package com.sizeguide.mapper;

import com.sizeguide.domain.SizeGuide;
import com.sizeguide.dto.SizeGuideTabularWsDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-08T15:02:09+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.41.0.z20250115-2156, environment: Java 21.0.5 (Eclipse Adoptium)"
)
@Component
public class SizeGuideMapperImpl implements SizeGuideMapper {

    @Override
    public SizeGuideTabularWsDTO toDto(SizeGuide sizeGuide) {
        if ( sizeGuide == null ) {
            return null;
        }

        SizeGuideTabularWsDTO sizeGuideTabularWsDTO = new SizeGuideTabularWsDTO();

        sizeGuideTabularWsDTO.setImageURL( sizeGuide.getImageUrl() );
        sizeGuideTabularWsDTO.setSizeGuideTabularWsData( dimensionsToData( sizeGuide.getDimensions() ) );
        sizeGuideTabularWsDTO.setImagePosition( sizeGuide.getImagePosition() );

        sizeGuideTabularWsDTO.setType( "sizeGuideTabularWsDTO" );
        sizeGuideTabularWsDTO.setStatus( sizeGuide.getValidationStatus().name() );
        sizeGuideTabularWsDTO.setGarmentSizePresent( sizeGuide.getGarmentSize() != null );

        return sizeGuideTabularWsDTO;
    }
}
