package com.meterflow.backend.repository;

import com.meterflow.backend.model.Api;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApiRepository extends JpaRepository<Api, UUID> {
    List<Api> findByUserId(UUID userId);
    Optional<Api> findByIdAndUserId(UUID id, UUID userId);
}