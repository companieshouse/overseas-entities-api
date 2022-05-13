package uk.gov.companieshouse.overseasentitiesapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.companieshouse.api.model.payment.Cost;
import uk.gov.companieshouse.overseasentitiesapi.service.CostsService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/transactions/{transaction_id}/overseas-entity/{overseas_entity_id}/costs")
public class CostsController {

    private final CostsService costsService;

    @Autowired
    public CostsController(CostsService costService) {
        this.costsService = costService;
    }

    @GetMapping
    public ResponseEntity<List<Cost>> getCosts() {

        var cost = costsService.getCosts();

        return ResponseEntity.ok(Collections.singletonList(cost));
    }
}
