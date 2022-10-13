package uk.gov.companieshouse.overseasentitiesapi.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataSanitisationTest {

    @Test
    void testMakeStringSafeForLogging() {
        DataSanitisation dataSanitisation = new DataSanitisation();
        String sanitisedInput = dataSanitisation.makeStringSafeForLogging("abc\t\nabc");
        assertEquals("abc\\t\\nabc", sanitisedInput);
    }
}
