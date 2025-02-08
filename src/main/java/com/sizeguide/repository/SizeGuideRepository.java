package com.sizeguide.repository;

import com.sizeguide.domain.SizeGuide;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SizeGuideRepository extends MongoRepository<SizeGuide, String> {
    Optional<SizeGuide> findBySizeGuideId(String sizeGuideId);
    List<SizeGuide> findAllBySizeGuideId(String sizeGuideId);
    List<SizeGuide> findByValidationStatus(SizeGuide.ValidationStatus status);
}
