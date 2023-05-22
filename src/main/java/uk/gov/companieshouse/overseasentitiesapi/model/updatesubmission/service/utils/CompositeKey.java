package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.service.utils;

import java.util.Objects;

public class CompositeKey<S,T> {

  private final Class<S> class1;
  private final Class<T> class2;

  public CompositeKey(Class<S> class1, Class<T> class2) {
    this.class1 = class1;
    this.class2 = class2;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompositeKey that = (CompositeKey) o;
    return Objects.equals(class1, that.class1) && Objects.equals(class2,
        that.class2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(class1, class2);
  }
}
