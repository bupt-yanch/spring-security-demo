package org.example.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@ConfigurationProperties(prefix="secure.ignore")
public class WhiteListUrlsConfig {
    private List<String> urls;
}
