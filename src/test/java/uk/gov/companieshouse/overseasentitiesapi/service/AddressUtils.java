package uk.gov.companieshouse.overseasentitiesapi.service;

import uk.gov.companieshouse.api.model.utils.AddressApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;


/**
 * Utility class for creating dummy addresses for different models.
 * Regardless of the target model, providing the same string array should result in logically equivalent address instances.
 * The order of the field values in the array should be as follows: careOf, poBox, property name/number, address line 1,
 * address line 2, town, county/region, postcode, country.
 */
public class AddressUtils {

    /**
     * Creates a new {@link uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto} object with provided field values.
     *
     * @param fields The array of field values in order: careOf, poBox, propertyNameNumber, line1, line2, town, county, postcode, country.
     * @return A fully populated {@link uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto} object.
     */
    public static AddressDto createDummyAddressDto(String... fields) {
        AddressDto addressDto = new AddressDto();

        addressDto.setCareOf(fields[0]);
        addressDto.setPoBox(fields[1]);
        addressDto.setPropertyNameNumber(fields[2]);
        addressDto.setLine1(fields[3]);
        addressDto.setLine2(fields[4]);
        addressDto.setTown(fields[5]);
        addressDto.setCounty(fields[6]);
        addressDto.setPostcode(fields[7]);
        addressDto.setCountry(fields[8]);

        return addressDto;
    }

    /**
     * Creates a new {@link uk.gov.companieshouse.api.model.utils.AddressApi} object with provided field values.
     *
     * @param fields The array of field values in order: careOf, poBox, premises, addressLine1, addressLine2, locality, region, postcode, country.
     * @return A fully populated {@link uk.gov.companieshouse.api.model.utils.AddressApi} object.
     */
    public static AddressApi createDummyAddressApi(String... fields) {
        AddressApi addressApi = new AddressApi();

        addressApi.setCareOf(fields[0]);
        addressApi.setPoBox(fields[1]);
        addressApi.setPremises(fields[2]);
        addressApi.setAddressLine1(fields[3]);
        addressApi.setAddressLine2(fields[4]);
        addressApi.setLocality(fields[5]);
        addressApi.setRegion(fields[6]);
        addressApi.setPostcode(fields[7]);
        addressApi.setCountry(fields[8]);

        return addressApi;
    }

    /**
     * Creates a new {@link uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address} object with provided field values.
     *
     * @param fields The array of field values in order: careOf, poBox, houseNameNum, street, area, postTown, region, postCode, country.
     * @return A fully populated {@link uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address} object.
     */
    public static Address createDummyAddress(String... fields) {
        Address address = new Address();
        address.setCareOf(fields[0]);
        address.setPoBox(fields[1]);
        address.setHouseNameNum(fields[2]);
        address.setStreet(fields[3]);
        address.setArea(fields[4]);
        address.setPostTown(fields[5]);
        address.setRegion(fields[6]);
        address.setPostCode(fields[7]);
        address.setCountry(fields[8]);

        return address;
    }
}
