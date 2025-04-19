package com.semiramide.timetracker.gateway.properties;

import lombok.Builder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@Builder
@ConfigurationProperties(prefix = "op.gateway")
public class GatewayProperties {
    private String backendHost;
}
