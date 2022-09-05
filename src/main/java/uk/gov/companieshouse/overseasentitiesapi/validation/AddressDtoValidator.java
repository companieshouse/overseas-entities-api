package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.Country;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.StringValidators;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.util.Objects;

@Component
public class AddressDtoValidator {

    public Errors validate(String parentAddressField, AddressDto addressDto, Errors errors, String loggingContext) {
        validatePropertyNameNumber(parentAddressField, addressDto.getPropertyNameNumber(), errors, loggingContext);
        validateLine1(parentAddressField, addressDto.getLine1(), errors, loggingContext);
        if(Objects.nonNull(addressDto.getLine2())) {
            validateLine2(parentAddressField, addressDto.getLine2(), errors, loggingContext);
        }
        validateTown(parentAddressField, addressDto.getTown(), errors, loggingContext);
        if(Objects.nonNull(addressDto.getCounty())) {
            validateCounty(parentAddressField, addressDto.getCounty(), errors, loggingContext);
        }
        validateCountry(parentAddressField, addressDto.getCountry(), errors, loggingContext);
        if(Objects.nonNull(addressDto.getPostcode())) {
            validatePostcode(parentAddressField, addressDto.getPostcode(), errors, loggingContext);
        }
        return errors;
    }

    private boolean validatePropertyNameNumber(String parentAddressField, String propertyNameNumber, Errors errors, String loggingContext) {
        String location = getQualifiedAddressFieldName(parentAddressField, AddressDto.PROPERTY_NAME_NUMBER_FIELD);
        return StringValidators.validateStringNotBlank(propertyNameNumber, location, errors, loggingContext)
                && StringValidators.validateMaxLength(propertyNameNumber, 50, location, errors, loggingContext)
                && StringValidators.validateCharacters(propertyNameNumber, location, errors, loggingContext);
    }

    private boolean validateLine1(String parentAddressField, String line1, Errors errors, String loggingContext) {
        String location = getQualifiedAddressFieldName(parentAddressField, AddressDto.LINE_1);
        return StringValidators.validateStringNotBlank(line1, location, errors, loggingContext)
                && StringValidators.validateMaxLength(line1, 50, location, errors, loggingContext)
                && StringValidators.validateCharacters(line1, location, errors, loggingContext);
    }

    private boolean validateLine2(String parentAddressField, String line2, Errors errors, String loggingContext) {
        String location = getQualifiedAddressFieldName(parentAddressField, AddressDto.LINE_2);
        return StringValidators.validateMaxLength(line2, 50, location, errors, loggingContext)
                && StringValidators.validateCharacters(line2, location, errors, loggingContext);
    }

    private boolean validateTown(String parentAddressField, String town, Errors errors, String loggingContext) {
        String location = getQualifiedAddressFieldName(parentAddressField, AddressDto.TOWN);
        return StringValidators.validateStringNotBlank(town, location, errors, loggingContext)
                && StringValidators.validateMaxLength(town, 50, location, errors, loggingContext)
                && StringValidators.validateCharacters(town, location, errors, loggingContext);
    }

    private boolean validateCounty(String parentAddressField, String county, Errors errors, String loggingContext) {
        String location = getQualifiedAddressFieldName(parentAddressField, AddressDto.COUNTY);
        return StringValidators.validateMaxLength(county, 50, location, errors, loggingContext)
                && StringValidators.validateCharacters(county, location, errors, loggingContext);
    }

    private boolean validateCountry(String parentAddressField, String country, Errors errors, String loggingContext) {
        String location = getQualifiedAddressFieldName(parentAddressField, AddressDto.COUNTRY);
        return Country.isValid(country, location, errors, loggingContext);
    }

    private boolean validatePostcode(String parentAddressField, String postcode, Errors errors, String loggingContext) {
        String location = getQualifiedAddressFieldName(parentAddressField, AddressDto.POSTCODE);
        return StringValidators.validateMaxLength(postcode, 20, location, errors, loggingContext)
                && StringValidators.validateCharacters(postcode, location, errors, loggingContext);
    }

    private String getQualifiedAddressFieldName(String parentAddressField, String fieldName) {
        return String.format("%s.%s", parentAddressField, fieldName);
    }
}
