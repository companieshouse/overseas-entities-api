package uk.gov.companieshouse.overseasentitiesapi.validation.utils;

import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.overseasentitiesapi.mocks.DueDiligenceMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.OverseasEntityDueDiligenceMock;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.DueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntityDueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.companieshouse.overseasentitiesapi.utils.Constants.AGENT_REGISTERING;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.DueDiligenceDataBlockValidator.QUALIFIED_FIELD_NAMES;

class DueDiligenceDataBlockValidatorTest {

   private static final String LOGGING_CONTEXT = "12345";
   private DueDiligenceDataBlockValidator dueDiligenceDataBlockValidator = new DueDiligenceDataBlockValidator();
   private OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto = OverseasEntityDueDiligenceMock.getOverseasEntityDueDiligenceDto();
   private DueDiligenceDto dueDiligenceDto = DueDiligenceMock.getDueDiligenceDto();

   @Test
   void testErrorReportedWhenBothDueDiligenceBlocksPresentWithSomeFieldsPopulatedForBoth() {
      Errors errors = new Errors();
      assertFalse(dueDiligenceDataBlockValidator.onlyOneCorrectBlockPresent("someone_else", dueDiligenceDto, overseasEntityDueDiligenceDto, errors, LOGGING_CONTEXT));
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
      assertFalse(dueDiligenceDataBlockValidator.onlyOneCorrectBlockPresent(AGENT_REGISTERING, dueDiligenceDto, overseasEntityDueDiligenceDto, errors, LOGGING_CONTEXT));
      String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BOTH_BE_PRESENT_ERROR_MESSAGE, QUALIFIED_FIELD_NAMES);
      assertError(QUALIFIED_FIELD_NAMES, validationMessage, errors);
   }

   @Test
   void testErrorReportedWhenBothDueDiligenceBlocksAbsentBothNull() {
      Errors errors = new Errors();
      assertFalse(dueDiligenceDataBlockValidator.onlyOneCorrectBlockPresent(AGENT_REGISTERING, null, null, errors, LOGGING_CONTEXT));
      String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BOTH_BE_ABSENT_ERROR_MESSAGE, QUALIFIED_FIELD_NAMES);
      assertError(QUALIFIED_FIELD_NAMES, validationMessage, errors);
   }

   @Test
   void testErrorReportedWhenBothDueDiligenceBlocksAbsentBothEmpty() {
      Errors errors = new Errors();
      assertFalse(dueDiligenceDataBlockValidator.onlyOneCorrectBlockPresent(AGENT_REGISTERING, new DueDiligenceDto(), new OverseasEntityDueDiligenceDto(), errors, LOGGING_CONTEXT));
      String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BOTH_BE_ABSENT_ERROR_MESSAGE, QUALIFIED_FIELD_NAMES);
      assertError(QUALIFIED_FIELD_NAMES, validationMessage, errors);
   }

   @Test
   void testErrorReportedWhenBothDueDiligenceBlocksAbsentDueDiligenceNullOtherEmpty() {
      Errors errors = new Errors();
      assertFalse(dueDiligenceDataBlockValidator.onlyOneCorrectBlockPresent(AGENT_REGISTERING, new DueDiligenceDto(), null, errors, LOGGING_CONTEXT));
      String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BOTH_BE_ABSENT_ERROR_MESSAGE, QUALIFIED_FIELD_NAMES);
      assertError(QUALIFIED_FIELD_NAMES, validationMessage, errors);
   }

   @Test
   void testErrorReportedWhenBothDueDiligenceBlocksAbsentOverseasEntityDueDiligenceNullOtherEmpty() {
      Errors errors = new Errors();
      assertFalse(dueDiligenceDataBlockValidator.onlyOneCorrectBlockPresent(AGENT_REGISTERING,null, new OverseasEntityDueDiligenceDto(), errors, LOGGING_CONTEXT));
      String validationMessage = String.format(ValidationMessages.SHOULD_NOT_BOTH_BE_ABSENT_ERROR_MESSAGE, QUALIFIED_FIELD_NAMES);
      assertError(QUALIFIED_FIELD_NAMES, validationMessage, errors);
   }

   @Test
   void testErrorReportedWhenOnlyDueDiligenceBlocksPresentOtherEmpty() {
      Errors errors = new Errors();
      assertTrue(dueDiligenceDataBlockValidator.onlyOneCorrectBlockPresent(AGENT_REGISTERING, dueDiligenceDto, new OverseasEntityDueDiligenceDto(), errors, LOGGING_CONTEXT));
      assertFalse(errors.hasErrors());
   }

   @Test
   void testErrorReportedWhenOnlyDueDiligenceBlocksPresentOtherNull() {
      Errors errors = new Errors();
      assertTrue(dueDiligenceDataBlockValidator.onlyOneCorrectBlockPresent(AGENT_REGISTERING, dueDiligenceDto, null, errors, LOGGING_CONTEXT));
      assertFalse(errors.hasErrors());
   }

   @Test
   void testErrorReportedWhenOnlyOverseasEntitiesDueDiligenceBlocksPresentOtherEmpty() {
      Errors errors = new Errors();
      assertTrue(dueDiligenceDataBlockValidator.onlyOneCorrectBlockPresent("someone_else", new DueDiligenceDto(), overseasEntityDueDiligenceDto, errors, LOGGING_CONTEXT));
      assertFalse(errors.hasErrors());
   }

   @Test
   void testErrorReportedWhenOnlyOverseasEntitiesDueDiligenceBlocksPresentOtherNull() {
      Errors errors = new Errors();
      assertTrue(dueDiligenceDataBlockValidator.onlyOneCorrectBlockPresent("someone_else", null, overseasEntityDueDiligenceDto, errors, LOGGING_CONTEXT));
      assertFalse(errors.hasErrors());
   }

   @Test
   void testErrorReportedWhenIncorrectBlockIsPresentForAgentRegistering() {
      Errors errors = new Errors();
      assertFalse(dueDiligenceDataBlockValidator.onlyOneCorrectBlockPresent("someone_else", dueDiligenceDto,null, errors, LOGGING_CONTEXT));
      String validationMessage = String.format(ValidationMessages.INCORRECT_DATA_FOR_SELECTION, OverseasEntitySubmissionDto.DUE_DILIGENCE_FIELD);
      assertError(OverseasEntitySubmissionDto.DUE_DILIGENCE_FIELD, validationMessage, errors);
   }

   @Test
   void testErrorReportedWhenIncorrectBlockIsPresentForSomeoneElseRegistering() {
      Errors errors = new Errors();
      assertFalse(dueDiligenceDataBlockValidator.onlyOneCorrectBlockPresent(AGENT_REGISTERING, null, overseasEntityDueDiligenceDto, errors, LOGGING_CONTEXT));
      String validationMessage = String.format(ValidationMessages.INCORRECT_DATA_FOR_SELECTION, OverseasEntitySubmissionDto.OVERSEAS_ENTITY_DUE_DILIGENCE);
      assertError(OverseasEntitySubmissionDto.OVERSEAS_ENTITY_DUE_DILIGENCE, validationMessage, errors);
   }

   private void assertError(String fieldName, String message, Errors errors) {
      Err err = Err.invalidBodyBuilderWithLocation(fieldName).withError(message).build();
      assertTrue(errors.containsError(err));
   }

}
