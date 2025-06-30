package com.prography.minari.common.util.uuid;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class WordLoaderTest {

    @Test
    void loadAdjectives() {
        WordLoader loader = new WordLoader();
        List<String> adjectives = loader.loadWords("words/adjectives.txt");

        assertNotNull(adjectives);
        assertEquals(500, adjectives.size());
        assertTrue(adjectives.stream().allMatch(word -> word.length() == 3));
    }

    @Test
    void loadNouns() {
        WordLoader loader = new WordLoader();
        List<String> nouns = loader.loadWords("words/nouns.txt");

        assertNotNull(nouns);
        assertEquals(500, nouns.size());
        assertTrue(nouns.stream().allMatch(word -> word.length() == 3));
    }

}