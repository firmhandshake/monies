package com.piotrglazar.wellpaidwork.util;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TagFileReader {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public Try<TagFile> readTagsFrom(String filePath) {
        return readRawTagsFrom(filePath).map(this::processRawTags);
    }

    private Try<List<String>> readRawTagsFrom(String filePath) {
        ClassPathResource resource = new ClassPathResource(filePath);
        return Try.of(resource::getFile).flatMap(f -> Try.of(() -> Files.readLines(f, Charsets.UTF_8)));
    }

    private TagFile processRawTags(List<String> lines) {
        Set<String> simpleTags = collectTags(lines);
        Map<String, String> synonyms = collectSynonyms(lines);

        return new TagFile(simpleTags, synonyms);
    }

    private Map<String, String> collectSynonyms(List<String> lines) {
        return lines.stream()
                .filter(StringUtils::isNotBlank)
                .filter(this::isNotComment)
                .filter(this::isSynonym)
                .map(this::lineToPair)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
    }

    private Set<String> collectTags(List<String> lines) {
        return lines.stream()
                .filter(StringUtils::isNotBlank)
                .filter(this::isNotComment)
                .filter(this::isTag)
                .collect(Collectors.toSet());
    }

    private boolean isNotComment(String line) {
        return !line.startsWith("#");
    }

    private boolean isSynonym(String line) {
        return line.contains("=>");
    }

    private boolean isTag(String line) {
        return !isSynonym(line);
    }

    private Optional<Pair<String, String>> lineToPair(String line) {
        List<String> parts = Stream.of(line.split("=>"))
                .map(String::trim)
                .collect(Collectors.toList());
        if (parts.size() < 2) {
            logger.error("Failed to convert line {} to pair", line);
            return Optional.empty();
        } else if (parts.size() == 2) {
            return Optional.of(Pair.of(parts.get(0), parts.get(1)));
        } else {
            logger.warn("Too many parts {} in line {}, will take first two", parts, line);
            return Optional.of(Pair.of(parts.get(0), parts.get(1)));
        }
    }
}
