package uk.gov.companieshouse.overseasentitiesapi.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.companieshouse.api.model.filinggenerator.FilingApi;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotFoundException;
import uk.gov.companieshouse.overseasentitiesapi.mocks.Mocks;
import uk.gov.companieshouse.overseasentitiesapi.model.BeneficialOwnersStatementType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerGovernmentOrPublicAuthorityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.PresenterDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_CORPORATE_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_STATEMENT;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.MANAGING_OFFICERS_CORPORATE_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.FILING_KIND_OVERSEAS_ENTITY;

@ExtendWith(MockitoExtension.class)
class FilingServiceTest {

    private static final String OVERSEAS_ENTITY_ID = "abc123";
    private static final String FILING_DESCRIPTION_IDENTIFIER = "Filing Description Id";
    private static final String FILING_DESCRIPTION = "Filing Description with registration date {registration date}";
    private static final LocalDate DUMMY_DATE = LocalDate.of(2022, 3, 26);

    @InjectMocks
    private FilingsService filingsService;

    @Mock
    private OverseasEntitiesService overseasEntitiesService;

    @Mock
    private Supplier<LocalDate> localDateSupplier;

    @Test
    void testFilingGenerationWhenSuccessful() throws SubmissionNotFoundException, ServiceException {
        when(localDateSupplier.get()).thenReturn(DUMMY_DATE);
        ReflectionTestUtils.setField(filingsService, "filingDescriptionIdentifier", FILING_DESCRIPTION_IDENTIFIER);
        ReflectionTestUtils.setField(filingsService, "filingDescription", FILING_DESCRIPTION);
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDto();
        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.of(overseasEntitySubmissionDto);
        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);

        FilingApi filing = filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID);

        verify(localDateSupplier, times(1)).get();
        assertEquals(FILING_KIND_OVERSEAS_ENTITY, filing.getKind());
        assertEquals(FILING_DESCRIPTION_IDENTIFIER, filing.getDescriptionIdentifier());
        assertEquals("Filing Description with registration date 26 March 2022", filing.getDescription());
        final PresenterDto presenterInFiling = (PresenterDto)filing.getData().get("presenter");
        assertEquals("Joe Bloggs", presenterInFiling.getFullName());
        assertEquals("user@domain.roe", presenterInFiling.getEmail());
        final EntityDto entityInFiling = ((EntityDto) filing.getData().get("entity"));
        assertEquals("Joe Bloggs Ltd", entityInFiling.getName());
        assertEquals("Eutopia", entityInFiling.getIncorporationCountry());
        final BeneficialOwnersStatementType beneficialOwnersStatement = (BeneficialOwnersStatementType)filing.getData().get(BENEFICIAL_OWNERS_STATEMENT);
        assertEquals(BeneficialOwnersStatementType.ALL_IDENTIFIED_ALL_DETAILS, beneficialOwnersStatement);
        final List<BeneficialOwnerIndividualDto> beneficialOwnersIndividualInFiling = ((List<BeneficialOwnerIndividualDto>) filing.getData().get(BENEFICIAL_OWNERS_INDIVIDUAL_FIELD));
        assertEquals(1, beneficialOwnersIndividualInFiling.size());
        final BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = beneficialOwnersIndividualInFiling.get(0);
        assertEquals("Jack", beneficialOwnerIndividualDto.getFirstName());
        assertEquals("Jones", beneficialOwnerIndividualDto.getLastName());
        assertEquals("Welsh", beneficialOwnerIndividualDto.getNationality());
        List<BeneficialOwnerGovernmentOrPublicAuthorityDto> beneficialOwnerGovernmentOrPublicAuthorityInFiling = ((List<BeneficialOwnerGovernmentOrPublicAuthorityDto>) filing.getData().get(BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD));
        assertEquals(1, beneficialOwnerGovernmentOrPublicAuthorityInFiling.size());
        final BeneficialOwnerGovernmentOrPublicAuthorityDto beneficialOwnerGovernmentOrPublicAuthorityDto = beneficialOwnerGovernmentOrPublicAuthorityInFiling.get(0);
        assertEquals("The Government", beneficialOwnerGovernmentOrPublicAuthorityDto.getName());
        assertEquals("Legal form", beneficialOwnerGovernmentOrPublicAuthorityDto.getLegalForm());
        assertEquals("The Law", beneficialOwnerGovernmentOrPublicAuthorityDto.getLawGoverned());
        final List<BeneficialOwnerCorporateDto> beneficialOwnersCorporateDto = ((List<BeneficialOwnerCorporateDto>) filing.getData().get(BENEFICIAL_OWNERS_CORPORATE_FIELD));
        assertEquals(1, beneficialOwnersIndividualInFiling.size());
        final BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = beneficialOwnersCorporateDto.get(0);
        assertEquals("Top Class", beneficialOwnerCorporateDto.getLegalForm());
        assertTrue(beneficialOwnerCorporateDto.getOnRegisterInCountryFormedIn());
        assertEquals(LocalDate.of(2020, 4, 23), beneficialOwnerCorporateDto.getStartDate());
        List<ManagingOfficerIndividualDto> managingOfficersIndividualDto = (List<ManagingOfficerIndividualDto>) filing.getData().get(MANAGING_OFFICERS_INDIVIDUAL_FIELD);
        ManagingOfficerIndividualDto managingOfficerIndividualDto = managingOfficersIndividualDto.get(0);
        assertEquals("Walter", managingOfficerIndividualDto.getFirstName());
        assertEquals("Blanc", managingOfficerIndividualDto.getLastName());
        assertEquals("French", managingOfficerIndividualDto.getNationality());
        List<ManagingOfficerCorporateDto> managingOfficersCorporateDto = (List<ManagingOfficerCorporateDto>) filing.getData().get(MANAGING_OFFICERS_CORPORATE_FIELD);
        ManagingOfficerCorporateDto managingOfficerCorporateDto = managingOfficersCorporateDto.get(0);
        assertEquals("Corporate Man", managingOfficerCorporateDto.getName());
        assertEquals("The Law", managingOfficerCorporateDto.getLawGoverned());
        assertEquals("Legal FM", managingOfficerCorporateDto.getLegalForm());
    }

    @Test
    void testFilingGenerationWhenThrowsExceptionForNoSubmission()  {
        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.empty();
                when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);
        assertThrows(SubmissionNotFoundException.class, () -> filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID));
    }
}
