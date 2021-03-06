package uk.gov.companieshouse.overseasentitiesapi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.gov.companieshouse.overseasentitiesapi.mapper.OverseasEntityDtoDaoMapper;
import uk.gov.companieshouse.overseasentitiesapi.mapper.OverseasEntityDtoDaoMapperImpl;
import uk.gov.companieshouse.overseasentitiesapi.mocks.OverseasEntityDueDiligenceMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.PresenterMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.TrustMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.ManagingOfficerMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.BeneficialOwnerAllFieldsMock;
import uk.gov.companieshouse.overseasentitiesapi.mocks.DueDiligenceMock;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.AddressDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.EntityDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.DueDiligenceDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.BeneficialOwnerCorporateDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.BeneficialOwnerIndividualDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.OverseasEntityDueDiligenceDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.PresenterDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.ManagingOfficerCorporateDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.ManagingOfficerIndividualDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.BeneficialOwnerGovernmentOrPublicAuthorityDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.HistoricalBeneficialOwnerDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.TrustCorporateDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.TrustDataDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.TrustIndividualDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.OverseasEntitySubmissionDao;

import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.DueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntityDueDiligenceDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.PresenterDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerGovernmentOrPublicAuthorityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.HistoricalBeneficialOwnerDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustDataDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustIndividualDto;

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

        PresenterDao presenter = PresenterMock.getPresenterDao();

        overseasEntitySubmission.setPresenter(presenter);

        overseasEntitySubmission.setBeneficialOwnersStatement(BeneficialOwnersStatementType.ALL_IDENTIFIED_ALL_DETAILS);

        DueDiligenceDao dueDiligenceDao = DueDiligenceMock.getDueDiligenceDao();
        overseasEntitySubmission.setDueDiligence(dueDiligenceDao);

        OverseasEntityDueDiligenceDao overseasEntityDueDiligenceDao = OverseasEntityDueDiligenceMock.getOverseasEntityDueDiligenceDao();
        overseasEntitySubmission.setOverseasEntityDueDiligence(overseasEntityDueDiligenceDao);

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

        List<ManagingOfficerCorporateDao> managingOfficersCorporateDao = new ArrayList<>();
        ManagingOfficerCorporateDao managingOC = ManagingOfficerMock.getManagingOfficerCorporateDao();
        managingOfficersCorporateDao.add(managingOC);
        overseasEntitySubmission.setManagingOfficersCorporate(managingOfficersCorporateDao);

        List<TrustDataDao> trustDataDao = new ArrayList<>();
        TrustDataDao trustData = TrustMock.getTrustDataDao();
        trustDataDao.add(trustData);
        overseasEntitySubmission.setTrusts(trustDataDao);


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

        PresenterDto presenter = PresenterMock.getPresenterDto();

        overseasEntitySubmission.setPresenter(presenter);

        DueDiligenceDto dueDiligenceDto = DueDiligenceMock.getDueDiligenceDto();
        overseasEntitySubmission.setDueDiligence(dueDiligenceDto);

        OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto = OverseasEntityDueDiligenceMock.getOverseasEntityDueDiligenceDto();
        overseasEntitySubmission.setOverseasEntityDueDiligence(overseasEntityDueDiligenceDto);

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

        List<ManagingOfficerCorporateDto> managingOfficersCorporateDto = new ArrayList<>();
        ManagingOfficerCorporateDto managingOC = ManagingOfficerMock.getManagingOfficerCorporateDto();
        managingOfficersCorporateDto.add(managingOC);
        overseasEntitySubmission.setManagingOfficersCorporate(managingOfficersCorporateDto);


        List<TrustDataDto> trustDataDto = new ArrayList<>();
        TrustDataDto trustData = TrustMock.getTrustDataDto();
        trustDataDto.add(trustData);
        overseasEntitySubmission.setTrusts(trustDataDto);

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
        assertEquals(presenterDto.getEmail(), presenterDao.getEmail());

        assertDueDiligenceIsEqual(dao.getDueDiligence(), dto.getDueDiligence());
        assertOverseasEntityDueDiligenceIsEqual(dao.getOverseasEntityDueDiligence(), dto.getOverseasEntityDueDiligence());

        assertEquals(dto.getBeneficialOwnersStatement(), dao.getBeneficialOwnersStatement());

        assertIndividualBeneficialOwnersAreEqual(dto, dao);
        assertCorporateBeneficialOwnersAreEqual(dto, dao);
        assertGovernmentBeneficialOwnersAreEqual(dto, dao);
        assertManagingOfficerIndividualAreEqual(dto, dao);
        assertManagingOfficerCorporateAreEqual(dto, dao);
        assertTrustsAreEqual(dto, dao);
    }

    private void assertDueDiligenceIsEqual(DueDiligenceDao dueDiligenceDao, DueDiligenceDto dueDiligenceDto) {
        assertEquals(dueDiligenceDto.getIdentityDate(), dueDiligenceDao.getIdentityDate());
        assertEquals(dueDiligenceDto.getName(), dueDiligenceDao.getName());
        assertEquals(dueDiligenceDto.getEmail(), dueDiligenceDao.getEmail());
        assertEquals(dueDiligenceDto.getSupervisoryName(), dueDiligenceDao.getSupervisoryName());
        assertEquals(dueDiligenceDto.getAmlNumber(), dueDiligenceDao.getAmlNumber());
        assertEquals( dueDiligenceDto.getAgentCode(), dueDiligenceDao.getAgentCode());
        assertEquals(dueDiligenceDto.getPartnerName(), dueDiligenceDao.getPartnerName());
        assertEquals( dueDiligenceDto.getDiligence(), dueDiligenceDao.getDiligence());
    }

    private void assertOverseasEntityDueDiligenceIsEqual(
            OverseasEntityDueDiligenceDao overseasEntityDueDiligenceDao, OverseasEntityDueDiligenceDto overseasEntityDueDiligenceDto) {

        assertEquals(overseasEntityDueDiligenceDto.getIdentityDate(), overseasEntityDueDiligenceDao.getIdentityDate());
        assertEquals(overseasEntityDueDiligenceDto.getName(), overseasEntityDueDiligenceDao.getName());
        assertEquals(overseasEntityDueDiligenceDto.getEmail(), overseasEntityDueDiligenceDao.getEmail());
        assertEquals(overseasEntityDueDiligenceDto.getSupervisoryName(), overseasEntityDueDiligenceDao.getSupervisoryName());
        assertEquals(overseasEntityDueDiligenceDto.getAmlNumber(), overseasEntityDueDiligenceDao.getAmlNumber());
        assertEquals(overseasEntityDueDiligenceDto.getPartnerName(), overseasEntityDueDiligenceDao.getPartnerName());

        assertAddressesAreEqual(overseasEntityDueDiligenceDto.getAddress(), overseasEntityDueDiligenceDao.getAddress());
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

    private void assertCorporateBeneficialOwnersAreEqual(OverseasEntitySubmissionDto dto, OverseasEntitySubmissionDao dao)
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
        assertEquals(boDto.getOnSanctionsList(), boDao.getOnSanctionsList());
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

    private void assertManagingOfficerCorporateAreEqual(OverseasEntitySubmissionDto dto, OverseasEntitySubmissionDao dao) {
        ManagingOfficerCorporateDao mocDao = dao.getManagingOfficersCorporate().get(0);
        ManagingOfficerCorporateDto mocDto = dto.getManagingOfficersCorporate().get(0);

        assertEquals(mocDto.getName(), mocDao.getName());
        assertEquals(mocDto.getServiceAddressSameAsPrincipalAddress(), mocDao.getServiceAddressSameAsPrincipalAddress());
        assertEquals(mocDto.getLegalForm(), mocDao.getLegalForm());
        assertEquals(mocDto.getLawGoverned(), mocDao.getLawGoverned());
        assertEquals(mocDto.getOnRegisterInCountryFormedIn(), mocDao.getOnRegisterInCountryFormedIn());
        assertEquals(mocDto.getPublicRegisterName(), mocDao.getPublicRegisterName());
        assertEquals(mocDto.getRegistrationNumber(), mocDao.getRegistrationNumber());
        assertEquals(mocDto.getRoleAndResponsibilities(), mocDao.getRoleAndResponsibilities());
        assertEquals(mocDto.getContactFullName(), mocDao.getContactFullName());
        assertEquals(mocDto.getContactEmail(), mocDao.getContactEmail());

        assertAddressesAreEqual(mocDto.getPrincipalAddress(), mocDao.getPrincipalAddress());
        assertAddressesAreEqual(mocDto.getServiceAddress(), mocDao.getServiceAddress());
    }

    private void assertTrustsAreEqual(OverseasEntitySubmissionDto dto, OverseasEntitySubmissionDao dao) {
        TrustDataDao trustDao = dao.getTrusts().get(0);
        TrustDataDto trustDto = dto.getTrusts().get(0);

        assertEquals(trustDto.getTrustName(), trustDao.getTrustName());
        assertEquals(trustDto.getCreationDate(), trustDao.getCreationDate());
        assertEquals(trustDto.getUnableToObtainAllTrustInfo(), trustDao.isUnableToObtainAllTrustInfo());

        assertTrustIndividualsAreEqual(trustDto.getIndividuals().get(0), trustDao.getIndividuals().get(0));
        assertTrustHistoricalBeneficialOwnerAreEqual(trustDto.getHistoricalBeneficialOwners().get(0), trustDao.getHistoricalBeneficialOwners().get(0));
        assertTrustCorporatesAreEqual(trustDto.getCorporates().get(0), trustDao.getCorporates().get(0) );
    }

    private void assertTrustIndividualsAreEqual(TrustIndividualDto dto, TrustIndividualDao dao) {
        assertEquals(dto.getDateBecameInterestedPerson(), dao.getDateBecameInterestedPerson());
        assertEquals(dto.getDateOfBirth(), dao.getDateOfBirth());
        assertEquals(dto.getNationality(), dao.getNationality());
        assertEquals(dto.getForename(), dao.getForename());
        assertEquals(dto.getOtherForenames(), dao.getOtherForenames());
        assertEquals(dto.getSurname(), dao.getSurname());
        assertEquals(dto.getType().toLowerCase(), dao.getType().getValue().toLowerCase());
    }

    private void assertTrustHistoricalBeneficialOwnerAreEqual(HistoricalBeneficialOwnerDto dto, HistoricalBeneficialOwnerDao dao) {
        assertEquals(dto.getForename(), dao.getForename());
        assertEquals(dto.getOtherForenames(), dao.getOtherForenames());
        assertEquals(dto.getSurname(), dao.getSurname());
        assertEquals(dto.getCeasedDate(), dao.getCeasedDate());
        assertEquals(dto.getNotifiedDate(), dao.getNotifiedDate());
    }

    private void assertTrustCorporatesAreEqual(TrustCorporateDto dto, TrustCorporateDao dao) {
        assertEquals(dto.getName(), dao.getName());
        assertEquals(dto.getDateBecameInterestedPerson(), dao.getDateBecameInterestedPerson());
        assertEquals(dto.getType().toLowerCase(), dao.getType().getValue().toLowerCase());
        assertEquals(dto.getIdentificationCountryRegistration(), dao.getIdentificationCountryRegistration());
        assertEquals(dto.getIdentificationLegalAuthority(), dao.getIdentificationLegalAuthority());
        assertEquals(dto.getIdentificationLegalForm(), dao.getIdentificationLegalForm());
        assertEquals(dto.getIdentificationPlaceRegistered(), dao.getIdentificationPlaceRegistered());
        assertEquals(dto.getIdentificationRegistrationNumber(), dao.getIdentificationRegistrationNumber());
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
