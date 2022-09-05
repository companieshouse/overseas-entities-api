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
        String qualifiedFieldName = getQualifiedAddressFieldName(parentAddressField, AddressDto.PROPERTY_NAME_NUMBER_FIELD);
        return StringValidators.isValidNotBlank(propertyNameNumber, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidMaxLength(propertyNameNumber, 50, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(propertyNameNumber, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateLine1(String parentAddressField, String line1, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedAddressFieldName(parentAddressField, AddressDto.LINE_1_FIELD);
        return StringValidators.isValidNotBlank(line1, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidMaxLength(line1, 50, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(line1, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateLine2(String parentAddressField, String line2, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedAddressFieldName(parentAddressField, AddressDto.LINE_2_FIELD);
        return StringValidators.isValidMaxLength(line2, 50, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(line2, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateTown(String parentAddressField, String town, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedAddressFieldName(parentAddressField, AddressDto.TOWN_FIELD);
        return StringValidators.isValidNotBlank(town, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidMaxLength(town, 50, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(town, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateCounty(String parentAddressField, String county, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedAddressFieldName(parentAddressField, AddressDto.COUNTY_FIELD);
        return StringValidators.isValidMaxLength(county, 50, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(county, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateCountry(String parentAddressField, String country, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedAddressFieldName(parentAddressField, AddressDto.COUNTRY_FIELD);
        return Country.isValid(country, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validatePostcode(String parentAddressField, String postcode, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedAddressFieldName(parentAddressField, AddressDto.POSTCODE_FIELD);
        return StringValidators.isValidMaxLength(postcode, 20, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(postcode, qualifiedFieldName, errors, loggingContext);
    }

    private String getQualifiedAddressFieldName(String parentAddressField, String fieldName) {
        return String.format("%s.%s", parentAddressField, fieldName);
    }
}
