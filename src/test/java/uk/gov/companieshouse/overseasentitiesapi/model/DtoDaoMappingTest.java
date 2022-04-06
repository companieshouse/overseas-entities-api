package uk.gov.companieshouse.overseasentitiesapi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.gov.companieshouse.overseasentitiesapi.mapper.OverseasEntityDtoDaoMapper;
import uk.gov.companieshouse.overseasentitiesapi.mapper.OverseasEntityDtoDaoMapperImpl;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.AddressDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.EntityDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.OverseasEntitySubmissionDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.PresenterDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.PresenterDto;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DtoDaoMappingTest {

    private OverseasEntityDtoDaoMapper overseasEntityDtoDaoMapper;

    @BeforeEach
    void init() {
        overseasEntityDtoDaoMapper = new OverseasEntityDtoDaoMapperImpl();
    }

    @Test
    void testDaoToDtoMappingIsSuccessful() {
        OverseasEntitySubmissionDao dao = getOverseasEntitySubmissionDao();

        OverseasEntitySubmissionDto dto = overseasEntityDtoDaoMapper.daoToDto((dao));

        assertContentIsEqual(dto, dao);
    }

    @Test
    void testDtoToDaoMappingIsSuccessful() {
        OverseasEntitySubmissionDto dto = getOverseasEntitySubmissionDto();

        OverseasEntitySubmissionDao dao = overseasEntityDtoDaoMapper.dtoToDao((dto));

        assertContentIsEqual(dto, dao);
    }

    private OverseasEntitySubmissionDao getOverseasEntitySubmissionDao() {
        OverseasEntitySubmissionDao overseasEntitySubmission = new OverseasEntitySubmissionDao();

        AddressDao address = new AddressDao();
        address.setPropertyNameNumber("name number");
        address.setLine1("line 1");
        address.setLine2("line 2");
        address.setTown("town");
        address.setCounty("county");
        address.setCountry("country");
        address.setPostcode("post code");

        EntityDao entity = new EntityDao();
        entity.setName("name");
        entity.setEmail("email");
        entity.setIncorporationCountry("country");
        entity.setLawGoverned("law governed");
        entity.setLegalForm("legal form");
        entity.setServiceAddressSameAsPrincipalAddress(true);
        entity.setRegistrationNumber("reg number");
        entity.setPublicRegisterEntityRegisteredOn(LocalDate.of(1994, 11, 23));
        entity.setServiceAddress(address);
        entity.setPrincipalAddress(address);

        overseasEntitySubmission.setEntity(entity);

        PresenterDao presenter = new PresenterDao();
        presenter.setFullName("full name");
        presenter.setPhoneNumber("phone number");
        presenter.setRole("role");
        presenter.setRoleTitle("role title");
        presenter.setAntiMoneyLaunderingRegistrationNumber("launder reg number");

        overseasEntitySubmission.setPresenter(presenter);

        return overseasEntitySubmission;
    }

    private OverseasEntitySubmissionDto getOverseasEntitySubmissionDto() {
        OverseasEntitySubmissionDto overseasEntitySubmission = new OverseasEntitySubmissionDto();

        AddressDto address = new AddressDto();
        address.setPropertyNameNumber("name number");
        address.setLine1("line 1");
        address.setLine2("line 2");
        address.setTown("town");
        address.setCounty("county");
        address.setCountry("country");
        address.setPostcode("post code");

        EntityDto entity = new EntityDto();
        entity.setName("name");
        entity.setEmail("email");
        entity.setIncorporationCountry("country");
        entity.setLawGoverned("law governed");
        entity.setLegalForm("legal form");
        entity.setServiceAddressSameAsPrincipalAddress(true);
        entity.setRegistrationNumber("reg number");
        entity.setPublicRegisterEntityRegisteredOn(LocalDate.of(1994, 11, 23));
        entity.setServiceAddress(address);
        entity.setPrincipalAddress(address);

        overseasEntitySubmission.setEntity(entity);

        PresenterDto presenter = new PresenterDto();
        presenter.setFullName("full name");
        presenter.setPhoneNumber("phone number");
        presenter.setRole("role");
        presenter.setRoleTitle("role title");
        presenter.setAntiMoneyLaunderingRegistrationNumber("launder reg number");

        overseasEntitySubmission.setPresenter(presenter);

        return overseasEntitySubmission;
    }

    private void assertContentIsEqual(OverseasEntitySubmissionDto dto, OverseasEntitySubmissionDao dao) {

        EntityDao entityDao = dao.getEntity();
        EntityDto entityDto = dto.getEntity();

        assertEquals(entityDto.getName(), entityDao.getName());
        assertEquals(entityDto.getEmail(), entityDao.getEmail());
        assertEquals(entityDto.getIncorporationCountry(), entityDao.getIncorporationCountry());
        assertEquals(entityDto.getLawGoverned(), entityDao.getLawGoverned());
        assertEquals(entityDto.getLegalForm(), entityDao.getLegalForm());
        assertEquals(entityDto.getRegistrationNumber(), entityDao.getRegistrationNumber());
        assertEquals(entityDto.getPublicRegisterEntityRegisteredOn(), entityDao.getPublicRegisterEntityRegisteredOn());
        assertEquals(entityDto.getServiceAddressSameAsPrincipalAddress(), entityDao.getServiceAddressSameAsPrincipalAddress());

        assertAddressesAreEqual(entityDto.getPrincipalAddress(), entityDao.getPrincipalAddress());
        assertAddressesAreEqual(entityDto.getServiceAddress(), entityDao.getServiceAddress());

        PresenterDao presenterDao = dao.getPresenter();
        PresenterDto presenterDto = dto.getPresenter();

        assertEquals(presenterDto.getFullName(), presenterDao.getFullName());
        assertEquals(presenterDto.getPhoneNumber(), presenterDao.getPhoneNumber());
        assertEquals(presenterDto.getRole(), presenterDao.getRole());
        assertEquals(presenterDto.getRoleTitle(), presenterDao.getRoleTitle());
        assertEquals(presenterDto.getAntiMoneyLaunderingRegistrationNumber(), presenterDao.getAntiMoneyLaunderingRegistrationNumber());
    }

    private void assertAddressesAreEqual(AddressDto dto, AddressDao dao) {
        assertEquals(dto.getPropertyNameNumber(), dao.getPropertyNameNumber());
        assertEquals(dto.getLine1(), dao.getLine1());
        assertEquals(dto.getLine2(), dao.getLine2());
        assertEquals(dto.getTown(), dao.getTown());
        assertEquals(dto.getCounty(), dao.getCounty());
        assertEquals(dto.getCountry(), dao.getCountry());
        assertEquals(dto.getPostcode(), dao.getPostcode());
    }
}
