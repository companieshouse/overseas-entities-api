package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.StringValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.util.List;
import java.util.Objects;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators.setErrorMsgToLocation;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

@Component
public class AddressDtoValidator {

    public Errors validate(String parentAddressField, AddressDto addressDto, List<String> allowedCountries, Errors errors, String loggingContext) {
        if (UtilsValidators.isNotNull(addressDto, parentAddressField, errors, loggingContext)) {
            validatePropertyNameNumber(parentAddressField, addressDto.getPropertyNameNumber(), errors, loggingContext);
            validateLine1(parentAddressField, addressDto.getLine1(), errors, loggingContext);
            if (Objects.nonNull(addressDto.getLine2())) {
                validateLine2(parentAddressField, addressDto.getLine2(), errors, loggingContext);
            }
            validateTown(parentAddressField, addressDto.getTown(), errors, loggingContext);
            if (Objects.nonNull(addressDto.getCounty())) {
                validateCounty(parentAddressField, addressDto.getCounty(), errors, loggingContext);
            }
            validateCountry(parentAddressField, addressDto.getCountry(), allowedCountries, errors, loggingContext);
            if (Objects.nonNull(addressDto.getPostcode())) {
                validatePostcode(parentAddressField, addressDto.getPostcode(), errors, loggingContext);
            }
        }
        return errors;
    }

    public Errors validateOtherAddressIsNotSupplied(String parentAddressField, AddressDto addressDto, Errors errors, String loggingContext) {
        if (Objects.nonNull(addressDto)) {
            StringValidators.checkIsEmpty(addressDto.getPropertyNameNumber(), getQualifiedFieldName(parentAddressField, AddressDto.PROPERTY_NAME_NUMBER_FIELD), errors, loggingContext);
            StringValidators.checkIsEmpty(addressDto.getLine1(), getQualifiedFieldName(parentAddressField, AddressDto.LINE_1_FIELD), errors, loggingContext);
            StringValidators.checkIsEmpty(addressDto.getLine2(), getQualifiedFieldName(parentAddressField, AddressDto.LINE_2_FIELD), errors, loggingContext);
            StringValidators.checkIsEmpty(addressDto.getTown(), getQualifiedFieldName(parentAddressField, AddressDto.TOWN_FIELD), errors, loggingContext);
            StringValidators.checkIsEmpty(addressDto.getCounty(), getQualifiedFieldName(parentAddressField, AddressDto.COUNTY_FIELD), errors, loggingContext);
            StringValidators.checkIsEmpty(addressDto.getCountry(), getQualifiedFieldName(parentAddressField, AddressDto.COUNTRY_FIELD), errors, loggingContext);
            StringValidators.checkIsEmpty(addressDto.getPostcode(), getQualifiedFieldName(parentAddressField, AddressDto.POSTCODE_FIELD), errors, loggingContext);
            StringValidators.checkIsEmpty(addressDto.getCareOf(), getQualifiedFieldName(parentAddressField, AddressDto.CARE_OF_FIELD), errors, loggingContext);
            StringValidators.checkIsEmpty(addressDto.getLocality(), getQualifiedFieldName(parentAddressField, AddressDto.LOCALITY_FIELD), errors, loggingContext);
            StringValidators.checkIsEmpty(addressDto.getPoBox(), getQualifiedFieldName(parentAddressField, AddressDto.PO_BOX_FIELD), errors, loggingContext);
        }
        return errors;
    }

    private boolean validatePropertyNameNumber(String parentAddressField, String propertyNameNumber, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(parentAddressField, AddressDto.PROPERTY_NAME_NUMBER_FIELD);
        return StringValidators.isNotBlank(propertyNameNumber, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(propertyNameNumber, 50, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(propertyNameNumber, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateLine1(String parentAddressField, String line1, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(parentAddressField, AddressDto.LINE_1_FIELD);
        return StringValidators.isNotBlank(line1, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(line1, 50, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(line1, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateLine2(String parentAddressField, String line2, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(parentAddressField, AddressDto.LINE_2_FIELD);
        return StringValidators.isLessThanOrEqualToMaxLength(line2, 50, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(line2, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateTown(String parentAddressField, String town, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(parentAddressField, AddressDto.TOWN_FIELD);
        return StringValidators.isNotBlank(town, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(town, 50, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(town, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateCounty(String parentAddressField, String county, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(parentAddressField, AddressDto.COUNTY_FIELD);
        return StringValidators.isLessThanOrEqualToMaxLength(county, 50, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isValidCharacters(county, qualifiedFieldName, errors, loggingContext);
    }

    private void validateCountry(String parentAddressField, String country, List<String> allowedCountries, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(parentAddressField, AddressDto.COUNTRY_FIELD);

        boolean countryNotBlank = StringValidators.isNotBlank(country, qualifiedFieldName, errors, loggingContext);

        if (countryNotBlank) {
            boolean isOnList = allowedCountries.contains(country);
            if (!isOnList) {
                var validationMessage = String.format(ValidationMessages.COUNTRY_NOT_ON_LIST_ERROR_MESSAGE, country);
                setErrorMsgToLocation(errors, qualifiedFieldName, validationMessage);
                ApiLogger.infoContext(loggingContext, validationMessage);
            }
        }
    }

    private void validatePostcode(String parentAddressField, String postcode, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(parentAddressField, AddressDto.POSTCODE_FIELD);

        StringValidators.isLessThanOrEqualToMaxLength(postcode, 20, qualifiedFieldName, errors, loggingContext);
        StringValidators.isValidCharacters(postcode, qualifiedFieldName, errors, loggingContext);
    }
}
