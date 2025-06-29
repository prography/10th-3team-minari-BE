package com.prography.minari.common.util;

import com.prography.minari.common.util.uuid.WordLoader;
import com.prography.minari.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UuidUtil {

    private final WordLoader wordLoader;
    private final UserRepository userRepository;
    private final SecureRandom random = new SecureRandom();

    private String generate() {
        List<String> adjectives = wordLoader.loadAdjectives();
        List<String> nouns = wordLoader.loadNouns();

        return adjectives.get(random.nextInt(adjectives.size()))     // 형용사
                + nouns.get(random.nextInt(nouns.size()))            // 명사
                + String.format("%02d", random.nextInt(100)); //
    }

    public String generateUniqueUuid() {
        String uuid;
        do {
            uuid = generate();
        } while (userRepository.existsByUuid(uuid));
        return uuid;
    }

}
