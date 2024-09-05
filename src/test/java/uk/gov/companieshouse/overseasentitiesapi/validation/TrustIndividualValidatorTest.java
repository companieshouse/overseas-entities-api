package uk.gov.companieshouse.overseasentitiesapi.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.gov.companieshouse.overseasentitiesapi.mocks.TrustMock;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.BeneficialOwnerType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustDataDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.any;

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
        trustDataDtoList.getFirst().getIndividuals().getFirst().setForename(null);
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.FORENAME_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenForenameFieldIsEmpty() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setForename(" ");
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.FORENAME_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenForenameFieldIsNotEmpty() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setForename("TESTY");
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenForenameFieldExceedsMaxLength() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setForename(StringUtils.repeat("A", 161));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.FORENAME_FIELD);
        String validationMessage = qualifiedFieldName + " must be 160 characters or less";

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenForenameFieldContainsInvalidCharacters() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setForename("Дракон");
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.FORENAME_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s",
                qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenSurnameFieldIsNull() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setSurname(null);
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.SURNAME_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenSurnameFieldIsEmpty() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setSurname(" ");
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.SURNAME_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenSurnameFieldIsNotEmpty() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setSurname("TESTY");
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenSurnameFieldExceedsMaxLength() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setSurname(StringUtils.repeat("A", 161));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.SURNAME_FIELD);
        String validationMessage = qualifiedFieldName + " must be 160 characters or less";

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenSurnameFieldContainsInvalidCharacters() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setSurname("Дракон");
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.SURNAME_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s",
                qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenBirthDateFieldIsNull() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setDateOfBirth(null);
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.DATE_OF_BIRTH_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenBirthDateFieldIsInThePast() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setDateOfBirth(LocalDate.of(1970, 1, 1));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenBirthDateIsInTheFuture() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setDateOfBirth(LocalDate.now().plusDays(1));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.DATE_OF_BIRTH_FIELD);
        String validationMessage = ValidationMessages.DATE_NOT_IN_PAST_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenTypeFieldIsNull() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setType(null);
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.TYPE_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenTypeFieldIsEmpty() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setType(" ");
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.TYPE_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenTypeFieldValueIsNotExpected() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setType("TESTY");
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.TYPE_FIELD);
        String validationMessage = ValidationMessages.TRUST_INDIVIDUAL_TYPE_ERROR_MESSAGE.replace("%s",
                qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenTypeFieldValueIsExpected() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setType(BeneficialOwnerType.BENEFICIARY.getValue());
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenDateBecomeInterestedPersonFieldIsNull() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setType(BeneficialOwnerType.INTERESTED_PERSON.getValue());
        trustDataDtoList.getFirst().getIndividuals().getFirst().setDateBecameInterestedPerson(null);
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                TrustIndividualDto.DATE_BECAME_INTERESTED_PERSON_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenTypeFieldIsNotInterestedPersonAndDateBecomeInterestedPersonFieldIsNull() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setType(BeneficialOwnerType.BENEFICIARY.getValue());
        trustDataDtoList.getFirst().getIndividuals().getFirst().setDateBecameInterestedPerson(null);
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT,false);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenDateBecomeInterestedPersonFieldIsInThePast() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setType(BeneficialOwnerType.INTERESTED_PERSON.getValue());
        trustDataDtoList.getFirst().getIndividuals().getFirst().setDateBecameInterestedPerson(LocalDate.of(1970, 1, 1));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenDateBecomeInterestedPersonIsInTheFuture() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setType(BeneficialOwnerType.INTERESTED_PERSON.getValue());
        trustDataDtoList.getFirst().getIndividuals().getFirst().setDateBecameInterestedPerson(LocalDate.now().plusDays(1));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                TrustIndividualDto.DATE_BECAME_INTERESTED_PERSON_FIELD);
        String validationMessage = ValidationMessages.DATE_NOT_IN_PAST_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @ParameterizedTest
    @ValueSource(strings = {"true", "false"})
    void testErrorReportedWhenServiceAddressSameAsUsualResidentialAddressFieldIsNull(boolean isUpdateOrRemove) {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setServiceAddressSameAsUsualResidentialAddress(null);
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, isUpdateOrRemove);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.IS_SERVICE_ADDRESS_SAME_AS_USUAL_RESIDENTIAL_ADDRESS_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @ParameterizedTest
    @ValueSource(strings = {"true", "false"})
    void testErrorReportedWhenServiceAddressSameAsUsualResidentialAddressFieldIsTrueButServiceAddressIsNull(boolean isUpdateOrRemove) {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setServiceAddressSameAsUsualResidentialAddress(true);
        trustDataDtoList.getFirst().getIndividuals().getFirst().setServiceAddress(null);
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, isUpdateOrRemove);

        assertFalse(errors.hasErrors());
    }

    @ParameterizedTest
    @ValueSource(strings = {"true", "false"})
    void testNoErrorReportedWhenServiceAddressSameAsUsualResidentialAddressFields(boolean isUpdateOrRemove) {
        var trustee = trustDataDtoList.getFirst().getIndividuals().getFirst();
        trustee.setServiceAddressSameAsUsualResidentialAddress(true);
        trustee.setUraAddressPremises("1");
        trustee.setUraAddressLine1("Broadway");
        trustee.setUraAddressLocality("New York");
        trustee.setUraAddressCountry("USA");

        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, isUpdateOrRemove);

        assertFalse(errors.hasErrors());
        assertEquals("1", trustee.getServiceAddress().getPropertyNameNumber());
        assertEquals("Broadway", trustee.getServiceAddress().getLine1());
        assertEquals("New York", trustee.getServiceAddress().getTown());
        assertEquals("USA", trustee.getServiceAddress().getCountry());
    }
    
    @Test
    void testErrorReportedWhenSecondNationalitySameAsFirstNationality() {

        String qualifiedFieldNameFirstNationality = getQualifiedFieldName(PARENT_FIELD,
                TrustIndividualDto.NATIONALITY_FIELD);
        String qualifiedFieldNameForSecondNationality = getQualifiedFieldName(PARENT_FIELD,
                TrustIndividualDto.SECOND_NATIONALITY_FIELD);

        when(nationalityValidator.validateSecondNationality(eq(qualifiedFieldNameFirstNationality),
                eq(qualifiedFieldNameForSecondNationality), eq("Afghan"), eq("Afghan"), any(Errors.class),
                eq(LOGGING_CONTEXT))).thenCallRealMethod();

        trustDataDtoList.getFirst().getIndividuals().getFirst().setNationality("Afghan");
        trustDataDtoList.getFirst().getIndividuals().getFirst().setSecondNationality("Afghan");
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        String validationMessage = ValidationMessages.SECOND_NATIONALITY_SHOULD_BE_DIFFERENT.replace("%s", qualifiedFieldNameForSecondNationality);

        assertError(qualifiedFieldNameForSecondNationality, validationMessage, errors);
    }

    @Test
    void testErrorNotReportedWhenSecondNationalityDifferentToFirstNationality() {

        String qualifiedFieldNameFirstNationality = getQualifiedFieldName(PARENT_FIELD,
                TrustIndividualDto.NATIONALITY_FIELD);
        String qualifiedFieldNameForSecondNationality = getQualifiedFieldName(PARENT_FIELD,
                TrustIndividualDto.SECOND_NATIONALITY_FIELD);

        when(nationalityValidator.validateSecondNationality(eq(qualifiedFieldNameFirstNationality),
                eq(qualifiedFieldNameForSecondNationality), eq("Afghan"), eq("Spain"), any(Errors.class),
                eq(LOGGING_CONTEXT))).thenCallRealMethod();

        trustDataDtoList.getFirst().getIndividuals().getFirst().setNationality("Afghan");
        trustDataDtoList.getFirst().getIndividuals().getFirst().setSecondNationality("Spain");
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorIsStillInvolvedTrueAndCeasedDateNull() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setIndividualStillInvolvedInTrust(true);
        trustDataDtoList.getFirst().getIndividuals().getFirst().setCeasedDate(null);
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorIsStillInvolvedFalseAndCeasedDateNotNull() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setIndividualStillInvolvedInTrust(false);
        trustDataDtoList.getFirst().getIndividuals().getFirst().setCeasedDate(LocalDate.of(2001, 1, 1));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorIsStillInvolvedNull() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setIndividualStillInvolvedInTrust(null);
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, true);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorIsStillInvolvedNullAndCeasedDateIsNotNull() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setIndividualStillInvolvedInTrust(null);
        trustDataDtoList.getFirst().getIndividuals().getFirst().setCeasedDate(LocalDate.of(2020, 1, 1));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, true);
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.CEASED_DATE_FIELD);
        String validationMessage = String.format(ValidationMessages.NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorIsStillInvolvedTrueAndCeasedDateNotNull() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setIndividualStillInvolvedInTrust(true);
        trustDataDtoList.getFirst().getIndividuals().getFirst().setCeasedDate(LocalDate.of(2001, 1, 1));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, true);
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.CEASED_DATE_FIELD);
        String validationMessage = ValidationMessages.NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorIsStillInvolvedFalseAndCeasedDateNull() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setIndividualStillInvolvedInTrust(false);
        trustDataDtoList.getFirst().getIndividuals().getFirst().setCeasedDate(null);
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, true);
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.CEASED_DATE_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorIsStillInvolvedFalseAndCeasedDateFuture() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setIndividualStillInvolvedInTrust(false);
        trustDataDtoList.getFirst().getIndividuals().getFirst().setCeasedDate(LocalDate.now().plusDays(1));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, true);
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.CEASED_DATE_FIELD);
        String validationMessage = ValidationMessages.DATE_NOT_IN_PAST_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorIsStillInvolvedFalseAndCeasedDateBeforeTrustCreationDate() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setIndividualStillInvolvedInTrust(false);
        trustDataDtoList.getFirst().setCreationDate(LocalDate.of(2001, 1, 2));
        trustDataDtoList.getFirst().getIndividuals().getFirst().setCeasedDate(LocalDate.of(2001, 1, 1));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, true);
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.CEASED_DATE_FIELD);
        String validationMessage = ValidationMessages.CEASED_DATE_BEFORE_CREATION_DATE_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorIsStillInvolvedTrueAndCeasedDateIsNull() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setIndividualStillInvolvedInTrust(true);
        trustDataDtoList.getFirst().getIndividuals().getFirst().setCeasedDate(null);
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, true);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenCeasedDateIsAfterDateBecameInterestedPerson() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setType(BeneficialOwnerType.INTERESTED_PERSON.getValue());
        trustDataDtoList.getFirst().getIndividuals().getFirst().setIndividualStillInvolvedInTrust(false);
        trustDataDtoList.getFirst().getIndividuals().getFirst().setCeasedDate(LocalDate.of(2001, 1,1));
        trustDataDtoList.getFirst().getIndividuals().getFirst().setDateBecameInterestedPerson(LocalDate.of(2000,1,2));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, true);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenCeasedDateIsOnDateBecameInterestedPerson() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setType(BeneficialOwnerType.INTERESTED_PERSON.getValue());
        trustDataDtoList.getFirst().getIndividuals().getFirst().setIndividualStillInvolvedInTrust(false);
        trustDataDtoList.getFirst().getIndividuals().getFirst().setCeasedDate(LocalDate.of(2000, 1,2));
        trustDataDtoList.getFirst().getIndividuals().getFirst().setDateBecameInterestedPerson(LocalDate.of(2000,1,2));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, true);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenCeasedDateIsBeforeDateBecameInterestedPerson() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setType(BeneficialOwnerType.INTERESTED_PERSON.getValue());
        trustDataDtoList.getFirst().getIndividuals().getFirst().setIndividualStillInvolvedInTrust(false);
        trustDataDtoList.getFirst().getIndividuals().getFirst().setCeasedDate(LocalDate.of(2000, 1,2));
        trustDataDtoList.getFirst().getIndividuals().getFirst().setDateBecameInterestedPerson(LocalDate.of(2003,1,1));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, true);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                TrustIndividualDto.CEASED_DATE_FIELD);
        String validationMessage = ValidationMessages.CEASED_DATE_BEFORE_DATE_BECAME_INTERESTED_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenCeasedDateIsAfterDateOfBirth() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setType(BeneficialOwnerType.INTERESTED_PERSON.getValue());
        trustDataDtoList.getFirst().getIndividuals().getFirst().setIndividualStillInvolvedInTrust(false);
        trustDataDtoList.getFirst().getIndividuals().getFirst().setCeasedDate(LocalDate.of(2001, 1,1));
        trustDataDtoList.getFirst().getIndividuals().getFirst().setDateOfBirth(LocalDate.of(2000,1,2));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, true);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenCeasedDateIsOnDateOfBirth() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setType(BeneficialOwnerType.INTERESTED_PERSON.getValue());
        trustDataDtoList.getFirst().getIndividuals().getFirst().setIndividualStillInvolvedInTrust(false);
        trustDataDtoList.getFirst().getIndividuals().getFirst().setCeasedDate(LocalDate.of(2000, 1,2));
        trustDataDtoList.getFirst().getIndividuals().getFirst().setDateOfBirth(LocalDate.of(2000,1,2));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, true);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenCeasedDateIsBeforeDateOfBirth() {
        trustDataDtoList.getFirst().getIndividuals().getFirst().setType(BeneficialOwnerType.INTERESTED_PERSON.getValue());
        trustDataDtoList.getFirst().getIndividuals().getFirst().setIndividualStillInvolvedInTrust(false);
        trustDataDtoList.getFirst().getIndividuals().getFirst().setCeasedDate(LocalDate.of(2000, 1,2));
        trustDataDtoList.getFirst().getIndividuals().getFirst().setDateOfBirth(LocalDate.of(2003,1,1));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, true);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                TrustIndividualDto.CEASED_DATE_FIELD);
        String validationMessage = ValidationMessages.CEASED_DATE_BEFORE_DATE_OF_BIRTH.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    private void assertError(String qualifiedFieldName, String message, Errors errors) {
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(message).build();
        assertTrue(errors.containsError(err));
    }
}
