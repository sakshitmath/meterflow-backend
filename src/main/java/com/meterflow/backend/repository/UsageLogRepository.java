package com.meterflow.backend.repository;

import com.meterflow.backend.model.UsageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface UsageLogRepository extends JpaRepository<UsageLog, UUID> {
    List<UsageLog> findByApiKeyIdOrderByTimestampDesc(UUID apiKeyId);
    long countByApiKeyIdIn(List<UUID> apiKeyIds);
    List<UsageLog> findByApiKeyIdInOrderByTimestampDesc(List<UUID> apiKeyIds);
}