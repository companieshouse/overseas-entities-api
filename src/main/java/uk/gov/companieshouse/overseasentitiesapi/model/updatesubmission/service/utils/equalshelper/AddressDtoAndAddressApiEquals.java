package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.service.utils.equalshelper;

import java.util.Objects;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.api.model.utils.AddressApi;

public class AddressDtoAndAddressApiEquals implements EqualsHelper<AddressDto, AddressApi>{

  @Override
  public boolean objectsEqual(AddressDto addressDto, AddressApi addressApi) {
    if (addressDto == null || addressApi == null) {
      return false;
    }

    return Objects.equals(addressDto.getPropertyNameNumber(), addressApi.getAddressLine1())
        && Objects.equals(addressDto.getLine1(), addressApi.getAddressLine2())
        && Objects.equals(addressDto.getLine2(), addressApi.getPremises())
        && Objects.equals(addressDto.getTown(), addressApi.getLocality())
        && Objects.equals(addressDto.getCounty(), addressApi.getRegion())
        && Objects.equals(addressDto.getCountry(), addressApi.getCountry())
        && Objects.equals(addressDto.getPoBox(), addressApi.getPoBox())
        && Objects.equals(addressDto.getCareOf(), addressApi.getCareOf())
        && Objects.equals(addressDto.getPostcode(), addressApi.getPostcode());
  }

}
