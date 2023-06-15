package uk.gov.companieshouse.overseasentitiesapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.companieshouse.overseasentitiesapi.exception.ServiceException;
import uk.gov.companieshouse.overseasentitiesapi.service.PrivateDataRetrievalService;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import java.util.HashMap;

import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.ERIC_REQUEST_ID_KEY;

@RestController
@RequestMapping("/overseas-entity-details")
public class OverseasEntitiesDataController {

    PrivateDataRetrievalService privateDataRetrievalService;

    @Autowired
    public OverseasEntitiesDataController(final PrivateDataRetrievalService privateDataRetrievalService){
        this.privateDataRetrievalService = privateDataRetrievalService;
    }

    @GetMapping("/{company_number}")
    public ResponseEntity<Object> getOverseasEntityDetails (@PathVariable("company_number") String companyNumber,
                                                            @RequestHeader(value = ERIC_REQUEST_ID_KEY) String requestId) throws ServiceException {
        var logMap = new HashMap<String, Object>();
        logMap.put("Company Number", companyNumber);

        try {
            ApiLogger.infoContext(requestId, "Calling service to get OE details", logMap);
            return ResponseEntity.ok(privateDataRetrievalService.getOverseasEntityData(companyNumber));
        } catch (ServiceException e) {
            ApiLogger.errorContext(requestId, e.getMessage(), e, logMap);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
