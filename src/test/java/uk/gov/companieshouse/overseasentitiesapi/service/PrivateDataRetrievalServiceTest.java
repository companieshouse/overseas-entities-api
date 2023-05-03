package uk.gov.companieshouse.overseasentitiesapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.InternalApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.beneficialowner.PrivateBeneficialOwnerResourceHandler;
import uk.gov.companieshouse.api.handler.beneficialowner.request.PrivateBeneficialOwnerGet;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.handler.managingofficerdata.PrivateManagingOfficerDataResourceHandler;
import uk.gov.companieshouse.api.handler.managingofficerdata.request.PrivateManagingOfficerDataGet;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataApi;
import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataListApi;
import uk.gov.companieshouse.overseasentitiesapi.client.ApiClientService;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrivateDataRetrievalServiceTest {

    private static final String COMPANY_REFERENCE = "OE123456";

    @InjectMocks
    private PrivateDataRetrievalService oePrivateDataRetrievalService;

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private InternalApiClient apiClient;

    @Mock
    private ApiResponse<PrivateBoDataListApi> apiGetResponse;

    @Mock
    private PrivateBeneficialOwnerResourceHandler privateBeneficialOwnerResourceHandler;

    @Mock
    private PrivateManagingOfficerDataResourceHandler privateManagingOfficerDataResourceHandler;

    @Mock
    private PrivateBeneficialOwnerGet privateBeneficialOwnerGet;

    @Mock
    private PrivateManagingOfficerDataGet privateManagingOfficerDataGet;

    @BeforeEach
    public void init() throws IOException {
        when(apiClientService.getInternalApiClient()).thenReturn(apiClient);
        when(apiClient.privateBeneficialOwnerResourceHandler()).thenReturn(
                privateBeneficialOwnerResourceHandler);
        when(privateBeneficialOwnerResourceHandler.getBeneficialOwners(Mockito.anyString())).thenReturn(
                privateBeneficialOwnerGet);

        when(apiClient.privateManagingOfficerDataResourceHandler()).thenReturn(
            privateManagingOfficerDataResourceHandler);
        when(privateManagingOfficerDataResourceHandler.getManagingOfficerData(Mockito.anyString())).thenReturn(
            privateManagingOfficerDataGet);
    }

    @Test
    void testBeneficialOwnerPrivateDataIsSuccessful() throws IOException, URIValidationException, ServiceException {
        List<PrivateBoDataApi> privateBoDataApiList = Collections.emptyList();
        var privateBoDataListApi = new PrivateBoDataListApi(privateBoDataApiList);

        when(privateBeneficialOwnerGet.execute()).thenReturn(apiGetResponse);
        when(apiGetResponse.getData()).thenReturn(privateBoDataListApi);

        oePrivateDataRetrievalService.initialisePrivateData(COMPANY_REFERENCE);
        verify(apiClientService, times(1)).getInternalApiClient();
    }

    @Test
    void testServiceExceptionThrownWhenGetBeneficialOwnerPrivateDataThrowsURIValidationException()
        throws IOException, URIValidationException {
        when(privateBeneficialOwnerGet.execute()).thenThrow(new URIValidationException("ERROR"));

        assertThrows(
            ServiceException.class,
            () -> {
                oePrivateDataRetrievalService.initialisePrivateData((COMPANY_REFERENCE));
            });
    }

    @Test
    void testServiceExceptionThrownWhenGetBeneficialOwnerPrivateDataThrowsIOException()
        throws IOException, URIValidationException {
        when(privateBeneficialOwnerGet.execute())
            .thenThrow(ApiErrorResponseException.fromIOException(new IOException("ERROR")));
        assertThrows(
            ServiceException.class,
            () -> {
                oePrivateDataRetrievalService.initialisePrivateData(COMPANY_REFERENCE);
            });
    }
}