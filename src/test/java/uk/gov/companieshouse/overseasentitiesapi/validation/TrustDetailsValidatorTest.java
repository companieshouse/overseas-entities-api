package uk.gov.companieshouse.overseasentitiesapi.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.gov.companieshouse.overseasentitiesapi.mocks.TrustMock;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustDataDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

@ExtendWith(MockitoExtension.class)
class TrustDetailsValidatorTest {

    private static final String LOGGING_CONTEXT = "12345";

    private TrustDetailsValidator trustDetailsValidator;
    private List<TrustDataDto> trustDataDtoList;

    @BeforeEach
    public void init() {
      trustDetailsValidator = new TrustDetailsValidator();

      trustDataDtoList = new ArrayList<>();
      trustDataDtoList.add(TrustMock.getTrustDataDto());
    }

    @Test
    void testNoValidationErrorReportedWhenTrustIdFieldIsNull() {
        trustDataDtoList.get(0).setTrustId(null);
        Errors errors = trustDetailsValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA, TrustDataDto.TRUST_ID_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoValidationErrorReportedWhenTrustIdFieldIsEmpty() {
        trustDataDtoList.get(0).setTrustId("");
        Errors errors = trustDetailsValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA, TrustDataDto.TRUST_ID_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoValidationErrorReportedWhenTrustIdFieldIsDuplicated() {
        trustDataDtoList.add(TrustMock.getTrustDataDto());
        Errors errors = trustDetailsValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA, TrustDataDto.TRUST_ID_FIELD);
        String validationMessage = ValidationMessages.DUPLICATE_TRUST_ID.replace("%s", "Trust Name");

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoValidationErrorReportedWhenTrustNameFieldIsNull() {
        trustDataDtoList.get(0).setTrustName(null);
        Errors errors = trustDetailsValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA, TrustDataDto.TRUST_NAME_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoValidationErrorReportedWhenTrustNameFieldIsEmpty() {
        trustDataDtoList.get(0).setTrustName(" ");
        Errors errors = trustDetailsValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA, TrustDataDto.TRUST_NAME_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoValidationErrorReportedWhenTrustNameFieldIsNotEmpty() {
        trustDataDtoList.get(0).setTrustName("TESTY OVERSEAS ENTITY");
        Errors errors = trustDetailsValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testValidationErrorReportedWhenTrustNameFieldExceedsMaxLength() {
        trustDataDtoList.get(0).setTrustName(StringUtils.repeat("A", 161));
        Errors errors = trustDetailsValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA, TrustDataDto.TRUST_NAME_FIELD);
        String validationMessage = qualifiedFieldName + " must be 160 characters or less";

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testValidationErrorReportedWhenTrustNameFieldContainsInvalidCharacters() {
        trustDataDtoList.get(0).setTrustName("Дракон");
        Errors errors = trustDetailsValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA, TrustDataDto.TRUST_NAME_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenCreationDateFieldIsNull() {
        trustDataDtoList.get(0).setCreationDate(null);
        Errors errors = trustDetailsValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA, TrustDataDto.CREATION_DATE_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenCreationDateFieldIsInThePast() {
        trustDataDtoList.get(0).setCreationDate(LocalDate.of(1970,1, 1));
        Errors errors = trustDetailsValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenCreationDateIsInTheFuture() {
        trustDataDtoList.get(0).setCreationDate(LocalDate.now().plusDays(1));
        Errors errors = trustDetailsValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA, TrustDataDto.CREATION_DATE_FIELD);
        String validationMessage = ValidationMessages.DATE_NOT_IN_PAST_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenUnableToObtainAllTrustInfoFieldIsNull() {
        trustDataDtoList.get(0).setUnableToObtainAllTrustInfo(null);
        Errors errors = trustDetailsValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA, TrustDataDto.UNABLE_TO_OBTAIN_ALL_TRUST_INFO);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenUnableToObtainAllTrustInfoFieldNotEmpty() {
        trustDataDtoList.get(0).setUnableToObtainAllTrustInfo(false);
        Errors errors = trustDetailsValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
    }

    private void assertError(String qualifiedFieldName, String message, Errors errors) {
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(message).build();
        assertTrue(errors.containsError(err));
    }


}
