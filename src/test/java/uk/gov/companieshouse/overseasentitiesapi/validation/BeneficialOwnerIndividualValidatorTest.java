package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.companieshouse.overseasentitiesapi.mocks.AddressMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.BeneficialOwnerAllFieldsMock;
import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
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

    private static final String LOGGING_CONTEXT = "12345";

    @InjectMocks
    private AddressDtoValidator addressDtoValidator;

    @InjectMocks
    private NationalityValidator nationalityValidator;

    private BeneficialOwnerIndividualValidator beneficialOwnerIndividualValidator;

    private List<BeneficialOwnerIndividualDto> beneficialOwnerIndividualDtoList;

    @BeforeEach
    public void init() {
        beneficialOwnerIndividualValidator = new BeneficialOwnerIndividualValidator(addressDtoValidator, nationalityValidator);
        beneficialOwnerIndividualDtoList = new ArrayList<>();
        BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerIndividualDto();
        beneficialOwnerIndividualDto.setUsualResidentialAddress(AddressMock.getAddressDto());
        beneficialOwnerIndividualDto.setServiceAddress(new AddressDto());
        beneficialOwnerIndividualDtoList.add(beneficialOwnerIndividualDto);
    }

    @Test
    void testNoErrorReportedWhenAllFieldsAreCorrect() {
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenFirstNameFieldIsEmpty() {
        beneficialOwnerIndividualDtoList.get(0).setFirstName("  ");
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.FIRST_NAME_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerIndividualDto.FIRST_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenFirstNameFieldIsNull() {
        beneficialOwnerIndividualDtoList.get(0).setFirstName(null);
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.FIRST_NAME_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerIndividualDto.FIRST_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenFirstNameFieldExceedsMaxLength() {
        beneficialOwnerIndividualDtoList.get(0).setFirstName(StringUtils.repeat("A", 51));
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.FIRST_NAME_FIELD);

        assertError(BeneficialOwnerIndividualDto.FIRST_NAME_FIELD, qualifiedFieldName + " must be 50 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenFirstNameFieldContainsInvalidCharacters() {
        beneficialOwnerIndividualDtoList.get(0).setFirstName("Дракон");
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.FIRST_NAME_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerIndividualDto.FIRST_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLastNameFieldIsEmpty() {
        beneficialOwnerIndividualDtoList.get(0).setLastName("  ");
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.LAST_NAME_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerIndividualDto.LAST_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLastNameFieldIsNull() {
        beneficialOwnerIndividualDtoList.get(0).setLastName(null);
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.LAST_NAME_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerIndividualDto.LAST_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLastNameFieldExceedsMaxLength() {
        beneficialOwnerIndividualDtoList.get(0).setLastName(StringUtils.repeat("A", 161));
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.LAST_NAME_FIELD);

        assertError(BeneficialOwnerIndividualDto.LAST_NAME_FIELD, qualifiedFieldName + " must be 160 characters or less", errors);
    }

    @Test
    void testErrorReportedWhenLastNameFieldContainsInvalidCharacters() {
        beneficialOwnerIndividualDtoList.get(0).setLastName("Дракон");
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.LAST_NAME_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerIndividualDto.LAST_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenDateOfBirthFieldIsInThePast() {
        beneficialOwnerIndividualDtoList.get(0).setDateOfBirth(LocalDate.of(1970,1, 1));
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenDateOfBirthFieldIsNow() {
        beneficialOwnerIndividualDtoList.get(0).setDateOfBirth(LocalDate.now());
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenDateOfBirthIsInTheFuture() {
        beneficialOwnerIndividualDtoList.get(0).setDateOfBirth(LocalDate.now().plusDays(1));
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.DATE_OF_BIRTH_FIELD);
        String validationMessage = ValidationMessages.DATE_NOT_IN_PAST_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerIndividualDto.DATE_OF_BIRTH_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenDateOfBirthFieldIsNull() {
        beneficialOwnerIndividualDtoList.get(0).setDateOfBirth(null);
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.DATE_OF_BIRTH_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerIndividualDto.DATE_OF_BIRTH_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNationalityFieldIsEmpty() {
        beneficialOwnerIndividualDtoList.get(0).setNationality("  ");
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.NATIONALITY_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerIndividualDto.NATIONALITY_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNationalityFieldIsNull() {
        beneficialOwnerIndividualDtoList.get(0).setNationality(null);
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.NATIONALITY_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerIndividualDto.NATIONALITY_FIELD, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenOptionalSecondNationalityFieldIsEmpty() {
        beneficialOwnerIndividualDtoList.get(0).setSecondNationality("  ");
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenOptionalSecondNationalityFieldIsNull() {
        beneficialOwnerIndividualDtoList.get(0).setSecondNationality(null);
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenSameAddressFieldIsNull() {
        beneficialOwnerIndividualDtoList.get(0).setServiceAddressSameAsUsualResidentialAddress(null);
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.IS_SERVICE_ADDRESS_SAME_AS_USUAL_RESIDENTIAL_ADDRESS_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerIndividualDto.IS_SERVICE_ADDRESS_SAME_AS_USUAL_RESIDENTIAL_ADDRESS_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenSameAddressFlagIsFalseWhenServiceAddressIsEmpty() {
        beneficialOwnerIndividualDtoList.get(0).setServiceAddressSameAsUsualResidentialAddress(false);
        beneficialOwnerIndividualDtoList.get(0).setServiceAddress(new AddressDto());
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.SERVICE_ADDRESS_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.PROPERTY_NAME_NUMBER_FIELD));
        assertError(getQualifiedFieldName(BeneficialOwnerIndividualDto.SERVICE_ADDRESS_FIELD, AddressDto.PROPERTY_NAME_NUMBER_FIELD), validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenSameAddressFlagIsTrueWhenServiceAddressNotEmpty() {
        beneficialOwnerIndividualDtoList.get(0).setServiceAddressSameAsUsualResidentialAddress(true);
        beneficialOwnerIndividualDtoList.get(0).setServiceAddress(AddressMock.getAddressDto());
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.SERVICE_ADDRESS_FIELD);

        String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.PROPERTY_NAME_NUMBER_FIELD));
        assertError(getQualifiedFieldName(BeneficialOwnerIndividualDto.SERVICE_ADDRESS_FIELD, AddressDto.PROPERTY_NAME_NUMBER_FIELD), validationMessage, errors);
        validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.LINE_1_FIELD));
        assertError(getQualifiedFieldName(BeneficialOwnerIndividualDto.SERVICE_ADDRESS_FIELD, AddressDto.LINE_1_FIELD), validationMessage, errors);
        validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.TOWN_FIELD));
        assertError(getQualifiedFieldName(BeneficialOwnerIndividualDto.SERVICE_ADDRESS_FIELD, AddressDto.TOWN_FIELD), validationMessage, errors);
        validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.COUNTY_FIELD));
        assertError(getQualifiedFieldName(BeneficialOwnerIndividualDto.SERVICE_ADDRESS_FIELD, AddressDto.COUNTY_FIELD), validationMessage, errors);
        validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.COUNTRY_FIELD));
        assertError(getQualifiedFieldName(BeneficialOwnerIndividualDto.SERVICE_ADDRESS_FIELD, AddressDto.COUNTRY_FIELD), validationMessage, errors);
        validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.POSTCODE_FIELD));
        assertError(getQualifiedFieldName(BeneficialOwnerIndividualDto.SERVICE_ADDRESS_FIELD, AddressDto.POSTCODE_FIELD), validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenStartDateFieldIsInThePast() {
        beneficialOwnerIndividualDtoList.get(0).setStartDate(LocalDate.of(1970,1, 1));
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenStartDateFieldIsNow() {
        beneficialOwnerIndividualDtoList.get(0).setStartDate(LocalDate.now());
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenStartDateFieldIsInTheFuture() {
        beneficialOwnerIndividualDtoList.get(0).setStartDate(LocalDate.now().plusDays(1));
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.START_DATE_FIELD);
        String validationMessage = ValidationMessages.DATE_NOT_IN_PAST_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerIndividualDto.START_DATE_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenStartDateFieldIsNull() {
        beneficialOwnerIndividualDtoList.get(0).setStartDate(null);
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.START_DATE_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerIndividualDto.START_DATE_FIELD, validationMessage, errors);
    }

    @Test
    void testNoErrorsWhenCeasedDateFieldIsInThePast() {
        beneficialOwnerIndividualDtoList.get(0).setStartDate(LocalDate.now().minusDays(2));
        beneficialOwnerIndividualDtoList.get(0).setCeasedDate(LocalDate.now().minusDays(1));
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorsWhenCeasedDateFieldIsNow() {
        beneficialOwnerIndividualDtoList.get(0).setCeasedDate(LocalDate.now());
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenCeasedDateIsBeforeStartDate() {
        beneficialOwnerIndividualDtoList.get(0).setStartDate(LocalDate.now().plusDays(1));
        beneficialOwnerIndividualDtoList.get(0).setCeasedDate(LocalDate.now());
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualDto.CEASED_DATE_FIELD);
        String validationMessage = String.format(ValidationMessages.CEASED_DATE_BEFORE_START_DATE_ERROR_MESSAGE, qualifiedFieldName);

        assertError(BeneficialOwnerIndividualDto.CEASED_DATE_FIELD, validationMessage, errors);
    }

    @Test
    void testNoErrorWhenCeasedDateIsSameAsStartDate() {
        beneficialOwnerIndividualDtoList.get(0).setStartDate(LocalDate.now().minusDays(1));
        beneficialOwnerIndividualDtoList.get(0).setCeasedDate(LocalDate.now().minusDays(1));
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
    }

    //Added for UAR-1595 can remove test when FEATURE_FLAG_ENABLE_PROPERTY_OR_LAND_OWNER_NOC is removed
    @Test
    void testErrorReportedWhenNoNatureOfControlValuesAreAllNullWhenNewNocsFlagIsFalse() {
        setNewNocsEnabledFeatureFlag(false);
        beneficialOwnerIndividualDtoList.get(0).setBeneficialOwnerNatureOfControlTypes(null);
        beneficialOwnerIndividualDtoList.get(0).setNonLegalFirmMembersNatureOfControlTypes(null);
        beneficialOwnerIndividualDtoList.get(0).setTrusteesNatureOfControlTypes(null);
        beneficialOwnerIndividualDtoList.get(0).setTrustNatureOfNatureOfControlTypes(null);
        beneficialOwnerIndividualDtoList.get(0).setOwnerOfLandPersonNatureOfNatureOfControlJurisdiction(null);
        beneficialOwnerIndividualDtoList.get(0).setOwnerOfLandOtherEntityNatureOfNatureOfControlJurisdiction(null);
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualValidator.NATURE_OF_CONTROL_FIELDS);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerIndividualValidator.NATURE_OF_CONTROL_FIELDS, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNoNatureOfControlValuesAreAllNullWhenNewNocsFlagIsTrue() {
        setNewNocsEnabledFeatureFlag(true);
        beneficialOwnerIndividualDtoList.get(0).setBeneficialOwnerNatureOfControlTypes(null);
        beneficialOwnerIndividualDtoList.get(0).setNonLegalFirmMembersNatureOfControlTypes(null);
        beneficialOwnerIndividualDtoList.get(0).setTrusteesNatureOfControlTypes(null);
        beneficialOwnerIndividualDtoList.get(0).setTrustNatureOfNatureOfControlTypes(null);
        beneficialOwnerIndividualDtoList.get(0).setOwnerOfLandPersonNatureOfNatureOfControlJurisdiction(null);
        beneficialOwnerIndividualDtoList.get(0).setOwnerOfLandOtherEntityNatureOfNatureOfControlJurisdiction(null);
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualValidator.NATURE_OF_CONTROL_FIELDS);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerIndividualValidator.NATURE_OF_CONTROL_FIELDS, validationMessage, errors);
    }

    //Added for UAR-1595 can remove test when FEATURE_FLAG_ENABLE_PROPERTY_OR_LAND_OWNER_NOC is removed
    @Test
    void testErrorReportedWhenNoNatureOfControlValuesAreAllEmptyWhenNewNocsFlagIsFalse() {
        setNewNocsEnabledFeatureFlag(false);
        beneficialOwnerIndividualDtoList.get(0).setBeneficialOwnerNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerIndividualDtoList.get(0).setNonLegalFirmMembersNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerIndividualDtoList.get(0).setTrusteesNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerIndividualDtoList.get(0).setTrustNatureOfNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerIndividualDtoList.get(0).setOwnerOfLandPersonNatureOfNatureOfControlJurisdiction(new ArrayList<>());
        beneficialOwnerIndividualDtoList.get(0).setOwnerOfLandOtherEntityNatureOfNatureOfControlJurisdiction(new ArrayList<>());
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualValidator.NATURE_OF_CONTROL_FIELDS);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerIndividualValidator.NATURE_OF_CONTROL_FIELDS, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNoNatureOfControlValuesAreAllEmptyWhenNewNocsFlagIsTrue() {
        setNewNocsEnabledFeatureFlag(true);
        beneficialOwnerIndividualDtoList.get(0).setBeneficialOwnerNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerIndividualDtoList.get(0).setNonLegalFirmMembersNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerIndividualDtoList.get(0).setTrusteesNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerIndividualDtoList.get(0).setTrustNatureOfNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerIndividualDtoList.get(0).setOwnerOfLandPersonNatureOfNatureOfControlJurisdiction(new ArrayList<>());
        beneficialOwnerIndividualDtoList.get(0).setOwnerOfLandOtherEntityNatureOfNatureOfControlJurisdiction(new ArrayList<>());
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualValidator.NATURE_OF_CONTROL_FIELDS);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerIndividualValidator.NATURE_OF_CONTROL_FIELDS, validationMessage, errors);
    }

    /*
       When FEATURE_FLAG_ENABLE_PROPERTY_OR_LAND_OWNER_NOC is removed choose this or one of the three below
       do not need to keep all 4
       Changed for UAR-1595 can keep when FEATURE_FLAG_ENABLE_PROPERTY_OR_LAND_OWNER_NOC is removed
       but will need to remove the word "Existing" end flag suffix and setFeatureFlag
     */
    @Test
    void testNoErrorReportedWhenExistingNocPresentButOtherNatureOfControlValuesAreAllEmptyAndNullWhenNewNocsFlagIsFalse() {
        setNewNocsEnabledFeatureFlag(false);
        beneficialOwnerIndividualDtoList.get(0).setBeneficialOwnerNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerIndividualDtoList.get(0).setTrusteesNatureOfControlTypes(new ArrayList<>());
        List<NatureOfControlType> nonLegalNoc = new ArrayList<>();
        nonLegalNoc.add(NatureOfControlType.OVER_25_PERCENT_OF_SHARES);
        beneficialOwnerIndividualDtoList.get(0).setNonLegalFirmMembersNatureOfControlTypes(nonLegalNoc);

        // New NOCs
        beneficialOwnerIndividualDtoList.get(0).setTrustNatureOfNatureOfControlTypes(null);
        beneficialOwnerIndividualDtoList.get(0).setOwnerOfLandPersonNatureOfNatureOfControlJurisdiction(new ArrayList<>());
        beneficialOwnerIndividualDtoList.get(0).setOwnerOfLandOtherEntityNatureOfNatureOfControlJurisdiction(new ArrayList<>());

        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    /*
      Changed for UAR-1595 can keep when FEATURE_FLAG_ENABLE_PROPERTY_OR_LAND_OWNER_NOC is removed
      but will need to remove the word "Existing" end flag suffix and setFeatureFlag
    */
    @Test
    void testNoErrorReportedWhenExistingNocPresentButOtherNatureOfControlValuesAreAllEmptyAndNullWhenNewNocsFlagIsTrue() {
        setNewNocsEnabledFeatureFlag(true);
        beneficialOwnerIndividualDtoList.get(0).setBeneficialOwnerNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerIndividualDtoList.get(0).setTrusteesNatureOfControlTypes(new ArrayList<>());
        List<NatureOfControlType> nonLegalNoc = new ArrayList<>();
        nonLegalNoc.add(NatureOfControlType.OVER_25_PERCENT_OF_SHARES);
        beneficialOwnerIndividualDtoList.get(0).setNonLegalFirmMembersNatureOfControlTypes(nonLegalNoc);

        // New NOCs
        beneficialOwnerIndividualDtoList.get(0).setTrustNatureOfNatureOfControlTypes(null);
        beneficialOwnerIndividualDtoList.get(0).setOwnerOfLandPersonNatureOfNatureOfControlJurisdiction(new ArrayList<>());
        beneficialOwnerIndividualDtoList.get(0).setOwnerOfLandOtherEntityNatureOfNatureOfControlJurisdiction(new ArrayList<>());

        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    /*
      Addded for UAR-1595 can keep when FEATURE_FLAG_ENABLE_PROPERTY_OR_LAND_OWNER_NOC is removed
      but will need to remove the word "Existing" end flag suffix and setFeatureFlag
    */
    @Test
    void testErrorReportedWhenNewNocPresentButOtherNatureOfControlValuesAreAllEmptyAndNullWhenNewNocsFlagIsFalse() {
        setNewNocsEnabledFeatureFlag(false);
        beneficialOwnerIndividualDtoList.get(0).setBeneficialOwnerNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerIndividualDtoList.get(0).setTrusteesNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerIndividualDtoList.get(0).setNonLegalFirmMembersNatureOfControlTypes(null);

        // New NOCs
        List<NatureOfControlType> trustNoc = new ArrayList<>();
        trustNoc.add(NatureOfControlType.OVER_25_PERCENT_OF_SHARES);
        beneficialOwnerIndividualDtoList.get(0).setTrustNatureOfNatureOfControlTypes(trustNoc); // New NOC present
        beneficialOwnerIndividualDtoList.get(0).setOwnerOfLandPersonNatureOfNatureOfControlJurisdiction(new ArrayList<>());
        beneficialOwnerIndividualDtoList.get(0).setOwnerOfLandOtherEntityNatureOfNatureOfControlJurisdiction(new ArrayList<>());

        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
                BeneficialOwnerIndividualValidator.NATURE_OF_CONTROL_FIELDS);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(BeneficialOwnerIndividualValidator.NATURE_OF_CONTROL_FIELDS, validationMessage, errors);
    }

    /*
       Added for UAR-1595 can keep when FEATURE_FLAG_ENABLE_PROPERTY_OR_LAND_OWNER_NOC is removed
       but will need to remove the word "Existing" end flag suffix and setFeatureFlag
     */
    @Test
    void testNoErrorReportedWhenNewNocPresentButOtherNatureOfControlValuesAreAllEmptyAndNullWhenNewNocsFlagIsTrue() {
        setNewNocsEnabledFeatureFlag(true);
        beneficialOwnerIndividualDtoList.get(0).setBeneficialOwnerNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerIndividualDtoList.get(0).setTrusteesNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerIndividualDtoList.get(0).setNonLegalFirmMembersNatureOfControlTypes(null);

        // New NOCs
        List<NatureOfControlType> trustNoc = new ArrayList<>();
        trustNoc.add(NatureOfControlType.OVER_25_PERCENT_OF_SHARES);
        beneficialOwnerIndividualDtoList.get(0).setTrustNatureOfNatureOfControlTypes(trustNoc); // New NOC present
        beneficialOwnerIndividualDtoList.get(0).setOwnerOfLandPersonNatureOfNatureOfControlJurisdiction(new ArrayList<>());
        beneficialOwnerIndividualDtoList.get(0).setOwnerOfLandOtherEntityNatureOfNatureOfControlJurisdiction(new ArrayList<>());

        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenOnSanctionListFieldIsNull() {
        beneficialOwnerIndividualDtoList.get(0).setOnSanctionsList(null);
        Errors errors = beneficialOwnerIndividualValidator.validate(beneficialOwnerIndividualDtoList, new Errors(), LOGGING_CONTEXT);
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

    private void setNewNocsEnabledFeatureFlag(boolean value) {
        ReflectionTestUtils.setField(beneficialOwnerIndividualValidator, "isPropertyAndLAndNocEnabled", value);
    }

}
