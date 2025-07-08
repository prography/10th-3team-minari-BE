package com.prography.minari.batch.item.processor;

import com.prography.minari.user.entity.User;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class UserProcessor implements ItemProcessor<User, Long> {
    @Override
    public Long process(User user) {
        return user.getId();
    }
} 