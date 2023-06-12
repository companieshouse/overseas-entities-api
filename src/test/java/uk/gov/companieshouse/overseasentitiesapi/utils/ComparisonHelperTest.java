package uk.gov.companieshouse.overseasentitiesapi.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.api.model.utils.AddressApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;

class ComparisonHelperTest {

  @Test
  void equalsAddressDtoAndAddressApiCompleteFalseTest(){
    String[] addressDtoFields = {"PropertyNameNumber", "Line1", "Line2", "Town", "County", "Country", "PoBox", "CareOf", "Postcode"};
    String[] addressApiFields = addressDtoFields.clone();

    var output = false;

    for(var i = 0; i < addressDtoFields.length; i++){
      addressDtoFields[i] = "Different--" + addressDtoFields[i];
      if(i < addressDtoFields.length - 1) {
        addressApiFields[i + 1] = "Different--" + addressApiFields[i + 1];
      }

      AddressDto addressDto = new AddressDto();
      addressDto.setPropertyNameNumber(addressDtoFields[0]);
      addressDto.setLine1(addressDtoFields[1]);
      addressDto.setLine2(addressDtoFields[2]);
      addressDto.setTown(addressDtoFields[3]);
      addressDto.setCounty(addressDtoFields[4]);
      addressDto.setCountry(addressDtoFields[5]);
      addressDto.setPoBox(addressDtoFields[6]);
      addressDto.setCareOf(addressDtoFields[7]);
      addressDto.setPostcode(addressDtoFields[8]);

      AddressApi addressApi = new AddressApi();
      addressApi.setPremises(addressApiFields[0]);
      addressApi.setAddressLine2(addressApiFields[1]);
      addressApi.setAddressLine1(addressApiFields[2]);
      addressApi.setLocality(addressApiFields[3]);
      addressApi.setRegion(addressApiFields[4]);
      addressApi.setCountry(addressApiFields[5]);
      addressApi.setPoBox(addressApiFields[6]);
      addressApi.setCareOf(addressApiFields[7]);
      addressApi.setPostcode(addressApiFields[8]);

      output |= ComparisonHelper.equals(addressDto, addressApi);

      if(i < addressDtoFields.length - 1) {
        addressDtoFields[i] = addressApiFields[i];
        addressApiFields[i + 1] = addressDtoFields[i + 1];
      }
    }
    assertFalse(output);
  }


  @Test
  void equalsAddressDtoAndAddressApiReturnFalseWhenOneFieldDiffers() {
    AddressDto addressDto = new AddressDto();
    addressDto.setPropertyNameNumber("PropertyNameNumber");
    addressDto.setLine1("Line1");
    addressDto.setLine2("Line2");
    addressDto.setTown("Town");
    addressDto.setCounty("County");
    addressDto.setCountry("Country");
    addressDto.setPoBox("PoBox");
    addressDto.setCareOf("CareOf");
    addressDto.setPostcode("Postcode");

    AddressApi addressApi = new AddressApi();
    addressApi.setPremises("PropertyNameNumber");
    addressApi.setAddressLine2("Line1");
    addressApi.setAddressLine1("Line2");
    addressApi.setLocality("Town");
    addressApi.setRegion("County");
    addressApi.setCountry("DifferentCountry");
    addressApi.setPoBox("PoBox");
    addressApi.setCareOf("CareOf");
    addressApi.setPostcode("Postcode");

    assertFalse(ComparisonHelper.equals(addressDto, addressApi));
  }
  @Test
  void equalsAddressDtoAndAddressApiReturnFalseWhenMultipleFieldsDiffer() {
    AddressDto addressDto = new AddressDto();
    addressDto.setPropertyNameNumber("PropertyNameNumber");
    addressDto.setLine1("Line1");
    addressDto.setLine2("Line2");
    addressDto.setTown("Town");
    addressDto.setCounty("County");
    addressDto.setCountry("Country");
    addressDto.setPoBox("PoBox");
    addressDto.setCareOf("CareOf");
    addressDto.setPostcode("Postcode");

    AddressApi addressApi = new AddressApi();
    addressApi.setPremises("DifferentPropertyNameNumber");
    addressApi.setAddressLine2("DifferentLine1");
    addressApi.setAddressLine1("Line2");
    addressApi.setLocality("Town");
    addressApi.setRegion("County");
    addressApi.setCountry("Country");
    addressApi.setPoBox("DifferentPoBox");
    addressApi.setCareOf("CareOf");
    addressApi.setPostcode("Postcode");

    assertFalse(ComparisonHelper.equals(addressDto, addressApi));
  }

  @Test
  void equalsAddressDtoAndAddressApiWhenNullReturnFalse() {
    AddressApi addressApi = new AddressApi();
    assertFalse(ComparisonHelper.equals(null, addressApi));
    assertFalse(ComparisonHelper.equals(new AddressDto(), null));
  }

  @Test
  void equalsLocalDateAndStringWithTimeReturnCorrectResult() {
    LocalDate localDate = LocalDate.of(2001, 2, 3);
    String dateString = "2001-02-03 00:00:00.000000";

    assertTrue(ComparisonHelper.equals(localDate, dateString));
  }

  @Test
  void equalsLocalDateAndStringWithoutTimeReturnCorrectResult() {
    LocalDate localDate = LocalDate.of(2001, 2, 3);
    String dateString = "2001-02-03";

    assertTrue(ComparisonHelper.equals(localDate, dateString));
  }

  @Test
  void equalsLocalDateWhenBothNull(){
    LocalDate localDate = null;
    String strDate = null;
    assertTrue(ComparisonHelper.equals(localDate, strDate));
  }

  @Test
  void equalsLocalDateWhenLocalDateNull() {
    LocalDate localDate = null;
    String strDate = "2001-02-03";
    assertFalse(ComparisonHelper.equals(localDate, strDate));
  }

  @Test
  void equalsLocalDateWhenStringDateNull() {
    LocalDate localDate = LocalDate.of(2001, 2, 3);;
    String strDate = null;
    assertFalse(ComparisonHelper.equals(localDate, strDate));
  }

  @Test
  void equalsListAndArrayReturnCorrectResult() {
    List<String> list = Arrays.asList("element1", "element2", "element3");
    String[] array = {"element1", "element2", "element3"};

    assertTrue(ComparisonHelper.equals(list, array));
  }

  @Test
  void equalsListAndArrayWithSameElementsReturnCorrectResult() {
    List<String> list = Arrays.asList("element2", "element1", "element3");
    String[] array = {"element1", "element2", "element3"};

    assertTrue(ComparisonHelper.equals(list, array));
  }

  @Test
  void equalsListAndArrayWithDifferentElementsReturnCorrectResult() {
    List<String> list = Arrays.asList("element2", "element1", "element3");
    String[] array = {"element1", "element2"};

    assertFalse(ComparisonHelper.equals(list, array));
  }

  @Test
  void equalsListAndArrayWhenNullReturnCorrectResult() {
    List<String> list = Arrays.asList("element1", "element2", "element3");

    assertFalse(ComparisonHelper.equals(null, new String[]{"element1", "element2", "element3"}));
    assertFalse(ComparisonHelper.equals(list, null));

    List<String> nullList = null;
    String[] nullArray = null;

    assertTrue(ComparisonHelper.equals(nullList, nullArray));
  }

  @Test
  void equalsBooleanNull() {
    Boolean first = null;
    boolean second = true;
    assertFalse(ComparisonHelper.equals(first, second));
  }

  @Test
  void equalsBooleanAllFalse() {
    Boolean first = false;
    boolean second = false;
    assertTrue(ComparisonHelper.equals(first, second));
  }

  @Test
  void equalsBooleanAllTrue() {
    Boolean first = true;
    boolean second = true;
    assertTrue(ComparisonHelper.equals(first, second));
  }

  @Test
  void equalsBooleanOneFalse() {
    Boolean first = false;
    boolean second = true;
    assertFalse(ComparisonHelper.equals(first, second));
  }

  @Test
  void equalsBooleanOtherFalse() {
    Boolean first = true;
    boolean second = false;
    assertFalse(ComparisonHelper.equals(first, second));
  }

  @Test
  void equalsAddressesNullTrue() {
    AddressDto addressDto = null;
    AddressApi addressApi = null;

    assertFalse(ComparisonHelper.equals(addressDto, addressApi));
  }

  @Test
  void equalsAddressApiNullTrue() {
    AddressDto addressDto = new AddressDto();
    AddressApi addressApi = null;

    assertFalse(ComparisonHelper.equals(addressDto, addressApi));
  }

  @Test
  void equalsAddressDtoNullTrue() {
    AddressDto addressDto = null;
    AddressApi addressApi = new AddressApi();

    assertFalse(ComparisonHelper.equals(addressDto, addressApi));
  }

  @Test
  void equalAddressesTrue() {
    AddressDto addressDto = new AddressDto();
    addressDto.setPropertyNameNumber("1");
    addressDto.setLine1("line1");
    addressDto.setLine2("line2");
    addressDto.setTown("town1");
    addressDto.setCounty("county1");
    addressDto.setCountry("Canada");
    addressDto.setPoBox("pobox1");
    addressDto.setCareOf("careOf1");
    addressDto.setPostcode("postcode1");

    AddressApi addressApi = new AddressApi();
    addressApi.setPremises("1");
    addressApi.setAddressLine1("line1");
    addressApi.setAddressLine2("line2");
    addressApi.setLocality("town1");
    addressApi.setRegion("county1");
    addressApi.setCountry("Canada");
    addressApi.setPoBox("pobox1");
    addressApi.setCareOf("careOf1");
    addressApi.setPostcode("postcode1");

    assertTrue(ComparisonHelper.equals(addressDto, addressApi));
  }

  @Test
  void notEqualAddressesFalse() {
    AddressDto addressDto = new AddressDto();
    addressDto.setPropertyNameNumber("1");
    addressDto.setLine1("line1");
    addressDto.setLine2("line2");
    addressDto.setTown("town1");
    addressDto.setCounty("county1");
    addressDto.setCountry("Canada");
    addressDto.setPoBox("pobox1");
    addressDto.setCareOf("careOf1");
    addressDto.setPostcode("postcode1");

    AddressApi addressApi = new AddressApi();
    addressApi.setPremises("2");
    addressApi.setAddressLine1("line2");
    addressApi.setAddressLine2("line1");
    addressApi.setLocality("town2");
    addressApi.setRegion("county2");
    addressApi.setCountry("Australia");
    addressApi.setPoBox("pobox2");
    addressApi.setCareOf("careOf2");
    addressApi.setPostcode("postcode2");

    assertFalse(ComparisonHelper.equals(addressDto, addressApi));
  }
}
