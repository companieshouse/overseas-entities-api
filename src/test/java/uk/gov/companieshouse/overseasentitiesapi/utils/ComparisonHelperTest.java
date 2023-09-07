package uk.gov.companieshouse.overseasentitiesapi.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import uk.gov.companieshouse.api.model.common.Address;
import uk.gov.companieshouse.api.model.officers.FormerNamesApi;
import uk.gov.companieshouse.api.model.officers.OfficerRoleApi;
import uk.gov.companieshouse.api.model.utils.AddressApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.PersonName;
import utils.AddressTestUtils;

class ComparisonHelperTest {

    private static final String[] ADDRESS_FIELD_NAMES = {"CareOf", "PoBox", "PropertyNameNumber",
            "Line1", "Line2", "Town", "County", "Postcode", "Country"};

    private static final String[] ADDRESS_FIELD_VALUES_CONCATENATED_FIRST_LINE = {"CareOf", "PoBox",
            "PropertyNameNumber Line1", null, "Line2", "Town", "County", "Postcode", "Country"};

    private static Stream<Arguments> provideTestDataForNatureOfControlsEquals() {
        return Stream.of(
                Arguments.of(List.of("OE_VOTINGRIGHTS_MORETHAN25PERCENT_AS_PERSON",
                                "OE_OWNERSHIPOFSHARES_MORETHAN25PERCENT_AS_PERSON"),
                        new String[]{
                                "voting-rights-more-than-25-percent-registered-overseas-entity",
                                "ownership-of-shares-more-than-25-percent-registered-overseas-entity"},
                        true),
                Arguments.of(null,
                        new String[]{
                                "voting-rights-more-than-25-percent-registered-overseas-entity",
                                "ownership-of-shares-more-than-25-percent-registered-overseas-entity"},
                        false),
                Arguments.of(List.of("OE_VOTINGRIGHTS_MORETHAN25PERCENT_AS_PERSON",
                                "OE_OWNERSHIPOFSHARES_MORETHAN25PERCENT_AS_PERSON"),
                        null,
                        false),
                Arguments.of(List.of("OE_VOTINGRIGHTS_MORETHAN25PERCENT_AS_PERSON",
                                "OE_OWNERSHIPOFSHARES_MORETHAN25PERCENT_AS_PERSON"),
                        new String[]{
                                "right-to-appoint-and-remove-directors-registered-overseas-entity",
                                "significant-influence-or-control-registered-overseas-entity"},
                        false),
                Arguments.of(List.of("OE_OWNERSHIPOFSHARES_MORETHAN25PERCENT_AS_PERSON",
                                "OE_VOTINGRIGHTS_MORETHAN25PERCENT_AS_PERSON"),
                        new String[]{
                                "voting-rights-more-than-25-percent-registered-overseas-entity",
                                "ownership-of-shares-more-than-25-percent-registered-overseas-entity"},
                        true),
                Arguments.of(List.of(
                                "OE_OWNERSHIPOFSHARES_MORETHAN25PERCENT_AS_PERSON",
                                "OE_VOTINGRIGHTS_MORETHAN25PERCENT_AS_PERSON",
                                "OE_RIGHTTOAPPOINTANDREMOVEDIRECTORS_AS_PERSON",
                                "OE_SIGINFLUENCECONTROL_AS_PERSON",
                                "OE_OWNERSHIPOFSHARES_MORETHAN25PERCENT_AS_TRUST",
                                "OE_VOTINGRIGHTS_MORETHAN25PERCENT_AS_TRUST",
                                "OE_RIGHTTOAPPOINTANDREMOVEDIRECTORS_AS_TRUST",
                                "OE_SIGINFLUENCECONTROL_AS_TRUST",
                                "OE_OWNERSHIPOFSHARES_MORETHAN25PERCENT_AS_FIRM",
                                "OE_VOTINGRIGHTS_MORETHAN25PERCENT_AS_FIRM",
                                "OE_RIGHTTOAPPOINTANDREMOVEDIRECTORS_AS_FIRM",
                                "OE_SIGINFLUENCECONTROL_AS_FIRM"
                        ),
                        new String[]{
                                "ownership-of-shares-more-than-25-percent-registered-overseas-entity",
                                "voting-rights-more-than-25-percent-registered-overseas-entity",
                                "right-to-appoint-and-remove-directors-registered-overseas-entity",
                                "significant-influence-or-control-registered-overseas-entity",
                                "ownership-of-shares-more-than-25-percent-as-trust-registered-overseas-entity",
                                "voting-rights-more-than-25-percent-as-trust-registered-overseas-entity",
                                "right-to-appoint-and-remove-directors-as-trust-registered-overseas-entity",
                                "significant-influence-or-control-as-trust-registered-overseas-entity",
                                "ownership-of-shares-more-than-25-percent-as-firm-registered-overseas-entity",
                                "voting-rights-more-than-25-percent-as-firm-registered-overseas-entity",
                                "right-to-appoint-and-remove-directors-as-firm-registered-overseas-entity",
                                "significant-influence-or-control-as-firm-registered-overseas-entity"
                        },
                        true)
        );
    }

    private static Stream<Arguments> provideTestArguments() {
        return Stream.of(
                Arguments.of(null, " "),
                Arguments.of(null, ""),
                Arguments.of(" ", "")
        );
    }

    @Test
    void equalsAddressDtoAndAddressApiReturnCorrectResult() {
        String[] addressFields = ADDRESS_FIELD_NAMES;

        AddressDto addressDto = AddressTestUtils.createDummyAddressDto(addressFields);
        AddressApi addressApi = AddressTestUtils.createDummyModelUtilsAddressApi(addressFields);

        assertTrue(ComparisonHelper.equals(addressDto, addressApi));
    }

    @Test
    void equalsAddressDtoAndAddressApiCompleteFalseTest() {
        String[] addressDtoFields = ADDRESS_FIELD_NAMES;
        String[] addressApiFields = addressDtoFields.clone();

        var output = false;

        for (var i = 0; i < addressDtoFields.length; i++) {
            addressDtoFields[i] = "Different--" + addressDtoFields[i];
            if (i < addressDtoFields.length - 1) {
                addressApiFields[i + 1] = "Different--" + addressApiFields[i + 1];
            }

            AddressDto addressDto = AddressTestUtils.createDummyAddressDto(addressDtoFields);
            AddressApi addressApi = AddressTestUtils.createDummyModelUtilsAddressApi(addressApiFields);

            output |= ComparisonHelper.equals(addressDto, addressApi);

            if (i < addressDtoFields.length - 1) {
                addressDtoFields[i] = addressApiFields[i];
                addressApiFields[i + 1] = addressDtoFields[i + 1];
            }
        }
        assertFalse(output);
    }

    @ParameterizedTest
    @MethodSource("provideTestArguments")
    void testEquality(String value1, String value2) {
        String[] controlAddressFields = ADDRESS_FIELD_NAMES;
        String[] addressFields1 = Arrays.copyOf(controlAddressFields, controlAddressFields.length);
        String[] addressFields2 = Arrays.copyOf(controlAddressFields, controlAddressFields.length);

        for (int i = 0; i < addressFields1.length; i++) {
            addressFields1[i] = value1;
            addressFields2[i] = value2;

            AddressDto addressDto1 = AddressTestUtils.createDummyAddressDto(addressFields1);
            AddressApi addressApi1 = AddressTestUtils.createDummyModelUtilsAddressApi(
                    addressFields1);

            AddressDto addressDto2 = AddressTestUtils.createDummyAddressDto(addressFields2);
            AddressApi addressApi2 = AddressTestUtils.createDummyModelUtilsAddressApi(
                    addressFields2);

            addressFields1[i] = controlAddressFields[i];
            addressFields2[i] = controlAddressFields[i];

            String fieldDescription = "Field: " + controlAddressFields[i];
            assertTrue(ComparisonHelper.equals(addressDto1, addressApi1),
                    fieldDescription + " - equals() does not treat '" + value1 + "' and '" + value2
                            + "' the same");
            assertTrue(ComparisonHelper.equals(addressDto2, addressApi2),
                    fieldDescription + " - equals() does not treat '" + value1 + "' and '" + value2
                            + "' the same");
        }
    }

    @Test
    void testEqualsNonNormalSpacesHasNoEffect() {
        String[] fieldNames = ADDRESS_FIELD_NAMES;

        String[] addressFields1 = {"Care Of", "Po Box", "House Name Num", "The Street", "The Area", "Post Town",
                "Region", "The Country", "Post Code"};
        String[] addressFields2 = Arrays.copyOf(addressFields1, addressFields1.length);

        for (int i = 0; i < addressFields1.length; i++) {
            addressFields1[i] = addressFields1[i].replace(" ", "   ");
            if (i < addressFields1.length - 1) {
                addressFields2[i + 1] = addressFields2[i + 1].replace(" ", "   ");
            }
            AddressDto addressDto1 = AddressTestUtils.createDummyAddressDto(addressFields1);
            AddressApi addressApi1 = AddressTestUtils.createDummyModelUtilsAddressApi(
                    addressFields1);

            AddressDto addressDto2 = AddressTestUtils.createDummyAddressDto(addressFields2);
            AddressApi addressApi2 = AddressTestUtils.createDummyModelUtilsAddressApi(
                    addressFields2);

            addressFields1[i] = addressFields2[i];
            if (i < addressFields1.length - 1) {
                addressFields2[i + 1] = addressFields1[i + 1];
            }
            String fieldDescription = "Field: " + fieldNames[i];

            assertTrue(ComparisonHelper.equals(addressDto1, addressApi1),
                    fieldDescription + " - equals() does not treat '" + addressFields1[i]
                            + "' and '" + addressFields2[i] + "' the same");
            assertTrue(ComparisonHelper.equals(addressDto2, addressApi2),
                    fieldDescription + " - equals() does not treat '" + addressFields1[i]
                            + "' and '" + addressFields2[i] + "' the same");
        }
    }

    @Test
    void testAddressEqualsCasingIsIgnoredOnEquals() {
        String[] controlAddressFields = ADDRESS_FIELD_NAMES;
        String[] addressFields1 = Arrays.copyOf(controlAddressFields, controlAddressFields.length);
        String[] addressFields2 = Arrays.copyOf(controlAddressFields, controlAddressFields.length);

        for (int i = 0; i < addressFields1.length; i++) {
            addressFields1[i] = addressFields1[i].toUpperCase();
            addressFields2[i] = addressFields2[i].toLowerCase();

            AddressDto addressDto1 = AddressTestUtils.createDummyAddressDto(addressFields1);
            AddressApi addressApi1 = AddressTestUtils.createDummyModelUtilsAddressApi(
                    addressFields1);

            AddressDto addressDto2 = AddressTestUtils.createDummyAddressDto(addressFields2);
            AddressApi addressApi2 = AddressTestUtils.createDummyModelUtilsAddressApi(
                    addressFields2);

            addressFields1[i] = controlAddressFields[i];
            addressFields2[i] = controlAddressFields[i];

            String fieldDescription = "Field: " + controlAddressFields[i];

            assertTrue(ComparisonHelper.equals(addressDto1, addressApi1),
                    fieldDescription + " - equals() does not treat '" + addressFields1[i]
                            + "' and '" + addressFields2[i] + "' the same");
            assertTrue(ComparisonHelper.equals(addressDto2, addressApi2),
                    fieldDescription + " - equals() does not treat '" + addressFields1[i]
                            + "' and '" + addressFields2[i] + "' the same");
        }
    }

    @Test
    void equalsAddressWhenPremisesAndLineOneAreConcatenatedWithAddressDtoAndModelUtilsAddressApi() {
        assertTrue(ComparisonHelper.equals(
                AddressTestUtils.createDummyAddressDto(
                        ADDRESS_FIELD_VALUES_CONCATENATED_FIRST_LINE),
                AddressTestUtils.createDummyModelUtilsAddressApi(ADDRESS_FIELD_NAMES)));
    }

    @Test
    void equalsAddressWhenPremisesAndLineOneAreConcatenatedWithAddressDtoAndMOAddressApi() {
        assertTrue(ComparisonHelper.equals(
                AddressTestUtils.createDummyAddressDto(
                        ADDRESS_FIELD_VALUES_CONCATENATED_FIRST_LINE),
                AddressTestUtils.createDummyManagingOfficerAddressApi(ADDRESS_FIELD_NAMES)));
    }

    @Test
    void equalsAddressWhenPremisesAndLineOneAreConcatenatedWithAddressDtoAndCommonAddress() {
        assertTrue(ComparisonHelper.equals(
                AddressTestUtils.createDummyAddressDto(
                        ADDRESS_FIELD_VALUES_CONCATENATED_FIRST_LINE),
                AddressTestUtils.createDummyCommonAddress(ADDRESS_FIELD_NAMES)));
    }

    @Test
    void equalsAddressWhenAddressesAreNotEqualWithAddressDtoAndCommonAddress() {
        String[] addressDtoFields = ADDRESS_FIELD_VALUES_CONCATENATED_FIRST_LINE.clone();
        addressDtoFields[2] = "Different" + addressDtoFields[2];
        String[] addressApiFields = ADDRESS_FIELD_NAMES;
        assertFalse(
                ComparisonHelper.equals(
                        AddressTestUtils.createDummyAddressDto(addressDtoFields),
                        AddressTestUtils.createDummyCommonAddress(addressApiFields)));
    }

    @Test
    void equalsAddressWhenAddressesAreNotEqualWithAddressDtoAndManagingOfficerAddressApi() {
        String[] addressDtoFields = ADDRESS_FIELD_VALUES_CONCATENATED_FIRST_LINE.clone();
        addressDtoFields[2] = "Different" + addressDtoFields[2];
        String[] addressApiFields = ADDRESS_FIELD_NAMES;
        assertFalse(
                ComparisonHelper.equals(AddressTestUtils.createDummyAddressDto(addressDtoFields),
                        AddressTestUtils.createDummyManagingOfficerAddressApi(addressApiFields)));
    }

    @Test
    void equalsAddressWhenAddressesAreNotEqualWithAddressDtoAndMOAddressApi() {
        String[] addressDtoFields = ADDRESS_FIELD_VALUES_CONCATENATED_FIRST_LINE.clone();
        addressDtoFields[2] = "Different" + addressDtoFields[2];
        String[] addressApiFields = ADDRESS_FIELD_NAMES;
        assertFalse(
                ComparisonHelper.equals(AddressTestUtils.createDummyAddressDto(addressDtoFields),
                        AddressTestUtils.createDummyManagingOfficerAddressApi(addressApiFields)));
    }

    @Test
    void equalsAddressDtoAndAddressApiWhenNullReturnFalse() {
        AddressApi addressApi = new AddressApi();
        assertTrue(ComparisonHelper.equals(null, (AddressApi) null));
        assertFalse(ComparisonHelper.equals(null, addressApi));
        assertFalse(ComparisonHelper.equals(new AddressDto(), (AddressApi) null));
    }

    @Test
    void equalsLocalDateAndStringWithTimeReturnCorrectResult() {
        LocalDate localDate = LocalDate.of(2001, 2, 3);
        String dateString = "2001-02-03 00:00:00.000000";

        assertTrue(ComparisonHelper.equals(localDate, dateString));
    }

    @Test
    void equalsLocalDateAndStringWithoutTimeReturnCorrectResult() {
        LocalDate localDate = LocalDate.of(2001, 2, 3);
        String dateString = "2001-02-03";

        assertTrue(ComparisonHelper.equals(localDate, dateString));
    }

    @Test
    void equalsLocalDateWhenNull() {
        LocalDate localDate = LocalDate.of(2001, 2, 3);
        String strDate = "2001-02-03";

        assertTrue(ComparisonHelper.equals((LocalDate) null, null));
        assertFalse(ComparisonHelper.equals((LocalDate) null, strDate));
        assertFalse(ComparisonHelper.equals(localDate, null));
    }

    @ParameterizedTest
    @MethodSource("provideTestDataForNatureOfControlsEquals")
    void testNatureOfControlsEquals(List<String> chipsFormatList, String[] publicDataFormat,
            boolean expectedResult) {
        assertEquals(expectedResult,
                ComparisonHelper.natureOfControlsEquals(chipsFormatList, publicDataFormat));
    }

    @Test
    void equalsBooleanNull() {
        Boolean first = null;
        boolean second = true;
        assertFalse(ComparisonHelper.equals(first, second));
    }

    @Test
    void equalsBooleanAllFalse() {
        Boolean first = false;
        boolean second = false;
        assertTrue(ComparisonHelper.equals(first, second));
    }

    @Test
    void equalsBooleanAllTrue() {
        Boolean first = true;
        boolean second = true;
        assertTrue(ComparisonHelper.equals(first, second));
    }

    @Test
    void equalsBooleanOneFalse() {
        Boolean first = false;
        boolean second = true;
        assertFalse(ComparisonHelper.equals(first, second));
    }

    @Test
    void equalsBooleanOtherFalse() {
        Boolean first = true;
        boolean second = false;
        assertFalse(ComparisonHelper.equals(first, second));
    }

    @Test
    void equalsStringAndOfficerRoleApiReturnCorrectResult() {
        String string = "managing-officer";
        var officerRoleApi = OfficerRoleApi.MANAGING_OFFICER;

        assertTrue(ComparisonHelper.equals(string, officerRoleApi));
    }

    @Test
    void equalsStringAndOfficerRoleApiDifferentValuesReturnFalse() {
        String string = "different";
        var officerRoleApi = OfficerRoleApi.MANAGING_OFFICER;

        assertFalse(ComparisonHelper.equals(string, officerRoleApi));
    }

    @Test
    void equalsStringAndOfficerRoleApiWhenNullReturnCorrectResult() {
        String string = "managing-officer";
        var officerRoleApi = OfficerRoleApi.MANAGING_OFFICER;

        assertTrue(ComparisonHelper.equals(null, (OfficerRoleApi) null));
        assertFalse(ComparisonHelper.equals(null, officerRoleApi));
        assertFalse(ComparisonHelper.equals(string, (OfficerRoleApi) null));
    }

    @Test
    void equalsStringAndStringArrayReturnCorrectResult() {
        String string = "test1 test2";
        String[] strings = {"test1", "test2"};

        assertTrue(ComparisonHelper.equals(string, strings));
    }

    @Test
    void equalsStringAndStringArrayDifferentValuesReturnFalse() {
        String string = "test1";
        String[] strings = {"test1", "test2"};

        assertFalse(ComparisonHelper.equals(string, strings));
    }

    @Test
    void equalsStringAndStringArrayWhenNullReturnCorrectResult() {
        String string = "test1 test2";
        String[] strings = {"test1", "test2"};

        assertTrue(ComparisonHelper.equals(null, (String[]) null));
        assertFalse(ComparisonHelper.equals(null, strings));
        assertFalse(ComparisonHelper.equals(string, (String[]) null));
    }

    @Test
    void equalsFormerNameReturnCorrectResult() {
        String string = "John James Doe, Jonathan Doe";
        List<FormerNamesApi> formerNamesApiList = List.of(
                new FormerNamesApi() {{
                    setForenames("John James");
                    setSurname("Doe");
                }},
                new FormerNamesApi() {{
                    setForenames("Jonathan");
                    setSurname("Doe");
                }}
        );

        assertTrue(ComparisonHelper.equalsFormerName(string, formerNamesApiList));
    }

    @Test
    void equalsFormerNamesDifferentValuesReturnFalse() {
        String string = "John James Doe";
        List<FormerNamesApi> formerNamesApiList = List.of(
                new FormerNamesApi() {{
                    setForenames("John James");
                    setSurname("Doe");
                }},
                new FormerNamesApi() {{
                    setForenames("Jonathan");
                    setSurname("Doe");
                }}
        );

        assertFalse(ComparisonHelper.equalsFormerName(string, formerNamesApiList));
    }

    @Test
    void equalsFormerNamesBothNullReturnCorrectResult() {
        String string = "John James Doe, Jonathan Doe";
        List<FormerNamesApi> formerNamesApiList = List.of(
                new FormerNamesApi() {{
                    setForenames("John James");
                    setSurname("Doe");
                }},
                new FormerNamesApi() {{
                    setForenames("Jonathan");
                    setSurname("Doe");
                }}
        );

        assertTrue(ComparisonHelper.equalsFormerName(null, null));
        assertFalse(ComparisonHelper.equalsFormerName(null, formerNamesApiList));
        assertFalse(ComparisonHelper.equalsFormerName(string, null));
    }

    @Test
    void equalsAddressDtoAndMoDataAddressApiReturnCorrectResult() {
        String[] addressFields = ADDRESS_FIELD_NAMES;

        var addressDto = AddressTestUtils.createDummyAddressDto(addressFields);
        var addressApi = AddressTestUtils.createDummyManagingOfficerAddressApi(addressFields);

        assertTrue(ComparisonHelper.equals(addressDto, addressApi));
    }

    @Test
    void equalsAddressDtoAndMoDataAddressApiReturnFalse() {
        String[] addressDtoFields = ADDRESS_FIELD_NAMES.clone();
        String[] addressApiFields = addressDtoFields.clone();

        var output = false;

        for (var i = 0; i < addressDtoFields.length; i++) {
            addressDtoFields[i] = "Different--" + addressDtoFields[i];
            if (i < addressDtoFields.length - 1) {
                addressApiFields[i + 1] = "Different--" + addressApiFields[i + 1];
            }

            var addressDto = AddressTestUtils.createDummyAddressDto(addressDtoFields);
            var addressApi = AddressTestUtils.createDummyManagingOfficerAddressApi(
                    addressApiFields);

            output |= ComparisonHelper.equals(addressDto, addressApi);

            if (i < addressDtoFields.length - 1) {
                addressDtoFields[i] = addressApiFields[i];
                addressApiFields[i + 1] = addressDtoFields[i + 1];
            }
        }

        assertFalse(output);
    }

    @Test
    void equalsAddressDtoAndAddressApiReturnFalse() {

        String[] addressDtoFields = ADDRESS_FIELD_NAMES.clone();
        String[] addressApiFields = addressDtoFields.clone();

        var output = false;

        for (var i = 0; i < addressDtoFields.length; i++) {
            addressDtoFields[i] = "Different--" + addressDtoFields[i];
            if (i < addressDtoFields.length - 1) {
                addressApiFields[i + 1] = "Different--" + addressApiFields[i + 1];
            }

            var addressDto = AddressTestUtils.createDummyAddressDto(addressDtoFields);
            var addressApi = AddressTestUtils.createDummyModelUtilsAddressApi(addressApiFields);

            output |= ComparisonHelper.equals(addressDto, addressApi);

            if (i < addressDtoFields.length - 1) {
                addressDtoFields[i] = addressApiFields[i];
                addressApiFields[i + 1] = addressDtoFields[i + 1];
            }
        }

        assertFalse(output);
    }


    @Test
    void equalsAddressDtoAndMoDataAddressApiWhenNullReturnCorrectResult() {
        String[] addressFields = ADDRESS_FIELD_NAMES;

        var addressDto = AddressTestUtils.createDummyAddressDto(addressFields);
        var addressApi = AddressTestUtils.createDummyManagingOfficerAddressApi(addressFields);

        assertTrue(ComparisonHelper.equals(null,
                (uk.gov.companieshouse.api.model.managingofficerdata.AddressApi) null));
        assertFalse(ComparisonHelper.equals(null, addressApi));
        assertFalse(ComparisonHelper.equals(addressDto,
                (uk.gov.companieshouse.api.model.managingofficerdata.AddressApi) null));
    }

    @Test
    void equalsAddressDtoAndAddressReturnCorrectResult() {
        String[] addressFields = ADDRESS_FIELD_NAMES;

        var addressDto = AddressTestUtils.createDummyAddressDto(addressFields);
        var address = AddressTestUtils.createDummyCommonAddress(addressFields);

        assertTrue(ComparisonHelper.equals(addressDto, address));
    }

    @Test
    void equalsAddressDtoAndAddressReturnFalse() {

        String[] addressDtoFields = ADDRESS_FIELD_NAMES.clone();
        String[] addressFields = addressDtoFields.clone();

        var output = false;

        for (var i = 0; i < addressDtoFields.length; i++) {
            addressDtoFields[i] = "Different--" + addressDtoFields[i];
            if (i < addressDtoFields.length - 1) {
                addressFields[i + 1] = "Different--" + addressFields[i + 1];
            }

            var addressDto = AddressTestUtils.createDummyAddressDto(addressDtoFields);
            var address = AddressTestUtils.createDummyCommonAddress(addressFields);

            output |= ComparisonHelper.equals(addressDto, address);

            if (i < addressDtoFields.length - 1) {
                addressDtoFields[i] = addressFields[i];
                addressFields[i + 1] = addressDtoFields[i + 1];
            }
        }

        assertFalse(output);
    }

    @Test
    void equalsAddressDtoAndAddressWhenNullReturnCorrectResult() {
        String[] addressFields = ADDRESS_FIELD_NAMES;

        var addressDto = AddressTestUtils.createDummyAddressDto(addressFields);
        var address = AddressTestUtils.createDummyCommonAddress(addressFields);

        assertTrue(ComparisonHelper.equals(null, (Address) null));
        assertFalse(ComparisonHelper.equals(null, address));
        assertFalse(ComparisonHelper.equals(addressDto, (Address) null));
    }

    @Test
    void equalsPersonNameAndStringReturnCorrectResult() {
        var personName = new PersonName("John", "Doe");
        var string = "John Doe";

        assertTrue(ComparisonHelper.equals(personName, string));
    }

    @Test
    void equalsPersonNameAndStringDifferentReturnFalse() {
        var personName = new PersonName("John", "Doe");
        var string = "Johnny Doe";

        assertFalse(ComparisonHelper.equals(personName, string));
    }

    @Test
    void equalsPersonNameAndStringWhenNullReturnCorrectResult() {
        var personName = new PersonName("John", "Doe");
        var string = "John Doe";

        assertTrue(ComparisonHelper.equals((PersonName) null, null));
        assertFalse(ComparisonHelper.equals((PersonName) null, string));
        assertFalse(ComparisonHelper.equals(personName, null));
    }

    private static Stream<Arguments> provideTestCasesForEqualsIndividualNationality() {
        return Stream.of(
                Arguments.of("American", "American", true),
                Arguments.of("American,British", "American,British", true),
                Arguments.of("American , British", "American,British", true),
                Arguments.of("American,British,", "American,British", true),
                Arguments.of("American,British", "American, British,", true),
                Arguments.of("American,British", "American ,British", true),
                Arguments.of("American", "British", false),
                Arguments.of("American,French", "American,British", false),
                Arguments.of("American , French", "American,British", false),
                Arguments.of("American, French", "American,British ", false),
                Arguments.of("American,French", "French,American ", false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestCasesForEqualsIndividualNationality")
    void testEqualsIndividualNationality(String nationality1, String nationality2, boolean expectedOutcome) {
        assertEquals(expectedOutcome, ComparisonHelper.equalsIndividualNationality(nationality1, nationality2));
    }

}
