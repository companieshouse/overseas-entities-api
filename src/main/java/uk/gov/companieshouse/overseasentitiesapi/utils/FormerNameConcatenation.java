package uk.gov.companieshouse.overseasentitiesapi.utils;

import org.apache.commons.lang.StringUtils;
import uk.gov.companieshouse.api.model.officers.FormerNamesApi;

import java.util.List;

public class FormerNameConcatenation {
    private FormerNameConcatenation() {
        throw new IllegalAccessError("Use the static method designation");
    }

    public static String concatenateFormerNames(List<FormerNamesApi> formerNames) {
        var allFormerNames = new StringBuilder();

        if (formerNames != null) {
            for(FormerNamesApi formerName : formerNames){
                var forenames = formerName.getForenames();
                var surname = formerName.getSurname();
                var fullName = StringUtils.isNotEmpty(forenames) ? forenames.concat(" " + surname) : surname;

                if (StringUtils.isEmpty(allFormerNames.toString()) && fullName != null) {
                    allFormerNames.append(fullName);
                }
                else {
                    allFormerNames.append(", " + fullName);
                }
            }
        }

        return allFormerNames.toString();
    }
}
