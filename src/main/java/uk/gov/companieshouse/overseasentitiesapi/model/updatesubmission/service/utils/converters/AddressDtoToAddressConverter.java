package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.service.utils.converters;

import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;

public class AddressDtoToAddressConverter implements
    Converter<AddressDto, Address> {

  @Override
  public Address convert(AddressDto value) {
    Address address = new Address();

    address.setCareOf(value.getCareOf());
    address.setPoBox(value.getPoBox());
    address.setHouseNameNum(value.getPropertyNameNumber());
    address.setStreet(value.getLine1());

    address.setArea(value.getLine2());
    address.setPostTown(value.getTown());

    address.setRegion(value.getCounty());
    address.setPostCode(value.getPostcode());
    address.setCountry(value.getCountry());

    return address;
  }

}
