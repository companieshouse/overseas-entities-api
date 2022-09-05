package uk.gov.companieshouse.overseasentitiesapi.validation.utils;

import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.util.regex.Pattern;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators.setErrorMsgToLocation;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators.isValidNotNull;

public final class StringValidators {

    private StringValidators() {}

    public static boolean isValidNotBlank(String toTest, String qualifiedFieldName, Errors errs, String loggingContext) {
        return isValidNotNull(toTest, qualifiedFieldName, errs, loggingContext) && isValidNotEmpty(toTest, qualifiedFieldName, errs, loggingContext);
    }

    public static boolean isValidMaxLength(String toTest, Integer maxLength, String qualifiedFieldName, Errors errs, String loggingContext) {
        if (toTest.length() > maxLength) {
            setErrorMsgToLocation(errs, qualifiedFieldName,
                    qualifiedFieldName + ValidationMessages.MAX_LENGTH_EXCEEDED_ERROR_MESSAGE.replace("%s", maxLength.toString()));
            ApiLogger.infoContext(loggingContext, "Invalid length for " + qualifiedFieldName);
            return false;
        }
        return true;
    }

    public static boolean isValidCharacters(String toTest, String qualifiedFieldName, Errors errs, String loggingContext) {
        var regex = "^[-,.:; 0-9A-Z&@$£¥€'\"«»?!/\\\\()\\[\\]{}<>*=#%+ÀÁÂÃÄÅĀĂĄÆǼÇĆĈĊČÞĎÐÈÉÊËĒĔĖĘĚĜĞĠĢĤĦÌÍÎÏĨĪĬĮİĴĶĹĻĽĿŁÑŃŅŇŊÒÓÔÕÖØŌŎŐǾŒŔŖŘŚŜŞŠŢŤŦÙÚÛÜŨŪŬŮŰŲŴẀẂẄỲÝŶŸŹŻŽa-zſƒǺàáâãäåāăąæǽçćĉċčþďðèéêëēĕėęěĝģğġĥħìíîïĩīĭįĵķĺļľŀłñńņňŋòóôõöøōŏőǿœŕŗřśŝşšţťŧùúûüũūŭůűųŵẁẃẅỳýŷÿźżž]*$";

        var pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(toTest);

        if (!matcher.matches()) {
            setErrorMsgToLocation(errs, qualifiedFieldName, ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE.replace("%s", qualifiedFieldName));
            ApiLogger.infoContext(loggingContext, "Invalid characters for " + qualifiedFieldName);
            return false;
        }
        return true;
    }


    /**
     * The email regex is taken from OWASP https://owasp.org/www-community/OWASP_Validation_Regex_Repository
     * ^[a-zA-Z0-9_+&*-]+(?:\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\.)+[a-zA-Z]{2,15}$
     * Sonar has highlighted issues with the * and + grouping quantifiers causing backstepping (recursion)
     * so we are modifying them to be ++ and *+ (possessive = no backstepping)
     * @param email
     * @param qualifiedFieldName
     * @param errs
     * @param loggingContext
     * @return
     */
    public static boolean isValidEmailAddress(String email, String qualifiedFieldName, Errors errs, String loggingContext) {

        var regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*+@(?:[a-zA-Z0-9-]+\\.)++[a-zA-Z]{2,15}$";

        var pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            setErrorMsgToLocation(errs, qualifiedFieldName, ValidationMessages.INVALID_EMAIL_ERROR_MESSAGE.replace("%s", qualifiedFieldName));
            ApiLogger.infoContext(loggingContext, "Email address is not in the correct format for " + qualifiedFieldName);
            return false;
        }
        return true;
    }

    private static boolean isValidNotEmpty(String toTest, String qualifiedFieldName, Errors errs, String loggingContext) {
        if (toTest.trim().isEmpty()) {
            setErrorMsgToLocation(errs, qualifiedFieldName, ValidationMessages.NOT_EMPTY_ERROR_MESSAGE.replace("%s", qualifiedFieldName));
            ApiLogger.infoContext(loggingContext, qualifiedFieldName + " Field is empty");
            return false;
        }
        return true;
    }
}
