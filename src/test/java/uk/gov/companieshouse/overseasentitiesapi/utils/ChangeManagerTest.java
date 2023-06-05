package uk.gov.companieshouse.overseasentitiesapi.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChangeManagerTest {

  private ChangeManager<Dummy, String, String> changeManager;
  private Dummy dummy;

  @BeforeEach
  public void setup() {
    dummy = new Dummy();
    changeManager = new ChangeManager<>(dummy, Pair.of("public", "private"));
  }

  @Test
  void testCompareAndBuildPublicChange() {
    boolean hasChanged = changeManager.compareAndBuildLeftChange("newValue",
        String::new,
        Dummy::setField);

    assertTrue(hasChanged);
    assertEquals("newValue", dummy.getField());
  }

  @Test
  void testCompareAndBuildPublicChangeNullProposed() {
    boolean hasChanged = changeManager.compareAndBuildLeftChange(null,
        String::new,
        Dummy::setField);

    assertFalse(hasChanged);
    assertNull(dummy.getField());
  }

  @Test
  void testCompareAndBuildPublicChangeEmptyArray() {
    boolean hasChanged = changeManager.compareAndBuildLeftChange(new String[]{},
        null,
        Dummy::setArrayField);

    assertFalse(hasChanged);
    assertNull(dummy.getArrayField());
  }

  @Test
  void testCompareAndBuildPrivateChangeNullProposed() {
    boolean hasChanged = changeManager.compareAndBuildRightChange(null,
        String::new,
        Dummy::setField);

    assertFalse(hasChanged);
    assertNull(dummy.getField());
  }

  @Test
  void testCompareAndBuildPublicChangeNullCurrent() {
    dummy = new Dummy();
    changeManager = new ChangeManager<>(dummy, Pair.of(null, "private"));

    boolean hasChanged = changeManager.compareAndBuildLeftChange("newValue",
        String::new,
        Dummy::setField);

    assertTrue(hasChanged);
    assertEquals("newValue", dummy.getField());
  }

  @Test
  void testCompareAndBuildPublicChangeLeftDataNullAndObjectToAddChangesNull() {
    dummy = new Dummy();
    changeManager = new ChangeManager<>(null, Pair.of(null, "private"));

    boolean hasChanged = changeManager.compareAndBuildLeftChange("newValue",
        String::new,
        Dummy::setField);

    assertFalse(hasChanged);
  }

  @Test
  void testCompareAndBuildPrivateChangeNullCurrent() {
    dummy = new Dummy();
    changeManager = new ChangeManager<>(dummy, Pair.of("public", null));

    boolean hasChanged = changeManager.compareAndBuildRightChange("newValue",
        String::new,
        Dummy::setField);

    assertTrue(hasChanged);
    assertEquals("newValue", dummy.getField());
  }

  private static class Dummy {

    private String field;

    private String[] arrayField;

    public String getField() {
      return field;
    }

    public void setField(String field) {
      this.field = field;
    }

    public String[] getArrayField() {
      return arrayField;
    }

    public void setArrayField(String[] arrayField) {
      this.arrayField = arrayField;
    }
  }
}
