package com.prography.minari.common.util.uuid;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static com.prography.minari.common.util.uuid.UuidCreateUtil.createTossUUIDKeyLongToString;
import static org.junit.jupiter.api.Assertions.*;

class UuidCreateUtilTest {
    private static final String ALLOWED_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";

    @RepeatedTest(10)
    void sameInputShouldReturnSameOutput() {
        long value = 123456789L;
        String first = createTossUUIDKeyLongToString(value);
        String second = createTossUUIDKeyLongToString(value);
        System.out.println(first);
        assertEquals(first, second, "같은 값은 같은 문자열을 반환해야 합니다.");
    }

    @Test
    void differentInputShouldReturnDifferentOutput() {
        long value1 = 123456789L;
        long value2 = 987654321L;
        String first = createTossUUIDKeyLongToString(value1);
        String second = createTossUUIDKeyLongToString(value2);

        assertNotEquals(first, second, "다른 값은 다른 문자열을 반환해야 합니다.");
    }

    @Test
    void outputShouldBe64CharactersLong() {
        long value = 42L;
        String output = createTossUUIDKeyLongToString(value);

        assertEquals(64, output.length(), "출력 문자열 길이는 항상 64자여야 합니다.");
    }

    @Test
    void outputShouldContainOnlyAllowedCharacters() {
        long value = 123456789L;
        String output = createTossUUIDKeyLongToString(value);

        for (char c : output.toCharArray()) {
            assertTrue(ALLOWED_CHARS.indexOf(c) >= 0,
                    "허용되지 않은 문자가 포함되어 있습니다: " + c);
        }
    }
}