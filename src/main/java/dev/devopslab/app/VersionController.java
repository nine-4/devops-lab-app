package dev.devopslab.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class VersionController {

    private final String applicationName;
    private final String applicationVersion;
    private final String environment;

    public VersionController(
            @Value("${spring.application.name}") String applicationName,
            @Value("${app.version}") String applicationVersion,
            @Value("${app.environment}") String environment) {
        this.applicationName = applicationName;
        this.applicationVersion = applicationVersion;
        this.environment = environment;
    }

    @GetMapping("/version")
    public Map<String, String> getVersion() {
        return Map.of(
                "service", applicationName,
                "version", applicationVersion,
                "environment", environment
        );
    }
}
