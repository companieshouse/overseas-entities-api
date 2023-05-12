package uk.gov.companieshouse.overseasentitiesapi.service.changelist;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.update.OverseasEntityDataApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityNameDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.EntityNameChange;
import uk.gov.companieshouse.overseasentitiesapi.service.CostsService;
import uk.gov.companieshouse.overseasentitiesapi.validation.OverseasEntityChangeValidator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class OverseasEntityChangeServiceTest {
    @Mock
    OverseasEntityChangeValidator overseasEntityChangeValidator;

    OverseasEntityChangeService overseasEntityChangeService;

    @BeforeEach
    void init() {
        overseasEntityChangeService = new OverseasEntityChangeService(overseasEntityChangeValidator);
    }

//    @Test
//    void testCollateOverseasEntityChangesOnlyEntityNameChangeReturnsChangeList() {
//        Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration = new ImmutablePair<>
//                (new CompanyProfileApi() {{
//                    setCompanyName("Existing Name");
//                }},
//                        new OverseasEntityDataApi());
//        OverseasEntitySubmissionDto updateSubmission =
//                new OverseasEntitySubmissionDto(){{ setEntityName(new EntityNameDto(){{setName("New name");}}); }};
//        when(overseasEntityChangeValidator.verifyEntityNameChange(existingRegistration.getLeft().getCompanyName(), updateSubmission.getEntityName().getName())).thenReturn(new EntityNameChange("Existing Name", "New name"));
//        when(overseasEntityChangeValidator.verifyPrincipalAddressChange(any(), any())).thenReturn(null);
////        when(overseasEntityChangeValidator.verifyCorrespondenceAddressChange(any(), any())).thenReturn(null);
////        when(overseasEntityChangeValidator.verifyCompanyIdentificationChange(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(null);
////        when(overseasEntityChangeValidator.verifyEntityEmailAddressChange(any(), any())).thenReturn(null);
//
//        var result = overseasEntityChangeService.collateOverseasEntityChanges(existingRegistration, updateSubmission);
//    }
}
