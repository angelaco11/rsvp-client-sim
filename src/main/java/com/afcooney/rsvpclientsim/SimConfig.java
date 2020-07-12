package com.afcooney.rsvpclientsim;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "sim")
@Validated
public class SimConfig {
    @NotNull
    private String host;

    @NotNull
    private String resourceFilePath;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @NotNull
    public String getResourceFilePath() {
        return resourceFilePath;
    }

    public void setResourceFilePath(@NotNull String resourceFilePath) {
        this.resourceFilePath = resourceFilePath;
    }
}
