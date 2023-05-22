package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.service.utils.converters;

public interface Converter<T, V> {
  public T convert(V value);
}
