package uk.gov.companieshouse.overseasentitiesapi.utils;

import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;
import uk.gov.companieshouse.overseasentitiesapi.OverseasEntitiesApiApplication;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validators {

    private static final String NOT_NULL_ERROR_MESSAGE = " must not be null";
    private static final String NOT_EMPTY_ERROR_MESSAGE = " must not be empty and must not only consist of whitespace";
    private static final String MAX_LENGTH_EXCEEDED_ERROR_MESSAGE = " must be %s characters or less.";
    private static final String INVALID_CHARACTERS_ERROR_MESSAGE = " must only include letters a to z, numbers, and special characters such as hyphens, spaces and apostrophes";
    private static final String INVALID_EMAIL_ERROR_MESSAGE = "Enter an email address in the correct format for %s, like name@example.com";

    private static final Logger LOG = LoggerFactory.getLogger(OverseasEntitiesApiApplication.APP_NAMESPACE);

    public boolean validateStringNotBlank(String toTest, String location, Errors errs, String loggingContext) {
        return validateNotNull(toTest, location, errs, loggingContext) && validateStringNotEmpty(toTest, location, errs, loggingContext);
    }

    public boolean validateStringNotEmpty(String toTest, String location, Errors errs, String loggingContext) {
        if (toTest.toString().trim().isEmpty()) {
            setErrorMsgToLocation(errs, location,location + NOT_EMPTY_ERROR_MESSAGE);
            LOG.errorContext(loggingContext,location + " Field is empty" , null, null);
            return false;
        }
        return true;
    }

    public boolean validateNotNull(String toTest, String location, Errors errs, String loggingContext) {
        if (toTest == null) {
            setErrorMsgToLocation(errs, location,location + NOT_NULL_ERROR_MESSAGE);
            LOG.errorContext(loggingContext , location + " Field is null", null,  null);
            return false;
        }
        return true;
    }

    public boolean validateLength(String toTest, Integer maxLength, String location, Errors errs, String loggingContext) {
        if (toTest.toString().length() > maxLength) {
            setErrorMsgToLocation(errs, location,
                    location + MAX_LENGTH_EXCEEDED_ERROR_MESSAGE.replace("%s", maxLength.toString()));
            LOG.errorContext(loggingContext, "Invalid length for " + location,  null, null);
            return false;
        }
        return true;
    }

    public boolean validateCharacters(String toTest, String location, Errors errs, String loggingContext) {
        String regex = "^[-,.:; 0-9A-Z&@$£¥€'\"«»''\"\"?!/\\\\()[\\]{}<>*=#%+ÀÁÂÃÄÅĀĂĄÆǼÇĆĈĊČÞĎÐÈÉÊËĒĔĖĘĚĜĞĠĢĤĦÌÍÎÏĨĪĬĮİĴĶĹĻĽĿŁÑŃŅŇŊÒÓÔÕÖØŌŎŐǾŒŔŖŘŚŜŞŠŢŤŦÙÚÛÜŨŪŬŮŰŲŴẀẂẄỲÝŶŸŹŻŽa-zÀÖØſƒǺẀỲàáâãäåāăąæǽçćĉċčþďðèéêëēĕėęěĝģğġĥħìíîïĩīĭįĵķĺļľŀłñńņňŋòóôõöøōŏőǿœŕŗřśŝşšţťŧùúûüũūŭůűųŵẁẃẅỳýŷÿźżž]*$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(toTest);

        if (!matcher.matches()) {
            setErrorMsgToLocation(errs, location, location + INVALID_CHARACTERS_ERROR_MESSAGE);
            LOG.errorContext(loggingContext, "Invalid characters for " + location,  null, null);
            return false;
        }
        return true;
    }

    public boolean validateEmail(String email, String location, Errors errs, String loggingContext) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            setErrorMsgToLocation(errs, location, INVALID_EMAIL_ERROR_MESSAGE.replace("%s", location));
            LOG.errorContext(loggingContext, "Email address is not in the correct format for " + location, null,  null);
            return false;
        }
        return true;
    }

    private void setErrorMsgToLocation(Errors errors, String location, String msg){
        final Err error = Err.invalidBodyBuilderWithLocation(location).withError(msg).build();
        errors.addError(error);
    }
}
