package uk.gov.companieshouse.overseasentitiesapi.service;

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
    private CompanyResourceHandler company;

    @Mock
    private CompanyGet companyGet;

    @Mock
    private OfficersResourceHandler officers;

    @Mock
    private OfficersList officersList;

    @BeforeEach
    public void init() throws IOException {
        when(apiClientService.getOauthAuthenticatedClient(PASS_THROUGH_HEADER)).thenReturn(apiClient);

        when(apiClient.company()).thenReturn(company);
        when(company.get(Mockito.anyString())).thenReturn(companyGet);
    }

    @Test
    void testInitialisePublicDataIsSuccessful() throws IOException, URIValidationException, ServiceException {
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

        publicDataRetrievalService.initialisePublicData(COMPANY_REFERENCE, PASS_THROUGH_HEADER);
        verify(apiClientService, times(2)).getOauthAuthenticatedClient(PASS_THROUGH_HEADER);
    }

    @Test
    void testInitialisePublicDataOfficersDataNotFound() throws IOException, URIValidationException, ServiceException {
        var companyProfileApi = new CompanyProfileApi();
        var officersApi = new OfficersApi();
        var companyOfficerApi = new CompanyOfficerApi();
        var officersApiResponse = new ApiResponse<OfficersApi>(404, null);
        companyOfficerApi.setName("Tim Bill");
        officersApi.setItems(List.of(companyOfficerApi));

        when(companyGet.execute()).thenReturn(apiGetResponseCompanyProfile);
        when(apiGetResponseCompanyProfile.getData()).thenReturn(companyProfileApi);

        when(apiClient.officers()).thenReturn(officers);
        when(officers.list(Mockito.anyString())).thenReturn(officersList);
        when(officersList.execute()).thenReturn(officersApiResponse);

        publicDataRetrievalService.initialisePublicData(COMPANY_REFERENCE, PASS_THROUGH_HEADER);
        verify(apiClientService, times(2)).getOauthAuthenticatedClient(PASS_THROUGH_HEADER);
    }

    @Test
    void testServiceExceptionThrownWhenInitialisePublicDataForCompanyProfileDataThrowsURIValidationException() throws IOException, URIValidationException {
        when(companyGet.execute()).thenThrow(new URIValidationException("ERROR"));

        assertThrows(ServiceException.class, () -> {
            publicDataRetrievalService.initialisePublicData(COMPANY_REFERENCE, PASS_THROUGH_HEADER);
        });
    }

    @Test
    void testServiceExceptionThrownWhenInitialisePublicDataForCompanyProfileDataThrowsIOException() throws IOException, URIValidationException {
        when(companyGet.execute()).thenThrow(ApiErrorResponseException.fromIOException(new IOException("ERROR")));

        assertThrows(ServiceException.class, () -> {
            publicDataRetrievalService.initialisePublicData(COMPANY_REFERENCE, PASS_THROUGH_HEADER);
        });
    }

    @Test
    void testServiceExceptionThrownWhenInitialisePublicDataForOfficersDataThrowsURIValidationException() throws IOException, URIValidationException {
        var companyProfileApi = new CompanyProfileApi();

        when(companyGet.execute()).thenReturn(apiGetResponseCompanyProfile);
        when(apiGetResponseCompanyProfile.getData()).thenReturn(companyProfileApi);

        when(apiClient.officers()).thenReturn(officers);
        when(officers.list(Mockito.anyString())).thenReturn(officersList);
        when(officersList.execute()).thenThrow(new URIValidationException("ERROR"));

        assertThrows(ServiceException.class, () -> {
            publicDataRetrievalService.initialisePublicData(COMPANY_REFERENCE, PASS_THROUGH_HEADER);
        });
    }

    @Test
    void testServiceExceptionThrownWhenInitialisePublicDataForOfficersDataThrowsIOException() throws IOException, URIValidationException {
        var companyProfileApi = new CompanyProfileApi();

        when(companyGet.execute()).thenReturn(apiGetResponseCompanyProfile);
        when(apiGetResponseCompanyProfile.getData()).thenReturn(companyProfileApi);

        when(apiClient.officers()).thenReturn(officers);
        when(officers.list(Mockito.anyString())).thenReturn(officersList);
        when(officersList.execute()).thenThrow(ApiErrorResponseException.fromIOException(new IOException("ERROR")));

        assertThrows(ServiceException.class, () -> {
            publicDataRetrievalService.initialisePublicData(COMPANY_REFERENCE, PASS_THROUGH_HEADER);
        });
    }
}
