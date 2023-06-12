package uk.gov.companieshouse.overseasentitiesapi.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import uk.gov.companieshouse.api.model.common.Address;
import uk.gov.companieshouse.api.model.officers.FormerNamesApi;
import uk.gov.companieshouse.api.model.officers.OfficerRoleApi;
import uk.gov.companieshouse.api.model.utils.AddressApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.PersonName;

import static uk.gov.companieshouse.overseasentitiesapi.utils.FormerNameConcatenation.concatenateFormerNames;

public class ComparisonHelper {

  private ComparisonHelper() {
    throw new IllegalAccessError("Use the static method designation");
  }

  public static boolean equals(AddressDto addressDto, AddressApi addressApi) {
    var nullValuesCheck = handleNulls(addressDto, addressApi);
    if (nullValuesCheck != null) {
      return nullValuesCheck;
    }

    return Objects.equals(addressDto.getPropertyNameNumber(), addressApi.getPremises())
            && Objects.equals(addressDto.getLine1(), addressApi.getAddressLine1())
            && Objects.equals(addressDto.getLine2(), addressApi.getAddressLine2())
            && Objects.equals(addressDto.getTown(), addressApi.getLocality())
            && Objects.equals(addressDto.getCounty(), addressApi.getRegion())
            && Objects.equals(addressDto.getCountry(), addressApi.getCountry())
            && Objects.equals(addressDto.getPoBox(), addressApi.getPoBox())
            && Objects.equals(addressDto.getCareOf(), addressApi.getCareOf())
            && Objects.equals(addressDto.getPostcode(), addressApi.getPostcode());
  }

  public static boolean equals(AddressDto addressDto,
                               uk.gov.companieshouse.api.model.managingofficerdata.AddressApi addressApi) {
    var nullValuesCheck = handleNulls(addressDto, addressApi);
    if (nullValuesCheck != null) {
      return nullValuesCheck;
    }

    return Objects.equals(addressDto.getPropertyNameNumber(), addressApi.getPremises())
            && Objects.equals(addressDto.getLine1(), addressApi.getAddressLine1())
            && Objects.equals(addressDto.getLine2(), addressApi.getAddressLine2())
            && Objects.equals(addressDto.getTown(), addressApi.getLocality())
            && Objects.equals(addressDto.getCounty(), addressApi.getRegion())
            && Objects.equals(addressDto.getCountry(), addressApi.getCountry())
            && Objects.equals(addressDto.getPoBox(), addressApi.getPoBox())
            && Objects.equals(addressDto.getCareOf(), addressApi.getCareOf())
            && Objects.equals(addressDto.getPostcode(), addressApi.getPostalCode());
  }

  public static boolean equals(AddressDto addressDto, Address address) {
    var nullValuesCheck = handleNulls(addressDto, address);
    if (nullValuesCheck != null) {
      return nullValuesCheck;
    }

    return Objects.equals(addressDto.getPropertyNameNumber(), address.getPremises())
            && Objects.equals(addressDto.getLine1(), address.getAddressLine1())
            && Objects.equals(addressDto.getLine2(), address.getAddressLine2())
            && Objects.equals(addressDto.getTown(), address.getLocality())
            && Objects.equals(addressDto.getCounty(), address.getRegion())
            && Objects.equals(addressDto.getCountry(), address.getCountry())
            && Objects.equals(addressDto.getPoBox(), address.getPoBox())
            && Objects.equals(addressDto.getCareOf(), address.getCareOf())
            && Objects.equals(addressDto.getPostcode(), address.getPostalCode());
  }

  public static boolean equals(LocalDate a, String b) {
    var nullValuesCheck = handleNulls(a, b);
    if (nullValuesCheck != null) {
      return nullValuesCheck;
    }

    if (b.contains(" ")) {
      b = b.substring(0, b.indexOf(" "));
    }

    var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    var localDate = LocalDate.parse(b, formatter);
    return a.equals(localDate);
  }

  public static boolean equals(List<String> list, String[] array) {
    var nullValuesCheck = handleNulls(list, array);
    if (nullValuesCheck != null) {
      return nullValuesCheck;
    }

    var arrayFromList = list.toArray();
    Arrays.sort(arrayFromList);
    Arrays.sort(array);
    return Arrays.equals(arrayFromList, array);
  }

  public static boolean equals(PersonName personName, String string) {
    var nullValuesCheck = handleNulls(string, string);
    if (nullValuesCheck != null) {
      return nullValuesCheck;
    }


    return personName.toString().equals(string);
  }

  public static boolean equals(Boolean b, boolean b2) {
    var nullValuesCheck = handleNulls(b, b2);
    if (nullValuesCheck != null) {
      return nullValuesCheck;
    }

    return b == b2;
  }

  public static boolean equals(String string, OfficerRoleApi officerRoleApi) {
    var nullValuesCheck = handleNulls(string, officerRoleApi);
    if (nullValuesCheck != null) {
      return nullValuesCheck;
    }

    return string.equals(officerRoleApi.getOfficerRole());
  }

  public static boolean equals(String string, String[] strings) {
    var nullValuesCheck = handleNulls(string, strings);
    if (nullValuesCheck != null) {
      return nullValuesCheck;
    }

    return string.equals(String.join(" ", strings));
  }

  public static boolean equalsFormerName(String string, List<FormerNamesApi> strings) {
    var nullValuesCheck = handleNulls(string, strings);
    if (nullValuesCheck != null) {
      return nullValuesCheck;
    }

    var concatenatedFormerNames = concatenateFormerNames(strings);

    return string.equals(concatenatedFormerNames);
  }

  private static Boolean handleNulls(Object a, Object b) {
    if (a == null || b == null) {
      return a == null && b == null;
    }

    return null;
  }
}
