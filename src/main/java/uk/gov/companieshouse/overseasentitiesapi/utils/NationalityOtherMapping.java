package uk.gov.companieshouse.overseasentitiesapi.utils;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;

public class NationalityOtherMapping {
    private NationalityOtherMapping() {
        throw new IllegalAccessError("Use the static method designation");
    }

    /**
     * Converts an array of nationality Strings into a comma-separated list, removing blanks and rogue commas.
     *
     * @param nationality Varargs parameter of nationality strings to be processed.
     * @return A comma-separated string without rogue commas and blank spaces, or an empty string if all inputs are blank.
     */
    public static String generateNationalityOtherField(String... nationality) {
        return Arrays.stream(nationality)
                .filter(StringUtils::isNotBlank)
                .map(String::trim)
                .map(s -> s.replaceAll(",{0,5}$|^,{0,5}", ""))
                .map(String::trim)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining(","));

    }
}
