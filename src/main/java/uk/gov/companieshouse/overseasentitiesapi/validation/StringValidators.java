package uk.gov.companieshouse.overseasentitiesapi.validation;

import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.service.rest.err.Err;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringValidators {

    private StringValidators() {}

    public static boolean validateStringNotBlank(String toTest, String location, Errors errs, String loggingContext) {
        return validateNotNull(toTest, location, errs, loggingContext) && validateStringNotEmpty(toTest, location, errs, loggingContext);
    }

    public static boolean validateStringNotEmpty(String toTest, String location, Errors errs, String loggingContext) {
        if (toTest.trim().isEmpty()) {
            setErrorMsgToLocation(errs, location,location + ValidationMessages.NOT_EMPTY_ERROR_MESSAGE);
            ApiLogger.infoContext(loggingContext, location + " Field is empty");
            return false;
        }
        return true;
    }

    public static boolean validateNotNull(String toTest, String location, Errors errs, String loggingContext) {
        if (toTest == null) {
            setErrorMsgToLocation(errs, location,location + ValidationMessages.NOT_NULL_ERROR_MESSAGE);
            ApiLogger.infoContext(loggingContext , location + " Field is null");
            return false;
        }
        return true;
    }

    public static boolean validateLength(String toTest, Integer maxLength, String location, Errors errs, String loggingContext) {
        if (toTest.length() > maxLength) {
            setErrorMsgToLocation(errs, location,
                    location + ValidationMessages.MAX_LENGTH_EXCEEDED_ERROR_MESSAGE.replace("%s", maxLength.toString()));
            ApiLogger.infoContext(loggingContext, "Invalid length for " + location);
            return false;
        }
        return true;
    }

    public static boolean validateCharacters(String toTest, String location, Errors errs, String loggingContext) {
        String regex = "^[-,.:; 0-9A-Z&@$£¥€'\"«»''\"\"?!/\\\\()[\\]{}<>*=#%+ÀÁÂÃÄÅĀĂĄÆǼÇĆĈĊČÞĎÐÈÉÊËĒĔĖĘĚĜĞĠĢĤĦÌÍÎÏĨĪĬĮİĴĶĹĻĽĿŁÑŃŅŇŊÒÓÔÕÖØŌŎŐǾŒŔŖŘŚŜŞŠŢŤŦÙÚÛÜŨŪŬŮŰŲŴẀẂẄỲÝŶŸŹŻŽa-zÀÖØſƒǺẀỲàáâãäåāăąæǽçćĉċčþďðèéêëēĕėęěĝģğġĥħìíîïĩīĭįĵķĺļľŀłñńņňŋòóôõöøōŏőǿœŕŗřśŝşšţťŧùúûüũūŭůűųŵẁẃẅỳýŷÿźżž]*$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(toTest);

        if (!matcher.matches()) {
            setErrorMsgToLocation(errs, location, location + ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE);
            ApiLogger.infoContext(loggingContext, "Invalid characters for " + location);
            return false;
        }
        return true;
    }

    public static boolean validateEmail(String email, String location, Errors errs, String loggingContext) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            setErrorMsgToLocation(errs, location, ValidationMessages.INVALID_EMAIL_ERROR_MESSAGE.replace("%s", location));
            ApiLogger.infoContext(loggingContext, "Email address is not in the correct format for " + location);
            return false;
        }
        return true;
    }

    private static void setErrorMsgToLocation(Errors errors, String location, String msg){
        final Err error = Err.invalidBodyBuilderWithLocation(location).withError(msg).build();
        errors.addError(error);
    }
}
