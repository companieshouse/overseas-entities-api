package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class NatureOfControlTest {

  @Test
  void testSetAndGetNatureOfControlTypes() {
    List<String> expected = List.of("type1", "type2", "type3");
    NatureOfControl natureOfControl = new NatureOfControl();
    natureOfControl.setNatureOfControlTypes(expected);
    assertEquals(expected, natureOfControl.getNatureOfControlTypes());
  }

  @Test
  void testSetAndGetNatureOfControlTypesWithNull() {
    NatureOfControl natureOfControl = new NatureOfControl();
    natureOfControl.setNatureOfControlTypes(null);
    assertEquals(null, natureOfControl.getNatureOfControlTypes());
  }

  @Test
  void testSetAndGetNatureOfControlTypesWithEmptyList() {
    NatureOfControl natureOfControl = new NatureOfControl();
    natureOfControl.setNatureOfControlTypes(List.of());
    assertEquals(List.of(), natureOfControl.getNatureOfControlTypes());
  }
  @Test
  void testEqualsAndHashCode() {
    NatureOfControl noc1 = new NatureOfControl();
    noc1.setNatureOfControlTypes(List.of("type1", "type2"));

    NatureOfControl noc2 = new NatureOfControl();
    noc2.setNatureOfControlTypes(List.of("type1", "type2"));

    assertTrue(noc1.equals(noc2) && noc2.equals(noc1));
    assertEquals(noc1.hashCode(), noc2.hashCode());

    noc2.setNatureOfControlTypes(List.of("type1", "type3"));

    assertFalse(noc1.equals(noc2) || noc2.equals(noc1));
    assertNotEquals(noc1.hashCode(), noc2.hashCode());
  }
}