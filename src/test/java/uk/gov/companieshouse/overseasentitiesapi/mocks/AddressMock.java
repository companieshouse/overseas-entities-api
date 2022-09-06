package uk.gov.companieshouse.overseasentitiesapi.mocks;

import uk.gov.companieshouse.overseasentitiesapi.model.dao.AddressDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;

public class AddressMock {

    public static final String PROPERTY_NAME_NUMBER = "100";
    public static final String LINE1 = "No Street";
    public static final String LINE2 = "";
    public static final String TOWN = "Notown";
    public static final String COUNTY = "Noshire";
    public static final String COUNTRY = "France";
    public static final String POST_CODE = "NOW 3RE";

    public static AddressDto getAddressDto() {
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

    public static AddressDao getAddressDao() {
        AddressDao addressDao = new AddressDao();
        addressDao.setPropertyNameNumber(PROPERTY_NAME_NUMBER);
        addressDao.setLine1(LINE1);
        addressDao.setLine2(LINE2);
        addressDao.setTown(TOWN);
        addressDao.setCounty(COUNTY);
        addressDao.setCountry(COUNTRY);
        addressDao.setPostcode(POST_CODE);
        return addressDao;
    }
}
