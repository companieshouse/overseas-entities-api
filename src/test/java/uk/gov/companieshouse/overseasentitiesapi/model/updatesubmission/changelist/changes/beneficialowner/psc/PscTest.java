package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.beneficialowner.psc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PscTest {
  private OtherBeneficialOwnerPsc psc1;
  private OtherBeneficialOwnerPsc psc2;

  @BeforeEach
  void setUp() {
    psc1 = new OtherBeneficialOwnerPsc();
    psc2 = new OtherBeneficialOwnerPsc();
  }

  @Test
  void testSetAndGetServiceAddress() {
    Address serviceAddress = createAddress();
    psc1.setServiceAddress(serviceAddress);
    assertEquals(serviceAddress, psc1.getServiceAddress());

    psc2.setServiceAddress(serviceAddress);
    assertEquals(serviceAddress, psc2.getServiceAddress());
  }

  @Test
  void testSetAndGetResidentialAddress() {
    Address residentialAddress = createAddress();
    psc1.setResidentialAddress(residentialAddress);
    assertEquals(residentialAddress, psc1.getResidentialAddress());

    psc2.setResidentialAddress(residentialAddress);
    assertEquals(residentialAddress, psc2.getResidentialAddress());
  }

  @Test
  void testSetAndGetNatureOfControls() {
    var natureOfControl = List.of("over_25_percent_of_voting_rights");
    psc1.setNatureOfControls(natureOfControl);
    assertEquals(natureOfControl, psc1.getNatureOfControls());

    psc2.setNatureOfControls(natureOfControl);
    assertEquals(natureOfControl, psc2.getNatureOfControls());
  }

  @Test
  void testEqualsAndHashCode() {
    var psc1 = createOtherBeneficialOwnerPsc();
    var psc2 = createOtherBeneficialOwnerPsc();

    assertEquals(psc1, psc2);
    assertEquals(psc1.hashCode(), psc2.hashCode());

    var psc3 = createOtherBeneficialOwnerPsc();
    psc3.setCorporateSoleName("Other Corporate Sole Name");

    assertNotEquals(psc1, psc3);
    assertNotEquals(psc1.hashCode(), psc3.hashCode());
  }

  private OtherBeneficialOwnerPsc createOtherBeneficialOwnerPsc() {
    OtherBeneficialOwnerPsc psc = new OtherBeneficialOwnerPsc();
    psc.setServiceAddress(createAddress());
    psc.setResidentialAddress(createAddress());
    psc.setNatureOfControls(List.of("Type 1", "Type 2"));
    return psc;
  }

  private Address createAddress() {
    Address address = new Address();
    address.setCareOf("Care Of");
    address.setPoBox("PO Box");
    address.setCareOfCompany("Care Of Company");
    address.setHouseNameNum("House Name/Num");
    address.setStreet("Street");
    address.setArea("Area");
    address.setPostTown("Post Town");
    address.setRegion("Region");
    address.setPostCode("Post Code");
    address.setCountry("Country");
    return address;
  }
}
