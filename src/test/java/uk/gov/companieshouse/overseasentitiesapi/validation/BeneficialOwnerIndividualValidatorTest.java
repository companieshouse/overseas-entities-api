package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.overseasentitiesapi.mocks.AddressMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.BeneficialOwnerAllFieldsMock;
import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
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
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

@ExtendWith(MockitoExtension.class)
class BeneficialOwnerIndividualValidatorTest {

    private static final String CONTEXT = "12345";

    private AddressDtoValidator addressDtoValidator;

    private BeneficialOwnerIndividualValidator beneficialOwnerIndividualValidator;

    private List<BeneficialOwnerIndividualDto> beneficialOwnerIndividualDtoList;

    @BeforeEach
    public void init() {
        addressDtoValidator = new AddressDtoValidator();
        beneficialOwnerIndividualValidator = new BeneficialOwnerIndividualValidator(addressDtoValidator);
        beneficialOwnerIndividualDtoList = new ArrayList<>();
        BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerIndividualDto();
        beneficialOwnerIndividualDto.setUsualResidentialAddress(AddressMock.getAddressDto());
        beneficialOwnerIndividualDtoList.add(beneficialOwnerIndividualDto);
    }

    @Test
    void testNoErrorReportedWhenAllFieldsAreCorrect() {
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenFirstNameFieldIsEmpty() {
        beneficialOwnerIndividualDtoList.get(0).setFirstName("  ");
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.FIRST_NAME_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerIndividualDto.FIRST_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenFirstNameFieldIsNull() {
        beneficialOwnerIndividualDtoList.get(0).setFirstName(null);
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.FIRST_NAME_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerIndividualDto.FIRST_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenFirstNameFieldExceedsMaxLength() {
        beneficialOwnerIndividualDtoList.get(0).setFirstName(StringUtils.repeat("A", 51));
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.FIRST_NAME_FIELD);

        assertError(BeneficialOwnerIndividualDto.FIRST_NAME_FIELD, qualifiedFieldName + " must be 50 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenFirstNameFieldContainsInvalidCharacters() {
        beneficialOwnerIndividualDtoList.get(0).setFirstName("Дракон");
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.FIRST_NAME_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerIndividualDto.FIRST_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLastNameFieldIsEmpty() {
        beneficialOwnerIndividualDtoList.get(0).setLastName("  ");
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.LAST_NAME_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerIndividualDto.LAST_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLastNameFieldIsNull() {
        beneficialOwnerIndividualDtoList.get(0).setLastName(null);
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.LAST_NAME_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerIndividualDto.LAST_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLastNameFieldExceedsMaxLength() {
        beneficialOwnerIndividualDtoList.get(0).setLastName(StringUtils.repeat("A", 161));
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.LAST_NAME_FIELD);

        assertError(BeneficialOwnerIndividualDto.LAST_NAME_FIELD, qualifiedFieldName + " must be 160 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenLastNameFieldContainsInvalidCharacters() {
        beneficialOwnerIndividualDtoList.get(0).setLastName("Дракон");
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.LAST_NAME_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerIndividualDto.LAST_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenDateOfBirthFieldIsInThePast() {
        beneficialOwnerIndividualDtoList.get(0).setDateOfBirth(LocalDate.of(1970,1, 1));
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenDateOfBirthFieldIsNow() {
        beneficialOwnerIndividualDtoList.get(0).setDateOfBirth(LocalDate.now());
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenIdentityDateOfBirthIsInTheFuture() {
        beneficialOwnerIndividualDtoList.get(0).setDateOfBirth(LocalDate.now().plusDays(1));
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.DATE_OF_BIRTH_FIELD);
        String validationMessage = ValidationMessages.DATE_NOT_IN_PAST_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerIndividualDto.DATE_OF_BIRTH_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNationalityFieldIsEmpty() {
        beneficialOwnerIndividualDtoList.get(0).setNationality("  ");
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.NATIONALITY_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerIndividualDto.NATIONALITY_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNationalityFieldIsNull() {
        beneficialOwnerIndividualDtoList.get(0).setNationality(null);
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.NATIONALITY_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerIndividualDto.NATIONALITY_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNationalityFieldExceedsMaxLength() {
        beneficialOwnerIndividualDtoList.get(0).setNationality(StringUtils.repeat("A", 51));
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.NATIONALITY_FIELD);

        assertError(BeneficialOwnerIndividualDto.NATIONALITY_FIELD, qualifiedFieldName + " must be 50 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenNationalityFieldContainsInvalidCharacters() {
        beneficialOwnerIndividualDtoList.get(0).setNationality("Дракон");
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.NATIONALITY_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerIndividualDto.NATIONALITY_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenSameAddressFieldIsNull() {
        beneficialOwnerIndividualDtoList.get(0).setServiceAddressSameAsUsualResidentialAddress(null);
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.IS_SERVICE_ADDRESS_SAME_AS_USUAL_RESIDENTIAL_ADDRESS_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerIndividualDto.IS_SERVICE_ADDRESS_SAME_AS_USUAL_RESIDENTIAL_ADDRESS_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenStartDateFieldIsInThePast() {
        beneficialOwnerIndividualDtoList.get(0).setStartDate(LocalDate.of(1970,1, 1));
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenStartDateFieldIsNow() {
        beneficialOwnerIndividualDtoList.get(0).setStartDate(LocalDate.now());
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenIdentityStartDateIsInTheFuture() {
        beneficialOwnerIndividualDtoList.get(0).setStartDate(LocalDate.now().plusDays(1));
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.START_DATE_FIELD);
        String validationMessage = ValidationMessages.DATE_NOT_IN_PAST_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerIndividualDto.START_DATE_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNoNatureOfControlValuesAreAllNull() {
        beneficialOwnerIndividualDtoList.get(0).setBeneficialOwnerNatureOfControlTypes(null);
        beneficialOwnerIndividualDtoList.get(0).setNonLegalFirmMembersNatureOfControlTypes(null);
        beneficialOwnerIndividualDtoList.get(0).setTrusteesNatureOfControlTypes(null);
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualValidator.NATURE_OF_CONTROL_FIELDS);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerIndividualValidator.NATURE_OF_CONTROL_FIELDS, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNoNatureOfControlValuesAreAllEmpty() {
        beneficialOwnerIndividualDtoList.get(0).setBeneficialOwnerNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerIndividualDtoList.get(0).setNonLegalFirmMembersNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerIndividualDtoList.get(0).setTrusteesNatureOfControlTypes(new ArrayList<>());
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualValidator.NATURE_OF_CONTROL_FIELDS);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerIndividualValidator.NATURE_OF_CONTROL_FIELDS, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenSomeNatureOfControlValuesAreAllEmptyAndNull() {
        beneficialOwnerIndividualDtoList.get(0).setBeneficialOwnerNatureOfControlTypes(new ArrayList<>());
        List<NatureOfControlType> nonLegalNoc = new ArrayList<>();
        nonLegalNoc.add(NatureOfControlType.OVER_25_PERCENT_OF_SHARES);
        beneficialOwnerIndividualDtoList.get(0).setNonLegalFirmMembersNatureOfControlTypes(nonLegalNoc);
        beneficialOwnerIndividualDtoList.get(0).setTrusteesNatureOfControlTypes(null);

        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenOnSanctionListFieldIsNull() {
        beneficialOwnerIndividualDtoList.get(0).setOnSanctionsList(null);
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.IS_ON_SANCTIONS_LIST_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerIndividualDto.IS_ON_SANCTIONS_LIST_FIELD, validationMessage, errors);
    }

    private void assertError(String fieldName, String message, Errors errors) {
        String qualifiedFieldName = BENEFICIAL_OWNERS_INDIVIDUAL_FIELD + "." + fieldName;
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(message).build();
        assertTrue(errors.containsError(err));
    }

}
