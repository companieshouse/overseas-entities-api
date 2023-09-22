package uk.gov.companieshouse.overseasentitiesapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.companieshouse.overseasentitiesapi.service.OverseasEntitiesService;
import uk.gov.companieshouse.overseasentitiesapi.service.PrivateDataRetrievalService;

@RestController
@RequestMapping("/private/transactions/{transaction_id}/overseas-entity/{overseas_entity_id}/trusts")
public class TrustsDataController {
    PrivateDataRetrievalService privateDataRetrievalService;
    OverseasEntitiesService overseasEntitiesService;

    @Value("${FEATURE_FLAG_ENABLE_ROE_UPDATE_24112022:false}")
    private boolean isRoeUpdateEnabled;

    @Autowired
    public TrustsDataController(
            final PrivateDataRetrievalService privateDataRetrievalService,
            final OverseasEntitiesService overseasEntitiesService
    ) {
        this.privateDataRetrievalService = privateDataRetrievalService;
        this.overseasEntitiesService = overseasEntitiesService;
    }
}
