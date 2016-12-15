package com.piotrglazar.wellpaidwork.model;

import com.piotrglazar.wellpaidwork.api.NoFluffJob;

import java.util.List;

public interface JobSource {

    String description();

    List<NoFluffJob> fetch();
}
