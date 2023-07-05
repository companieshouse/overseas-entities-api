package uk.gov.companieshouse.overseasentitiesapi.utils;

import org.apache.commons.lang.StringUtils;

public class NationalityOtherMapping {
    private NationalityOtherMapping() {
        throw new IllegalAccessError("Use the static method designation");
    }

    public static String generateNationalityOtherField(String nationality, String secondNationality) {
        if (StringUtils.isEmpty(secondNationality)) {
            return nationality;
        } else {
            return String.format("%s, %s", nationality, secondNationality);
        }
    }

    public static String generateMoNationalityOtherField(String nationality, String secondNationality) {
        if (StringUtils.isEmpty(secondNationality)) {
            return nationality != null ? nationality.concat(","): nationality;
        } else {
            return String.format("%s,%s", nationality, secondNationality);
        }
    }
}
