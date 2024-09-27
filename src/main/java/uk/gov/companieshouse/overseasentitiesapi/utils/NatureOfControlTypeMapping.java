package uk.gov.companieshouse.overseasentitiesapi.utils;

import static uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlJurisdictionType.ENGLAND_AND_WALES;
import static uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlJurisdictionType.NORTHERN_IRELAND;
import static uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlJurisdictionType.SCOTLAND;
import static uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType.APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS;
import static uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType.OVER_25_PERCENT_OF_SHARES;
import static uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType.OVER_25_PERCENT_OF_VOTING_RIGHTS;
import static uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType.SIGNIFICANT_INFLUENCE_OR_CONTROL;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlJurisdictionType;
import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType;

public class NatureOfControlTypeMapping {

  static final Map<NatureOfControlType, String> OVERSEAS_ENTITIES_PERSON_MAP;
  static final Map<NatureOfControlType, String> OVERSEAS_ENTITIES_TRUST_MAP;
  static final Map<NatureOfControlType, String> OVERSEAS_ENTITIES_CONTROL_OVER_TRUST_MAP;
  static final Map<NatureOfControlJurisdictionType, String> OVERSEAS_ENTITIES_PERSON_JURISDICTIONS_MAP;
  static final Map<NatureOfControlJurisdictionType, String> OVERSEAS_ENTITIES_OTHER_ENTITY_JURISDICTIONS_MAP;
  static final Map<NatureOfControlType, String> OVERSEAS_ENTITIES_CONTROL_OVER_FIRM_MAP;
  static final Map<NatureOfControlType, String> OVERSEAS_ENTITIES_FIRM_MAP;

  public static final Map<String, String> CHIPS_FORMAT_TO_PUBLIC_FORMAT = Map.ofEntries(
          Map.entry("OE_OWNERSHIPOFSHARES_MORETHAN25PERCENT_AS_PERSON", "ownership-of-shares-more-than-25-percent-registered-overseas-entity"),
          Map.entry("OE_VOTINGRIGHTS_MORETHAN25PERCENT_AS_PERSON", "voting-rights-more-than-25-percent-registered-overseas-entity"),
          Map.entry("OE_RIGHTTOAPPOINTANDREMOVEDIRECTORS_AS_PERSON", "right-to-appoint-and-remove-directors-registered-overseas-entity"),
          Map.entry("OE_SIGINFLUENCECONTROL_AS_PERSON", "significant-influence-or-control-registered-overseas-entity"),

          Map.entry("OE_OWNERSHIPOFSHARES_MORETHAN25PERCENT_AS_TRUST", "ownership-of-shares-more-than-25-percent-as-trust-registered-overseas-entity"),
          Map.entry("OE_VOTINGRIGHTS_MORETHAN25PERCENT_AS_TRUST", "voting-rights-more-than-25-percent-as-trust-registered-overseas-entity"),
          Map.entry("OE_RIGHTTOAPPOINTANDREMOVEDIRECTORS_AS_TRUST", "right-to-appoint-and-remove-directors-as-trust-registered-overseas-entity"),
          Map.entry("OE_SIGINFLUENCECONTROL_AS_TRUST", "significant-influence-or-control-as-trust-registered-overseas-entity"),

          Map.entry("OE_OWNERSHIPOFSHARES_MORETHAN25PERCENT_AS_CONTROLOVERTRUST", "ownership-of-shares-more-than-25-percent-as-control-over-trust-registered-overseas-entity"),
          Map.entry("OE_VOTINGRIGHTS_MORETHAN25PERCENT_AS_CONTROLOVERTRUST", "voting-rights-more-than-25-percent-as-control-over-trust-registered-overseas-entity"),
          Map.entry("OE_RIGHTTOAPPOINTANDREMOVEDIRECTORS_AS_CONTROLOVERTRUST", "right-to-appoint-and-remove-directors-as-control-over-trust-registered-overseas-entity"),
          Map.entry("OE_SIGINFLUENCECONTROL_AS_CONTROLOVERTRUST", "significant-influence-or-control-as-control-over-trust-registered-overseas-entity"),

          Map.entry("OE_REGOWNER_AS_NOMINEEPERSON_ENGLANDWALES", "registered-owner-as-jurisdiction-person-england-and-wales"),
          Map.entry("OE_REGOWNER_AS_NOMINEEPERSON_SCOTLAND", "registered-owner-as-jurisdiction-person-scotland"),
          Map.entry("OE_REGOWNER_AS_NOMINEEPERSON_NORTHERNIRELAND", "registered-owner-as-jurisdiction-person-northern-ireland"),

          Map.entry("OE_REGOWNER_AS_NOMINEEANOTHERENTITY_ENGLANDWALES", "registered-owner-as-jurisdiction-other-entity-england-and-wales"),
          Map.entry("OE_REGOWNER_AS_NOMINEEANOTHERENTITY_SCOTLAND", "registered-owner-as-jurisdiction-other-entity-scotland"),
          Map.entry("OE_REGOWNER_AS_NOMINEEANOTHERENTITY_NORTHERNIRELAND", "registered-owner-as-jurisdiction-other-entity-northern-ireland"),

          Map.entry("OE_OWNERSHIPOFSHARES_MORETHAN25PERCENT_AS_CONTROLOVERFIRM", "ownership-of-shares-more-than-25-percent-as-control-over-firm-registered-overseas-entity"),
          Map.entry("OE_VOTINGRIGHTS_MORETHAN25PERCENT_AS_CONTROLOVERFIRM", "voting-rights-more-than-25-percent-as-control-over-firm-registered-overseas-entity"),
          Map.entry("OE_RIGHTTOAPPOINTANDREMOVEDIRECTORS_AS_CONTROLOVERFIRM", "right-to-appoint-and-remove-directors-as-control-over-firm-registered-overseas-entity"),
          Map.entry("OE_SIGINFLUENCECONTROL_AS_CONTROLOVERFIRM", "significant-influence-or-control-as-control-over-firm-registered-overseas-entity"),

          Map.entry("OE_OWNERSHIPOFSHARES_MORETHAN25PERCENT_AS_FIRM", "ownership-of-shares-more-than-25-percent-as-firm-registered-overseas-entity"),
          Map.entry("OE_VOTINGRIGHTS_MORETHAN25PERCENT_AS_FIRM", "voting-rights-more-than-25-percent-as-firm-registered-overseas-entity"),
          Map.entry("OE_RIGHTTOAPPOINTANDREMOVEDIRECTORS_AS_FIRM", "right-to-appoint-and-remove-directors-as-firm-registered-overseas-entity"),
          Map.entry("OE_SIGINFLUENCECONTROL_AS_FIRM", "significant-influence-or-control-as-firm-registered-overseas-entity")
  );

  static {
    OVERSEAS_ENTITIES_PERSON_MAP = new EnumMap<>(NatureOfControlType.class);
    OVERSEAS_ENTITIES_PERSON_MAP.put(OVER_25_PERCENT_OF_SHARES,
        "OE_OWNERSHIPOFSHARES_MORETHAN25PERCENT_AS_PERSON");
    OVERSEAS_ENTITIES_PERSON_MAP.put(OVER_25_PERCENT_OF_VOTING_RIGHTS,
        "OE_VOTINGRIGHTS_MORETHAN25PERCENT_AS_PERSON");
    OVERSEAS_ENTITIES_PERSON_MAP.put(APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS,
        "OE_RIGHTTOAPPOINTANDREMOVEDIRECTORS_AS_PERSON");
    OVERSEAS_ENTITIES_PERSON_MAP.put(SIGNIFICANT_INFLUENCE_OR_CONTROL,
        "OE_SIGINFLUENCECONTROL_AS_PERSON");

    OVERSEAS_ENTITIES_TRUST_MAP = new EnumMap<>(NatureOfControlType.class);
    OVERSEAS_ENTITIES_TRUST_MAP.put(OVER_25_PERCENT_OF_SHARES,
        "OE_OWNERSHIPOFSHARES_MORETHAN25PERCENT_AS_TRUST");
    OVERSEAS_ENTITIES_TRUST_MAP.put(OVER_25_PERCENT_OF_VOTING_RIGHTS,
        "OE_VOTINGRIGHTS_MORETHAN25PERCENT_AS_TRUST");
    OVERSEAS_ENTITIES_TRUST_MAP.put(APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS,
        "OE_RIGHTTOAPPOINTANDREMOVEDIRECTORS_AS_TRUST");
    OVERSEAS_ENTITIES_TRUST_MAP.put(SIGNIFICANT_INFLUENCE_OR_CONTROL,
        "OE_SIGINFLUENCECONTROL_AS_TRUST");

    OVERSEAS_ENTITIES_CONTROL_OVER_TRUST_MAP = new EnumMap<>(NatureOfControlType.class);
    OVERSEAS_ENTITIES_CONTROL_OVER_TRUST_MAP.put(OVER_25_PERCENT_OF_SHARES,
            "OE_OWNERSHIPOFSHARES_MORETHAN25PERCENT_AS_CONTROLOVERTRUST");
    OVERSEAS_ENTITIES_CONTROL_OVER_TRUST_MAP.put(OVER_25_PERCENT_OF_VOTING_RIGHTS,
            "OE_VOTINGRIGHTS_MORETHAN25PERCENT_AS_CONTROLOVERTRUST");
    OVERSEAS_ENTITIES_CONTROL_OVER_TRUST_MAP.put(APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS,
            "OE_RIGHTTOAPPOINTANDREMOVEDIRECTORS_AS_CONTROLOVERTRUST");
    OVERSEAS_ENTITIES_CONTROL_OVER_TRUST_MAP.put(SIGNIFICANT_INFLUENCE_OR_CONTROL,
            "OE_SIGINFLUENCECONTROL_AS_CONTROLOVERTRUST");

    OVERSEAS_ENTITIES_PERSON_JURISDICTIONS_MAP = new EnumMap<>(NatureOfControlJurisdictionType.class);
    OVERSEAS_ENTITIES_PERSON_JURISDICTIONS_MAP.put(ENGLAND_AND_WALES, "OE_REGOWNER_AS_NOMINEEPERSON_ENGLANDWALES");
    OVERSEAS_ENTITIES_PERSON_JURISDICTIONS_MAP.put(SCOTLAND, "OE_REGOWNER_AS_NOMINEEPERSON_SCOTLAND");
    OVERSEAS_ENTITIES_PERSON_JURISDICTIONS_MAP.put(NORTHERN_IRELAND, "OE_REGOWNER_AS_NOMINEEPERSON_NORTHERNIRELAND");

    OVERSEAS_ENTITIES_OTHER_ENTITY_JURISDICTIONS_MAP = new EnumMap<>(NatureOfControlJurisdictionType.class);
    OVERSEAS_ENTITIES_OTHER_ENTITY_JURISDICTIONS_MAP.put(ENGLAND_AND_WALES, "OE_REGOWNER_AS_NOMINEEANOTHERENTITY_ENGLANDWALES");
    OVERSEAS_ENTITIES_OTHER_ENTITY_JURISDICTIONS_MAP.put(SCOTLAND, "OE_REGOWNER_AS_NOMINEEANOTHERENTITY_SCOTLAND");
    OVERSEAS_ENTITIES_OTHER_ENTITY_JURISDICTIONS_MAP.put(NORTHERN_IRELAND, "OE_REGOWNER_AS_NOMINEEANOTHERENTITY_NORTHERNIRELAND");

    OVERSEAS_ENTITIES_FIRM_MAP = new EnumMap<>(NatureOfControlType.class);
    OVERSEAS_ENTITIES_FIRM_MAP.put(OVER_25_PERCENT_OF_SHARES,
        "OE_OWNERSHIPOFSHARES_MORETHAN25PERCENT_AS_FIRM");
    OVERSEAS_ENTITIES_FIRM_MAP.put(OVER_25_PERCENT_OF_VOTING_RIGHTS,
        "OE_VOTINGRIGHTS_MORETHAN25PERCENT_AS_FIRM");
    OVERSEAS_ENTITIES_FIRM_MAP.put(APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS,
        "OE_RIGHTTOAPPOINTANDREMOVEDIRECTORS_AS_FIRM");
    OVERSEAS_ENTITIES_FIRM_MAP.put(SIGNIFICANT_INFLUENCE_OR_CONTROL,
        "OE_SIGINFLUENCECONTROL_AS_FIRM");

    OVERSEAS_ENTITIES_CONTROL_OVER_FIRM_MAP = new EnumMap<>(NatureOfControlType.class);
    OVERSEAS_ENTITIES_CONTROL_OVER_FIRM_MAP.put(OVER_25_PERCENT_OF_SHARES,
            "OE_OWNERSHIPOFSHARES_MORETHAN25PERCENT_AS_CONTROLOVERFIRM");
    OVERSEAS_ENTITIES_CONTROL_OVER_FIRM_MAP.put(OVER_25_PERCENT_OF_VOTING_RIGHTS,
            "OE_VOTINGRIGHTS_MORETHAN25PERCENT_AS_CONTROLOVERFIRM");
    OVERSEAS_ENTITIES_CONTROL_OVER_FIRM_MAP.put(APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS,
            "OE_RIGHTTOAPPOINTANDREMOVEDIRECTORS_AS_CONTROLOVERFIRM");
    OVERSEAS_ENTITIES_CONTROL_OVER_FIRM_MAP.put(SIGNIFICANT_INFLUENCE_OR_CONTROL,
            "OE_SIGINFLUENCECONTROL_AS_CONTROLOVERFIRM");
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
          List<NatureOfControlType> firmNatureOfControlTypes) {

    return collectAllNatureOfControlsIntoSingleList(
            personNatureOfControlTypes,
            trusteesNatureOfControlTypes,
            firmNatureOfControlTypes,
            null,
            null,
            null,
            null,
            false);
  }

  public static List<String> collectAllNatureOfControlsIntoSingleList(
      List<NatureOfControlType> personNatureOfControlTypes,
      List<NatureOfControlType> trusteesNatureOfControlTypes,
      List<NatureOfControlType> firmNatureOfControlTypes,
      List<NatureOfControlType> trustControlNatureOfControlTypes,
      List<NatureOfControlJurisdictionType> ownerOfLandPersonNatureOfControlJurisdictions,
      List<NatureOfControlJurisdictionType> ownerOfLandOtherEntityNatureOfControlJurisdictions,
      List<NatureOfControlType> firmControlNatureOfControlTypes,
      boolean isPropertyAndLandNocEnabled
  ) {

    List<String> output = new ArrayList<>();

    if (personNatureOfControlTypes != null) {
       var personList = personNatureOfControlTypes.stream()
           .map(NatureOfControlTypeMapping.OVERSEAS_ENTITIES_PERSON_MAP::get)
           .toList();
       output.addAll(personList);
    }

    if (trusteesNatureOfControlTypes != null) {
      output.addAll(
          trusteesNatureOfControlTypes.stream()
              .map(NatureOfControlTypeMapping.OVERSEAS_ENTITIES_TRUST_MAP::get)
              .toList()
      );
    }

    if (firmNatureOfControlTypes != null) {
      output.addAll(
              firmNatureOfControlTypes.stream()
                      .map(NatureOfControlTypeMapping.OVERSEAS_ENTITIES_FIRM_MAP::get)
                      .toList()
      );
    }

    if (isPropertyAndLandNocEnabled) {
       if (trustControlNatureOfControlTypes != null) {
           var trustControlList = trustControlNatureOfControlTypes.stream()
                    .map(NatureOfControlTypeMapping.OVERSEAS_ENTITIES_CONTROL_OVER_TRUST_MAP::get)
                    .toList();
           output.addAll(trustControlList);
       }

       if (ownerOfLandPersonNatureOfControlJurisdictions != null) {
           var ownerOfLandPersonList = ownerOfLandPersonNatureOfControlJurisdictions.stream()
                    .map(NatureOfControlTypeMapping.OVERSEAS_ENTITIES_PERSON_JURISDICTIONS_MAP::get)
                    .toList();
           output.addAll(ownerOfLandPersonList);
       }

       if (ownerOfLandOtherEntityNatureOfControlJurisdictions != null) {
           var ownerOfLandOtherEntityList = ownerOfLandOtherEntityNatureOfControlJurisdictions.stream()
                     .map(NatureOfControlTypeMapping.OVERSEAS_ENTITIES_OTHER_ENTITY_JURISDICTIONS_MAP::get)
                     .toList();
           output.addAll(ownerOfLandOtherEntityList);
       }

       if (firmControlNatureOfControlTypes != null) {
           var firmControlList = firmControlNatureOfControlTypes.stream()
                     .map(NatureOfControlTypeMapping.OVERSEAS_ENTITIES_CONTROL_OVER_FIRM_MAP::get)
                     .toList();
           output.addAll(firmControlList);
       }
    }
    return output;
  }
}