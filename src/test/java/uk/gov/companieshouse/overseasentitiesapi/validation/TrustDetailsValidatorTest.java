package uk.gov.companieshouse.overseasentitiesapi.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType.OVER_25_PERCENT_OF_SHARES;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.gov.companieshouse.overseasentitiesapi.mocks.TrustMock;
import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustDataDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

@ExtendWith(MockitoExtension.class)
class TrustDetailsValidatorTest {

    private static final String LOGGING_CONTEXT = "12345";

    private TrustDetailsValidator trustDetailsValidator;
    private OverseasEntitySubmissionDto overseasEntitySubmissionDto;
    private List<TrustDataDto> trustDataDtoList;

    @BeforeEach
    public void init() {
        trustDetailsValidator = new TrustDetailsValidator();

        trustDataDtoList = new ArrayList<>();
        trustDataDtoList.add(TrustMock.getTrustDataDto());

        overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        overseasEntitySubmissionDto.setTrusts(trustDataDtoList);

        // Associate an Individual and Corporate BO with the trust
        BeneficialOwnerIndividualDto boIndividualDto = new BeneficialOwnerIndividualDto();
        boIndividualDto.setTrusteesNatureOfControlTypes(List.of(new NatureOfControlType[]{ OVER_25_PERCENT_OF_SHARES }));
        boIndividualDto.setTrustIds(List.of(trustDataDtoList.get(0).getTrustId()));

        BeneficialOwnerCorporateDto boCorporateDto1 = new BeneficialOwnerCorporateDto();
        boCorporateDto1.setTrusteesNatureOfControlTypes(List.of(new NatureOfControlType[]{ OVER_25_PERCENT_OF_SHARES }));
        boCorporateDto1.setTrustIds(List.of(trustDataDtoList.get(0).getTrustId()));

        // And add another corporate BO but linked to another trust
        BeneficialOwnerCorporateDto boCorporateDto2 = new BeneficialOwnerCorporateDto();
        boCorporateDto2.setTrusteesNatureOfControlTypes(List.of(new NatureOfControlType[]{ OVER_25_PERCENT_OF_SHARES }));
        boCorporateDto2.setTrustIds(List.of("Another-Trust-Id"));

        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(List.of(new BeneficialOwnerIndividualDto[]{ boIndividualDto }));
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(List.of(new BeneficialOwnerCorporateDto[]{ boCorporateDto1, boCorporateDto2 }));
    }

    @Test
    void testNoValidationErrorReportedWhenTrustIdFieldIsNull() {
        trustDataDtoList.get(0).setTrustId(null);
        Errors errors = trustDetailsValidator.validate(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, true);

        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA, TrustDataDto.TRUST_ID_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoValidationErrorReportedWhenTrustIdFieldIsEmpty() {
        trustDataDtoList.get(0).setTrustId("");
        Errors errors = trustDetailsValidator.validate(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, true);

        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA, TrustDataDto.TRUST_ID_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoValidationErrorReportedWhenTrustIdFieldIsDuplicated() {
        trustDataDtoList.add(TrustMock.getTrustDataDto());
        Errors errors = trustDetailsValidator.validate(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, true);

        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA, TrustDataDto.TRUST_ID_FIELD);
        String validationMessage = ValidationMessages.DUPLICATE_TRUST_ID.replace("%s", "Trust Name");

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoValidationErrorReportedWhenTrustNameFieldIsNull() {
        trustDataDtoList.get(0).setTrustName(null);
        Errors errors = trustDetailsValidator.validate(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, true);

        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA, TrustDataDto.TRUST_NAME_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoValidationErrorReportedWhenTrustNameFieldIsEmpty() {
        trustDataDtoList.get(0).setTrustName(" ");
        Errors errors = trustDetailsValidator.validate(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, true);

        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA, TrustDataDto.TRUST_NAME_FIELD);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoValidationErrorReportedWhenTrustNameFieldIsNotEmpty() {
        trustDataDtoList.get(0).setTrustName("TESTY OVERSEAS ENTITY");
        Errors errors = trustDetailsValidator.validate(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, true);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testValidationErrorReportedWhenTrustNameFieldExceedsMaxLength() {
        trustDataDtoList.get(0).setTrustName(StringUtils.repeat("A", 161));
        Errors errors = trustDetailsValidator.validate(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, true);

        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA, TrustDataDto.TRUST_NAME_FIELD);
        String validationMessage = qualifiedFieldName + " must be 160 characters or less";

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testValidationErrorReportedWhenTrustNameFieldContainsInvalidCharacters() {
        trustDataDtoList.get(0).setTrustName("Дракон");
        Errors errors = trustDetailsValidator.validate(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, true);

        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA, TrustDataDto.TRUST_NAME_FIELD);
        String validationMessage = ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenCreationDateFieldIsNull() {
        trustDataDtoList.get(0).setCreationDate(null);
        Errors errors = trustDetailsValidator.validate(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, true);
        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA, TrustDataDto.CREATION_DATE_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoValidationErrorReportedWhenCreationDateFieldIsInThePast() {
        trustDataDtoList.get(0).setCreationDate(LocalDate.of(1970,1, 1));
        Errors errors = trustDetailsValidator.validate(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, true);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenCreationDateIsInTheFuture() {
        trustDataDtoList.get(0).setCreationDate(LocalDate.now().plusDays(1));
        Errors errors = trustDetailsValidator.validate(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, true);

        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA, TrustDataDto.CREATION_DATE_FIELD);
        String validationMessage = ValidationMessages.DATE_NOT_IN_PAST_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenCeaseDateFieldIsNullAndNoBosAssociatedWithTrust() {
        disassociateBosFromTrust();

        trustDataDtoList.get(0).setCeasedDate(null);
        trustDataDtoList.get(0).setCreationDate(LocalDate.of(1970,1, 1));

        Errors errors = trustDetailsValidator.validate(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, true);

        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA, TrustDataDto.CEASED_DATE_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoValidationErrorReportedWhenCeaseDateFieldIsNullAndBosStillAssociatedWithTrust() {
        trustDataDtoList.get(0).setCeasedDate(null);

        Errors errors = trustDetailsValidator.validate(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, true);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenCeaseDateFieldIsNotNullAndBosStillAssociatedWithTrust() {
        trustDataDtoList.get(0).setCeasedDate(LocalDate.now().minusDays(10));
        trustDataDtoList.get(0).setCreationDate(LocalDate.of(1970,1, 1));

        Errors errors = trustDetailsValidator.validate(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, true);

        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA, TrustDataDto.CEASED_DATE_FIELD);
        String validationMessage = ValidationMessages.NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenCeaseDateIsInTheFuture() {
        disassociateBosFromTrust();

        trustDataDtoList.get(0).setCeasedDate(LocalDate.now().plusDays(1));
        trustDataDtoList.get(0).setCreationDate(LocalDate.of(1970,1, 1));

        Errors errors = trustDetailsValidator.validate(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, true);

        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA, TrustDataDto.CEASED_DATE_FIELD);
        String validationMessage = ValidationMessages.DATE_NOT_IN_PAST_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoValidationErrorReportedWhenCeasedDateIsInTheFutureAndCeasedDateCheckingDisabled() {
        disassociateBosFromTrust();

        trustDataDtoList.get(0).setCeasedDate(LocalDate.now().plusDays(1));
        trustDataDtoList.get(0).setCreationDate(LocalDate.of(1970,1, 1));

        Errors errors = trustDetailsValidator.validate(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, false);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoValidationErrorReportedWhenCeaseDateIsToday() {
        disassociateBosFromTrust();

        trustDataDtoList.get(0).setCeasedDate(LocalDate.now());
        trustDataDtoList.get(0).setCreationDate(LocalDate.of(1970,1, 1));

        Errors errors = trustDetailsValidator.validate(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, true);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenCeaseDateIsBeforeCreationDate() {
        disassociateBosFromTrust();

        trustDataDtoList.get(0).setCeasedDate(LocalDate.of(1968,11, 30));
        trustDataDtoList.get(0).setCreationDate(LocalDate.of(1972,3, 28));

        Errors errors = trustDetailsValidator.validate(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, true);

        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA, TrustDataDto.CEASED_DATE_FIELD);
        String validationMessage = ValidationMessages.CEASED_DATE_BEFORE_CREATION_DATE_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testNoValidationErrorReportedWhenCeaseDateIsInThePastAndAfterCreationDate() {
        disassociateBosFromTrust();

        trustDataDtoList.get(0).setCeasedDate(LocalDate.of(1978,4, 11));
        trustDataDtoList.get(0).setCreationDate(LocalDate.of(1970,1, 1));

        Errors errors = trustDetailsValidator.validate(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, true);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenUnableToObtainAllTrustInfoFieldIsNull() {
        trustDataDtoList.get(0).setUnableToObtainAllTrustInfo(null);
        Errors errors = trustDetailsValidator.validate(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, true);

        String qualifiedFieldName = getQualifiedFieldName(OverseasEntitySubmissionDto.TRUST_DATA, TrustDataDto.UNABLE_TO_OBTAIN_ALL_TRUST_INFO_FIELD);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);

        assertError(qualifiedFieldName, validationMessage, errors);
    }

    @Test
    void testErrorReportedWhenUnableToObtainAllTrustInfoFieldNotEmpty() {
        trustDataDtoList.get(0).setUnableToObtainAllTrustInfo(false);
        Errors errors = trustDetailsValidator.validate(overseasEntitySubmissionDto, new Errors(), LOGGING_CONTEXT, true);

        assertFalse(errors.hasErrors());
    }

    private void disassociateBosFromTrust() {
        // Remove the trust NOC on the Individual BO
        overseasEntitySubmissionDto.getBeneficialOwnersIndividual().get(0).setTrusteesNatureOfControlTypes(null);

        // Cease the Corporate BO
        overseasEntitySubmissionDto.getBeneficialOwnersCorporate().get(0).setCeasedDate(LocalDate.of(1911,1, 1));
    }

    private void assertError(String qualifiedFieldName, String message, Errors errors) {
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(message).build();
        assertTrue(errors.containsError(err));
    }
}
