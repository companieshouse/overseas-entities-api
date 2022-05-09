package uk.gov.companieshouse.overseasentitiesapi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.gov.companieshouse.overseasentitiesapi.mapper.OverseasEntityDtoDaoMapper;
import uk.gov.companieshouse.overseasentitiesapi.mapper.OverseasEntityDtoDaoMapperImpl;
import uk.gov.companieshouse.overseasentitiesapi.mocks.BeneficialOwnerAllFieldsMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.ManagingOfficerMock;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.AddressDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.BeneficialOwnerCorporateDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.BeneficialOwnerGovernmentOrPublicAuthorityDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.BeneficialOwnerIndividualDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.EntityDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.ManagingOfficerIndividualDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.OverseasEntitySubmissionDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.PresenterDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerGovernmentOrPublicAuthorityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.PresenterDto;

import java.util.ArrayList;
import java.util.List;

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
        entity.setPublicRegisterName("reg name");
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

        overseasEntitySubmission.setBeneficialOwnersStatement(BeneficialOwnersStatementType.ALL_IDENTIFIED_ALL_DETAILS);

        List<BeneficialOwnerIndividualDao> beneficialOwnersIndividual = new ArrayList<>();
        BeneficialOwnerIndividualDao individualBo = BeneficialOwnerAllFieldsMock.getBeneficialOwnerIndividualDao();
        individualBo.setUsualResidentialAddress(address);
        individualBo.setServiceAddress(address);
        beneficialOwnersIndividual.add(individualBo);
        overseasEntitySubmission.setBeneficialOwnersIndividual(beneficialOwnersIndividual);

        List<BeneficialOwnerCorporateDao> beneficialOwnersCorporate = new ArrayList<>();
        BeneficialOwnerCorporateDao corporateBo = BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDao();
        corporateBo.setPrincipalAddress(address);
        corporateBo.setServiceAddress(address);
        beneficialOwnersCorporate.add(corporateBo);
        overseasEntitySubmission.setBeneficialOwnersCorporate(beneficialOwnersCorporate);

        List<BeneficialOwnerGovernmentOrPublicAuthorityDao> beneficialOwnersGovernmentOrPublicAuthority = new ArrayList<>();
        BeneficialOwnerGovernmentOrPublicAuthorityDao governmentBo =  BeneficialOwnerAllFieldsMock.getBeneficialOwnerGovernmentOrPublicAuthorityDao();
        governmentBo.setPrincipalAddress(address);
        governmentBo.setServiceAddress(address);
        beneficialOwnersGovernmentOrPublicAuthority.add(governmentBo);
        overseasEntitySubmission.setBeneficialOwnersGovernmentOrPublicAuthority(beneficialOwnersGovernmentOrPublicAuthority);

        List<ManagingOfficerIndividualDao> managingOfficersIndividualDao = new ArrayList<>();
        ManagingOfficerIndividualDao managingOI = ManagingOfficerMock.getManagingOfficerIndividualDao();
        managingOfficersIndividualDao.add(managingOI);
        overseasEntitySubmission.setManagingOfficersIndividual(managingOfficersIndividualDao);

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
        entity.setPublicRegisterName("reg name");
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

        overseasEntitySubmission.setBeneficialOwnersStatement(BeneficialOwnersStatementType.ALL_IDENTIFIED_ALL_DETAILS);

        List<BeneficialOwnerIndividualDto> beneficialOwnersIndividual = new ArrayList<>();
        BeneficialOwnerIndividualDto individualBo =  BeneficialOwnerAllFieldsMock.getBeneficialOwnerIndividualDto();
        individualBo.setUsualResidentialAddress(address);
        individualBo.setServiceAddress(address);
        beneficialOwnersIndividual.add(individualBo);
        overseasEntitySubmission.setBeneficialOwnersIndividual(beneficialOwnersIndividual);

        List<BeneficialOwnerCorporateDto> beneficialOwnersCorporate = new ArrayList<>();
        BeneficialOwnerCorporateDto coroporateBo = BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto();
        coroporateBo.setPrincipalAddress(address);
        coroporateBo.setServiceAddress(address);
        beneficialOwnersCorporate.add(coroporateBo);
        overseasEntitySubmission.setBeneficialOwnersCorporate(beneficialOwnersCorporate);

        List<BeneficialOwnerGovernmentOrPublicAuthorityDto> beneficialOwnersGovernmentOrPublicAuthority = new ArrayList<>();
        BeneficialOwnerGovernmentOrPublicAuthorityDto governmentBo = BeneficialOwnerAllFieldsMock.getBeneficialOwnerGovernmentOrPublicAuthorityDto();
        governmentBo.setPrincipalAddress(address);
        governmentBo.setServiceAddress(address);
        beneficialOwnersGovernmentOrPublicAuthority.add(governmentBo);
        overseasEntitySubmission.setBeneficialOwnersGovernmentOrPublicAuthority(beneficialOwnersGovernmentOrPublicAuthority);

        List<ManagingOfficerIndividualDto> managingOfficersIndividualDto = new ArrayList<>();
        ManagingOfficerIndividualDto managingOI = ManagingOfficerMock.getManagingOfficerIndividualDto();
        managingOfficersIndividualDto.add(managingOI);
        overseasEntitySubmission.setManagingOfficersIndividual(managingOfficersIndividualDto);

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
        assertEquals(entityDto.getPublicRegisterName(), entityDao.getPublicRegisterName());
        assertEquals(entityDto.isOnRegisterInCountryFormedIn(), entityDao.isOnRegisterInCountryFormedIn());
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

        assertEquals(dto.getBeneficialOwnersStatement(), dao.getBeneficialOwnersStatement());

        assertIndividualBeneficialOwnersAreEqual(dto, dao);
        assertCorprotateBeneficialOwnersAreEqual(dto, dao);
        assertGovernmentBeneficialOwnersAreEqual(dto, dao);
        assertManagingOfficerIndividualAreEqual(dto, dao);
    }

    private void assertIndividualBeneficialOwnersAreEqual(OverseasEntitySubmissionDto dto, OverseasEntitySubmissionDao dao) {
        BeneficialOwnerIndividualDao boDao = dao.getBeneficialOwnersIndividual().get(0);
        BeneficialOwnerIndividualDto boDto = dto.getBeneficialOwnersIndividual().get(0);
        assertEquals(boDto.getFirstName(), boDao.getFirstName());
        assertEquals(boDto.getLastName(), boDao.getLastName());
        assertEquals(boDto.getDateOfBirth(), boDao.getDateOfBirth());
        assertEquals(boDto.getNationality(), boDao.getNationality());
        assertEquals(boDto.getServiceAddressSameAsUsualResidentialAddress(),
                boDao.getServiceAddressSameAsUsualResidentialAddress());
        assertEquals(boDto.getStartDate(), boDao.getStartDate());
        assertEquals(boDto.getBeneficialOwnerNatureOfControlTypes(), boDao.getBeneficialOwnerNatureOfControlTypes());
        assertEquals(boDto.getTrusteesNatureOfControlTypes(), boDao.getTrusteesNatureOfControlTypes());
        assertEquals(boDto.getNonLegalFirmMembersNatureOfControlTypes(),
                boDao.getNonLegalFirmMembersNatureOfControlTypes());
        assertEquals(boDto.getOnSanctionsList(), boDao.getOnSanctionsList());
    }

    private void assertCorprotateBeneficialOwnersAreEqual(OverseasEntitySubmissionDto dto, OverseasEntitySubmissionDao dao)
    {
        BeneficialOwnerCorporateDao boDao = dao.getBeneficialOwnersCorporate().get(0);
        BeneficialOwnerCorporateDto boDto = dto.getBeneficialOwnersCorporate().get(0);
        assertEquals(boDto.getName(), boDao.getName());
        assertEquals(boDto.getServiceAddressSameAsPrincipalAddress(),
                boDao.getServiceAddressSameAsPrincipalAddress());
        assertEquals(boDto.getLegalForm(), boDao.getLegalForm());
        assertEquals(boDto.getLawGoverned(), boDao.getLawGoverned());
        assertEquals(boDto.getOnRegisterInCountryFormedIn(), boDao.getOnRegisterInCountryFormedIn());
        assertEquals(boDto.getPublicRegisterName(), boDao.getPublicRegisterName());
        assertEquals(boDto.getRegistrationNumber(), boDao.getRegistrationNumber());
        assertEquals(boDto.getStartDate(), boDao.getStartDate());
        assertEquals(boDto.getBeneficialOwnerNatureOfControlTypes(), boDao.getBeneficialOwnerNatureOfControlTypes());
        assertEquals(boDto.getTrusteesNatureOfControlTypes(), boDao.getTrusteesNatureOfControlTypes());
        assertEquals(boDto.getNonLegalFirmMembersNatureOfControlTypes(),
                boDao.getNonLegalFirmMembersNatureOfControlTypes());
        assertEquals(boDto.getOnSanctionsList(), boDao.getOnSanctionsList());
    }

    private void assertGovernmentBeneficialOwnersAreEqual(OverseasEntitySubmissionDto dto, OverseasEntitySubmissionDao dao) {
        BeneficialOwnerGovernmentOrPublicAuthorityDao boDao = dao.getBeneficialOwnersGovernmentOrPublicAuthority().get(0);
        BeneficialOwnerGovernmentOrPublicAuthorityDto boDto = dto.getBeneficialOwnersGovernmentOrPublicAuthority().get(0);
        assertEquals(boDto.getName(), boDao.getName());
        assertEquals(boDto.getServiceAddressSameAsPrincipalAddress(),
                boDao.getServiceAddressSameAsPrincipalAddress());
        assertEquals(boDto.getLegalForm(), boDao.getLegalForm());
        assertEquals(boDto.getLawGoverned(), boDao.getLawGoverned());
        assertEquals(boDto.getStartDate(), boDao.getStartDate());
        assertEquals(boDto.getBeneficialOwnerNatureOfControlTypes(), boDao.getBeneficialOwnerNatureOfControlTypes());
        assertEquals(boDto.getNonLegalFirmMembersNatureOfControlTypes(),
                boDao.getNonLegalFirmMembersNatureOfControlTypes());
    }

    private void assertManagingOfficerIndividualAreEqual(OverseasEntitySubmissionDto dto, OverseasEntitySubmissionDao dao) {
        ManagingOfficerIndividualDao moiDao = dao.getManagingOfficersIndividual().get(0);
        ManagingOfficerIndividualDto moiDto = dto.getManagingOfficersIndividual().get(0);

        assertEquals(moiDto.getFirstName(), moiDao.getFirstName());
        assertEquals(moiDto.getLastName(), moiDao.getLastName());
        assertEquals(moiDto.getHasFormerNames(), moiDao.getHasFormerNames());
        assertEquals(moiDto.getFormerNames(), moiDao.getFormerNames());
        assertEquals(moiDto.getDateOfBirth(), moiDao.getDateOfBirth());
        assertEquals(moiDto.getNationality(), moiDao.getNationality());
        assertEquals(moiDto.getServiceAddressSameAsUsualResidentialAddress(),
                moiDao.getServiceAddressSameAsUsualResidentialAddress());
        assertEquals(moiDto.getOccupation(), moiDao.getOccupation());
        assertEquals(moiDto.getRoleAndResponsibilities(), moiDao.getRoleAndResponsibilities());

        assertAddressesAreEqual(moiDto.getUsualResidentialAddress(), moiDao.getUsualResidentialAddress());
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
