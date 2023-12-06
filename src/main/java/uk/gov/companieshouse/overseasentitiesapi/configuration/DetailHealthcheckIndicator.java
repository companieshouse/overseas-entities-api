package uk.gov.companieshouse.overseasentitiesapi.configuration;

import org.springframework.boot.actuate.autoconfigure.health.HealthEndpointProperties;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.boot.actuate.health.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
//@WebEndpoint(id = "detail-healthcheck")
public class DetailHealthcheckIndicator implements HealthIndicator {
    @Override
    public Health health() {
        double chance = ThreadLocalRandom.current().nextDouble();
        Health.Builder status = Health.up();

        if (chance > 0.9) {
            status = Health.down();
        }
        return status
                .withDetail("chance", chance)
                .withDetail("strategy", "thread-local")
                .build();
    }
}
