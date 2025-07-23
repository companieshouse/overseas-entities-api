package uk.gov.companieshouse.overseasentitiesapi.utils;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.TRUNCATED_DATA_LENGTH;

class DataSanitisationTest {

    @Test
    void testMakeStringSafeForLogging() {
        DataSanitisation dataSanitisation = new DataSanitisation();
        String sanitisedInput = dataSanitisation.makeStringSafeForLogging("abc\t\nabc");
        assertEquals("abc\\t\\nabc", sanitisedInput);
    }

    @Test
    void testMakeStringSafeForLoggingWithTruncation() {
        DataSanitisation dataSanitisation = new DataSanitisation();
        String sanitisedInput = dataSanitisation.makeStringSafeForLogging("abc\t\nabc" + StringUtils.repeat("A", TRUNCATED_DATA_LENGTH));
        String sanitised = "abc\\t\\nabc";
        String expected = sanitised + (StringUtils.repeat("A", TRUNCATED_DATA_LENGTH - sanitised.length()));
        assertEquals(expected, sanitisedInput);
    }
}
