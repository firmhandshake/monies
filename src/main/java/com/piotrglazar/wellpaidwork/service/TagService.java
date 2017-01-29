package com.piotrglazar.wellpaidwork.service;

import com.piotrglazar.wellpaidwork.model.CityTags;
import com.piotrglazar.wellpaidwork.model.TechnologyTags;
import com.piotrglazar.wellpaidwork.model.TitleTags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TagService {

    private final TitleTags title;
    private final TechnologyTags technology;
    private final CityTags city;

    @Autowired
    public TagService(TitleTags title, TechnologyTags technology, CityTags city) {
        this.title = title;
        this.technology = technology;
        this.city = city;
    }

    public Set<String> titleTags(String rawTag) {
        return title.tags(rawTag);
    }

    public Set<String> technologyTags(String rawTag) {
        return technology.tags(rawTag);
    }

    public String cityTag(String rawTag) {
        return city.cityTag(rawTag);
    }
}
