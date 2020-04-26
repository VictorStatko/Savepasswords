package com.statkovit.authorizationservice.repositories;

import com.statkovit.authorizationservice.entities.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {

    void deleteAllByIdIsLessThanEqual(Long id);
}
