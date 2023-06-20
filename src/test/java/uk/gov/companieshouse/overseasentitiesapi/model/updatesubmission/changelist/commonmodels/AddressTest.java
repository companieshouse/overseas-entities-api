package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

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

    Assertions.assertEquals(careOf, address.getCareOf());
    Assertions.assertEquals(poBox, address.getPoBox());
    Assertions.assertEquals(careOfCompany, address.getCareOfCompany());
    Assertions.assertEquals(houseNameNum, address.getHouseNameNum());
    Assertions.assertEquals(street, address.getStreet());
    Assertions.assertEquals(area, address.getArea());
    Assertions.assertEquals(postTown, address.getPostTown());
    Assertions.assertEquals(region, address.getRegion());
    Assertions.assertEquals(postCode, address.getPostCode());
    Assertions.assertEquals(country, address.getCountry());
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

    Assertions.assertEquals(address1, address2);
    Assertions.assertEquals(address1.hashCode(), address2.hashCode());
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

    Assertions.assertNull(address.getCareOf());
    Assertions.assertNull(address.getPoBox());
    Assertions.assertNull(address.getCareOfCompany());
    Assertions.assertNull(address.getHouseNameNum());
    Assertions.assertNull(address.getStreet());
    Assertions.assertNull(address.getArea());
    Assertions.assertNull(address.getPostTown());
    Assertions.assertNull(address.getRegion());
    Assertions.assertNull(address.getPostCode());
    Assertions.assertNull(address.getCountry());
  }

  @Test
  void testEqualsAndHashCodeFailure() {
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
    address2.setCareOf("Different Care Of");
    address2.setPoBox("PO Box");
    address2.setCareOfCompany("Care Of Company");
    address2.setHouseNameNum("House Name Num");
    address2.setStreet("Street");
    address2.setArea("Area");
    address2.setPostTown("Post Town");
    address2.setRegion("Region");
    address2.setPostCode("Post Code");
    address2.setCountry("Country");

    Assertions.assertNotEquals(address1, address2);
    Assertions.assertNotEquals(address1.hashCode(), address2.hashCode());
  }
}
