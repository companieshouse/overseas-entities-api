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
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.handler.update.PrivateOverseasEntityDataHandler;
import uk.gov.companieshouse.api.handler.update.request.PrivateOverseasEntityDataGet;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.update.OverseasEntityDataApi;
import uk.gov.companieshouse.api.handler.beneficialowner.PrivateBeneficialOwnerResourceHandler;
import uk.gov.companieshouse.api.handler.beneficialowner.request.PrivateBeneficialOwnerGet;
import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataApi;
import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataListApi;
import uk.gov.companieshouse.overseasentitiesapi.client.ApiClientService;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;

import java.io.IOException;
import java.util.List;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PrivateDataRetrievalServiceTest {
  private static final String COMPANY_REFERENCE = "OE123456";

  @InjectMocks
  private PrivateDataRetrievalService privateDataRetrievalService;

  @Mock
  private ApiClientService apiClientService;

  @Mock
  private InternalApiClient apiClient;

  @Mock
  private ApiResponse<OverseasEntityDataApi> apiGetResponse;

  @Mock
  private PrivateOverseasEntityDataHandler overseasEntity;

  @Mock
  private PrivateOverseasEntityDataGet overseasEntityDataGet;

  @InjectMocks
  private PrivateDataRetrievalService oePrivateDataRetrievalService;

  @Mock
  private ApiResponse<PrivateBoDataListApi> apiBoDataListGetResponse;

  @Mock
  private PrivateBeneficialOwnerResourceHandler privateBeneficialOwnerResourceHandler;

  @Mock
  private PrivateBeneficialOwnerGet privateBeneficialOwnerGet;

  @BeforeEach
  public void init() throws IOException {
    when(apiClientService.getInternalApiClient()).thenReturn(apiClient);
    when(apiClient.privateOverseasEntityDataHandler()).thenReturn(overseasEntity);
    when(overseasEntity.getOverseasEntityData(Mockito.anyString())).thenReturn(overseasEntityDataGet);
    when(apiClientService.getInternalApiClient()).thenReturn(apiClient);
        when(apiClient.privateBeneficialOwnerResourceHandler()).thenReturn(
                privateBeneficialOwnerResourceHandler);
        when(privateBeneficialOwnerResourceHandler.getBeneficialOwners(Mockito.anyString())).thenReturn(
                privateBeneficialOwnerGet);
  }

  @Test
  void testGetOverseasEntityPrivateDataIsSuccessful()
      throws IOException, URIValidationException, ServiceException {
    var overseasEntityApi = new OverseasEntityDataApi();

    when(overseasEntityDataGet.execute()).thenReturn(apiGetResponse);
    when(apiGetResponse.getData()).thenReturn(overseasEntityApi);

    privateDataRetrievalService.initialisePrivateData(COMPANY_REFERENCE);
    verify(apiClientService, times(1)).getInternalApiClient();
  }

  @Test
  void testServiceExceptionThrownWhenGetOverseasEntityPrivateDataThrowsURIValidationException()
      throws IOException, URIValidationException {
    when(overseasEntityDataGet.execute()).thenThrow(new URIValidationException("ERROR"));

    assertThrows(
        ServiceException.class,
        () -> {
          privateDataRetrievalService.initialisePrivateData((COMPANY_REFERENCE));
        });
  }

  @Test
  void testServiceExceptionThrownWhenGetOverseasEntityPrivateDataThrowsIOException()
      throws IOException, URIValidationException {
    when(overseasEntityDataGet.execute())
        .thenThrow(ApiErrorResponseException.fromIOException(new IOException("ERROR")));
    assertThrows(
        ServiceException.class,
        () -> {
          privateDataRetrievalService.initialisePrivateData(COMPANY_REFERENCE);
        });
  }

  @Test
  void testBeneficialOwnerPrivateDataIsSuccessful() throws IOException, URIValidationException, ServiceException {
    List<PrivateBoDataApi> privateBoDataApiList = Collections.emptyList();
    var overseasEntityApi = new PrivateBoDataListApi(privateBoDataApiList);

    when(privateBeneficialOwnerGet.execute()).thenReturn(apiBoDataListGetResponse);
    when(apiBoDataListGetResponse.getData()).thenReturn(overseasEntityApi);

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
