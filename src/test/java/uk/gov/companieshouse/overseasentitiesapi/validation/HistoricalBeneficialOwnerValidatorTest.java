package uk.gov.companieshouse.overseasentitiesapi.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.companieshouse.overseasentitiesapi.validation.HistoricalBeneficialOwnerValidator.PARENT_FIELD;
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
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.HistoricalBeneficialOwnerDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustDataDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

@ExtendWith(MockitoExtension.class)
class HistoricalBeneficialOwnerValidatorTest {

    private static final String LOGGING_CONTEXT = "12345";

    private HistoricalBeneficialOwnerValidator historicalBeneficialOwnerValidator;
    private List<TrustDataDto> individualTrustDataDtoList;
    private List<TrustDataDto> corporateTrustDataDtoList;

    @BeforeEach
    public void init() {
        historicalBeneficialOwnerValidator = new HistoricalBeneficialOwnerValidator();

        individualTrustDataDtoList = new ArrayList<>();
        individualTrustDataDtoList.add(TrustMock.getTrustDataDto());
        individualTrustDataDtoList.get(0).getHistoricalBeneficialOwners().remove(1);

        corporateTrustDataDtoList = new ArrayList<>();
        corporateTrustDataDtoList.add(TrustMock.getTrustDataDto());
        corporateTrustDataDtoList.get(0).getHistoricalBeneficialOwners().remove(0);
    }

    @Test
    void testErrorReportedWhenForenameFieldIsNull() {
        individualTrustDataDtoList.get(0).getHistoricalBeneficialOwners().get(0).setForename(null);
        Errors errors = historicalBeneficialOwnerValidator.validate(individualTrustDataDtoList, new Errors(),
                LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                HistoricalBeneficialOwnerDto.FORENAME_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenForenameFieldIsEmpty() {
        individualTrustDataDtoList.get(0).getHistoricalBeneficialOwners().get(0).setForename(" ");
        Errors errors = historicalBeneficialOwnerValidator.validate(individualTrustDataDtoList, new Errors(),
                LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                HistoricalBeneficialOwnerDto.FORENAME_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenForenameFieldIsNotEmpty() {
        individualTrustDataDtoList.get(0).getHistoricalBeneficialOwners().get(0).setForename("TESTY OVERSEAS ENTITY");
        Errors errors = historicalBeneficialOwnerValidator.validate(individualTrustDataDtoList, new Errors(),
                LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenForenameFieldExceedsMaxLength() {
        individualTrustDataDtoList.get(0).getHistoricalBeneficialOwners().get(0)
                .setForename(StringUtils.repeat("A", 161));
        Errors errors = historicalBeneficialOwnerValidator.validate(individualTrustDataDtoList, new Errors(),
                LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                HistoricalBeneficialOwnerDto.FORENAME_FIELD);
        String validationMessage = qualifiedFieldName + " must be 160 characters or less";

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenForenameFieldContainsInvalidCharacters() {
        individualTrustDataDtoList.get(0).getHistoricalBeneficialOwners().get(0).setForename("Дракон");
        Errors errors = historicalBeneficialOwnerValidator.validate(individualTrustDataDtoList, new Errors(),
                LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                HistoricalBeneficialOwnerDto.FORENAME_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s",
                qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenSurnameFieldIsNull() {
        individualTrustDataDtoList.get(0).getHistoricalBeneficialOwners().get(0).setSurname(null);
        Errors errors = historicalBeneficialOwnerValidator.validate(individualTrustDataDtoList, new Errors(),
                LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                HistoricalBeneficialOwnerDto.SURNAME_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenSurnameFieldIsEmpty() {
        individualTrustDataDtoList.get(0).getHistoricalBeneficialOwners().get(0).setSurname(" ");
        Errors errors = historicalBeneficialOwnerValidator.validate(individualTrustDataDtoList, new Errors(),
                LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                HistoricalBeneficialOwnerDto.SURNAME_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenSurnameFieldIsNotEmpty() {
        individualTrustDataDtoList.get(0).getHistoricalBeneficialOwners().get(0).setSurname("TESTY OVERSEAS ENTITY");
        Errors errors = historicalBeneficialOwnerValidator.validate(individualTrustDataDtoList, new Errors(),
                LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenSurnameFieldExceedsMaxLength() {
        individualTrustDataDtoList.get(0).getHistoricalBeneficialOwners().get(0)
                .setSurname(StringUtils.repeat("A", 161));
        Errors errors = historicalBeneficialOwnerValidator.validate(individualTrustDataDtoList, new Errors(),
                LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                HistoricalBeneficialOwnerDto.SURNAME_FIELD);
        String validationMessage = qualifiedFieldName + " must be 160 characters or less";

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenSurnameFieldContainsInvalidCharacters() {
        individualTrustDataDtoList.get(0).getHistoricalBeneficialOwners().get(0).setSurname("Дракон");
        Errors errors = historicalBeneficialOwnerValidator.validate(individualTrustDataDtoList, new Errors(),
                LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                HistoricalBeneficialOwnerDto.SURNAME_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s",
                qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenCeasedDateFieldIsNull() {
        individualTrustDataDtoList.get(0).getHistoricalBeneficialOwners().get(0).setCeasedDate(null);
        Errors errors = historicalBeneficialOwnerValidator.validate(individualTrustDataDtoList, new Errors(),
                LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                HistoricalBeneficialOwnerDto.CEASED_DATE_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenCeasedDateFieldIsInThePast() {
        individualTrustDataDtoList.get(0).getHistoricalBeneficialOwners().get(0)
                .setCeasedDate(LocalDate.of(1970, 1, 1));
        Errors errors = historicalBeneficialOwnerValidator.validate(individualTrustDataDtoList, new Errors(),
                LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenCeasedDateIsInTheFuture() {
        individualTrustDataDtoList.get(0).getHistoricalBeneficialOwners().get(0)
                .setCeasedDate(LocalDate.now().plusDays(1));
        Errors errors = historicalBeneficialOwnerValidator.validate(individualTrustDataDtoList, new Errors(),
                LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                HistoricalBeneficialOwnerDto.CEASED_DATE_FIELD);
        String validationMessage = ValidationMessages.DATE_NOT_IN_PAST_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNotifiedDateFieldIsNull() {
        individualTrustDataDtoList.get(0).getHistoricalBeneficialOwners().get(0).setNotifiedDate(null);
        Errors errors = historicalBeneficialOwnerValidator.validate(individualTrustDataDtoList, new Errors(),
                LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                HistoricalBeneficialOwnerDto.NOTIFIED_DATE_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenNotifiedDateFieldIsInThePast() {
        individualTrustDataDtoList.get(0).getHistoricalBeneficialOwners().get(0)
                .setNotifiedDate(LocalDate.of(1970, 1, 1));
        Errors errors = historicalBeneficialOwnerValidator.validate(individualTrustDataDtoList, new Errors(),
                LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenNotifiedDateIsInTheFuture() {
        individualTrustDataDtoList.get(0).getHistoricalBeneficialOwners().get(0)
                .setNotifiedDate(LocalDate.now().plusDays(1));
        Errors errors = historicalBeneficialOwnerValidator.validate(individualTrustDataDtoList, new Errors(),
                LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                HistoricalBeneficialOwnerDto.NOTIFIED_DATE_FIELD);
        String validationMessage = ValidationMessages.DATE_NOT_IN_PAST_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenCorporatenameFieldIsNull() {
        corporateTrustDataDtoList.get(0).getHistoricalBeneficialOwners().get(0).setCorporateName(null);
        Errors errors = historicalBeneficialOwnerValidator.validate(corporateTrustDataDtoList, new Errors(),
                LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                HistoricalBeneficialOwnerDto.CORPORATE_NAME_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenCorporateNameFieldIsEmpty() {
        corporateTrustDataDtoList.get(0).getHistoricalBeneficialOwners().get(0).setCorporateName(" ");
        Errors errors = historicalBeneficialOwnerValidator.validate(corporateTrustDataDtoList, new Errors(),
                LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                HistoricalBeneficialOwnerDto.CORPORATE_NAME_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenCorporateNameFieldIsNotEmpty() {
        corporateTrustDataDtoList.get(0).getHistoricalBeneficialOwners().get(0)
                .setCorporateName("TESTY OVERSEAS ENTITY CORPORATE NAME");
        Errors errors = historicalBeneficialOwnerValidator.validate(corporateTrustDataDtoList, new Errors(),
                LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenCorporateNameFieldExceedsMaxLength() {
        corporateTrustDataDtoList.get(0).getHistoricalBeneficialOwners().get(0)
                .setCorporateName(StringUtils.repeat("A", 161));
        Errors errors = historicalBeneficialOwnerValidator.validate(corporateTrustDataDtoList, new Errors(),
                LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                HistoricalBeneficialOwnerDto.CORPORATE_NAME_FIELD);
        String validationMessage = qualifiedFieldName + " must be 160 characters or less";

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void atestErrorReportedWhenSurnameFieldContainsInvalidCharacters() {
        corporateTrustDataDtoList.get(0).getHistoricalBeneficialOwners().get(0).setCorporateName("Дракон");
        Errors errors = historicalBeneficialOwnerValidator.validate(corporateTrustDataDtoList, new Errors(),
                LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                HistoricalBeneficialOwnerDto.CORPORATE_NAME_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s",
                qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    private void assertError(String qualifiedFieldName, String message, Errors errors) {
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(message).build();
        assertTrue(errors.containsError(err));
    }
}
