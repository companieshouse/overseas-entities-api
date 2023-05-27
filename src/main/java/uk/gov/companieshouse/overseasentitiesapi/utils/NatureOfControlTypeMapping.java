package uk.gov.companieshouse.overseasentitiesapi.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType;

import java.util.EnumMap;
import java.util.Map;

import static uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType.OVER_25_PERCENT_OF_SHARES;
import static uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType.SIGNIFICANT_INFLUENCE_OR_CONTROL;
import static uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType.OVER_25_PERCENT_OF_VOTING_RIGHTS;
import static uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType.APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS;

public class NatureOfControlTypeMapping {
    static final Map<NatureOfControlType, String> OVERSEAS_ENTITIES_PERSON_MAP;
    static final Map<NatureOfControlType, String> OVERSEAS_ENTITIES_TRUST_MAP;
    static final Map<NatureOfControlType, String> OVERSEAS_ENTITIES_FIRM_MAP;

    static {
        OVERSEAS_ENTITIES_PERSON_MAP = new EnumMap<>(NatureOfControlType.class);
        OVERSEAS_ENTITIES_PERSON_MAP.put(OVER_25_PERCENT_OF_SHARES, "OE_OWNERSHIPOFSHARES_MORETHAN25PERCENT_AS_PERSON");
        OVERSEAS_ENTITIES_PERSON_MAP.put(OVER_25_PERCENT_OF_VOTING_RIGHTS, "OE_VOTINGRIGHTS_MORETHAN25PERCENT_AS_PERSON");
        OVERSEAS_ENTITIES_PERSON_MAP.put(APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, "OE_RIGHTTOAPPOINTANDREMOVEDIRECTORS_AS_PERSON");
        OVERSEAS_ENTITIES_PERSON_MAP.put(SIGNIFICANT_INFLUENCE_OR_CONTROL, "OE_SIGINFLUENCECONTROL_AS_PERSON");

        OVERSEAS_ENTITIES_TRUST_MAP = new EnumMap<>(NatureOfControlType.class);
        OVERSEAS_ENTITIES_TRUST_MAP.put(OVER_25_PERCENT_OF_SHARES, "OE_OWNERSHIPOFSHARES_MORETHAN25PERCENT_AS_TRUST");
        OVERSEAS_ENTITIES_TRUST_MAP.put(OVER_25_PERCENT_OF_VOTING_RIGHTS, "OE_VOTINGRIGHTS_MORETHAN25PERCENT_AS_TRUST");
        OVERSEAS_ENTITIES_TRUST_MAP.put(APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, "OE_RIGHTTOAPPOINTANDREMOVEDIRECTORS_AS_TRUST");
        OVERSEAS_ENTITIES_TRUST_MAP.put(SIGNIFICANT_INFLUENCE_OR_CONTROL, "OE_SIGINFLUENCECONTROL_AS_TRUST");

        OVERSEAS_ENTITIES_FIRM_MAP = new EnumMap<>(NatureOfControlType.class);
        OVERSEAS_ENTITIES_FIRM_MAP.put(OVER_25_PERCENT_OF_SHARES, "OE_OWNERSHIPOFSHARES_MORETHAN25PERCENT_AS_FIRM");
        OVERSEAS_ENTITIES_FIRM_MAP.put(OVER_25_PERCENT_OF_VOTING_RIGHTS, "OE_VOTINGRIGHTS_MORETHAN25PERCENT_AS_FIRM");
        OVERSEAS_ENTITIES_FIRM_MAP.put(APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS, "OE_RIGHTTOAPPOINTANDREMOVEDIRECTORS_AS_FIRM");
        OVERSEAS_ENTITIES_FIRM_MAP.put(SIGNIFICANT_INFLUENCE_OR_CONTROL, "OE_SIGINFLUENCECONTROL_AS_FIRM");
    }

    private NatureOfControlTypeMapping() {
        throw new IllegalAccessError("Use the static method designation");
    }
    
    /**
     * Collects the nature of control types from the dto and maps them to the api values
     *
     * @param personNatureOfControlTypes
     * @param trusteesNatureOfControlTypes
     * @param firmNatureOfControlTypes
     * @return a list of nature of control types
     */
    public static List<String> collectAllNatureOfControlsIntoSingleList(
        List<NatureOfControlType> personNatureOfControlTypes,
        List<NatureOfControlType> trusteesNatureOfControlTypes,
        List<NatureOfControlType> firmNatureOfControlTypes
    ) {

        List<String> output = new ArrayList<>();

        if (personNatureOfControlTypes != null) {
            output.addAll(
                personNatureOfControlTypes.stream()
                    .map(NatureOfControlTypeMapping.OVERSEAS_ENTITIES_PERSON_MAP::get)
                    .collect(Collectors.toList())
            );
        }

        if (trusteesNatureOfControlTypes != null) {
            output.addAll(
                trusteesNatureOfControlTypes.stream()
                    .map(NatureOfControlTypeMapping.OVERSEAS_ENTITIES_TRUST_MAP::get)
                    .collect(Collectors.toList())
            );
        }

        if (firmNatureOfControlTypes != null) {
            output.addAll(
                firmNatureOfControlTypes.stream()
                    .map(NatureOfControlTypeMapping.OVERSEAS_ENTITIES_FIRM_MAP::get)
                    .collect(Collectors.toList())
            );
        }

        return output;
    }
}
