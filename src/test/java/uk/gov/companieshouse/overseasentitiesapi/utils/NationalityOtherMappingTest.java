package uk.gov.companieshouse.overseasentitiesapi.utils;

import java.util.stream.Stream;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.companieshouse.overseasentitiesapi.utils.NationalityOtherMapping.generateNationalityOtherField;

class NationalityOtherMappingTest {
    @ParameterizedTest
    @MethodSource("provideTestCases")
    void testGenerateNationalityOtherField(String nationality, String secondNationality, String expected) {
        var result = generateNationalityOtherField(nationality, secondNationality);
        assertEquals(expected, result);
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
                Arguments.of("Irish", "Spanish", "Irish,Spanish"),
                Arguments.of("Irish", "Spanish,", "Irish,Spanish"),
                Arguments.of("Irish,", "Spanish,", "Irish,Spanish"),
                Arguments.of("Irish,", "Spanish,,,", "Irish,Spanish"),
                Arguments.of("Irish", "Spanish,", "Irish,Spanish"),
                Arguments.of("Irish", ",Spanish", "Irish,Spanish"),
                Arguments.of("Irish", ", Spanish", "Irish,Spanish"),
                Arguments.of("Irish", null, "Irish"),
                Arguments.of("Irish", StringUtils.EMPTY, "Irish")
        );
    }
}
