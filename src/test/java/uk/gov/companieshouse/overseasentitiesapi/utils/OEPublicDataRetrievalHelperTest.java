package uk.gov.companieshouse.overseasentitiesapi.utils;

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
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.overseasentitiesapi.client.ApiClientService;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OEPublicDataRetrievalHelperTest {
    private static final String COMPANY_REFERENCE = "12345234";

    private static final String PASS_THROUGH_HEADER = "432342353255";

    @InjectMocks
    private OEPublicDataRetrievalHelper oePublicDataRetrievalHelper;

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ApiClient apiClient;

    @Mock
    private ApiResponse<CompanyProfileApi> apiGetResponse;

    @Mock
    private CompanyResourceHandler company;

    @Mock
    private CompanyGet companyGet;

    @BeforeEach
    public void init() throws IOException {
        when(apiClientService.getOauthAuthenticatedClient(PASS_THROUGH_HEADER)).thenReturn(apiClient);
        when(apiClient.company()).thenReturn(company);
        when(company.get(Mockito.anyString())).thenReturn(companyGet);
    }

    @Test
    void testGetOverseasEntityPublicDataIsSuccessful() throws IOException, URIValidationException, ServiceException {
        var companyProfileApi = new CompanyProfileApi();

        when(companyGet.execute()).thenReturn(apiGetResponse);
        when(apiGetResponse.getData()).thenReturn(companyProfileApi);

        oePublicDataRetrievalHelper.getOverseasEntityPublicData(COMPANY_REFERENCE, PASS_THROUGH_HEADER);
        verify(apiClientService, times(1)).getOauthAuthenticatedClient(PASS_THROUGH_HEADER);
    }

    @Test
    void testServiceExceptionThrownWhenGetOverseasEntityPublicDataThrowsURIValidationException() throws IOException, URIValidationException {
        when(companyGet.execute()).thenThrow(new URIValidationException("ERROR"));

        assertThrows(ServiceException.class, () -> {
            oePublicDataRetrievalHelper.getOverseasEntityPublicData(COMPANY_REFERENCE, PASS_THROUGH_HEADER);
        });
    }

    @Test
    void testServiceExceptionThrownWhenGetOverseasEntityPublicDataThrowsIOException() throws IOException, URIValidationException {
        when(companyGet.execute()).thenThrow(ApiErrorResponseException.fromIOException(new IOException("ERROR")));

        assertThrows(ServiceException.class, () -> {
            oePublicDataRetrievalHelper.getOverseasEntityPublicData(COMPANY_REFERENCE, PASS_THROUGH_HEADER);
        });
    }
}
