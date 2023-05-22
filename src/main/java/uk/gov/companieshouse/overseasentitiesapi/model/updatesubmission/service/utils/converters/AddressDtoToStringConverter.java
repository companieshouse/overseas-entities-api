package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.service.utils.converters;

import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;

public class AddressDtoToStringConverter implements Converter<AddressDto, String>{
  @Override
  public String convert(AddressDto address) {
    StringBuilder sb = new StringBuilder();

    if (address.getCareOf() != null) {
      sb.append("c/o ").append(address.getCareOf()).append(", ");
    }

    if (address.getPropertyNameNumber() != null) {
      sb.append(address.getPropertyNameNumber()).append(", ");
    }

    if (address.getLine1() != null) {
      sb.append(address.getLine1()).append(", ");
    }

    if (address.getLine2() != null) {
      sb.append(address.getLine2()).append(", ");
    }

    if (address.getTown() != null) {
      sb.append(address.getTown()).append(", ");
    }

    if (address.getCounty() != null) {
      sb.append(address.getCounty()).append(", ");
    }

    if (address.getLocality() != null) {
      sb.append(address.getLocality()).append(", ");
    }

    if (address.getPostcode() != null) {
      sb.append(address.getPostcode()).append(", ");
    }

    if (address.getCountry() != null) {
      sb.append(address.getCountry());
    }

    if (address.getPoBox() != null) {
      sb.append(", PO Box ").append(address.getPoBox());
    }

    // Remove the trailing comma, if there is one
    if (sb.length() > 0 && sb.charAt(sb.length() - 2) == ',') {
      sb.setLength(sb.length() - 2);
    }

    return sb.toString().trim();
  }

}
