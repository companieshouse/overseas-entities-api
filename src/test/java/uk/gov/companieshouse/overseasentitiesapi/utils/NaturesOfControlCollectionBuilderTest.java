package uk.gov.companieshouse.overseasentitiesapi.utils;

import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlJurisdictionType;
import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class NaturesOfControlCollectionBuilderTest {


    @Test
    void testThereAreNoDuplicateBuilds() {
        var firstNaturesOfControlCollection =
                NaturesOfControlCollectionBuilder.createNaturesOfControlCollectionBuilder()
                        .addPersonType(List.of(NatureOfControlType.SIGNIFICANT_INFLUENCE_OR_CONTROL))
                        .addTrusteesType(List.of(NatureOfControlType.OVER_25_PERCENT_OF_SHARES))
                        .addFirmType(List.of(NatureOfControlType.OVER_25_PERCENT_OF_VOTING_RIGHTS))
                        .addTrustControlType(List.of(NatureOfControlType.SIGNIFICANT_INFLUENCE_OR_CONTROL))
                        .addOwnerOfLandPerson(List.of(NatureOfControlJurisdictionType.SCOTLAND))
                        .addOwnerOfLandOtherEntity(List.of(NatureOfControlJurisdictionType.ENGLAND_AND_WALES))
                        .addFirmControlType(List.of(NatureOfControlType.OVER_25_PERCENT_OF_SHARES))
                        .addFeatureFlag(true)
                        .build();
        assertNotNull(firstNaturesOfControlCollection.getPersonNatureOfControlTypes());
        assertNotNull(firstNaturesOfControlCollection.getTrusteesNatureOfControlTypes());
        assertNotNull(firstNaturesOfControlCollection.getFirmNatureOfControlTypes());
        assertNotNull(firstNaturesOfControlCollection.getTrustControlNatureOfControlTypes());
        assertNotNull(firstNaturesOfControlCollection.getOwnerOfLandPersonNatureOfControlJurisdictions());
        assertNotNull(firstNaturesOfControlCollection.getOwnerOfLandOtherEntityNatureOfControlJurisdictions());
        assertNotNull(firstNaturesOfControlCollection.getFirmControlNatureOfControlTypes());

        var secondNaturesOfControlCollection =
                NaturesOfControlCollectionBuilder.createNaturesOfControlCollectionBuilder()
                        .addPersonType(List.of(NatureOfControlType.SIGNIFICANT_INFLUENCE_OR_CONTROL))
                        .addTrusteesType(List.of(NatureOfControlType.OVER_25_PERCENT_OF_SHARES))
                        .addFirmType(List.of(NatureOfControlType.OVER_25_PERCENT_OF_VOTING_RIGHTS))
                        .addFeatureFlag(true)
                        .build();

        assertNotNull(secondNaturesOfControlCollection.getPersonNatureOfControlTypes());
        assertNotNull(secondNaturesOfControlCollection.getTrusteesNatureOfControlTypes());
        assertNotNull(secondNaturesOfControlCollection.getFirmNatureOfControlTypes());
        assertNull(secondNaturesOfControlCollection.getTrustControlNatureOfControlTypes());
        assertNull(secondNaturesOfControlCollection.getOwnerOfLandPersonNatureOfControlJurisdictions());
        assertNull(secondNaturesOfControlCollection.getOwnerOfLandOtherEntityNatureOfControlJurisdictions());
        assertNull(secondNaturesOfControlCollection.getFirmControlNatureOfControlTypes());
    }
}
