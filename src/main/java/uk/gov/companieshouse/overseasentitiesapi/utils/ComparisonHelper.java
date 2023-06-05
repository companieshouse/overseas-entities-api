package uk.gov.companieshouse.overseasentitiesapi.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import uk.gov.companieshouse.api.model.utils.AddressApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.PersonName;

public class ComparisonHelper {

  private ComparisonHelper() {
    throw new IllegalAccessError("Use the static method designation");
  }

  public static boolean equals(AddressDto addressDto, AddressApi addressApi) {
    if (addressDto == null || addressApi == null) {
      return false;
    }
    return Objects.equals(addressDto.getPropertyNameNumber(), addressApi.getPremises())
        && Objects.equals(addressDto.getLine1(), addressApi.getAddressLine2())
        && Objects.equals(addressDto.getLine2(), addressApi.getAddressLine1())
        && Objects.equals(addressDto.getTown(), addressApi.getLocality())
        && Objects.equals(addressDto.getCounty(), addressApi.getRegion())
        && Objects.equals(addressDto.getCountry(), addressApi.getCountry())
        && Objects.equals(addressDto.getPoBox(), addressApi.getPoBox())
        && Objects.equals(addressDto.getCareOf(), addressApi.getCareOf())
        && Objects.equals(addressDto.getPostcode(), addressApi.getPostcode());
  }

  public static boolean equals(LocalDate a, String b) {
    if (a == null && b == null) {
      return true;
    }
    if (a == null || b == null) {
      return false;
    }
    if (b.contains(" ")) {
      b = b.substring(0, b.indexOf(" "));
    }

    var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    var localDate = LocalDate.parse(b, formatter);
    return a.equals(localDate);
  }

  public static boolean equals(List<String> list, String[] array) {
    if (list == null && array == null) {
      return true;
    } else if (list == null || array == null) {
      return false;
    }

    var arrayFromList = list.toArray();
    Arrays.sort(arrayFromList);
    Arrays.sort(array);
    return Arrays.equals(arrayFromList, array);
  }

  public static boolean equals(PersonName personName, String string) {
    return personName.toString().equals(string);
  }
}
