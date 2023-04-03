package uk.gov.companieshouse.overseasentitiesapi.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum BeneficialOwnersStatementType {

    ALL_IDENTIFIED_ALL_DETAILS("all_identified_all_details"),
    SOME_IDENTIFIED_ALL_DETAILS("some_identified_all_details"),
    NONE_IDENTIFIED("none_identified");

   private final String beneficialOwnersStatement;

   BeneficialOwnersStatementType(String beneficialOwnersStatement) {
        this.beneficialOwnersStatement = beneficialOwnersStatement;
    }

    /**
     * Json creator allows non-matching enum values to be interpreted by the method which determines them as null
     * @param beneficialOwnersStatement
     * @return
     */
   @JsonCreator
   public static BeneficialOwnersStatementType findByBeneficialOwnersStatementTypeString(String beneficialOwnersStatement) {
       for (BeneficialOwnersStatementType type: values()) {
           if(type.beneficialOwnersStatement.equals(beneficialOwnersStatement)) {
               return type;
           }
       }
       return null;
   }
}
