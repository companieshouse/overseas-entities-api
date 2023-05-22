package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.service.utils.equalshelper;

public interface EqualsHelper<T, S> {
  public boolean objectsEqual(T a, S b);
}
