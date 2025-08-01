package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.companieshouse.overseasentitiesapi.mocks.AddressMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.BeneficialOwnerAllFieldsMock;
import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlJurisdictionType;
import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerGovernmentOrPublicAuthorityDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

@ExtendWith(MockitoExtension.class)
class BeneficialOwnerGovernmentOrPublicAuthorityValidatorTest {

    private static final String LOGGING_CONTEXT = "12345";

    private BeneficialOwnerGovernmentOrPublicAuthorityValidator beneficialOwnerGovernmentOrPublicAuthorityValidator;

    @InjectMocks
    private AddressDtoValidator addressDtoValidator;

    private List<BeneficialOwnerGovernmentOrPublicAuthorityDto> beneficialOwnerGovernmentOrPublicAuthorityDtoList;

    @BeforeEach
    void init() {
        beneficialOwnerGovernmentOrPublicAuthorityValidator = new BeneficialOwnerGovernmentOrPublicAuthorityValidator(addressDtoValidator);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList = new ArrayList<>();
        BeneficialOwnerGovernmentOrPublicAuthorityDto beneficialOwnerGovernmentOrPublicAuthorityDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerGovernmentOrPublicAuthorityDto();
        beneficialOwnerGovernmentOrPublicAuthorityDto.setPrincipalAddress(AddressMock.getAddressDto());
        beneficialOwnerGovernmentOrPublicAuthorityDto.setServiceAddress(new AddressDto());
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.add(beneficialOwnerGovernmentOrPublicAuthorityDto);
    }

    @Test
    void testNoErrorReportedWhenAllFieldsAreCorrect() {
        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenNameFieldIsEmpty() {
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setName("  ");
        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD,
                BeneficialOwnerGovernmentOrPublicAuthorityDto.NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);
        assertError(BeneficialOwnerGovernmentOrPublicAuthorityDto.NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNameFieldIsNull() {
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setName(null);
        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD,
                BeneficialOwnerGovernmentOrPublicAuthorityDto.NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(BeneficialOwnerGovernmentOrPublicAuthorityDto.NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenNameFieldIsAtMaxLength() {
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setName(StringUtils.repeat("A", 160));
        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenNameFieldExceedsMaxLength() {
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setName(StringUtils.repeat("A", 161));
        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD,
                BeneficialOwnerGovernmentOrPublicAuthorityDto.NAME_FIELD);
        String validationMessage = qualifiedFieldName + " must be 160 characters or less";
        assertError(BeneficialOwnerGovernmentOrPublicAuthorityDto.NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNameFieldContainsInvalidCharacters() {
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setName("Дракон");
        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD,
                BeneficialOwnerGovernmentOrPublicAuthorityDto.NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName);
        assertError(BeneficialOwnerGovernmentOrPublicAuthorityDto.NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenSameAddressFieldIsNull() {
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setServiceAddressSameAsPrincipalAddress(null);
        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD,
                BeneficialOwnerGovernmentOrPublicAuthorityDto.IS_SERVICE_ADDRESS_SAME_AS_PRINCIPAL_ADDRESS_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);

        assertError(BeneficialOwnerGovernmentOrPublicAuthorityDto.IS_SERVICE_ADDRESS_SAME_AS_PRINCIPAL_ADDRESS_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenSameAddressFlagIsFalseWhenServiceAddressIsEmpty() {
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setServiceAddressSameAsPrincipalAddress(false);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setServiceAddress(new AddressDto());
        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD,
                BeneficialOwnerGovernmentOrPublicAuthorityDto.SERVICE_ADDRESS_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.PROPERTY_NAME_NUMBER_FIELD));
        assertError(getQualifiedFieldName(BeneficialOwnerGovernmentOrPublicAuthorityDto.SERVICE_ADDRESS_FIELD, AddressDto.PROPERTY_NAME_NUMBER_FIELD), validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenSameAddressFlagIsTrueWhenServiceAddressNotEmpty() {
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setServiceAddressSameAsPrincipalAddress(true);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setServiceAddress(AddressMock.getAddressDto());
        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD,
                BeneficialOwnerGovernmentOrPublicAuthorityDto.SERVICE_ADDRESS_FIELD);

        String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.PROPERTY_NAME_NUMBER_FIELD));
        assertError(getQualifiedFieldName(BeneficialOwnerGovernmentOrPublicAuthorityDto.SERVICE_ADDRESS_FIELD, AddressDto.PROPERTY_NAME_NUMBER_FIELD), validationMessage, errors);
        validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.LINE_1_FIELD));
        assertError(getQualifiedFieldName(BeneficialOwnerGovernmentOrPublicAuthorityDto.SERVICE_ADDRESS_FIELD, AddressDto.LINE_1_FIELD), validationMessage, errors);
        validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.TOWN_FIELD));
        assertError(getQualifiedFieldName(BeneficialOwnerGovernmentOrPublicAuthorityDto.SERVICE_ADDRESS_FIELD, AddressDto.TOWN_FIELD), validationMessage, errors);
        validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.COUNTY_FIELD));
        assertError(getQualifiedFieldName(BeneficialOwnerGovernmentOrPublicAuthorityDto.SERVICE_ADDRESS_FIELD, AddressDto.COUNTY_FIELD), validationMessage, errors);
        validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.COUNTRY_FIELD));
        assertError(getQualifiedFieldName(BeneficialOwnerGovernmentOrPublicAuthorityDto.SERVICE_ADDRESS_FIELD, AddressDto.COUNTRY_FIELD), validationMessage, errors);
        validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.POSTCODE_FIELD));
        assertError(getQualifiedFieldName(BeneficialOwnerGovernmentOrPublicAuthorityDto.SERVICE_ADDRESS_FIELD, AddressDto.POSTCODE_FIELD), validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLegalFormFieldIsEmpty() {
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setLegalForm("  ");
        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD,
                BeneficialOwnerGovernmentOrPublicAuthorityDto.LEGAL_FORM_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);
        assertError(BeneficialOwnerGovernmentOrPublicAuthorityDto.LEGAL_FORM_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLegalFormFieldIsNull() {
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setLegalForm(null);
        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD,
                BeneficialOwnerGovernmentOrPublicAuthorityDto.LEGAL_FORM_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(BeneficialOwnerGovernmentOrPublicAuthorityDto.LEGAL_FORM_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLegalFormFieldExceedsMaxLength() {
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setLegalForm(StringUtils.repeat("A", 161));
        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD,
                BeneficialOwnerGovernmentOrPublicAuthorityDto.LEGAL_FORM_FIELD);
        String validationMessage = qualifiedFieldName + " must be 160 characters or less";
        assertError(BeneficialOwnerGovernmentOrPublicAuthorityDto.LEGAL_FORM_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLegalFormFieldContainsInvalidCharacters() {
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setLegalForm("Дракон");
        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD,
                BeneficialOwnerGovernmentOrPublicAuthorityDto.LEGAL_FORM_FIELD);
        String validationMessage = String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName);
        assertError(BeneficialOwnerGovernmentOrPublicAuthorityDto.LEGAL_FORM_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLawGovernedFieldIsEmpty() {
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setLawGoverned("  ");
        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD,
                BeneficialOwnerGovernmentOrPublicAuthorityDto.LAW_GOVERNED_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);
        assertError(BeneficialOwnerGovernmentOrPublicAuthorityDto.LAW_GOVERNED_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLawGovernedFieldIsNull() {
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setLawGoverned(null);
        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD,
                BeneficialOwnerGovernmentOrPublicAuthorityDto.LAW_GOVERNED_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(BeneficialOwnerGovernmentOrPublicAuthorityDto.LAW_GOVERNED_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLawGovernedFieldExceedsMaxLength() {
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setLawGoverned(StringUtils.repeat("A", 161));
        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD,
                BeneficialOwnerGovernmentOrPublicAuthorityDto.LAW_GOVERNED_FIELD);
        String validationMessage = qualifiedFieldName + " must be 160 characters or less";
        assertError(BeneficialOwnerGovernmentOrPublicAuthorityDto.LAW_GOVERNED_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLawGovernedFieldContainsInvalidCharacters() {
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setLawGoverned("Дракон");
        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD,
                BeneficialOwnerGovernmentOrPublicAuthorityDto.LAW_GOVERNED_FIELD);
        String validationMessage = String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName);
        assertError(BeneficialOwnerGovernmentOrPublicAuthorityDto.LAW_GOVERNED_FIELD, validationMessage, errors);
    }

    // Changed for UAR-1598 can remove test when FEATURE_FLAG_ENABLE_PROPERTY_OR_LAND_OWNER_NOC is removed
    @Test
    void testErrorReportedWhenNatureOfControlValuesAreAllNullWhenNewNocsFlagIsFalse() {
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setBeneficialOwnerNatureOfControlTypes(null);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setNonLegalFirmMembersNatureOfControlTypes(null);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setTrustControlNatureOfControlTypes(null);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setOwnerOfLandPersonNatureOfControlJurisdictions(null);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setOwnerOfLandOtherEntityNatureOfControlJurisdictions(null);
        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD,
                BeneficialOwnerGovernmentOrPublicAuthorityValidator.NATURE_OF_CONTROL_FIELDS);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);

        assertError(BeneficialOwnerGovernmentOrPublicAuthorityValidator.NATURE_OF_CONTROL_FIELDS, validationMessage, errors);
    }

    // Added for UAR-1598 can remove suffix from test when FEATURE_FLAG_ENABLE_PROPERTY_OR_LAND_OWNER_NOC is removed
    @Test
    void testErrorReportedWhenNatureOfControlValuesAreAllNullWhenNewNocsFlagIsTrue() {
        setNewNocsEnabledFeatureFlag(true);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setBeneficialOwnerNatureOfControlTypes(null);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setNonLegalFirmMembersNatureOfControlTypes(null);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setTrustControlNatureOfControlTypes(null);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setOwnerOfLandPersonNatureOfControlJurisdictions(null);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setOwnerOfLandOtherEntityNatureOfControlJurisdictions(null);
        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD,
                BeneficialOwnerGovernmentOrPublicAuthorityValidator.NATURE_OF_CONTROL_FIELDS);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);

        assertError(BeneficialOwnerGovernmentOrPublicAuthorityValidator.NATURE_OF_CONTROL_FIELDS, validationMessage, errors);

    }

    // Changed for UAR-1598 can remove test when FEATURE_FLAG_ENABLE_PROPERTY_OR_LAND_OWNER_NOC is removed
    @Test
    void testErrorReportedWhenNatureOfControlValuesAreAllEmptyWhenNewNocsFlagIsFalse() {
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setBeneficialOwnerNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setNonLegalFirmMembersNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setTrustControlNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setOwnerOfLandPersonNatureOfControlJurisdictions(new ArrayList<>());
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setOwnerOfLandOtherEntityNatureOfControlJurisdictions(new ArrayList<>());
        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD,
                BeneficialOwnerGovernmentOrPublicAuthorityValidator.NATURE_OF_CONTROL_FIELDS);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);

        assertError(BeneficialOwnerGovernmentOrPublicAuthorityValidator.NATURE_OF_CONTROL_FIELDS, validationMessage, errors);
    }

    // Added for UAR-1598 can remove suffix from test when FEATURE_FLAG_ENABLE_PROPERTY_OR_LAND_OWNER_NOC is removed
    @Test
    void testErrorReportedWhenNatureOfControlValuesAreAllEmptyWhenNewNocsFlagIsTrue() {
        setNewNocsEnabledFeatureFlag(true);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setBeneficialOwnerNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setNonLegalFirmMembersNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setTrustControlNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setOwnerOfLandPersonNatureOfControlJurisdictions(new ArrayList<>());
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setOwnerOfLandOtherEntityNatureOfControlJurisdictions(new ArrayList<>());
        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD,
                BeneficialOwnerGovernmentOrPublicAuthorityValidator.NATURE_OF_CONTROL_FIELDS);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);

        assertError(BeneficialOwnerGovernmentOrPublicAuthorityValidator.NATURE_OF_CONTROL_FIELDS, validationMessage, errors);
    }

    /*
      When FEATURE_FLAG_ENABLE_PROPERTY_OR_LAND_OWNER_NOC_30082024 is removed choose this or one of the three below
      do not need to keep all 4
      Changed for UAR-1598 can keep when FEATURE_FLAG_ENABLE_PROPERTY_OR_LAND_OWNER_NOC_30082024 is removed
      but will need to remove the word "Existing" end flag suffix and setFeatureFlag
     */
    @Test
    void testNoErrorReportedWhenExistingNocPresentButOtherNatureOfControlValuesAreAllEmptyAndNullWhenNewNocsFlagIsFalse() {
        setNewNocsEnabledFeatureFlag(false);

        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setBeneficialOwnerNatureOfControlTypes(new ArrayList<>());
        List<NatureOfControlType> nonLegalNoc = new ArrayList<>();
        nonLegalNoc.add(NatureOfControlType.OVER_25_PERCENT_OF_SHARES);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setNonLegalFirmMembersNatureOfControlTypes(nonLegalNoc);

        // New NOCs
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setTrustControlNatureOfControlTypes(null);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setOwnerOfLandPersonNatureOfControlJurisdictions(new ArrayList<>());
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setOwnerOfLandOtherEntityNatureOfControlJurisdictions(new ArrayList<>());

        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    /*
      Changed for UAR-1598 can keep when FEATURE_FLAG_ENABLE_PROPERTY_OR_LAND_OWNER_NOC_30082024 is removed
      but will need to remove the word "Existing" end flag suffix and setFeatureFlag
    */
    @Test
    void testNoErrorReportedWhenExistingNocPresentButOtherNatureOfControlValuesAreAllEmptyAndNullWhenNewNocsFlagIsTrue() {
        setNewNocsEnabledFeatureFlag(true);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setBeneficialOwnerNatureOfControlTypes(new ArrayList<>());
        List<NatureOfControlType> nonLegalNoc = new ArrayList<>();
        nonLegalNoc.add(NatureOfControlType.OVER_25_PERCENT_OF_SHARES);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setNonLegalFirmMembersNatureOfControlTypes(nonLegalNoc);

        // New NOCs
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setTrustControlNatureOfControlTypes(null);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setOwnerOfLandPersonNatureOfControlJurisdictions(new ArrayList<>());
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setOwnerOfLandOtherEntityNatureOfControlJurisdictions(new ArrayList<>());

        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    /*
      Added for UAR-1598 can keep when FEATURE_FLAG_ENABLE_PROPERTY_OR_LAND_OWNER_NOC_30082024 is removed
      but will need to remove the word "Existing" end flag suffix and setFeatureFlag
    */
    @Test
    void testErrorReportedWhenNewNocPresentButOtherNatureOfControlValuesAreAllEmptyAndNullWhenNewNocsFlagIsFalse() {
        setNewNocsEnabledFeatureFlag(false);

        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setBeneficialOwnerNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setNonLegalFirmMembersNatureOfControlTypes(null);

        // New NOCs
        List<NatureOfControlType> trustNoc = new ArrayList<>();
        trustNoc.add(NatureOfControlType.OVER_25_PERCENT_OF_SHARES);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setTrustControlNatureOfControlTypes(trustNoc); // New NOC present
        List<NatureOfControlJurisdictionType> jurisdictionNoc = new ArrayList<>();
        jurisdictionNoc.add(NatureOfControlJurisdictionType.ENGLAND_AND_WALES);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setOwnerOfLandPersonNatureOfControlJurisdictions(jurisdictionNoc); // New jurisdiction NOC present
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setOwnerOfLandOtherEntityNatureOfControlJurisdictions(new ArrayList<>());

        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD,
                BeneficialOwnerIndividualValidator.NATURE_OF_CONTROL_FIELDS);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(beneficialOwnerGovernmentOrPublicAuthorityValidator.NATURE_OF_CONTROL_FIELDS, validationMessage, errors);
    }

    /*
      Added for UAR-1598 can keep when FEATURE_FLAG_ENABLE_PROPERTY_OR_LAND_OWNER_NOC_30082024 is removed
      but will need to remove the word "Existing" end flag suffix and setFeatureFlag
     */
    @Test
    void testNoErrorReportedWhenNewNocPresentButOtherNatureOfControlValuesAreAllEmptyAndNullWhenNewNocsFlagIsTrue() {
        setNewNocsEnabledFeatureFlag(true);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setNonLegalFirmControlNatureOfControlTypes(null);

        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setBeneficialOwnerNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setNonLegalFirmMembersNatureOfControlTypes(null);

        // New NOCs
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setNonLegalFirmControlNatureOfControlTypes(new ArrayList<>());
        List<NatureOfControlType> trustNoc = new ArrayList<>();
        trustNoc.add(NatureOfControlType.OVER_25_PERCENT_OF_SHARES);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setTrustControlNatureOfControlTypes(trustNoc); // New NOC present
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setOwnerOfLandPersonNatureOfControlJurisdictions(new ArrayList<>());
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setOwnerOfLandOtherEntityNatureOfControlJurisdictions(new ArrayList<>());

        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    /*
     Added for UAR-1598 can keep when FEATURE_FLAG_ENABLE_PROPERTY_OR_LAND_OWNER_NOC_30082024 is removed
     but will need to remove the word "Existing" end flag suffix and setFeatureFlag
    */
    @Test
    void testNoErrorReportedWhenNewJurisdictionNocPresentButOtherNatureOfControlValuesAreAllEmptyAndNullWhenNewNocsFlagIsTrue() {
        setNewNocsEnabledFeatureFlag(true);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setNonLegalFirmMembersNatureOfControlTypes(null);

        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setBeneficialOwnerNatureOfControlTypes(new ArrayList<>());

        // New NOCs
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setNonLegalFirmControlNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setTrustControlNatureOfControlTypes(null);
        List<NatureOfControlJurisdictionType> jurisdictionNoc = new ArrayList<>();
        jurisdictionNoc.add(NatureOfControlJurisdictionType.ENGLAND_AND_WALES);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setOwnerOfLandPersonNatureOfControlJurisdictions(jurisdictionNoc);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setOwnerOfLandOtherEntityNatureOfControlJurisdictions(new ArrayList<>());

        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenSomeNatureOfControlValuesAreAllEmptyAndNull() {
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setBeneficialOwnerNatureOfControlTypes(new ArrayList<>());
        List<NatureOfControlType> nonLegalNoc = new ArrayList<>();
        nonLegalNoc.add(NatureOfControlType.OVER_25_PERCENT_OF_SHARES);
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setNonLegalFirmMembersNatureOfControlTypes(nonLegalNoc);
       
        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }   

    @Test
    void testErrorReportedWhenStartFieldIsInThePast() {
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setStartDate(LocalDate.of(1970,1, 1));
        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenStartFieldIsNow() {
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setStartDate(LocalDate.now());
        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenStartDateIsInTheFuture() {
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setStartDate(LocalDate.now().plusDays(1));
        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD,
                BeneficialOwnerGovernmentOrPublicAuthorityDto.START_DATE_FIELD);
        String validationMessage = String.format(ValidationMessages.DATE_NOT_IN_PAST_ERROR_MESSAGE, qualifiedFieldName);

        assertError(BeneficialOwnerGovernmentOrPublicAuthorityDto.START_DATE_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenStartDateIsNull() {
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setStartDate(null);
        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD,
                BeneficialOwnerGovernmentOrPublicAuthorityDto.START_DATE_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);

        assertError(BeneficialOwnerGovernmentOrPublicAuthorityDto.START_DATE_FIELD, validationMessage, errors);
    }

    @Test
    void testNoErrorsWhenCeasedDateIsAfterStartDate() {
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setStartDate(LocalDate.now().minusDays(2));
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setCeasedDate(LocalDate.now().minusDays(1));
        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorsWhenCeasedDateFieldIsToday() {
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setCeasedDate(LocalDate.now());
        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorWhenCeasedDateIsBeforeStartDate() {
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setStartDate(LocalDate.now().minusDays(1));
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setCeasedDate(LocalDate.now().minusDays(2));
        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD,
                BeneficialOwnerGovernmentOrPublicAuthorityDto.CEASED_DATE_FIELD);
        String validationMessage = String.format(ValidationMessages.CEASED_DATE_BEFORE_START_DATE_ERROR_MESSAGE, qualifiedFieldName);

        assertError(BeneficialOwnerGovernmentOrPublicAuthorityDto.CEASED_DATE_FIELD, validationMessage, errors);
    }

    @Test
    void testNoErrorWhenCeasedDateIsSameAsStartDate() {
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setStartDate(LocalDate.now().minusDays(1));
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setCeasedDate(LocalDate.now().minusDays(1));
        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenOnSanctionListFieldIsNull() {
        beneficialOwnerGovernmentOrPublicAuthorityDtoList.get(0).setOnSanctionsList(null);
        Errors errors = beneficialOwnerGovernmentOrPublicAuthorityValidator.validate(beneficialOwnerGovernmentOrPublicAuthorityDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD,
                BeneficialOwnerGovernmentOrPublicAuthorityDto.IS_ON_SANCTIONS_LIST_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(BeneficialOwnerGovernmentOrPublicAuthorityDto.IS_ON_SANCTIONS_LIST_FIELD, validationMessage, errors);
    }

    private void assertError(String fieldName, String message, Errors errors) {
        String qualifiedFieldName = BENEFICIAL_OWNERS_GOVERNMENT_OR_PUBLIC_AUTHORITY_FIELD + "." + fieldName;
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(message).build();
        assertTrue(errors.containsError(err));
    }

    private void setNewNocsEnabledFeatureFlag(boolean value) {
        ReflectionTestUtils.setField(beneficialOwnerGovernmentOrPublicAuthorityValidator, "isPropertyAndLandNocEnabled", value);
    }
}
