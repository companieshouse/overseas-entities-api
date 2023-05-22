package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.service.utils.converters;

import java.time.LocalDate;
import uk.gov.companieshouse.api.model.common.DateOfBirth;

public class LocalDateToDateOfBirthConverter implements Converter<DateOfBirth, LocalDate>{
  
  @Override
  public DateOfBirth convert(LocalDate value) {
    return null;
  }
}
