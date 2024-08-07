package uk.gov.companieshouse.overseasentitiesapi.service;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.company.CompanyResourceHandler;
import uk.gov.companieshouse.api.handler.company.request.CompanyGet;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.handler.officers.OfficersResourceHandler;
import uk.gov.companieshouse.api.handler.officers.request.OfficersList;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.officers.CompanyOfficerApi;
import uk.gov.companieshouse.api.model.officers.OfficersApi;
import uk.gov.companieshouse.api.handler.psc.PscsResourceHandler;
import uk.gov.companieshouse.api.handler.psc.request.PscsList;
import uk.gov.companieshouse.api.model.psc.PscApi;
import uk.gov.companieshouse.api.model.psc.PscsApi;
import uk.gov.companieshouse.overseasentitiesapi.client.ApiClientService;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PublicDataRetrievalServiceTest {
    private static final String COMPANY_REFERENCE = "12345234";

    private static final String PASS_THROUGH_HEADER = "432342353255";
    private static final String ERROR_MESSAGE = "ERROR";

    @InjectMocks
    private PublicDataRetrievalService publicDataRetrievalService;

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ApiClient apiClient;

    @Mock
    private ApiResponse<CompanyProfileApi> apiGetResponseCompanyProfile;

    @Mock
    private ApiResponse<OfficersApi> apiGetResponseOfficers;
    
    @Mock
    private ApiResponse<PscsApi> apiGetResponsePscs;

    @Mock
    private CompanyResourceHandler company;

    @Mock
    private CompanyGet companyGet;

    @Mock
    private OfficersResourceHandler officers;

    @Mock
    private OfficersList officersList;

    @Mock
    private PscsList pscsList;

    @Mock
    private PscsResourceHandler pscs;

    @BeforeEach
    public void init() throws IOException {
        when(apiClientService.getOauthAuthenticatedClient(PASS_THROUGH_HEADER)).thenReturn(apiClient);

        when(apiClient.company()).thenReturn(company);
        when(company.get(Mockito.anyString())).thenReturn(companyGet);
    }

    private void retrievePublicData() throws ServiceException{
        publicDataRetrievalService.getCompanyProfile(COMPANY_REFERENCE, PASS_THROUGH_HEADER);
        publicDataRetrievalService.getOfficers(COMPANY_REFERENCE, PASS_THROUGH_HEADER);
        publicDataRetrievalService.getPSCs(COMPANY_REFERENCE, PASS_THROUGH_HEADER);
    }

    @Test
    void testPublicDataIsSuccessful() throws IOException, URIValidationException, ServiceException {
        var companyProfileApi = new CompanyProfileApi();
        var officersApi = new OfficersApi();
        var companyOfficerApi = new CompanyOfficerApi();
        var pscsApi = new PscsApi();
        var companyPSCsApi = new PscApi();

        companyOfficerApi.setName("Tim Bill");
        officersApi.setItems(List.of(companyOfficerApi));

        companyPSCsApi.setName("Test Name");
        pscsApi.setItems(List.of(companyPSCsApi));

        when(companyGet.execute()).thenReturn(apiGetResponseCompanyProfile);
        when(apiGetResponseCompanyProfile.getData()).thenReturn(companyProfileApi);

        when(apiClient.officers()).thenReturn(officers);
        when(officers.list(Mockito.anyString())).thenReturn(officersList);
        when(officersList.execute()).thenReturn(apiGetResponseOfficers);


        when(apiClient.pscs()).thenReturn(pscs);
        when(pscs.list(Mockito.anyString())).thenReturn(pscsList);
        when(pscsList.execute()).thenReturn(apiGetResponsePscs);

        when(apiGetResponsePscs.getData()).thenReturn(pscsApi);
        when(apiGetResponseOfficers.getData()).thenReturn(officersApi);

        retrievePublicData();
        verify(apiClientService, times(3)).getOauthAuthenticatedClient(PASS_THROUGH_HEADER);
    }

    @Test
    void testPublicDataOfficersDataNotFound() throws IOException, URIValidationException, ServiceException {
        var companyProfileApi = new CompanyProfileApi();
        var pscsApi = new PscsApi();
        var companyPSCsApi = new PscApi();
        var officersApi = new OfficersApi();
        var companyOfficerApi = new CompanyOfficerApi();
        var officersApiResponse = new ApiResponse<OfficersApi>(404, null);

        companyPSCsApi.setName("Test Name");
        pscsApi.setItems(List.of(companyPSCsApi));

        companyOfficerApi.setName("Tim Bill");
        officersApi.setItems(List.of(companyOfficerApi));

        when(companyGet.execute()).thenReturn(apiGetResponseCompanyProfile);
        when(apiGetResponseCompanyProfile.getData()).thenReturn(companyProfileApi);

        when(apiClient.pscs()).thenReturn(pscs);
        when(pscs.list(Mockito.anyString())).thenReturn(pscsList);
        when(pscsList.execute()).thenReturn(apiGetResponsePscs);
        when(apiGetResponsePscs.getData()).thenReturn(pscsApi);

        when(apiClient.officers()).thenReturn(officers);
        when(officers.list(Mockito.anyString())).thenReturn(officersList);
        when(officersList.execute()).thenReturn(officersApiResponse);

        retrievePublicData();
        verify(apiClientService, times(3)).getOauthAuthenticatedClient(PASS_THROUGH_HEADER);
    }

    @Test
    void testGetOverseasEntityPublicPscsNotFound() throws IOException, URIValidationException, ServiceException {
        var companyProfileApi = new CompanyProfileApi();
        var officersApi = new OfficersApi();
        var companyOfficerApi = new CompanyOfficerApi();
        var pscsApi = new PscsApi();
        var companyPSCsApi = new PscApi();
        var pscsApiResponse = new ApiResponse<PscsApi>(404, null);

        companyOfficerApi.setName("Tim Bill");
        officersApi.setItems(List.of(companyOfficerApi));

        companyPSCsApi.setName("Test name");
        pscsApi.setItems(List.of(companyPSCsApi));

        when(companyGet.execute()).thenReturn(apiGetResponseCompanyProfile);
        when(apiGetResponseCompanyProfile.getData()).thenReturn(companyProfileApi);

        when(apiClient.officers()).thenReturn(officers);
        when(officers.list(Mockito.anyString())).thenReturn(officersList);
        when(officersList.execute()).thenReturn(apiGetResponseOfficers);
        when(apiGetResponseOfficers.getData()).thenReturn(officersApi);

        when(apiClient.pscs()).thenReturn(pscs);
        when(pscs.list(Mockito.anyString())).thenReturn(pscsList);
        when(pscsList.execute()).thenReturn(pscsApiResponse);

        retrievePublicData();
        verify(apiClientService, times(3)).getOauthAuthenticatedClient(PASS_THROUGH_HEADER);
    }

    @Test
    void testServiceExceptionThrownWhenPublicDataForCompanyProfileDataThrowsURIValidationException() throws IOException, URIValidationException {
        when(companyGet.execute()).thenThrow(new URIValidationException(ERROR_MESSAGE));

        assertThrows(ServiceException.class, () -> publicDataRetrievalService.getCompanyProfile(COMPANY_REFERENCE, PASS_THROUGH_HEADER));
    }

    @Test
    void testServiceExceptionThrownWhenPublicDataForCompanyProfileDataThrowsIOException() throws IOException, URIValidationException {
        when(companyGet.execute()).thenThrow(ApiErrorResponseException.fromIOException(new IOException(ERROR_MESSAGE)));

        assertThrows(ServiceException.class, () -> publicDataRetrievalService.getCompanyProfile(COMPANY_REFERENCE, PASS_THROUGH_HEADER));
    }

    @Test
    void testPublicDataReturnsNoOfficersData() throws IOException, URIValidationException, ServiceException {
        var companyProfileApi = new CompanyProfileApi();

        when(companyGet.execute()).thenReturn(apiGetResponseCompanyProfile);
        when(apiGetResponseCompanyProfile.getData()).thenReturn(companyProfileApi);

        when(apiClient.officers()).thenReturn(officers);
        when(officers.list(Mockito.anyString())).thenReturn(officersList);

        when(officersList.execute()).thenThrow(ApiErrorResponseException.fromHttpResponseException(
                new HttpResponseException.Builder(404, "not found", new HttpHeaders()).setMessage(ERROR_MESSAGE).build()));

        when(apiClient.pscs()).thenReturn(pscs);
        when(pscs.list(Mockito.anyString())).thenReturn(pscsList);
        when(pscsList.execute()).thenReturn(apiGetResponsePscs);

        retrievePublicData();
        verify(apiClientService, times(3)).getOauthAuthenticatedClient(PASS_THROUGH_HEADER);
    }

    @Test
    void testPublicDataReturnsNoPSCsData() throws IOException, URIValidationException, ServiceException {
        var companyProfileApi = new CompanyProfileApi();

        when(companyGet.execute()).thenReturn(apiGetResponseCompanyProfile);
        when(apiGetResponseCompanyProfile.getData()).thenReturn(companyProfileApi);

        when(apiClient.officers()).thenReturn(officers);
        when(officers.list(Mockito.anyString())).thenReturn(officersList);
        when(officersList.execute()).thenReturn(apiGetResponseOfficers);

        when(apiClient.pscs()).thenReturn(pscs);
        when(pscs.list(Mockito.anyString())).thenReturn(pscsList);
        when(pscsList.execute()).thenThrow(ApiErrorResponseException.fromHttpResponseException(
                new HttpResponseException.Builder(404, "not found", new HttpHeaders()).setMessage(ERROR_MESSAGE).build()));

        retrievePublicData();
        verify(apiClientService, times(3)).getOauthAuthenticatedClient(PASS_THROUGH_HEADER);
    }

    @Test
    void testServiceExceptionThrownWhenPublicDataForOfficersDataThrowsURIValidationException() throws IOException, URIValidationException {
        var companyProfileApi = new CompanyProfileApi();

        when(companyGet.execute()).thenReturn(apiGetResponseCompanyProfile);
        when(apiGetResponseCompanyProfile.getData()).thenReturn(companyProfileApi);

        when(apiClient.officers()).thenReturn(officers);
        when(officers.list(Mockito.anyString())).thenReturn(officersList);
        when(officersList.execute()).thenThrow(new URIValidationException("ERROR"));

        assertThrows(ServiceException.class, this::retrievePublicData);
    }

    @Test
    void testServiceExceptionThrownWhenPublicDataForOfficersDataThrowsIOException() throws IOException, URIValidationException {
        var companyProfileApi = new CompanyProfileApi();

        when(companyGet.execute()).thenReturn(apiGetResponseCompanyProfile);
        when(apiGetResponseCompanyProfile.getData()).thenReturn(companyProfileApi);

        when(apiClient.officers()).thenReturn(officers);
        when(officers.list(Mockito.anyString())).thenReturn(officersList);
        when(officersList.execute()).thenThrow(ApiErrorResponseException.fromIOException(new IOException("ERROR")));

        assertThrows(ServiceException.class, this::retrievePublicData);
    }

    @Test
    void testServiceExceptionThrownWhenPublicDataForPscsDataThrowsIOException() throws IOException, URIValidationException {
        var companyProfileApi = new CompanyProfileApi();
        var officersApi = new OfficersApi();
        var companyOfficerApi = new CompanyOfficerApi();

        companyOfficerApi.setName("Tim Bill");
        officersApi.setItems(List.of(companyOfficerApi));

        when(companyGet.execute()).thenReturn(apiGetResponseCompanyProfile);
        when(apiGetResponseCompanyProfile.getData()).thenReturn(companyProfileApi);

        when(apiClient.officers()).thenReturn(officers);
        when(officers.list(Mockito.anyString())).thenReturn(officersList);
        when(officersList.execute()).thenReturn(apiGetResponseOfficers);
        when(apiGetResponseOfficers.getData()).thenReturn(officersApi);

        when(apiClient.pscs()).thenReturn(pscs);
        when(pscs.list(Mockito.anyString())).thenReturn(pscsList);
        when(pscsList.execute()).thenThrow(ApiErrorResponseException.fromIOException(new IOException("ERROR")));

        assertThrows(ServiceException.class, this::retrievePublicData);
    }

    @Test
    void testServiceExceptionThrownWhenPublicDataForPscsDataThrowsURIValidationException() throws IOException, URIValidationException {
        var companyProfileApi = new CompanyProfileApi();
        var officersApi = new OfficersApi();
        var companyOfficerApi = new CompanyOfficerApi();

        companyOfficerApi.setName("Tim Bill");
        officersApi.setItems(List.of(companyOfficerApi));

        when(companyGet.execute()).thenReturn(apiGetResponseCompanyProfile);
        when(apiGetResponseCompanyProfile.getData()).thenReturn(companyProfileApi);

        when(apiClient.officers()).thenReturn(officers);
        when(officers.list(Mockito.anyString())).thenReturn(officersList);
        when(officersList.execute()).thenReturn(apiGetResponseOfficers);
        when(apiGetResponseOfficers.getData()).thenReturn(officersApi);

        when(apiClient.pscs()).thenReturn(pscs);
        when(pscs.list(Mockito.anyString())).thenReturn(pscsList);
        when(pscsList.execute()).thenThrow(new URIValidationException("ERROR"));

        assertThrows(ServiceException.class, this::retrievePublicData);
    }
}
