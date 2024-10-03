package uk.gov.companieshouse.overseasentitiesapi.utils;

import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlJurisdictionType;
import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType;

import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

class NaturesOfControlCollectionBuilderTest {


    @Test
    void testThereAreNoDuplicateBuilds() {
        var firstNaturesOfControlCollection =
                NaturesOfControlCollectionBuilder.createNaturesOfControlCollectionBuilder()
                        .addPersonType(Arrays.asList(NatureOfControlType.SIGNIFICANT_INFLUENCE_OR_CONTROL))
                        .addTrusteesType(Arrays.asList(NatureOfControlType.OVER_25_PERCENT_OF_SHARES))
                        .addFirmType(Arrays.asList(NatureOfControlType.OVER_25_PERCENT_OF_VOTING_RIGHTS))
                        .addTrustType(Arrays.asList(NatureOfControlType.SIGNIFICANT_INFLUENCE_OR_CONTROL))
                        .addOwnerOfLandPerson(Arrays.asList(NatureOfControlJurisdictionType.SCOTLAND))
                        .addOwnerOfLandOtherEntity(Arrays.asList(NatureOfControlJurisdictionType.ENGLAND_AND_WALES))
                        .addFirmControlType(Arrays.asList(NatureOfControlType.OVER_25_PERCENT_OF_SHARES))
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
                        .addPersonType(Arrays.asList(NatureOfControlType.SIGNIFICANT_INFLUENCE_OR_CONTROL))
                        .addTrusteesType(Arrays.asList(NatureOfControlType.OVER_25_PERCENT_OF_SHARES))
                        .addFirmType(Arrays.asList(NatureOfControlType.OVER_25_PERCENT_OF_VOTING_RIGHTS))
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
