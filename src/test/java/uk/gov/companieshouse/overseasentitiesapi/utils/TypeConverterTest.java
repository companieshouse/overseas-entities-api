package uk.gov.companieshouse.overseasentitiesapi.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.companieshouse.api.model.utils.AddressApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;

class TypeConverterTest {

  @Mock
  private AddressDto addressDto;

  @Mock
  private AddressApi addressApi;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testPrivateConstructor() throws NoSuchMethodException {
    Constructor<TypeConverter> constructor = TypeConverter.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(constructor.getModifiers()));

    constructor.setAccessible(true);
    InvocationTargetException e = assertThrows(InvocationTargetException.class,
        constructor::newInstance);
    assertTrue(e.getTargetException() instanceof IllegalAccessError);
  }

  @Test
  void addressDtoToAddressTest() {
    var expectedAddress = new Address();
    expectedAddress.setCareOf(addressDto.getCareOf());
    expectedAddress.setPoBox(addressDto.getPoBox());
    expectedAddress.setHouseNameNum(addressDto.getPropertyNameNumber());
    expectedAddress.setStreet(addressDto.getLine1());
    expectedAddress.setArea(addressDto.getLine2());
    expectedAddress.setPostTown(addressDto.getTown());
    expectedAddress.setRegion(addressDto.getCounty());
    expectedAddress.setPostCode(addressDto.getPostcode());
    expectedAddress.setCountry(addressDto.getCountry());

    Address result = TypeConverter.addressDtoToAddress(addressDto);
    assertEquals(expectedAddress, result);
  }

  @Test
  void addressDtoToAddressTestNull() {
    assertNull(TypeConverter.addressDtoToAddress(null));
  }

  @Test
  void addressApiToAddressTest() {
    var expectedAddress = new Address();
    expectedAddress.setCareOf(addressApi.getCareOf());
    expectedAddress.setPoBox(addressApi.getPoBox());
    expectedAddress.setHouseNameNum(addressApi.getPremises());
    expectedAddress.setStreet(addressApi.getAddressLine1());
    expectedAddress.setArea(addressApi.getRegion());
    expectedAddress.setPostTown(addressApi.getLocality());
    expectedAddress.setRegion(addressApi.getRegion());
    expectedAddress.setPostCode(addressApi.getPostcode());
    expectedAddress.setCountry(addressApi.getCountry());

    Address result = TypeConverter.addressApiToAddress(addressApi);
    assertEquals(expectedAddress, result);
  }

  @Test
  void addressApitoAddressTestNull() {
    assertNull(TypeConverter.addressApiToAddress(null));
  }

  @Test
  void stringToLocalDateTest() {
    String dateStr = "2023-05-30";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate expectedDate = LocalDate.parse(dateStr, formatter);

    LocalDate result = TypeConverter.stringToLocalDate(dateStr);
    assertEquals(expectedDate, result);
  }

  @Test
  void stringToLocalDateTestNull() {
    assertNull(TypeConverter.stringToLocalDate(null));
  }

  @Test
  void registeredOfficeAddressApiToAddressInputIsNull() {
    assertNull(TypeConverter.registeredOfficeAddressApiToAddress(null));
  }
}
