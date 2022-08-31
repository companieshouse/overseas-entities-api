package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.StringValidators;
import uk.gov.companieshouse.service.rest.err.Errors;

@Component
public class AddressDtoValidator {

    public Errors validate(String field, AddressDto addressDto, Errors errs, String loggingContext) {
        validatePropertyNameNumber(field, addressDto.getPropertyNameNumber(), errs, loggingContext);
        validateLine1(field, addressDto.getLine1(), errs, loggingContext);
        validateLine2(field, addressDto.getLine2(), errs, loggingContext);
        validateTown(field, addressDto.getTown(), errs, loggingContext);
        validateCounty(field, addressDto.getCounty(), errs, loggingContext);
        validateCountry(field, addressDto.getCountry(), errs, loggingContext);
        validatePostcode(field, addressDto.getPostcode(), errs, loggingContext);
        return errs;
    }

    private boolean validatePropertyNameNumber(String field, String propertyNameNumber, Errors errs, String loggingContext) {
        String location = getAddressFieldName(field, AddressDto.PROPERTY_NAME_NUMBER_FIELD);
        return StringValidators.validateStringNotBlank(propertyNameNumber, location, errs, loggingContext)
                && StringValidators.validateLength(propertyNameNumber, 50, location, errs, loggingContext)
                && StringValidators.validateCharacters(propertyNameNumber, location, errs, loggingContext);
    }

    private boolean validateLine1(String field, String line1, Errors errs, String loggingContext) {
        String location = getAddressFieldName(field, AddressDto.LINE_1);
        return StringValidators.validateStringNotBlank(line1, location, errs, loggingContext)
                && StringValidators.validateLength(line1, 50, location, errs, loggingContext)
                && StringValidators.validateCharacters(line1, location, errs, loggingContext);
    }

    private boolean validateLine2(String field, String line2, Errors errs, String loggingContext) {
        String location = getAddressFieldName(field, AddressDto.LINE_2);
        return StringValidators.validateLength(line2, 50, location, errs, loggingContext)
                && StringValidators.validateCharacters(line2, location, errs, loggingContext);
    }

    private boolean validateTown(String field, String town, Errors errs, String loggingContext) {
        String location = getAddressFieldName(field, AddressDto.TOWN);
        return StringValidators.validateStringNotBlank(town, location, errs, loggingContext)
                && StringValidators.validateLength(town, 50, location, errs, loggingContext)
                && StringValidators.validateCharacters(town, location, errs, loggingContext);
    }

    private boolean validateCounty(String field, String county, Errors errs, String loggingContext) {
        String location = getAddressFieldName(field, AddressDto.COUNTY);
        return StringValidators.validateLength(county, 50, location, errs, loggingContext)
                && StringValidators.validateCharacters(county, location, errs, loggingContext);
    }

    private boolean validateCountry(String field, String country, Errors errs, String loggingContext) {
        String location = getAddressFieldName(field, AddressDto.COUNTRY);
        return StringValidators.validateStringNotBlank(country, location, errs, loggingContext);
    }

    private boolean validatePostcode(String field, String postcode, Errors errs, String loggingContext) {
        String location = getAddressFieldName(field, AddressDto.POSTCODE);
        return StringValidators.validateLength(postcode, 20, location, errs, loggingContext)
                && StringValidators.validateCharacters(postcode, location, errs, loggingContext);
    }

    private String getAddressFieldName(String field, String fieldName) {
        return String.format("%s.%s", field, fieldName);
    }
}
