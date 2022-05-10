package uk.gov.companieshouse.overseasentitiesapi.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.filinggenerator.FilingApi;
import uk.gov.companieshouse.overseasentitiesapi.client.ApiClientService;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotFoundException;
import uk.gov.companieshouse.overseasentitiesapi.mocks.Mocks;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerGovernmentOrPublicAuthorityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.FILING_KIND_OVERSEAS_ENTITY;

@ExtendWith(MockitoExtension.class)
class FilingServiceTest {

    private static final String OVERSEAS_ENTITY_ID = "abc123";
    public static final String FILING_DESCRIPTION = "Test this";

    @InjectMocks
    private FilingsService filingsService;

    @Mock
    private OverseasEntitiesService overseasEntitiesService;

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ApiClient apiClient;

    @Test
    void testFilingGenerationWhenSuccessful() throws SubmissionNotFoundException, ServiceException {
        ReflectionTestUtils.setField(filingsService, "filingDescription", FILING_DESCRIPTION);
                OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDto();
        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.of(overseasEntitySubmissionDto);
        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);
        FilingApi filing = filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID);
        assertEquals(FILING_KIND_OVERSEAS_ENTITY, filing.getKind());
        assertEquals(FILING_DESCRIPTION, filing.getDescriptionIdentifier());
        final EntityDto entityInFiling = ((EntityDto) filing.getData().get("entity"));
        assertEquals("Joe Bloggs Ltd", entityInFiling.getName());
        assertEquals("Eutopia", entityInFiling.getIncorporationCountry());
        final List<BeneficialOwnerIndividualDto> beneficialOwnersIndividualInFiling = ((List<BeneficialOwnerIndividualDto>) filing.getData().get(BENEFICIAL_OWNERS_INDIVIDUAL_FIELD));
        assertEquals(1, beneficialOwnersIndividualInFiling.size());
        final BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = beneficialOwnersIndividualInFiling.get(0);
        assertEquals("Jack", beneficialOwnerIndividualDto.getFirstName());
        assertEquals("Jones", beneficialOwnerIndividualDto.getLastName());
        assertEquals("Welsh", beneficialOwnerIndividualDto.getNationality());
        List<BeneficialOwnerGovernmentOrPublicAuthorityDto> beneficialOwnerGovernmentOrPublicAuthorityInFiling = ((List<BeneficialOwnerGovernmentOrPublicAuthorityDto>) filing.getData().get(BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD));
        assertEquals(1, beneficialOwnerGovernmentOrPublicAuthorityInFiling.size());
        final BeneficialOwnerGovernmentOrPublicAuthorityDto  beneficialOwnerGovernmentOrPublicAuthorityDto = beneficialOwnerGovernmentOrPublicAuthorityInFiling.get(0);
        assertEquals("The Government", beneficialOwnerGovernmentOrPublicAuthorityDto.getName());
        assertEquals("Legal form", beneficialOwnerGovernmentOrPublicAuthorityDto.getLegalForm());
        assertEquals("The Law", beneficialOwnerGovernmentOrPublicAuthorityDto.getLawGoverned());
    }

    @Test
    void testFilingGenerationWhenThrowsExceptionForNoSubmission()  {
        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.empty();
                when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);
        assertThrows(SubmissionNotFoundException.class, () -> filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID));
    }
}
