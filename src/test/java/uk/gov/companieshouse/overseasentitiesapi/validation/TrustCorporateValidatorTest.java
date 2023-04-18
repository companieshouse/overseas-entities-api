package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.overseasentitiesapi.mocks.TrustMock;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustDataDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.companieshouse.overseasentitiesapi.validation.TrustCorporateValidator.PARENT_FIELD;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

@ExtendWith(MockitoExtension.class)
class TrustCorporateValidatorTest {

    private static final String LOGGING_CONTEXT = "12345";

    private TrustCorporateValidator trustCorporateValidator;

    private List<TrustDataDto> trustDataDtoList;

    @BeforeEach
    public void init() {
        trustCorporateValidator = new TrustCorporateValidator();
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

    private void assertError(String qualifiedFieldName, String message, Errors errors) {
        Err err = Err.invalidBodyBuilderWithLocation(qualifiedFieldName).withError(message).build();

        assertTrue(errors.containsError(err));
    }
}
