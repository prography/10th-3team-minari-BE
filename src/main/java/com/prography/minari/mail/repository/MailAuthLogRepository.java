package com.prography.minari.mail.repository;

import com.prography.minari.mail.entity.MailAuthLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

public interface MailAuthLogRepository extends JpaRepository<MailAuthLog, Long> {
    // 필요시 커스텀 쿼리 메소드 추가
    Optional<MailAuthLog> findFirstByUserIdOrderByCreatedDateTimeDesc(Long userId);

    @Transactional
    void deleteByUserId(Long userId);
} 