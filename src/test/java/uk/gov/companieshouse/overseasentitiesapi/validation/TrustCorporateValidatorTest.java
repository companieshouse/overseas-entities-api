package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.overseasentitiesapi.mocks.TrustMock;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.BeneficialOwnerType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustDataDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.companieshouse.overseasentitiesapi.validation.TrustCorporateValidator.PARENT_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
class TrustCorporateValidatorTest {

    private static final String LOGGING_CONTEXT = "12345";

    @Mock
    private AddressDtoValidator addressDtoValidator;

    private TrustCorporateValidator trustCorporateValidator;
    private List<TrustDataDto> trustDataDtoList;

    @BeforeEach
    public void init() {
        trustCorporateValidator = new TrustCorporateValidator(addressDtoValidator);
        trustDataDtoList = new ArrayList<>();

        TrustDataDto trustDataDto = TrustMock.getTrustDataDto();
        trustDataDtoList.add(trustDataDto);
    }

    @Test
    void testNoErrorReportedWhenAllFieldsAreCorrect() {
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenNameFieldIsEmpty() {
        trustDataDtoList.get(0).getCorporates().get(0).setName("  ");
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNameFieldIsNull() {
        trustDataDtoList.get(0).getCorporates().get(0).setName(null);
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenNameFieldIsAtMaxLength() {
        trustDataDtoList.get(0).getCorporates().get(0).setName(StringUtils.repeat("A", 160));

        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenNameFieldExceedsMaxLength() {
        trustDataDtoList.get(0).getCorporates().get(0).setName(StringUtils.repeat("A", 161));
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.NAME_FIELD);
        String validationMessage = qualifiedFieldName +
                String.format(ValidationMessages.MAX_LENGTH_EXCEEDED_ERROR_MESSAGE, 160);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNameFieldContainsInvalidCharacters() {
        trustDataDtoList.get(0).getCorporates().get(0).setName("Дракон");
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenTypeFieldIsNull() {
        trustDataDtoList.get(0).getCorporates().get(0).setType(null);
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.TYPE_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenTypeFieldIsEmpty() {
        trustDataDtoList.get(0).getCorporates().get(0).setType(" ");
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.TYPE_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenTypeFieldValueIsNotExpected() {
        trustDataDtoList.get(0).getCorporates().get(0).setType("TESTY");
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.TYPE_FIELD);
        String validationMessage = ValidationMessages.TRUST_CORPORATE_TYPE_ERROR_MESSAGE.replace("%s",
                qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenTypeFieldValueIsExpected() {
        trustDataDtoList.get(0).getCorporates().get(0).setType(BeneficialOwnerType.BENEFICIARY.getValue());
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenDateBecameInterestedPersonFieldIsNull() {
        trustDataDtoList.get(0).getCorporates().get(0).setType(BeneficialOwnerType.INTERESTED_PERSON.getValue());
        trustDataDtoList.get(0).getCorporates().get(0).setDateBecameInterestedPerson(null);
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                TrustCorporateDto.DATE_BECAME_INTERESTED_PERSON_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenDateBecameInterestedPersonFieldIsInThePast() {
        trustDataDtoList.get(0).getCorporates().get(0).setDateBecameInterestedPerson(LocalDate.of(1970, 1, 1));
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenDateBecameInterestedPersonIsInTheFuture() {
        trustDataDtoList.get(0).getCorporates().get(0).setType(BeneficialOwnerType.INTERESTED_PERSON.getValue());
        trustDataDtoList.get(0).getCorporates().get(0).setDateBecameInterestedPerson(LocalDate.now().plusDays(1));
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                TrustCorporateDto.DATE_BECAME_INTERESTED_PERSON_FIELD);
        String validationMessage = ValidationMessages.DATE_NOT_IN_PAST_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenRegisteredOfficeAddressFieldIsNull() {
        trustDataDtoList.get(0).getCorporates().get(0).setRegisteredOfficeAddress(null);

        when(addressDtoValidator.validate(any(), any(), any(), any(), any())).thenCallRealMethod();
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.REGISTERED_OFFICE_ADDRESS_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenServiceAddressSameAsUsualResidentialAddressFieldIsNull() {
        trustDataDtoList.get(0).getCorporates().get(0).setServiceAddressSameAsPrincipalAddress(null);
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IS_SERVICE_ADDRESS_SAME_AS_PRINCIPAL_ADDRESS_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLegalFormIsNull() {
        trustDataDtoList.get(0).getCorporates().get(0).setIdentificationLegalForm(null);
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_LEGAL_FORM_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLegalFormIsEmpty() {
        trustDataDtoList.get(0).getCorporates().get(0).setIdentificationLegalForm("  ");
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_LEGAL_FORM_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLegalFormExceedsMaxLength() {
        trustDataDtoList.get(0).getCorporates().get(0).setIdentificationLegalForm(StringUtils.repeat("A", 161));
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_LEGAL_FORM_FIELD);
        String validationMessage = qualifiedFieldName +
                String.format(ValidationMessages.MAX_LENGTH_EXCEEDED_ERROR_MESSAGE, 160);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenLegalFormIsAtMaxLength() {
        trustDataDtoList.get(0).getCorporates().get(0).setIdentificationLegalForm(StringUtils.repeat("A", 160));

        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenLegalFormContainsInvalidCharacters() {
        trustDataDtoList.get(0).getCorporates().get(0).setIdentificationLegalForm("Дракон");
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_LEGAL_FORM_FIELD);
        String validationMessage = String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLegalAuthorityIsNull() {
        trustDataDtoList.get(0).getCorporates().get(0).setIdentificationLegalForm(null);
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_LEGAL_FORM_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLegalAuthorityIsEmpty() {
        trustDataDtoList.get(0).getCorporates().get(0).setIdentificationLegalForm("  ");
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_LEGAL_FORM_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLegalAuthorityExceedsMaxLength() {
        trustDataDtoList.get(0).getCorporates().get(0).setIdentificationLegalForm(StringUtils.repeat("A", 161));
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_LEGAL_FORM_FIELD);
        String validationMessage = qualifiedFieldName +
                String.format(ValidationMessages.MAX_LENGTH_EXCEEDED_ERROR_MESSAGE, 160);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenLegalAuthorityIsAtMaxLength() {
        trustDataDtoList.get(0).getCorporates().get(0).setIdentificationLegalForm(StringUtils.repeat("A", 160));

        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenLegalAuthorityContainsInvalidCharacters() {
        trustDataDtoList.get(0).getCorporates().get(0).setIdentificationLegalForm("Дракон");
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_LEGAL_FORM_FIELD);
        String validationMessage = String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedPlaceRegisteredIsNull() {
        trustDataDtoList.get(0).getCorporates().get(0).setIdentificationPlaceRegistered(null);
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_PLACE_REGISTERED_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedPlaceRegisteredIsEmpty() {
        trustDataDtoList.get(0).getCorporates().get(0).setIdentificationPlaceRegistered("  ");
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_PLACE_REGISTERED_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedPlaceRegisteredExceedsMaxLength() {
        trustDataDtoList.get(0).getCorporates().get(0).setIdentificationPlaceRegistered(StringUtils.repeat("A", 161));
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_PLACE_REGISTERED_FIELD);
        String validationMessage = qualifiedFieldName +
                String.format(ValidationMessages.MAX_LENGTH_EXCEEDED_ERROR_MESSAGE, 160);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedPlaceRegisteredIsAtMaxLength() {
        trustDataDtoList.get(0).getCorporates().get(0).setIdentificationPlaceRegistered(StringUtils.repeat("A", 160));

        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedPlaceRegisteredContainsInvalidCharacters() {
        trustDataDtoList.get(0).getCorporates().get(0).setIdentificationPlaceRegistered("Дракон");
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_PLACE_REGISTERED_FIELD);
        String validationMessage = String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedCountryRegistrationIsNull() {
        trustDataDtoList.get(0).getCorporates().get(0).setIdentificationCountryRegistration(null);
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_COUNTRY_REGISTRATION_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedCountryRegistrationIsEmpty() {
        trustDataDtoList.get(0).getCorporates().get(0).setIdentificationCountryRegistration("  ");
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_COUNTRY_REGISTRATION_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedCountryRegistrationExceedsMaxLength() {
        trustDataDtoList.get(0).getCorporates().get(0).setIdentificationCountryRegistration(StringUtils.repeat("A", 161));
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_COUNTRY_REGISTRATION_FIELD);
        String validationMessage = qualifiedFieldName +
                String.format(ValidationMessages.MAX_LENGTH_EXCEEDED_ERROR_MESSAGE, 160);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedCountryRegistrationIsAtMaxLength() {
        trustDataDtoList.get(0).getCorporates().get(0).setIdentificationCountryRegistration(StringUtils.repeat("A", 160));

        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedCountryRegistrationContainsInvalidCharacters() {
        trustDataDtoList.get(0).getCorporates().get(0).setIdentificationCountryRegistration("Дракон");
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_COUNTRY_REGISTRATION_FIELD);
        String validationMessage = String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorRegistrationNumberIsNull() {
        trustDataDtoList.get(0).getCorporates().get(0).setIdentificationRegistrationNumber(null);
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_REGISTRATION_NUMBER_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorRegistrationNumberIsEmpty() {
        trustDataDtoList.get(0).getCorporates().get(0).setIdentificationRegistrationNumber("  ");
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_REGISTRATION_NUMBER_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorRegistrationNumberExceedsMaxLength() {
        trustDataDtoList.get(0).getCorporates().get(0).setIdentificationRegistrationNumber(StringUtils.repeat("A", 161));
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_REGISTRATION_NUMBER_FIELD);
        String validationMessage = qualifiedFieldName +
                String.format(ValidationMessages.MAX_LENGTH_EXCEEDED_ERROR_MESSAGE, 160);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorRegistrationNumberIsAtMaxLength() {
        trustDataDtoList.get(0).getCorporates().get(0).setIdentificationRegistrationNumber(StringUtils.repeat("A", 160));

        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorRegistrationNumberContainsInvalidCharacters() {
        trustDataDtoList.get(0).getCorporates().get(0).setIdentificationRegistrationNumber("Дракон");
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_REGISTRATION_NUMBER_FIELD);
        String validationMessage = String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorRegisteredInCountryFormedInNotSupplied() {
        trustDataDtoList.get(0).getCorporates().get(0).setOnRegisterInCountryFormedIn(Boolean.FALSE);
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_PLACE_REGISTERED_FIELD);
        String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorRegisteredInCountryFormedInSupplied() {
        trustDataDtoList.get(0).getCorporates().get(0).setOnRegisterInCountryFormedIn(Boolean.TRUE);
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    private void assertError(String qualifiedFieldName, String message, Errors errors) {
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(message).build();

        assertTrue(errors.containsError(err));
    }
}
