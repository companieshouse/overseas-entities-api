package uk.gov.companieshouse.overseasentitiesapi.validation.utils;

import org.apache.commons.lang3.StringUtils;
import uk.gov.companieshouse.overseasentitiesapi.utils.ApiLogger;
import uk.gov.companieshouse.service.rest.err.Errors;

import java.util.Objects;
import java.util.regex.Pattern;

import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators.setErrorMsgToLocation;
import static uk.gov.companieshouse.overseasentitiesapi.validation.utils.UtilsValidators.isNotNull;

public final class StringValidators {

    private static final String REG_EXP_FOR_INVALID_CHARACTERS = "^[-,.:; 0-9A-Z&@$£¥€'\"«»?!/\\\\()\\[\\]{}<>*=#%+ÀÁÂÃÄÅĀĂĄÆǼÇĆĈĊČÞĎÐÈÉÊËĒĔĖĘĚĜĞĠĢĤĦÌÍÎÏĨĪĬĮİĴĶĹĻĽĿŁÑŃŅŇŊÒÓÔÕÖØŌŎŐǾŒŔŖŘŚŜŞŠŢŤŦÙÚÛÜŨŪŬŮŰŲŴẀẂẄỲÝŶŸŹŻŽa-zſƒǺàáâãäåāăąæǽçćĉċčþďðèéêëēĕėęěĝģğġĥħìíîïĩīĭįĵķĺļľŀłñńņňŋòóôõöøōŏőǿœŕŗřśŝşšţťŧùúûüũūŭůűųŵẁẃẅỳýŷÿźżž]*$";

    private StringValidators() {}

    public static boolean isNotBlank(String toTest, String qualifiedFieldName, Errors errs, String loggingContext) {
        return isNotNull(toTest, qualifiedFieldName, errs, loggingContext)
                && isNotEmpty(toTest, qualifiedFieldName, errs, loggingContext);
    }

    public static boolean isLessThanOrEqualToMaxLength(String toTest, Integer maxLength, String qualifiedFieldName, Errors errs, String loggingContext) {
        if (toTest.length() > maxLength) {
            setErrorMsgToLocation(errs, qualifiedFieldName,
                    qualifiedFieldName + ValidationMessages.MAX_LENGTH_EXCEEDED_ERROR_MESSAGE.replace("%s", maxLength.toString()));
            ApiLogger.infoContext(loggingContext, "Invalid length for " + qualifiedFieldName);
            return false;
        }
        return true;
    }

    public static boolean isValidCharacters(String toTest, String qualifiedFieldName, Errors errs, String loggingContext) {
        var pattern = Pattern.compile(REG_EXP_FOR_INVALID_CHARACTERS);
        var matcher = pattern.matcher(toTest);

        if (!matcher.matches()) {
            setErrorMsgToLocation(errs, qualifiedFieldName, String.format(ValidationMessages.INVALID_CHARACTERS_ERROR_MESSAGE, qualifiedFieldName));
            ApiLogger.infoContext(loggingContext, "Invalid characters for " + qualifiedFieldName);
            return false;
        }
        return true;
    }


    /**
     * This is the same regular expression as used by the CH account service
     * @param email
     * @param qualifiedFieldName
     * @param errs
     * @param loggingContext
     * @return
     */
    public static boolean isValidEmailAddress(String email, String qualifiedFieldName, Errors errs, String loggingContext) {

        var regex = "^.+@.+\\..+$";
        var pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            setErrorMsgToLocation(errs, qualifiedFieldName, String.format(ValidationMessages.INVALID_EMAIL_ERROR_MESSAGE, qualifiedFieldName));
            ApiLogger.infoContext(loggingContext, "Email address is not in the correct format for " + qualifiedFieldName);
            return false;
        }
        return true;
    }

    public static boolean isNotEmpty(String toTest, String qualifiedFieldName, Errors errs, String loggingContext) {
        if (toTest.trim().isEmpty()) {
            setErrorMsgToLocation(errs, qualifiedFieldName, String.format(ValidationMessages.NOT_EMPTY_ERROR_MESSAGE, qualifiedFieldName));
            ApiLogger.infoContext(loggingContext, qualifiedFieldName + " Field is empty");
            return false;
        }
        return true;
    }

    public static void checkIsEmpty(String toTest, String qualifiedFieldName, Errors errs, String loggingContext) {
        if (Objects.nonNull(toTest) && !toTest.trim().isEmpty()) {
            setErrorMsgToLocation(errs, qualifiedFieldName, String.format(ValidationMessages.SHOULD_NOT_BE_POPULATED_ERROR_MESSAGE, qualifiedFieldName));
            ApiLogger.infoContext(loggingContext, qualifiedFieldName + " Field should not be populated");
        }
    }

    public static boolean checkIsNotEqual(String string1, String string2, String errorMsg, String qualifiedFieldName, Errors errors, String loggingContext) {
        if (StringUtils.equals(string1, string2)) {
            setErrorMsgToLocation(errors, qualifiedFieldName, String.format(errorMsg, qualifiedFieldName));
            ApiLogger.infoContext(loggingContext, String.format(errorMsg, qualifiedFieldName));
            return false;
        }
        return true;
    }
}
