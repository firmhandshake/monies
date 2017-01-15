package com.piotrglazar.wellpaidwork.model.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobOfferRepository extends JpaRepository<JobOfferEntity, Long> {

    Optional<JobOfferEntity> findByExternalIdAndSource(String externalId, JobOfferSource source);
}
