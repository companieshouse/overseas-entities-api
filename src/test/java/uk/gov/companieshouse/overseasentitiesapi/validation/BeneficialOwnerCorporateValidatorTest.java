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
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
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

    private static final String LOGGING_CONTEXT = "12345";

    private BeneficialOwnerCorporateValidator beneficialOwnerCorporateValidator;

    @InjectMocks
    private AddressDtoValidator addressDtoValidator;

    private List<BeneficialOwnerCorporateDto> beneficialOwnerCorporateDtoList;

    @BeforeEach
    public void init() {
        beneficialOwnerCorporateValidator = new BeneficialOwnerCorporateValidator(addressDtoValidator);
        beneficialOwnerCorporateDtoList = new ArrayList<>();
        BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto();
        beneficialOwnerCorporateDto.setPrincipalAddress(AddressMock.getAddressDto());
        beneficialOwnerCorporateDto.setServiceAddress(new AddressDto());
        beneficialOwnerCorporateDtoList.add(beneficialOwnerCorporateDto);
    }

    @Test
    void testNoErrorReportedWhenAllFieldsAreCorrect() {
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenNameFieldIsEmpty() {
        beneficialOwnerCorporateDtoList.get(0).setName("  ");
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);
        assertError(BeneficialOwnerCorporateDto.NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNameFieldIsNull() {
        beneficialOwnerCorporateDtoList.get(0).setName(null);
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(BeneficialOwnerCorporateDto.NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenNameFieldIsAtMaxLength() {
        beneficialOwnerCorporateDtoList.get(0).setName(StringUtils.repeat("A", 160));
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenNameFieldExceedsMaxLength() {
        beneficialOwnerCorporateDtoList.get(0).setName(StringUtils.repeat("A", 161));
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.NAME_FIELD);
        String validationMessage = qualifiedFieldName + " must be 160 characters or less";
        assertError(BeneficialOwnerCorporateDto.NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNameFieldContainsInvalidCharacters() {
        beneficialOwnerCorporateDtoList.get(0).setName("Дракон");
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName);
        assertError(BeneficialOwnerCorporateDto.NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenSameAddressFieldIsNull() {
        beneficialOwnerCorporateDtoList.get(0).setServiceAddressSameAsPrincipalAddress(null);
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.IS_SERVICE_ADDRESS_SAME_AS_PRINCIPAL_ADDRESS_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);

        assertError(BeneficialOwnerCorporateDto.IS_SERVICE_ADDRESS_SAME_AS_PRINCIPAL_ADDRESS_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenSameAddressFlagIsFalseWhenServiceAddressIsEmpty() {
        beneficialOwnerCorporateDtoList.get(0).setServiceAddressSameAsPrincipalAddress(false);
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.SERVICE_ADDRESS_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.PROPERTY_NAME_NUMBER_FIELD));
        assertError(getQualifiedFieldName(BeneficialOwnerCorporateDto.SERVICE_ADDRESS_FIELD, AddressDto.PROPERTY_NAME_NUMBER_FIELD), validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenSameAddressFlagIsTrueWhenServiceAddressNotEmpty() {
        beneficialOwnerCorporateDtoList.get(0).setServiceAddressSameAsPrincipalAddress(true);
        beneficialOwnerCorporateDtoList.get(0).setServiceAddress(AddressMock.getAddressDto());
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.SERVICE_ADDRESS_FIELD);

        String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.PROPERTY_NAME_NUMBER_FIELD));
        assertError(getQualifiedFieldName(BeneficialOwnerCorporateDto.SERVICE_ADDRESS_FIELD, AddressDto.PROPERTY_NAME_NUMBER_FIELD), validationMessage, errors);
        validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.LINE_1_FIELD));
        assertError(getQualifiedFieldName(BeneficialOwnerCorporateDto.SERVICE_ADDRESS_FIELD, AddressDto.LINE_1_FIELD), validationMessage, errors);
        validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.TOWN_FIELD));
        assertError(getQualifiedFieldName(BeneficialOwnerCorporateDto.SERVICE_ADDRESS_FIELD, AddressDto.TOWN_FIELD), validationMessage, errors);
        validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.COUNTY_FIELD));
        assertError(getQualifiedFieldName(BeneficialOwnerCorporateDto.SERVICE_ADDRESS_FIELD, AddressDto.COUNTY_FIELD), validationMessage, errors);
        validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.COUNTRY_FIELD));
        assertError(getQualifiedFieldName(BeneficialOwnerCorporateDto.SERVICE_ADDRESS_FIELD, AddressDto.COUNTRY_FIELD), validationMessage, errors);
        validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, getQualifiedFieldName(qualifiedFieldName, AddressDto.POSTCODE_FIELD));
        assertError(getQualifiedFieldName(BeneficialOwnerCorporateDto.SERVICE_ADDRESS_FIELD, AddressDto.POSTCODE_FIELD), validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLegalFormFieldIsEmpty() {
        beneficialOwnerCorporateDtoList.get(0).setLegalForm("  ");
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.LEGAL_FORM_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);
        assertError(BeneficialOwnerCorporateDto.LEGAL_FORM_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLegalFormFieldIsNull() {
        beneficialOwnerCorporateDtoList.get(0).setLegalForm(null);
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.LEGAL_FORM_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(BeneficialOwnerCorporateDto.LEGAL_FORM_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLegalFormFieldExceedsMaxLength() {
        beneficialOwnerCorporateDtoList.get(0).setLegalForm(StringUtils.repeat("A", 161));
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.LEGAL_FORM_FIELD);
        String validationMessage = qualifiedFieldName + " must be 160 characters or less";
        assertError(BeneficialOwnerCorporateDto.LEGAL_FORM_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLegalFormFieldContainsInvalidCharacters() {
        beneficialOwnerCorporateDtoList.get(0).setLegalForm("Дракон");
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.LEGAL_FORM_FIELD);
        String validationMessage = String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName);
        assertError(BeneficialOwnerCorporateDto.LEGAL_FORM_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLawGovernedFieldIsEmpty() {
        beneficialOwnerCorporateDtoList.get(0).setLawGoverned("  ");
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.LAW_GOVERNED_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);
        assertError(BeneficialOwnerCorporateDto.LAW_GOVERNED_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLawGovernedFieldIsNull() {
        beneficialOwnerCorporateDtoList.get(0).setLawGoverned(null);
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.LAW_GOVERNED_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(BeneficialOwnerCorporateDto.LAW_GOVERNED_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLawGovernedFieldExceedsMaxLength() {
        beneficialOwnerCorporateDtoList.get(0).setLawGoverned(StringUtils.repeat("A", 161));
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.LAW_GOVERNED_FIELD);
        String validationMessage = qualifiedFieldName + " must be 160 characters or less";
        assertError(BeneficialOwnerCorporateDto.LAW_GOVERNED_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLawGovernedFieldContainsInvalidCharacters() {
        beneficialOwnerCorporateDtoList.get(0).setLawGoverned("Дракон");
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.LAW_GOVERNED_FIELD);
        String validationMessage = String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName);
        assertError(BeneficialOwnerCorporateDto.LAW_GOVERNED_FIELD, validationMessage, errors);
    }

    // Changed for UAR-1597 can remove test when FEATURE_FLAG_ENABLE_PROPERTY_OR_LAND_OWNER_NOC is removed
    @Test
    void testErrorReportedWhenNatureOfControlValuesAreAllNullWhenNewNocsFlagIsFalse() {
        setNewNocsEnabledFeatureFlag(false);
        beneficialOwnerCorporateDtoList.get(0).setBeneficialOwnerNatureOfControlTypes(null);
        beneficialOwnerCorporateDtoList.get(0).setNonLegalFirmMembersNatureOfControlTypes(null);
        beneficialOwnerCorporateDtoList.get(0).setTrusteesNatureOfControlTypes(null);
        beneficialOwnerCorporateDtoList.get(0).setNonLegalFirmControlNatureOfControlTypes(null);
        beneficialOwnerCorporateDtoList.get(0).setTrustControlNatureOfControlTypes(null);
        beneficialOwnerCorporateDtoList.get(0).setOwnerOfLandPersonNatureOfControlJurisdictions(null);
        beneficialOwnerCorporateDtoList.get(0).setOwnerOfLandOtherEntityNatureOfControlJurisdictions(null);
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateValidator.NATURE_OF_CONTROL_FIELDS);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);

        assertError(BeneficialOwnerCorporateValidator.NATURE_OF_CONTROL_FIELDS, validationMessage, errors);
    }

    // Added for UAR-1597 can remove test when FEATURE_FLAG_ENABLE_PROPERTY_OR_LAND_OWNER_NOC is removed
    @Test
    void testErrorReportedWhenNatureOfControlValuesAreAllNullWhenNewNocsFlagIsTrue() {
        setNewNocsEnabledFeatureFlag(true);
        beneficialOwnerCorporateDtoList.get(0).setBeneficialOwnerNatureOfControlTypes(null);
        beneficialOwnerCorporateDtoList.get(0).setNonLegalFirmMembersNatureOfControlTypes(null);
        beneficialOwnerCorporateDtoList.get(0).setTrusteesNatureOfControlTypes(null);
        beneficialOwnerCorporateDtoList.get(0).setNonLegalFirmControlNatureOfControlTypes(null);
        beneficialOwnerCorporateDtoList.get(0).setTrustControlNatureOfControlTypes(null);
        beneficialOwnerCorporateDtoList.get(0).setOwnerOfLandPersonNatureOfControlJurisdictions(null);
        beneficialOwnerCorporateDtoList.get(0).setOwnerOfLandOtherEntityNatureOfControlJurisdictions(null);
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateValidator.NATURE_OF_CONTROL_FIELDS);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);

        assertError(BeneficialOwnerCorporateValidator.NATURE_OF_CONTROL_FIELDS, validationMessage, errors);
    }

    // Changed for UAR-1597 can remove test when FEATURE_FLAG_ENABLE_PROPERTY_OR_LAND_OWNER_NOC is removed
    @Test
    void testErrorReportedWhenNatureOfControlValuesAreAllEmptyWhenNewNocsFlagIsFalse() {
        setNewNocsEnabledFeatureFlag(false);
        beneficialOwnerCorporateDtoList.get(0).setBeneficialOwnerNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerCorporateDtoList.get(0).setNonLegalFirmMembersNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerCorporateDtoList.get(0).setTrusteesNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerCorporateDtoList.get(0).setNonLegalFirmControlNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerCorporateDtoList.get(0).setTrustControlNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerCorporateDtoList.get(0).setOwnerOfLandPersonNatureOfControlJurisdictions(new ArrayList<>());
        beneficialOwnerCorporateDtoList.get(0).setOwnerOfLandOtherEntityNatureOfControlJurisdictions(new ArrayList<>());
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateValidator.NATURE_OF_CONTROL_FIELDS);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);

        assertError(BeneficialOwnerCorporateValidator.NATURE_OF_CONTROL_FIELDS, validationMessage, errors);
    }

    // Added for UAR-1597 can remove test when FEATURE_FLAG_ENABLE_PROPERTY_OR_LAND_OWNER_NOC is removed
    @Test
    void testErrorReportedWhenNatureOfControlValuesAreAllEmptyWhenNewNocsFlagIsTrue() {
        setNewNocsEnabledFeatureFlag(true);
        beneficialOwnerCorporateDtoList.get(0).setBeneficialOwnerNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerCorporateDtoList.get(0).setNonLegalFirmMembersNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerCorporateDtoList.get(0).setTrusteesNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerCorporateDtoList.get(0).setNonLegalFirmControlNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerCorporateDtoList.get(0).setTrustControlNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerCorporateDtoList.get(0).setOwnerOfLandPersonNatureOfControlJurisdictions(new ArrayList<>());
        beneficialOwnerCorporateDtoList.get(0).setOwnerOfLandOtherEntityNatureOfControlJurisdictions(new ArrayList<>());
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateValidator.NATURE_OF_CONTROL_FIELDS);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);

        assertError(BeneficialOwnerCorporateValidator.NATURE_OF_CONTROL_FIELDS, validationMessage, errors);
    }

    /*
       When FEATURE_FLAG_ENABLE_PROPERTY_OR_LAND_OWNER_NOC_30082024 is removed choose this or one of the three below
       do not need to keep all 4
       Changed for UAR-1597 can keep when FEATURE_FLAG_ENABLE_PROPERTY_OR_LAND_OWNER_NOC_30082024 is removed
       but will need to remove the word "Existing" end flag suffix and setFeatureFlag
    */
    @Test
    void testNoErrorReportedWhenExistingNocPresentButOtherNatureOfControlValuesAreAllEmptyAndNullWhenNewNocsFlagIsFalse() {
        setNewNocsEnabledFeatureFlag(false);
        beneficialOwnerCorporateDtoList.get(0).setBeneficialOwnerNatureOfControlTypes(new ArrayList<>());
        List<NatureOfControlType> nonLegalNoc = new ArrayList<>();
        nonLegalNoc.add(NatureOfControlType.OVER_25_PERCENT_OF_SHARES);
        beneficialOwnerCorporateDtoList.get(0).setNonLegalFirmMembersNatureOfControlTypes(nonLegalNoc);
        beneficialOwnerCorporateDtoList.get(0).setTrusteesNatureOfControlTypes(null);

        beneficialOwnerCorporateDtoList.get(0).setNonLegalFirmControlNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerCorporateDtoList.get(0).setTrustControlNatureOfControlTypes(null);
        beneficialOwnerCorporateDtoList.get(0).setOwnerOfLandPersonNatureOfControlJurisdictions(new ArrayList<>());
        beneficialOwnerCorporateDtoList.get(0).setOwnerOfLandOtherEntityNatureOfControlJurisdictions(null);

        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    /*
      When FEATURE_FLAG_ENABLE_PROPERTY_OR_LAND_OWNER_NOC_30082024 is removed choose this or one of the three below
      do not need to keep all 4
      Changed for UAR-1597 can keep when FEATURE_FLAG_ENABLE_PROPERTY_OR_LAND_OWNER_NOC_30082024 is removed
      but will need to remove the word "Existing" end flag suffix and setFeatureFlag
   */
    @Test
    void testNoErrorReportedWhenExistingNocPresentButOtherNatureOfControlValuesAreAllEmptyAndNullWhenNewNocsFlagIsTrue() {
        setNewNocsEnabledFeatureFlag(true);
        beneficialOwnerCorporateDtoList.get(0).setBeneficialOwnerNatureOfControlTypes(new ArrayList<>());
        List<NatureOfControlType> nonLegalNoc = new ArrayList<>();
        nonLegalNoc.add(NatureOfControlType.OVER_25_PERCENT_OF_SHARES);
        beneficialOwnerCorporateDtoList.get(0).setNonLegalFirmMembersNatureOfControlTypes(nonLegalNoc);
        beneficialOwnerCorporateDtoList.get(0).setTrusteesNatureOfControlTypes(null);

        beneficialOwnerCorporateDtoList.get(0).setNonLegalFirmControlNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerCorporateDtoList.get(0).setTrustControlNatureOfControlTypes(null);
        beneficialOwnerCorporateDtoList.get(0).setOwnerOfLandPersonNatureOfControlJurisdictions(new ArrayList<>());
        beneficialOwnerCorporateDtoList.get(0).setOwnerOfLandOtherEntityNatureOfControlJurisdictions(null);

        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    /*
     Added for UAR-1597 can keep when FEATURE_FLAG_ENABLE_PROPERTY_OR_LAND_OWNER_NOC_30082024 is removed
     but will need to remove the word "Existing" end flag suffix and setFeatureFlag
    */
    @Test
    void testErrorReportedWhenNewNocPresentButOtherNatureOfControlValuesAreAllEmptyAndNullWhenNewNocsFlagIsFalse() {
        setNewNocsEnabledFeatureFlag(false);
        beneficialOwnerCorporateDtoList.get(0).setBeneficialOwnerNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerCorporateDtoList.get(0).setNonLegalFirmMembersNatureOfControlTypes(null);
        beneficialOwnerCorporateDtoList.get(0).setTrusteesNatureOfControlTypes(new ArrayList<>());

        beneficialOwnerCorporateDtoList.get(0).setNonLegalFirmControlNatureOfControlTypes(new ArrayList<>());
        List<NatureOfControlType> trustNoc = new ArrayList<>();
        trustNoc.add(NatureOfControlType.OVER_25_PERCENT_OF_SHARES);
        beneficialOwnerCorporateDtoList.get(0).setTrustControlNatureOfControlTypes(trustNoc);
        beneficialOwnerCorporateDtoList.get(0).setOwnerOfLandPersonNatureOfControlJurisdictions(new ArrayList<>());
        beneficialOwnerCorporateDtoList.get(0).setOwnerOfLandOtherEntityNatureOfControlJurisdictions(null);

        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateValidator.NATURE_OF_CONTROL_FIELDS);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);

        assertError(BeneficialOwnerCorporateValidator.NATURE_OF_CONTROL_FIELDS, validationMessage, errors);

    }

    /*
     Added for UAR-1597 can keep when FEATURE_FLAG_ENABLE_PROPERTY_OR_LAND_OWNER_NOC_30082024 is removed
     but will need to remove the word "Existing" end flag suffix and setFeatureFlag
    */
    @Test
    void testNoErrorReportedWhenNewNocPresentButOtherNatureOfControlValuesAreAllEmptyAndNullWhenNewNocsFlagIsTrue() {
        setNewNocsEnabledFeatureFlag(true);

        beneficialOwnerCorporateDtoList.get(0).setBeneficialOwnerNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerCorporateDtoList.get(0).setNonLegalFirmMembersNatureOfControlTypes(null);
        beneficialOwnerCorporateDtoList.get(0).setTrusteesNatureOfControlTypes(new ArrayList<>());

        beneficialOwnerCorporateDtoList.get(0).setNonLegalFirmControlNatureOfControlTypes(new ArrayList<>());
        List<NatureOfControlType> trustNoc = new ArrayList<>();
        trustNoc.add(NatureOfControlType.OVER_25_PERCENT_OF_SHARES);
        beneficialOwnerCorporateDtoList.get(0).setTrustControlNatureOfControlTypes(trustNoc);
        beneficialOwnerCorporateDtoList.get(0).setOwnerOfLandPersonNatureOfControlJurisdictions(new ArrayList<>());
        beneficialOwnerCorporateDtoList.get(0).setOwnerOfLandOtherEntityNatureOfControlJurisdictions(null);

        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    /*
     Added for UAR-1597 can keep when FEATURE_FLAG_ENABLE_PROPERTY_OR_LAND_OWNER_NOC_30082024 is removed
     but will need to remove the word "Existing" end flag suffix and setFeatureFlag
   */
    @Test
    void testNoErrorReportedWhenNewJurisdictionNocPresentButOtherNatureOfControlValuesAreAllEmptyAndNullWhenNewNocsFlagIsTrue() {
        setNewNocsEnabledFeatureFlag(true);

        beneficialOwnerCorporateDtoList.get(0).setBeneficialOwnerNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerCorporateDtoList.get(0).setNonLegalFirmMembersNatureOfControlTypes(null);
        beneficialOwnerCorporateDtoList.get(0).setTrusteesNatureOfControlTypes(new ArrayList<>());

        beneficialOwnerCorporateDtoList.get(0).setNonLegalFirmControlNatureOfControlTypes(new ArrayList<>());
        beneficialOwnerCorporateDtoList.get(0).setTrustControlNatureOfControlTypes(null);
        beneficialOwnerCorporateDtoList.get(0).setOwnerOfLandPersonNatureOfControlJurisdictions(new ArrayList<>());
        List<NatureOfControlJurisdictionType> jurisdictionOtherNoc = new ArrayList<>();
        jurisdictionOtherNoc.add(NatureOfControlJurisdictionType.ENGLAND_AND_WALES);
        beneficialOwnerCorporateDtoList.get(0).setOwnerOfLandOtherEntityNatureOfControlJurisdictions(jurisdictionOtherNoc);

        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenIsOnPublicRegisterNameFieldIsNull() {
        beneficialOwnerCorporateDtoList.get(0).setOnRegisterInCountryFormedIn(null);
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.IS_ON_REGISTER_IN_COUNTRY_FORMED_IN_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(BeneficialOwnerCorporateDto.IS_ON_REGISTER_IN_COUNTRY_FORMED_IN_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenPublicRegisterNameFieldIsEmpty() {
        beneficialOwnerCorporateDtoList.get(0).setOnRegisterInCountryFormedIn(Boolean.TRUE);
        beneficialOwnerCorporateDtoList.get(0).setPublicRegisterName("  ");
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.PUBLIC_REGISTER_NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);
        assertError(BeneficialOwnerCorporateDto.PUBLIC_REGISTER_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenPublicRegisterNameFieldIsNull() {
        beneficialOwnerCorporateDtoList.get(0).setOnRegisterInCountryFormedIn(Boolean.TRUE);
        beneficialOwnerCorporateDtoList.get(0).setPublicRegisterName(null);
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.PUBLIC_REGISTER_NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(BeneficialOwnerCorporateDto.PUBLIC_REGISTER_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenPublicRegisterNameFieldExceedsMaxLength() {
        beneficialOwnerCorporateDtoList.get(0).setOnRegisterInCountryFormedIn(Boolean.TRUE);
        beneficialOwnerCorporateDtoList.get(0).setPublicRegisterName(StringUtils.repeat("A", 161));
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.PUBLIC_REGISTER_NAME_FIELD);
        String validationMessage = qualifiedFieldName + " must be 160 characters or less";
        assertError(BeneficialOwnerCorporateDto.PUBLIC_REGISTER_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenPublicRegisterNameFieldContainsInvalidCharacters() {
        beneficialOwnerCorporateDtoList.get(0).setOnRegisterInCountryFormedIn(Boolean.TRUE);
        beneficialOwnerCorporateDtoList.get(0).setPublicRegisterName("Дракон");
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.PUBLIC_REGISTER_NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName);
        assertError(BeneficialOwnerCorporateDto.PUBLIC_REGISTER_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenRegistrationNumberFieldIsEmpty() {
        beneficialOwnerCorporateDtoList.get(0).setOnRegisterInCountryFormedIn(Boolean.TRUE);
        beneficialOwnerCorporateDtoList.get(0).setRegistrationNumber("  ");
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.REGISTRATION_NUMBER_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);
        assertError(BeneficialOwnerCorporateDto.REGISTRATION_NUMBER_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenRegistrationNumberFieldIsNull() {
        beneficialOwnerCorporateDtoList.get(0).setOnRegisterInCountryFormedIn(Boolean.TRUE);
        beneficialOwnerCorporateDtoList.get(0).setRegistrationNumber(null);
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.REGISTRATION_NUMBER_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(BeneficialOwnerCorporateDto.REGISTRATION_NUMBER_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenRegistrationNumberFieldExceedsMaxLength() {
        beneficialOwnerCorporateDtoList.get(0).setOnRegisterInCountryFormedIn(Boolean.TRUE);
        beneficialOwnerCorporateDtoList.get(0).setRegistrationNumber(StringUtils.repeat("A", 161));
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.REGISTRATION_NUMBER_FIELD);
        String validationMessage = qualifiedFieldName + " must be 160 characters or less";
        assertError(BeneficialOwnerCorporateDto.REGISTRATION_NUMBER_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenRegistrationNumberFieldContainsInvalidCharacters() {
        beneficialOwnerCorporateDtoList.get(0).setOnRegisterInCountryFormedIn(Boolean.TRUE);
        beneficialOwnerCorporateDtoList.get(0).setRegistrationNumber("Дракон");
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.REGISTRATION_NUMBER_FIELD);
        String validationMessage = String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName);
        assertError(BeneficialOwnerCorporateDto.REGISTRATION_NUMBER_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenOnRegisterFlagIsFalseWhenPublicRegisterNameFieldNotEmpty() {
        beneficialOwnerCorporateDtoList.get(0).setOnRegisterInCountryFormedIn(Boolean.FALSE);
        beneficialOwnerCorporateDtoList.get(0).setPublicRegisterName("Name");
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.PUBLIC_REGISTER_NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, qualifiedFieldName);
        assertError(BeneficialOwnerCorporateDto.PUBLIC_REGISTER_NAME_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenOnRegisterFlagIsFalseWhenRegistrationNumberFieldNotEmpty() {
        beneficialOwnerCorporateDtoList.get(0).setOnRegisterInCountryFormedIn(Boolean.FALSE);
        beneficialOwnerCorporateDtoList.get(0).setRegistrationNumber("123456");
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.REGISTRATION_NUMBER_FIELD);
        String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, qualifiedFieldName);
        assertError(BeneficialOwnerCorporateDto.REGISTRATION_NUMBER_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenStartFieldIsInThePast() {
        beneficialOwnerCorporateDtoList.get(0).setStartDate(LocalDate.of(1970,1, 1));
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenStartFieldIsNow() {
        beneficialOwnerCorporateDtoList.get(0).setStartDate(LocalDate.now());
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenStartDateIsInTheFuture() {
        beneficialOwnerCorporateDtoList.get(0).setStartDate(LocalDate.now().plusDays(1));
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.START_DATE_FIELD);
        String validationMessage = String.format(ValidationMessages.DATE_NOT_IN_PAST_ERROR_MESSAGE, qualifiedFieldName);

        assertError(BeneficialOwnerCorporateDto.START_DATE_FIELD, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenStartDateIsNull() {
        beneficialOwnerCorporateDtoList.get(0).setStartDate(null);
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.START_DATE_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);

        assertError(BeneficialOwnerCorporateDto.START_DATE_FIELD, validationMessage, errors);
    }

    @Test
    void testNoErrorsWhenCeasedDateFieldIsInThePast() {
        beneficialOwnerCorporateDtoList.get(0).setStartDate(LocalDate.now().minusDays(2));
        beneficialOwnerCorporateDtoList.get(0).setCeasedDate(LocalDate.now().minusDays(1));
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorsWhenCeasedDateFieldIsNow() {
        beneficialOwnerCorporateDtoList.get(0).setCeasedDate(LocalDate.now());
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenCeasedDateIsBeforeStartDate() {
        beneficialOwnerCorporateDtoList.get(0).setStartDate(LocalDate.now().plusDays(1));
        beneficialOwnerCorporateDtoList.get(0).setCeasedDate(LocalDate.now());
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.CEASED_DATE_FIELD);
        String validationMessage = String.format(ValidationMessages.CEASED_DATE_BEFORE_START_DATE_ERROR_MESSAGE, qualifiedFieldName);

        assertError(BeneficialOwnerCorporateDto.CEASED_DATE_FIELD, validationMessage, errors);
    }

    @Test
    void testNoErrorWhenCeasedDateIsSameAsStartDate() {
        beneficialOwnerCorporateDtoList.get(0).setStartDate(LocalDate.now().minusDays(1));
        beneficialOwnerCorporateDtoList.get(0).setCeasedDate(LocalDate.now().minusDays(1));
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenOnSanctionListFieldIsNull() {
        beneficialOwnerCorporateDtoList.get(0).setOnSanctionsList(null);
        Errors errors = beneficialOwnerCorporateValidator.validate(beneficialOwnerCorporateDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(
                BENEFICIAL_OWNERS_CORPORATE_FIELD,
                BeneficialOwnerCorporateDto.IS_ON_SANCTIONS_LIST_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(BeneficialOwnerCorporateDto.IS_ON_SANCTIONS_LIST_FIELD, validationMessage, errors);
    }

    private void assertError(String fieldName, String message, Errors errors) {
        String qualifiedFieldName = BENEFICIAL_OWNERS_CORPORATE_FIELD + "." + fieldName;
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(message).build();
        assertTrue(errors.containsError(err));
    }

    private void setNewNocsEnabledFeatureFlag(boolean value) {
        ReflectionTestUtils.setField(beneficialOwnerCorporateValidator, "isPropertyAndLandNocEnabled", value);
    }
}
