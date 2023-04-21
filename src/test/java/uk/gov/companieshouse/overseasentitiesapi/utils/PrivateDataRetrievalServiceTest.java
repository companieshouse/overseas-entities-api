package uk.gov.companieshouse.overseasentitiesapi.utils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
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
import uk.gov.companieshouse.api.handler.managingofficerdata.PrivateManagingOfficerDataResourceHandler;
import uk.gov.companieshouse.api.handler.managingofficerdata.request.PrivateManagingOfficerDataGet;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.managingofficerdata.ManagingOfficerDataApi;
import uk.gov.companieshouse.overseasentitiesapi.client.ApiClientService;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.service.PrivateDataRetrievalService;

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
  private ApiResponse<ManagingOfficerDataApi> apiGetResponse;

  @Mock
  private PrivateManagingOfficerDataResourceHandler privateManagingOfficerDataResourceHandler;

  @Mock
  private PrivateManagingOfficerDataGet privateManagingOfficerDataGet;

  @BeforeEach
  public void init() throws IOException {
    when(apiClientService.getInternalApiClient()).thenReturn(apiClient);
    when(apiClient.privateManagingOfficerDataResourceHandler()).thenReturn(
        privateManagingOfficerDataResourceHandler);
    when(privateManagingOfficerDataResourceHandler.getMOData(Mockito.anyString())).thenReturn(
        privateManagingOfficerDataGet);
  }

  @Test
  void testManagingOfficerPrivateDataIsSuccessful()
      throws IOException, URIValidationException, ServiceException {
    var overseasEntityApi = new ManagingOfficerDataApi();

    when(privateManagingOfficerDataGet.execute()).thenReturn(apiGetResponse);
    when(apiGetResponse.getData()).thenReturn(overseasEntityApi);

    oePrivateDataRetrievalService.initialisePrivateData(COMPANY_REFERENCE);
    verify(apiClientService, times(1)).getInternalApiClient();
  }

  @Test
  void testServiceExceptionThrownWhenGetManagingOfficerPrivateDataThrowsURIValidationException()
      throws IOException, URIValidationException {
    when(privateManagingOfficerDataGet.execute()).thenThrow(new URIValidationException("ERROR"));

    assertThrows(
        ServiceException.class,
        () -> {
          oePrivateDataRetrievalService.initialisePrivateData((COMPANY_REFERENCE));
        });
  }

  @Test
  void testServiceExceptionThrownWhenGetManagingOfficerPrivateDataThrowsIOException()
      throws IOException, URIValidationException {
    when(privateManagingOfficerDataGet.execute())
        .thenThrow(ApiErrorResponseException.fromIOException(new IOException("ERROR")));
    assertThrows(
        ServiceException.class,
        () -> {
          oePrivateDataRetrievalService.initialisePrivateData(COMPANY_REFERENCE);
        });
  }
}
