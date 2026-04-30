package com.meterflow.backend.model;

import com.meterflow.backend.enums.BillingStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "billing")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime billingPeriodStart;

    private LocalDateTime billingPeriodEnd;

    private Long totalRequests;

    private BigDecimal amountDue;

    @Enumerated(EnumType.STRING)
    private BillingStatus status;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) status = BillingStatus.PENDING;
        if (totalRequests == null) totalRequests = 0L;
        if (amountDue == null) amountDue = BigDecimal.ZERO;
    }
}