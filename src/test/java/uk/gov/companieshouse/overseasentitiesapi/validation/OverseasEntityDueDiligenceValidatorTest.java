package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.overseasentitiesapi.mocks.OverseasEntityDueDiligenceMock;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.DueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntityDueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.utils.DataSanitisation;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.OVERSEAS_ENTITY_DUE_DILIGENCE;

class OverseasEntityDueDiligenceValidatorTest {

    private static final String LOGGING_CONTEXT = "12345";
    private AddressDtoValidator addressDtoValidator;
    private OverseasEntityDueDiligenceValidator overseasEntityDueDiligenceValidator;
    private OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto;

    @BeforeEach
    void init() {
        addressDtoValidator = new AddressDtoValidator(new DataSanitisation());
        overseasEntityDueDiligenceValidator = new OverseasEntityDueDiligenceValidator(addressDtoValidator);
        overseasEntityDueDiligenceDto = OverseasEntityDueDiligenceMock.getOverseasEntityDueDiligenceDto();
        overseasEntityDueDiligenceDto.getAddress().setCountry("England");
        overseasEntityDueDiligenceDto.setIdentityDate(LocalDate.now().minusMonths(1));
    }

    @Test
    void testNoErrorReportedWhenIdentityDateFieldIsNow() {
        overseasEntityDueDiligenceDto.setIdentityDate(LocalDate.now());
        Errors errors = overseasEntityDueDiligenceValidator.validate(overseasEntityDueDiligenceDto, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testIsEmptyFalseWhenAllFieldsSupplied() {
        assertFalse(overseasEntityDueDiligenceDto.isEmpty());
    }

    @Test
    void testIsEmptyFalseWhenOneFieldSupplied() {
        OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto = new OverseasEntityDueDiligenceDto();
        overseasEntityDueDiligenceDto.setName("Test");
        assertFalse(overseasEntityDueDiligenceDto.isEmpty());
    }

    @Test
    void testIsEmptyTrueWhenNoFieldsSupplied() {
        OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto = new OverseasEntityDueDiligenceDto();
        assertTrue(overseasEntityDueDiligenceDto.isEmpty());
    }

    @Test
    void testNoErrorReportedWhenCountryIsInTheUk() {
        overseasEntityDueDiligenceDto.getAddress().setCountry("Wales");
        Errors errors = overseasEntityDueDiligenceValidator.validate(overseasEntityDueDiligenceDto, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenCountryIsNotInTheUk() {
        final String invalidCountry = "France";
        overseasEntityDueDiligenceDto.getAddress().setCountry(invalidCountry);
        Errors errors = overseasEntityDueDiligenceValidator.validate(overseasEntityDueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = OverseasEntityDueDiligenceDto.IDENTITY_ADDRESS_FIELD + "." + AddressDto.COUNTRY_FIELD;
        String validationMessage = String.format(ValidationMessages.COUNTRY_NOT_ON_LIST_ERROR_MESSAGE, invalidCountry);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenIdentityDateFieldIsInTheFuture() {
        overseasEntityDueDiligenceDto.setIdentityDate(LocalDate.now().plusDays(1));
        Errors errors = overseasEntityDueDiligenceValidator.validate(overseasEntityDueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntityDueDiligenceDto.IDENTITY_DATE_FIELD);
        String validationMessage = ValidationMessages.DATE_NOT_IN_PAST_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(OverseasEntityDueDiligenceDto.IDENTITY_DATE_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenIdentityDateFieldIsNull() {
        overseasEntityDueDiligenceDto.setIdentityDate(null);
        Errors errors = overseasEntityDueDiligenceValidator.validate(overseasEntityDueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(DueDiligenceDto.IDENTITY_DATE_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(DueDiligenceDto.IDENTITY_DATE_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenIdentityDateFieldIsGreaterThan3MonthsInThePast() {
        overseasEntityDueDiligenceDto.setIdentityDate(LocalDate.of(2022, 3,20));
        Errors errors = overseasEntityDueDiligenceValidator.validate(overseasEntityDueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntityDueDiligenceDto.IDENTITY_DATE_FIELD);
        String validationMessage = ValidationMessages.DATE_NOT_WITHIN_PAST_3_MONTHS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(OverseasEntityDueDiligenceDto.IDENTITY_DATE_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNameFieldIsEmpty() {
        overseasEntityDueDiligenceDto.setName("  ");
        Errors errors = overseasEntityDueDiligenceValidator.validate(overseasEntityDueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntityDueDiligenceDto.NAME_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(OverseasEntityDueDiligenceDto.NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNameFieldIsNull() {
        overseasEntityDueDiligenceDto.setName(null);
        Errors errors = overseasEntityDueDiligenceValidator.validate(overseasEntityDueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntityDueDiligenceDto.NAME_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(OverseasEntityDueDiligenceDto.NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNameFieldExceedsMaxLength() {
        overseasEntityDueDiligenceDto.setName(StringUtils.repeat("A", 257));
        Errors errors = overseasEntityDueDiligenceValidator.validate(overseasEntityDueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntityDueDiligenceDto.NAME_FIELD);

        assertError(OverseasEntityDueDiligenceDto.NAME_FIELD, qualifiedFieldName + " must be 256 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenNameFieldContainsInvalidCharacters() {
        overseasEntityDueDiligenceDto.setName("Дракон");
        Errors errors = overseasEntityDueDiligenceValidator.validate(overseasEntityDueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntityDueDiligenceDto.NAME_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(OverseasEntityDueDiligenceDto.NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenEmailFieldIsEmpty() {
        overseasEntityDueDiligenceDto.setEmail("  ");
        Errors errors = overseasEntityDueDiligenceValidator.validate(overseasEntityDueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntityDueDiligenceDto.EMAIL_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(OverseasEntityDueDiligenceDto.EMAIL_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenEmailFieldIsNull() {
        overseasEntityDueDiligenceDto.setEmail(null);
        Errors errors = overseasEntityDueDiligenceValidator.validate(overseasEntityDueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntityDueDiligenceDto.EMAIL_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(OverseasEntityDueDiligenceDto.EMAIL_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenEmailFieldExceedsMaxLength() {
        overseasEntityDueDiligenceDto.setEmail(StringUtils.repeat("A", 257) + "@long.com");
        Errors errors = overseasEntityDueDiligenceValidator.validate(overseasEntityDueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntityDueDiligenceDto.EMAIL_FIELD);

        assertError(OverseasEntityDueDiligenceDto.EMAIL_FIELD, qualifiedFieldName + " must be 256 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenEmailFieldContainsInvalidCharacters() {
        overseasEntityDueDiligenceDto.setEmail("wrong.com");
        Errors errors = overseasEntityDueDiligenceValidator.validate(overseasEntityDueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntityDueDiligenceDto.EMAIL_FIELD);
        String validationMessage = ValidationMessages.INVALID_EMAIL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(OverseasEntityDueDiligenceDto.EMAIL_FIELD,  validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenSupervisoryNameFieldExceedsMaxLength() {
        overseasEntityDueDiligenceDto.setSupervisoryName(StringUtils.repeat("A", 257));
        Errors errors = overseasEntityDueDiligenceValidator.validate(overseasEntityDueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntityDueDiligenceDto.SUPERVISORY_NAME_FIELD);

        assertError(OverseasEntityDueDiligenceDto.SUPERVISORY_NAME_FIELD, qualifiedFieldName + " must be 256 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenSupervisoryNameFieldContainsInvalidCharacters() {
        overseasEntityDueDiligenceDto.setSupervisoryName("Дракон");
        Errors errors = overseasEntityDueDiligenceValidator.validate(overseasEntityDueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntityDueDiligenceDto.SUPERVISORY_NAME_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(OverseasEntityDueDiligenceDto.SUPERVISORY_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenPartnerNameFieldExceedsMaxLength() {
        overseasEntityDueDiligenceDto.setPartnerName(StringUtils.repeat("A", 257));
        Errors errors = overseasEntityDueDiligenceValidator.validate(overseasEntityDueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntityDueDiligenceDto.PARTNER_NAME_FIELD);

        assertError(OverseasEntityDueDiligenceDto.PARTNER_NAME_FIELD, qualifiedFieldName + " must be 256 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenPartnerNameFieldContainsInvalidCharacters() {
        overseasEntityDueDiligenceDto.setPartnerName("Дракон");
        Errors errors = overseasEntityDueDiligenceValidator.validate(overseasEntityDueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntityDueDiligenceDto.PARTNER_NAME_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(OverseasEntityDueDiligenceDto.PARTNER_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenAmlNumberFieldExceedsMaxLength() {
        overseasEntityDueDiligenceDto.setAmlNumber(StringUtils.repeat("A", 257));
        Errors errors = overseasEntityDueDiligenceValidator.validate(overseasEntityDueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntityDueDiligenceDto.AML_NUMBER_FIELD);

        assertError(OverseasEntityDueDiligenceDto.AML_NUMBER_FIELD, qualifiedFieldName + " must be 256 characters or less", errors);
    }


    private void assertError(String fieldName, String message, Errors errors) {
        String qualifiedFieldName = OVERSEAS_ENTITY_DUE_DILIGENCE + "." + fieldName;
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(message).build();
        assertTrue(errors.containsError(err));
    }

    private String getQualifiedFieldName(String field) {
        return OVERSEAS_ENTITY_DUE_DILIGENCE + "." + field;
    }

}
