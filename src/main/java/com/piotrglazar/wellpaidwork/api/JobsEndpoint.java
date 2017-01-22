package com.piotrglazar.wellpaidwork.api;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.piotrglazar.wellpaidwork.model.JobResults;
import com.piotrglazar.wellpaidwork.model.OptionalSerializer;
import com.piotrglazar.wellpaidwork.service.Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

@RestController
public class JobsEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Engine engine;

    @Autowired
    public JobsEndpoint(Engine engine) {
        this.engine = engine;
    }

    @RequestMapping("/jobs")
    public JobResults fetchJobs() {
        logger.info("About to fetch jobs");
        return engine.fetchJobs();
    }

    @RequestMapping("/serializerTest")
    public Tester testSerializer() {
        return new Tester();
    }

    static class Tester {
        private String name = "Piotr";
        private Optional<String> nick = Optional.empty();
        private Optional<String> surname = Optional.of("Glazar");

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @JsonSerialize(using = OptionalSerializer.class)
        public Optional<String> getNick() {
            return nick;
        }

        public void setNick(Optional<String> nick) {
            this.nick = nick;
        }

        @JsonSerialize(using = OptionalSerializer.class)
        public Optional<String> getSurname() {
            return surname;
        }

        public void setSurname(Optional<String> surname) {
            this.surname = surname;
        }
    }
}
