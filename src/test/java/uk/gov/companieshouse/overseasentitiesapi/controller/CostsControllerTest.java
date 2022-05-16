package uk.gov.companieshouse.overseasentitiesapi.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.overseasentitiesapi.service.CostsService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CostsControllerTest {

    @Mock
    private CostsService costService;

    @InjectMocks
    private CostsController costsController;

    @Test
    void getCosts() {
//        var response = costsController.getCosts();
//
//        verify(costService, times(1)).getCosts();
    }
}
