package com.stdApi.pacificOcean.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfiguration {
    private String secret;

    private  String ref;

    private long tokenValidity = 0;

    private long reftokenValidity = 0;

    public String getRef(){
        return ref;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public long getTokenValidity() {
        return tokenValidity;
    }

    public long getRefreshTokenValidity() {
        return reftokenValidity;
    }

    public void setTokenValidity(long tokenValidity) {
        this.tokenValidity = tokenValidity;
    }

    public void setRefreshTokenValidity(long reftokenValidity) {
        this.reftokenValidity = reftokenValidity;
    }
}
