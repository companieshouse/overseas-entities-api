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
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.MANAGING_OFFICERS_CORPORATE_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

@ExtendWith(MockitoExtension.class)
class ManagingOfficerCorporateValidatorTest {

    private static final String LOGGING_CONTEXT = "12345";

    private ManagingOfficerCorporateValidator managingOfficerCorporateValidator;

    @InjectMocks
    private AddressDtoValidator addressDtoValidator;

    private List<ManagingOfficerCorporateDto> managingOfficerCorporateDtoList;

    @BeforeEach
    public void init() {
        managingOfficerCorporateValidator = new ManagingOfficerCorporateValidator(addressDtoValidator);
        managingOfficerCorporateDtoList = new ArrayList<>();
        ManagingOfficerCorporateDto managingOfficerCorporateDto = ManagingOfficerMock.getManagingOfficerCorporateDto();
        managingOfficerCorporateDto.setPrincipalAddress(AddressMock.getAddressDto());
        managingOfficerCorporateDtoList.add(managingOfficerCorporateDto);
    }

    @Test
    void testNoErrorReportedWhenAllFieldsAreCorrect() {
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenNameFieldIsEmpty() {
        managingOfficerCorporateDtoList.get(0).setName("   ");
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);
        assertError(ManagingOfficerCorporateDto.NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNameFieldIsNull() {
        managingOfficerCorporateDtoList.get(0).setName(null);
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(ManagingOfficerCorporateDto.NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNameFieldExceedsMaxLength() {
        managingOfficerCorporateDtoList.get(0).setName(StringUtils.repeat("A", 51));
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.NAME_FIELD);
        String validationMessage = qualifiedFieldName + " must be 50 characters or less";
        assertError(ManagingOfficerCorporateDto.NAME_FIELD, validationMessage, errors);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Name\n", "Name\r", "Дракон"})
    void testErrorReportedWhenNameFieldContainsInvalidCharacters(String name) {
        managingOfficerCorporateDtoList.get(0).setName(name);
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName);
        assertError(ManagingOfficerCorporateDto.NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenSameAddressFieldsIsNull() {
        managingOfficerCorporateDtoList.get(0).setServiceAddressSameAsPrincipalAddress(null);
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.IS_SERVICE_ADDRESS_SAME_AS_PRINCIPAL_ADDRESS_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);

        assertError(ManagingOfficerCorporateDto.IS_SERVICE_ADDRESS_SAME_AS_PRINCIPAL_ADDRESS_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenSameAddressFlagIsFalseWhenServiceAddressIsEmpty() {
        managingOfficerCorporateDtoList.get(0).setServiceAddressSameAsPrincipalAddress(false);
        managingOfficerCorporateDtoList.get(0).setServiceAddress(new AddressDto());
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.SERVICE_ADDRESS_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.PROPERTY_NAME_NUMBER_FIELD));
        assertError(getQualifiedFieldName(ManagingOfficerCorporateDto.SERVICE_ADDRESS_FIELD, AddressDto.PROPERTY_NAME_NUMBER_FIELD), validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenSameAddressFlagIsTrueWhenServiceAddressNotEmpty() {
        managingOfficerCorporateDtoList.get(0).setServiceAddressSameAsPrincipalAddress(true);
        managingOfficerCorporateDtoList.get(0).setServiceAddress(AddressMock.getAddressDto());
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.SERVICE_ADDRESS_FIELD);

        String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.PROPERTY_NAME_NUMBER_FIELD));
        assertError(getQualifiedFieldName(ManagingOfficerCorporateDto.SERVICE_ADDRESS_FIELD, AddressDto.PROPERTY_NAME_NUMBER_FIELD), validationMessage, errors);
        validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.LINE_1_FIELD));
        assertError(getQualifiedFieldName(ManagingOfficerCorporateDto.SERVICE_ADDRESS_FIELD, AddressDto.LINE_1_FIELD), validationMessage, errors);
        validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.TOWN_FIELD));
        assertError(getQualifiedFieldName(ManagingOfficerCorporateDto.SERVICE_ADDRESS_FIELD, AddressDto.TOWN_FIELD), validationMessage, errors);
        validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.COUNTY_FIELD));
        assertError(getQualifiedFieldName(ManagingOfficerCorporateDto.SERVICE_ADDRESS_FIELD, AddressDto.COUNTY_FIELD), validationMessage, errors);
        validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.COUNTRY_FIELD));
        assertError(getQualifiedFieldName(ManagingOfficerCorporateDto.SERVICE_ADDRESS_FIELD, AddressDto.COUNTRY_FIELD), validationMessage, errors);
        validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.POSTCODE_FIELD));
        assertError(getQualifiedFieldName(ManagingOfficerCorporateDto.SERVICE_ADDRESS_FIELD, AddressDto.POSTCODE_FIELD), validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLegalFormFieldIsEmpty() {
        managingOfficerCorporateDtoList.get(0).setLegalForm("  ");
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.LEGAL_FORM_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);
        assertError(ManagingOfficerCorporateDto.LEGAL_FORM_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLegalFormFieldIsNull() {
        managingOfficerCorporateDtoList.get(0).setLegalForm(null);
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.LEGAL_FORM_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(ManagingOfficerCorporateDto.LEGAL_FORM_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLegalFormFieldExceedsMaxLength() {
        managingOfficerCorporateDtoList.get(0).setLegalForm(StringUtils.repeat("A", 161));
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.LEGAL_FORM_FIELD);
        String validationMessage = qualifiedFieldName + " must be 160 characters or less";
        assertError(ManagingOfficerCorporateDto.LEGAL_FORM_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLegalFormFieldContainsInvalidCharacters() {
        managingOfficerCorporateDtoList.get(0).setLegalForm("Дракон");
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.LEGAL_FORM_FIELD);
        String validationMessage = String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName);
        assertError(ManagingOfficerCorporateDto.LEGAL_FORM_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLawGovernedFieldIsEmpty() {
        managingOfficerCorporateDtoList.get(0).setLawGoverned("  ");
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.LAW_GOVERNED_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);
        assertError(ManagingOfficerCorporateDto.LAW_GOVERNED_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLawGovernedFieldIsNull() {
        managingOfficerCorporateDtoList.get(0).setLawGoverned(null);
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.LAW_GOVERNED_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(ManagingOfficerCorporateDto.LAW_GOVERNED_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLawGovernedFieldExceedsMaxLength() {
        managingOfficerCorporateDtoList.get(0).setLawGoverned(StringUtils.repeat("A", 161));
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.LAW_GOVERNED_FIELD);
        String validationMessage = qualifiedFieldName + " must be 160 characters or less";
        assertError(ManagingOfficerCorporateDto.LAW_GOVERNED_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLawGovernedFieldContainsInvalidCharacters() {
        managingOfficerCorporateDtoList.get(0).setLawGoverned("Дракон");
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.LAW_GOVERNED_FIELD);
        String validationMessage = String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName);
        assertError(ManagingOfficerCorporateDto.LAW_GOVERNED_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenIsOnPublicRegisterNameFieldIsNull() {
        managingOfficerCorporateDtoList.get(0).setOnRegisterInCountryFormedIn(null);
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.IS_ON_REGISTER_IN_COUNTRY_FORMED_IN_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(ManagingOfficerCorporateDto.IS_ON_REGISTER_IN_COUNTRY_FORMED_IN_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenPublicRegisterNameFieldIsEmpty() {
        managingOfficerCorporateDtoList.get(0).setOnRegisterInCountryFormedIn(Boolean.TRUE);
        managingOfficerCorporateDtoList.get(0).setPublicRegisterName("  ");
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.PUBLIC_REGISTER_NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);
        assertError(ManagingOfficerCorporateDto.PUBLIC_REGISTER_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenPublicRegisterNameFieldIsNull() {
        managingOfficerCorporateDtoList.get(0).setOnRegisterInCountryFormedIn(Boolean.TRUE);
        managingOfficerCorporateDtoList.get(0).setPublicRegisterName(null);
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.PUBLIC_REGISTER_NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(ManagingOfficerCorporateDto.PUBLIC_REGISTER_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenPublicRegisterNameFieldExceedsMaxLength() {
        managingOfficerCorporateDtoList.get(0).setOnRegisterInCountryFormedIn(Boolean.TRUE);
        managingOfficerCorporateDtoList.get(0).setPublicRegisterName(StringUtils.repeat("A", 161));
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.PUBLIC_REGISTER_NAME_FIELD);
        String validationMessage = qualifiedFieldName + " must be 160 characters or less";
        assertError(ManagingOfficerCorporateDto.PUBLIC_REGISTER_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenPublicRegisterNameFieldContainsInvalidCharacters() {
        managingOfficerCorporateDtoList.get(0).setOnRegisterInCountryFormedIn(Boolean.TRUE);
        managingOfficerCorporateDtoList.get(0).setPublicRegisterName("Дракон");
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.PUBLIC_REGISTER_NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName);
        assertError(ManagingOfficerCorporateDto.PUBLIC_REGISTER_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenRegistrationNumberFieldIsEmpty() {
        managingOfficerCorporateDtoList.get(0).setOnRegisterInCountryFormedIn(Boolean.TRUE);
        managingOfficerCorporateDtoList.get(0).setRegistrationNumber("  ");
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.REGISTRATION_NUMBER_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);
        assertError(ManagingOfficerCorporateDto.REGISTRATION_NUMBER_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenRegistrationNumberFieldIsNull() {
        managingOfficerCorporateDtoList.get(0).setOnRegisterInCountryFormedIn(Boolean.TRUE);
        managingOfficerCorporateDtoList.get(0).setRegistrationNumber(null);
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.REGISTRATION_NUMBER_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(ManagingOfficerCorporateDto.REGISTRATION_NUMBER_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenRegistrationNumberFieldExceedsMaxLength() {
        managingOfficerCorporateDtoList.get(0).setOnRegisterInCountryFormedIn(Boolean.TRUE);
        managingOfficerCorporateDtoList.get(0).setRegistrationNumber(StringUtils.repeat("A", 161));
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.REGISTRATION_NUMBER_FIELD);
        String validationMessage = qualifiedFieldName + " must be 160 characters or less";
        assertError(ManagingOfficerCorporateDto.REGISTRATION_NUMBER_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenRegistrationNumberFieldContainsInvalidCharacters() {
        managingOfficerCorporateDtoList.get(0).setOnRegisterInCountryFormedIn(Boolean.TRUE);
        managingOfficerCorporateDtoList.get(0).setRegistrationNumber("Дракон");
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.REGISTRATION_NUMBER_FIELD);
        String validationMessage = String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName);
        assertError(ManagingOfficerCorporateDto.REGISTRATION_NUMBER_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenOnRegisterFlagIsFalseWhenPublicRegisterNameFieldNotEmpty() {
        managingOfficerCorporateDtoList.get(0).setOnRegisterInCountryFormedIn(Boolean.FALSE);
        managingOfficerCorporateDtoList.get(0).setPublicRegisterName("Name");
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.PUBLIC_REGISTER_NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, qualifiedFieldName);
        assertError(ManagingOfficerCorporateDto.PUBLIC_REGISTER_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenOnRegisterFlagIsFalseWhenRegistrationNumberFieldNotEmpty() {
        managingOfficerCorporateDtoList.get(0).setOnRegisterInCountryFormedIn(Boolean.FALSE);
        managingOfficerCorporateDtoList.get(0).setRegistrationNumber("123456");
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.REGISTRATION_NUMBER_FIELD);
        String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, qualifiedFieldName);
        assertError(ManagingOfficerCorporateDto.REGISTRATION_NUMBER_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenRoleAndResponsibilitiesFieldIsEmpty() {
        managingOfficerCorporateDtoList.get(0).setRoleAndResponsibilities("   ");
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.ROLE_AND_RESPONSIBILITIES_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);
        assertError(ManagingOfficerCorporateDto.ROLE_AND_RESPONSIBILITIES_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenRoleAndResponsibilitiesFieldIsNull() {
        managingOfficerCorporateDtoList.get(0).setRoleAndResponsibilities(null);
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.ROLE_AND_RESPONSIBILITIES_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(ManagingOfficerCorporateDto.ROLE_AND_RESPONSIBILITIES_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenRoleAndResponsibilitiesFieldExceedsMaxLength() {
        managingOfficerCorporateDtoList.get(0).setRoleAndResponsibilities(StringUtils.repeat("A", 257));
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.ROLE_AND_RESPONSIBILITIES_FIELD);
        String validationMessage = qualifiedFieldName + " must be 256 characters or less";
        assertError(ManagingOfficerCorporateDto.ROLE_AND_RESPONSIBILITIES_FIELD, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenLineFeedIsUsed() {
        managingOfficerCorporateDtoList.get(0).setRoleAndResponsibilities("abc\nxyz");
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenCarriageReturnIsUsed() {
        managingOfficerCorporateDtoList.get(0).setRoleAndResponsibilities("abc\rxyz");
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }


    @Test
    void testErrorReportedWhenRoleAndResponsibilitiesFieldContainsInvalidCharacters() {
        managingOfficerCorporateDtoList.get(0).setRoleAndResponsibilities("Дракон");
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.ROLE_AND_RESPONSIBILITIES_FIELD);
        String validationMessage = String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName);
        assertError(ManagingOfficerCorporateDto.ROLE_AND_RESPONSIBILITIES_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenContactFullNameFieldIsEmpty() {
        managingOfficerCorporateDtoList.get(0).setContactFullName("   ");
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.CONTACT_FULL_NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);
        assertError(ManagingOfficerCorporateDto.CONTACT_FULL_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenContactFullNameFieldIsNull() {
        managingOfficerCorporateDtoList.get(0).setContactFullName(null);
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.CONTACT_FULL_NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(ManagingOfficerCorporateDto.CONTACT_FULL_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenContactFullNameFieldExceedsMaxLength() {
        managingOfficerCorporateDtoList.get(0).setContactFullName(StringUtils.repeat("A", 161));
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.CONTACT_FULL_NAME_FIELD);
        String validationMessage = qualifiedFieldName + " must be 160 characters or less";
        assertError(ManagingOfficerCorporateDto.CONTACT_FULL_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenContactFullNameFieldContainsInvalidCharacters() {
        managingOfficerCorporateDtoList.get(0).setContactFullName("Дракон");
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.CONTACT_FULL_NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName);
        assertError(ManagingOfficerCorporateDto.CONTACT_FULL_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenEmailFieldIsEmpty() {
        managingOfficerCorporateDtoList.get(0).setContactEmail("  ");
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.CONTACT_EMAIL_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);
        assertError(ManagingOfficerCorporateDto.CONTACT_EMAIL_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenEmailFieldIsNull() {
        managingOfficerCorporateDtoList.get(0).setContactEmail(null);
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.CONTACT_EMAIL_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(ManagingOfficerCorporateDto.CONTACT_EMAIL_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenEmailFieldExceedsMaxLength() {
        managingOfficerCorporateDtoList.get(0).setContactEmail(StringUtils.repeat("A", 251) + "@long.com");
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.CONTACT_EMAIL_FIELD);
        String validationMessage = qualifiedFieldName + " must be 250 characters or less";
        assertError(ManagingOfficerCorporateDto.CONTACT_EMAIL_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenEmailFieldContainsInvalidEntry() {
        managingOfficerCorporateDtoList.get(0).setContactEmail("wrong.com");
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                MANAGING_OFFICERS_CORPORATE_FIELD,
                ManagingOfficerCorporateDto.CONTACT_EMAIL_FIELD);
        String validationMessage = String.format(ValidationMessages.INVALID_EMAIL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(ManagingOfficerCorporateDto.CONTACT_EMAIL_FIELD,  validationMessage, errors);
    }

    @ParameterizedTest
    @ValueSource(strings = {"vsocarroll@QQQQQQQT123465798U123456789V123456789W123456789X123456789Y123456.companieshouse.gov.uk",
            "socarrollA123456789B132456798C123456798D123456789@T123465798U123456789V123456789W123456789X123456789Y123456.companieshouse.gov.uk",
            "socarrollA123456789B132456798C123456798D123456789E123456789F123XX@T123465798U123456789V123456789W123456789X123456789Y123456.companieshouse.gov.uk"})
    void testNoErrorReportedWithLongEmailAddress(String email) {
        managingOfficerCorporateDtoList.get(0).setContactEmail(email);
        Errors errors = managingOfficerCorporateValidator.validate(managingOfficerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    private void assertError(String fieldName, String message, Errors errors) {
        String qualifiedFieldName = MANAGING_OFFICERS_CORPORATE_FIELD + "." + fieldName;
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(message).build();
        assertTrue(errors.containsError(err));
    }
}
