package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations.Cessation;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions.Addition;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.Change;

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
            "\"entityNumber\":\"OE123456\"," +
            "\"dueDiligence\":{\"dateChecked\":\"01-01-2001\",\"agentName\":\"Agent name\"," +
                "\"dueDiligenceCorrespondenceAddress\":{\"line_1\":\"Line 1\"}," +
                "\"agentAssuranceCode\":\"Agent assurance code\",\"amlRegistrationNumber\":\"Aml number\"," +
                "\"email\":\"emai@test.com\",\"supervisoryBody\":\"Supervisory body\",\"partnerName\":\"Partner name\"}," +
            "\"presenter\":{\"name\":\"John Doe\",\"email\":\"john@test.com\"}," +
            "\"filingForDate\":{\"year\":\"2000\",\"month\":\"01\",\"day\":\"01\"}," +
            "\"noChangesInFilingPeriodStatement\":true," +
            "\"anyBOsOrMOsAddedOrCeased\":true," +
            "\"beneficialOwnerStatement\":\"BO Statement\"," +
            "\"changes\":[{\"change\":\"Change 1\"},{\"change\":\"Change 2\"}]," +
            "\"additions\":[{\"change\":\"Addition 1\",\"type\":\"Type 1\"},{\"change\":\"Addition 2\",\"type\":\"Type 2\"}]," +
            "\"cessations\":[{\"change\":\"Cessation 1\"},{\"change\":\"Cessation 2\"}]}";
    
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
        updateSubmission.setEntityNumber("OE123456");
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
        updateSubmission.setChanges(Arrays.asList(
            new Change(){{ setChangeName("Change 1"); }},
            new Change(){{ setChangeName("Change 2");}}));
        updateSubmission.setAdditions(Arrays.asList(
            new Addition(){{ setChangeName("Addition 1"); setType("Type 1"); }},
            new Addition(){{ setChangeName("Addition 2"); setType("Type 2"); }}));
        updateSubmission.setCessations(Arrays.asList(
            new Cessation(){{ setChangeName("Cessation 1"); }},
            new Cessation(){{ setChangeName("Cessation 2");}}));
        updateSubmission.setBeneficialOwnerStatement("BO Statement");
        updateSubmission.setAnyBOsOrMOsAddedOrCeased(true);
        updateSubmission.setNoChangesInFilingPeriodStatement(true);

        return updateSubmission;
    }
}
