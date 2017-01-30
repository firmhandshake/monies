package com.piotrglazar.wellpaidwork.model;

import rx.Observable;

public interface JobSource {

    String description();

    Observable<JobOffer> fetch();
}
