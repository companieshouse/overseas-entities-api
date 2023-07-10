package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.beneficialowner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.beneficialowner.psc.CorporateBeneficialOwnerPsc;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.beneficialowner.psc.IndividualBeneficialOwnerPsc;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.beneficialowner.psc.OtherBeneficialOwnerPsc;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.PersonName;

class BeneficialOwnerChangeTest {

  private CorporateBeneficialOwnerChange corporateChange1;
  private CorporateBeneficialOwnerChange corporateChange2;
  private IndividualBeneficialOwnerChange individualChange1;
  private IndividualBeneficialOwnerChange individualChange2;
  private OtherBeneficialOwnerChange otherChange1;
  private OtherBeneficialOwnerChange otherChange2;

  @BeforeEach
  void setUp() {
    corporateChange1 = createCorporateBeneficialOwnerChange();
    corporateChange2 = createCorporateBeneficialOwnerChange();
    individualChange1 = createIndividualBeneficialOwnerChange();
    individualChange2 = createIndividualBeneficialOwnerChange();
    otherChange1 = createOtherBeneficialOwnerChange();
    otherChange2 = createOtherBeneficialOwnerChange();
  }

  @Test
  void testSetAndGetChange() {
    String change = "pscAppointmentChange";
    corporateChange1.setChange(change);
    assertEquals(change, corporateChange1.getChange());
    assertEquals(change, individualChange1.getChange());
    assertEquals(change, otherChange1.getChange());
  }

  @Test
  void testSetAndGetAppointmentId() {
    String appointmentId = "123456";
    corporateChange1.setAppointmentId(appointmentId);
    individualChange1.setAppointmentId(appointmentId);
    otherChange1.setAppointmentId(appointmentId);

    assertEquals(appointmentId, corporateChange1.getAppointmentId());
    assertEquals(appointmentId, individualChange1.getAppointmentId());
    assertEquals(appointmentId, otherChange1.getAppointmentId());
  }

  @Test
  void testSetAndGetPsc() {
    CorporateBeneficialOwnerPsc corporatePsc = new CorporateBeneficialOwnerPsc();
    IndividualBeneficialOwnerPsc individualPsc = new IndividualBeneficialOwnerPsc();
    OtherBeneficialOwnerPsc otherPsc = new OtherBeneficialOwnerPsc();

    corporateChange1.setPsc(corporatePsc);
    individualChange1.setPsc(individualPsc);
    otherChange1.setPsc(otherPsc);

    assertEquals(corporatePsc, corporateChange1.getPsc());
    assertEquals(individualPsc, individualChange1.getPsc());
    assertEquals(otherPsc, otherChange1.getPsc());
  }

  @Test
  void testEqualsAndHashCode() {

    assertEquals(corporateChange1, corporateChange2);
    assertEquals(corporateChange1.hashCode(), corporateChange2.hashCode());

    assertEquals(individualChange1, individualChange2);
    assertEquals(individualChange1.hashCode(), individualChange2.hashCode());

    assertEquals(otherChange1, otherChange2);
    assertEquals(otherChange1.hashCode(), otherChange2.hashCode());

    assertNotEquals(corporateChange1, individualChange1);
    assertNotEquals(corporateChange1.hashCode(), individualChange1.hashCode());

    assertNotEquals(individualChange1, otherChange1);
    assertNotEquals(individualChange1.hashCode(), otherChange1.hashCode());

    // Additional assertNotEquals comparisons
    corporateChange2.setAppointmentId("654321");
    assertNotEquals(corporateChange1, corporateChange2);
    assertNotEquals(corporateChange1.hashCode(), corporateChange2.hashCode());

    individualChange2.getPsc().setPersonName(new PersonName("Alice", "Smith"));
    assertNotEquals(individualChange1, individualChange2);
    assertNotEquals(individualChange1.hashCode(), individualChange2.hashCode());

    otherChange2.getPsc().setCorporateName("New Corporate Sole Name");
    assertNotEquals(otherChange1, otherChange2);
    assertNotEquals(otherChange1.hashCode(), otherChange2.hashCode());
  }

  private CorporateBeneficialOwnerChange createCorporateBeneficialOwnerChange() {
    CorporateBeneficialOwnerChange change = new CorporateBeneficialOwnerChange();
    change.setChange("pscAppointmentChange");
    change.setAppointmentId("123456");
    change.setPsc(new CorporateBeneficialOwnerPsc());
    return change;
  }

  private IndividualBeneficialOwnerChange createIndividualBeneficialOwnerChange() {
    IndividualBeneficialOwnerChange change = new IndividualBeneficialOwnerChange();
    change.setChange("pscAppointmentChange");
    change.setAppointmentId("123456");
    change.setPsc(new IndividualBeneficialOwnerPsc());
    return change;
  }

  private OtherBeneficialOwnerChange createOtherBeneficialOwnerChange() {
    OtherBeneficialOwnerChange change = new OtherBeneficialOwnerChange();
    change.setChange("pscAppointmentChange");
    change.setAppointmentId("123456");
    change.setPsc(new OtherBeneficialOwnerPsc());
    return change;
  }
}
