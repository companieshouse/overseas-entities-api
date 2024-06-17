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
        trustDataDtoList.get(0).getIndividuals().get(0).setForename(null);
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.FORENAME_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenForenameFieldIsEmpty() {
        trustDataDtoList.get(0).getIndividuals().get(0).setForename(" ");
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.FORENAME_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenForenameFieldIsNotEmpty() {
        trustDataDtoList.get(0).getIndividuals().get(0).setForename("TESTY");
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenForenameFieldExceedsMaxLength() {
        trustDataDtoList.get(0).getIndividuals().get(0).setForename(StringUtils.repeat("A", 161));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.FORENAME_FIELD);
        String validationMessage = qualifiedFieldName + " must be 160 characters or less";

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenForenameFieldContainsInvalidCharacters() {
        trustDataDtoList.get(0).getIndividuals().get(0).setForename("Дракон");
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.FORENAME_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s",
                qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenSurnameFieldIsNull() {
        trustDataDtoList.get(0).getIndividuals().get(0).setSurname(null);
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.SURNAME_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenSurnameFieldIsEmpty() {
        trustDataDtoList.get(0).getIndividuals().get(0).setSurname(" ");
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.SURNAME_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenSurnameFieldIsNotEmpty() {
        trustDataDtoList.get(0).getIndividuals().get(0).setSurname("TESTY");
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenSurnameFieldExceedsMaxLength() {
        trustDataDtoList.get(0).getIndividuals().get(0).setSurname(StringUtils.repeat("A", 161));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.SURNAME_FIELD);
        String validationMessage = qualifiedFieldName + " must be 160 characters or less";

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenSurnameFieldContainsInvalidCharacters() {
        trustDataDtoList.get(0).getIndividuals().get(0).setSurname("Дракон");
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.SURNAME_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s",
                qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenBirthDateFieldIsNull() {
        trustDataDtoList.get(0).getIndividuals().get(0).setDateOfBirth(null);
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.DATE_OF_BIRTH_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenBirthDateFieldIsInThePast() {
        trustDataDtoList.get(0).getIndividuals().get(0).setDateOfBirth(LocalDate.of(1970, 1, 1));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenBirthDateIsInTheFuture() {
        trustDataDtoList.get(0).getIndividuals().get(0).setDateOfBirth(LocalDate.now().plusDays(1));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.DATE_OF_BIRTH_FIELD);
        String validationMessage = ValidationMessages.DATE_NOT_IN_PAST_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenTypeFieldIsNull() {
        trustDataDtoList.get(0).getIndividuals().get(0).setType(null);
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.TYPE_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenTypeFieldIsEmpty() {
        trustDataDtoList.get(0).getIndividuals().get(0).setType(" ");
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.TYPE_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenTypeFieldValueIsNotExpected() {
        trustDataDtoList.get(0).getIndividuals().get(0).setType("TESTY");
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.TYPE_FIELD);
        String validationMessage = ValidationMessages.TRUST_INDIVIDUAL_TYPE_ERROR_MESSAGE.replace("%s",
                qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenTypeFieldVlaueIsExpected() {
        trustDataDtoList.get(0).getIndividuals().get(0).setType(BeneficialOwnerType.BENEFICIARY.getValue());
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenDateBecomeInterestedPersonFieldIsNull() {
        trustDataDtoList.get(0).getIndividuals().get(0).setType(BeneficialOwnerType.INTERESTED_PERSON.getValue());
        trustDataDtoList.get(0).getIndividuals().get(0).setDateBecameInterestedPerson(null);
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                TrustIndividualDto.DATE_BECAME_INTERESTED_PERSON_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenTypeFieldIsNotIntestestedPersonAndDateBecomeInterestedPersonFieldIsNull() {
        trustDataDtoList.get(0).getIndividuals().get(0).setType(BeneficialOwnerType.BENEFICIARY.getValue());
        trustDataDtoList.get(0).getIndividuals().get(0).setDateBecameInterestedPerson(null);
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT,false);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenDateBecomeInterestedPersonFieldIsInThePast() {
        trustDataDtoList.get(0).getIndividuals().get(0).setType(BeneficialOwnerType.INTERESTED_PERSON.getValue());
        trustDataDtoList.get(0).getIndividuals().get(0).setDateBecameInterestedPerson(LocalDate.of(1970, 1, 1));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenDateBecomeInterestedPersonIsInTheFuture() {
        trustDataDtoList.get(0).getIndividuals().get(0).setType(BeneficialOwnerType.INTERESTED_PERSON.getValue());
        trustDataDtoList.get(0).getIndividuals().get(0).setDateBecameInterestedPerson(LocalDate.now().plusDays(1));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                TrustIndividualDto.DATE_BECAME_INTERESTED_PERSON_FIELD);
        String validationMessage = ValidationMessages.DATE_NOT_IN_PAST_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenServiceAddressSameAsUsualResidentialAddressFieldIsNull() {
        trustDataDtoList.get(0).getIndividuals().get(0).setServiceAddressSameAsUsualResidentialAddress(null);
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.IS_SERVICE_ADDRESS_SAME_AS_USUAL_RESIDENTIAL_ADDRESS_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenServiceAddressSameAsUsualResidentialAddressFieldIs() {
        trustDataDtoList.get(0).getIndividuals().get(0).setServiceAddressSameAsUsualResidentialAddress(true);
        trustDataDtoList.get(0).getIndividuals().get(0).setServiceAddress(null);
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenServiceAddressSameAsUsualResidentialAddressFields() {
        var trustee = trustDataDtoList.get(0).getIndividuals().get(0);
        trustee.setServiceAddressSameAsUsualResidentialAddress(true);
        trustee.setUraAddressPremises("1");
        trustee.setUraAddressLine1("Broadway");
        trustee.setUraAddressLocality("New York");
        trustee.setUraAddressCountry("USA");

        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

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

        trustDataDtoList.get(0).getIndividuals().get(0).setNationality("Afghan");
        trustDataDtoList.get(0).getIndividuals().get(0).setSecondNationality("Afghan");
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

        trustDataDtoList.get(0).getIndividuals().get(0).setNationality("Afghan");
        trustDataDtoList.get(0).getIndividuals().get(0).setSecondNationality("Spain");
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorIsStillInvolvedTrueAndCeasedDateNull() {
        trustDataDtoList.get(0).getIndividuals().get(0).setIndividualStillInvolvedInTrust(true);
        trustDataDtoList.get(0).getIndividuals().get(0).setCeasedDate(null);
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorIsStillInvolvedFalseAndCeasedDateNotNull() {
        trustDataDtoList.get(0).getIndividuals().get(0).setIndividualStillInvolvedInTrust(false);
        trustDataDtoList.get(0).getIndividuals().get(0).setCeasedDate(LocalDate.of(2001, 1, 1));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, false);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorIsStillInvolvedNull() {
        trustDataDtoList.get(0).getIndividuals().get(0).setIndividualStillInvolvedInTrust(null);
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, true);
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.IS_INDIVIDUAL_STILL_INVOLVED_IN_TRUST_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorIsStillInvolvedTrueAndCeasedDateNotNull() {
        trustDataDtoList.get(0).getIndividuals().get(0).setIndividualStillInvolvedInTrust(true);
        trustDataDtoList.get(0).getIndividuals().get(0).setCeasedDate(LocalDate.of(2001, 1, 1));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, true);
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.CEASED_DATE_FIELD);
        String validationMessage = ValidationMessages.NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorIsStillInvolvedFalseAndCeasedDateNull() {
        trustDataDtoList.get(0).getIndividuals().get(0).setIndividualStillInvolvedInTrust(false);
        trustDataDtoList.get(0).getIndividuals().get(0).setCeasedDate(null);
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, true);
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.CEASED_DATE_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorIsStillInvolvedFalseAndCeasedDateFuture() {
        trustDataDtoList.get(0).getIndividuals().get(0).setIndividualStillInvolvedInTrust(false);
        trustDataDtoList.get(0).getIndividuals().get(0).setCeasedDate(LocalDate.now().plusDays(1));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, true);
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.CEASED_DATE_FIELD);
        String validationMessage = ValidationMessages.DATE_NOT_IN_PAST_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorIsStillInvolvedFalseAndCeasedDateBeforeTrustCreationDate() {
        trustDataDtoList.get(0).getIndividuals().get(0).setIndividualStillInvolvedInTrust(false);
        trustDataDtoList.get(0).setCreationDate(LocalDate.of(2001, 1, 2));
        trustDataDtoList.get(0).getIndividuals().get(0).setCeasedDate(LocalDate.of(2001, 1, 1));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, true);
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.CEASED_DATE_FIELD);
        String validationMessage = ValidationMessages.CEASED_DATE_BEFORE_CREATION_DATE_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }
    @Test
    void testErrorIsStillInvolvedTrueAndCeasedDateIsNull() {
        trustDataDtoList.get(0).getIndividuals().get(0).setIndividualStillInvolvedInTrust(true);
        trustDataDtoList.get(0).getIndividuals().get(0).setCeasedDate(null); //LocalDate.of(2001, 1, 1));
        Errors errors = trustIndividualValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT, true);

        assertFalse(errors.hasErrors());
    }
    private void assertError(String qualifiedFieldName, String message, Errors errors) {
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(message).build();
        assertTrue(errors.containsError(err));
    }
}
