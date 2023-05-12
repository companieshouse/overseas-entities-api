package uk.gov.companieshouse.overseasentitiesapi.mocks;

import org.apache.commons.lang3.tuple.Pair;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.update.OverseasEntityDataApi;

public class CollatedOverseasEntityDataMock {

    public static Pair<CompanyProfileApi, OverseasEntityDataApi> existingRegistration getCollatedOverseasEntityMock() {
        AddressDto addressDto = new AddressDto();
        addressDto.setPropertyNameNumber(PROPERTY_NAME_NUMBER);
        addressDto.setLine1(LINE1);
        addressDto.setLine2(LINE2);
        addressDto.setTown(TOWN);
        addressDto.setCounty(COUNTY);
        addressDto.setCountry(COUNTRY);
        addressDto.setPostcode(POST_CODE);
        return addressDto;
    }
}
