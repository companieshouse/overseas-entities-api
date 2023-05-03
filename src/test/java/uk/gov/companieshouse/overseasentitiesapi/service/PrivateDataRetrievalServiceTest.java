package uk.gov.companieshouse.overseasentitiesapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.InternalApiClient;
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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PrivateDataRetrievalServiceTest {
  private static final String COMPANY_REFERENCE = "OE123456";

  @InjectMocks private PrivateDataRetrievalService privateDataRetrievalService;

  @Mock private ApiClientService apiClientService;

  @Mock private InternalApiClient apiClient;

  @Mock private ApiResponse<OverseasEntityDataApi> apiGetResponse;

  @Mock private PrivateOverseasEntityDataHandler overseasEntity;

  @Mock private PrivateOverseasEntityDataGet overseasEntityDataGet;

  @Mock private ApiResponse<PrivateBoDataListApi> apiBoDataListGetResponse;

  @Mock private PrivateBeneficialOwnerResourceHandler privateBeneficialOwnerResourceHandler;

  @Mock private PrivateBeneficialOwnerGet privateBeneficialOwnerGet;

  @BeforeEach
  public void init() throws IOException {
    when(apiClientService.getInternalApiClient()).thenReturn(apiClient);

    when(apiClient.privateOverseasEntityDataHandler()).thenReturn(overseasEntity);
    when(overseasEntity.getOverseasEntityData(Mockito.anyString()))
        .thenReturn(overseasEntityDataGet);
    when(apiClient.privateBeneficialOwnerResourceHandler())
        .thenReturn(privateBeneficialOwnerResourceHandler);
    when(privateBeneficialOwnerResourceHandler.getBeneficialOwners(Mockito.anyString()))
        .thenReturn(privateBeneficialOwnerGet);
  }

  @Test
  void testGetPrivateDataIsSuccessful()
      throws IOException, URIValidationException, ServiceException {
    var overseasEntityApi = new OverseasEntityDataApi();
    List<PrivateBoDataApi> privateBoDataApiList = Collections.emptyList();
    var boDataListApi = new PrivateBoDataListApi(privateBoDataApiList);

    when(overseasEntityDataGet.execute()).thenReturn(apiGetResponse);
    when(apiGetResponse.getData()).thenReturn(overseasEntityApi);

    when(privateBeneficialOwnerGet.execute()).thenReturn(apiBoDataListGetResponse);
    when(apiBoDataListGetResponse.getData()).thenReturn(boDataListApi);

    privateDataRetrievalService.initialisePrivateData(COMPANY_REFERENCE);
    verify(apiClientService, times(2)).getInternalApiClient();
  }

  @Test
  void testGetPrivateDataOverseasEntityDataNotFound()
      throws IOException, URIValidationException, ServiceException {
    var overseasEntityApiResponse = new ApiResponse<OverseasEntityDataApi>(404, null);

    List<PrivateBoDataApi> privateBoDataApiList = Collections.emptyList();
    var boDataListApi = new PrivateBoDataListApi(privateBoDataApiList);

    when(overseasEntityDataGet.execute()).thenReturn(overseasEntityApiResponse);

    when(privateBeneficialOwnerGet.execute()).thenReturn(apiBoDataListGetResponse);
    when(apiBoDataListGetResponse.getData()).thenReturn(boDataListApi);

    privateDataRetrievalService.initialisePrivateData(COMPANY_REFERENCE);
    verify(apiClientService, times(2)).getInternalApiClient();
  }

  @Test
  void testGetPrivateDataBeneficialOwnerDataNotFound()
      throws IOException, URIValidationException, ServiceException {
    var overseasEntityApi = new OverseasEntityDataApi();
    var boDataListApiResponse = new ApiResponse<PrivateBoDataListApi>(404, null);

    when(overseasEntityDataGet.execute()).thenReturn(apiGetResponse);
    when(apiGetResponse.getData()).thenReturn(overseasEntityApi);

    when(privateBeneficialOwnerGet.execute()).thenReturn(boDataListApiResponse);

    privateDataRetrievalService.initialisePrivateData(COMPANY_REFERENCE);
    verify(apiClientService, times(2)).getInternalApiClient();
  }
}
