package uk.gov.companieshouse.overseasentitiesapi.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.DueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntityDueDiligenceDto;

import static org.junit.jupiter.api.Assertions.assertFalse;

class DtoIgnoreFieldsTest {

    @Test
    void testDueDiligenceDoesNotContainFieldsThatShouldBeIgnored() throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(new DueDiligenceDto());

        assertFalse(json.contains("\"empty\""), "Json should not contain an \"empty\" field");
    }

    @Test
    void testOverseasEntityDueDiligenceDoesNotContainFieldsThatShouldBeIgnored() throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(new OverseasEntityDueDiligenceDto());

        assertFalse(json.contains("\"empty\""), "Json should not contain an \"empty\" field");
    }
}
