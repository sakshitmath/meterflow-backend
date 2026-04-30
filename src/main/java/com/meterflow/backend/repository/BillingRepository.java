package com.meterflow.backend.repository;

import com.meterflow.backend.enums.BillingStatus;
import com.meterflow.backend.model.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BillingRepository extends JpaRepository<Billing, UUID> {
    List<Billing> findByUserIdOrderByCreatedAtDesc(UUID userId);
    Optional<Billing> findByUserIdAndStatus(UUID userId, BillingStatus status);
}