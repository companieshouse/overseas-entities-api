package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.overseasentitiesapi.mocks.AddressMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.ManagingOfficerMock;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
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
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

@ExtendWith(MockitoExtension.class)
class ManagingOfficerIndividualValidatorTest {

    private static final String LOGGING_CONTEXT = "12345";

    @InjectMocks
    private AddressDtoValidator addressDtoValidator;

    @InjectMocks
    private NationalityValidator nationalityValidator;

    private ManagingOfficerIndividualValidator managingOfficerIndividualValidator;

    private List<ManagingOfficerIndividualDto> managingOfficerIndividualDtoList;

    @BeforeEach
    void init() {
        managingOfficerIndividualValidator = new ManagingOfficerIndividualValidator(addressDtoValidator, nationalityValidator);
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
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.FIRST_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenFirstNameFieldIsNull() {
        managingOfficerIndividualDtoList.get(0).setFirstName(null);
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.FIRST_NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);

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
        String validationMessage = String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.FIRST_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLastNameFieldIsEmpty() {
        managingOfficerIndividualDtoList.get(0).setLastName("  ");
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.LAST_NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.LAST_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLastNameFieldIsNull() {
        managingOfficerIndividualDtoList.get(0).setLastName(null);
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.LAST_NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);

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

    @ParameterizedTest
    @ValueSource(strings = {"Name\n", "Name\r", "Дракон"})
    void testErrorReportedWhenLastNameFieldContainsInvalidCharacters(String name) {
        managingOfficerIndividualDtoList.get(0).setLastName(name);
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.LAST_NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.LAST_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenHasFormerNamesFieldIsNull() {
        managingOfficerIndividualDtoList.get(0).setHasFormerNames(null);
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.HAS_FORMER_NAMES_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.HAS_FORMER_NAMES_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenHasFormerNamesIsFalseAndFormerNamesAreSupplied() {
        managingOfficerIndividualDtoList.get(0).setHasFormerNames(false);
        managingOfficerIndividualDtoList.get(0).setFormerNames("Jim Bloggs Jr");
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.FORMER_NAMES_FIELD);
        String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.FORMER_NAMES_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenFormerNamesFieldIsEmpty() {
        managingOfficerIndividualDtoList.get(0).setFormerNames("  ");
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.FORMER_NAMES_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.FORMER_NAMES_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenFormerNamesFieldIsNull() {
        managingOfficerIndividualDtoList.get(0).setFormerNames(null);
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.FORMER_NAMES_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);

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
        String validationMessage = String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName);

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
        String validationMessage = String.format(ValidationMessages.DATE_NOT_IN_PAST_ERROR_MESSAGE, qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.DATE_OF_BIRTH_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenDateOfBirthFieldIsNull() {
        managingOfficerIndividualDtoList.get(0).setDateOfBirth(null);
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.DATE_OF_BIRTH_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.DATE_OF_BIRTH_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNationalityFieldIsEmpty() {
        managingOfficerIndividualDtoList.get(0).setNationality("  ");
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.NATIONALITY_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.NATIONALITY_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNationalityFieldIsNull() {
        managingOfficerIndividualDtoList.get(0).setNationality(null);
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.NATIONALITY_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.NATIONALITY_FIELD, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenOptionalSecondNationalityFieldIsEmpty() {
        managingOfficerIndividualDtoList.get(0).setSecondNationality("  ");
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenOptionalSecondNationalityFieldIsNull() {
        managingOfficerIndividualDtoList.get(0).setSecondNationality(null);
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenSameAddressFieldIsNull() {
        managingOfficerIndividualDtoList.get(0).setServiceAddressSameAsUsualResidentialAddress(null);
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.IS_SERVICE_ADDRESS_SAME_AS_USUAL_RESIDENTIAL_ADDRESS_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.IS_SERVICE_ADDRESS_SAME_AS_USUAL_RESIDENTIAL_ADDRESS_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenSameAddressFlagIsFalseWhenServiceAddressIsEmpty() {
        managingOfficerIndividualDtoList.get(0).setServiceAddressSameAsUsualResidentialAddress(false);
        managingOfficerIndividualDtoList.get(0).setServiceAddress(new AddressDto());
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.SERVICE_ADDRESS_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.PROPERTY_NAME_NUMBER_FIELD));
        assertError(getQualifiedFieldName(ManagingOfficerIndividualDto.SERVICE_ADDRESS_FIELD, AddressDto.PROPERTY_NAME_NUMBER_FIELD), validationMessage, errors);
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
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.OCCUPATION_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenOccupationFieldIsNull() {
        managingOfficerIndividualDtoList.get(0).setOccupation(null);
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.OCCUPATION_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);

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
        String validationMessage = String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.OCCUPATION_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenRoleAndResponsibilitiesFieldIsEmpty() {
        managingOfficerIndividualDtoList.get(0).setRoleAndResponsibilities("  ");
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.ROLE_AND_RESPONSIBILITIES_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.ROLE_AND_RESPONSIBILITIES_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenRoleAndResponsibilitiesFieldIsNull() {
        managingOfficerIndividualDtoList.get(0).setRoleAndResponsibilities(null);
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.ROLE_AND_RESPONSIBILITIES_FIELD);
        String validationMessage =  String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);

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
    void testNoErrorReportedWhenLineFeedIsUsed() {
        managingOfficerIndividualDtoList.get(0).setRoleAndResponsibilities("abc\nxyz");
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenCarriageReturnIsUsed() {
        managingOfficerIndividualDtoList.get(0).setRoleAndResponsibilities("abc\rxyz");
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenRoleAndResponsibilitiesFieldContainsInvalidCharacters() {
        managingOfficerIndividualDtoList.get(0).setRoleAndResponsibilities("Дракон");
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.ROLE_AND_RESPONSIBILITIES_FIELD);
        String validationMessage =  String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.ROLE_AND_RESPONSIBILITIES_FIELD, validationMessage, errors);
    }

    private void assertError(String fieldName, String message, Errors errors) {
        String qualifiedFieldName = MANAGING_OFFICERS_INDIVIDUAL_FIELD + "." + fieldName;
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(message).build();
        assertTrue(errors.containsError(err));
    }

    @Test
    void testNoErrorsWhenResignedOnDateFieldIsNow() {
        managingOfficerIndividualDtoList.get(0).setResignedOn(LocalDate.now());
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenResignedOnDateIsBeforeStartDate() {
        managingOfficerIndividualDtoList.get(0).setStartDate(LocalDate.now().plusDays(1));
        managingOfficerIndividualDtoList.get(0).setResignedOn(LocalDate.now());
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_INDIVIDUAL_FIELD,
                ManagingOfficerIndividualDto.RESIGNED_ON_DATE_FIELD);
        String validationMessage = String.format(ValidationMessages.CEASED_DATE_BEFORE_START_DATE_ERROR_MESSAGE, qualifiedFieldName);

        assertError(ManagingOfficerIndividualDto.RESIGNED_ON_DATE_FIELD, validationMessage, errors);
    }

    @Test
    void testNoErrorWhenResignedOnDateIsSameAsStartDate() {
        managingOfficerIndividualDtoList.get(0).setStartDate(LocalDate.now().minusDays(1));
        managingOfficerIndividualDtoList.get(0).setResignedOn(LocalDate.now().minusDays(1));
        Errors errors = managingOfficerIndividualValidator.validate(managingOfficerIndividualDtoList, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
    }
}
