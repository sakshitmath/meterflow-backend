package com.meterflow.backend.repository;

import com.meterflow.backend.enums.KeyStatus;
import com.meterflow.backend.model.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, UUID> {
    List<ApiKey> findByUserId(UUID userId);
    List<ApiKey> findByApiId(UUID apiId);
    Optional<ApiKey> findByKeyValueAndStatus(String keyValue, KeyStatus status);
}