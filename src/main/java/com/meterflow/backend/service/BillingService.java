package com.meterflow.backend.service;

import com.meterflow.backend.dto.response.BillingResponse;
import com.meterflow.backend.enums.BillingStatus;
import com.meterflow.backend.model.Billing;
import com.meterflow.backend.model.User;
import com.meterflow.backend.repository.ApiKeyRepository;
import com.meterflow.backend.repository.BillingRepository;
import com.meterflow.backend.repository.UsageLogRepository;
import com.meterflow.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BillingService {

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Autowired
    private UsageLogRepository usageLogRepository;

    public BillingResponse calculateAndSaveBill(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<UUID> keyIds = apiKeyRepository.findByUserId(user.getId())
                .stream().map(k -> k.getId()).collect(Collectors.toList());

        long totalRequests = keyIds.isEmpty() ? 0 :
                usageLogRepository.countByApiKeyIdIn(keyIds);

        BigDecimal amount = BigDecimal.ZERO;
        if (totalRequests > 1000) {
            long billable = totalRequests - 1000;
            long units = (long) Math.ceil(billable / 100.0);
            amount = BigDecimal.valueOf(0.50).multiply(BigDecimal.valueOf(units));
        }

        Billing billing = Billing.builder()
                .user(user)
                .totalRequests(totalRequests)
                .amountDue(amount)
                .billingPeriodStart(LocalDateTime.now().withDayOfMonth(1))
                .billingPeriodEnd(LocalDateTime.now())
                .status(BillingStatus.PENDING)
                .build();

        Billing saved = billingRepository.save(billing);
        return mapToResponse(saved);
    }

    public List<BillingResponse> getBillingHistory(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return billingRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public BillingResponse getCurrentBill(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return billingRepository
                .findByUserIdAndStatus(user.getId(), BillingStatus.PENDING)
                .map(this::mapToResponse)
                .orElse(new BillingResponse(null, 0L,
                        BigDecimal.ZERO, "NO_BILL",
                        null, null, null));
    }

    private BillingResponse mapToResponse(Billing b) {
        return new BillingResponse(
                b.getId(),
                b.getTotalRequests(),
                b.getAmountDue(),
                b.getStatus().name(),
                b.getBillingPeriodStart(),
                b.getBillingPeriodEnd(),
                b.getCreatedAt()
        );
    }
}