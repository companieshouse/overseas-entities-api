package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.service.utils.equalshelper;

import java.time.LocalDate;
import java.util.Map;
import uk.gov.companieshouse.api.model.common.DateOfBirth;
import uk.gov.companieshouse.api.model.utils.AddressApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.service.utils.CompositeKey;

public class EqualsFactory {

  static Map<CompositeKey<?,?>, EqualsHelper<?,?>> converters = Map.of(
      new CompositeKey(LocalDate.class, DateOfBirth.class), new LocalDateAndDateOfBirthEquals(),
      new CompositeKey(AddressDto.class, AddressApi.class), new AddressDtoAndAddressApiEquals()
  );

  public static EqualsHelper getEquals(Class<?> class1, Class<?> class2) {
    return converters.get(new CompositeKey(class1, class2));
  }

}
