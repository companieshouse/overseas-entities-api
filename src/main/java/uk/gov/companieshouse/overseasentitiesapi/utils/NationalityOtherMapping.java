package uk.gov.companieshouse.overseasentitiesapi.utils;

import java.util.Objects;

public class NationalityOtherMapping {
    private NationalityOtherMapping() {
        throw new IllegalAccessError("Use the static method designation");
    }

    public static String generateNationalityOtherField(String nationality, String secondNationality) {
        if (Objects.isNull(secondNationality)) {
            return nationality;
        } else {
            return String.format("%s, %s", nationality, secondNationality);
        }
    }
}
