package com.statkovit.userservice.repository;

import com.statkovit.userservice.domain.OutboxEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {

    Page<OutboxEvent> findAllByIdIsGreaterThan(Long id, Pageable pageable);

    void deleteAllByIdIsLessThanEqual(Long id);
}
