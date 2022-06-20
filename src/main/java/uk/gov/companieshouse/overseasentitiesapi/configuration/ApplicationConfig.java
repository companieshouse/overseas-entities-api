package uk.gov.companieshouse.overseasentitiesapi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Supplier;

@Configuration
public class ApplicationConfig {
    @Bean
    public Supplier<LocalDateTime> dateTimeNow() {
        return LocalDateTime::now;
    }

    @Bean
    public Supplier<LocalDate> dateNow() {
        return LocalDate::now;
    }
}
