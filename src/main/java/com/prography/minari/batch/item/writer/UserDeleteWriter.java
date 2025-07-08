package com.prography.minari.batch.item.writer;

import com.prography.minari.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserDeleteWriter implements ItemWriter<Long> {
    private final UserService userService;

    @Override
    public void write(List<? extends Long> userIds) {
        for (Long userId : userIds) {
            userService.deleteAllUserData(userId);
        }
    }
} 