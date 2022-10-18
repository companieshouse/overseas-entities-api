package uk.gov.companieshouse.overseasentitiesapi.validation.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.overseasentitiesapi.mocks.DueDiligenceMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.OverseasEntityDueDiligenceMock;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.DueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntityDueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.validation.DueDiligenceValidator;
import uk.gov.companieshouse.overseasentitiesapi.validation.OverseasEntityDueDiligenceValidator;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.DueDiligenceDataBlockValidator.QUALIFIED_FIELD_NAMES;

@ExtendWith(MockitoExtension.class)
class DueDiligenceDataBlockValidatorTest {

   private static final String LOGGING_CONTEXT = "12345";
   @InjectMocks
   private DueDiligenceDataBlockValidator dueDiligenceDataBlockValidator;
   @Mock
   private OverseasEntityDueDiligenceValidator overseasEntityDueDiligenceValidator;
   @Mock
   private DueDiligenceValidator dueDiligenceValidator;

   private OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto = OverseasEntityDueDiligenceMock.getOverseasEntityDueDiligenceDto();
   private DueDiligenceDto dueDiligenceDto = DueDiligenceMock.getDueDiligenceDto();

   @Test
   void testErrorReportedWhenBothDueDiligenceBlocksPresentWithSomeFieldsPopulatedForBoth() {
      Errors errors = new Errors();
      DueDiligenceDto dueDiligenceDto = new DueDiligenceDto();
      dueDiligenceDto.setName("Test");
      dueDiligenceDto.setEmail("test@test.gov.uk");
      OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto = new OverseasEntityDueDiligenceDto();
      overseasEntityDueDiligenceDto.setName("OeTest");
      overseasEntityDueDiligenceDto.setAmlNumber("abc123");

      dueDiligenceDataBlockValidator.validateDueDiligenceFields(dueDiligenceDto, overseasEntityDueDiligenceDto, errors, LOGGING_CONTEXT);
      String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BOTH_BE_PRESENT_ERROR_MESSAGE, QUALIFIED_FIELD_NAMES);
      assertError(QUALIFIED_FIELD_NAMES, validationMessage, errors);
   }

   @Test
   void testErrorReportedWhenBothDueDiligenceBlocksPresentWithAllFieldsPopulatedForBoth() {
      Errors errors = new Errors();
      dueDiligenceDataBlockValidator.validateDueDiligenceFields(dueDiligenceDto, overseasEntityDueDiligenceDto, errors, LOGGING_CONTEXT);
      String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BOTH_BE_PRESENT_ERROR_MESSAGE, QUALIFIED_FIELD_NAMES);
      assertError(QUALIFIED_FIELD_NAMES, validationMessage, errors);
   }

   @Test
   void testErrorReportedWhenBothDueDiligenceBlocksAbsentBothNull() {
      Errors errors = new Errors();
      dueDiligenceDataBlockValidator.validateDueDiligenceFields(null, null, errors, LOGGING_CONTEXT);
      String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BOTH_BE_ABSENT_ERROR_MESSAGE, QUALIFIED_FIELD_NAMES);
      assertError(QUALIFIED_FIELD_NAMES, validationMessage, errors);
   }

   @Test
   void testErrorReportedWhenBothDueDiligenceBlocksAbsentBothEmpty() {
      Errors errors = new Errors();
      dueDiligenceDataBlockValidator.validateDueDiligenceFields(new DueDiligenceDto(), new OverseasEntityDueDiligenceDto(), errors, LOGGING_CONTEXT);
      String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BOTH_BE_ABSENT_ERROR_MESSAGE, QUALIFIED_FIELD_NAMES);
      assertError(QUALIFIED_FIELD_NAMES, validationMessage, errors);
   }

   @Test
   void testErrorReportedWhenBothDueDiligenceBlocksAbsentDueDiligenceNullOtherEmpty() {
      Errors errors = new Errors();
      dueDiligenceDataBlockValidator.validateDueDiligenceFields(new DueDiligenceDto(), null, errors, LOGGING_CONTEXT);
      String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BOTH_BE_ABSENT_ERROR_MESSAGE, QUALIFIED_FIELD_NAMES);
      assertError(QUALIFIED_FIELD_NAMES, validationMessage, errors);
   }

   @Test
   void testErrorReportedWhenBothDueDiligenceBlocksAbsentOverseasEntityDueDiligenceNullOtherEmpty() {
      Errors errors = new Errors();
      dueDiligenceDataBlockValidator.validateDueDiligenceFields(null, new OverseasEntityDueDiligenceDto(), errors, LOGGING_CONTEXT);
      String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BOTH_BE_ABSENT_ERROR_MESSAGE, QUALIFIED_FIELD_NAMES);
      assertError(QUALIFIED_FIELD_NAMES, validationMessage, errors);
   }

   @Test
   void testErrorReportedWhenOnlyDueDiligenceBlocksPresentOtherEmpty() {
      Errors errors = new Errors();
      dueDiligenceDataBlockValidator.validateDueDiligenceFields(dueDiligenceDto, new OverseasEntityDueDiligenceDto(), errors, LOGGING_CONTEXT);
      assertFalse(errors.hasErrors());
   }

   @Test
   void testErrorReportedWhenOnlyDueDiligenceBlocksPresentOtherNull() {
      Errors errors = new Errors();
      dueDiligenceDataBlockValidator.validateDueDiligenceFields(dueDiligenceDto, null, errors, LOGGING_CONTEXT);
      assertFalse(errors.hasErrors());
   }

   @Test
   void testErrorReportedWhenOnlyOverseasEntitiesDueDiligenceBlocksPresentOtherEmpty() {
      Errors errors = new Errors();
      dueDiligenceDataBlockValidator.validateDueDiligenceFields(new DueDiligenceDto(), overseasEntityDueDiligenceDto, errors, LOGGING_CONTEXT);
      assertFalse(errors.hasErrors());
   }

   @Test
   void testErrorReportedWhenOnlyOverseasEntitiesDueDiligenceBlocksPresentOtherNull() {
      Errors errors = new Errors();
      dueDiligenceDataBlockValidator.validateDueDiligenceFields(null, overseasEntityDueDiligenceDto, errors, LOGGING_CONTEXT);
      assertFalse(errors.hasErrors());
   }

   private void assertError(String fieldName, String message, Errors errors) {
      Err err = Err.invalidBodyBuilderWithLocation(fieldName).withError(message).build();
      assertTrue(errors.containsError(err));
   }
}
