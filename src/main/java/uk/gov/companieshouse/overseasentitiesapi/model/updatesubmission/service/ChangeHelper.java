package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.service;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.benificialowner.Psc;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.service.utils.equalshelper.EqualsFactory;

class ChangeHelper<T, U, V> {

  public final T proposedData;
  public final U currentData;
  public final BiConsumer<Psc, V> setter;

  public final BiPredicate<T, U> objectsEqual;
  public final Class<V> setterParameterClass;

  public ChangeHelper(T proposedData, U currentData, BiConsumer<Psc, V> setter,
      Class<V> setterParameterClass) {
    this.proposedData = proposedData;
    this.currentData = currentData;
    this.setterParameterClass = setterParameterClass;

    this.setter = setter;
    if (currentData.getClass() == proposedData.getClass()) {
      this.objectsEqual = (a, b) -> a.equals(b);
    } else {
      this.objectsEqual = EqualsFactory.getEquals(currentData.getClass(),
          proposedData.getClass())::objectsEqual;
    }
  }
}
