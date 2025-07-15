package uk.gov.companieshouse.overseasentitiesapi.utils;

import org.apache.commons.lang3.StringUtils;
import uk.gov.companieshouse.api.model.officers.FormerNamesApi;

import java.util.List;

public class FormerNameConcatenation {
    private FormerNameConcatenation() {
    }

    public static String concatenateFormerNames(List<FormerNamesApi> formerNames) {
        var allFormerNames = new StringBuilder();

        if (formerNames != null) {
            for (FormerNamesApi formerName : formerNames) {
                if (!allFormerNames.isEmpty()) {
                    allFormerNames.append(", ");
                }

                var fullName = StringUtils.EMPTY;

                var forenames = formerName.getForenames();
                if (StringUtils.isNotEmpty(forenames)) {
                    fullName = forenames;
                }

                var surname = formerName.getSurname();
                if (StringUtils.isNotEmpty(surname)) {
                    fullName += " " + surname;
                }

                allFormerNames.append(fullName.trim());
            }
        }

        return allFormerNames.toString();
    }
}
