package uk.gov.companieshouse.overseasentitiesapi.validation;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators.setErrorMsgToLocation;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.BeneficialOwnerType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustDataDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.CountryLists;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.DateValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.StringValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Errors;

@Component
public class TrustIndividualValidator {

    public static final String PARENT_FIELD = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA,
            TrustDataDto.INDIVIDUAL_FIELD);

    private final AddressDtoValidator addressDtoValidator;

    private final NationalityValidator nationalityValidator;

    @Autowired
    public TrustIndividualValidator(AddressDtoValidator addressDtoValidator,
            NationalityValidator nationalityValidator) {
        this.addressDtoValidator = addressDtoValidator;
        this.nationalityValidator = nationalityValidator;
    }

    public Errors validate(List<TrustDataDto> trustDataDtoList, Errors errors, String loggingContext, boolean isForUpdateOrRemove) {
        for (TrustDataDto trustDataDto : trustDataDtoList) {
            List<TrustIndividualDto> trustIndividuals = trustDataDto.getIndividuals();
            if (!CollectionUtils.isEmpty(trustIndividuals)) {
                for (TrustIndividualDto trustIndividualDto : trustIndividuals) {
                    validateForename(trustIndividualDto.getForename(), errors, loggingContext);
                    validateSurname(trustIndividualDto.getSurname(), errors, loggingContext);
                    validateDateOfBirth(trustIndividualDto.getDateOfBirth(), errors, loggingContext);

                    String type = trustIndividualDto.getType();
                    if (validateType(type, errors, loggingContext) && BeneficialOwnerType
                            .findByBeneficialOwnerTypeString(type).equals(BeneficialOwnerType.INTERESTED_PERSON)) {
                        validateDateBecameInterestedPerson(trustIndividualDto.getDateBecameInterestedPerson(), errors,
                                loggingContext);
                    }

                    validateNationality(trustIndividualDto.getNationality(), errors, loggingContext);
                    validateSecondNationality(trustIndividualDto.getNationality(),
                            trustIndividualDto.getSecondNationality(), errors, loggingContext);

                    validateAddress(TrustIndividualDto.USUAL_RESIDENTIAL_ADDRESS_FIELD,
                            trustIndividualDto.getUsualResidentialAddress(), errors, loggingContext);

                    if (!isForUpdateOrRemove) {
                        // Currently, the 'isServiceAddressSameAsUsualResidentialAddress' flag isn't retrieved and populated
                        // on the update and remove journeys (an existing defect). It's therefore only possible to run this
                        // part of the validation if the submission is a registration
                        validateSameAsAddress(trustIndividualDto, errors, loggingContext);
                    } else {
                        // Validating 'ceased date' doesn't make sense on the registration journey, as the option to enter
                        // something in this field isn't present, so this is only validated on the update and remove journeys
                        validateCeasedDate(trustIndividualDto, trustDataDto.getCreationDate(), errors, loggingContext);
                    }
                }
            }
        }
        return errors;
    }

    private boolean validateForename(String forename, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.FORENAME_FIELD);

        return StringValidators.isNotBlank(forename, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(forename, 160, qualifiedFieldName, errors,
                        loggingContext)
                && StringValidators.isValidCharacters(forename, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateSurname(String surname, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.SURNAME_FIELD);

        return StringValidators.isNotBlank(surname, qualifiedFieldName, errors, loggingContext)
                && StringValidators.isLessThanOrEqualToMaxLength(surname, 160, qualifiedFieldName, errors,
                        loggingContext)
                && StringValidators.isValidCharacters(surname, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateDateOfBirth(LocalDate dateOfBirth, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.DATE_OF_BIRTH_FIELD);
        return UtilsValidators.isNotNull(dateOfBirth, qualifiedFieldName, errors, loggingContext)
                && DateValidators.isDateInPast(dateOfBirth, qualifiedFieldName, errors, loggingContext);
    }

    private boolean validateType(String type, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.TYPE_FIELD);

        if (!StringValidators.isNotBlank(type, qualifiedFieldName, errors, loggingContext)) {
            return false;
        }

        if (BeneficialOwnerType.findByBeneficialOwnerTypeString(type) == null) {
            String message = ValidationMessages.TRUST_INDIVIDUAL_TYPE_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
            setErrorMsgToLocation(errors, qualifiedFieldName, message);
            ApiLogger.infoContext(loggingContext, message);
            return false;
        }
        return true;
    }

    private boolean validateDateBecameInterestedPerson(LocalDate dateBecameInterestedPerson, Errors errors,
            String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                TrustIndividualDto.DATE_BECAME_INTERESTED_PERSON_FIELD);

        return UtilsValidators.isNotNull(dateBecameInterestedPerson, qualifiedFieldName, errors, loggingContext)
                && DateValidators.isDateInPast(dateBecameInterestedPerson, qualifiedFieldName, errors, loggingContext);
    }

    private Errors validateNationality(String nationality, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, TrustIndividualDto.NATIONALITY_FIELD);

        boolean nationalityIsPresent = StringValidators.isNotBlank(nationality, qualifiedFieldName, errors,
                loggingContext);
        if (nationalityIsPresent) {
            nationalityValidator.validateAgainstNationalityList(qualifiedFieldName, nationality, errors,
                    loggingContext);
        }
        return errors;
    }

    private Errors validateSecondNationality(String nationality, String secondNationality, Errors errors,
            String loggingContext) {
        String qualifiedFieldNameFirstNationality = getQualifiedFieldName(PARENT_FIELD,
                TrustIndividualDto.NATIONALITY_FIELD);
        String qualifiedFieldNameSecondNationality = getQualifiedFieldName(PARENT_FIELD,
                TrustIndividualDto.SECOND_NATIONALITY_FIELD);
        boolean isSecondNationalityNotBlank = StringUtils.isNotBlank(secondNationality);
        if (isSecondNationalityNotBlank) {
            nationalityValidator.validateSecondNationality(qualifiedFieldNameFirstNationality,
                    qualifiedFieldNameSecondNationality, nationality, secondNationality, errors, loggingContext);
        }
        return errors;
    }

    private Errors validateAddress(String addressField, AddressDto addressDto, Errors errors, String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD, addressField);
        addressDtoValidator.validate(qualifiedFieldName, addressDto, CountryLists.getAllCountries(), errors,
                loggingContext);
        return errors;
    }

    private boolean validateServiceAddressSameAsUsualResidentialAddress(Boolean same, Errors errors,
            String loggingContext) {
        String qualifiedFieldName = getQualifiedFieldName(PARENT_FIELD,
                TrustIndividualDto.IS_SERVICE_ADDRESS_SAME_AS_USUAL_RESIDENTIAL_ADDRESS_FIELD);
        return UtilsValidators.isNotNull(same, qualifiedFieldName, errors, loggingContext);
    }

    private void validateSameAsAddress(TrustIndividualDto trustIndividualDto, Errors errors, String loggingContext) {
        boolean isSameAddressFlagValid = validateServiceAddressSameAsUsualResidentialAddress(
                trustIndividualDto.getServiceAddressSameAsUsualResidentialAddress(), errors, loggingContext);
        if (isSameAddressFlagValid
                && Boolean.FALSE.equals(trustIndividualDto.getServiceAddressSameAsUsualResidentialAddress())) {
            validateAddress(TrustIndividualDto.SERVICE_ADDRESS_FIELD, trustIndividualDto.getServiceAddress(), errors,
                    loggingContext);
        }
    }

    private Errors validateCeasedDate(TrustIndividualDto trustIndividualDto,
                                      LocalDate trustCreationDate,
                                      Errors errors,
                                      String loggingContext) {

        Boolean isStillInvolved = trustIndividualDto.getIndividualStillInvolvedInTrust();
        LocalDate ceasedDate = trustIndividualDto.getCeasedDate();
        final String qualifiedFieldNameCeasedDate = getQualifiedFieldName(PARENT_FIELD,
                TrustIndividualDto.CEASED_DATE_FIELD);


        if (Objects.isNull(isStillInvolved) || Boolean.TRUE.equals(isStillInvolved)) {
            if (Objects.nonNull(ceasedDate)) {
                final String errorMessage = ValidationMessages.NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldNameCeasedDate);
                setErrorMsgToLocation(errors, qualifiedFieldNameCeasedDate, errorMessage);
            }
        } else if (UtilsValidators.isNotNull(ceasedDate, qualifiedFieldNameCeasedDate, errors, loggingContext)) {
            DateValidators.isDateInPast(ceasedDate, qualifiedFieldNameCeasedDate, errors, loggingContext);
            DateValidators.isCeasedDateOnOrAfterCreationDate(ceasedDate, trustCreationDate, qualifiedFieldNameCeasedDate, errors, loggingContext);
            DateValidators.isCeasedDateOnOrAfterDateOfBirth(ceasedDate, trustIndividualDto.getDateOfBirth(), qualifiedFieldNameCeasedDate,
                    errors, loggingContext);
            String type = trustIndividualDto.getType();
            if (validateType(type, errors, loggingContext)
                    && BeneficialOwnerType.findByBeneficialOwnerTypeString(type).equals(BeneficialOwnerType.INTERESTED_PERSON)) {
                DateValidators.isCeasedDateOnOrAfterDateBecameInterestedPerson(ceasedDate, trustIndividualDto.getDateBecameInterestedPerson(),
                        qualifiedFieldNameCeasedDate, errors, loggingContext);
            }
        }
        return errors;
    }
}
