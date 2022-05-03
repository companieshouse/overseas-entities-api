package uk.gov.companieshouse.overseasentitiesapi.mocks;

import uk.gov.companieshouse.overseasentitiesapi.model.dao.AddressDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;

public class AddressMock {
    public static AddressDto getAddressDto() {
        AddressDto addressDto = new AddressDto();
        addressDto.setPropertyNameNumber("100");
        addressDto.setLine1("No Street");
        addressDto.setLine2("");
        addressDto.setTown("Notown");
        addressDto.setCounty("Noshire");
        addressDto.setCountry("Eutopia");
        addressDto.setPostcode("NOW 3RE");
        return addressDto;
    }

    public static AddressDao getAddressDao() {
        AddressDao addressDao = new AddressDao();
        addressDao.setPropertyNameNumber("100");
        addressDao.setLine1("No Street");
        addressDao.setLine2("");
        addressDao.setTown("Notown");
        addressDao.setCounty("Noshire");
        addressDao.setCountry("Eutopia");
        addressDao.setPostcode("NOW 3RE");
        return addressDao;
    }
}
