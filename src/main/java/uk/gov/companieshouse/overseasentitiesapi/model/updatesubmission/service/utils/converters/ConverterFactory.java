package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.service.utils.converters;

import java.time.LocalDate;
import java.util.Map;
import uk.gov.companieshouse.api.model.common.Address;
import uk.gov.companieshouse.api.model.common.DateOfBirth;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.PersonName;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.service.utils.CompositeKey;

public class ConverterFactory<S, T> {

  static Map<CompositeKey<?, ?>, Converter<?, ?>> converters = Map.of(
      new CompositeKey<>(AddressDto.class, Address.class), new AddressDtoToAddressConverter(),
      new CompositeKey<>(LocalDate.class, DateOfBirth.class), new LocalDateToDateOfBirthConverter(),
      new CompositeKey<>(String.class, PersonName.class), new StringToPersonalNameConverter(),
      new CompositeKey<>(AddressDto.class, String.class), new AddressDtoToStringConverter()
  );

  public static <S, T> Converter<S, T> getConverter(Class<S> class1, Class<T> class2) {
    Converter<?, ?> converter = converters.get(new CompositeKey<>(class1, class2));
    if (converter != null) {
      return (Converter<S, T>) converter;
    }
    throw new IllegalArgumentException("No converter found for " + class1.getName() + " and " + class2.getName());
  }
}

