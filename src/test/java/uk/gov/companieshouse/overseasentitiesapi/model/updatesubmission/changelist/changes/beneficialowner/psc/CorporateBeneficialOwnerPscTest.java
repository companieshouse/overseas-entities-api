package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.beneficialowner.psc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.CompanyIdentification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CorporateBeneficialOwnerPscTest {
  private CorporateBeneficialOwnerPsc corporateBeneficialOwnerPsc1;
  private CorporateBeneficialOwnerPsc corporateBeneficialOwnerPsc2;

  @BeforeEach
  void setUp() {
    corporateBeneficialOwnerPsc1 = new CorporateBeneficialOwnerPsc();
    corporateBeneficialOwnerPsc2 = new CorporateBeneficialOwnerPsc();
  }

  @Test
  void testSetAndGetCorporateName() {
    String corporateName = "Example Corporation";
    corporateBeneficialOwnerPsc1.setCorporateName(corporateName);
    assertEquals(corporateName, corporateBeneficialOwnerPsc1.getCorporateName());

    corporateBeneficialOwnerPsc2.setCorporateName(corporateName);
    assertEquals(corporateName, corporateBeneficialOwnerPsc2.getCorporateName());
  }

  @Test
  void testSetAndGetCompanyIdentification() {
    CompanyIdentification companyIdentification = createCompanyIdentification();
    corporateBeneficialOwnerPsc1.setCompanyIdentification(companyIdentification);
    assertEquals(companyIdentification, corporateBeneficialOwnerPsc1.getCompanyIdentification());

    corporateBeneficialOwnerPsc2.setCompanyIdentification(companyIdentification);
    assertEquals(companyIdentification, corporateBeneficialOwnerPsc2.getCompanyIdentification());
  }

  @Test
  void testSetAndGetAppointmentType() {
    String expected = "OE OLE BO";
    assertEquals(expected, corporateBeneficialOwnerPsc1.getAppointmentType());
  }

  @Test
  void testEqualsAndHashCode() {
    var corporateBeneficialOwnerPsc1 = createCorporateBeneficialOwnerPsc();
    var corporateBeneficialOwnerPsc2 = createCorporateBeneficialOwnerPsc();

    assertEquals(corporateBeneficialOwnerPsc1, corporateBeneficialOwnerPsc2);
    assertEquals(corporateBeneficialOwnerPsc1.hashCode(), corporateBeneficialOwnerPsc2.hashCode());

    var corporateBeneficialOwnerPsc3 = createCorporateBeneficialOwnerPsc();
    corporateBeneficialOwnerPsc3.setCorporateName("Different Name");

    assertNotEquals(corporateBeneficialOwnerPsc1, corporateBeneficialOwnerPsc3);
    assertNotEquals(corporateBeneficialOwnerPsc1.hashCode(), corporateBeneficialOwnerPsc3.hashCode());
  }

  private CorporateBeneficialOwnerPsc createCorporateBeneficialOwnerPsc() {
    CorporateBeneficialOwnerPsc psc = new CorporateBeneficialOwnerPsc();
    psc.setCorporateName("Example Corporation");
    psc.setCompanyIdentification(createCompanyIdentification());
    return psc;
  }

  private CompanyIdentification createCompanyIdentification() {
    return new CompanyIdentification("legalForm", "governingLaw",
        "registerLocation", "placeRegistered", "registerJurisdiction","registrationNumber");
  }
}
