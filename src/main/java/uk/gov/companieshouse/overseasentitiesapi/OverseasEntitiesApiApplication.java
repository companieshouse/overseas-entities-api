package uk.gov.companieshouse.overseasentitiesapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class OverseasEntitiesApiApplication {

    public static final String APP_NAMESPACE = "overseas-entities-api";

    public static void main(String[] args) {
        SpringApplication.run(OverseasEntitiesApiApplication.class, args);
    }

    @PostConstruct
    public void init() {
        // This is to prevent times being out of time by an hour during British Summer Time in MongoDB
        // MongoDB stores UTC datetime, and LocalDate doesn't contain timezone
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
