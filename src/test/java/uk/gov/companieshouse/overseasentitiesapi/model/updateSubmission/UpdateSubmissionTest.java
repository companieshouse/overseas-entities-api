package uk.gov.companieshouse.overseasentitiesapi.model.updateSubmission;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updateSubmission.changes.overseasEntity.OENameChange;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UpdateSubmissionTest {
    private final String EXPECTED_JSON_OUTPUT =
            "{\"type\":\"OE02\"," +
            "\"userSubmission\":" +
                "{\"entity_name\":null,\"entity_number\":\"OE123456\",\"presenter\":null,\"entity\":null," +
                "\"due_diligence\":null,\"overseas_entity_due_diligence\":null,\"beneficial_owners_statement\":null," +
                "\"beneficial_owners_individual\":null,\"beneficial_owners_corporate\":null," +
                "\"beneficial_owners_government_or_public_authority\":null,\"managing_officers_individual\":null," +
                "\"managing_officers_corporate\":null,\"update\":null,\"links\":null}," +
            "\"dueDiligence\":{\"dateChecked\":\"01-01-2001\",\"agentName\":\"Agent name\"," +
                "\"dueDiligenceCorrespondenceAddress\":{\"line_1\":\"Line 1\"}," +
                "\"agentAssuranceCode\":\"Agent assurance code\",\"amlRegistrationNumber\":\"Aml number\"," +
                "\"email\":\"emai@test.com\",\"supervisoryBody\":\"Supervisory body\",\"partnerName\":\"Partner name\"}," +
            "\"presenter\":{\"name\":\"John Doe\",\"email\":\"john@test.com\"}," +
            "\"filingForDate\":{\"year\":\"2000\",\"month\":\"01\",\"day\":\"01\"}," +
            "\"noChangesInFilingPeriodStatement\":\"No\"," +
            "\"anyBOsOrMOsAddedOrCeased\":\"Yes\"," +
            "\"beneficialOwnerStatement\":\"BO Statement\"," +
            "\"changes\":[{\"change\":\"changeOfEntityName\",\"proposedCorporateBodyName\":\"New Name\"}]," +
            "\"additions\":[{\"change\":\"changeOfEntityName\",\"proposedCorporateBodyName\":\"New Name\"}]," +
            "\"cessations\":[{\"change\":\"changeOfEntityName\",\"proposedCorporateBodyName\":\"New Name\"}]}";
    
    @Test
    void testJsonSerialisation() throws Exception {
        var updateSubmission = generateUpdateSubmission();
        ObjectMapper objectMapper = new ObjectMapper();

        var serialisedJson = objectMapper.writeValueAsString(updateSubmission);

        assertEquals(EXPECTED_JSON_OUTPUT, serialisedJson);
    }

    private UpdateSubmission generateUpdateSubmission(){
        UpdateSubmission updateSubmission = new UpdateSubmission();

        updateSubmission.setUserSubmission(new OverseasEntitySubmissionDto(){{
            setEntityNumber("OE123456");
        }});
        updateSubmission.setFilingForDate(new FilingForDate(){{
            setDay("01");
            setMonth("01");
            setYear("2000");
        }});
        updateSubmission.setPresenter(new Presenter(){{
            setName("John Doe");
            setEmail("john@test.com");
        }});
        updateSubmission.setDueDiligence(new DueDiligence(){{
            setDateChecked("01-01-2001");
            setDueDiligenceCorrespondenceAddress(new AddressDto(){{
                setLine1("Line 1");
            }});
            setAgentName("Agent name");
            setAgentAssuranceCode("Agent assurance code");
            setAmlRegistrationNumber("Aml number");
            setEmail("emai@test.com");
            setSupervisoryBody("Supervisory body");
            setPartnerName("Partner name");
        }});
        updateSubmission.setChanges(Arrays.asList(new OENameChange(){{ setProposedCorporateBodyName("New Name"); }}));
        //TODO: These will need to be changed to Addition/Cessation changes when added for consistency
        updateSubmission.setAdditions(Arrays.asList(new OENameChange(){{ setProposedCorporateBodyName("New Name"); }}));
        updateSubmission.setCessations(Arrays.asList(new OENameChange(){{ setProposedCorporateBodyName("New Name"); }}));
        updateSubmission.setBeneficialOwnerStatement("BO Statement");
        updateSubmission.setAnyBOsOrMOsAddedOrCeased("Yes");
        updateSubmission.setNoChangesInFilingPeriodStatement("No");

        return updateSubmission;
    }
}
