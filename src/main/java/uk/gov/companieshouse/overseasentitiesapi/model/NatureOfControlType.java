package uk.gov.companieshouse.overseasentitiesapi.model;

public enum NatureOfControlType {
    OVER_25_PERCENT_OF_SHARES("over_25_percent_of_shares"),
    OVER_25_PERCENT_OF_VOTING_RIGHTS("over_25_percent_of_voting_rights"),
    APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS("appoint_or_remove_majority_board_directors"),
    SIGNIFICANT_INFLUENCE_OR_CONTROL("significant_influence_or_control");

    private final String natureOfControl;

    NatureOfControlType(String natureOfControl) {
        this.natureOfControl = natureOfControl;
    }

    public static NatureOfControlType findByNatureOfControlTypeString(String natureOfControl) {
        for (NatureOfControlType type: values()) {
            if(type.natureOfControl.equals(natureOfControl)) {
                return type;
            }
        }
        return null;
    }
}
