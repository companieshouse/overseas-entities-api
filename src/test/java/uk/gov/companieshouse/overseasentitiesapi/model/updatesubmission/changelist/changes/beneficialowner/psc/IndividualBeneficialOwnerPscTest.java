package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.beneficialowner.psc;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.PersonName;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class IndividualBeneficialOwnerPscTest {
  private IndividualBeneficialOwnerPsc individualBeneficialOwnerPsc1;
  private IndividualBeneficialOwnerPsc individualBeneficialOwnerPsc2;

  @BeforeEach
  void setUp() {
    individualBeneficialOwnerPsc1 = new IndividualBeneficialOwnerPsc();
    individualBeneficialOwnerPsc2 = new IndividualBeneficialOwnerPsc();
  }

  @Test
  void testSetAndGetPersonName() {
    PersonName personName = new PersonName("John", "Smith");
    individualBeneficialOwnerPsc1.setPersonName(personName);
    assertEquals(personName, individualBeneficialOwnerPsc1.getPersonName());

    individualBeneficialOwnerPsc2.setPersonName(personName);
    assertEquals(personName, individualBeneficialOwnerPsc2.getPersonName());
  }

  @Test
  void testSetAndGetNationalityOther() {
    String expected = "nationalityOther";
    individualBeneficialOwnerPsc1.setNationalityOther(expected);
    assertEquals(expected, individualBeneficialOwnerPsc1.getNationalityOther());

    individualBeneficialOwnerPsc2.setNationalityOther(expected);
    assertEquals(expected, individualBeneficialOwnerPsc2.getNationalityOther());
  }

  @Test
  void testSetAndGetBirthDate() {
    String expected = LocalDate.now().toString();
    individualBeneficialOwnerPsc1.setBirthDate(expected);
    assertEquals(expected, individualBeneficialOwnerPsc1.getBirthDate());

    individualBeneficialOwnerPsc2.setBirthDate(expected);
    assertEquals(expected, individualBeneficialOwnerPsc2.getBirthDate());
  }

  @Test
  void testSetAndGetAppointmentType() {
    String expected = "OE INDIVIDUAL BO";
    assertEquals(expected, individualBeneficialOwnerPsc1.getAppointmentType());
  }

  @Test
  void testSetAndGetActionDate() {
    String expected = LocalDate.now().toString();
    individualBeneficialOwnerPsc1.setActionDate(expected);
    assertEquals(expected, individualBeneficialOwnerPsc1.getActionDate());

    individualBeneficialOwnerPsc2.setActionDate(expected);
    assertEquals(expected, individualBeneficialOwnerPsc2.getActionDate());
  }

  @Test
  void testSetAndGetServiceAddress() {
    Address address = new Address();
    individualBeneficialOwnerPsc1.setServiceAddress(address);
    assertEquals(address, individualBeneficialOwnerPsc1.getServiceAddress());

    individualBeneficialOwnerPsc2.setServiceAddress(address);
    assertEquals(address, individualBeneficialOwnerPsc2.getServiceAddress());
  }

  @Test
  void testSetAndGetResidentialAddress() {
    Address address = new Address();
    individualBeneficialOwnerPsc1.setResidentialAddress(address);
    assertEquals(address, individualBeneficialOwnerPsc1.getResidentialAddress());

    individualBeneficialOwnerPsc2.setResidentialAddress(address);
    assertEquals(address, individualBeneficialOwnerPsc2.getResidentialAddress());
  }

  @Test
  void testEqualsAndHashCode() {
    var individualBeneficialOwnerPsc1 = createIndividualBeneficialOwnerPsc();
    var individualBeneficialOwnerPsc2 = createIndividualBeneficialOwnerPsc();

    assertEquals(individualBeneficialOwnerPsc1, individualBeneficialOwnerPsc2);
    assertEquals(individualBeneficialOwnerPsc1.hashCode(), individualBeneficialOwnerPsc2.hashCode());

    var individualBeneficialOwnerPsc3 = createIndividualBeneficialOwnerPsc();
    individualBeneficialOwnerPsc3.setBirthDate(LocalDate.of(2000, 1, 2).toString());

    assertNotEquals(individualBeneficialOwnerPsc1, individualBeneficialOwnerPsc3);
    assertNotEquals(individualBeneficialOwnerPsc1.hashCode(), individualBeneficialOwnerPsc3.hashCode());
  }

  private IndividualBeneficialOwnerPsc createIndividualBeneficialOwnerPsc() {
    IndividualBeneficialOwnerPsc psc = new IndividualBeneficialOwnerPsc();
    psc.setPersonName(new PersonName("John", "Smith"));
    psc.setNationalityOther("nationality,nationalityOther");
    psc.setBirthDate(LocalDate.of(2000, 1, 1).toString());
    psc.setActionDate(LocalDate.now().toString());
    psc.setServiceAddress(new Address());
    psc.setResidentialAddress(new Address());
    psc.setNatureOfControls(List.of("over_25_percent_of_voting_rights"));
    return psc;
  }
}
