package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels;

import static com.mongodb.assertions.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class AddressTest {

    @Mock
    private Address address;

    @BeforeEach
    void setUp() {
        address = new Address();
    }

    @Test
    void testGettersAndSetters() {
        String careOf = "Care Of";
        String poBox = "PO Box";
        String careOfCompany = "Care Of Company";
        String houseNameNum = "House Name Num";
        String street = "Street";
        String area = "Area";
        String postTown = "Post Town";
        String region = "Region";
        String postCode = "Post Code";
        String country = "Country";

        address.setCareOf(careOf);
        address.setPoBox(poBox);
        address.setCareOfCompany(careOfCompany);
        address.setHouseNameNum(houseNameNum);
        address.setStreet(street);
        address.setArea(area);
        address.setPostTown(postTown);
        address.setRegion(region);
        address.setPostCode(postCode);
        address.setCountry(country);

        assertEquals(careOf, address.getCareOf());
        assertEquals(poBox, address.getPoBox());
        assertEquals(careOfCompany, address.getCareOfCompany());
        assertEquals(houseNameNum, address.getHouseNameNum());
        assertEquals(street, address.getStreet());
        assertEquals(area, address.getArea());
        assertEquals(postTown, address.getPostTown());
        assertEquals(region, address.getRegion());
        assertEquals(postCode, address.getPostCode());
        assertEquals(country, address.getCountry());
    }

    @Test
    void testEqualsAndHashCode() {
        Address address1 = new Address();
        address1.setCareOf("Care Of");
        address1.setPoBox("PO Box");
        address1.setCareOfCompany("Care Of Company");
        address1.setHouseNameNum("House Name Num");
        address1.setStreet("Street");
        address1.setArea("Area");
        address1.setPostTown("Post Town");
        address1.setRegion("Region");
        address1.setPostCode("Post Code");
        address1.setCountry("Country");

        Address address2 = new Address();
        address2.setCareOf("Care Of");
        address2.setPoBox("PO Box");
        address2.setCareOfCompany("Care Of Company");
        address2.setHouseNameNum("House Name Num");
        address2.setStreet("Street");
        address2.setArea("Area");
        address2.setPostTown("Post Town");
        address2.setRegion("Region");
        address2.setPostCode("Post Code");
        address2.setCountry("Country");

        assertEquals(address1, address2);
        assertEquals(address1.hashCode(), address2.hashCode());
    }

    @Test
    void testNullValues() {
        address.setCareOf(null);
        address.setPoBox(null);
        address.setCareOfCompany(null);
        address.setHouseNameNum(null);
        address.setStreet(null);
        address.setArea(null);
        address.setPostTown(null);
        address.setRegion(null);
        address.setPostCode(null);
        address.setCountry(null);

        assertNull(address.getCareOf());
        assertNull(address.getPoBox());
        assertNull(address.getCareOfCompany());
        assertNull(address.getHouseNameNum());
        assertNull(address.getStreet());
        assertNull(address.getArea());
        assertNull(address.getPostTown());
        assertNull(address.getRegion());
        assertNull(address.getPostCode());
        assertNull(address.getCountry());
    }

    @Test
    void testEqualsAndHashCodeFailure() {
        String[] addressFields = {"careOf", "poBox", "careOfCompany", "houseNameNum", "street",
                "area", "postTown", "region", "postCode", "country"};
        String[] addressFields2 = Arrays.copyOf(addressFields, addressFields.length);

        for (int i = 0; i < addressFields.length; i++) {
            addressFields[i] = "Diff " + addressFields[i];
            if (i < addressFields.length - 1) {
                addressFields2[i + 1] = "Different " + addressFields2[i + 1];
            }
            Address address1 = createTestAddress(addressFields);
            Address address2 = createTestAddress(addressFields2);

            addressFields[i] = addressFields2[i];
            if (i < addressFields.length - 1) {
                addressFields2[i + 1] = addressFields[i + 1];
            }
            assertNotEquals(address1, address2, "Equals() test failed for " + addressFields[i]);
            assertNotEquals(address1.hashCode(), address2.hashCode(),
                    "HashCode() test failed for " + addressFields[i]);
        }
    }

    @Test
    void testEqualsSpaceAndNullAreTreatedTheSame() {
        String[] controlAddressFields = {"careOf", "poBox", "careOfCompany", "houseNameNum",
                "street", "area", "postTown", "region", "postCode", "country"};
        String[] addressFields1 = Arrays.copyOf(controlAddressFields, controlAddressFields.length);
        String[] addressFields2 = Arrays.copyOf(controlAddressFields, controlAddressFields.length);

        for (int i = 0; i < addressFields1.length; i++) {
            addressFields1[i] = null;
            addressFields2[i] = " ";

            Address address1 = createTestAddress(addressFields1);
            Address address2 = createTestAddress(addressFields2);

            addressFields1[i] = controlAddressFields[i];
            addressFields2[i] = controlAddressFields[i];

            String fieldDescription = "Field: " + controlAddressFields[i];
            assertEquals(address1, address2,
                    fieldDescription + " - equals() does not treat null and space the same");
            assertEquals(address1.hashCode(), address2.hashCode(),
                    fieldDescription + " - hashCode() does not treat null and space the same");
        }
    }

    @Test
    void testEqualsBlankStringAndNullAreTreatedTheSame() {
        String[] controlAddressFields = {"careOf", "poBox", "careOfCompany", "houseNameNum",
                "street", "area", "postTown", "region", "postCode", "country"};
        String[] addressFields1 = Arrays.copyOf(controlAddressFields, controlAddressFields.length);
        String[] addressFields2 = Arrays.copyOf(controlAddressFields, controlAddressFields.length);

        for (int i = 0; i < addressFields1.length; i++) {
            addressFields1[i] = null;
            addressFields2[i] = "";

            Address address1 = createTestAddress(addressFields1);
            Address address2 = createTestAddress(addressFields2);

            addressFields1[i] = controlAddressFields[i];
            addressFields2[i] = controlAddressFields[i];

            String fieldDescription = "Field: " + controlAddressFields[i];
            assertEquals(address1, address2,
                    fieldDescription + " - equals() does not treat null and blank string the same");
            assertEquals(address1.hashCode(), address2.hashCode(), fieldDescription
                    + " - hashCode() does not treat null and blank string the same");
        }
    }

    @Test
    void testEqualsBlankStringAndSpaceAreTreatedTheSame() {
        String[] controlAddressFields = {"careOf", "poBox", "careOfCompany", "houseNameNum",
                "street", "area", "postTown", "region", "postCode", "country"};
        String[] addressFields1 = Arrays.copyOf(controlAddressFields, controlAddressFields.length);
        String[] addressFields2 = Arrays.copyOf(controlAddressFields, controlAddressFields.length);

        for (int i = 0; i < addressFields1.length; i++) {
            addressFields1[i] = " ";
            addressFields2[i] = "";

            Address address1 = createTestAddress(addressFields1);
            Address address2 = createTestAddress(addressFields2);

            addressFields1[i] = controlAddressFields[i];
            addressFields2[i] = controlAddressFields[i];

            String fieldDescription = "Field: " + controlAddressFields[i];
            assertEquals(address1, address2,
                    fieldDescription + " - equals() does not treat null and blank string the same");
            assertEquals(address1.hashCode(), address2.hashCode(), fieldDescription
                    + " - hashCode() does not treat null and blank string the same");
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
            Address address1 = createTestAddress(addressFields1);
            Address address2 = createTestAddress(addressFields2);

            addressFields1[i] = addressFields2[i];
            if (i < addressFields1.length - 1) {
                addressFields2[i + 1] = addressFields1[i + 1];
            }
            String fieldDescription = "Field: " + fieldNames[i];
            assertEquals(address1, address2,
                    "Non-normal spaces in " + fieldDescription + " have an effect on equals()");
            assertEquals(address1.hashCode(), address2.hashCode(),
                    "Non-normal spaces in " + fieldDescription + " have an effect on hashCode()");
        }
    }

    @Test
    void testEqualsCasingIsIgnoredOnEquals() {
        String[] controlAddressFields = {"careOf", "poBox", "careOfCompany", "houseNameNum",
                "street", "area", "postTown", "region", "postCode", "country"};
        String[] addressFields1 = Arrays.copyOf(controlAddressFields, controlAddressFields.length);
        String[] addressFields2 = Arrays.copyOf(controlAddressFields, controlAddressFields.length);

        for (int i = 0; i < addressFields1.length; i++) {
            addressFields1[i] = addressFields1[i].toUpperCase();
            addressFields2[i] = addressFields1[i].toLowerCase();

            Address address1 = createTestAddress(addressFields1);
            Address address2 = createTestAddress(addressFields2);

            addressFields1[i] = controlAddressFields[i];
            addressFields2[i] = controlAddressFields[i];

            String fieldDescription = "Field: " + controlAddressFields[i];
            assertEquals(address1, address2,
                    fieldDescription + " - equals() does not ignore casing");
            assertEquals(address1.hashCode(), address2.hashCode(),
                    fieldDescription + " - hashCode() does not ignore casing");
        }
    }

    @Test
    void testSerialisation() throws JsonProcessingException {
        String expectedJson =
                "{\"careOf\":\"John Smith\"," +
                        "\"poBox\":\"PO Box 123\"," +
                        "\"careOfCompany\":\"ABC Company\"," +
                        "\"houseNameNum\":\"123 Main Street\"," +
                        "\"street\":\"Apt 4B\"," +
                        "\"area\":\"Downtown\"," +
                        "\"postTown\":\"New York\"," +
                        "\"region\":\"NY\"," +
                        "\"postCode\":\"10001\"," +
                        "\"country\":\"United States\"}";

        String[] controlAddressFields = {"John Smith", "PO Box 123", "ABC Company",
                "123 Main Street", "Apt 4B", "Downtown", "New York", "NY", "10001",
                "United States"};
        address = createTestAddress(controlAddressFields);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(address);

        assertEquals(expectedJson, json, "Serialisation failed");
    }

    @Test
    void testSerialisationWithMissingField() throws JsonProcessingException {
        String expectedJson =
                "{\"careOf\":\"John Smith\"," +
                        "\"careOfCompany\":\"ABC Company\"," +
                        "\"houseNameNum\":\"123 Main Street\"," +
                        "\"street\":\"Apt 4B\"," +
                        "\"area\":\"Downtown\"," +
                        "\"postTown\":\"New York\"," +
                        "\"region\":\"NY\"," +
                        "\"postCode\":\"10001\"," +
                        "\"country\":\"United States\"}";

        String[] controlAddressFields = {"John Smith", null, "ABC Company",
                "123 Main Street", "Apt 4B", "Downtown", "New York", "NY", "10001",
                "United States"};
        address = createTestAddress(controlAddressFields);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(address);

        assertEquals(expectedJson, json, "Serialisation failed");
    }

    @Test
    void testSerialisationBlankStringField() throws JsonProcessingException {
        String expectedJson =
                "{\"careOf\":\"\"," +
                        "\"poBox\":\"PO Box 123\"," +
                        "\"careOfCompany\":\"ABC Company\"," +
                        "\"houseNameNum\":\"123 Main Street\"," +
                        "\"street\":\"Apt 4B\"," +
                        "\"area\":\"Downtown\"," +
                        "\"postTown\":\"New York\"," +
                        "\"region\":\"NY\"," +
                        "\"postCode\":\"10001\"," +
                        "\"country\":\"United States\"}";

        String[] controlAddressFields = {"", "PO Box 123", "ABC Company",
                "123 Main Street", "Apt 4B", "Downtown", "New York", "NY", "10001",
                "United States"};
        address = createTestAddress(controlAddressFields);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(address);

        assertEquals(expectedJson, json, "Serialisation failed");
    }

    @Test
    void testSerialisationSpaceField() throws JsonProcessingException {
        String expectedJson =
                "{\"careOf\":\" \"," +
                        "\"poBox\":\"PO Box 123\"," +
                        "\"careOfCompany\":\"ABC Company\"," +
                        "\"houseNameNum\":\"123 Main Street\"," +
                        "\"street\":\"Apt 4B\"," +
                        "\"area\":\"Downtown\"," +
                        "\"postTown\":\"New York\"," +
                        "\"region\":\"NY\"," +
                        "\"postCode\":\"10001\"," +
                        "\"country\":\"United States\"}";

        String[] controlAddressFields = {" ", "PO Box 123", "ABC Company",
                "123 Main Street", "Apt 4B", "Downtown", "New York", "NY", "10001",
                "United States"};
        address = createTestAddress(controlAddressFields);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(address);

        assertEquals(expectedJson, json, "Serialisation failed");
    }

    private Address createTestAddress(String... fields) {
        Address address = new Address();
        address.setCareOf(fields[0]);
        address.setPoBox(fields[1]);
        address.setCareOfCompany(fields[2]);
        address.setHouseNameNum(fields[3]);
        address.setStreet(fields[4]);
        address.setArea(fields[5]);
        address.setPostTown(fields[6]);
        address.setRegion(fields[7]);
        address.setPostCode(fields[8]);
        address.setCountry(fields[9]);
        return address;
    }


}
