package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.overseasentitiesapi.mocks.AddressMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.BeneficialOwnerAllFieldsMock;
import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_CORPORATE_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

@ExtendWith(MockitoExtension.class)
class BeneficialOwnerCorporateValidatorTest {

    private static final String CONTEXT = "12345";

    private AddressDtoValidator addressDtoValidator;
    private BeneficialOwnerCorporateValidator beneficialOwnerCorporateValidator;

    private List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList;

    @BeforeEach
    public void init() {
        addressDtoValidator = new AddressDtoValidator();
        beneficialOwnerCorporateValidator = new BeneficialOwnerCorporateValidator(addressDtoValidator);
        beneficialOwnerCorporateDtoList = new ArrayList<>();
        BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto();
        beneficialOwnerCorporateDto.setPrincipalAddress(AddressMock.getAddressDto());
        beneficialOwnerCorporateDtoList.add(beneficialOwnerCorporateDto);
    }

    @Test
    void testNoErrorReportedWhenAllFieldsAreCorrect() {
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenNameFieldIsEmpty() {
        beneficialOwnerCorporateDtoList.get(0).setName("  ");
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.NAME_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        assertError(BeneficialOwnerCorporateDto.NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNameFieldIsNull() {
        beneficialOwnerCorporateDtoList.get(0).setName(null);
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.NAME_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        assertError(BeneficialOwnerCorporateDto.NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNameFieldExceedsMaxLength() {
        beneficialOwnerCorporateDtoList.get(0).setName(StringUtils.repeat("A", 51));
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.NAME_FIELD);
        String validationMessage = qualifiedFieldName + " must be 50 characters or less";
        assertError(BeneficialOwnerCorporateDto.NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNameFieldContainsInvalidCharacters() {
        beneficialOwnerCorporateDtoList.get(0).setName("Дракон");
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.NAME_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        assertError(BeneficialOwnerCorporateDto.NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLegalFormFieldIsEmpty() {
        beneficialOwnerCorporateDtoList.get(0).setLegalForm("  ");
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.LEGAL_FORM_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        assertError(BeneficialOwnerCorporateDto.LEGAL_FORM_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLegalFormFieldIsNull() {
        beneficialOwnerCorporateDtoList.get(0).setLegalForm(null);
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.LEGAL_FORM_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        assertError(BeneficialOwnerCorporateDto.LEGAL_FORM_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLegalFormFieldExceedsMaxLength() {
        beneficialOwnerCorporateDtoList.get(0).setLegalForm(StringUtils.repeat("A", 161));
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.LEGAL_FORM_FIELD);
        String validationMessage = qualifiedFieldName + " must be 160 characters or less";
        assertError(BeneficialOwnerCorporateDto.LEGAL_FORM_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLegalFormFieldContainsInvalidCharacters() {
        beneficialOwnerCorporateDtoList.get(0).setLegalForm("Дракон");
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.LEGAL_FORM_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        assertError(BeneficialOwnerCorporateDto.LEGAL_FORM_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLawGovernedFieldIsEmpty() {
        beneficialOwnerCorporateDtoList.get(0).setLawGoverned("  ");
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.LAW_GOVERNED_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        assertError(BeneficialOwnerCorporateDto.LAW_GOVERNED_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLawGovernedFieldIsNull() {
        beneficialOwnerCorporateDtoList.get(0).setLawGoverned(null);
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.LAW_GOVERNED_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        assertError(BeneficialOwnerCorporateDto.LAW_GOVERNED_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLawGovernedFieldExceedsMaxLength() {
        beneficialOwnerCorporateDtoList.get(0).setLawGoverned(StringUtils.repeat("A", 161));
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.LAW_GOVERNED_FIELD);
        String validationMessage = qualifiedFieldName + " must be 160 characters or less";
        assertError(BeneficialOwnerCorporateDto.LAW_GOVERNED_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLawGovernedFieldContainsInvalidCharacters() {
        beneficialOwnerCorporateDtoList.get(0).setLawGoverned("Дракон");
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.LAW_GOVERNED_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        assertError(BeneficialOwnerCorporateDto.LAW_GOVERNED_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNoNatureOfControlValuesAreAllNull() {
        beneficialOwnerCorporateDtoList.get(0).setBeneficialOwnerNatureOfControlTypes(null);
        beneficialOwnerCorporateDtoList.get(0).setNonLegalFirmMembersNatureOfControlTypes(null);
        beneficialOwnerCorporateDtoList.get(0).setTrusteesNatureOfControlTypes(null);
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateValidator.NATURE_OF_CONTROL_FIELDS);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerCorporateValidator.NATURE_OF_CONTROL_FIELDS, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNoNatureOfControlValuesAreAllEmpty() {
        beneficialOwnerCorporateDtoList.get(0).setBeneficialOwnerNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerCorporateDtoList.get(0).setNonLegalFirmMembersNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerCorporateDtoList.get(0).setTrusteesNatureOfControlTypes(new ArrayList<>());
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateValidator.NATURE_OF_CONTROL_FIELDS);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerCorporateValidator.NATURE_OF_CONTROL_FIELDS, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenSomeNatureOfControlValuesAreAllEmptyAndNull() {
        beneficialOwnerCorporateDtoList.get(0).setBeneficialOwnerNatureOfControlTypes(new ArrayList<>());
        List<NatureOfControlType> nonLegalNoc = new ArrayList<>();
        nonLegalNoc.add(NatureOfControlType.OVER_25_PERCENT_OF_SHARES);
        beneficialOwnerCorporateDtoList.get(0).setNonLegalFirmMembersNatureOfControlTypes(nonLegalNoc);
        beneficialOwnerCorporateDtoList.get(0).setTrusteesNatureOfControlTypes(null);

        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenStartFieldIsInThePast() {
        beneficialOwnerCorporateDtoList.get(0).setStartDate(LocalDate.of(1970,1, 1));
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenStartFieldIsNow() {
        beneficialOwnerCorporateDtoList.get(0).setStartDate(LocalDate.now());
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenIdentityStartIsInTheFuture() {
        beneficialOwnerCorporateDtoList.get(0).setStartDate(LocalDate.now().plusDays(1));
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.START_DATE_FIELD);
        String validationMessage = ValidationMessages.DATE_NOT_IN_PAST_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerCorporateDto.START_DATE_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenOnSanctionListFieldIsNull() {
        beneficialOwnerCorporateDtoList.get(0).setOnSanctionsList(null);
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.IS_ON_SANCTIONS_LIST_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        assertError(BeneficialOwnerCorporateDto.IS_ON_SANCTIONS_LIST_FIELD, validationMessage, errors);
    }

    private void assertError(String fieldName, String message, Errors errors) {
        String qualifiedFieldName = BENEFICIAL_OWNERS_CORPORATE_FIELD + "." + fieldName;
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(message).build();
        assertTrue(errors.containsError(err));
    }
}
