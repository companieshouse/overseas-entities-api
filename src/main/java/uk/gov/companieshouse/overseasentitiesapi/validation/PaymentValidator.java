package uk.gov.companieshouse.overseasentitiesapi.validation;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators.setErrorMsgToLocation;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationUtils.getQualifiedFieldName;

import org.springframework.stereotype.Component;

import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.PaymentDto;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.overseasentitiesapi.validation.utils.ValidationMessages;
import uk.gov.companieshouse.service.rest.err.Errors;

@Component
public class PaymentValidator {

    public Errors validate(PaymentDto paymentDto, Errors errors, String loggingContext) {
        validatePayment(paymentDto, errors, loggingContext);

        return errors;
    }

    public Errors validateFull(PaymentDto paymentDto, Errors errors, String loggingContext) {

        if (paymentDto == null || paymentDto.getRedirectUri() == null ||
            paymentDto.getReference() == null || paymentDto.getResource() == null ||
            paymentDto.getState() == null) {
            
            String errorMessage = ValidationMessages.NOT_NULL_ERROR_MESSAGE.replace("%s", "payment");
            setErrorMsgToLocation(errors, "payment", errorMessage);
            ApiLogger.infoContext(loggingContext, errorMessage);
        } else {
            validatePayment(paymentDto, errors, loggingContext);
        }
        return errors;
    }

    private void validatePayment(PaymentDto paymentDto, Errors errors, String loggingContext) {
        String errorMessage = ValidationMessages.NOT_VALID_ERROR_MESSAGE.replace("%s", "payment");
        setErrorMsgToLocation(errors, "payment", errorMessage);
        ApiLogger.infoContext(loggingContext, errorMessage);
    }
}
