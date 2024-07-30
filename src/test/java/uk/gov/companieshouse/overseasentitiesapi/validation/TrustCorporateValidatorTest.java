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

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    private TrustDataDto trustDataDto;

    @BeforeEach
    public void init() {
        trustCorporateValidator = new TrustCorporateValidator(addressDtoValidator);
        trustDataDtoList = new ArrayList<>();

        trustDataDto = TrustMock.getTrustDataDto();
        trustDataDtoList.add(trustDataDto);
    }

    @Test
    void testNoErrorReportedWhenAllFieldsAreCorrect() {
        Errors errors = trustCorporateValidator.validate(trustDataDtoList,  true, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenNameFieldIsEmpty() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setName("  ");
        Errors errors = trustCorporateValidator.validate(trustDataDtoList,  true, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNameFieldIsNull() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setName(null);
        Errors errors = trustCorporateValidator.validate(trustDataDtoList,  true, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenNameFieldIsAtMaxLength() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setName(StringUtils.repeat("A", 160));

        Errors errors = trustCorporateValidator.validate(trustDataDtoList,  true, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenNameFieldExceedsMaxLength() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setName(StringUtils.repeat("A", 161));
        Errors errors = trustCorporateValidator.validate(trustDataDtoList,  true, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.NAME_FIELD);
        String validationMessage = qualifiedFieldName +
                String.format(ValidationMessages.MAX_LENGTH_EXCEEDED_ERROR_MESSAGE, 160);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenNameFieldContainsInvalidCharacters() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setName("Дракон");
        Errors errors = trustCorporateValidator.validate(trustDataDtoList,  true, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.NAME_FIELD);
        String validationMessage = String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenTypeFieldIsNull() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setType(null);
        Errors errors = trustCorporateValidator.validate(trustDataDtoList,  true, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.TYPE_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenTypeFieldIsEmpty() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setType(" ");
        Errors errors = trustCorporateValidator.validate(trustDataDtoList,  true, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.TYPE_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenTypeFieldValueIsNotExpected() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setType("TESTY");
        Errors errors = trustCorporateValidator.validate(trustDataDtoList,  true, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.TYPE_FIELD);
        String validationMessage = ValidationMessages.TRUST_CORPORATE_TYPE_ERROR_MESSAGE.replace("%s",
                qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenTypeFieldValueIsExpected() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setType(BeneficialOwnerType.BENEFICIARY.getValue());
        Errors errors = trustCorporateValidator.validate(trustDataDtoList,  true, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenDateBecameInterestedPersonFieldIsNull() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setType(BeneficialOwnerType.INTERESTED_PERSON.getValue());
        trustDataDtoList.getFirst().getCorporates().getFirst().setDateBecameInterestedPerson(null);
        Errors errors = trustCorporateValidator.validate(trustDataDtoList,  true, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                TrustCorporateDto.DATE_BECAME_INTERESTED_PERSON_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenDateBecameInterestedPersonFieldIsInThePast() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setDateBecameInterestedPerson(LocalDate.of(1970, 1, 1));
        Errors errors = trustCorporateValidator.validate(trustDataDtoList,  true, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenDateBecameInterestedPersonIsInTheFuture() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setType(BeneficialOwnerType.INTERESTED_PERSON.getValue());
        trustDataDtoList.getFirst().getCorporates().getFirst().setDateBecameInterestedPerson(LocalDate.now().plusDays(1));
        Errors errors = trustCorporateValidator.validate(trustDataDtoList,  true, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                TrustCorporateDto.DATE_BECAME_INTERESTED_PERSON_FIELD);
        String validationMessage = ValidationMessages.DATE_NOT_IN_PAST_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenRegisteredOfficeAddressFieldIsNull() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setRegisteredOfficeAddress(null);

        when(addressDtoValidator.validate(any(), any(), any(), any(), any())).thenCallRealMethod();
        Errors errors = trustCorporateValidator.validate(trustDataDtoList,  true, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.REGISTERED_OFFICE_ADDRESS_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenServiceAddressSameAsUsualResidentialAddressFieldIsNull() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setServiceAddressSameAsPrincipalAddress(null);
        Errors errors = trustCorporateValidator.validate(trustDataDtoList,  true, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IS_SERVICE_ADDRESS_SAME_AS_PRINCIPAL_ADDRESS_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenServiceAddressSameAsUsualResidentialAddressFields() {
        var trustee = trustDataDtoList.getFirst().getCorporates().getFirst();
        trustee.setServiceAddressSameAsPrincipalAddress(true);
        trustee.setRoAddressPremises("1");
        trustee.setRoAddressLine1("Broadway");
        trustee.setRoAddressLocality("New York");
        trustee.setRoAddressCountry("USA");

        Errors errors = trustCorporateValidator.validate(trustDataDtoList,  true, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
        assertEquals("1", trustee.getServiceAddress().getPropertyNameNumber());
        assertEquals("Broadway", trustee.getServiceAddress().getLine1());
        assertEquals("New York", trustee.getServiceAddress().getTown());
        assertEquals("USA", trustee.getServiceAddress().getCountry());
    }

    @Test
    void testErrorReportedWhenLegalFormIsNull() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setIdentificationLegalForm(null);
        Errors errors = trustCorporateValidator.validate(trustDataDtoList,  true, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_LEGAL_FORM_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLegalFormIsEmpty() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setIdentificationLegalForm("  ");
        Errors errors = trustCorporateValidator.validate(trustDataDtoList,  true, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_LEGAL_FORM_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLegalFormExceedsMaxLength() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setIdentificationLegalForm(StringUtils.repeat("A", 161));
        Errors errors = trustCorporateValidator.validate(trustDataDtoList,  true, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_LEGAL_FORM_FIELD);
        String validationMessage = qualifiedFieldName +
                String.format(ValidationMessages.MAX_LENGTH_EXCEEDED_ERROR_MESSAGE, 160);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenLegalFormIsAtMaxLength() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setIdentificationLegalForm(StringUtils.repeat("A", 160));

        Errors errors = trustCorporateValidator.validate(trustDataDtoList,  true, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenLegalFormContainsInvalidCharacters() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setIdentificationLegalForm("Дракон");
        Errors errors = trustCorporateValidator.validate(trustDataDtoList,  true, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_LEGAL_FORM_FIELD);
        String validationMessage = String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLegalAuthorityIsNull() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setIdentificationLegalAuthority(null);
        Errors errors = trustCorporateValidator.validate(trustDataDtoList,  true, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_LEGAL_AUTHORITY_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLegalAuthorityIsEmpty() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setIdentificationLegalAuthority("  ");
        Errors errors = trustCorporateValidator.validate(trustDataDtoList,  true, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_LEGAL_AUTHORITY_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenLegalAuthorityExceedsMaxLength() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setIdentificationLegalForm(StringUtils.repeat("A", 161));
        Errors errors = trustCorporateValidator.validate(trustDataDtoList,  true, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_LEGAL_FORM_FIELD);
        String validationMessage = qualifiedFieldName +
                String.format(ValidationMessages.MAX_LENGTH_EXCEEDED_ERROR_MESSAGE, 160);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedWhenLegalAuthorityIsAtMaxLength() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setIdentificationLegalForm(StringUtils.repeat("A", 160));

        Errors errors = trustCorporateValidator.validate(trustDataDtoList,  true, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenLegalAuthorityContainsInvalidCharacters() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setIdentificationLegalForm("Дракон");
        Errors errors = trustCorporateValidator.validate(trustDataDtoList,  true, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_LEGAL_FORM_FIELD);
        String validationMessage = String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedPlaceRegisteredIsNull() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setIdentificationPlaceRegistered(null);
        Errors errors = trustCorporateValidator.validate(trustDataDtoList,  true, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_PLACE_REGISTERED_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedPlaceRegisteredIsEmpty() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setIdentificationPlaceRegistered("  ");
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, true, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_PLACE_REGISTERED_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedPlaceRegisteredExceedsMaxLength() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setIdentificationPlaceRegistered(StringUtils.repeat("A", 161));
        Errors errors = trustCorporateValidator.validate(trustDataDtoList,  true, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_PLACE_REGISTERED_FIELD);
        String validationMessage = qualifiedFieldName +
                String.format(ValidationMessages.MAX_LENGTH_EXCEEDED_ERROR_MESSAGE, 160);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedPlaceRegisteredIsAtMaxLength() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setIdentificationPlaceRegistered(StringUtils.repeat("A", 160));

        Errors errors = trustCorporateValidator.validate(trustDataDtoList, true, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedPlaceRegisteredContainsInvalidCharacters() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setIdentificationPlaceRegistered("Дракон");
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, true, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_PLACE_REGISTERED_FIELD);
        String validationMessage = String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedCountryRegistrationIsNull() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setIdentificationCountryRegistration(null);
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, true, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_COUNTRY_REGISTRATION_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedCountryRegistrationIsEmpty() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setIdentificationCountryRegistration("  ");
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, true, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_COUNTRY_REGISTRATION_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedCountryRegistrationExceedsMaxLength() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setIdentificationCountryRegistration(StringUtils.repeat("A", 161));
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, true, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_COUNTRY_REGISTRATION_FIELD);
        String validationMessage = qualifiedFieldName +
                String.format(ValidationMessages.MAX_LENGTH_EXCEEDED_ERROR_MESSAGE, 160);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorReportedCountryRegistrationIsAtMaxLength() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setIdentificationCountryRegistration(StringUtils.repeat("A", 160));

        Errors errors = trustCorporateValidator.validate(trustDataDtoList, true, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedCountryRegistrationContainsInvalidCharacters() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setIdentificationCountryRegistration("Дракон");
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, true, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_COUNTRY_REGISTRATION_FIELD);
        String validationMessage = String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorRegistrationNumberIsNull() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setIdentificationRegistrationNumber(null);
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, true, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_REGISTRATION_NUMBER_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorRegistrationNumberIsEmpty() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setIdentificationRegistrationNumber("  ");
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, true, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_REGISTRATION_NUMBER_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorRegistrationNumberExceedsMaxLength() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setIdentificationRegistrationNumber(StringUtils.repeat("A", 161));
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, true, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_REGISTRATION_NUMBER_FIELD);
        String validationMessage = qualifiedFieldName +
                String.format(ValidationMessages.MAX_LENGTH_EXCEEDED_ERROR_MESSAGE, 160);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorRegistrationNumberIsAtMaxLength() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setIdentificationRegistrationNumber(StringUtils.repeat("A", 160));

        Errors errors = trustCorporateValidator.validate(trustDataDtoList, true, new Errors(), LOGGING_CONTEXT);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorRegistrationNumberContainsInvalidCharacters() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setIdentificationRegistrationNumber("Дракон");
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, true, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_REGISTRATION_NUMBER_FIELD);
        String validationMessage = String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorRegisteredInCountryFormedInNotSupplied() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setOnRegisterInCountryFormedIn(Boolean.FALSE);
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, true, new Errors(), LOGGING_CONTEXT);

        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.IDENTIFICATION_PLACE_REGISTERED_FIELD);
        String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorRegisteredInCountryFormedInSupplied() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setOnRegisterInCountryFormedIn(Boolean.TRUE);
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, true, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorIsStillInvolvedTrueAndCeasedDateNull() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setCorporateStillInvolvedInTrust(Boolean.TRUE);
        trustDataDtoList.getFirst().getCorporates().getFirst().setCeasedDate(null);
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, true, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorIsStillInvolvedFalseAndCeasedDateNotNull() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setCorporateStillInvolvedInTrust(Boolean.FALSE);
        trustDataDtoList.getFirst().getCorporates().getFirst().setCeasedDate(LocalDate.of(2020, 1, 1));
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, true, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorIsStillInvolvedNull() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setCorporateStillInvolvedInTrust(null);
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, true, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorIsStillInvolvedNullAndCeasedDateIsNotNull() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setCorporateStillInvolvedInTrust(null);
        trustDataDtoList.getFirst().getCorporates().getFirst().setCeasedDate(LocalDate.of(2020, 1, 1));
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, true, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.CEASED_DATE_FIELD);
        String validationMessage = String.format(ValidationMessages.NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorIsStillInvolvedTrueAndCeasedDateNotNull() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setCorporateStillInvolvedInTrust(Boolean.TRUE);
        trustDataDtoList.getFirst().getCorporates().getFirst().setCeasedDate(LocalDate.of(2020, 1, 1));
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, true, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.CEASED_DATE_FIELD);
        String validationMessage = String.format(ValidationMessages.NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorIsStillInvolvedFalseAndCeasedDateNull() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setCorporateStillInvolvedInTrust(Boolean.FALSE);
        trustDataDtoList.getFirst().getCorporates().getFirst().setCeasedDate(null);
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, true, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.CEASED_DATE_FIELD);
        String validationMessage = String.format(ValidationMessages.NOT_NULL_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorIsStillInvolvedFalseAndCeasedDateFuture() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setCorporateStillInvolvedInTrust(Boolean.FALSE);
        trustDataDtoList.getFirst().getCorporates().getFirst().setCeasedDate(LocalDate.now().plusDays(1));
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, true, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.CEASED_DATE_FIELD);
        String validationMessage = String.format(ValidationMessages.DATE_NOT_IN_PAST_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorIsStillInvolvedFalseAndCeasedDateBeforeTrustCreationDate() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setCorporateStillInvolvedInTrust(Boolean.FALSE);
        trustDataDto.setCreationDate(LocalDate.of(2020, 1, 1));
        trustDataDtoList.getFirst().getCorporates().getFirst().setCeasedDate(LocalDate.of(1999, 12, 31));
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, true, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.CEASED_DATE_FIELD);
        String validationMessage = String.format(ValidationMessages.CEASED_DATE_BEFORE_CREATION_DATE_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorIsStillInvolvedFalseAndCeasedDateBeforeDateBecameInterestedPerson() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setType(BeneficialOwnerType.INTERESTED_PERSON.getValue());
        trustDataDtoList.getFirst().getCorporates().getFirst().setCorporateStillInvolvedInTrust(Boolean.FALSE);
        trustDataDtoList.getFirst().getCorporates().getFirst().setDateBecameInterestedPerson(LocalDate.of(2020, 1, 1));
        trustDataDtoList.getFirst().getCorporates().getFirst().setCeasedDate(LocalDate.of(1999, 12, 31));
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, true, new Errors(), LOGGING_CONTEXT);
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustCorporateDto.CEASED_DATE_FIELD);
        String validationMessage = String.format(ValidationMessages.CEASED_DATE_BEFORE_DATE_BECAME_INTERESTED_ERROR_MESSAGE, qualifiedFieldName);
        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoErrorIsStillInvolvedFalseAndCeasedDateAfterDateBecameInterestedPerson() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setType(BeneficialOwnerType.INTERESTED_PERSON.getValue());
        trustDataDtoList.getFirst().getCorporates().getFirst().setCorporateStillInvolvedInTrust(Boolean.FALSE);
        trustDataDtoList.getFirst().getCorporates().getFirst().setDateBecameInterestedPerson(LocalDate.of(2020, 1, 1));
        trustDataDtoList.getFirst().getCorporates().getFirst().setCeasedDate(LocalDate.of(2020, 1, 2));
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, true, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorIsStillInvolvedFalseAndCeasedDateSameDateBecameInterestedPerson() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setType(BeneficialOwnerType.INTERESTED_PERSON.getValue());
        trustDataDtoList.getFirst().getCorporates().getFirst().setCorporateStillInvolvedInTrust(Boolean.FALSE);
        trustDataDtoList.getFirst().getCorporates().getFirst().setDateBecameInterestedPerson(LocalDate.of(2020, 1, 1));
        trustDataDtoList.getFirst().getCorporates().getFirst().setCeasedDate(LocalDate.of(2020, 1, 1));
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, true, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorIsStillInvolvedFalseAndCeasedDateBeforeDateBecameInterestedPersonWhenTypeIsNotRelevant() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setType(BeneficialOwnerType.BENEFICIARY.getValue());
        trustDataDtoList.getFirst().getCorporates().getFirst().setCorporateStillInvolvedInTrust(Boolean.FALSE);
        trustDataDtoList.getFirst().getCorporates().getFirst().setDateBecameInterestedPerson(LocalDate.of(2020, 1, 1));
        trustDataDtoList.getFirst().getCorporates().getFirst().setCeasedDate(LocalDate.of(2019, 12, 31));
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, true, new Errors(),  LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorsIsStillInvolvedNullRegistrationJourney() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setCorporateStillInvolvedInTrust(null);
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, false, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorsCeasedDatedNullRegistrationJourney() {
        trustDataDtoList.getFirst().getCorporates().getFirst().setCorporateStillInvolvedInTrust(Boolean.FALSE);
        trustDataDtoList.getFirst().getCorporates().getFirst().setCeasedDate(null);
        Errors errors = trustCorporateValidator.validate(trustDataDtoList, false, new Errors(), LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }


    private void assertError(String qualifiedFieldName, String message, Errors errors) {
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(message).build();

        assertTrue(errors.containsError(err));
    }
}
