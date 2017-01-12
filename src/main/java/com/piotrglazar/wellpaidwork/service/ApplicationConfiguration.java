package com.piotrglazar.wellpaidwork.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.piotrglazar.wellpaidwork.model.TechnologyTags;
import com.piotrglazar.wellpaidwork.model.TitleTags;
import com.piotrglazar.wellpaidwork.util.TagFile;
import com.piotrglazar.wellpaidwork.util.TagFileReader;
import com.piotrglazar.wellpaidwork.util.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;
import java.util.function.Function;

@Configuration
public class ApplicationConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String TECHNOLOGY_TAGS_FILE = "technologyTags.txt";
    private static final String TITLE_TAGS_FILE = "titleTags.txt";

    @Autowired
    private TagFileReader tagFileReader;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JodaModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }

    @Bean
    public TechnologyTags technologyTags() {
        return fromTagFile(TECHNOLOGY_TAGS_FILE, tf -> new TechnologyTags(tf.getTags(), tf.getSynonyms()));
    }

    @Bean
    public TitleTags titleTags() {
        return fromTagFile(TITLE_TAGS_FILE, tf -> new TitleTags(tf.getTags(), tf.getSynonyms()));
    }

    private <T> T fromTagFile(String file, Function<TagFile, T> factory) {
        Try<TagFile> tagFileTry = tagFileReader.readTagsFrom(file);

        if (tagFileTry.isSuccess()) {
            return factory.apply(tagFileTry.get());
        } else {
            logger.error("Failed to read tags from " + file);
            throw new IllegalStateException("Failed to read tags from " + file, tagFileTry.getException());
        }
    }
}
