package uk.gov.companieshouse.overseasentitiesapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OverseasEntitiesApiApplication {

    public static final String APP_NAMESPACE = "overseas-entities-api";

    public static void main(String[] args) {
        SpringApplication.run(OverseasEntitiesApiApplication.class, args);
    }
}
