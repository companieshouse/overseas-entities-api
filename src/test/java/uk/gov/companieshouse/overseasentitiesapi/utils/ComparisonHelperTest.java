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

class ComparisonHelperTest {

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

    @Test
    void equalsAddressDtoAndAddressApiReturnCorrectResult() {
        AddressDto addressDto = new AddressDto();
        addressDto.setPropertyNameNumber("PropertyNameNumber");
        addressDto.setLine1("Line1");
        addressDto.setLine2("Line2");
        addressDto.setTown("Town");
        addressDto.setCounty("County");
        addressDto.setCountry("Country");
        addressDto.setPoBox("PoBox");
        addressDto.setCareOf("CareOf");
        addressDto.setPostcode("Postcode");

        AddressApi addressApi = new AddressApi();
        addressApi.setPremises("PropertyNameNumber");
        addressApi.setAddressLine1("Line1");
        addressApi.setAddressLine2("Line2");
        addressApi.setLocality("Town");
        addressApi.setRegion("County");
        addressApi.setCountry("Country");
        addressApi.setPoBox("PoBox");
        addressApi.setCareOf("CareOf");
        addressApi.setPostcode("Postcode");

        assertTrue(ComparisonHelper.equals(addressDto, addressApi));
    }

    @Test
    void equalsAddressDtoAndAddressApiCompleteFalseTest() {
        String[] addressDtoFields = {"PropertyNameNumber", "Line1", "Line2", "Town", "County", "Country", "PoBox", "CareOf", "Postcode"};
        String[] addressApiFields = addressDtoFields.clone();

        var output = false;

        for (var i = 0; i < addressDtoFields.length; i++) {
            addressDtoFields[i] = "Different--" + addressDtoFields[i];
            if (i < addressDtoFields.length - 1) {
                addressApiFields[i + 1] = "Different--" + addressApiFields[i + 1];
            }

            AddressDto addressDto = new AddressDto();
            addressDto.setPropertyNameNumber(addressDtoFields[0]);
            addressDto.setLine1(addressDtoFields[1]);
            addressDto.setLine2(addressDtoFields[2]);
            addressDto.setTown(addressDtoFields[3]);
            addressDto.setCounty(addressDtoFields[4]);
            addressDto.setCountry(addressDtoFields[5]);
            addressDto.setPoBox(addressDtoFields[6]);
            addressDto.setCareOf(addressDtoFields[7]);
            addressDto.setPostcode(addressDtoFields[8]);

            AddressApi addressApi = new AddressApi();
            addressApi.setPremises(addressApiFields[0]);
            addressApi.setAddressLine2(addressApiFields[1]);
            addressApi.setAddressLine1(addressApiFields[2]);
            addressApi.setLocality(addressApiFields[3]);
            addressApi.setRegion(addressApiFields[4]);
            addressApi.setCountry(addressApiFields[5]);
            addressApi.setPoBox(addressApiFields[6]);
            addressApi.setCareOf(addressApiFields[7]);
            addressApi.setPostcode(addressApiFields[8]);

            output |= ComparisonHelper.equals(addressDto, addressApi);

            if (i < addressDtoFields.length - 1) {
                addressDtoFields[i] = addressApiFields[i];
                addressApiFields[i + 1] = addressDtoFields[i + 1];
            }
        }
        assertFalse(output);
    }

    private static Stream<Arguments> provideTestArguments() {
        return Stream.of(
                Arguments.of(null, " "),
                Arguments.of(null, ""),
                Arguments.of(" ", "")
        );
    }

    @Test
    void equalsAddressDtoAndAddressApiReturnFalseWhenOneFieldDiffers() {
        AddressDto addressDto = new AddressDto();
        addressDto.setPropertyNameNumber("PropertyNameNumber");
        addressDto.setLine1("Line1");
        addressDto.setLine2("Line2");
        addressDto.setTown("Town");
        addressDto.setCounty("County");
        addressDto.setCountry("Country");
        addressDto.setPoBox("PoBox");
        addressDto.setCareOf("CareOf");
        addressDto.setPostcode("Postcode");

        AddressApi addressApi = new AddressApi();
        addressApi.setPremises("PropertyNameNumber");
        addressApi.setAddressLine2("Line1");
        addressApi.setAddressLine1("Line2");
        addressApi.setLocality("Town");
        addressApi.setRegion("County");
        addressApi.setCountry("DifferentCountry");
        addressApi.setPoBox("PoBox");
        addressApi.setCareOf("CareOf");
        addressApi.setPostcode("Postcode");

        assertFalse(ComparisonHelper.equals(addressDto, addressApi));
    }

    @ParameterizedTest
    @MethodSource("provideTestArguments")
    void testEquality(String value1, String value2) {
        String[] controlAddressFields = {"careOf", "poBox", "careOfCompany", "houseNameNum",
                "street", "area", "postTown", "region", "postCode", "country"};
        String[] addressFields1 = Arrays.copyOf(controlAddressFields, controlAddressFields.length);
        String[] addressFields2 = Arrays.copyOf(controlAddressFields, controlAddressFields.length);

        for (int i = 0; i < addressFields1.length; i++) {
            addressFields1[i] = value1;
            addressFields2[i] = value2;

            AddressDto addressDto1 = createTestAddressDto(addressFields1);
            AddressApi addressApi1 = createTestAddressApi(addressFields1);

            AddressDto addressDto2 = createTestAddressDto(addressFields2);
            AddressApi addressApi2 = createTestAddressApi(addressFields2);

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
        String[] fieldNames = {"careOf", "poBox", "careOfCompany", "houseNameNum", "street", "area",
                "postTown", "region", "postCode", "country"};
        String[] addressFields1 = {"Care Of", "Po Box", "Care Of Company", "House Name Num",
                "The Street", "The Area", "Post Town", "Region", "Post Code", "The Country"};
        String[] addressFields2 = Arrays.copyOf(addressFields1, addressFields1.length);

        for (int i = 0; i < addressFields1.length; i++) {
            addressFields1[i] = addressFields1[i].replace(" ", "   ");
            if (i < addressFields1.length - 1) {
                addressFields2[i + 1] = addressFields2[i + 1].replace(" ", "   ");
            }
            AddressDto addressDto1 = createTestAddressDto(addressFields1);
            AddressApi addressApi1 = createTestAddressApi(addressFields1);

            AddressDto addressDto2 = createTestAddressDto(addressFields2);
            AddressApi addressApi2 = createTestAddressApi(addressFields2);

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

    private AddressDto createTestAddressDto(String[] fields) {
        AddressDto addressDto = new AddressDto();

        addressDto.setCareOf(fields[0]);
        addressDto.setPoBox(fields[1]);
        addressDto.setCareOf(fields[2]);
        addressDto.setPropertyNameNumber(fields[3]);
        addressDto.setLine1(fields[4]);
        addressDto.setLine2(fields[5]);
        addressDto.setTown(fields[6]);
        addressDto.setCounty(fields[7]);
        addressDto.setPostcode(fields[8]);
        addressDto.setCountry(fields[9]);

        return addressDto;
    }

    private AddressApi createTestAddressApi(String[] fields) {
        AddressApi addressApi = new AddressApi();

        addressApi.setCareOf(fields[0]);
        addressApi.setPoBox(fields[1]);
        addressApi.setCareOf(fields[2]);
        addressApi.setPremises(fields[3]);
        addressApi.setAddressLine1(fields[4]);
        addressApi.setAddressLine2(fields[5]);
        addressApi.setLocality(fields[6]);
        addressApi.setRegion(fields[7]);
        addressApi.setPostcode(fields[8]);
        addressApi.setCountry(fields[9]);

        return addressApi;
    }

    @Test
    void testAddressEqualsCasingIsIgnoredOnEquals() {
        String[] controlAddressFields = {"careOf", "poBox", "careOfCompany", "houseNameNum",
                "street", "area", "postTown", "region", "postCode", "country"};
        String[] addressFields1 = Arrays.copyOf(controlAddressFields, controlAddressFields.length);
        String[] addressFields2 = Arrays.copyOf(controlAddressFields, controlAddressFields.length);

        for (int i = 0; i < addressFields1.length; i++) {
            addressFields1[i] = addressFields1[i].toUpperCase();
            addressFields2[i] = addressFields2[i].toLowerCase();

            AddressDto addressDto1 = createTestAddressDto(addressFields1);
            AddressApi addressApi1 = createTestAddressApi(addressFields1);

            AddressDto addressDto2 = createTestAddressDto(addressFields2);
            AddressApi addressApi2 = createTestAddressApi(addressFields2);

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
    void equalsAddressDtoAndAddressApiReturnFalseWhenMultipleFieldsDiffer() {
        AddressDto addressDto = new AddressDto();
        addressDto.setPropertyNameNumber("PropertyNameNumber");
        addressDto.setLine1("Line1");
        addressDto.setLine2("Line2");
        addressDto.setTown("Town");
        addressDto.setCounty("County");
        addressDto.setCountry("Country");
        addressDto.setPoBox("PoBox");
        addressDto.setCareOf("CareOf");
        addressDto.setPostcode("Postcode");

        AddressApi addressApi = new AddressApi();
        addressApi.setPremises("DifferentPropertyNameNumber");
        addressApi.setAddressLine2("DifferentLine1");
        addressApi.setAddressLine1("Line2");
        addressApi.setLocality("Town");
        addressApi.setRegion("County");
        addressApi.setCountry("Country");
        addressApi.setPoBox("DifferentPoBox");
        addressApi.setCareOf("CareOf");
        addressApi.setPostcode("Postcode");

        assertFalse(ComparisonHelper.equals(addressDto, addressApi));
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

        assertTrue(ComparisonHelper.equals((String) null, (OfficerRoleApi) null));
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

        assertTrue(ComparisonHelper.equals((String) null, (String[]) null));
        assertFalse(ComparisonHelper.equals((String) null, strings));
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
        var addressDto = new AddressDto();
        addressDto.setPropertyNameNumber("PropertyNameNumber");
        addressDto.setLine1("Line1");
        addressDto.setLine2("Line2");
        addressDto.setTown("Town");
        addressDto.setCounty("County");
        addressDto.setCountry("Country");
        addressDto.setPoBox("PoBox");
        addressDto.setCareOf("CareOf");
        addressDto.setPostcode("Postcode");

        var addressApi = new uk.gov.companieshouse.api.model.managingofficerdata.AddressApi();
        addressApi.setPremises("PropertyNameNumber");
        addressApi.setAddressLine1("Line1");
        addressApi.setAddressLine2("Line2");
        addressApi.setLocality("Town");
        addressApi.setRegion("County");
        addressApi.setCountry("Country");
        addressApi.setPoBox("PoBox");
        addressApi.setCareOf("CareOf");
        addressApi.setPostalCode("Postcode");

        assertTrue(ComparisonHelper.equals(addressDto, addressApi));
    }

    @Test
    void equalsAddressDtoAndMoDataAddressApiReturnFalse() {

        String[] addressDtoFields = {"PropertyNameNumber", "Line1", "Line2", "Town", "County", "Country", "PoBox", "CareOf", "Postcode"};
        String[] addressApiFields = addressDtoFields.clone();

        var output = false;

        for (var i = 0; i < addressDtoFields.length; i++) {
            addressDtoFields[i] = "Different--" + addressDtoFields[i];
            if (i < addressDtoFields.length - 1) {
                addressApiFields[i + 1] = "Different--" + addressApiFields[i + 1];
            }

            var addressDto = new AddressDto();
            addressDto.setPropertyNameNumber(addressDtoFields[0]);
            addressDto.setLine1(addressDtoFields[1]);
            addressDto.setLine2(addressDtoFields[2]);
            addressDto.setTown(addressDtoFields[3]);
            addressDto.setCounty(addressDtoFields[4]);
            addressDto.setCountry(addressDtoFields[5]);
            addressDto.setPoBox(addressDtoFields[6]);
            addressDto.setCareOf(addressDtoFields[7]);
            addressDto.setPostcode(addressDtoFields[8]);

            var addressApi = new uk.gov.companieshouse.api.model.managingofficerdata.AddressApi();
            addressApi.setPremises(addressApiFields[0]);
            addressApi.setAddressLine1(addressApiFields[1]);
            addressApi.setAddressLine2(addressApiFields[2]);
            addressApi.setLocality(addressApiFields[3]);
            addressApi.setRegion(addressApiFields[4]);
            addressApi.setCountry(addressApiFields[5]);
            addressApi.setPoBox(addressApiFields[6]);
            addressApi.setCareOf(addressApiFields[7]);
            addressApi.setPostalCode(addressApiFields[8]);

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

        String[] addressDtoFields = {"PropertyNameNumber", "Line1", "Line2", "Town", "County", "Country", "PoBox", "CareOf", "Postcode"};
        String[] addressApiFields = addressDtoFields.clone();

        var output = false;

        for (var i = 0; i < addressDtoFields.length; i++) {
            addressDtoFields[i] = "Different--" + addressDtoFields[i];
            if (i < addressDtoFields.length - 1) {
                addressApiFields[i + 1] = "Different--" + addressApiFields[i + 1];
            }

            var addressDto = new AddressDto();
            addressDto.setPropertyNameNumber(addressDtoFields[0]);
            addressDto.setLine1(addressDtoFields[1]);
            addressDto.setLine2(addressDtoFields[2]);
            addressDto.setTown(addressDtoFields[3]);
            addressDto.setCounty(addressDtoFields[4]);
            addressDto.setCountry(addressDtoFields[5]);
            addressDto.setPoBox(addressDtoFields[6]);
            addressDto.setCareOf(addressDtoFields[7]);
            addressDto.setPostcode(addressDtoFields[8]);

            var addressApi = new AddressApi();
            addressApi.setPremises(addressApiFields[0]);
            addressApi.setAddressLine1(addressApiFields[1]);
            addressApi.setAddressLine2(addressApiFields[2]);
            addressApi.setLocality(addressApiFields[3]);
            addressApi.setRegion(addressApiFields[4]);
            addressApi.setCountry(addressApiFields[5]);
            addressApi.setPoBox(addressApiFields[6]);
            addressApi.setCareOf(addressApiFields[7]);
            addressApi.setPostcode(addressApiFields[8]);

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
        var addressDto = new AddressDto();
        addressDto.setPropertyNameNumber("PropertyNameNumber");
        addressDto.setLine1("Line1");
        addressDto.setLine2("Line2");
        addressDto.setTown("Town");
        addressDto.setCounty("County");
        addressDto.setCountry("Country");
        addressDto.setPoBox("PoBox");
        addressDto.setCareOf("CareOf");
        addressDto.setPostcode("Postcode");

        var addressApi = new uk.gov.companieshouse.api.model.managingofficerdata.AddressApi();
        addressApi.setPremises("PropertyNameNumber");
        addressApi.setAddressLine1("Line1");
        addressApi.setAddressLine2("Line2");
        addressApi.setLocality("Town");
        addressApi.setRegion("County");
        addressApi.setCountry("Country");
        addressApi.setPoBox("PoBox");
        addressApi.setCareOf("CareOf");
        addressApi.setPostalCode("Postcode");

        assertTrue(ComparisonHelper.equals(null,
                (uk.gov.companieshouse.api.model.managingofficerdata.AddressApi) null));
        assertFalse(ComparisonHelper.equals(null, addressApi));
        assertFalse(ComparisonHelper.equals(addressDto,
                (uk.gov.companieshouse.api.model.managingofficerdata.AddressApi) null));
    }

    @Test
    void equalsAddressDtoAndAddressReturnCorrectResult() {
        var addressDto = new AddressDto();
        addressDto.setPropertyNameNumber("PropertyNameNumber");
        addressDto.setLine1("Line1");
        addressDto.setLine2("Line2");
        addressDto.setTown("Town");
        addressDto.setCounty("County");
        addressDto.setCountry("Country");
        addressDto.setPoBox("PoBox");
        addressDto.setCareOf("CareOf");
        addressDto.setPostcode("Postcode");

        var address = new Address();
        address.setPremises("PropertyNameNumber");
        address.setAddressLine1("Line1");
        address.setAddressLine2("Line2");
        address.setLocality("Town");
        address.setRegion("County");
        address.setCountry("Country");
        address.setPoBox("PoBox");
        address.setCareOf("CareOf");
        address.setPostalCode("Postcode");

        assertTrue(ComparisonHelper.equals(addressDto, address));
    }

    @Test
    void equalsAddressDtoAndAddressReturnFalse() {

        String[] addressDtoFields = {"PropertyNameNumber", "Line1", "Line2", "Town", "County", "Country", "PoBox", "CareOf", "Postcode"};
        String[] addressFields = addressDtoFields.clone();

        var output = false;

        for (var i = 0; i < addressDtoFields.length; i++) {
            addressDtoFields[i] = "Different--" + addressDtoFields[i];
            if (i < addressDtoFields.length - 1) {
                addressFields[i + 1] = "Different--" + addressFields[i + 1];
            }

            var addressDto = new AddressDto();
            addressDto.setPropertyNameNumber(addressDtoFields[0]);
            addressDto.setLine1(addressDtoFields[1]);
            addressDto.setLine2(addressDtoFields[2]);
            addressDto.setTown(addressDtoFields[3]);
            addressDto.setCounty(addressDtoFields[4]);
            addressDto.setCountry(addressDtoFields[5]);
            addressDto.setPoBox(addressDtoFields[6]);
            addressDto.setCareOf(addressDtoFields[7]);
            addressDto.setPostcode(addressDtoFields[8]);

            var address = new Address();
            address.setPremises(addressFields[0]);
            address.setAddressLine1(addressFields[1]);
            address.setAddressLine2(addressFields[2]);
            address.setLocality(addressFields[3]);
            address.setRegion(addressFields[4]);
            address.setCountry(addressFields[5]);
            address.setPoBox(addressFields[6]);
            address.setCareOf(addressFields[7]);
            address.setPostalCode(addressFields[8]);

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
        var addressDto = new AddressDto();
        addressDto.setPropertyNameNumber("PropertyNameNumber");
        addressDto.setLine1("Line1");
        addressDto.setLine2("Line2");
        addressDto.setTown("Town");
        addressDto.setCounty("County");
        addressDto.setCountry("Country");
        addressDto.setPoBox("PoBox");
        addressDto.setCareOf("CareOf");
        addressDto.setPostcode("Postcode");

        var address = new Address();
        address.setPremises("PropertyNameNumber");
        address.setAddressLine1("Line1");
        address.setAddressLine2("Line2");
        address.setLocality("Town");
        address.setRegion("County");
        address.setCountry("Country");
        address.setPoBox("PoBox");
        address.setCareOf("CareOf");
        address.setPostalCode("Postcode");

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

        assertTrue(ComparisonHelper.equals((PersonName) null, (String) null));
        assertFalse(ComparisonHelper.equals((PersonName) null, string));
        assertFalse(ComparisonHelper.equals(personName, null));
    }
}
