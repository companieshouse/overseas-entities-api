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
import uk.gov.companieshouse.api.handler.psc.PscsResourceHandler;
import uk.gov.companieshouse.api.handler.psc.request.PscsList;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.psc.PscApi;
import uk.gov.companieshouse.api.model.psc.PscsApi;
import uk.gov.companieshouse.overseasentitiesapi.client.ApiClientService;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.service.PublicDataRetrievalService;

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
    private ApiResponse<PscsApi> apiGetResponsePscs;

    @Mock
    private CompanyResourceHandler company;

    @Mock
    private CompanyGet companyGet;

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
    @Test
    void testGetOverseasEntityPublicDataIsSuccessful() throws IOException, URIValidationException, ServiceException {
        var companyProfileApi = new CompanyProfileApi();
        var pscsApi = new PscsApi();
        var companyPSCsApi = new PscApi();
        companyPSCsApi.setName("Test Name");
        pscsApi.setItems(List.of(companyPSCsApi));

        when(companyGet.execute()).thenReturn(apiGetResponseCompanyProfile);
        when(apiGetResponseCompanyProfile.getData()).thenReturn(companyProfileApi);

        when(apiClient.pscs()).thenReturn(pscs);
        when(pscs.list(Mockito.anyString())).thenReturn(pscsList);
        when(pscsList.execute()).thenReturn(apiGetResponsePscs);

        when(apiGetResponsePscs.getData()).thenReturn(pscsApi);

        publicDataRetrievalService.getOverseasEntityPublicData(COMPANY_REFERENCE, PASS_THROUGH_HEADER);
        verify(apiClientService, times(2)).getOauthAuthenticatedClient(PASS_THROUGH_HEADER);
    }

    @Test
    void testGetOverseasEntityPublicPscsNotFound() throws IOException, URIValidationException, ServiceException {
        var companyProfileApi = new CompanyProfileApi();
        var pscsApi = new PscsApi();
        var companyPSCsApi = new PscApi();
        var pscsApiResponse = new ApiResponse<PscsApi>(404, null);
        companyPSCsApi.setName("Tim Bill");
        pscsApi.setItems(List.of(companyPSCsApi));

        when(companyGet.execute()).thenReturn(apiGetResponseCompanyProfile);
        when(apiGetResponseCompanyProfile.getData()).thenReturn(companyProfileApi);

        when(apiClient.pscs()).thenReturn(pscs);
        when(pscs.list(Mockito.anyString())).thenReturn(pscsList);
        when(pscsList.execute()).thenReturn(pscsApiResponse);

        publicDataRetrievalService.getOverseasEntityPublicData(COMPANY_REFERENCE, PASS_THROUGH_HEADER);
        verify(apiClientService, times(2)).getOauthAuthenticatedClient(PASS_THROUGH_HEADER);
    }

    @Test
    void testServiceExceptionThrownWhenGetOverseasEntityPublicDataForCompanyProfileDataThrowsURIValidationException() throws IOException, URIValidationException {
        when(companyGet.execute()).thenThrow(new URIValidationException("ERROR"));

        assertThrows(ServiceException.class, () -> {
            publicDataRetrievalService.getOverseasEntityPublicData(COMPANY_REFERENCE, PASS_THROUGH_HEADER);
        });
    }

    @Test
    void testServiceExceptionThrownWhenGetOverseasEntityPublicDataForCompanyProfileDataThrowsIOException() throws IOException, URIValidationException {
        when(companyGet.execute()).thenThrow(ApiErrorResponseException.fromIOException(new IOException("ERROR")));

        assertThrows(ServiceException.class, () -> {
            publicDataRetrievalService.getOverseasEntityPublicData(COMPANY_REFERENCE, PASS_THROUGH_HEADER);
        });
    }

    @Test
    void testServiceExceptionThrownWhenGetOverseasEntityPublicDataForPscsDataThrowsURIValidationException() throws IOException, URIValidationException {
        var companyProfileApi = new CompanyProfileApi();

        when(companyGet.execute()).thenReturn(apiGetResponseCompanyProfile);
        when(apiGetResponseCompanyProfile.getData()).thenReturn(companyProfileApi);

        when(apiClient.pscs()).thenReturn(pscs);
        when(pscs.list(Mockito.anyString())).thenReturn(pscsList);
        when(pscsList.execute()).thenThrow(new URIValidationException("ERROR"));

        assertThrows(ServiceException.class, () -> {
            publicDataRetrievalService.getOverseasEntityPublicData(COMPANY_REFERENCE, PASS_THROUGH_HEADER);
        });
    }

    @Test
    void testServiceExceptionThrownWhenGetOverseasEntityPublicDataForPscsDataThrowsIOException() throws IOException, URIValidationException {
        var companyProfileApi = new CompanyProfileApi();

        when(companyGet.execute()).thenReturn(apiGetResponseCompanyProfile);
        when(apiGetResponseCompanyProfile.getData()).thenReturn(companyProfileApi);

        when(apiClient.pscs()).thenReturn(pscs);
        when(pscs.list(Mockito.anyString())).thenReturn(pscsList);
        when(pscsList.execute()).thenThrow(ApiErrorResponseException.fromIOException(new IOException("ERROR")));

        assertThrows(ServiceException.class, () -> {
            publicDataRetrievalService.getOverseasEntityPublicData(COMPANY_REFERENCE, PASS_THROUGH_HEADER);
        });
    }
}
