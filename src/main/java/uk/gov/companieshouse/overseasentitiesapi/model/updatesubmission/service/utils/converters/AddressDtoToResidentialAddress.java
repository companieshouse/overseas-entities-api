package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.service.utils.converters;

import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.benificialowner.ResidentialAddress;

public class AddressDtoToResidentialAddress implements Converter<AddressDto, ResidentialAddress>{

  @Override
  public ResidentialAddress convert(AddressDto value) {
    ResidentialAddress residentialAddress = new ResidentialAddress();
    residentialAddress.setAddress(new AddressDtoToAddressConverter().convert(value));
    return residentialAddress;
  }
}
