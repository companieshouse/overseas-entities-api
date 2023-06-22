package uk.gov.companieshouse.overseasentitiesapi.utils;

import org.apache.commons.lang3.tuple.Pair;
import uk.gov.companieshouse.api.model.beneficialowner.PrivateBoDataApi;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.managingofficerdata.ManagingOfficerDataApi;
import uk.gov.companieshouse.api.model.officers.CompanyOfficerApi;
import uk.gov.companieshouse.api.model.psc.PscApi;
import uk.gov.companieshouse.api.model.update.OverseasEntityDataApi;

import java.util.Map;

public class MissingPublicPrivateDataHandler {
    public static final String OE = "overseas entity";
    public static final String BO = "beneficial owner";
    public static final String MO = "managing officer";
    public static final String NO_PUBLIC_AND_NO_PRIVATE_DATA_FOUND = "No public and no private data found for  %s";
    public static final String NO_PUBLIC_DATA_FOUND = "No public data found for %s";
    public static final String NO_PRIVATE_DATA_FOUND = "No private data found for %s";

    public static boolean containsMissingOePublicPrivateData(
            Pair<CompanyProfileApi, OverseasEntityDataApi> publicPrivateData, String service, Map<String, Object> logMap) {
        return containsMissingData(publicPrivateData, service, OE, logMap);
    }

    public static boolean containsMissingBoPublicPrivateData(
            Pair<PscApi, PrivateBoDataApi> publicPrivateData, String service, Map<String, Object> logMap) {
        return containsMissingData(publicPrivateData, service, BO, logMap);
    }

    public static boolean containsMissingMoPublicPrivateData(
            Pair<CompanyOfficerApi, ManagingOfficerDataApi> publicPrivateData, String service, Map<String, Object> logMap) {
        return containsMissingData(publicPrivateData, service, MO, logMap);
    }

    private static boolean containsMissingData(Pair publicPrivateData, String service, String dataType, Map<String, Object> logMap){
        boolean containsMissingData = false;

        if (publicPrivateData == null) {
            ApiLogger.errorContext(service, String.format(NO_PUBLIC_AND_NO_PRIVATE_DATA_FOUND, dataType), null, logMap);
            containsMissingData = true;
        }

        if (publicPrivateData.getLeft() == null) {
            ApiLogger.errorContext(service, String.format(NO_PUBLIC_DATA_FOUND, dataType), null, logMap);
            containsMissingData = true;
        }

        if (publicPrivateData.getRight() == null) {
            ApiLogger.errorContext(service, String.format(NO_PRIVATE_DATA_FOUND, dataType), null, logMap);
            containsMissingData = true;
        }

        return containsMissingData;
    }

    private MissingPublicPrivateDataHandler() {
    }
}
