package com.piotrglazar.wellpaidwork.model.dao;

import com.piotrglazar.wellpaidwork.model.JobOffer;
import com.piotrglazar.wellpaidwork.model.db.JobOfferEntity;
import com.piotrglazar.wellpaidwork.model.db.JobOfferRepository;
import com.piotrglazar.wellpaidwork.model.db.JobOfferSource;
import com.piotrglazar.wellpaidwork.util.JobConverter;
import com.piotrglazar.wellpaidwork.util.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JobOfferDao {

    private final JobOfferRepository repository;
    private final JobConverter jobConverter;

    @Autowired
    public JobOfferDao(JobOfferRepository repository, JobConverter jobConverter) {
        this.repository = repository;
        this.jobConverter = jobConverter;
    }


    public Optional<JobOfferEntity> findRaw(String externalId, JobOfferSource source) {
        return repository.findByExternalIdAndSource(externalId, source);
    }

    public Try<Long> save(JobOffer jobOffer) {
        return Try.of(() -> jobConverter.toDb(jobOffer))
                .flatMap(e -> Try.of(() -> repository.save(e)))
                .map(JobOfferEntity::getId);
    }

    public Optional<JobOffer> find(String externalId, JobOfferSource source) {
        return findRaw(externalId, source).map(jobConverter::fromDb);
    }

    public List<JobOffer> all() {
        return repository.findAll()
                .stream()
                .map(jobConverter::fromDb)
                .collect(Collectors.toList());
    }
}
