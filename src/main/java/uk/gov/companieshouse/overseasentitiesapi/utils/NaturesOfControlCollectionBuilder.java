package uk.gov.companieshouse.overseasentitiesapi.utils;

import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlJurisdictionType;
import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType;

import java.util.List;

public class NaturesOfControlCollectionBuilder {

       private static NaturesOfControlCollectionBuilder naturesOfControlCollectionBuilder;

       private List<NatureOfControlType> personNatureOfControlTypes;
       private List<NatureOfControlType> trusteesNatureOfControlTypes;
       private List<NatureOfControlType> firmNatureOfControlTypes;
       private List<NatureOfControlType> trustControlNatureOfControlTypes;
       private List<NatureOfControlJurisdictionType> ownerOfLandPersonNatureOfControlJurisdictions;
       private List<NatureOfControlJurisdictionType> ownerOfLandOtherEntityNatureOfControlJurisdictions;
       private List<NatureOfControlType> firmControlNatureOfControlTypes;
       private boolean isPropertyAndLandNocEnabled;

       public static NaturesOfControlCollectionBuilder createNaturesOfControlCollectionBuilder() {
           if (naturesOfControlCollectionBuilder == null) {
                  naturesOfControlCollectionBuilder = new NaturesOfControlCollectionBuilder();
           }
           return naturesOfControlCollectionBuilder;
       }

       public NaturesOfControlCollectionBuilder addPersonType(List<NatureOfControlType> personNatureOfControlTypes) {
           if (naturesOfControlCollectionBuilder != null) {
              naturesOfControlCollectionBuilder.personNatureOfControlTypes = personNatureOfControlTypes;
           }
           return naturesOfControlCollectionBuilder;
       }
       public NaturesOfControlCollectionBuilder addTrusteesType(List<NatureOfControlType> trusteesNatureOfControlTypes) {
          if (naturesOfControlCollectionBuilder != null) {
              naturesOfControlCollectionBuilder.trusteesNatureOfControlTypes = trusteesNatureOfControlTypes;
          }
          return naturesOfControlCollectionBuilder;
       }
       public NaturesOfControlCollectionBuilder addFirmType(List<NatureOfControlType> firmNatureOfControlTypes) {
          if (naturesOfControlCollectionBuilder != null) {
              naturesOfControlCollectionBuilder.firmNatureOfControlTypes = firmNatureOfControlTypes;
          }
          return naturesOfControlCollectionBuilder;
       }
       public NaturesOfControlCollectionBuilder addTrustType(List<NatureOfControlType> trustControlNatureOfControlTypes) {
           if (naturesOfControlCollectionBuilder != null) {
                naturesOfControlCollectionBuilder.trustControlNatureOfControlTypes = trustControlNatureOfControlTypes;
           }
           return naturesOfControlCollectionBuilder;
       }
       public NaturesOfControlCollectionBuilder addOwnerOfLandPerson(List<NatureOfControlJurisdictionType> ownerOfLandPersonNatureOfControlJurisdictions) {
           if (naturesOfControlCollectionBuilder != null) {
               naturesOfControlCollectionBuilder.ownerOfLandPersonNatureOfControlJurisdictions = ownerOfLandPersonNatureOfControlJurisdictions;
           }
           return naturesOfControlCollectionBuilder;
       }
       public NaturesOfControlCollectionBuilder addOwnerOfLandOtherEntity(List<NatureOfControlJurisdictionType> ownerOfLandOtherEntityNatureOfControlJurisdictions) {
            if (naturesOfControlCollectionBuilder != null) {
                naturesOfControlCollectionBuilder.ownerOfLandOtherEntityNatureOfControlJurisdictions = ownerOfLandOtherEntityNatureOfControlJurisdictions;
            }
           return naturesOfControlCollectionBuilder;
       }
       public NaturesOfControlCollectionBuilder addFirmControlType(List<NatureOfControlType> firmControlNatureOfControlTypes) {
           if (naturesOfControlCollectionBuilder != null) {
               naturesOfControlCollectionBuilder.firmControlNatureOfControlTypes = firmControlNatureOfControlTypes;
           }
           return naturesOfControlCollectionBuilder;
       }
       public NaturesOfControlCollectionBuilder addFeatureFlag(boolean isPropertyAndLandNocEnabled) {
           if (naturesOfControlCollectionBuilder != null) {
               naturesOfControlCollectionBuilder.isPropertyAndLandNocEnabled = isPropertyAndLandNocEnabled;
           }
           return naturesOfControlCollectionBuilder;
       }

       public NaturesOfControlCollection build() {
           NaturesOfControlCollection naturesOfControlCollection = new NaturesOfControlCollection();
           naturesOfControlCollection.setPersonNatureOfControlTypes(personNatureOfControlTypes);
           naturesOfControlCollection.setTrusteesNatureOfControlTypes(trusteesNatureOfControlTypes);
           naturesOfControlCollection.setFirmNatureOfControlTypes(firmNatureOfControlTypes);
           naturesOfControlCollection.setTrustControlNatureOfControlTypes(trustControlNatureOfControlTypes);
           naturesOfControlCollection.setOwnerOfLandPersonNatureOfControlJurisdictions(ownerOfLandPersonNatureOfControlJurisdictions);
           naturesOfControlCollection.setOwnerOfLandOtherEntityNatureOfControlJurisdictions(ownerOfLandOtherEntityNatureOfControlJurisdictions);
           naturesOfControlCollection.setFirmControlNatureOfControlTypes(firmControlNatureOfControlTypes);
           naturesOfControlCollection.setPropertyAndLandNocEnabled(isPropertyAndLandNocEnabled);
           return naturesOfControlCollection;
       }


       public class NaturesOfControlCollection {

              private List<NatureOfControlType> personNatureOfControlTypes;
              private List<NatureOfControlType> trusteesNatureOfControlTypes;
              private List<NatureOfControlType> firmNatureOfControlTypes;
              private List<NatureOfControlType> trustControlNatureOfControlTypes;
              private List<NatureOfControlJurisdictionType> ownerOfLandPersonNatureOfControlJurisdictions;
              private List<NatureOfControlJurisdictionType> ownerOfLandOtherEntityNatureOfControlJurisdictions;
              private List<NatureOfControlType> firmControlNatureOfControlTypes;
              private boolean isPropertyAndLandNocEnabled;

              public List<NatureOfControlType> getPersonNatureOfControlTypes() {
                     return personNatureOfControlTypes;
              }

              public void setPersonNatureOfControlTypes(List<NatureOfControlType> personNatureOfControlTypes) {
                     this.personNatureOfControlTypes = personNatureOfControlTypes;
              }

              public List<NatureOfControlType> getTrusteesNatureOfControlTypes() {
                     return trusteesNatureOfControlTypes;
              }

              public void setTrusteesNatureOfControlTypes(List<NatureOfControlType> trusteesNatureOfControlTypes) {
                     this.trusteesNatureOfControlTypes = trusteesNatureOfControlTypes;
              }

              public List<NatureOfControlType> getFirmNatureOfControlTypes() {
                     return firmNatureOfControlTypes;
              }

              public void setFirmNatureOfControlTypes(List<NatureOfControlType> firmNatureOfControlTypes) {
                     this.firmNatureOfControlTypes = firmNatureOfControlTypes;
              }

              public List<NatureOfControlType> getTrustControlNatureOfControlTypes() {
                     return trustControlNatureOfControlTypes;
              }

              public void setTrustControlNatureOfControlTypes(List<NatureOfControlType> trustControlNatureOfControlTypes) {
                     this.trustControlNatureOfControlTypes = trustControlNatureOfControlTypes;
              }

              public List<NatureOfControlJurisdictionType> getOwnerOfLandPersonNatureOfControlJurisdictions() {
                     return ownerOfLandPersonNatureOfControlJurisdictions;
              }

              public void setOwnerOfLandPersonNatureOfControlJurisdictions(List<NatureOfControlJurisdictionType> ownerOfLandPersonNatureOfControlJurisdictions) {
                     this.ownerOfLandPersonNatureOfControlJurisdictions = ownerOfLandPersonNatureOfControlJurisdictions;
              }

              public List<NatureOfControlJurisdictionType> getOwnerOfLandOtherEntityNatureOfControlJurisdictions() {
                     return ownerOfLandOtherEntityNatureOfControlJurisdictions;
              }

              public void setOwnerOfLandOtherEntityNatureOfControlJurisdictions(List<NatureOfControlJurisdictionType> ownerOfLandOtherEntityNatureOfControlJurisdictions) {
                     this.ownerOfLandOtherEntityNatureOfControlJurisdictions = ownerOfLandOtherEntityNatureOfControlJurisdictions;
              }

              public List<NatureOfControlType> getFirmControlNatureOfControlTypes() {
                     return firmControlNatureOfControlTypes;
              }

              public void setFirmControlNatureOfControlTypes(List<NatureOfControlType> firmControlNatureOfControlTypes) {
                     this.firmControlNatureOfControlTypes = firmControlNatureOfControlTypes;
              }

              public boolean isPropertyAndLandNocEnabled() {
                     return isPropertyAndLandNocEnabled;
              }

              public void setPropertyAndLandNocEnabled(boolean propertyAndLandNocEnabled) {
                     isPropertyAndLandNocEnabled = propertyAndLandNocEnabled;
              }
       }
}
