package uk.gov.companieshouse.overseasentitiesapi.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.companieshouse.overseasentitiesapi.validation.TrustIndividualValidator.PARENT_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.gov.companieshouse.overseasentitiesapi.mocks.TrustMock;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.BeneficialOwnerType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustDataDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

@ExtendWith(MockitoExtension.class)
class TrustIndividualValidatorTest {

    private static final String LOGGING_CONTEXT = "12345";

    @Mock
    private AddressDtoValidator addressDtoValidator;
    @Mock
    private NationalityValidator nationalityValidator;

    private TrustIndividualValidator trustIndividualValidator;
    private List<TrustDataDto> trustDataDtoList;

    @BeforeEach
    public void init() {
        trustIndividualValidator = new TrustIndividualValidator(addressDtoValidator, nationalityValidator);
        trustDataDtoList = new ArrayList<>();
        trustDataDtoList.add(TrustMock.getTrustDataDto());
    }

    @Test
    void testErrorReportedWhenForenameFieldIsNull() {
        trustDataDtoList.get(0).getIndividuals().get(0).setForename(null);
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.FORENAME_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenForenameFieldIsEmpty() {
        trustDataDtoList.get(0).getIndividuals().get(0).setForename(" ");
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.FORENAME_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenForenameFieldIsNotEmpty() {
        trustDataDtoList.get(0).getIndividuals().get(0).setForename("TESTY");
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenForenameFieldExceedsMaxLength() {
        trustDataDtoList.get(0).getIndividuals().get(0).setForename(StringUtils.repeat("A", 161));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.FORENAME_FIELD);
        String validationMessage = qualifiedFieldName + " must be 160 characters or less";

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenForenameFieldContainsInvalidCharacters() {
        trustDataDtoList.get(0).getIndividuals().get(0).setForename("Дракон");
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.FORENAME_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s",
                qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenSurnameFieldIsNull() {
        trustDataDtoList.get(0).getIndividuals().get(0).setSurname(null);
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.SURNAME_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenSurnameFieldIsEmpty() {
        trustDataDtoList.get(0).getIndividuals().get(0).setSurname(" ");
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.SURNAME_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenSurnameFieldIsNotEmpty() {
        trustDataDtoList.get(0).getIndividuals().get(0).setSurname("TESTY");
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenSurnameFieldExceedsMaxLength() {
        trustDataDtoList.get(0).getIndividuals().get(0).setSurname(StringUtils.repeat("A", 161));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.SURNAME_FIELD);
        String validationMessage = qualifiedFieldName + " must be 160 characters or less";

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenSurnameFieldContainsInvalidCharacters() {
        trustDataDtoList.get(0).getIndividuals().get(0).setSurname("Дракон");
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.SURNAME_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s",
                qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenBirthDateFieldIsNull() {
        trustDataDtoList.get(0).getIndividuals().get(0).setDateOfBirth(null);
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.DATE_OF_BIRTH_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenBirthDateFieldIsInThePast() {
        trustDataDtoList.get(0).getIndividuals().get(0).setDateOfBirth(LocalDate.of(1970, 1, 1));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenBirthDateIsInTheFuture() {
        trustDataDtoList.get(0).getIndividuals().get(0).setDateOfBirth(LocalDate.now().plusDays(1));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.DATE_OF_BIRTH_FIELD);
        String validationMessage = ValidationMessages.DATE_NOT_IN_PAST_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenTypeFieldIsNull() {
        trustDataDtoList.get(0).getIndividuals().get(0).setType(null);
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.TYPE_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenTypeFieldIsEmpty() {
        trustDataDtoList.get(0).getIndividuals().get(0).setType(" ");
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.TYPE_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenTypeFieldValueIsNotExpected() {
        trustDataDtoList.get(0).getIndividuals().get(0).setType("TESTY");
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.TYPE_FIELD);
        String validationMessage = ValidationMessages.TRUST_INDIVIDUAL_TYPE_ERROR_MESSAGE.replace("%s",
                qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenTypeFieldVlaueIsExpected() {
        trustDataDtoList.get(0).getIndividuals().get(0).setType(BeneficialOwnerType.BENEFICIARY.getValue());
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenDateBecomeInterestedPersonFieldIsNull() {
        trustDataDtoList.get(0).getIndividuals().get(0).setDateBecameInterestedPerson(null);
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                TrustIndividualDto.DATE_BECAME_INTERESTED_PERSON_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenDateBecomeInterestedPersonFieldIsInThePast() {
        trustDataDtoList.get(0).getIndividuals().get(0).setDateBecameInterestedPerson(LocalDate.of(1970, 1, 1));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenDateBecomeInterestedPersonIsInTheFuture() {
        trustDataDtoList.get(0).getIndividuals().get(0).setDateBecameInterestedPerson(LocalDate.now().plusDays(1));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                TrustIndividualDto.DATE_BECAME_INTERESTED_PERSON_FIELD);
        String validationMessage = ValidationMessages.DATE_NOT_IN_PAST_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenServiceAddressSameAsUsualResidentialAddressFieldIsNull() {
        trustDataDtoList.get(0).getIndividuals().get(0).setServiceAddressSameAsUsualResidentialAddress(null);
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.FORENAME_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    private void assertError(String qualifiedFieldName, String message, Errors errors) {
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(message).build();
        assertTrue(errors.containsError(err));
    }
}
