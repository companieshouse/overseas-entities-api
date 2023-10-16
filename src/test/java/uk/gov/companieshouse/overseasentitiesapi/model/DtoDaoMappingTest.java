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
import uk.gov.companieshouse.overseasentitiesapi.model.dao.UpdateDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.*;
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
import uk.gov.companieshouse.overseasentitiesapi.model.dto.UpdateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.companieshouse.overseasentitiesapi.mocks.Mocks.EMAIL_WITHOUT_LEADING_AND_TRAILING_SPACES;

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
        entity.setEmail(EMAIL_WITHOUT_LEADING_AND_TRAILING_SPACES);
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

        UpdateDao updateDao = new UpdateDao();
        updateDao.setDateOfCreation(LocalDate.of(2020,1,1));
        updateDao.setBoMoDataFetched(false);
        updateDao.setRegistrableBeneficialOwner(false);

        List<TrustDataToReviewDao> reviewTrusts = new ArrayList<>();
        TrustDataToReviewDao trustToReviewData = TrustMock.getTrustDataToReviewDao();
        reviewTrusts.add(trustToReviewData);

        updateDao.setTrustDataFetched(true);
        updateDao.setReviewTrusts(reviewTrusts);

        overseasEntitySubmission.setUpdate(updateDao);

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
        // Leading and trailing spaces are added but should be trimmed by the time that the object is mapped
        entity.setEmail(" " + EMAIL_WITHOUT_LEADING_AND_TRAILING_SPACES + " ");
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
        BeneficialOwnerCorporateDto corporateBo = BeneficialOwnerAllFieldsMock.getBeneficialOwnerCorporateDto();
        corporateBo.setPrincipalAddress(address);
        corporateBo.setServiceAddress(address);
        beneficialOwnersCorporate.add(corporateBo);
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

        UpdateDto updateDto = new UpdateDto();
        updateDto.setDateOfCreation(LocalDate.of(2020,1,1));
        updateDto.setFilingDate(LocalDate.of(2023,1,2));
        updateDto.setBoMoDataFetched(false);
        updateDto.setRegistrableBeneficialOwner(false);

        List<TrustDataToReviewDto> reviewTrusts = new ArrayList<>();
        TrustDataToReviewDto trustToReviewData = TrustMock.getTrustDataToReviewDto();
        reviewTrusts.add(trustToReviewData);

        updateDto.setTrustDataFetched(false);
        updateDto.setReviewTrusts(reviewTrusts);

        overseasEntitySubmission.setUpdate(updateDto);

        List<TrustDataDto> trustDataDto = new ArrayList<>();
        TrustDataDto trustData = TrustMock.getTrustDataDto();
        trustDataDto.add(trustData);
        overseasEntitySubmission.setTrusts(trustDataDto);

        return overseasEntitySubmission;
    }

    private void assertContentIsEqual(OverseasEntitySubmissionDto dto, OverseasEntitySubmissionDao dao) {

        EntityDao entityDao = dao.getEntity();
        EntityDto entityDto = dto.getEntity();

        assertEquals(EMAIL_WITHOUT_LEADING_AND_TRAILING_SPACES, entityDao.getEmail());
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
        assertEquals(EMAIL_WITHOUT_LEADING_AND_TRAILING_SPACES, presenterDao.getEmail());

        assertDueDiligenceIsEqual(dao.getDueDiligence(), dto.getDueDiligence());
        assertOverseasEntityDueDiligenceIsEqual(dao.getOverseasEntityDueDiligence(), dto.getOverseasEntityDueDiligence());

        assertEquals(dto.getBeneficialOwnersStatement(), dao.getBeneficialOwnersStatement());

        assertIndividualBeneficialOwnersAreEqual(dto, dao);
        assertCorporateBeneficialOwnersAreEqual(dto, dao);
        assertGovernmentBeneficialOwnersAreEqual(dto, dao);
        assertManagingOfficerIndividualAreEqual(dto, dao);
        assertManagingOfficerCorporateAreEqual(dto, dao);
        assertTrustsAreEqual(dto, dao);
        assertUpdateIsEqual(dto.getUpdate(), dao.getUpdate());
    }

    private void assertDueDiligenceIsEqual(DueDiligenceDao dueDiligenceDao, DueDiligenceDto dueDiligenceDto) {
        assertEquals(dueDiligenceDto.getIdentityDate(), dueDiligenceDao.getIdentityDate());
        assertEquals(dueDiligenceDto.getName(), dueDiligenceDao.getName());
        assertEquals(EMAIL_WITHOUT_LEADING_AND_TRAILING_SPACES, dueDiligenceDao.getEmail());
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
        assertEquals(EMAIL_WITHOUT_LEADING_AND_TRAILING_SPACES, overseasEntityDueDiligenceDao.getEmail());
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
        assertEquals(boDto.getHaveDayOfBirth(), boDao.getHaveDayOfBirth());
        assertEquals(boDto.getNationality(), boDao.getNationality());
        assertEquals(boDto.getServiceAddressSameAsUsualResidentialAddress(),
                boDao.getServiceAddressSameAsUsualResidentialAddress());
        assertEquals(boDto.getStartDate(), boDao.getStartDate());
        assertEquals(boDto.getCeasedDate(), boDao.getCeasedDate());
        assertEquals(boDto.getBeneficialOwnerNatureOfControlTypes(), boDao.getBeneficialOwnerNatureOfControlTypes());
        assertEquals(boDto.getTrusteesNatureOfControlTypes(), boDao.getTrusteesNatureOfControlTypes());
        assertEquals(boDto.getNonLegalFirmMembersNatureOfControlTypes(),
                boDao.getNonLegalFirmMembersNatureOfControlTypes());
        assertEquals(boDto.getOnSanctionsList(), boDao.getOnSanctionsList());
        assertEquals(boDto.getChipsReference(), boDao.getChipsReference());
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
        assertEquals(boDto.getCeasedDate(), boDao.getCeasedDate());
        assertEquals(boDto.getBeneficialOwnerNatureOfControlTypes(), boDao.getBeneficialOwnerNatureOfControlTypes());
        assertEquals(boDto.getTrusteesNatureOfControlTypes(), boDao.getTrusteesNatureOfControlTypes());
        assertEquals(boDto.getNonLegalFirmMembersNatureOfControlTypes(),
                boDao.getNonLegalFirmMembersNatureOfControlTypes());
        assertEquals(boDto.getOnSanctionsList(), boDao.getOnSanctionsList());
        assertEquals(boDto.getChipsReference(), boDao.getChipsReference());
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
        assertEquals(boDto.getCeasedDate(), boDao.getCeasedDate());
        assertEquals(boDto.getBeneficialOwnerNatureOfControlTypes(), boDao.getBeneficialOwnerNatureOfControlTypes());
        assertEquals(boDto.getNonLegalFirmMembersNatureOfControlTypes(),
                boDao.getNonLegalFirmMembersNatureOfControlTypes());
        assertEquals(boDto.getOnSanctionsList(), boDao.getOnSanctionsList());
        assertEquals(boDto.getChipsReference(), boDao.getChipsReference());
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
        assertEquals(moiDto.getStartDate(), moiDao.getStartDate());
        assertEquals(moiDto.getResignedOn(), moiDao.getResignedOn());
        assertEquals(moiDto.getRoleAndResponsibilities(), moiDao.getRoleAndResponsibilities());
        assertEquals(moiDto.getChipsReference(), moiDao.getChipsReference());

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
        assertEquals(mocDto.getStartDate(), mocDao.getStartDate());
        assertEquals(mocDto.getResignedOn(), mocDao.getResignedOn());
        assertEquals(mocDto.getChipsReference(), mocDao.getChipsReference());
        assertEquals(EMAIL_WITHOUT_LEADING_AND_TRAILING_SPACES, mocDao.getContactEmail());

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
        assertTrustHistoricalBeneficialOwnerAreEqual(trustDto.getHistoricalBeneficialOwners().get(1), trustDao.getHistoricalBeneficialOwners().get(1));
        assertTrustCorporatesAreEqual(trustDto.getCorporates().get(0), trustDao.getCorporates().get(0) );
    }

    private void assertUpdateIsEqual(UpdateDto updateDto, UpdateDao updateDao) {
        assertEquals(updateDto.getDateOfCreation(), updateDao.getDateOfCreation());
        assertEquals(updateDto.getFilingDate(), updateDao.getFilingDate());
        assertEquals(updateDto.isBoMoDataFetched(), updateDao.isBoMoDataFetched());
        assertEquals(updateDto.isRegistrableBeneficialOwner(), updateDao.isRegistrableBeneficialOwner());
        assertEquals(updateDto.isNoChange(), updateDao.isNoChange());
        assertEquals(updateDto.isTrustDataFetched(), updateDao.isTrustDataFetched());
        assertTrustsToReviewAreEqual(updateDto, updateDao);
    }

    private void assertTrustsToReviewAreEqual(UpdateDto updateDto, UpdateDao updateDao) {
        TrustDataToReviewDao trustDao = updateDao.getReviewTrusts().get(0);
        TrustDataToReviewDto trustDto = updateDto.getReviewTrusts().get(0);

        assertEquals(trustDto.getTrustName(), trustDao.getTrustName());
        assertEquals(trustDto.getCreationDate(), trustDao.getCreationDate());
        assertEquals(trustDto.getUnableToObtainAllTrustInfo(), trustDao.isUnableToObtainAllTrustInfo());

        assertTrustIndividualsAreEqual(trustDto.getIndividuals().get(0), trustDao.getIndividuals().get(0));
        assertTrustHistoricalBeneficialOwnerAreEqual(trustDto.getHistoricalBeneficialOwners().get(0), trustDao.getHistoricalBeneficialOwners().get(0));
        assertTrustHistoricalBeneficialOwnerAreEqual(trustDto.getHistoricalBeneficialOwners().get(1), trustDao.getHistoricalBeneficialOwners().get(1));
        assertTrustCorporatesAreEqual(trustDto.getCorporates().get(0), trustDao.getCorporates().get(0));

        assertTrustReviewAreEqual(trustDto.getReviewStatus(), trustDao.getReviewStatus());
    }

    private void assertTrustIndividualsAreEqual(TrustIndividualDto dto, TrustIndividualDao dao) {
        assertEquals(dto.getDateBecameInterestedPerson(), dao.getDateBecameInterestedPerson());
        assertEquals(dto.getDateOfBirth(), dao.getDateOfBirth());
        assertEquals(dto.getNationality(), dao.getNationality());
        assertEquals(dto.getSecondNationality(), dao.getSecondNationality());
        assertEquals(dto.getForename(), dao.getForename());
        assertEquals(dto.getOtherForenames(), dao.getOtherForenames());
        assertEquals(dto.getSurname(), dao.getSurname());
        assertEquals(dto.getType().toLowerCase(), dao.getType().getValue().toLowerCase());
    }

    private void assertTrustHistoricalBeneficialOwnerAreEqual(HistoricalBeneficialOwnerDto dto, HistoricalBeneficialOwnerDao dao) {
        assertEquals(dto.getForename(), dao.getForename());
        assertEquals(dto.getOtherForenames(), dao.getOtherForenames());
        assertEquals(dto.getSurname(), dao.getSurname());
        assertEquals(dto.getCorporateName(), dao.getCorporateName());
        assertEquals(dto.isCorporateIndicator(), dao.isCorporateIndicator());      
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
        assertEquals(dto.getOnRegisterInCountryFormedIn(), dao.getOnRegisterInCountryFormedIn());
    }

    private void assertTrustReviewAreEqual(TrustReviewStatusDto dto, TrustReviewStatusDao dao) {
        assertEquals(dto.getInReview(), dao.getInReview());
        assertEquals(dto.getReviewedTrustDetails(), dao.getReviewedTrustDetails());
        assertEquals(dto.getReviewedFormerBOs(), dao.getReviewedFormerBOs());
        assertEquals(dto.getReviewedIndividuals(), dao.getReviewedIndividuals());
        assertEquals(dto.getReviewedLegalEntities(), dao.getReviewedLegalEntities());
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
