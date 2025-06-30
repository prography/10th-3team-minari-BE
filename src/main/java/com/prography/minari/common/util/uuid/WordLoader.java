package com.prography.minari.common.util.uuid;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WordLoader {

    public List<String> loadAdjectives() {
        return loadWords("words/adjectives.txt");
    }

    public List<String> loadNouns() {
        return loadWords("words/nouns.txt");
    }

    public List<String> loadWords(String path) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource(path).getInputStream()))) {
            return reader.lines().collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("단어 파일을 읽는 중 오류 발생: " + path, e);
        }
    }
}
