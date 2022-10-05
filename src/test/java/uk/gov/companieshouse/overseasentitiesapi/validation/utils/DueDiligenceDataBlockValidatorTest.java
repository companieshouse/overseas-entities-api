package uk.gov.companieshouse.overseasentitiesapi.validation.utils;

import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.overseasentitiesapi.mocks.DueDiligenceMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.OverseasEntityDueDiligenceMock;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.DueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntityDueDiligenceDto;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.DueDiligenceDataBlockValidator.QUALIFIED_FIELD_NAMES;

class DueDiligenceDataBlockValidatorTest {

   private static final String LOGGING_CONTEXT = "12345";

   private DueDiligenceDataBlockValidator dueDiligenceDataBlockValidator = new DueDiligenceDataBlockValidator();
   private OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto = OverseasEntityDueDiligenceMock.getOverseasEntityDueDiligenceDto();
   private DueDiligenceDto dueDiligenceDto = DueDiligenceMock.getDueDiligenceDto();

   @Test
   void testErrorReportedWhenBothDueDiligenceBlocksPresentWithSomeFieldsPopulatedForBoth() {
      Errors errors = new Errors();
      assertFalse(dueDiligenceDataBlockValidator.onlyOneBlockPresent(dueDiligenceDto, overseasEntityDueDiligenceDto, errors, LOGGING_CONTEXT));
      DueDiligenceDto dueDiligenceDto = new DueDiligenceDto();
      dueDiligenceDto.setName("Test");
      dueDiligenceDto.setEmail("test@test.gov.uk");
      OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto = new OverseasEntityDueDiligenceDto();
      overseasEntityDueDiligenceDto.setName("OeTest");
      overseasEntityDueDiligenceDto.setAmlNumber("abc123");
      String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BOTH_BE_PRESENT_ERROR_MESSAGE, QUALIFIED_FIELD_NAMES);
      assertError(QUALIFIED_FIELD_NAMES, validationMessage, errors);
   }

   @Test
   void testErrorReportedWhenBothDueDiligenceBlocksPresentWithAllFieldsPopulatedForBoth() {
      Errors errors = new Errors();
      assertFalse(dueDiligenceDataBlockValidator.onlyOneBlockPresent(dueDiligenceDto, overseasEntityDueDiligenceDto, errors, LOGGING_CONTEXT));
      String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BOTH_BE_PRESENT_ERROR_MESSAGE, QUALIFIED_FIELD_NAMES);
      assertError(QUALIFIED_FIELD_NAMES, validationMessage, errors);
   }

   @Test
   void testErrorReportedWhenBothDueDiligenceBlocksAbsentBothNull() {
      Errors errors = new Errors();
      assertFalse(dueDiligenceDataBlockValidator.onlyOneBlockPresent(null, null, errors, LOGGING_CONTEXT));
      String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BOTH_BE_ABSENT_ERROR_MESSAGE, QUALIFIED_FIELD_NAMES);
      assertError(QUALIFIED_FIELD_NAMES, validationMessage, errors);
   }

   @Test
   void testErrorReportedWhenBothDueDiligenceBlocksAbsentBothEmpty() {
      Errors errors = new Errors();
      assertFalse(dueDiligenceDataBlockValidator.onlyOneBlockPresent(new DueDiligenceDto(), new OverseasEntityDueDiligenceDto(), errors, LOGGING_CONTEXT));
      String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BOTH_BE_ABSENT_ERROR_MESSAGE, QUALIFIED_FIELD_NAMES);
      assertError(QUALIFIED_FIELD_NAMES, validationMessage, errors);
   }

   @Test
   void testErrorReportedWhenBothDueDiligenceBlocksAbsentDueDiligenceNullOtherEmpty() {
      Errors errors = new Errors();
      assertFalse(dueDiligenceDataBlockValidator.onlyOneBlockPresent(new DueDiligenceDto(), null, errors, LOGGING_CONTEXT));
      String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BOTH_BE_ABSENT_ERROR_MESSAGE, QUALIFIED_FIELD_NAMES);
      assertError(QUALIFIED_FIELD_NAMES, validationMessage, errors);
   }

   @Test
   void testErrorReportedWhenBothDueDiligenceBlocksAbsentOverseasEntityDueDiligenceNullOtherEmpty() {
      Errors errors = new Errors();
      assertFalse(dueDiligenceDataBlockValidator.onlyOneBlockPresent(null, new OverseasEntityDueDiligenceDto(), errors, LOGGING_CONTEXT));
      String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BOTH_BE_ABSENT_ERROR_MESSAGE, QUALIFIED_FIELD_NAMES);
      assertError(QUALIFIED_FIELD_NAMES, validationMessage, errors);
   }

   @Test
   void testErrorReportedWhenOnlyDueDiligenceBlocksPresentOtherEmpty() {
      Errors errors = new Errors();
      assertTrue(dueDiligenceDataBlockValidator.onlyOneBlockPresent(dueDiligenceDto, new OverseasEntityDueDiligenceDto(), errors, LOGGING_CONTEXT));
      assertFalse(errors.hasErrors());
   }

   @Test
   void testErrorReportedWhenOnlyDueDiligenceBlocksPresentOtherNull() {
      Errors errors = new Errors();
      assertTrue(dueDiligenceDataBlockValidator.onlyOneBlockPresent(dueDiligenceDto, null, errors, LOGGING_CONTEXT));
      assertFalse(errors.hasErrors());
   }

   @Test
   void testErrorReportedWhenOnlyOverseasEntitiesDueDiligenceBlocksPresentOtherEmpty() {
      Errors errors = new Errors();
      assertTrue(dueDiligenceDataBlockValidator.onlyOneBlockPresent(new DueDiligenceDto(), overseasEntityDueDiligenceDto, errors, LOGGING_CONTEXT));
      assertFalse(errors.hasErrors());
   }

   @Test
   void testErrorReportedWhenOnlyOverseasEntitiesDueDiligenceBlocksPresentOtherNull() {
      Errors errors = new Errors();
      assertTrue(dueDiligenceDataBlockValidator.onlyOneBlockPresent(null, overseasEntityDueDiligenceDto, errors, LOGGING_CONTEXT));
      assertFalse(errors.hasErrors());
   }

   private void assertError(String fieldName, String message, Errors errors) {
      Err err = Err.invalidBodyBuilderWithLocation(fieldName).withError(message).build();
      assertTrue(errors.containsError(err));
   }

}
