package uk.gov.companieshouse.overseasentitiesapi.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import uk.gov.companieshouse.api.model.company.RegisteredOfficeAddressApi;
import uk.gov.companieshouse.api.model.utils.AddressApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;

public class TypeConverter {

  private TypeConverter() {
    throw new IllegalAccessError("Use the static methods instead of instantiating Converters");
  }

  public static uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address addressDtoToAddress(AddressDto addressDto) {
    if (addressDto == null) {
      return null;
    }
    var address = new uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address();

    address.setCareOf(addressDto.getCareOf());
    address.setPoBox(addressDto.getPoBox());
    address.setHouseNameNum(addressDto.getPropertyNameNumber());
    address.setStreet(addressDto.getLine1());
    address.setArea(addressDto.getLine2());
    address.setPostTown(addressDto.getTown());
    address.setRegion(addressDto.getCounty());
    address.setPostCode(addressDto.getPostcode());
    address.setCountry(addressDto.getCountry());

    return address;
  }

  public static uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address addressApiToAddress(AddressApi addressApi) {
    if (addressApi == null) {
      return null;
    }

    var address = new uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address();

    address.setCareOf(addressApi.getCareOf());
    address.setPoBox(addressApi.getPoBox());
    address.setHouseNameNum(addressApi.getPremises());
    address.setStreet(addressApi.getAddressLine1());
    address.setArea(addressApi.getAddressLine2());
    address.setPostTown(addressApi.getLocality());
    address.setRegion(addressApi.getRegion());
    address.setPostCode(addressApi.getPostcode());
    address.setCountry(addressApi.getCountry());

    return address;
  }

  public static LocalDate stringToLocalDate(String dateStr) {
    if (dateStr == null) {
      return null;
    }
    var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    return LocalDate.parse(dateStr, formatter);
  }

  public static uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address registeredOfficeAddressApiToAddress(
      RegisteredOfficeAddressApi registeredOfficeAddressApi) {
    if (registeredOfficeAddressApi == null) {
      return null;
    }

    var address = new uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address();
    address.setHouseNameNum(registeredOfficeAddressApi.getPremises());
    address.setStreet(registeredOfficeAddressApi.getAddressLine1());
    address.setArea(registeredOfficeAddressApi.getAddressLine2());
    address.setPostTown(registeredOfficeAddressApi.getLocality());
    address.setRegion(registeredOfficeAddressApi.getRegion());
    address.setCountry(registeredOfficeAddressApi.getCountry());
    address.setPostCode(registeredOfficeAddressApi.getPostalCode());
    address.setPoBox(registeredOfficeAddressApi.getPoBox());
    address.setCareOf(registeredOfficeAddressApi.getCareOf());
    return address;
  }

  public static String localDateToString(LocalDate date) {
    if (date == null) {
      return null;
    }
    var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    return date.format(formatter);
  }
}
