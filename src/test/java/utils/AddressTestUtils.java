package utils;

import uk.gov.companieshouse.api.model.utils.AddressApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;


/**
 * Utility class for creating dummy addresses for different models. Regardless of the target model,
 * providing the same string array should result in logically equivalent address instances. The
 * order of the field values in the array should be as follows: careOf, poBox, property name/number,
 * address line 1, address line 2, town, county/region, postcode, country.
 */
public class AddressTestUtils {

    /**
     * Creates a new {@link uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto} object
     * with provided field values.
     *
     * @param fieldValues The array of field values in order: careOf, poBox, propertyNameNumber,
     *                    line1, line2, town, county, postcode, country.
     * @return A fully populated
     * {@link uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto} object.
     */
    public static AddressDto createDummyAddressDto(String... fieldValues) {
        AddressDto addressDto = new AddressDto();

        addressDto.setCareOf(fieldValues[0]);
        addressDto.setPoBox(fieldValues[1]);
        addressDto.setPropertyNameNumber(fieldValues[2]);
        addressDto.setLine1(fieldValues[3]);
        addressDto.setLine2(fieldValues[4]);
        addressDto.setTown(fieldValues[5]);
        addressDto.setCounty(fieldValues[6]);
        addressDto.setPostcode(fieldValues[7]);
        addressDto.setCountry(fieldValues[8]);
        
        return addressDto;
    }

    /**
     * Creates a new {@link uk.gov.companieshouse.api.model.utils.AddressApi} object with provided
     * field values.
     *
     * @param fieldValues The array of field values in order: careOf, poBox, premises, addressLine1,
     *                    addressLine2, locality, region, postcode, country.
     * @return A fully populated {@link uk.gov.companieshouse.api.model.utils.AddressApi} object.
     */
    public static AddressApi createDummyModelUtilsAddressApi(String... fieldValues) {
        AddressApi addressApi = new AddressApi();

        addressApi.setCareOf(fieldValues[0]);
        addressApi.setPoBox(fieldValues[1]);
        addressApi.setPremises(fieldValues[2]);
        addressApi.setAddressLine1(fieldValues[3]);
        addressApi.setAddressLine2(fieldValues[4]);
        addressApi.setLocality(fieldValues[5]);
        addressApi.setRegion(fieldValues[6]);
        addressApi.setPostcode(fieldValues[7]);
        addressApi.setCountry(fieldValues[8]);

        return addressApi;
    }

    /**
     * Creates a new {@link uk.gov.companieshouse.api.model.managingofficerdata.AddressApi} object
     * with provided field values.
     *
     * @param fieldValues The array of field values in order: careOf, poBox, premises, addressLine1,
     *                    addressLine2, locality, region, postalCode, country.
     * @return A fully populated
     * {@link uk.gov.companieshouse.api.model.managingofficerdata.AddressApi} object.
     */
    public static uk.gov.companieshouse.api.model.managingofficerdata.AddressApi createDummyManagingOfficerAddressApi(
            String... fieldValues) {
        uk.gov.companieshouse.api.model.managingofficerdata.AddressApi addressApi = new uk.gov.companieshouse.api.model.managingofficerdata.AddressApi();

        addressApi.setCareOf(fieldValues[0]);
        addressApi.setPoBox(fieldValues[1]);
        addressApi.setPremises(fieldValues[2]);
        addressApi.setAddressLine1(fieldValues[3]);
        addressApi.setAddressLine2(fieldValues[4]);
        addressApi.setLocality(fieldValues[5]);
        addressApi.setRegion(fieldValues[6]);
        addressApi.setPostalCode(fieldValues[7]);
        addressApi.setCountry(fieldValues[8]);

        return addressApi;
    }


    /**
     * Creates a new
     * {@link
     * uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address}
     * object with provided field values.
     *
     * @param fieldValues The array of field values in order: careOf, poBox, houseNameNum, street,
     *                    area, postTown, region, postCode, country.
     * @return A fully populated
     * {@link
     * uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address}
     * object.
     */
    public static Address createDummyAddress(String... fieldValues) {
        Address address = new Address();

        address.setCareOf(fieldValues[0]);
        address.setPoBox(fieldValues[1]);
        address.setHouseNameNum(fieldValues[2]);
        address.setStreet(fieldValues[3]);
        address.setArea(fieldValues[4]);
        address.setPostTown(fieldValues[5]);
        address.setRegion(fieldValues[6]);
        address.setPostCode(fieldValues[7]);
        address.setCountry(fieldValues[8]);

        return address;
    }

    /**
     * Creates a new {@link uk.gov.companieshouse.api.model.common.Address} object with provided
     * field values.
     *
     * @param fieldValues The array of field values in order: careOf, poBox, premises, addressLine1,
     *                    addressLine2, locality, region, postalCode, country.
     * @return A fully populated {@link uk.gov.companieshouse.api.model.common.Address} object.
     */
    public static uk.gov.companieshouse.api.model.common.Address createDummyCommonAddress(
            String... fieldValues) {
        uk.gov.companieshouse.api.model.common.Address address = new uk.gov.companieshouse.api.model.common.Address();

        address.setCareOf(fieldValues[0]);
        address.setPoBox(fieldValues[1]);
        address.setPremises(fieldValues[2]);
        address.setAddressLine1(fieldValues[3]);
        address.setAddressLine2(fieldValues[4]);
        address.setLocality(fieldValues[5]);
        address.setRegion(fieldValues[6]);
        address.setPostalCode(fieldValues[7]);
        address.setCountry(fieldValues[8]);

        return address;
    }

}
