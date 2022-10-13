package uk.gov.companieshouse.overseasentitiesapi.utils;

import org.owasp.encoder.Encode;
import org.springframework.stereotype.Component;

import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.TRUNCATED_DATA_LENGTH;

@Component
public class DataSanitisation {

    public String makeStringSafeForLogging(String input) {
        String sanitisedInput = Encode.forJava(input);
        if (sanitisedInput.length() > TRUNCATED_DATA_LENGTH) {
            sanitisedInput = sanitisedInput.substring(0, TRUNCATED_DATA_LENGTH);
        }
        return sanitisedInput;
    }
}
