package uk.gov.companieshouse.overseasentitiesapi.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.utils.TestUtils;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DtoModelChangeTest {

    /**
     * If this test fails it's likely due to a DTO model structure change which is an indication of potential issues
     * for clients that still need to be able to work with Mongo records created using an older version of the data model.
     *
     * If the data model structure has been modified, consideration will need to be given to how the web client and other
     * clients of the OE API end-points will deal with older versions of the model structure and ultimately how older
     * versions of the model structure can be loaded into the DTO model.
     *
     * Only when those things have been considered and addressed should the JSON model structure in the
     * 'overseas_entity_valid.json' file be updated to reflect the new DTO structure, in order that this test passes again.
     *
     * @throws Exception If DTO model structure incompatibility detected
     */
    @Test
    void testCanGenerateDtoModelFromValidJson() throws Exception {

        TestUtils testUtils = new TestUtils();
        File file = testUtils.getFile("overseas_entity_valid.json");
        String jsonData = testUtils.readString(file);

        OverseasEntitySubmissionDto dto = getObjectMapper().readValue(jsonData, OverseasEntitySubmissionDto.class);

        assertNotNull(dto);
    }

    private ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        return mapper;
    }
}
