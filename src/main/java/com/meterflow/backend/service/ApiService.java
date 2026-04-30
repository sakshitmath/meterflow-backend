package com.meterflow.backend.service;

import com.meterflow.backend.dto.request.CreateApiRequest;
import com.meterflow.backend.dto.response.ApiKeyResponse;
import com.meterflow.backend.dto.response.ApiResponse;
import com.meterflow.backend.model.Api;
import com.meterflow.backend.model.ApiKey;
import com.meterflow.backend.model.User;
import com.meterflow.backend.repository.ApiKeyRepository;
import com.meterflow.backend.repository.ApiRepository;
import com.meterflow.backend.repository.UserRepository;
import com.meterflow.backend.util.ApiKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ApiService {

    @Autowired
    private ApiRepository apiRepository;

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApiKeyGenerator apiKeyGenerator;

    public ApiResponse createApi(String email, CreateApiRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Api api = Api.builder()
                .user(user)
                .name(request.getName())
                .baseUrl(request.getBaseUrl())
                .description(request.getDescription())
                .build();

        Api saved = apiRepository.save(api);
        return mapToApiResponse(saved);
    }

    public List<ApiResponse> getUserApis(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return apiRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToApiResponse)
                .collect(Collectors.toList());
    }

    public void deleteApi(UUID apiId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Api api = apiRepository.findByIdAndUserId(apiId, user.getId())
                .orElseThrow(() -> new RuntimeException("API not found"));

        apiRepository.delete(api);
    }

    public ApiKeyResponse generateApiKey(UUID apiId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Api api = apiRepository.findByIdAndUserId(apiId, user.getId())
                .orElseThrow(() -> new RuntimeException("API not found"));

        ApiKey apiKey = ApiKey.builder()
                .api(api)
                .user(user)
                .keyValue(apiKeyGenerator.generate())
                .build();

        ApiKey saved = apiKeyRepository.save(apiKey);
        return mapToApiKeyResponse(saved);
    }

    public List<ApiKeyResponse> getUserApiKeys(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return apiKeyRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToApiKeyResponse)
                .collect(Collectors.toList());
    }

    public void revokeApiKey(UUID keyId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ApiKey key = apiKeyRepository.findById(keyId)
                .orElseThrow(() -> new RuntimeException("Key not found"));

        if (!key.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        key.setStatus(com.meterflow.backend.enums.KeyStatus.REVOKED);
        apiKeyRepository.save(key);
    }

    private ApiResponse mapToApiResponse(Api api) {
        return new ApiResponse(
                api.getId(),
                api.getName(),
                api.getBaseUrl(),
                api.getDescription(),
                api.getIsActive(),
                api.getCreatedAt()
        );
    }

    private ApiKeyResponse mapToApiKeyResponse(ApiKey key) {
        return new ApiKeyResponse(
                key.getId(),
                key.getKeyValue(),
                key.getStatus().name(),
                key.getApi().getName(),
                key.getCreatedAt()
        );
    }
}