package uk.gov.companieshouse.overseasentitiesapi.utils;

import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlJurisdictionType;
import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlJurisdictionType.ENGLAND_AND_WALES;
import static uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlJurisdictionType.NORTHERN_IRELAND;
import static uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlJurisdictionType.SCOTLAND;
import static uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType.OVER_25_PERCENT_OF_SHARES;
import static uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType.OVER_25_PERCENT_OF_VOTING_RIGHTS;
import static uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType.APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS;
import static uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType.SIGNIFICANT_INFLUENCE_OR_CONTROL;
import static uk.gov.companieshouse.overseasentitiesapi.utils.NatureOfControlTypeMapping.collectAllNatureOfControlsIntoSingleList;

class NatureOfControlTypeMappingTest {
    @Test
    void privateConstructorThrowsException() {
        Class<NatureOfControlTypeMapping> natureOfControlTypeMappingClass = NatureOfControlTypeMapping.class;

        Constructor<?> constructor = natureOfControlTypeMappingClass.getDeclaredConstructors()[0];
        constructor.setAccessible(true);

        Throwable exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
        assertEquals(IllegalAccessError.class, exception.getCause().getClass());
        assertEquals("Use the static method designation", exception.getCause().getMessage());
    }

    // UAR-1583 remove when feature flag is removed
    @Test
    void collectAllNatureOfControlsIntoSingleListAllValueNonNullWhenNewNocFlagFalse() {
        List<NatureOfControlType> personNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);
        List<NatureOfControlType> trusteesNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);
        List<NatureOfControlType> firmNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);

        // UAR-1583 New nocs
        List<NatureOfControlType> trustControlNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);
        List<NatureOfControlJurisdictionType> ownerOfLandPersonNatureOfControlJurisdictions =
                Arrays.asList(ENGLAND_AND_WALES, SCOTLAND, NORTHERN_IRELAND);
        List<NatureOfControlJurisdictionType> ownerOfLandOtherEntityNatureOfControlJurisdictions =
                Arrays.asList(ENGLAND_AND_WALES, SCOTLAND, NORTHERN_IRELAND);
        List<NatureOfControlType> firmControlNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);

        var result = collectAllNatureOfControlsIntoSingleList(
                personNatureOfControlTypes,
                trusteesNatureOfControlTypes,
                firmNatureOfControlTypes,
                trustControlNatureOfControlTypes,
                ownerOfLandPersonNatureOfControlJurisdictions,
                ownerOfLandOtherEntityNatureOfControlJurisdictions,
                firmControlNatureOfControlTypes,
                false);

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
    void collectAllNatureOfControlsIntoSingleListAllValueNonNullWhenNewNocFlagTrue() {
        List<NatureOfControlType> personNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);
        List<NatureOfControlType> trusteesNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);
        List<NatureOfControlType> firmNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);
        List<NatureOfControlType> trustControlNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);
        List<NatureOfControlJurisdictionType> ownerOfLandPersonNatureOfControlJurisdictions =
                Arrays.asList(ENGLAND_AND_WALES, SCOTLAND, NORTHERN_IRELAND);
        List<NatureOfControlJurisdictionType> ownerOfLandOtherEntityNatureOfControlJurisdictions =
                Arrays.asList(ENGLAND_AND_WALES, SCOTLAND, NORTHERN_IRELAND);
        List<NatureOfControlType> firmControlNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);

        var result = collectAllNatureOfControlsIntoSingleList(
                personNatureOfControlTypes,
                trusteesNatureOfControlTypes,
                firmNatureOfControlTypes,
                trustControlNatureOfControlTypes,
                ownerOfLandPersonNatureOfControlJurisdictions,
                ownerOfLandOtherEntityNatureOfControlJurisdictions,
                firmControlNatureOfControlTypes,
                true);

        assertEquals(26, result.size());
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
        assertEquals("OE_OWNERSHIPOFSHARES_MORETHAN25PERCENT_AS_CONTROLOVERTRUST", result.get(12));
        assertEquals("OE_VOTINGRIGHTS_MORETHAN25PERCENT_AS_CONTROLOVERTRUST", result.get(13));
        assertEquals("OE_RIGHTTOAPPOINTANDREMOVEDIRECTORS_AS_CONTROLOVERTRUST", result.get(14));
        assertEquals("OE_SIGINFLUENCECONTROL_AS_CONTROLOVERTRUST", result.get(15));
        assertEquals("OE_REGOWNER_AS_NOMINEEPERSON_ENGLANDWALES", result.get(16));
        assertEquals("OE_REGOWNER_AS_NOMINEEPERSON_SCOTLAND", result.get(17));
        assertEquals("OE_REGOWNER_AS_NOMINEEPERSON_NORTHERNIRELAND", result.get(18));
        assertEquals("OE_REGOWNER_AS_NOMINEEANOTHERENTITY_ENGLANDWALES", result.get(19));
        assertEquals("OE_REGOWNER_AS_NOMINEEANOTHERENTITY_SCOTLAND", result.get(20));
        assertEquals("OE_REGOWNER_AS_NOMINEEANOTHERENTITY_NORTHERNIRELAND", result.get(21));
        assertEquals("OE_OWNERSHIPOFSHARES_MORETHAN25PERCENT_AS_CONTROLOVERFIRM", result.get(22));
        assertEquals("OE_VOTINGRIGHTS_MORETHAN25PERCENT_AS_CONTROLOVERFIRM", result.get(23));
        assertEquals("OE_RIGHTTOAPPOINTANDREMOVEDIRECTORS_AS_CONTROLOVERFIRM", result.get(24));
        assertEquals("OE_SIGINFLUENCECONTROL_AS_CONTROLOVERFIRM", result.get(25));
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

    @Test
    void collectAllNatureOfControlsIntoSingleTrustControlNull() {
        List<NatureOfControlType> personNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);
        List<NatureOfControlType> trusteesNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);
        List<NatureOfControlType> firmNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);

        List<NatureOfControlType> trustControlNatureOfControlTypes = null;
        List<NatureOfControlJurisdictionType> ownerOfLandPersonNatureOfControlJurisdictions =
                Arrays.asList(ENGLAND_AND_WALES, SCOTLAND, NORTHERN_IRELAND);
        List<NatureOfControlJurisdictionType> ownerOfLandOtherEntityNatureOfControlJurisdictions =
                Arrays.asList(ENGLAND_AND_WALES, SCOTLAND, NORTHERN_IRELAND);
        List<NatureOfControlType> firmControlNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);


        var result = collectAllNatureOfControlsIntoSingleList(
                personNatureOfControlTypes,
                trusteesNatureOfControlTypes,
                firmNatureOfControlTypes,
                trustControlNatureOfControlTypes,
                ownerOfLandPersonNatureOfControlJurisdictions,
                ownerOfLandOtherEntityNatureOfControlJurisdictions,
                firmControlNatureOfControlTypes,
                true);

        assertEquals(22, result.size());
    }

    @Test
    void collectAllNatureOfControlsIntoSingleOwnerOfLandPersonNull() {
        List<NatureOfControlType> personNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);
        List<NatureOfControlType> trusteesNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);
        List<NatureOfControlType> firmNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);

        List<NatureOfControlType> trustControlNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);
        List<NatureOfControlJurisdictionType> ownerOfLandPersonNatureOfControlJurisdictions = null;
        List<NatureOfControlJurisdictionType> ownerOfLandOtherEntityNatureOfControlJurisdictions =
                Arrays.asList(ENGLAND_AND_WALES, SCOTLAND, NORTHERN_IRELAND);
        List<NatureOfControlType> firmControlNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);


        var result = collectAllNatureOfControlsIntoSingleList(
                personNatureOfControlTypes,
                trusteesNatureOfControlTypes,
                firmNatureOfControlTypes,
                trustControlNatureOfControlTypes,
                ownerOfLandPersonNatureOfControlJurisdictions,
                ownerOfLandOtherEntityNatureOfControlJurisdictions,
                firmControlNatureOfControlTypes,
                true);

        assertEquals(23, result.size());
    }

    @Test
    void collectAllNatureOfControlsIntoSingleOwnerOfLandOtherNull() {
        List<NatureOfControlType> personNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);
        List<NatureOfControlType> trusteesNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);
        List<NatureOfControlType> firmNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);

        List<NatureOfControlType> trustControlNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);
        List<NatureOfControlJurisdictionType> ownerOfLandPersonNatureOfControlJurisdictions =
                Arrays.asList(ENGLAND_AND_WALES, SCOTLAND, NORTHERN_IRELAND);
        List<NatureOfControlJurisdictionType> ownerOfLandOtherEntityNatureOfControlJurisdictions = null;
        List<NatureOfControlType> firmControlNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);


        var result = collectAllNatureOfControlsIntoSingleList(
                personNatureOfControlTypes,
                trusteesNatureOfControlTypes,
                firmNatureOfControlTypes,
                trustControlNatureOfControlTypes,
                ownerOfLandPersonNatureOfControlJurisdictions,
                ownerOfLandOtherEntityNatureOfControlJurisdictions,
                firmControlNatureOfControlTypes,
                true);

        assertEquals(23, result.size());
    }

    @Test
    void collectAllNatureOfControlsIntoSingleFirmControlNull() {
        List<NatureOfControlType> personNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);
        List<NatureOfControlType> trusteesNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);
        List<NatureOfControlType> firmNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);

        List<NatureOfControlType> trustControlNatureOfControlTypes =
                Arrays.asList(OVER_25_PERCENT_OF_SHARES, OVER_25_PERCENT_OF_VOTING_RIGHTS,
                        APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, SIGNIFICANT_INFLUENCE_OR_CONTROL);
        List<NatureOfControlJurisdictionType> ownerOfLandPersonNatureOfControlJurisdictions =
                Arrays.asList(ENGLAND_AND_WALES, SCOTLAND, NORTHERN_IRELAND);
        List<NatureOfControlJurisdictionType> ownerOfLandOtherEntityNatureOfControlJurisdictions =
                Arrays.asList(ENGLAND_AND_WALES, SCOTLAND, NORTHERN_IRELAND);
        List<NatureOfControlType> firmControlNatureOfControlTypes = null;


        var result = collectAllNatureOfControlsIntoSingleList(
                personNatureOfControlTypes,
                trusteesNatureOfControlTypes,
                firmNatureOfControlTypes,
                trustControlNatureOfControlTypes,
                ownerOfLandPersonNatureOfControlJurisdictions,
                ownerOfLandOtherEntityNatureOfControlJurisdictions,
                firmControlNatureOfControlTypes,
                true);

        assertEquals(22, result.size());
    }
}
