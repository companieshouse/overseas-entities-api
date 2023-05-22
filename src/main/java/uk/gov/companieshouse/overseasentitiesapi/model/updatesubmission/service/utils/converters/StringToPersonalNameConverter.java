package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.service.utils.converters;

import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.PersonName;

public class StringToPersonalNameConverter implements Converter<String, PersonName>{

  @Override
  public PersonName convert(String value) {
    var nameArray = value.split(" ");
    var firstName = nameArray[0];
    var lastName = nameArray[nameArray.length - 1];

    return new PersonName(firstName, lastName);
  }
}
