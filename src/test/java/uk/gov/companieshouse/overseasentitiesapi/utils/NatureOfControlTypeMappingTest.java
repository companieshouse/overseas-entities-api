package uk.gov.companieshouse.overseasentitiesapi.utils;

import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType.OVER_25_PERCENT_OF_SHARES;
import static uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType.OVER_25_PERCENT_OF_VOTING_RIGHTS;
import static uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType.APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS;
import static uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType.SIGNIFICANT_INFLUENCE_OR_CONTROL;
import static uk.gov.companieshouse.overseasentitiesapi.utils.NatureOfControlTypeMapping.collectAllNatureOfControlsIntoSingleList;

class NatureOfControlTypeMappingTest {
    @Test
    void collectAllNatureOfControlsIntoSingleListAllValueNonNull() {
        List<NatureOfControlType> personNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);
        List<NatureOfControlType> trusteesNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);
        List<NatureOfControlType> firmNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);

        var result = collectAllNatureOfControlsIntoSingleList(
                personNatureOfControlTypes,
                trusteesNatureOfControlTypes,
                firmNatureOfControlTypes);

        assertEquals(12, result.size());
        assertEquals("OE_OWNERSHIPOFSHARES_MORETHAN25PERCENT_AS_PERSON", result.get(0));
        assertEquals("OE_VOTINGRIGHTS_MORETHAN25PERCENT_AS_PERSON", result.get(1));
        assertEquals("OE_RIGHTTOAPPOINTANDREMOVEDIRECTORS_AS_PERSON", result.get(2));
        assertEquals("OE_SIGINFLUENCECONTROL_AS_PERSON", result.get(3));
        assertEquals("OE_OWNERSHIPOFSHARES_MORETHAN25PERCENT_AS_TRUST", result.get(4));
        assertEquals("OE_VOTINGRIGHTS_MORETHAN25PERCENT_AS_TRUST", result.get(5));
        assertEquals("OE_RIGHTTOAPPOINTANDREMOVEDIRECTORS_AS_TRUST", result.get(6));
        assertEquals("OE_SIGINFLUENCECONTROL_AS_TRUST", result.get(7));
        assertEquals("OE_OWNERSHIPOFSHARES_MORETHAN25PERCENT_AS_FIRM", result.get(8));
        assertEquals("OE_VOTINGRIGHTS_MORETHAN25PERCENT_AS_FIRM", result.get(9));
        assertEquals("OE_RIGHTTOAPPOINTANDREMOVEDIRECTORS_AS_FIRM", result.get(10));
        assertEquals("OE_SIGINFLUENCECONTROL_AS_FIRM", result.get(11));
    }

    @Test
    void collectAllNatureOfControlsIntoSingleListPersonNocNull() {
        List<NatureOfControlType> personNatureOfControlTypes = null;
        List<NatureOfControlType> trusteesNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);
        List<NatureOfControlType> firmNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);

        var result = collectAllNatureOfControlsIntoSingleList(
                personNatureOfControlTypes,
                trusteesNatureOfControlTypes,
                firmNatureOfControlTypes);

        assertEquals(8, result.size());
    }

    @Test
    void collectAllNatureOfControlsIntoSingleListTrusteesNocNull() {
        List<NatureOfControlType> personNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);
        List<NatureOfControlType> trusteesNatureOfControlTypes = null;
        List<NatureOfControlType> firmNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);

        var result = collectAllNatureOfControlsIntoSingleList(
                personNatureOfControlTypes,
                trusteesNatureOfControlTypes,
                firmNatureOfControlTypes);

        assertEquals(8, result.size());
    }

    @Test
    void collectAllNatureOfControlsIntoSingleListFirmNocNull() {
        List<NatureOfControlType> personNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);
        List<NatureOfControlType> trusteesNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);
        List<NatureOfControlType> firmNatureOfControlTypes = null;

        var result = collectAllNatureOfControlsIntoSingleList(
                personNatureOfControlTypes,
                trusteesNatureOfControlTypes,
                firmNatureOfControlTypes);

        assertEquals(8, result.size());
    }

    @Test
    void collectAllNatureOfControlsIntoSingleListAllNocNull() {
        List<NatureOfControlType> personNatureOfControlTypes = null;
        List<NatureOfControlType> trusteesNatureOfControlTypes = null;
        List<NatureOfControlType> firmNatureOfControlTypes = null;

        var result = collectAllNatureOfControlsIntoSingleList(
                personNatureOfControlTypes,
                trusteesNatureOfControlTypes,
                firmNatureOfControlTypes);

        assertEquals(0, result.size());
    }
}
