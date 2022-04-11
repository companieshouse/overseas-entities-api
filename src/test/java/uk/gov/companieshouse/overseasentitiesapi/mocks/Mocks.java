package uk.gov.companieshouse.overseasentitiesapi.mocks;

import uk.gov.companieshouse.overseasentitiesapi.mapper.OverseasEntityDtoDaoMapper;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.OverseasEntitySubmissionDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.PresenterDto;

public class Mocks {

    public static OverseasEntitySubmissionDto buildSubmissionDto() {
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        EntityDto entity = buildEntityDto();
        overseasEntitySubmissionDto.setEntity(entity);
        PresenterDto presenter = buildPresenterDto();
        overseasEntitySubmissionDto.setPresenter(presenter);
        return overseasEntitySubmissionDto;
    }

    private static EntityDto buildEntityDto() {
        EntityDto entityDto = new EntityDto();
        entityDto.setName("Joe Bloggs Ltd");
        entityDto.setEmail("example@test123.co.uk");
        entityDto.setIncorporationCountry("Eutopia");
        entityDto.setLawGoverned("The law");
        entityDto.setLegalForm("Legal form");
        entityDto.setPublicRegisterName("JB");
        entityDto.setPrincipalAddress(buildAddressDto());
        entityDto.setServiceAddress(buildAddressDto());
        entityDto.setServiceAddressSameAsPrincipalAddress(true);
        return entityDto;
    }

    private static PresenterDto buildPresenterDto() {
        PresenterDto presenterDto = new PresenterDto();
        presenterDto.setFullName("Joe Bloggs");
        presenterDto.setPhoneNumber("01234 567890");
        presenterDto.setAntiMoneyLaunderingRegistrationNumber("999");
        presenterDto.setRole("Coder");
        presenterDto.setRoleTitle("Digital developer");
        return presenterDto;
    }

    private static AddressDto buildAddressDto() {
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
}
