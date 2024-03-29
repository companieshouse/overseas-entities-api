package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.overseasentitiesapi.mocks.DueDiligenceMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.OverseasEntityDueDiligenceMock;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.DueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntityDueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static uk.gov.companieshouse.overseasentitiesapi.validation.DueDiligenceDataBlockValidator.QUALIFIED_FIELD_NAMES;

@ExtendWith(MockitoExtension.class)
class DueDiligenceDataBlockValidatorTest {

   private static final String LOGGING_CONTEXT = "12345";
   @InjectMocks
   private DueDiligenceDataBlockValidator dueDiligenceDataBlockValidator;
   @Mock
   private OverseasEntityDueDiligenceValidator overseasEntityDueDiligenceValidator;
   @Mock
   private DueDiligenceValidator dueDiligenceValidator;

   private final OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto = OverseasEntityDueDiligenceMock.getOverseasEntityDueDiligenceDto();
   private final DueDiligenceDto dueDiligenceDto = DueDiligenceMock.getDueDiligenceDto();

   @Test
   void testErrorReportedWhenBothDueDiligenceBlocksPresentWithSomeFieldsPopulatedForBoth() {
      Errors errors = new Errors();
      DueDiligenceDto dueDiligenceDto = new DueDiligenceDto();
      dueDiligenceDto.setName("Test");
      dueDiligenceDto.setEmail("test@test.gov.uk");
      OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto = new OverseasEntityDueDiligenceDto();
      overseasEntityDueDiligenceDto.setName("OeTest");
      overseasEntityDueDiligenceDto.setAmlNumber("abc123");

      dueDiligenceDataBlockValidator.validateFullDueDiligenceFields(dueDiligenceDto, overseasEntityDueDiligenceDto, errors, LOGGING_CONTEXT);
      String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BOTH_BE_PRESENT_ERROR_MESSAGE, QUALIFIED_FIELD_NAMES);
      assertError(QUALIFIED_FIELD_NAMES, validationMessage, errors);
   }

   @Test
   void testErrorReportedWhenBothDueDiligenceBlocksPresentWithSomeFieldsPopulatedForBothWithoutIdentityDate() {
      Errors errors = new Errors();
      DueDiligenceDto dueDiligenceDto = new DueDiligenceDto();
      dueDiligenceDto.setName("Test");
      dueDiligenceDto.setEmail("test@test.gov.uk");
      OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto = new OverseasEntityDueDiligenceDto();
      overseasEntityDueDiligenceDto.setName("OeTest");
      overseasEntityDueDiligenceDto.setAmlNumber("abc123");

      dueDiligenceDataBlockValidator.validatePartialDueDiligenceFields(dueDiligenceDto, overseasEntityDueDiligenceDto, errors, LOGGING_CONTEXT);
      String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BOTH_BE_PRESENT_ERROR_MESSAGE, QUALIFIED_FIELD_NAMES);
      assertError(QUALIFIED_FIELD_NAMES, validationMessage, errors);
   }

   @Test
   void testErrorReportedWhenBothDueDiligenceBlocksPresentWithAllFieldsPopulatedForBoth() {
      Errors errors = new Errors();
      dueDiligenceDataBlockValidator.validateFullDueDiligenceFields(dueDiligenceDto, overseasEntityDueDiligenceDto, errors, LOGGING_CONTEXT);
      String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BOTH_BE_PRESENT_ERROR_MESSAGE, QUALIFIED_FIELD_NAMES);
      assertError(QUALIFIED_FIELD_NAMES, validationMessage, errors);
   }

   @Test
   void testErrorReportedWhenBothDueDiligenceBlocksPresentWithAllFieldsPopulatedForBothWithoutIdentityDate() {
      Errors errors = new Errors();
      dueDiligenceDataBlockValidator.validatePartialDueDiligenceFields(dueDiligenceDto, overseasEntityDueDiligenceDto, errors, LOGGING_CONTEXT);
      String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BOTH_BE_PRESENT_ERROR_MESSAGE, QUALIFIED_FIELD_NAMES);
      assertError(QUALIFIED_FIELD_NAMES, validationMessage, errors);
   }

   @Test
   void testErrorReportedWhenBothDueDiligenceBlocksAbsentBothNull() {
      Errors errors = new Errors();
      dueDiligenceDataBlockValidator.validateFullDueDiligenceFields(null, null, errors, LOGGING_CONTEXT);
      String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BOTH_BE_ABSENT_ERROR_MESSAGE, QUALIFIED_FIELD_NAMES);
      assertError(QUALIFIED_FIELD_NAMES, validationMessage, errors);
   }

   @Test
   void testErrorReportedWhenBothDueDiligenceBlocksAbsentBothNullWithoutIdentityDate() {
      Errors errors = new Errors();
      dueDiligenceDataBlockValidator.validatePartialDueDiligenceFields(null, null, errors, LOGGING_CONTEXT);
      String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BOTH_BE_ABSENT_ERROR_MESSAGE, QUALIFIED_FIELD_NAMES);
      assertError(QUALIFIED_FIELD_NAMES, validationMessage, errors);
   }

   @Test
   void testErrorReportedWhenBothDueDiligenceBlocksAbsentBothEmpty() {
      Errors errors = new Errors();
      dueDiligenceDataBlockValidator.validateFullDueDiligenceFields(new DueDiligenceDto(), new OverseasEntityDueDiligenceDto(), errors, LOGGING_CONTEXT);
      String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BOTH_BE_ABSENT_ERROR_MESSAGE, QUALIFIED_FIELD_NAMES);
      assertError(QUALIFIED_FIELD_NAMES, validationMessage, errors);
   }

   @Test
   void testErrorReportedWhenBothDueDiligenceBlocksAbsentBothEmptyWithoutIdentityDate() {
      Errors errors = new Errors();
      dueDiligenceDataBlockValidator.validatePartialDueDiligenceFields(new DueDiligenceDto(), new OverseasEntityDueDiligenceDto(), errors, LOGGING_CONTEXT);
      String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BOTH_BE_ABSENT_ERROR_MESSAGE, QUALIFIED_FIELD_NAMES);
      assertError(QUALIFIED_FIELD_NAMES, validationMessage, errors);
   }

   @Test
   void testErrorReportedWhenBothDueDiligenceBlocksAbsentDueDiligenceNullOtherEmpty() {
      Errors errors = new Errors();
      dueDiligenceDataBlockValidator.validateFullDueDiligenceFields(new DueDiligenceDto(), null, errors, LOGGING_CONTEXT);
      String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BOTH_BE_ABSENT_ERROR_MESSAGE, QUALIFIED_FIELD_NAMES);
      assertError(QUALIFIED_FIELD_NAMES, validationMessage, errors);
   }

   @Test
   void testErrorReportedWhenBothDueDiligenceBlocksAbsentDueDiligenceNullOtherEmptyWithoutIdentityDate() {
      Errors errors = new Errors();
      dueDiligenceDataBlockValidator.validatePartialDueDiligenceFields(new DueDiligenceDto(), null, errors, LOGGING_CONTEXT);
      String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BOTH_BE_ABSENT_ERROR_MESSAGE, QUALIFIED_FIELD_NAMES);
      assertError(QUALIFIED_FIELD_NAMES, validationMessage, errors);
   }

   @Test
   void testErrorReportedWhenBothDueDiligenceBlocksAbsentOverseasEntityDueDiligenceNullOtherEmpty() {
      Errors errors = new Errors();
      dueDiligenceDataBlockValidator.validateFullDueDiligenceFields(null, new OverseasEntityDueDiligenceDto(), errors, LOGGING_CONTEXT);
      String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BOTH_BE_ABSENT_ERROR_MESSAGE, QUALIFIED_FIELD_NAMES);
      assertError(QUALIFIED_FIELD_NAMES, validationMessage, errors);
   }

   @Test
   void testErrorReportedWhenBothDueDiligenceBlocksAbsentOverseasEntityDueDiligenceNullOtherEmptyWithoutIdentityDate() {
      Errors errors = new Errors();
      dueDiligenceDataBlockValidator.validatePartialDueDiligenceFields(null, new OverseasEntityDueDiligenceDto(), errors, LOGGING_CONTEXT);
      String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BOTH_BE_ABSENT_ERROR_MESSAGE, QUALIFIED_FIELD_NAMES);
      assertError(QUALIFIED_FIELD_NAMES, validationMessage, errors);
   }

   @Test
   void testNoErrorReportedWhenOnlyDueDiligenceBlocksPresentOtherEmpty() {
      Errors errors = new Errors();
      dueDiligenceDataBlockValidator.validateFullDueDiligenceFields(dueDiligenceDto, new OverseasEntityDueDiligenceDto(), errors, LOGGING_CONTEXT);
      assertFalse(errors.hasErrors());
   }

   @Test
   void testNoErrorReportedWhenOnlyDueDiligenceBlocksPresentOtherEmptyWithoutIdentityDate() {
      Errors errors = new Errors();
      dueDiligenceDataBlockValidator.validatePartialDueDiligenceFields(dueDiligenceDto, new OverseasEntityDueDiligenceDto(), errors, LOGGING_CONTEXT);
      assertFalse(errors.hasErrors());
   }

   @Test
   void testNoErrorReportedWhenOnlyDueDiligenceBlocksPresentOtherNull() {
      Errors errors = new Errors();
      dueDiligenceDataBlockValidator.validateFullDueDiligenceFields(dueDiligenceDto, null, errors, LOGGING_CONTEXT);
      assertFalse(errors.hasErrors());
   }

   @Test
   void testNoErrorReportedWhenOnlyDueDiligenceBlocksPresentOtherNullWithoutIdentityDate() {
      Errors errors = new Errors();
      dueDiligenceDataBlockValidator.validatePartialDueDiligenceFields(dueDiligenceDto, null, errors, LOGGING_CONTEXT);
      assertFalse(errors.hasErrors());
   }

   @Test
   void testnoErrorReportedWhenOnlyOverseasEntitiesDueDiligenceBlocksPresentOtherEmpty() {
      Errors errors = new Errors();
      dueDiligenceDataBlockValidator.validateFullDueDiligenceFields(new DueDiligenceDto(), overseasEntityDueDiligenceDto, errors, LOGGING_CONTEXT);
      assertFalse(errors.hasErrors());
   }

   @Test
   void testnoErrorReportedWhenOnlyOverseasEntitiesDueDiligenceBlocksPresentOtherEmptyWithoutIdentityDate() {
      Errors errors = new Errors();
      dueDiligenceDataBlockValidator.validatePartialDueDiligenceFields(new DueDiligenceDto(), overseasEntityDueDiligenceDto, errors, LOGGING_CONTEXT);
      assertFalse(errors.hasErrors());
   }

   @Test
   void testNoErrorReportedWhenOnlyOverseasEntitiesDueDiligenceBlocksPresentOtherNull() {
      Errors errors = new Errors();
      dueDiligenceDataBlockValidator.validateFullDueDiligenceFields(null, overseasEntityDueDiligenceDto, errors, LOGGING_CONTEXT);
      assertFalse(errors.hasErrors());
   }

   @Test
   void testNoErrorReportedWhenOnlyOverseasEntitiesDueDiligenceBlocksPresentOtherNullWithoutIdentityDate() {
      Errors errors = new Errors();
      dueDiligenceDataBlockValidator.validatePartialDueDiligenceFields(null, overseasEntityDueDiligenceDto, errors, LOGGING_CONTEXT);
      assertFalse(errors.hasErrors());
   }

   @Test
   void testOverseasDueDiligenceValidatorIsCalledForFullValidation() {
      dueDiligenceDataBlockValidator.validateFullDueDiligenceFields(null, overseasEntityDueDiligenceDto, new Errors(), LOGGING_CONTEXT);
      verify(overseasEntityDueDiligenceValidator, times(1)).validate(eq(overseasEntityDueDiligenceDto), any(), any());
   }

   @Test
   void testDueDiligenceValidatorIsCalledForFullValidation() {
      dueDiligenceDataBlockValidator.validateFullDueDiligenceFields(dueDiligenceDto, null, new Errors(), LOGGING_CONTEXT);
      verify(dueDiligenceValidator, times(1)).validate(eq(dueDiligenceDto), any(), any());
   }

   @Test
   void testOverseasDueDiligenceValidatorIsCalledForPartialValidation() {
      dueDiligenceDataBlockValidator.validatePartialDueDiligenceFields(null, overseasEntityDueDiligenceDto, new Errors(), LOGGING_CONTEXT);
      verify(overseasEntityDueDiligenceValidator, times(0)).validate(eq(overseasEntityDueDiligenceDto), any(), any());
      verify(overseasEntityDueDiligenceValidator, times(1)).validateWithoutIdentityDate(eq(overseasEntityDueDiligenceDto), any(), any());
   }

   @Test
   void testDueDiligenceValidatorIsCalledForPartialValidation() {
      dueDiligenceDataBlockValidator.validatePartialDueDiligenceFields(dueDiligenceDto, null, new Errors(), LOGGING_CONTEXT);
      verify(dueDiligenceValidator, times(0)).validate(eq(dueDiligenceDto), any(), any());
      verify(dueDiligenceValidator, times(1)).validateWithoutIdentityDate(eq(dueDiligenceDto), any(), any());
   }

   private void assertError(String fieldName, String message, Errors errors) {
      Err err = Err.invalidBodyBuilderWithLocation(fieldName).withError(message).build();
      assertTrue(errors.containsError(err));
   }
}
