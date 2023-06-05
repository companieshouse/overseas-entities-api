package uk.gov.companieshouse.overseasentitiesapi.utils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
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
        (a) -> new String[]{"aaa", "ccc"},
        Dummy::setArrayField);

    assertFalse(hasChanged);
    assertNull(dummy.getArrayField());
  }

  @Test
  void testCompareAndBuildPublicChangeFullArray() {
    boolean hasChanged = changeManager.compareAndBuildLeftChange(new String[]{"aaa", "bbb"},
        (a) -> new String[]{"aaa", "ccc"},
        Dummy::setArrayField);

    assertTrue(hasChanged);
    assertArrayEquals(new String[]{"aaa", "bbb"}, dummy.getArrayField());
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
  void testCompareAndBuildPrivateChangeNullCurrent() {
    dummy = new Dummy();
    changeManager = new ChangeManager<>(dummy, Pair.of("public", null));

    boolean hasChanged = changeManager.compareAndBuildRightChange("newValue",
        String::new,
        Dummy::setField);

    assertTrue(hasChanged);
    assertEquals("newValue", dummy.getField());
  }

  @Test
  void testCompareAndBuildChangeProposedDataNullConverterNotNull() {
    Function<String, String> proposedConverter = Function.identity();

    boolean hasChanged = changeManager.compareAndBuildLeftChange(null,
        String::new,
        proposedConverter,
        Objects::equals,
        Dummy::setField);

    assertFalse(hasChanged);
    assertNull(dummy.getField());
  }

  @Test
  void testCompareAndBuildChangeProposedDataNotNullConverterNull() {
    Exception exception = assertThrows(IllegalArgumentException.class, () ->
        changeManager.compareAndBuildLeftChange("newValue",
            String::new,
            null,
            Objects::equals,
            Dummy::setField)
    );

    String expectedMessage = "Invalid input parameter: proposedConverter is null.";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void testCompareAndBuildChangeCurrentDataGetterNull() {

    var function = Function.identity();
    Exception exception = assertThrows(IllegalArgumentException.class, () ->
        changeManager.compareAndBuildLeftChange("newValue",
            null,
            a -> a,
            Objects::equals,
            Dummy::setField)
    );

    String expectedMessage = "Invalid input parameter: currentDataGetter is null.";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void testCompareAndBuildChangeLeftDataNull() {
    dummy = new Dummy();
    changeManager = new ChangeManager<>(dummy, Pair.of(null, "private"));

    boolean hasChanged = changeManager.compareAndBuildLeftChange("newValue",
        String::new,
        Function.identity(),
        Objects::equals,
        Dummy::setField);

    assertTrue(hasChanged);
    assertEquals("newValue", dummy.getField());
  }

  @Test
  void testCompareAndBuildChangeEqualityPredicateNonNullAndDataNotEqual() {
    changeManager = new ChangeManager<>(dummy, Pair.of("public", "private"));

    BiPredicate<String, String> equalityPredicate = String::equals;

    boolean hasChanged = changeManager.compareAndBuildLeftChange("newValue",
        String::new,
        Function.identity(),
        equalityPredicate,
        Dummy::setField);

    assertTrue(hasChanged);
    assertEquals("newValue", dummy.getField());
  }

  @Test
  void testCompareAndBuildChangeNullObjectToAddChanges() {
    var pair = Pair.of("public", "private");
    Exception exception = assertThrows(IllegalArgumentException.class, () ->
        new ChangeManager<>(null, pair)
    );

    String expectedMessage = "Invalid input parameter: objectToAddChanges is null";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void testCompareAndBuildPublicChangeLeftDataNullAndSetterNull() {
    var changeManager = new ChangeManager<>(dummy, Pair.of("public", "private"));
    Function<String, String> functionIdentity = Function.identity();
    Exception exception = assertThrows(IllegalArgumentException.class, () ->
        changeManager.compareAndBuildLeftChange("newValue",
            String::new,
            functionIdentity,
            Objects::equals,
            null)
    );

    String expectedMessage = "Invalid input parameter: dataSetter is null.";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void testCompareAndBuildChangeEqualityPredicateNonNullAndDataEqual() {
    var changeManager = new ChangeManager<>(dummy, Pair.of("public", "private"));

    BiPredicate<String, String> alwaysTrueEqualityPredicate = (s1, s2) -> true;

    boolean hasChanged = changeManager.compareAndBuildLeftChange("public",
        String::new,
        Function.identity(),
        alwaysTrueEqualityPredicate,
        Dummy::setField);

    assertFalse(hasChanged);
    assertNull(dummy.getField());
  }

  @Test
  void testCompareAndBuildChangeLeftDataNullEqualityPredicateNonNullAndDataNotEqual() {
    dummy = new Dummy();
    changeManager = new ChangeManager<>(dummy, Pair.of(null, "private"));

    BiPredicate<String, String> equalityPredicate = (s1, s2) -> false;

    boolean hasChanged = changeManager.compareAndBuildLeftChange("newValue",
        String::new,
        Function.identity(),
        equalityPredicate,
        Dummy::setField);

    assertTrue(hasChanged);
    assertEquals("newValue", dummy.getField());
  }

  @Test
  void testCompareAndBuildChangeCurrentDataEqualityPredicateNull() {

    Exception exception = assertThrows(IllegalArgumentException.class, () ->
        changeManager.compareAndBuildLeftChange("newValue",
            String::new,
            a -> a,
            null,
            Dummy::setField)
    );

    String expectedMessage = "nvalid input parameter: equalityPredicate is null.";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
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
