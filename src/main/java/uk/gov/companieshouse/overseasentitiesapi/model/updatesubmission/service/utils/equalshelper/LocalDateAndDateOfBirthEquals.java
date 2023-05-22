package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.service.utils.equalshelper;

import java.time.LocalDate;
import uk.gov.companieshouse.api.model.common.DateOfBirth;

public class LocalDateAndDateOfBirthEquals implements EqualsHelper<LocalDate, DateOfBirth>{

  @Override
  public boolean objectsEqual(LocalDate a, DateOfBirth b) {
    if (a == null && b == null) {
      return true;
    }
    if (a == null || b == null) {
      return false;
    }
    // The DateOfBirth model does not contain a day field, so we can't compare it
    return a.getMonthValue() == b.getMonth() && a.getYear() == b.getYear();
  }
}
