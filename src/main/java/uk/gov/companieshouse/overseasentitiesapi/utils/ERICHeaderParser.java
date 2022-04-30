package uk.gov.companieshouse.overseasentitiesapi.utils;

import org.springframework.stereotype.Component;

/**
 * Helper class to parse the authorised user information ERIC puts in the HTTP Request headers.
 */
@Component
public class ERICHeaderParser {

    private static final String DELIMITER = ";";
    private static final String EMAIL_IDENTIFIER = "@";


    public String getEmailAddress(String ericAuthorisedUser) {
        String email = null;
        if (ericAuthorisedUser != null) {
            String[] values = ericAuthorisedUser.split(DELIMITER);

            //email should be first value in the string
            String firstValue = values[0];

            if (firstValue.contains(EMAIL_IDENTIFIER)) {
                email = firstValue;
            }
        }

        return email;
    }
}