package org.martini.backend.repository;

import org.martini.backend.model.dao.EmailQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmailQueueRepository extends JpaRepository<EmailQueue, Long> {
    List<EmailQueue> findAllBySentFalseAndFailedFalseAndNextAttemptAtBefore(LocalDateTime now);
}
