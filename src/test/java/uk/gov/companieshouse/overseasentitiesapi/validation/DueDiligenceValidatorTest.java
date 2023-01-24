package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.overseasentitiesapi.mocks.DueDiligenceMock;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.DueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.utils.DataSanitisation;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.DUE_DILIGENCE_FIELD;

@ExtendWith(MockitoExtension.class)
class DueDiligenceValidatorTest {
    private static final String LOGGING_CONTEXT = "12345";
    private DueDiligenceValidator dueDiligenceValidator;
    @InjectMocks
    private AddressDtoValidator addressDtoValidator;
    @Mock
    private DataSanitisation dataSanitisation;
    private DueDiligenceDto dueDiligenceDto;

    @BeforeEach
    void init() {
        dueDiligenceValidator = new DueDiligenceValidator(addressDtoValidator);
        dueDiligenceDto = DueDiligenceMock.getDueDiligenceDto();
        dueDiligenceDto.getAddress().setCountry("England");
        dueDiligenceDto.setIdentityDate(LocalDate.now().minusMonths(1));
    }

    @Test
    void testNoErrorReportedWhenIdentityDateFieldIsNow() {
        Errors errors = dueDiligenceValidator.validate(dueDiligenceDto, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testIsEmptyFalseWhenAllFieldsSupplied() {
        assertFalse(dueDiligenceDto.isEmpty());
    }

    @Test
    void testIsEmptyFalseWhenOneFieldSupplied() {
        DueDiligenceDto dueDiligenceDto = new DueDiligenceDto();
        dueDiligenceDto.setName("Test");
        assertFalse(dueDiligenceDto.isEmpty());
    }

    @Test
    void testIsEmptyTrueWhenNoFieldsSupplied() {
        DueDiligenceDto dueDiligenceDto = new DueDiligenceDto();
        assertTrue(dueDiligenceDto.isEmpty());
    }

    @Test
    void testNoErrorReportedWhenCountryIsInTheUk() {
        dueDiligenceDto.getAddress().setCountry("Wales");
        Errors errors = dueDiligenceValidator.validate(dueDiligenceDto, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenCountryIsNotInTheUk() {
        final String invalidCountry = "France";
        dueDiligenceDto.getAddress().setCountry(invalidCountry);
        when(dataSanitisation.makeStringSafeForLogging(invalidCountry)).thenReturn(invalidCountry);
        Errors errors = dueDiligenceValidator.validate(dueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = DueDiligenceDto.IDENTITY_ADDRESS_FIELD + "." + AddressDto.COUNTRY_FIELD;
        String validationMessage = String.format(ValidationMessages.COUNTRY_NOT_ON_LIST_ERROR_MESSAGE, invalidCountry);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenIdentityDateFieldIsInTheFuture() {
        dueDiligenceDto.setIdentityDate(LocalDate.now().plusDays(1));
        Errors errors = dueDiligenceValidator.validate(dueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(DueDiligenceDto.IDENTITY_DATE_FIELD);
        String validationMessage = ValidationMessages.DATE_NOT_IN_PAST_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(DueDiligenceDto.IDENTITY_DATE_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenIdentityDateFieldIsNull() {
        dueDiligenceDto.setIdentityDate(null);
        Errors errors = dueDiligenceValidator.validate(dueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(DueDiligenceDto.IDENTITY_DATE_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(DueDiligenceDto.IDENTITY_DATE_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenIdentityDateFieldIsGreaterThan3MonthsInThePast() {
        dueDiligenceDto.setIdentityDate(LocalDate.of(2022, 3,20));
        Errors errors = dueDiligenceValidator.validate(dueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(DueDiligenceDto.IDENTITY_DATE_FIELD);
        String validationMessage = ValidationMessages.DATE_NOT_WITHIN_PAST_3_MONTHS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(DueDiligenceDto.IDENTITY_DATE_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNameFieldIsEmpty() {
        dueDiligenceDto.setName("  ");
        Errors errors = dueDiligenceValidator.validate(dueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(DueDiligenceDto.NAME_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(DueDiligenceDto.NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNameFieldIsNull() {
        dueDiligenceDto.setName(null);
        Errors errors = dueDiligenceValidator.validate(dueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(DueDiligenceDto.NAME_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(DueDiligenceDto.NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNameFieldExceedsMaxLength() {
        dueDiligenceDto.setName(StringUtils.repeat("A", 257));
        Errors errors = dueDiligenceValidator.validate(dueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(DueDiligenceDto.NAME_FIELD);

        assertError(DueDiligenceDto.NAME_FIELD, qualifiedFieldName + " must be 256 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenNameFieldContainsInvalidCharacters() {
        dueDiligenceDto.setName("Дракон");
        Errors errors = dueDiligenceValidator.validate(dueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(DueDiligenceDto.NAME_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(DueDiligenceDto.NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenEmailFieldIsEmpty() {
        dueDiligenceDto.setEmail("  ");
        Errors errors = dueDiligenceValidator.validate(dueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(DueDiligenceDto.EMAIL_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(DueDiligenceDto.EMAIL_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenEmailFieldIsNull() {
        dueDiligenceDto.setEmail(null);
        Errors errors = dueDiligenceValidator.validate(dueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(DueDiligenceDto.EMAIL_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(DueDiligenceDto.EMAIL_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenEmailFieldExceedsMaxLength() {
        dueDiligenceDto.setEmail(StringUtils.repeat("A", 257) + "@long.com");
        Errors errors = dueDiligenceValidator.validate(dueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(DueDiligenceDto.EMAIL_FIELD);

        assertError(DueDiligenceDto.EMAIL_FIELD, qualifiedFieldName + " must be 256 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenEmailFieldContainsInvalidCharacters() {
        dueDiligenceDto.setEmail("wrong.com");
        Errors errors = dueDiligenceValidator.validate(dueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(DueDiligenceDto.EMAIL_FIELD);
        String validationMessage = ValidationMessages.INVALID_EMAIL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(DueDiligenceDto.EMAIL_FIELD,  validationMessage, errors);
    }

    @ParameterizedTest
    @ValueSource(strings = {"vsocarroll@QQQQQQQT123465798U123456789V123456789W123456789X123456789Y123456.companieshouse.gov.uk",
            "socarrollA123456789B132456798C123456798D123456789@T123465798U123456789V123456789W123456789X123456789Y123456.companieshouse.gov.uk",
            "socarrollA123456789B132456798C123456798D123456789E123456789F123XX@T123465798U123456789V123456789W123456789X123456789Y123456.companieshouse.gov.uk"})
    void testNoErrorReportedWithLongEmailAddress(String email) {
        dueDiligenceDto.setEmail(email);
        Errors errors = dueDiligenceValidator.validate(dueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenSupervisoryNameFieldIsNull() {
        dueDiligenceDto.setSupervisoryName(null);
        Errors errors = dueDiligenceValidator.validate(dueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(DueDiligenceDto.SUPERVISORY_NAME_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(DueDiligenceDto.SUPERVISORY_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenSupervisoryNameFieldExceedsMaxLength() {
        dueDiligenceDto.setSupervisoryName(StringUtils.repeat("A", 257));
        Errors errors = dueDiligenceValidator.validate(dueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(DueDiligenceDto.SUPERVISORY_NAME_FIELD);

        assertError(DueDiligenceDto.SUPERVISORY_NAME_FIELD, qualifiedFieldName + " must be 256 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenSupervisoryNameFieldContainsInvalidCharacters() {
        dueDiligenceDto.setSupervisoryName("Дракон");
        Errors errors = dueDiligenceValidator.validate(dueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(DueDiligenceDto.SUPERVISORY_NAME_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(DueDiligenceDto.SUPERVISORY_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenAmlNumberFieldIsNull() {
        dueDiligenceDto.setAmlNumber(null);
        Errors errors = dueDiligenceValidator.validate(dueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenAmlNumberFieldExceedsMaxLength() {
        dueDiligenceDto.setAmlNumber(StringUtils.repeat("A", 257));
        Errors errors = dueDiligenceValidator.validate(dueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(DueDiligenceDto.AML_NUMBER_FIELD);

        assertError(DueDiligenceDto.AML_NUMBER_FIELD, qualifiedFieldName + " must be 256 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenAmlNumberFieldContainsInvalidCharacters() {
        dueDiligenceDto.setAmlNumber("Дракон");
        Errors errors = dueDiligenceValidator.validate(dueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(DueDiligenceDto.AML_NUMBER_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(DueDiligenceDto.AML_NUMBER_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenAgentCodeFieldIsNull() {
        dueDiligenceDto.setAgentCode(null);
        Errors errors = dueDiligenceValidator.validate(dueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(DueDiligenceDto.AGENT_CODE_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(DueDiligenceDto.AGENT_CODE_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenAgentCodeFieldContainsInvalidCharacters() {
        dueDiligenceDto.setAgentCode("Дракон");
        Errors errors = dueDiligenceValidator.validate(dueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(DueDiligenceDto.AGENT_CODE_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(DueDiligenceDto.AGENT_CODE_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenAgentCodeFieldExceedsMaxLength() {
        dueDiligenceDto.setAgentCode(StringUtils.repeat("A", 257));
        Errors errors = dueDiligenceValidator.validate(dueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(DueDiligenceDto.AGENT_CODE_FIELD);

        assertError(DueDiligenceDto.AGENT_CODE_FIELD, qualifiedFieldName + " must be 256 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenPartnerNameFieldIsNull() {
        dueDiligenceDto.setPartnerName(null);
        Errors errors = dueDiligenceValidator.validate(dueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(DueDiligenceDto.PARTNER_NAME_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(DueDiligenceDto.PARTNER_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenPartnerNameFieldExceedsMaxLength() {
        dueDiligenceDto.setPartnerName(StringUtils.repeat("A", 257));
        Errors errors = dueDiligenceValidator.validate(dueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(DueDiligenceDto.PARTNER_NAME_FIELD);

        assertError(DueDiligenceDto.PARTNER_NAME_FIELD, qualifiedFieldName + " must be 256 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenPartnerNameFieldContainsInvalidCharacters() {
        dueDiligenceDto.setPartnerName("Дракон");
        Errors errors = dueDiligenceValidator.validate(dueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(DueDiligenceDto.PARTNER_NAME_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(DueDiligenceDto.PARTNER_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenDiligenceFieldIsNull() {
        dueDiligenceDto.setDiligence(null);
        Errors errors = dueDiligenceValidator.validate(dueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(DueDiligenceDto.DILIGENCE_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(DueDiligenceDto.DILIGENCE_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenDiligenceFieldIsNotAgree() {
        dueDiligenceDto.setDiligence("not agree");
        Errors errors = dueDiligenceValidator.validate(dueDiligenceDto, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(DueDiligenceDto.DILIGENCE_FIELD);
        String validationMessage = ValidationMessages.SHOULD_BE_AGREE_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(DueDiligenceDto.DILIGENCE_FIELD, validationMessage, errors);
    }

    private void assertError(String fieldName, String message, Errors errors) {
        String qualifiedFieldName = DUE_DILIGENCE_FIELD + "." + fieldName;
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(message).build();
        assertTrue(errors.containsError(err));
    }

    private String getQualifiedFieldName(String field) {
        return DUE_DILIGENCE_FIELD + "." + field;
    }

}

