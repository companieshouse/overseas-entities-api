package uk.gov.companieshouse.overseasentitiesapi.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.PersonName;

class PersonNameTest {

    @Test
    void testGetAndSetForename() {
        PersonName personName = new PersonName("John", "Doe");
        assertEquals("John", personName.getForename());
        personName.setForename("Jane");
        assertEquals("Jane", personName.getForename());
    }

    @Test
    void testGetAndSetSurname() {
        PersonName personName = new PersonName("John", "Doe");
        assertEquals("Doe", personName.getSurname());
        personName.setSurname("Smith");
        assertEquals("Smith", personName.getSurname());
    }

    @Test
    void testEquals() {
        PersonName personName1 = new PersonName("John", "Doe");
        PersonName personName2 = new PersonName("John", "Doe");
        assertEquals(personName1, personName2);

        PersonName personName3 = new PersonName("Jane", "Smith");
        assertNotEquals(personName1, personName3);
        assertNotEquals("Not a PersonName", personName1)    ;
        assertEquals(personName1, personName1);
        assertNotEquals( null, personName1);
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
    void testEqualsWithNull() {
        PersonName personName = new PersonName("John", "Doe");
        assertNotEquals(null, personName);
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
}
