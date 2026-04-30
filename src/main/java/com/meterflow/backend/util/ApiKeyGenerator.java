package com.meterflow.backend.util;

import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class ApiKeyGenerator {

    public String generate() {
        String part1 = UUID.randomUUID().toString().replace("-", "");
        String part2 = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        return "mf_" + part1 + part2;
    }
}