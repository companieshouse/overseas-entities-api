package uk.gov.companieshouse.overseasentitiesapi.client;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.sdk.manager.ApiSdkManager;
import java.io.IOException;

@Component
public class ApiClientService {

    public ApiClient getOauthAuthenticatedClient(String ericPassThroughHeader) throws IOException {
        return ApiSdkManager.getSDK(ericPassThroughHeader);
    }
}
