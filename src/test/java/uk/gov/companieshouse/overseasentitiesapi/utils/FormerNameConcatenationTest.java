package uk.gov.companieshouse.overseasentitiesapi.utils;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.api.model.officers.FormerNamesApi;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.companieshouse.overseasentitiesapi.utils.FormerNameConcatenation.concatenateFormerNames;

class FormerNameConcatenationTest {
    @Test
    void testConcatenateFormerNamesMultipleNames() {
        List<FormerNamesApi> formerNamesApiList = List.of(
                new FormerNamesApi() {{
                    setForenames("John James");
                    setSurname("Doe");
                }},
                new FormerNamesApi() {{
                    setForenames("Jonathan");
                    setSurname("Doe");
                }}
        );

        var result = concatenateFormerNames(formerNamesApiList);

        assertEquals("John James Doe, Jonathan Doe", result);
    }

    @Test
    void testConcatenateFormerNamesNullInput() {
        var result = concatenateFormerNames(null);

        assertEquals(StringUtils.EMPTY, result);
    }

    @Test
    void testConcatenateFormerNamesEmptyListInput() {
        List<FormerNamesApi> formerNamesApiList = Collections.emptyList();

        var result = concatenateFormerNames(formerNamesApiList);

        assertEquals(StringUtils.EMPTY, result);
    }

    @Test
    void testConcatenateFormerNamesNoForenames() {
        List<FormerNamesApi> formerNamesApiList = List.of(
                new FormerNamesApi() {{
                    setSurname("Doe");
                }}
        );

        var result = concatenateFormerNames(formerNamesApiList);

        assertEquals("Doe", result);
    }

    @Test
    void testConcatenateFormerNamesNoSurname() {
        List<FormerNamesApi> formerNamesApiList = List.of(
                new FormerNamesApi() {{
                    setForenames("Jonathan");
                }}
        );

        var result = concatenateFormerNames(formerNamesApiList);

        assertEquals("Jonathan", result);
    }

    @Test
    void testConcatenateFormerNamesNoForenamesNorSurname() {
        List<FormerNamesApi> formerNamesApiList = List.of(
                new FormerNamesApi()
        );

        var result = concatenateFormerNames(formerNamesApiList);

        assertEquals(StringUtils.EMPTY, result);
    }
}
