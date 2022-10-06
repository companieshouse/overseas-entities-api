package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.overseasentitiesapi.mocks.AddressMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.ManagingOfficerMock;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

public class ManagingOfficerIndividualValidatorTest {

    private static final String LOGGING_CONTEXT = "12345";

    private AddressDtoValidator addressDtoValidator;

    private ManagingOfficerIndividualValidator managingOfficerIndividualValidator;

    private List<ManagingOfficerIndividualDto> managingOfficerIndividualDtoList;

    @BeforeEach
    public void init() {
        addressDtoValidator = new AddressDtoValidator();
        managingOfficerIndividualValidator = new ManagingOfficerIndividualValidator(addressDtoValidator);
        managingOfficerIndividualDtoList = new ArrayList<>();
        ManagingOfficerIndividualDto managingOfficerIndividualDto = ManagingOfficerMock.getManagingOfficerIndividualDto();
        managingOfficerIndividualDto.setUsualResidentialAddress(AddressMock.getAddressDto());
        managingOfficerIndividualDto.setServiceAddress(new AddressDto());
        managingOfficerIndividualDtoList.add(managingOfficerIndividualDto);
    }

    @Test
    void testNoErrorReportedWhenAllFieldsAreCorrect() {
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenFirstNameFieldIsEmpty() {
        managingOfficerIndividualDtoList.get(0).setFirstName("  ");
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.FIRST_NAME_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.FIRST_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenFirstNameFieldIsNull() {
        managingOfficerIndividualDtoList.get(0).setFirstName(null);
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.FIRST_NAME_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.FIRST_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenFirstNameFieldExceedsMaxLength() {
        managingOfficerIndividualDtoList.get(0).setFirstName(StringUtils.repeat("A", 51));
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.FIRST_NAME_FIELD);

        assertError(ManagingOfficerIndividualDto.FIRST_NAME_FIELD, qualifiedFieldName + " must be 50 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenFirstNameFieldContainsInvalidCharacters() {
        managingOfficerIndividualDtoList.get(0).setFirstName("Дракон");
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.FIRST_NAME_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.FIRST_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLastNameFieldIsEmpty() {
        managingOfficerIndividualDtoList.get(0).setLastName("  ");
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.LAST_NAME_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.LAST_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLastNameFieldIsNull() {
        managingOfficerIndividualDtoList.get(0).setLastName(null);
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.LAST_NAME_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.LAST_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLastNameFieldExceedsMaxLength() {
        managingOfficerIndividualDtoList.get(0).setLastName(StringUtils.repeat("A", 161));
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.LAST_NAME_FIELD);

        assertError(ManagingOfficerIndividualDto.LAST_NAME_FIELD, qualifiedFieldName + " must be 160 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenLastNameFieldContainsInvalidCharacters() {
        managingOfficerIndividualDtoList.get(0).setLastName("Дракон");
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.LAST_NAME_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.LAST_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenFormerNamesFieldIsEmpty() {
        managingOfficerIndividualDtoList.get(0).setFormerNames("  ");
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.FORMER_NAMES_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.FORMER_NAMES_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenFormerNamesFieldIsNull() {
        managingOfficerIndividualDtoList.get(0).setFormerNames(null);
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.FORMER_NAMES_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.FORMER_NAMES_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenFormerNamesFieldExceedsMaxLength() {
        managingOfficerIndividualDtoList.get(0).setFormerNames(StringUtils.repeat("A", 261));
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.FORMER_NAMES_FIELD);

        assertError(ManagingOfficerIndividualDto.FORMER_NAMES_FIELD, qualifiedFieldName + " must be 260 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenFormerNamesFieldContainsInvalidCharacters() {
        managingOfficerIndividualDtoList.get(0).setFormerNames("Дракон");
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.FORMER_NAMES_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.FORMER_NAMES_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenDateOfBirthFieldIsInThePast() {
        managingOfficerIndividualDtoList.get(0).setDateOfBirth(LocalDate.of(1970,1, 1));
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenDateOfBirthFieldIsNow() {
        managingOfficerIndividualDtoList.get(0).setDateOfBirth(LocalDate.now());
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenDateOfBirthIsInTheFuture() {
        managingOfficerIndividualDtoList.get(0).setDateOfBirth(LocalDate.now().plusDays(1));
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.DATE_OF_BIRTH_FIELD);
        String validationMessage = ValidationMessages.DATE_NOT_IN_PAST_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.DATE_OF_BIRTH_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenDateOfBirthFieldIsNull() {
        managingOfficerIndividualDtoList.get(0).setDateOfBirth(null);
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.DATE_OF_BIRTH_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.DATE_OF_BIRTH_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNationalityFieldIsEmpty() {
        managingOfficerIndividualDtoList.get(0).setNationality("  ");
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.NATIONALITY_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.NATIONALITY_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNationalityFieldIsNull() {
        managingOfficerIndividualDtoList.get(0).setNationality(null);
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.NATIONALITY_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.NATIONALITY_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNationalityFieldExceedsMaxLength() {
        managingOfficerIndividualDtoList.get(0).setNationality(StringUtils.repeat("A", 51));
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.NATIONALITY_FIELD);

        assertError(ManagingOfficerIndividualDto.NATIONALITY_FIELD, qualifiedFieldName + " must be 50 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenNationalityFieldContainsInvalidCharacters() {
        managingOfficerIndividualDtoList.get(0).setNationality("Дракон");
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.NATIONALITY_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.NATIONALITY_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenSameAddressFieldIsNull() {
        managingOfficerIndividualDtoList.get(0).setServiceAddressSameAsUsualResidentialAddress(null);
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.IS_SERVICE_ADDRESS_SAME_AS_USUAL_RESIDENTIAL_ADDRESS_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.IS_SERVICE_ADDRESS_SAME_AS_USUAL_RESIDENTIAL_ADDRESS_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenSameAddressFlagIsFalseWhenServiceAddressIsEmpty() {
        managingOfficerIndividualDtoList.get(0).setServiceAddressSameAsUsualResidentialAddress(false);
        managingOfficerIndividualDtoList.get(0).setServiceAddress(new AddressDto());
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        assertTrue(errors.size() > 0);
    }

    @Test
    void testErrorReportedWhenSameAddressFlagIsTrueWhenServiceAddressNotEmpty() {
        managingOfficerIndividualDtoList.get(0).setServiceAddressSameAsUsualResidentialAddress(true);
        managingOfficerIndividualDtoList.get(0).setServiceAddress(AddressMock.getAddressDto());
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.SERVICE_ADDRESS_FIELD);

        String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.PROPERTY_NAME_NUMBER_FIELD));
        assertError(getQualifiedFieldName(ManagingOfficerIndividualDto.SERVICE_ADDRESS_FIELD, AddressDto.PROPERTY_NAME_NUMBER_FIELD), validationMessage, errors);
        validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.LINE_1_FIELD));
        assertError(getQualifiedFieldName(ManagingOfficerIndividualDto.SERVICE_ADDRESS_FIELD, AddressDto.LINE_1_FIELD), validationMessage, errors);
        validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.TOWN_FIELD));
        assertError(getQualifiedFieldName(ManagingOfficerIndividualDto.SERVICE_ADDRESS_FIELD, AddressDto.TOWN_FIELD), validationMessage, errors);
        validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.COUNTY_FIELD));
        assertError(getQualifiedFieldName(ManagingOfficerIndividualDto.SERVICE_ADDRESS_FIELD, AddressDto.COUNTY_FIELD), validationMessage, errors);
        validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.COUNTRY_FIELD));
        assertError(getQualifiedFieldName(ManagingOfficerIndividualDto.SERVICE_ADDRESS_FIELD, AddressDto.COUNTRY_FIELD), validationMessage, errors);
        validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.POSTCODE_FIELD));
        assertError(getQualifiedFieldName(ManagingOfficerIndividualDto.SERVICE_ADDRESS_FIELD, AddressDto.POSTCODE_FIELD), validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenOccupationFieldIsEmpty() {
        managingOfficerIndividualDtoList.get(0).setOccupation("  ");
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.OCCUPATION_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.OCCUPATION_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenOccupationFieldIsNull() {
        managingOfficerIndividualDtoList.get(0).setOccupation(null);
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.OCCUPATION_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.OCCUPATION_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenOccupationFieldExceedsMaxLength() {
        managingOfficerIndividualDtoList.get(0).setOccupation(StringUtils.repeat("A", 101));
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.OCCUPATION_FIELD);

        assertError(ManagingOfficerIndividualDto.OCCUPATION_FIELD, qualifiedFieldName + " must be 100 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenOccupationFieldContainsInvalidCharacters() {
        managingOfficerIndividualDtoList.get(0).setOccupation("Дракон");
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.OCCUPATION_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.OCCUPATION_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenRoleAndResponsibilitiesFieldIsEmpty() {
        managingOfficerIndividualDtoList.get(0).setRoleAndResponsibilities("  ");
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.ROLE_AND_RESPONSIBILITIES_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.ROLE_AND_RESPONSIBILITIES_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenRoleAndResponsibilitiesFieldIsNull() {
        managingOfficerIndividualDtoList.get(0).setRoleAndResponsibilities(null);
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.ROLE_AND_RESPONSIBILITIES_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.ROLE_AND_RESPONSIBILITIES_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenRoleAndResponsibilitiesFieldExceedsMaxLength() {
        managingOfficerIndividualDtoList.get(0).setRoleAndResponsibilities(StringUtils.repeat("A", 257));
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.ROLE_AND_RESPONSIBILITIES_FIELD);

        assertError(ManagingOfficerIndividualDto.ROLE_AND_RESPONSIBILITIES_FIELD, qualifiedFieldName + " must be 256 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenRoleAndResponsibilitiesFieldContainsInvalidCharacters() {
        managingOfficerIndividualDtoList.get(0).setRoleAndResponsibilities("Дракон");
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.ROLE_AND_RESPONSIBILITIES_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.ROLE_AND_RESPONSIBILITIES_FIELD, validationMessage, errors);
    }

    private void assertError(String fieldName, String message, Errors errors) {
        String qualifiedFieldName = MANAGING_OFFICERS_INDIVIDUAL_FIELD + "." + fieldName;
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(message).build();
        assertTrue(errors.containsError(err));
    }
}
