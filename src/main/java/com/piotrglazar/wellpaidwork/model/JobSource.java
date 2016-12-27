package com.piotrglazar.wellpaidwork.model;

import java.util.List;

public interface JobSource {

    String description();

    List<JobOffer> fetch();
}
