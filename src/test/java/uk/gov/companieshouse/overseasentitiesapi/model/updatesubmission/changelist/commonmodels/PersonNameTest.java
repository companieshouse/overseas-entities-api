package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class PersonNameTest {

  @Test
  void testGetForename() {
    String forename = "John";
    String surname = "Doe";
    PersonName personName = new PersonName(forename, surname);
    assertEquals(forename, personName.getForename());
  }

  @Test
  void testSetForename() {
    String forename = "John";
    String surname = "Doe";
    PersonName personName = new PersonName("", surname);
    personName.setForename(forename);
    assertEquals(forename, personName.getForename());
  }

  @Test
  void testGetSurname() {
    String forename = "John";
    String surname = "Doe";
    PersonName personName = new PersonName(forename, surname);
    assertEquals(surname, personName.getSurname());
  }

  @Test
  void testSetSurname() {
    String forename = "John";
    String surname = "Doe";
    PersonName personName = new PersonName(forename, "");
    personName.setSurname(surname);
    assertEquals(surname, personName.getSurname());
  }

  @Test
  void testEquals() {
    PersonName personName1 = new PersonName("John", "Doe");
    PersonName personName2 = new PersonName("John", "Doe");
    assertEquals(personName1, personName2);

    PersonName personName3 = new PersonName("Jane", "Smith");
    assertNotEquals(personName1, personName3);

    assertNotEquals("Not a PersonName", personName1);
    assertEquals(personName1, personName1);
    assertNotEquals(null, personName1);
  }

  @Test
  void testHashCode() {
    PersonName personName1 = new PersonName("John", "Doe");
    PersonName personName2 = new PersonName("John", "Doe");
    assertEquals(personName1.hashCode(), personName2.hashCode());

    PersonName personName3 = new PersonName("Jane", "Smith");
    assertNotEquals(personName1.hashCode(), personName3.hashCode());
  }

  @Test
  void testEqualsWithDifferentClass() {
    PersonName personName = new PersonName("John", "Doe");
    assertNotEquals(personName, new Object());
  }

  @Test
  void testEqualsWithDifferentForename() {
    PersonName personName1 = new PersonName("John", "Doe");
    PersonName personName2 = new PersonName("Jane", "Doe");
    assertNotEquals(personName1, personName2);
  }

  @Test
  void testEqualsWithDifferentSurname() {
    PersonName personName1 = new PersonName("John", "Doe");
    PersonName personName2 = new PersonName("John", "Smith");
    assertNotEquals(personName1, personName2);
  }

  @Test
  void testEqualsWithNull() {
    PersonName personName = new PersonName("John", "Doe");
    assertNotEquals(null, personName);
  }
}
