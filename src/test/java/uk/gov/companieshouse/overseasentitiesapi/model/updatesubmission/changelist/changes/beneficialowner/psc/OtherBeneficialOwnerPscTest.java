package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.beneficialowner.psc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.CompanyIdentification;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.NatureOfControl;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class OtherBeneficialOwnerPscTest {
  private OtherBeneficialOwnerPsc otherBeneficialOwnerPsc;

  @BeforeEach
  void setUp() {
    otherBeneficialOwnerPsc = new OtherBeneficialOwnerPsc();
  }

  @Test
  void testSetAndGetCorporateSoleName() {
    String expected = "corporateSoleName";
    otherBeneficialOwnerPsc.setCorporateSoleName(expected);
    assertEquals(expected, otherBeneficialOwnerPsc.getCorporateSoleName());
  }

  @Test
  void testSetAndGetCompanyIdentification() {
    CompanyIdentification companyIdentification = mock(CompanyIdentification.class);
    otherBeneficialOwnerPsc.setCompanyIdentification(companyIdentification);
    assertEquals(companyIdentification, otherBeneficialOwnerPsc.getCompanyIdentification());
  }


  @Test
  void testSetAndGetActionDate() {
    String expected = LocalDate.now().toString();
    otherBeneficialOwnerPsc.setActionDate(expected);
    assertEquals(expected, otherBeneficialOwnerPsc.getActionDate());
  }

  @Test
  void testSetAndGetServiceAddress() {
    Address address = mock(Address.class);
    otherBeneficialOwnerPsc.setServiceAddress(address);
    assertEquals(address, otherBeneficialOwnerPsc.getServiceAddress());
  }

  @Test
  void testSetAndGetResidentialAddress() {
    Address address = mock(Address.class);
    otherBeneficialOwnerPsc.setResidentialAddress(address);
    assertEquals(address, otherBeneficialOwnerPsc.getResidentialAddress());
  }

  @Test
  void testSetAndGetNatureOfControls() {
    NatureOfControl natureOfControl = mock(NatureOfControl.class);
    otherBeneficialOwnerPsc.setNatureOfControls(natureOfControl);
    assertEquals(natureOfControl, otherBeneficialOwnerPsc.getNatureOfControls());
  }

  @Test
  void testEqualsAndHashCode() {
    OtherBeneficialOwnerPsc psc1 = new OtherBeneficialOwnerPsc();
    psc1.setCorporateSoleName("corporateSoleName");
    CompanyIdentification companyIdentification = new CompanyIdentification();
    psc1.setCompanyIdentification(companyIdentification);

    OtherBeneficialOwnerPsc psc2 = new OtherBeneficialOwnerPsc();
    psc2.setCorporateSoleName("corporateSoleName");
    psc2.setCompanyIdentification(companyIdentification);

    assertTrue(psc1.equals(psc2) && psc2.equals(psc1));
    assertEquals(psc1.hashCode(), psc2.hashCode());

    psc2.setActionDate(LocalDate.now().plusDays(1).toString());

    assertFalse(psc1.equals(psc2) || psc2.equals(psc1));
    assertNotEquals(psc1.hashCode(), psc2.hashCode());
  }
}
