package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.encoder.Encode;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.utils.DataSanitisation;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import static com.mongodb.internal.connection.tlschannel.util.Util.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

@ExtendWith(MockitoExtension.class)
public class NationalityValidatorTest {

    private static final String LOGGING_CONTEXT = "12345";

    private String qualifiedFieldName = getQualifiedFieldName(
            OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
            BeneficialOwnerIndividualDto.NATIONALITY_FIELD);

    private String qualifiedSecondFieldName = getQualifiedFieldName(
            OverseasEntitySubmissionDto.BENEFICIAL_OWNERS_INDIVIDUAL_FIELD,
            BeneficialOwnerIndividualDto.SECOND_NATIONALITY_FIELD);

    @InjectMocks
    private NationalityValidator nationalityValidator;

    @Mock
    private DataSanitisation dataSanitisation;

    @Test
    void testNoErrorReportedWhenNationalityIsPresentOnTheList() {
        Errors errors = new Errors();
        nationalityValidator.validateAgainstNationalityList(qualifiedFieldName, "French", errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testErrorReportedWhenNationalityFieldIsEmpty() {
        Errors errors = new Errors();
        nationalityValidator.validateAgainstNationalityList(qualifiedFieldName, "", errors, LOGGING_CONTEXT);
        String validationMessage = ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(validationMessage).build();
        assertTrue(errors.containsError(err));
    }

    @Test
    void testErrorReportedWhenNationalityFieldIsNull() {
        Errors errors = new Errors();
        nationalityValidator.validateAgainstNationalityList(qualifiedFieldName, null, errors, LOGGING_CONTEXT);
        String validationMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", qualifiedFieldName);
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(validationMessage).build();
        assertTrue(errors.containsError(err));
    }

    @Test
    void testErrorReportedWhenNationalityFieldIsNotOnTheList() {
        String noNationality = "Utopian";
        when(dataSanitisation.makeStringSafeForLogging(any())).thenReturn(Encode.forJava(noNationality));
        Errors errors = new Errors();
        nationalityValidator.validateAgainstNationalityList(qualifiedFieldName, noNationality, errors, LOGGING_CONTEXT);
        String validationMessage = ValidationMessages.NATIONALITY_NOT_ON_LIST_ERROR_MESSAGE.replace("%s",  noNationality);
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(validationMessage).build();
        assertTrue(errors.containsError(err));
    }

    @Test
    void testNoErrorReportedWhenSecondNationalityFieldIsNotEqualToTheNationalityGiven() {
        Errors errors = new Errors();
        nationalityValidator.validateSecondNationality(qualifiedFieldName, qualifiedSecondFieldName,"French","Algerian", errors, LOGGING_CONTEXT);
        assertFalse(errors.hasErrors());
    }

    @Test
    void testNoErrorReportedWhenBothNationalityFieldsAreWithinTheMaxLength() {
        Errors errors = new Errors();
        nationalityValidator.validateSecondNationality(qualifiedFieldName, qualifiedSecondFieldName,StringUtils.repeat("A", 24), StringUtils.repeat("B", 25), errors, LOGGING_CONTEXT);
        var compoundQualifiedFieldName = String.format("%s and %s", qualifiedFieldName, qualifiedSecondFieldName);
        String validationMessage = compoundQualifiedFieldName + ValidationMessages.MAX_LENGTH_EXCEEDED_ERROR_MESSAGE.replace("%s", "50");
        Err err = Err.invalidBodyBuilderWithLocation(compoundQualifiedFieldName).withError(validationMessage).build();
        assertFalse(errors.containsError(err));
    }

    @Test
    void testErrorReportedWhenBothNationalityFieldsAreTooLong() {
        Errors errors = new Errors();
        nationalityValidator.validateSecondNationality(qualifiedFieldName, qualifiedSecondFieldName,StringUtils.repeat("A", 25), StringUtils.repeat("B", 25), errors, LOGGING_CONTEXT);
        var compoundQualifiedFieldName = String.format("%s and %s", qualifiedFieldName, qualifiedSecondFieldName);
        String validationMessage = compoundQualifiedFieldName + ValidationMessages.MAX_LENGTH_EXCEEDED_ERROR_MESSAGE.replace("%s", "50");
        Err err = Err.invalidBodyBuilderWithLocation(compoundQualifiedFieldName).withError(validationMessage).build();
        assertTrue(errors.containsError(err));
    }

    @Test
    void testErrorReportedWhenSecondNationalityFieldEqualsTheNationalityGiven() {
        Errors errors = new Errors();
        nationalityValidator.validateSecondNationality(qualifiedFieldName, qualifiedSecondFieldName,"French", "French", errors, LOGGING_CONTEXT);
        String validationMessage = ValidationMessages.SECOND_NATIONALITY_SHOULD_BE_DIFFERENT.replace("%s", qualifiedSecondFieldName);
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedSecondFieldName).withError(validationMessage).build();
        assertTrue(errors.containsError(err));
    }
}
