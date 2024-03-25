package uk.gov.companieshouse.overseasentitiesapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static uk.gov.companieshouse.overseasentitiesapi.mocks.Mocks.EMAIL_WITHOUT_LEADING_AND_TRAILING_SPACES;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_CORPORATE_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_STATEMENT;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.DUE_DILIGENCE_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.ENTITY_NAME_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.MANAGING_OFFICERS_CORPORATE_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.OVERSEAS_ENTITY_DUE_DILIGENCE;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.TRUST_DATA;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.FILING_KIND_OVERSEAS_ENTITY;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.FILING_KIND_OVERSEAS_ENTITY_REMOVE;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.FILING_KIND_OVERSEAS_ENTITY_UPDATE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponseException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.handler.payment.PaymentResourceHandler;
import uk.gov.companieshouse.api.handler.payment.request.PaymentGet;
import uk.gov.companieshouse.api.handler.transaction.TransactionsResourceHandler;
import uk.gov.companieshouse.api.handler.transaction.request.TransactionsPaymentGet;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataApi;
import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataListApi;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.filinggenerator.FilingApi;
import uk.gov.companieshouse.api.model.managingofficerdata.ManagingOfficerDataApi;
import uk.gov.companieshouse.api.model.managingofficerdata.ManagingOfficerListDataApi;
import uk.gov.companieshouse.api.model.officers.CompanyOfficerApi;
import uk.gov.companieshouse.api.model.officers.OfficersApi;
import uk.gov.companieshouse.api.model.payment.PaymentApi;
import uk.gov.companieshouse.api.model.psc.PscApi;
import uk.gov.companieshouse.api.model.psc.PscsApi;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.api.model.transaction.TransactionLinks;
import uk.gov.companieshouse.api.model.transaction.TransactionPayment;
import uk.gov.companieshouse.api.model.update.OverseasEntityDataApi;
import uk.gov.companieshouse.overseasentitiesapi.client.ApiClientService;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.exception.SubmissionNotFoundException;
import uk.gov.companieshouse.overseasentitiesapi.mocks.AddressMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.Mocks;
import uk.gov.companieshouse.overseasentitiesapi.mocks.UpdateSubmissionMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.OverseasEntityDueDiligenceMock;
import uk.gov.companieshouse.overseasentitiesapi.model.BeneficialOwnersStatementType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerGovernmentOrPublicAuthorityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.DueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityNameDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntityDueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.PresenterDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.RemoveDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustDataDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations.IndividualManagingOfficerCessation;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.PersonName;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.UpdateSubmission;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions.Addition;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions.IndividualBeneficialOwnerAddition;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions.IndividualManagingOfficerAddition;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations.Cessation;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.cessations.IndividualBeneficialOwnerCessation;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.Change;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.EntityNameChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;
import uk.gov.companieshouse.overseasentitiesapi.utils.PublicPrivateDataCombiner;

@ExtendWith(MockitoExtension.class)
class FilingServiceTest {

    private static final String REQUEST_ID = "xyz987";
    private static final String OVERSEAS_ENTITY_ID = "abc123";

    private static final String TRANSACTION_ID = "3324324324-3243243-32424";
    private static final String PAYMENT_METHOD = "credit-card";
    private static final String PAYMENT_REFERENCE = "332432432423";
    private static final String FILING_DESCRIPTION_IDENTIFIER = "Filing Description Id";
    private static final String FILING_DESCRIPTION = "Filing Description with registration date {date}";
    private static final String UPDATE_FILING_DESCRIPTION = "Overseas entity update statement made {date}";
    private static final String REMOVE_FILING_DESCRIPTION = "Overseas entity remove statement made {date}";
    private static final LocalDate DUMMY_DATE = LocalDate.of(2022, 3, 26);
    private static final String ERROR_MESSAGE = "error message";
    private static final String PASS_THROUGH_HEADER = "432342353255";
    private static final List<Change> DUMMY_CHANGES = List.of(new EntityNameChange("New name"));
    private static final List<Cessation> DUMMY_BO_CESSATION = List.of(
            new IndividualBeneficialOwnerCessation("Appointment id",
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 1),
                Boolean.TRUE,
                new PersonName("Joe", "Bloggs")));
    private static final List<Cessation> DUMMY_MO_CESSATION = List.of(
            new IndividualManagingOfficerCessation("Appointment id",
                    new PersonName("Joe","Bloggs"),
                    "1980-01-01",
                    Boolean.TRUE,
                    LocalDate.of(2023, 1, 1)));
    private static final List<Addition> DUMMY_BO_ADDITION = List.of(
            new IndividualBeneficialOwnerAddition(
                    LocalDate.of(2023, 1, 1),
                    LocalDate.of(2023, 1, 1),
                    new Address(),
                    new Address(),
                    List.of("OE_OWNERSHIPOFSHARES_MORETHAN25PERCENT_AS_PERSON"),
                    false));
    private static final List<Addition> DUMMY_MO_ADDITION = List.of(
            new IndividualManagingOfficerAddition(
                    LocalDate.of(2023, 1, 1),
                    new Address(),
                    new Address(),
                    LocalDate.of(2023, 1, 1)));

    @InjectMocks
    private FilingsService filingsService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private OverseasEntitiesService overseasEntitiesService;

    @Mock
    private Supplier<LocalDate> localDateSupplier;

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ApiClient apiClient;

    @Mock
    private TransactionsResourceHandler transactionsResourceHandler;

    @Mock
    private TransactionsPaymentGet transactionsPaymentGet;

    @Mock
    private PaymentResourceHandler paymentResourceHandler;

    @Mock
    private PaymentGet paymentGet;

    @Mock
    private PublicDataRetrievalService publicDataRetrievalService;

    @Mock
    private PrivateDataRetrievalService privateDataRetrievalService;

    @Mock
    private BeneficialOwnerAdditionService beneficialOwnerAdditionService;

    @Mock
    private BeneficialOwnerCessationService beneficialOwnerCessationService;

    @Mock
    private ManagingOfficerAdditionService managingOfficerAdditionService;

    @Mock
    private PublicPrivateDataCombiner publicPrivateDataCombiner;

    @Mock
    private OverseasEntityChangeService overseasEntityChangeService;

    @Mock
    private BeneficialOwnerChangeService beneficialOwnerChangeService;

    @Mock
    private ManagingOfficerCessationService managingOfficerCessationService;

    @Mock
    private ManagingOfficerChangeService managingOfficerChangeService;

    private Transaction transaction;

    @BeforeEach
    void init() {
        setValidationEnabledFeatureFlag(false);

        transaction = new Transaction();
        transaction.setId(TRANSACTION_ID);
        var transactionLinks = new TransactionLinks();
        transactionLinks.setPayment("/12345678/payment");
        transaction.setLinks(transactionLinks);
        ReflectionTestUtils.setField(filingsService, "salt", "mockedSalt");
    }

    void initTransactionPaymentLinkMocks() throws IOException, URIValidationException {
        var transactionPayment = new TransactionPayment();
        transactionPayment.setPaymentReference(PAYMENT_REFERENCE);

        var transactionApiResponse = new ApiResponse<>(200, null, transactionPayment);

        when(apiClientService.getOauthAuthenticatedClient(PASS_THROUGH_HEADER)).thenReturn(apiClient);
        when(apiClient.transactions()).thenReturn(transactionsResourceHandler);
        when(transactionsResourceHandler.getPayment(anyString())).thenReturn(transactionsPaymentGet);
        when(transactionsPaymentGet.execute()).thenReturn(transactionApiResponse);
    }

    void initGetPaymentMocks() throws ApiErrorResponseException, URIValidationException {
        var paymentApi = new PaymentApi();
        paymentApi.setPaymentMethod(PAYMENT_METHOD);

        var paymentApiResponse = new ApiResponse<>(200, null, paymentApi);

        when(apiClient.payment()).thenReturn(paymentResourceHandler);
        when(paymentResourceHandler.get(anyString())).thenReturn(paymentGet);
        when(paymentGet.execute()).thenReturn(paymentApiResponse);
    }

    void initGetPublicPrivateDataCombinerMocks() throws JsonProcessingException, ServiceException {
        publicPrivateDataCombiner = new PublicPrivateDataCombiner(publicDataRetrievalService,
            privateDataRetrievalService,
            "test_salt");

        String pscJsonString = "{" +
            "\"links\": {" +
            "\"self\": \"/company/03681242/persons-with-significant-control/individual/7HGCOOm_5F9YAxMTt5UBo8xVsYY\""
            +
            "}" +
            "}";

        String officersApiString = "{" +
            "\"items\": [" +
            "{" +
            "\"links\": {" +
            "\"self\": \"/company/01913593/appointments/7HGCOOm_5F9YAxMTt5UBo8xVsYY\"" +
            "}" +
            "}" +
            "]" +
            "}";

        CompanyProfileApi publicOE = new CompanyProfileApi();
        publicOE.setCompanyName("Test Public Company");
        OverseasEntityDataApi privateOE = new OverseasEntityDataApi();
        privateOE.setEmail("john@smith.com");

        when(publicDataRetrievalService.getCompanyProfile(any(), any())).thenReturn(publicOE);
        when(privateDataRetrievalService.getOverseasEntityData(any())).thenReturn(privateOE);

        // Prepare test data
        PrivateBoDataApi privateBoDataApi = new PrivateBoDataApi();
        privateBoDataApi.setPscId("12345");

        ObjectMapper objectMapper = new ObjectMapper();
        PscApi pscApi = objectMapper.readValue(pscJsonString, PscApi.class);

        PscsApi pscsApi = new PscsApi();
        pscsApi.setItems(List.of(pscApi));

        ManagingOfficerDataApi managingOfficerDataApi = new ManagingOfficerDataApi();
        managingOfficerDataApi.setManagingOfficerAppointmentId("12345");

        OfficersApi officersApi = objectMapper.readValue(officersApiString, OfficersApi.class);

        // Configure mock behavior
        when(privateDataRetrievalService.getManagingOfficerData(any())).thenReturn(
            new ManagingOfficerListDataApi(List.of(managingOfficerDataApi)));
        when(publicDataRetrievalService.getOfficers(any(), any())).thenReturn(officersApi);

        // Configure mock behavior
        when(privateDataRetrievalService.getBeneficialOwnersData(any())).thenReturn(
            new PrivateBoDataListApi(List.of(privateBoDataApi)));
        when(publicDataRetrievalService.getPSCs(any(), any())).thenReturn(pscsApi);
    }

    @Test
    void testFilingGenerationWhenSuccessfulWithoutTrustsAndWithIdentityChecksForRegistration() throws SubmissionNotFoundException, ServiceException, IOException, URIValidationException {
        initTransactionPaymentLinkMocks();
        initGetPaymentMocks();
        when(localDateSupplier.get()).thenReturn(DUMMY_DATE);
        ReflectionTestUtils.setField(filingsService, "filingDescriptionIdentifier", FILING_DESCRIPTION_IDENTIFIER);
        ReflectionTestUtils.setField(filingsService, "registrationFilingDescription", FILING_DESCRIPTION);
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDto();
        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.of(overseasEntitySubmissionDto);
        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);

        FilingApi filing = filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction, PASS_THROUGH_HEADER);
        verify(overseasEntityChangeService, times(0)).collateOverseasEntityChanges(Mockito.any(), Mockito.any(), Mockito.any());
        verify(beneficialOwnerChangeService, times(0)).collateBeneficialOwnerChanges(Mockito.any(), Mockito.any(), Mockito.any());
        verify(beneficialOwnerAdditionService, times(0)).beneficialOwnerAdditions(Mockito.any());
        verify(beneficialOwnerCessationService, times(0)).beneficialOwnerCessations(Mockito.any(), Mockito.any(), Mockito.any());
        verify(managingOfficerChangeService, times(0)).collateManagingOfficerChanges(Mockito.any(), Mockito.any(), Mockito.any());
        verify(managingOfficerAdditionService, times(0)).managingOfficerAdditions(Mockito.any());
        verify(managingOfficerCessationService, times(0)).managingOfficerCessations(Mockito.any(), Mockito.any(), Mockito.any());

        verify(localDateSupplier, times(1)).get();
        assertEquals(FILING_KIND_OVERSEAS_ENTITY, filing.getKind());
        assertEquals(FILING_DESCRIPTION_IDENTIFIER, filing.getDescriptionIdentifier());
        assertEquals("Filing Description with registration date 26 March 2022", filing.getDescription());
        assertEquals("Joe Bloggs Ltd", filing.getData().get("entity_name"));
        final PresenterDto presenterInFiling = (PresenterDto)filing.getData().get("presenter");
        assertEquals("Joe Bloggs", presenterInFiling.getFullName());
        assertEquals("user@domain.roe", presenterInFiling.getEmail());
        final EntityDto entityInFiling = ((EntityDto) filing.getData().get("entity"));
        assertEquals("Eutopia", entityInFiling.getIncorporationCountry());

        checkDueDiligence(filing);
        checkOverseasEntityDueDiligence(filing);
        checkTrustDataIsEmpty(filing);
        checkBeneficialOwners(filing);
        checkManagingOfficers(filing);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testFilingGenerationWhenSuccessfulWithoutTrustsAndWithIdentityChecksForUpdate() throws SubmissionNotFoundException, ServiceException, IOException, URIValidationException {
        initTransactionPaymentLinkMocks();
        initGetPaymentMocks();
        initGetPublicPrivateDataCombinerMocks();
        when(localDateSupplier.get()).thenReturn(DUMMY_DATE);
        ReflectionTestUtils.setField(filingsService, "filingDescriptionIdentifier", FILING_DESCRIPTION_IDENTIFIER);
        ReflectionTestUtils.setField(filingsService, "updateFilingDescription", UPDATE_FILING_DESCRIPTION);
        UpdateSubmission updateSubmission = UpdateSubmissionMock.getUpdateSubmissionMock();
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDto();
        overseasEntitySubmissionDto.setEntityNumber("OE111229");
        overseasEntitySubmissionDto.setTrusts(new ArrayList<>());

        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.of(overseasEntitySubmissionDto);
        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);
        when(overseasEntityChangeService.collateOverseasEntityChanges(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(DUMMY_CHANGES);
        when(beneficialOwnerCessationService.beneficialOwnerCessations(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(DUMMY_BO_CESSATION);
        when(managingOfficerCessationService.managingOfficerCessations(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(DUMMY_MO_CESSATION);
        when(beneficialOwnerAdditionService.beneficialOwnerAdditions(Mockito.any())).thenReturn(DUMMY_BO_ADDITION);
        when(managingOfficerAdditionService.managingOfficerAdditions(Mockito.any())).thenReturn(DUMMY_MO_ADDITION);
        when(beneficialOwnerChangeService.collateBeneficialOwnerChanges(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(DUMMY_CHANGES);
        when(managingOfficerChangeService.collateManagingOfficerChanges(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(DUMMY_CHANGES);

        FilingApi filing = filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction, PASS_THROUGH_HEADER);
        verify(overseasEntityChangeService, times(1)).collateOverseasEntityChanges(Mockito.any(), Mockito.any(), Mockito.any());
        verify(beneficialOwnerChangeService, times(1)).collateBeneficialOwnerChanges(Mockito.any(), Mockito.any(), Mockito.any());
        verify(beneficialOwnerAdditionService, times(1)).beneficialOwnerAdditions(Mockito.any());
        verify(managingOfficerAdditionService, times(1)).managingOfficerAdditions(Mockito.any());
        verify(beneficialOwnerCessationService, times(1)).beneficialOwnerCessations(Mockito.any(), Mockito.any(), Mockito.any());
        verify(managingOfficerCessationService, times(1)).managingOfficerCessations(Mockito.any(), Mockito.any(), Mockito.any());
        verify(managingOfficerChangeService, times(1)).collateManagingOfficerChanges(Mockito.any(), Mockito.any(), Mockito.any());

        verify(localDateSupplier, times(1)).get();
        assertEquals(FILING_KIND_OVERSEAS_ENTITY_UPDATE, filing.getKind());
        assertEquals(FILING_DESCRIPTION_IDENTIFIER, filing.getDescriptionIdentifier());
        assertEquals("Overseas entity update statement made 26 March 2022", filing.getDescription());

        assertEquals("OE111229", filing.getData().get("entityNumber"));
        assertEquals("Test Public Company", filing.getData().get("entityName"));

        final List<Change> changesList = (List<Change>)filing.getData().get("changes");
        final EntityNameChange entityNameChange = (EntityNameChange)changesList.get(0);
        assertEquals("New name", entityNameChange.getProposedCorporateBodyName());
        assertEquals("Joe Bloggs Ltd", updateSubmission.getEntityName());

        testHaveFilingData(filing, true);

        assertNull(filing.getData().get("noChangesInFilingPeriodStatement"));
        assertFalse((Boolean) filing.getData().get("anyBOsOrMOsAddedOrCeased"));
        assertEquals(BeneficialOwnersStatementType.ALL_IDENTIFIED_ALL_DETAILS, filing.getData().get("beneficialOwnerStatement"));

        assertEquals(3, ((List<?>)filing.getData().get("changes")).size());
        assertEquals(2, ((List<?>)filing.getData().get("additions")).size());
        assertEquals(2, ((List<?>)filing.getData().get("cessations")).size());
    }

    @Test
    void testFilingGenerationWhenSuccessfulWithTrustsAndWithIdentityChecksForUpdate() throws SubmissionNotFoundException, ServiceException, IOException, URIValidationException {
        initTransactionPaymentLinkMocks();
        initGetPaymentMocks();
        initGetPublicPrivateDataCombinerMocks();
        when(localDateSupplier.get()).thenReturn(DUMMY_DATE);
        ReflectionTestUtils.setField(filingsService, "filingDescriptionIdentifier", FILING_DESCRIPTION_IDENTIFIER);
        ReflectionTestUtils.setField(filingsService, "updateFilingDescription", UPDATE_FILING_DESCRIPTION);
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDto();
        overseasEntitySubmissionDto.setEntityNumber("OE111229");
        overseasEntitySubmissionDto.setTrusts(List.of(new TrustDataDto()));
        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.of(overseasEntitySubmissionDto);
        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);
        when(overseasEntityChangeService.collateOverseasEntityChanges(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(DUMMY_CHANGES);
        when(beneficialOwnerCessationService.beneficialOwnerCessations(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(DUMMY_BO_CESSATION);
        when(managingOfficerCessationService.managingOfficerCessations(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(DUMMY_MO_CESSATION);
        when(beneficialOwnerAdditionService.beneficialOwnerAdditions(Mockito.any())).thenReturn(DUMMY_BO_ADDITION);
        when(managingOfficerAdditionService.managingOfficerAdditions(Mockito.any())).thenReturn(DUMMY_MO_ADDITION);
        when(beneficialOwnerChangeService.collateBeneficialOwnerChanges(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(DUMMY_CHANGES);
        when(managingOfficerChangeService.collateManagingOfficerChanges(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(DUMMY_CHANGES);

        FilingApi filing = filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction, PASS_THROUGH_HEADER);
        verify(overseasEntityChangeService, times(1)).collateOverseasEntityChanges(Mockito.any(), Mockito.any(), Mockito.any());
        verify(beneficialOwnerChangeService, times(1)).collateBeneficialOwnerChanges(Mockito.any(), Mockito.any(), Mockito.any());
        verify(beneficialOwnerAdditionService, times(1)).beneficialOwnerAdditions(Mockito.any());
        verify(managingOfficerAdditionService, times(1)).managingOfficerAdditions(Mockito.any());
        verify(beneficialOwnerCessationService, times(1)).beneficialOwnerCessations(Mockito.any(), Mockito.any(), Mockito.any());
        verify(managingOfficerCessationService, times(1)).managingOfficerCessations(Mockito.any(), Mockito.any(), Mockito.any());
        verify(managingOfficerChangeService, times(1)).collateManagingOfficerChanges(Mockito.any(), Mockito.any(), Mockito.any());

        verify(localDateSupplier, times(1)).get();
        assertEquals(FILING_KIND_OVERSEAS_ENTITY_UPDATE, filing.getKind());
        assertEquals(FILING_DESCRIPTION_IDENTIFIER, filing.getDescriptionIdentifier());
        assertEquals("Overseas entity update statement made 26 March 2022", filing.getDescription());

        assertEquals("OE111229", filing.getData().get("entityNumber"));

        assertNotNull(filing.getData().get("userSubmission"));
        assertNotNull(filing.getData().get("dueDiligence"));
        assertNotNull(filing.getData().get("presenter"));
        assertNotNull(filing.getData().get("filingForDate"));
        assertNotNull(filing.getData().get("trusts"));
        assertNull(filing.getData().get("noChangesInFilingPeriodStatement"));
        assertFalse((Boolean) filing.getData().get("anyBOsOrMOsAddedOrCeased"));
        assertEquals(BeneficialOwnersStatementType.ALL_IDENTIFIED_ALL_DETAILS, filing.getData().get("beneficialOwnerStatement"));

        assertEquals(3, ((List<?>)filing.getData().get("changes")).size());
        assertEquals(2, ((List<?>)filing.getData().get("additions")).size());
        assertEquals(2, ((List<?>)filing.getData().get("cessations")).size());
    }

    @Test
    void testFilingGenerationWhenSuccessfulForRemove() throws Exception {
        initTransactionPaymentLinkMocks();
        initGetPaymentMocks();
        initGetPublicPrivateDataCombinerMocks();
        when(localDateSupplier.get()).thenReturn(DUMMY_DATE);
        ReflectionTestUtils.setField(filingsService, "filingDescriptionIdentifier", FILING_DESCRIPTION_IDENTIFIER);
        ReflectionTestUtils.setField(filingsService, "removeFilingDescription", REMOVE_FILING_DESCRIPTION);
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDto();
        overseasEntitySubmissionDto.setEntityNumber("OE111229");

        // This indicates it's a Remove submission
        overseasEntitySubmissionDto.setIsRemove(true);
        RemoveDto removeDto = new RemoveDto();
        removeDto.setIsNotProprietorOfLand(true);
        overseasEntitySubmissionDto.setRemove(removeDto);

        overseasEntitySubmissionDto.setTrusts(List.of(new TrustDataDto()));
        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.of(overseasEntitySubmissionDto);
        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);
        when(overseasEntityChangeService.collateOverseasEntityChanges(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(DUMMY_CHANGES);
        when(beneficialOwnerCessationService.beneficialOwnerCessations(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(DUMMY_BO_CESSATION);
        when(managingOfficerCessationService.managingOfficerCessations(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(DUMMY_MO_CESSATION);
        when(beneficialOwnerAdditionService.beneficialOwnerAdditions(Mockito.any())).thenReturn(DUMMY_BO_ADDITION);
        when(managingOfficerAdditionService.managingOfficerAdditions(Mockito.any())).thenReturn(DUMMY_MO_ADDITION);
        when(beneficialOwnerChangeService.collateBeneficialOwnerChanges(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(DUMMY_CHANGES);
        when(managingOfficerChangeService.collateManagingOfficerChanges(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(DUMMY_CHANGES);

        FilingApi filing = filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction, PASS_THROUGH_HEADER);
        verify(overseasEntityChangeService, times(1)).collateOverseasEntityChanges(Mockito.any(), Mockito.any(), Mockito.any());
        verify(beneficialOwnerChangeService, times(1)).collateBeneficialOwnerChanges(Mockito.any(), Mockito.any(), Mockito.any());
        verify(beneficialOwnerAdditionService, times(1)).beneficialOwnerAdditions(Mockito.any());
        verify(managingOfficerAdditionService, times(1)).managingOfficerAdditions(Mockito.any());
        verify(beneficialOwnerCessationService, times(1)).beneficialOwnerCessations(Mockito.any(), Mockito.any(), Mockito.any());
        verify(managingOfficerCessationService, times(1)).managingOfficerCessations(Mockito.any(), Mockito.any(), Mockito.any());
        verify(managingOfficerChangeService, times(1)).collateManagingOfficerChanges(Mockito.any(), Mockito.any(), Mockito.any());

        verify(localDateSupplier, times(1)).get();
        assertEquals(FILING_KIND_OVERSEAS_ENTITY_REMOVE, filing.getKind());
        assertEquals(FILING_DESCRIPTION_IDENTIFIER, filing.getDescriptionIdentifier());
        assertEquals("Overseas entity remove statement made 26 March 2022", filing.getDescription());

        assertEquals("OE111229", filing.getData().get("entityNumber"));
        assertNull(filing.getCost());

        assertNotNull(filing.getData().get("userSubmission"));
        assertNotNull(filing.getData().get("dueDiligence"));
        assertNotNull(filing.getData().get("presenter"));
        assertNull(filing.getData().get("filingForDate"));
        assertNotNull(filing.getData().get("trusts"));
        assertNull(filing.getData().get("noChangesInFilingPeriodStatement"));
        assertFalse((Boolean) filing.getData().get("anyBOsOrMOsAddedOrCeased"));
        assertEquals(BeneficialOwnersStatementType.ALL_IDENTIFIED_ALL_DETAILS, filing.getData().get("beneficialOwnerStatement"));

        assertEquals(3, ((List<?>)filing.getData().get("changes")).size());
        assertEquals(2, ((List<?>)filing.getData().get("additions")).size());
        assertEquals(2, ((List<?>)filing.getData().get("cessations")).size());
    }

    private static void testHaveFilingData(FilingApi filing, boolean withDueDiligence) {
        assertNotNull(filing.getData().get("userSubmission"));
        if (withDueDiligence) {
            assertNotNull(filing.getData().get("dueDiligence"));
        } else {
            assertNull(filing.getData().get("dueDiligence"));
        }
        assertNotNull(filing.getData().get("presenter"));
        assertNotNull(filing.getData().get("filingForDate"));
    }

    @Test
    void testFilingGenerationWhenSuccessfulWithoutTrustsAndWithIdentityChecksForNoChangeUpdate() throws SubmissionNotFoundException, ServiceException, IOException, URIValidationException {
        initTransactionPaymentLinkMocks();
        initGetPaymentMocks();

        when(localDateSupplier.get()).thenReturn(DUMMY_DATE);
        ReflectionTestUtils.setField(filingsService, "filingDescriptionIdentifier", FILING_DESCRIPTION_IDENTIFIER);
        ReflectionTestUtils.setField(filingsService, "updateFilingDescription", UPDATE_FILING_DESCRIPTION);
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDto();
        overseasEntitySubmissionDto.getUpdate().setNoChange(true);
        overseasEntitySubmissionDto.setEntityNumber("OE111229");
        overseasEntitySubmissionDto.setTrusts(null);

        overseasEntitySubmissionDto.setEntityName(new EntityNameDto());
        overseasEntitySubmissionDto.getEntityName().setName("Test OE");

        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.of(overseasEntitySubmissionDto);
        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);

        FilingApi filing = filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction, PASS_THROUGH_HEADER);
        verify(overseasEntityChangeService, times(0)).collateOverseasEntityChanges(Mockito.any(), Mockito.any(), Mockito.any());
        verify(beneficialOwnerChangeService, times(0)).collateBeneficialOwnerChanges(Mockito.any(), Mockito.any(), Mockito.any());
        verify(beneficialOwnerAdditionService, times(0)).beneficialOwnerAdditions(Mockito.any());
        verify(managingOfficerAdditionService, times(0)).managingOfficerAdditions(Mockito.any());
        verify(beneficialOwnerCessationService, times(0)).beneficialOwnerCessations(Mockito.any(), Mockito.any(), Mockito.any());
        verify(managingOfficerCessationService, times(0)).managingOfficerCessations(Mockito.any(), Mockito.any(), Mockito.any());
        verify(managingOfficerChangeService, times(0)).collateManagingOfficerChanges(Mockito.any(), Mockito.any(), Mockito.any());

        verify(localDateSupplier, times(1)).get();
        assertEquals(FILING_KIND_OVERSEAS_ENTITY_UPDATE, filing.getKind());
        assertEquals(FILING_DESCRIPTION_IDENTIFIER, filing.getDescriptionIdentifier());
        assertEquals("Overseas entity update statement made 26 March 2022", filing.getDescription());

        assertEquals("OE111229", filing.getData().get("entityNumber"));
        assertEquals("Test OE", filing.getData().get("entityName"));

        testHaveFilingData(filing, false);

        assertNull(filing.getData().get("noChangesInFilingPeriodStatement"));
        assertNull(filing.getData().get("trusts"));
        assertFalse((Boolean) filing.getData().get("anyBOsOrMOsAddedOrCeased"));
        assertEquals(BeneficialOwnersStatementType.ALL_IDENTIFIED_ALL_DETAILS, filing.getData().get("beneficialOwnerStatement"));

        assertNull(filing.getData().get("changes"));
        assertNull(filing.getData().get("additions"));
        assertNull(filing.getData().get("cessations"));
    }

    @Test
    void testFilingGenerationAllowedWhenEntityNameBlockIsNull() throws Exception {
        initTransactionPaymentLinkMocks();
        initGetPaymentMocks();
        when(localDateSupplier.get()).thenReturn(DUMMY_DATE);
        ReflectionTestUtils.setField(filingsService, "filingDescriptionIdentifier", FILING_DESCRIPTION_IDENTIFIER);
        ReflectionTestUtils.setField(filingsService, "registrationFilingDescription", FILING_DESCRIPTION);
        ReflectionTestUtils.setField(filingsService, "updateFilingDescription", UPDATE_FILING_DESCRIPTION);
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDtoWithBoIndividualTrust();
        overseasEntitySubmissionDto.setEntityName(null);
        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.of(overseasEntitySubmissionDto);

        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);
        when(objectMapper.writeValueAsString(any())).thenReturn(
                "[{\"trust_id\":\"1\",\"trust_name\":\"Trust Name 1\",\"creation_date\":[2020,4,1],\"unable_to_obtain_all_trust_info\":null}]"
        );

        FilingApi filing = filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction, PASS_THROUGH_HEADER);

        assertNull(filing.getData().get(ENTITY_NAME_FIELD));
        checkDueDiligence(filing);
        checkOverseasEntityDueDiligence(filing);
        checkTrustDataIndividual(filing, 0, "1");
        checkBeneficialOwners(filing);
        checkManagingOfficers(filing);
    }

    @Test
    void testFilingGenerationWhenSuccessfulWithBOIndividualTrustAndWithIdentityChecks()
            throws SubmissionNotFoundException, ServiceException, IOException, URIValidationException, JSONException {
        initTransactionPaymentLinkMocks();
        initGetPaymentMocks();
        when(localDateSupplier.get()).thenReturn(DUMMY_DATE);
        ReflectionTestUtils.setField(filingsService, "filingDescriptionIdentifier", FILING_DESCRIPTION_IDENTIFIER);
        ReflectionTestUtils.setField(filingsService, "registrationFilingDescription", FILING_DESCRIPTION);
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDtoWithBoIndividualTrust();
        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.of(overseasEntitySubmissionDto);
        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);
        when(objectMapper.writeValueAsString(any())).thenReturn(
                "[{\"trust_id\":\"1\",\"trust_name\":\"Trust Name 1\",\"creation_date\":[2020,4,1],\"unable_to_obtain_all_trust_info\":null}]"
        );

        FilingApi filing = filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction, PASS_THROUGH_HEADER);

        verify(localDateSupplier, times(1)).get();
        assertEquals(FILING_KIND_OVERSEAS_ENTITY, filing.getKind());
        assertEquals(FILING_DESCRIPTION_IDENTIFIER, filing.getDescriptionIdentifier());
        assertEquals("Filing Description with registration date 26 March 2022", filing.getDescription());
        assertEquals("Joe Bloggs Ltd", filing.getData().get("entity_name"));
        final PresenterDto presenterInFiling = (PresenterDto)filing.getData().get("presenter");
        assertEquals("Joe Bloggs", presenterInFiling.getFullName());
        assertEquals("user@domain.roe", presenterInFiling.getEmail());
        final EntityDto entityInFiling = ((EntityDto) filing.getData().get("entity"));
        assertEquals("Eutopia", entityInFiling.getIncorporationCountry());

        checkDueDiligence(filing);
        checkOverseasEntityDueDiligence(filing);
        checkTrustDataIndividual(filing, 0, "1");
        checkBeneficialOwners(filing);
        checkManagingOfficers(filing);
    }


    @Test
    void testFilingGenerationWhenSuccessfulWithThreeBOIndividualTrustsAndWithIdentityChecks()
            throws SubmissionNotFoundException, ServiceException, IOException, URIValidationException, JSONException {
        initTransactionPaymentLinkMocks();
        initGetPaymentMocks();
        when(localDateSupplier.get()).thenReturn(DUMMY_DATE);
        ReflectionTestUtils.setField(filingsService, "filingDescriptionIdentifier", FILING_DESCRIPTION_IDENTIFIER);
        ReflectionTestUtils.setField(filingsService, "registrationFilingDescription", FILING_DESCRIPTION);
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDtoWithThreeBoIndividualTrusts();
        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.of(overseasEntitySubmissionDto);
        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);
        when(objectMapper.writeValueAsString(any())).thenReturn(
                "[{\"trust_id\":\"1\",\"trust_name\":\"Trust Name 1\",\"creation_date\":[2020,4,1],\"unable_to_obtain_all_trust_info\":null}]",
                "[{\"trust_id\":\"2\",\"trust_name\":\"Trust Name 2\",\"creation_date\":[2020,4,1],\"unable_to_obtain_all_trust_info\":null}]",
                "[{\"trust_id\":\"3\",\"trust_name\":\"Trust Name 3\",\"creation_date\":[2020,4,1],\"unable_to_obtain_all_trust_info\":null}]"
        );

        FilingApi filing = filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction, PASS_THROUGH_HEADER);

        verify(localDateSupplier, times(1)).get();
        assertEquals(FILING_KIND_OVERSEAS_ENTITY, filing.getKind());
        assertEquals(FILING_DESCRIPTION_IDENTIFIER, filing.getDescriptionIdentifier());
        assertEquals("Filing Description with registration date 26 March 2022", filing.getDescription());
        assertEquals("Joe Bloggs Ltd", filing.getData().get("entity_name"));
        final PresenterDto presenterInFiling = (PresenterDto)filing.getData().get("presenter");
        assertEquals("Joe Bloggs", presenterInFiling.getFullName());
        assertEquals("user@domain.roe", presenterInFiling.getEmail());
        final EntityDto entityInFiling = ((EntityDto) filing.getData().get("entity"));
        assertEquals("Eutopia", entityInFiling.getIncorporationCountry());

        checkDueDiligence(filing);
        checkOverseasEntityDueDiligence(filing);
        checkTrustDataIndividual(filing, 0, "1");
        checkTrustDataIndividual(filing, 1, "2");
        checkTrustDataIndividual(filing, 2, "3");
    }

    @Test
    void testFilingGenerationWhenSuccessfulWithBOCorporateTrustAndWithIdentityChecks()
            throws SubmissionNotFoundException, ServiceException, IOException, URIValidationException, JSONException {
        initTransactionPaymentLinkMocks();
        initGetPaymentMocks();
        when(localDateSupplier.get()).thenReturn(DUMMY_DATE);
        ReflectionTestUtils.setField(filingsService, "filingDescriptionIdentifier", FILING_DESCRIPTION_IDENTIFIER);
        ReflectionTestUtils.setField(filingsService, "registrationFilingDescription", FILING_DESCRIPTION);
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDtoWithBoCorporateTrust();
        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.of(overseasEntitySubmissionDto);
        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);
        when(objectMapper.writeValueAsString(any())).thenReturn(
                "[{\"trust_id\":\"1\",\"trust_name\":\"Trust Name 1\",\"creation_date\":[2020,4,1],\"unable_to_obtain_all_trust_info\":null}]"
        );

        FilingApi filing = filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction, PASS_THROUGH_HEADER);

        verify(localDateSupplier, times(1)).get();
        assertEquals(FILING_KIND_OVERSEAS_ENTITY, filing.getKind());
        assertEquals(FILING_DESCRIPTION_IDENTIFIER, filing.getDescriptionIdentifier());
        assertEquals("Filing Description with registration date 26 March 2022", filing.getDescription());
        assertEquals("Joe Bloggs Ltd", filing.getData().get("entity_name"));
        final PresenterDto presenterInFiling = (PresenterDto)filing.getData().get("presenter");
        assertEquals("Joe Bloggs", presenterInFiling.getFullName());
        assertEquals("user@domain.roe", presenterInFiling.getEmail());
        final EntityDto entityInFiling = ((EntityDto) filing.getData().get("entity"));
        assertEquals("Eutopia", entityInFiling.getIncorporationCountry());

        checkDueDiligence(filing);
        checkOverseasEntityDueDiligence(filing);
        checkTrustDataCorporate(filing, 0, "1");
        checkBeneficialOwners(filing);
        checkManagingOfficers(filing);
    }

    @Test
    void testFilingGenerationWhenSuccessfulWithThreeBOCorporateTrustAndWithIdentityChecks()
            throws SubmissionNotFoundException, ServiceException, IOException, URIValidationException, JSONException {
        initTransactionPaymentLinkMocks();
        initGetPaymentMocks();
        when(localDateSupplier.get()).thenReturn(DUMMY_DATE);
        ReflectionTestUtils.setField(filingsService, "filingDescriptionIdentifier", FILING_DESCRIPTION_IDENTIFIER);
        ReflectionTestUtils.setField(filingsService, "registrationFilingDescription", FILING_DESCRIPTION);
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDtoWithThreeBoCorporateTrusts();
        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.of(overseasEntitySubmissionDto);
        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);
        when(objectMapper.writeValueAsString(any())).thenReturn(
                "[{\"trust_id\":\"1\",\"trust_name\":\"Trust Name 1\",\"creation_date\":[2020,4,1],\"unable_to_obtain_all_trust_info\":null}]",
                "[{\"trust_id\":\"2\",\"trust_name\":\"Trust Name 2\",\"creation_date\":[2020,4,1],\"unable_to_obtain_all_trust_info\":null}]",
                "[{\"trust_id\":\"3\",\"trust_name\":\"Trust Name 3\",\"creation_date\":[2020,4,1],\"unable_to_obtain_all_trust_info\":null}]"
        );

        FilingApi filing = filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction, PASS_THROUGH_HEADER);

        verify(localDateSupplier, times(1)).get();
        assertEquals(FILING_KIND_OVERSEAS_ENTITY, filing.getKind());
        assertEquals(FILING_DESCRIPTION_IDENTIFIER, filing.getDescriptionIdentifier());
        assertEquals("Filing Description with registration date 26 March 2022", filing.getDescription());
        assertEquals("Joe Bloggs Ltd", filing.getData().get("entity_name"));
        final PresenterDto presenterInFiling = (PresenterDto)filing.getData().get("presenter");
        assertEquals("Joe Bloggs", presenterInFiling.getFullName());
        assertEquals("user@domain.roe", presenterInFiling.getEmail());
        final EntityDto entityInFiling = ((EntityDto) filing.getData().get("entity"));
        assertEquals("Eutopia", entityInFiling.getIncorporationCountry());

        checkDueDiligence(filing);
        checkOverseasEntityDueDiligence(filing);
        checkTrustDataCorporate(filing, 0, "1");
        checkTrustDataCorporate(filing, 1, "2");
        checkTrustDataCorporate(filing, 2, "3");
    }

    @Test
    void testFilingGenerationWhenSuccessfulWithThreeBOCorporateTrustAndThreeBOIndividualTrustAndWithIdentityChecks()
            throws SubmissionNotFoundException, ServiceException, IOException, URIValidationException, JSONException {
        initTransactionPaymentLinkMocks();
        initGetPaymentMocks();
        when(localDateSupplier.get()).thenReturn(DUMMY_DATE);
        ReflectionTestUtils.setField(filingsService, "filingDescriptionIdentifier", FILING_DESCRIPTION_IDENTIFIER);
        ReflectionTestUtils.setField(filingsService, "registrationFilingDescription", FILING_DESCRIPTION);
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDtoWithThreeBoCorporateTrustsAndThreeIndividualTrust();
        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.of(overseasEntitySubmissionDto);
        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);
        when(objectMapper.writeValueAsString(any())).thenReturn(
                "[{\"trust_id\":\"1\",\"trust_name\":\"Trust Name 1\",\"creation_date\":[2020,4,1],\"unable_to_obtain_all_trust_info\":null}]",
                "[{\"trust_id\":\"2\",\"trust_name\":\"Trust Name 2\",\"creation_date\":[2020,4,1],\"unable_to_obtain_all_trust_info\":null}]",
                "[{\"trust_id\":\"3\",\"trust_name\":\"Trust Name 3\",\"creation_date\":[2020,4,1],\"unable_to_obtain_all_trust_info\":null}]",
                "[{\"trust_id\":\"1\",\"trust_name\":\"Trust Name 1\",\"creation_date\":[2020,4,1],\"unable_to_obtain_all_trust_info\":null}]",
                "[{\"trust_id\":\"2\",\"trust_name\":\"Trust Name 2\",\"creation_date\":[2020,4,1],\"unable_to_obtain_all_trust_info\":null}]",
                "[{\"trust_id\":\"3\",\"trust_name\":\"Trust Name 3\",\"creation_date\":[2020,4,1],\"unable_to_obtain_all_trust_info\":null}]"
        );

        FilingApi filing = filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction, PASS_THROUGH_HEADER);

        verify(localDateSupplier, times(1)).get();
        assertEquals(FILING_KIND_OVERSEAS_ENTITY, filing.getKind());
        assertEquals(FILING_DESCRIPTION_IDENTIFIER, filing.getDescriptionIdentifier());
        assertEquals("Filing Description with registration date 26 March 2022", filing.getDescription());
        assertEquals("Joe Bloggs Ltd", filing.getData().get("entity_name"));
        final PresenterDto presenterInFiling = (PresenterDto)filing.getData().get("presenter");
        assertEquals("Joe Bloggs", presenterInFiling.getFullName());
        assertEquals("user@domain.roe", presenterInFiling.getEmail());
        final EntityDto entityInFiling = ((EntityDto) filing.getData().get("entity"));
        assertEquals("Eutopia", entityInFiling.getIncorporationCountry());

        checkDueDiligence(filing);
        checkOverseasEntityDueDiligence(filing);
        checkTrustDataCorporate(filing, 0, "1");
        checkTrustDataCorporate(filing, 1, "2");
        checkTrustDataCorporate(filing, 2, "3");

        checkTrustDataIndividual(filing, 0, "1");
        checkTrustDataIndividual(filing, 1, "2");
        checkTrustDataIndividual(filing, 2, "3");
    }

    @Test
    void testFilingGenerationWhenSuccessfulWithOneBOIndividualWithThreeTrustsAndWithIdentityChecks()
            throws SubmissionNotFoundException, ServiceException, IOException, URIValidationException, JSONException {
        initTransactionPaymentLinkMocks();
        initGetPaymentMocks();
        when(localDateSupplier.get()).thenReturn(DUMMY_DATE);
        ReflectionTestUtils.setField(filingsService, "filingDescriptionIdentifier", FILING_DESCRIPTION_IDENTIFIER);
        ReflectionTestUtils.setField(filingsService, "registrationFilingDescription", FILING_DESCRIPTION);
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDtoWithBoIndividualWithThreeTrusts();
        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.of(overseasEntitySubmissionDto);
        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);
        when(objectMapper.writeValueAsString(any())).thenReturn(
                "[{\"trust_id\":\"1\",\"trust_name\":\"Trust Name 1\",\"creation_date\":[2020,4,1],\"unable_to_obtain_all_trust_info\":null}," +
                        "{\"trust_id\":\"2\",\"trust_name\":\"Trust Name 2\",\"creation_date\":[2020,4,1],\"unable_to_obtain_all_trust_info\":null}," +
                        "{\"trust_id\":\"3\",\"trust_name\":\"Trust Name 3\",\"creation_date\":[2020,4,1],\"unable_to_obtain_all_trust_info\":null}]"
        );

        FilingApi filing = filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction, PASS_THROUGH_HEADER);

        verify(localDateSupplier, times(1)).get();
        assertEquals(FILING_KIND_OVERSEAS_ENTITY, filing.getKind());
        assertEquals(FILING_DESCRIPTION_IDENTIFIER, filing.getDescriptionIdentifier());
        assertEquals("Filing Description with registration date 26 March 2022", filing.getDescription());
        assertEquals("Joe Bloggs Ltd", filing.getData().get("entity_name"));
        final PresenterDto presenterInFiling = (PresenterDto)filing.getData().get("presenter");
        assertEquals("Joe Bloggs", presenterInFiling.getFullName());
        assertEquals("user@domain.roe", presenterInFiling.getEmail());
        final EntityDto entityInFiling = ((EntityDto) filing.getData().get("entity"));
        assertEquals("Eutopia", entityInFiling.getIncorporationCountry());

        checkDueDiligence(filing);
        checkOverseasEntityDueDiligence(filing);
        checkTrustDataIndividualWithThreeTrusts(filing);
        checkBeneficialOwners(filing);
        checkManagingOfficers(filing);
    }

    @Test
    void testFilingGenerationWhenSuccessfulWithOneBOCorporateWithThreeTrustsAndWithIdentityChecks()
            throws SubmissionNotFoundException, ServiceException, IOException, URIValidationException, JSONException {
        initTransactionPaymentLinkMocks();
        initGetPaymentMocks();
        when(localDateSupplier.get()).thenReturn(DUMMY_DATE);
        ReflectionTestUtils.setField(filingsService, "filingDescriptionIdentifier", FILING_DESCRIPTION_IDENTIFIER);
        ReflectionTestUtils.setField(filingsService, "registrationFilingDescription", FILING_DESCRIPTION);
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDtoWithBoCorporateWithThreeTrusts();
        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.of(overseasEntitySubmissionDto);
        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);
        when(objectMapper.writeValueAsString(any())).thenReturn(
                "[{\"trust_id\":\"1\",\"trust_name\":\"Trust Name 1\",\"creation_date\":[2020,4,1],\"unable_to_obtain_all_trust_info\":null}," +
                        "{\"trust_id\":\"2\",\"trust_name\":\"Trust Name 2\",\"creation_date\":[2020,4,1],\"unable_to_obtain_all_trust_info\":null}," +
                        "{\"trust_id\":\"3\",\"trust_name\":\"Trust Name 3\",\"creation_date\":[2020,4,1],\"unable_to_obtain_all_trust_info\":null}]"
        );

        FilingApi filing = filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction, PASS_THROUGH_HEADER);

        verify(localDateSupplier, times(1)).get();
        assertEquals(FILING_KIND_OVERSEAS_ENTITY, filing.getKind());
        assertEquals(FILING_DESCRIPTION_IDENTIFIER, filing.getDescriptionIdentifier());
        assertEquals("Filing Description with registration date 26 March 2022", filing.getDescription());
        assertEquals("Joe Bloggs Ltd", filing.getData().get("entity_name"));
        final PresenterDto presenterInFiling = (PresenterDto)filing.getData().get("presenter");
        assertEquals("Joe Bloggs", presenterInFiling.getFullName());
        assertEquals("user@domain.roe", presenterInFiling.getEmail());
        final EntityDto entityInFiling = ((EntityDto) filing.getData().get("entity"));
        assertEquals("Eutopia", entityInFiling.getIncorporationCountry());

        checkDueDiligence(filing);
        checkOverseasEntityDueDiligence(filing);
        checkTrustDataCorporateWithThreeTrusts(filing);
        checkBeneficialOwners(filing);
        checkManagingOfficers(filing);
    }

    @Test
    void testFilingGenerationThrowsExceptionWithBOIndividualMoreThanOneTrustID() {
        ReflectionTestUtils.setField(filingsService, "filingDescriptionIdentifier", FILING_DESCRIPTION_IDENTIFIER);
        ReflectionTestUtils.setField(filingsService, "registrationFilingDescription", FILING_DESCRIPTION);
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDtoWithBoIndividualWithTwoTrustsSameID();
        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.of(overseasEntitySubmissionDto);
        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);

        assertThrows(ServiceException.class, () -> filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction, PASS_THROUGH_HEADER));
    }

    @Test
    void testFilingGenerationThrowsExceptionWithBOIndividualNoTrustExists() {
        ReflectionTestUtils.setField(filingsService, "filingDescriptionIdentifier", FILING_DESCRIPTION_IDENTIFIER);
        ReflectionTestUtils.setField(filingsService, "registrationFilingDescription", FILING_DESCRIPTION);
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDtoWithBoIndividualNoTrustExists();
        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.of(overseasEntitySubmissionDto);
        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);

        assertThrows(ServiceException.class, () -> filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction, PASS_THROUGH_HEADER));
    }

    @Test
    void testFilingGenerationThrowsExceptionWithBOIndividualTrustDataIsEmpty() {
        ReflectionTestUtils.setField(filingsService, "filingDescriptionIdentifier", FILING_DESCRIPTION_IDENTIFIER);
        ReflectionTestUtils.setField(filingsService, "registrationFilingDescription", FILING_DESCRIPTION);
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDtoWithBoIndividualTrustDataIsEmpty();
        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.of(overseasEntitySubmissionDto);
        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);

        assertThrows(ServiceException.class, () -> filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction, PASS_THROUGH_HEADER));
    }

    @Test
    void testFilingGenerationThrowsExceptionWithBOCorporateMoreThanOneTrustID() {
        ReflectionTestUtils.setField(filingsService, "filingDescriptionIdentifier", FILING_DESCRIPTION_IDENTIFIER);
        ReflectionTestUtils.setField(filingsService, "registrationFilingDescription", FILING_DESCRIPTION);
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDtoWithBoCorporateWithTwoTrustsSameID();
        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.of(overseasEntitySubmissionDto);
        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);

        assertThrows(ServiceException.class, () -> filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction, PASS_THROUGH_HEADER));
    }

    @Test
    void testFilingGenerationThrowsExceptionWithBOCorporateNoTrustExists() {
        ReflectionTestUtils.setField(filingsService, "filingDescriptionIdentifier", FILING_DESCRIPTION_IDENTIFIER);
        ReflectionTestUtils.setField(filingsService, "registrationFilingDescription", FILING_DESCRIPTION);
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDtoWithBoCorporateNoTrustExists();
        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.of(overseasEntitySubmissionDto);
        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);

        assertThrows(ServiceException.class, () -> filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction, PASS_THROUGH_HEADER));
    }

    @Test
    void testFilingGenerationThrowsExceptionWithBOCorporateTrustDataIsEmpty() {
        ReflectionTestUtils.setField(filingsService, "filingDescriptionIdentifier", FILING_DESCRIPTION_IDENTIFIER);
        ReflectionTestUtils.setField(filingsService, "registrationFilingDescription", FILING_DESCRIPTION);
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDtoWithBoCorporateTrustDataIsEmpty();
        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.of(overseasEntitySubmissionDto);
        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);

        assertThrows(ServiceException.class, () -> filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction, PASS_THROUGH_HEADER));
    }

    @Test
    void testFilingGenerationWhenThrowsExceptionForNoSubmission()  {
        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.empty();
        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);
        assertThrows(SubmissionNotFoundException.class, () -> filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction, PASS_THROUGH_HEADER));
    }


    private void checkTrustDataIsEmpty(FilingApi filing) {
        final List<BeneficialOwnerIndividualDto> beneficialOwnersIndividualInFiling = ((List<BeneficialOwnerIndividualDto>) filing.getData().get(BENEFICIAL_OWNERS_INDIVIDUAL_FIELD));
        final BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = beneficialOwnersIndividualInFiling.get(0);
        assertEquals("", beneficialOwnerIndividualDto.getTrustData());
        final List<BeneficialOwnerCorporateDto> beneficialOwnersCorporateFiling = ((List<BeneficialOwnerCorporateDto>) filing.getData().get(BENEFICIAL_OWNERS_CORPORATE_FIELD));
        final BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = beneficialOwnersCorporateFiling.get(0);
        assertEquals("", beneficialOwnerCorporateDto.getTrustData());
    }

    private void checkTrustDataIsEmptyWithTrustFeatureFlagOn(FilingApi filing) {

        final List<TrustDataDto> trustDataList = ((List<TrustDataDto>) filing.getData().get(TRUST_DATA));

        assertNull(trustDataList);

        final List<BeneficialOwnerIndividualDto> beneficialOwnersIndividualInFiling = ((List<BeneficialOwnerIndividualDto>) filing.getData().get(BENEFICIAL_OWNERS_INDIVIDUAL_FIELD));
        final BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = beneficialOwnersIndividualInFiling.get(0);
        assertNull(beneficialOwnerIndividualDto.getTrustIds());
        final List<BeneficialOwnerCorporateDto> beneficialOwnersCorporateFiling = ((List<BeneficialOwnerCorporateDto>) filing.getData().get(BENEFICIAL_OWNERS_CORPORATE_FIELD));
        final BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = beneficialOwnersCorporateFiling.get(0);
        assertNull(beneficialOwnerCorporateDto.getTrustIds());
    }

    private void checkTrustDataIndividual(FilingApi filing, int boIndex, String trustId) throws JSONException {
        final List<BeneficialOwnerIndividualDto> beneficialOwnersIndividualInFiling = ((List<BeneficialOwnerIndividualDto>) filing.getData().get(BENEFICIAL_OWNERS_INDIVIDUAL_FIELD));
        final BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = beneficialOwnersIndividualInFiling.get(boIndex);
        final JSONArray trustsDataJSON = new JSONArray(beneficialOwnerIndividualDto.getTrustData());
        final JSONObject trustDataJSON = trustsDataJSON.getJSONObject(0);
        assertEquals(trustId, trustDataJSON.get("trust_id"));
        assertEquals("Trust Name " + trustId, trustDataJSON.get("trust_name"));
    }

    private void checkTrustDataIndividualWithTrustFeatureFlagOn(FilingApi filing, int boIndex, String trustId) throws JSONException {
        final List<TrustDataDto> trustDataList = ((List<TrustDataDto>) filing.getData().get(TRUST_DATA));

        final TrustDataDto trustData = trustDataList.get(boIndex);
        assertEquals(trustId, trustData.getTrustId());
        assertEquals("Trust Name " + trustId, trustData.getTrustName());

        final List<BeneficialOwnerIndividualDto> beneficialOwnersIndividualInFiling = ((List<BeneficialOwnerIndividualDto>) filing.getData().get(BENEFICIAL_OWNERS_INDIVIDUAL_FIELD));
        final BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = beneficialOwnersIndividualInFiling.get(boIndex);
        final List<String> trustsIdsList = beneficialOwnerIndividualDto.getTrustIds();

        assertEquals(trustId, trustsIdsList.get(0));
    }

    private void checkTrustDataIndividualWithThreeTrusts(FilingApi filing) throws JSONException {
        final List<BeneficialOwnerIndividualDto> beneficialOwnersIndividualInFiling = ((List<BeneficialOwnerIndividualDto>) filing.getData().get(BENEFICIAL_OWNERS_INDIVIDUAL_FIELD));
        final BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = beneficialOwnersIndividualInFiling.get(0);
        final JSONArray trustsDataJSON = new JSONArray(beneficialOwnerIndividualDto.getTrustData());

        final JSONObject trustDataJSON1 = trustsDataJSON.getJSONObject(0);
        assertEquals("1", trustDataJSON1.get("trust_id"));
        assertEquals("Trust Name 1", trustDataJSON1.get("trust_name"));

        final JSONObject trustDataJSON2 = trustsDataJSON.getJSONObject(1);
        assertEquals("2", trustDataJSON2.get("trust_id"));
        assertEquals("Trust Name 2", trustDataJSON2.get("trust_name"));

        final JSONObject trustDataJSON3 = trustsDataJSON.getJSONObject(2);
        assertEquals("3", trustDataJSON3.get("trust_id"));
        assertEquals("Trust Name 3", trustDataJSON3.get("trust_name"));
    }

    private void checkTrustDataIndividualWithThreeTrustsWithTrustFeatureFlagOn(FilingApi filing) throws JSONException {
        final List<TrustDataDto> trustDataList = ((List<TrustDataDto>) filing.getData().get(TRUST_DATA));

        final TrustDataDto trustData1 = trustDataList.get(0);
        assertEquals("1", trustData1.getTrustId());
        assertEquals("Trust Name 1", trustData1.getTrustName());

        final TrustDataDto trustData2 = trustDataList.get(1);
        assertEquals("2", trustData2.getTrustId());
        assertEquals("Trust Name 2", trustData2.getTrustName());

        final TrustDataDto trustData3 = trustDataList.get(2);
        assertEquals("3", trustData3.getTrustId());
        assertEquals("Trust Name 3", trustData3.getTrustName());

        final List<BeneficialOwnerIndividualDto> beneficialOwnersIndividualInFiling = ((List<BeneficialOwnerIndividualDto>) filing.getData().get(BENEFICIAL_OWNERS_INDIVIDUAL_FIELD));
        final BeneficialOwnerIndividualDto beneficialOwnerCorporateDto = beneficialOwnersIndividualInFiling.get(0);
        final List<String> trustsIdsList = beneficialOwnerCorporateDto.getTrustIds();

        assertEquals("1", trustsIdsList.get(0));
        assertEquals("2", trustsIdsList.get(1));
        assertEquals("3", trustsIdsList.get(2));
    }

    private void checkTrustDataCorporate(FilingApi filing, int boIndex, String trustId) throws JSONException {
        final List<BeneficialOwnerCorporateDto> beneficialOwnersCorporateInFiling = ((List<BeneficialOwnerCorporateDto>) filing.getData().get(BENEFICIAL_OWNERS_CORPORATE_FIELD));
        final BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = beneficialOwnersCorporateInFiling.get(boIndex);
        final JSONArray trustsDataJSON = new JSONArray(beneficialOwnerCorporateDto.getTrustData());
        final JSONObject trustDataJSON = trustsDataJSON.getJSONObject(0);
        assertEquals(trustId, trustDataJSON.get("trust_id"));
        assertEquals("Trust Name " + trustId, trustDataJSON.get("trust_name"));
    }

    private void checkTrustDataCorporateWithTrustFeatureFlagOn(FilingApi filing, int boIndex, String trustId) throws JSONException {

        final List<TrustDataDto> trustDataList = ((List<TrustDataDto>) filing.getData().get(TRUST_DATA));


        final TrustDataDto trustData = trustDataList.get(boIndex);
        assertEquals(trustId, trustData.getTrustId());
        assertEquals("Trust Name " + trustId, trustData.getTrustName());

        final List<BeneficialOwnerCorporateDto> beneficialOwnersCorporateInFiling = ((List<BeneficialOwnerCorporateDto>) filing.getData().get(BENEFICIAL_OWNERS_CORPORATE_FIELD));
        final BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = beneficialOwnersCorporateInFiling.get(boIndex);
        final List<String> trustsIdsList = beneficialOwnerCorporateDto.getTrustIds();

        assertEquals(trustId, trustsIdsList.get(0));
    }


    private void checkTrustDataCorporateWithThreeTrusts(FilingApi filing) throws JSONException {
        final List<BeneficialOwnerCorporateDto> beneficialOwnersCorporateInFiling = ((List<BeneficialOwnerCorporateDto>) filing.getData().get(BENEFICIAL_OWNERS_CORPORATE_FIELD));
        final BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = beneficialOwnersCorporateInFiling.get(0);
        final JSONArray trustsDataJSON = new JSONArray(beneficialOwnerCorporateDto.getTrustData());

        final JSONObject trustDataJSON1 = trustsDataJSON.getJSONObject(0);
        assertEquals("1", trustDataJSON1.get("trust_id"));
        assertEquals("Trust Name 1", trustDataJSON1.get("trust_name"));

        final JSONObject trustDataJSON2 = trustsDataJSON.getJSONObject(1);
        assertEquals("2", trustDataJSON2.get("trust_id"));
        assertEquals("Trust Name 2", trustDataJSON2.get("trust_name"));

        final JSONObject trustDataJSON3 = trustsDataJSON.getJSONObject(2);
        assertEquals("3", trustDataJSON3.get("trust_id"));
        assertEquals("Trust Name 3", trustDataJSON3.get("trust_name"));
    }

    private void checkTrustDataCorporateWithThreeTrustsWithTrustFeatureFlagOn(FilingApi filing) throws JSONException {
        final List<TrustDataDto> trustDataList = ((List<TrustDataDto>) filing.getData().get(TRUST_DATA));


        final TrustDataDto trustData1 = trustDataList.get(0);
        assertEquals("1", trustData1.getTrustId());
        assertEquals("Trust Name 1", trustData1.getTrustName());

        final TrustDataDto trustData2 = trustDataList.get(1);
        assertEquals("2", trustData2.getTrustId());
        assertEquals("Trust Name 2", trustData2.getTrustName());

        final TrustDataDto trustData3 = trustDataList.get(2);
        assertEquals("3", trustData3.getTrustId());
        assertEquals("Trust Name 3", trustData3.getTrustName());

        final List<BeneficialOwnerCorporateDto> beneficialOwnersCorporateInFiling = ((List<BeneficialOwnerCorporateDto>) filing.getData().get(BENEFICIAL_OWNERS_CORPORATE_FIELD));
        final BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = beneficialOwnersCorporateInFiling.get(0);
        final List<String> trustsIdsList = beneficialOwnerCorporateDto.getTrustIds();

        assertEquals("1", trustsIdsList.get(0));
        assertEquals("2", trustsIdsList.get(1));
        assertEquals("3", trustsIdsList.get(2));

    }

    private void checkManagingOfficers(FilingApi filing) {
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

    private void checkBeneficialOwners(FilingApi filing) {
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
    }

    private void checkDueDiligence(FilingApi filing) {
        DueDiligenceDto dueDiligenceDtoInFiling = (DueDiligenceDto)filing.getData().get(DUE_DILIGENCE_FIELD);
        assertEquals(LocalDate.of(2021,12,31), dueDiligenceDtoInFiling.getIdentityDate());
        assertEquals("ABC Checking limited", dueDiligenceDtoInFiling.getName());
        assertEquals("lorem@ipsum.com", dueDiligenceDtoInFiling.getEmail());
        assertEquals("abc123", dueDiligenceDtoInFiling.getAmlNumber());
        assertEquals("agent567", dueDiligenceDtoInFiling.getAgentCode());
        assertEquals("Super supervisor", dueDiligenceDtoInFiling.getSupervisoryName());
        assertEquals("Mr Partner", dueDiligenceDtoInFiling.getPartnerName());
        assertEquals("agreed", dueDiligenceDtoInFiling.getDiligence());
        checkAddress(dueDiligenceDtoInFiling.getAddress());
    }

    private void checkOverseasEntityDueDiligence(FilingApi filing) {
        OverseasEntityDueDiligenceDto oeDueDiligenceDtoInFiling =
                (OverseasEntityDueDiligenceDto) filing.getData().get(OVERSEAS_ENTITY_DUE_DILIGENCE);
        assertEquals(OverseasEntityDueDiligenceMock.IDENTITY_DATE, oeDueDiligenceDtoInFiling.getIdentityDate());
        assertEquals(OverseasEntityDueDiligenceMock.NAME, oeDueDiligenceDtoInFiling.getName());
        assertEquals(EMAIL_WITHOUT_LEADING_AND_TRAILING_SPACES, oeDueDiligenceDtoInFiling.getEmail());
        assertEquals(OverseasEntityDueDiligenceMock.SUPERVISOR_NAME, oeDueDiligenceDtoInFiling.getSupervisoryName());
        assertEquals(OverseasEntityDueDiligenceMock.AML_NUMBER, oeDueDiligenceDtoInFiling.getAmlNumber());
        assertEquals(OverseasEntityDueDiligenceMock.PARTNER_NAME, oeDueDiligenceDtoInFiling.getPartnerName());
        checkAddress(oeDueDiligenceDtoInFiling.getAddress());
    }

    private void checkAddress(AddressDto addressDto) {
        assertEquals(AddressMock.PROPERTY_NAME_NUMBER, addressDto.getPropertyNameNumber());
        assertEquals(AddressMock.LINE1, addressDto.getLine1());
        assertEquals(AddressMock.LINE2, addressDto.getLine2());
        assertEquals(AddressMock.TOWN, addressDto.getTown());
        assertEquals(AddressMock.COUNTY, addressDto.getCounty());
        assertEquals(AddressMock.COUNTRY, addressDto.getCountry());
        assertEquals(AddressMock.POST_CODE, addressDto.getPostcode());
    }

    @Test
    void testFilingGenerationForPaymentWhenSuccessful() throws SubmissionNotFoundException, ServiceException, IOException, URIValidationException {
        initTransactionPaymentLinkMocks();
        initGetPaymentMocks();
        when(localDateSupplier.get()).thenReturn(DUMMY_DATE);
        ReflectionTestUtils.setField(filingsService, "filingDescriptionIdentifier", FILING_DESCRIPTION_IDENTIFIER);
        ReflectionTestUtils.setField(filingsService, "registrationFilingDescription", FILING_DESCRIPTION);
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDto();
        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.of(overseasEntitySubmissionDto);
        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);

        FilingApi filing = filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction, PASS_THROUGH_HEADER);

        assertEquals(PAYMENT_METHOD, filing.getData().get("payment_method"));
        assertEquals(PAYMENT_REFERENCE, filing.getData().get("payment_reference"));
    }

    @Test
    void testThrowsServiceExceptionWhenUnableToGetPayment() throws IOException, URIValidationException {
        initTransactionPaymentLinkMocks();

        when(apiClient.payment()).thenReturn(paymentResourceHandler);
        when(paymentResourceHandler.get(anyString())).thenReturn(paymentGet);
        when(paymentGet.execute()).thenThrow(getApiErrorResponseException());

        OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDto();
        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.of(overseasEntitySubmissionDto);
        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);

        ServiceException serviceEx = assertThrows(ServiceException.class, () -> filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction, PASS_THROUGH_HEADER));
        assertEquals(ERROR_MESSAGE, serviceEx.getMessage());
    }

    @Test
    void testThrowsServiceExceptionWhenUnableToGetPaymentReference() throws IOException, URIValidationException {
        when(apiClientService.getOauthAuthenticatedClient(PASS_THROUGH_HEADER)).thenReturn(apiClient);
        when(apiClient.transactions()).thenReturn(transactionsResourceHandler);
        when(transactionsResourceHandler.getPayment(anyString())).thenReturn(transactionsPaymentGet);
        when(transactionsPaymentGet.execute()).thenThrow(getApiErrorResponseException());

        OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDto();
        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.of(overseasEntitySubmissionDto);
        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);

        ServiceException serviceEx = assertThrows(ServiceException.class, () -> filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction, PASS_THROUGH_HEADER));
        assertEquals(ERROR_MESSAGE, serviceEx.getMessage());
    }

    private ApiErrorResponseException getApiErrorResponseException() {
        return new ApiErrorResponseException(
                new HttpResponseException.Builder(401, "unauthorised", new HttpHeaders())
                        .setMessage(ERROR_MESSAGE));
    }

    @Test
    void testFilingGenerationWhenSuccessfulWithoutTrustsAndWithIdentityChecksWithTrustFeatureFlag() throws SubmissionNotFoundException, ServiceException, IOException, URIValidationException {
        
        setValidationEnabledFeatureFlag(true);

        initTransactionPaymentLinkMocks();
        initGetPaymentMocks();
        when(localDateSupplier.get()).thenReturn(DUMMY_DATE);
        ReflectionTestUtils.setField(filingsService, "filingDescriptionIdentifier", FILING_DESCRIPTION_IDENTIFIER);
        ReflectionTestUtils.setField(filingsService, "registrationFilingDescription", FILING_DESCRIPTION);
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDto();
        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.of(overseasEntitySubmissionDto);
        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);

        FilingApi filing = filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction, PASS_THROUGH_HEADER);

        verify(localDateSupplier, times(1)).get();
        assertEquals(FILING_KIND_OVERSEAS_ENTITY, filing.getKind());
        assertEquals(FILING_DESCRIPTION_IDENTIFIER, filing.getDescriptionIdentifier());
        assertEquals("Filing Description with registration date 26 March 2022", filing.getDescription());
        assertEquals("Joe Bloggs Ltd", filing.getData().get("entity_name"));
        final PresenterDto presenterInFiling = (PresenterDto)filing.getData().get("presenter");
        assertEquals("Joe Bloggs", presenterInFiling.getFullName());
        assertEquals("user@domain.roe", presenterInFiling.getEmail());
        final EntityDto entityInFiling = ((EntityDto) filing.getData().get("entity"));
        assertEquals("Eutopia", entityInFiling.getIncorporationCountry());

        checkDueDiligence(filing);
        checkOverseasEntityDueDiligence(filing);
        checkTrustDataIsEmptyWithTrustFeatureFlagOn(filing);
        checkBeneficialOwners(filing);
        checkManagingOfficers(filing);
    }

    @Test
    void testFilingGenerationWhenSuccessfulWithBOIndividualTrustAndWithIdentityChecksWithTrustFeatureFlagOn()
            throws SubmissionNotFoundException, ServiceException, IOException, URIValidationException, JSONException {

        setValidationEnabledFeatureFlag(true);

        initTransactionPaymentLinkMocks();
        initGetPaymentMocks();
        when(localDateSupplier.get()).thenReturn(DUMMY_DATE);
        ReflectionTestUtils.setField(filingsService, "filingDescriptionIdentifier", FILING_DESCRIPTION_IDENTIFIER);
        ReflectionTestUtils.setField(filingsService, "registrationFilingDescription", FILING_DESCRIPTION);
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDtoWithBoIndividualTrust();
        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.of(overseasEntitySubmissionDto);
        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);

        FilingApi filing = filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction, PASS_THROUGH_HEADER);

        verify(localDateSupplier, times(1)).get();
        assertEquals(FILING_KIND_OVERSEAS_ENTITY, filing.getKind());
        assertEquals(FILING_DESCRIPTION_IDENTIFIER, filing.getDescriptionIdentifier());
        assertEquals("Filing Description with registration date 26 March 2022", filing.getDescription());
        assertEquals("Joe Bloggs Ltd", filing.getData().get("entity_name"));
        final PresenterDto presenterInFiling = (PresenterDto)filing.getData().get("presenter");
        assertEquals("Joe Bloggs", presenterInFiling.getFullName());
        assertEquals("user@domain.roe", presenterInFiling.getEmail());
        final EntityDto entityInFiling = ((EntityDto) filing.getData().get("entity"));
        assertEquals("Eutopia", entityInFiling.getIncorporationCountry());

        checkDueDiligence(filing);
        checkOverseasEntityDueDiligence(filing);
        checkTrustDataIndividualWithTrustFeatureFlagOn(filing, 0, "1");
        checkBeneficialOwners(filing);
        checkManagingOfficers(filing);
    }

    @Test
    void testFilingGenerationWhenSuccessfulWithThreeBOIndividualTrustsAndWithIdentityChecksWithTrustFeatureFlagOn()
            throws SubmissionNotFoundException, ServiceException, IOException, URIValidationException, JSONException {

        setValidationEnabledFeatureFlag(true);

        initTransactionPaymentLinkMocks();
        initGetPaymentMocks();
        when(localDateSupplier.get()).thenReturn(DUMMY_DATE);
        ReflectionTestUtils.setField(filingsService, "filingDescriptionIdentifier", FILING_DESCRIPTION_IDENTIFIER);
        ReflectionTestUtils.setField(filingsService, "registrationFilingDescription", FILING_DESCRIPTION);
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDtoWithThreeBoIndividualTrusts();
        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.of(overseasEntitySubmissionDto);
        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);

        FilingApi filing = filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction, PASS_THROUGH_HEADER);

        verify(localDateSupplier, times(1)).get();
        assertEquals(FILING_KIND_OVERSEAS_ENTITY, filing.getKind());
        assertEquals(FILING_DESCRIPTION_IDENTIFIER, filing.getDescriptionIdentifier());
        assertEquals("Filing Description with registration date 26 March 2022", filing.getDescription());
        assertEquals("Joe Bloggs Ltd", filing.getData().get("entity_name"));
        final PresenterDto presenterInFiling = (PresenterDto)filing.getData().get("presenter");
        assertEquals("Joe Bloggs", presenterInFiling.getFullName());
        assertEquals("user@domain.roe", presenterInFiling.getEmail());
        final EntityDto entityInFiling = ((EntityDto) filing.getData().get("entity"));
        assertEquals("Eutopia", entityInFiling.getIncorporationCountry());

        checkDueDiligence(filing);
        checkOverseasEntityDueDiligence(filing);

        checkTrustDataIndividualWithTrustFeatureFlagOn(filing, 0, "1");
        checkTrustDataIndividualWithTrustFeatureFlagOn(filing, 1, "2");
        checkTrustDataIndividualWithTrustFeatureFlagOn(filing, 2, "3");
    }

    @Test
    void testFilingGenerationWhenSuccessfulWithBOCorporateTrustAndWithIdentityChecksWithTrustFeatureFlagOn()
            throws SubmissionNotFoundException, ServiceException, IOException, URIValidationException, JSONException {

        setValidationEnabledFeatureFlag(true);

        initTransactionPaymentLinkMocks();
        initGetPaymentMocks();
        when(localDateSupplier.get()).thenReturn(DUMMY_DATE);
        ReflectionTestUtils.setField(filingsService, "filingDescriptionIdentifier", FILING_DESCRIPTION_IDENTIFIER);
        ReflectionTestUtils.setField(filingsService, "registrationFilingDescription", FILING_DESCRIPTION);
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDtoWithBoCorporateTrust();
        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.of(overseasEntitySubmissionDto);
        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);

        FilingApi filing = filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction, PASS_THROUGH_HEADER);

        verify(localDateSupplier, times(1)).get();
        assertEquals(FILING_KIND_OVERSEAS_ENTITY, filing.getKind());
        assertEquals(FILING_DESCRIPTION_IDENTIFIER, filing.getDescriptionIdentifier());
        assertEquals("Filing Description with registration date 26 March 2022", filing.getDescription());
        assertEquals("Joe Bloggs Ltd", filing.getData().get("entity_name"));
        final PresenterDto presenterInFiling = (PresenterDto)filing.getData().get("presenter");
        assertEquals("Joe Bloggs", presenterInFiling.getFullName());
        assertEquals("user@domain.roe", presenterInFiling.getEmail());
        final EntityDto entityInFiling = ((EntityDto) filing.getData().get("entity"));
        assertEquals("Eutopia", entityInFiling.getIncorporationCountry());

        checkDueDiligence(filing);
        checkOverseasEntityDueDiligence(filing);
        checkTrustDataCorporateWithTrustFeatureFlagOn(filing, 0, "1");
        checkBeneficialOwners(filing);
        checkManagingOfficers(filing);
    }

    @Test
    void testFilingGenerationWhenSuccessfulWithThreeBOCorporateTrustAndWithIdentityChecksWithTrustFeatureFlagOn()
            throws SubmissionNotFoundException, ServiceException, IOException, URIValidationException, JSONException {

        setValidationEnabledFeatureFlag(true);

        initTransactionPaymentLinkMocks();
        initGetPaymentMocks();
        when(localDateSupplier.get()).thenReturn(DUMMY_DATE);
        ReflectionTestUtils.setField(filingsService, "filingDescriptionIdentifier", FILING_DESCRIPTION_IDENTIFIER);
        ReflectionTestUtils.setField(filingsService, "registrationFilingDescription", FILING_DESCRIPTION);
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDtoWithThreeBoCorporateTrusts();
        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.of(overseasEntitySubmissionDto);
        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);

        FilingApi filing = filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction, PASS_THROUGH_HEADER);

        verify(localDateSupplier, times(1)).get();
        assertEquals(FILING_KIND_OVERSEAS_ENTITY, filing.getKind());
        assertEquals(FILING_DESCRIPTION_IDENTIFIER, filing.getDescriptionIdentifier());
        assertEquals("Filing Description with registration date 26 March 2022", filing.getDescription());
        assertEquals("Joe Bloggs Ltd", filing.getData().get("entity_name"));
        final PresenterDto presenterInFiling = (PresenterDto)filing.getData().get("presenter");
        assertEquals("Joe Bloggs", presenterInFiling.getFullName());
        assertEquals("user@domain.roe", presenterInFiling.getEmail());
        final EntityDto entityInFiling = ((EntityDto) filing.getData().get("entity"));
        assertEquals("Eutopia", entityInFiling.getIncorporationCountry());

        checkDueDiligence(filing);
        checkOverseasEntityDueDiligence(filing);

        checkTrustDataCorporateWithTrustFeatureFlagOn(filing, 0, "1");
        checkTrustDataCorporateWithTrustFeatureFlagOn(filing, 1, "2");
        checkTrustDataCorporateWithTrustFeatureFlagOn(filing, 2, "3");
    }

    @Test
    void testFilingGenerationWhenSuccessfulWithThreeBOCorporateTrustAndThreeBOIndividualTrustAndWithIdentityChecksWithTrustFeatureFlagOn()
            throws SubmissionNotFoundException, ServiceException, IOException, URIValidationException, JSONException {

        setValidationEnabledFeatureFlag(true);

        initTransactionPaymentLinkMocks();
        initGetPaymentMocks();
        when(localDateSupplier.get()).thenReturn(DUMMY_DATE);
        ReflectionTestUtils.setField(filingsService, "filingDescriptionIdentifier", FILING_DESCRIPTION_IDENTIFIER);
        ReflectionTestUtils.setField(filingsService, "registrationFilingDescription", FILING_DESCRIPTION);
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDtoWithThreeBoCorporateTrustsAndThreeIndividualTrust();
        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.of(overseasEntitySubmissionDto);
        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);

        FilingApi filing = filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction, PASS_THROUGH_HEADER);

        verify(localDateSupplier, times(1)).get();
        assertEquals(FILING_KIND_OVERSEAS_ENTITY, filing.getKind());
        assertEquals(FILING_DESCRIPTION_IDENTIFIER, filing.getDescriptionIdentifier());
        assertEquals("Filing Description with registration date 26 March 2022", filing.getDescription());
        assertEquals("Joe Bloggs Ltd", filing.getData().get("entity_name"));
        final PresenterDto presenterInFiling = (PresenterDto)filing.getData().get("presenter");
        assertEquals("Joe Bloggs", presenterInFiling.getFullName());
        assertEquals("user@domain.roe", presenterInFiling.getEmail());
        final EntityDto entityInFiling = ((EntityDto) filing.getData().get("entity"));
        assertEquals("Eutopia", entityInFiling.getIncorporationCountry());

        checkDueDiligence(filing);
        checkOverseasEntityDueDiligence(filing);

        checkTrustDataCorporateWithTrustFeatureFlagOn(filing, 0, "1");
        checkTrustDataCorporateWithTrustFeatureFlagOn(filing, 1, "2");
        checkTrustDataCorporateWithTrustFeatureFlagOn(filing, 2, "3");

        checkTrustDataIndividualWithTrustFeatureFlagOn(filing, 0, "1");
        checkTrustDataIndividualWithTrustFeatureFlagOn(filing, 1, "2");
        checkTrustDataIndividualWithTrustFeatureFlagOn(filing, 2, "3");
    }

    @Test
    void testFilingGenerationWhenSuccessfulWithOneBOIndividualWithThreeTrustsAndWithIdentityChecksWithTrustFeatureFlagOn()
            throws SubmissionNotFoundException, ServiceException, IOException, URIValidationException, JSONException {

        setValidationEnabledFeatureFlag(true);

        initTransactionPaymentLinkMocks();
        initGetPaymentMocks();
        when(localDateSupplier.get()).thenReturn(DUMMY_DATE);
        ReflectionTestUtils.setField(filingsService, "filingDescriptionIdentifier", FILING_DESCRIPTION_IDENTIFIER);
        ReflectionTestUtils.setField(filingsService, "registrationFilingDescription", FILING_DESCRIPTION);
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDtoWithBoIndividualWithThreeTrusts();

        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.of(overseasEntitySubmissionDto);
        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);

        FilingApi filing = filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction, PASS_THROUGH_HEADER);

        verify(localDateSupplier, times(1)).get();
        assertEquals(FILING_KIND_OVERSEAS_ENTITY, filing.getKind());
        assertEquals(FILING_DESCRIPTION_IDENTIFIER, filing.getDescriptionIdentifier());
        assertEquals("Filing Description with registration date 26 March 2022", filing.getDescription());
        assertEquals("Joe Bloggs Ltd", filing.getData().get("entity_name"));
        final PresenterDto presenterInFiling = (PresenterDto)filing.getData().get("presenter");
        assertEquals("Joe Bloggs", presenterInFiling.getFullName());
        assertEquals("user@domain.roe", presenterInFiling.getEmail());
        final EntityDto entityInFiling = ((EntityDto) filing.getData().get("entity"));
        assertEquals("Eutopia", entityInFiling.getIncorporationCountry());

        checkDueDiligence(filing);
        checkOverseasEntityDueDiligence(filing);
        checkTrustDataIndividualWithThreeTrustsWithTrustFeatureFlagOn(filing);
        checkBeneficialOwners(filing);
        checkManagingOfficers(filing);
    }

    @Test
    void testFilingGenerationWhenSuccessfulWithOneBOCorporateWithThreeTrustsAndWithIdentityChecksWithTrustFeatureFlagOn()
            throws SubmissionNotFoundException, ServiceException, IOException, URIValidationException, JSONException {

        setValidationEnabledFeatureFlag(true);

        initTransactionPaymentLinkMocks();
        initGetPaymentMocks();
        when(localDateSupplier.get()).thenReturn(DUMMY_DATE);
        ReflectionTestUtils.setField(filingsService, "filingDescriptionIdentifier", FILING_DESCRIPTION_IDENTIFIER);
        ReflectionTestUtils.setField(filingsService, "registrationFilingDescription", FILING_DESCRIPTION);
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDtoWithBoCorporateWithThreeTrusts();

        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.of(overseasEntitySubmissionDto);
        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);

        FilingApi filing = filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction, PASS_THROUGH_HEADER);

        verify(localDateSupplier, times(1)).get();
        assertEquals(FILING_KIND_OVERSEAS_ENTITY, filing.getKind());
        assertEquals(FILING_DESCRIPTION_IDENTIFIER, filing.getDescriptionIdentifier());
        assertEquals("Filing Description with registration date 26 March 2022", filing.getDescription());
        assertEquals("Joe Bloggs Ltd", filing.getData().get("entity_name"));
        final PresenterDto presenterInFiling = (PresenterDto)filing.getData().get("presenter");
        assertEquals("Joe Bloggs", presenterInFiling.getFullName());
        assertEquals("user@domain.roe", presenterInFiling.getEmail());
        final EntityDto entityInFiling = ((EntityDto) filing.getData().get("entity"));
        assertEquals("Eutopia", entityInFiling.getIncorporationCountry());

        checkDueDiligence(filing);
        checkOverseasEntityDueDiligence(filing);
        checkTrustDataCorporateWithThreeTrustsWithTrustFeatureFlagOn(filing);
        checkBeneficialOwners(filing);
        checkManagingOfficers(filing);
    }

    @Test
    void testPublicAndPrivateDataServicesAreCalled() throws SubmissionNotFoundException, ServiceException, IOException, URIValidationException {
        initTransactionPaymentLinkMocks();
        initGetPaymentMocks();
        initGetPublicPrivateDataCombinerMocks();

        when(localDateSupplier.get()).thenReturn(DUMMY_DATE);
        ReflectionTestUtils.setField(filingsService, "filingDescriptionIdentifier", FILING_DESCRIPTION_IDENTIFIER);
        ReflectionTestUtils.setField(filingsService, "updateFilingDescription", UPDATE_FILING_DESCRIPTION);
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = Mocks.buildSubmissionDto();
        overseasEntitySubmissionDto.setEntityNumber("OE111229");
        overseasEntitySubmissionDto.setTrusts(new ArrayList<>());
        Optional<OverseasEntitySubmissionDto> submissionOpt = Optional.of(overseasEntitySubmissionDto);
        when(overseasEntitiesService.getOverseasEntitySubmission(OVERSEAS_ENTITY_ID)).thenReturn(submissionOpt);

        filingsService.generateOverseasEntityFiling(OVERSEAS_ENTITY_ID, transaction, PASS_THROUGH_HEADER);
        verify(beneficialOwnerCessationService, times(1)).beneficialOwnerCessations(Mockito.any(), Mockito.any(), Mockito.any());
    }

    private void setValidationEnabledFeatureFlag(boolean value) {
        ReflectionTestUtils.setField(filingsService, "isTrustsSubmissionThroughWebEnabled", value);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testManagingOfficerCessationsServiceIsCalled() {
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = mock(OverseasEntitySubmissionDto.class);
        Map<String, Pair<CompanyOfficerApi, ManagingOfficerDataApi>> combinedMoData = mock(Map.class);
        Map<String, Object> logMap = mock(Map.class);

        List<Cessation> cessations = managingOfficerCessationService.managingOfficerCessations(overseasEntitySubmissionDto, combinedMoData, logMap);

        assertNotNull(cessations);
    }

}
