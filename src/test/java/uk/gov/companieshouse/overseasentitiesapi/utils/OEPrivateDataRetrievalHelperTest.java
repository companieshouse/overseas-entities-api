package uk.gov.companieshouse.overseasentitiesapi.utils;

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
import uk.gov.companieshouse.overseasentitiesapi.client.ApiClientService;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OEPrivateDataRetrievalHelperTest {
  private static final String COMPANY_REFERENCE = "OE123456";

  @InjectMocks private OEPrivateDataRetrievalHelper oePrivateDataRetrievalHelper;

  @Mock private ApiClientService apiClientService;

  @Mock private InternalApiClient apiClient;

  @Mock private ApiResponse<OverseasEntityDataApi> apiGetResponse;

  @Mock private PrivateOverseasEntityDataHandler overseasEntity;

  @Mock private PrivateOverseasEntityDataGet overseasEntityDataGet;

  @BeforeEach
  public void init() throws IOException {
    when(apiClientService.getInternalApiClient()).thenReturn(apiClient);
    when(apiClient.privateOverseasEntityDataHandler()).thenReturn(overseasEntity);
    when(overseasEntity.getEmail(Mockito.anyString())).thenReturn(overseasEntityDataGet);
  }

  @Test
  void testGetOverseasEntityPublicDataIsSuccessful()
      throws IOException, URIValidationException, ServiceException {
    var overseasEntityApi = new OverseasEntityDataApi();

    when(overseasEntityDataGet.execute()).thenReturn(apiGetResponse);
    when(apiGetResponse.getData()).thenReturn(overseasEntityApi);

    oePrivateDataRetrievalHelper.getOverseasEntityPrivateData(COMPANY_REFERENCE);
    verify(apiClientService, times(1)).getInternalApiClient();
  }

  @Test
  void testServiceExceptionThrownWhenGetOverseasEntityPublicDataThrowsURIValidationException()
      throws IOException, URIValidationException {
    when(overseasEntityDataGet.execute()).thenThrow(new URIValidationException("ERROR"));

    assertThrows(
        ServiceException.class,
        () -> {
          oePrivateDataRetrievalHelper.getOverseasEntityPrivateData((COMPANY_REFERENCE));
        });
  }

  @Test
  void testServiceExceptionThrownWhenGetOverseasEntityPublicDataThrowsIOException()
      throws IOException, URIValidationException {
    when(overseasEntityDataGet.execute())
        .thenThrow(ApiErrorResponseException.fromIOException(new IOException("ERROR")));
    assertThrows(
        ServiceException.class,
        () -> {
          oePrivateDataRetrievalHelper.getOverseasEntityPrivateData(COMPANY_REFERENCE);
        });
  }
}
