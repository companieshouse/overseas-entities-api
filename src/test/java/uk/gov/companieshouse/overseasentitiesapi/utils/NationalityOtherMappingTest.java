package uk.gov.companieshouse.overseasentitiesapi.utils;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.companieshouse.overseasentitiesapi.utils.NationalityOtherMapping.generateMoNationalityOtherField;
import static uk.gov.companieshouse.overseasentitiesapi.utils.NationalityOtherMapping.generateNationalityOtherField;

class NationalityOtherMappingTest {
    @Test
    void testGenerateNationalityOtherField() {
        String nationality = "Irish";
        String secondNationality = "Spanish";

        var result = generateNationalityOtherField(nationality, secondNationality);

        assertEquals("Irish, Spanish", result);
    }
    @Test
    void testGenerateNationalityOtherFieldSecondNationalityNull() {
        String nationality = "Irish";
        String secondNationality = null;

        var result = generateNationalityOtherField(nationality, secondNationality);

        assertEquals("Irish", result);
    }
    @Test
    void testGenerateNationalityOtherFieldSecondNationalityEmptyString() {
        String nationality = "Irish";
        String secondNationality = StringUtils.EMPTY;

        var result = generateNationalityOtherField(nationality, secondNationality);

        assertEquals("Irish", result);
    }

    @Test
    void testGenerateMoNationalityOtherField() {
        String nationality = "Irish";
        String secondNationality = "Spanish";

        var result = generateMoNationalityOtherField(nationality, secondNationality);

        assertEquals("Irish,Spanish", result);
    }

    @Test
    void testGenerateMoNationalitySecondNationalityEmpty() {
        String nationality = "Irish";
        String secondNationality = StringUtils.EMPTY;

        var result = generateMoNationalityOtherField(nationality, secondNationality);

        assertEquals("Irish", result);
    }

    @Test
    void testGenerateMoNationalitySecondNationalityNull() {
        String nationality = "Irish";
        String secondNationality = null;

        var result = generateMoNationalityOtherField(nationality, secondNationality);

        assertEquals("Irish", result);
    }
}
