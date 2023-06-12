package uk.gov.companieshouse.overseasentitiesapi.utils;

import org.apache.commons.lang.StringUtils;
import uk.gov.companieshouse.api.model.officers.FormerNamesApi;

import java.util.List;

public class FormerNameConcatenator {
    private FormerNameConcatenator() {
        throw new IllegalAccessError("Use the static method designation");
    }

    public static String concatenateFormerNames(List<FormerNamesApi> formerNames) {
        var allFormerNames = StringUtils.EMPTY;

        if (formerNames != null) {
            for(FormerNamesApi formerName : formerNames){
                var forenames = formerName.getForenames();
                var surname = formerName.getForenames();
                var fullName = StringUtils.isNotEmpty(forenames) ? forenames.concat(" " + surname) : surname;

                if (StringUtils.isEmpty(allFormerNames) && fullName != null) {
                    allFormerNames = fullName;
                }
                else {
                    allFormerNames += ", " + fullName;
                }
            }
        }

        return allFormerNames;
    }
}
