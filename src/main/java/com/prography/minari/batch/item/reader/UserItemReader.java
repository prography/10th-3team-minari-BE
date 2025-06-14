package com.prography.minari.batch.item.reader;

import com.prography.minari.user.entity.User;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class UserItemReader {
    @Bean
    public JpaPagingItemReader<User> reader(EntityManagerFactory emf) {
        return new JpaPagingItemReaderBuilder<User>()
                .name("userItemReader")                     // 리더 이름
                .entityManagerFactory(emf)                  // EntityManagerFactory (필수)
                .queryString("SELECT u FROM User u")        // JPQL
                .pageSize(100)                              // chunk 크기와 일치시킴
                .build();
    }
}
