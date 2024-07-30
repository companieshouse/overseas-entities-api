package uk.gov.companieshouse.overseasentitiesapi.mocks;

import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.HistoricalBeneficialOwnerDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.TrustCorporateDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.TrustDataDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.TrustDataToReviewDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.TrustIndividualDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.TrustReviewStatusDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.HistoricalBeneficialOwnerDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustDataDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustDataToReviewDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustReviewStatusDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TrustMock {
    private static void populateCommonTrustDataDao(TrustDataDao trustDataDao) {
        trustDataDao.setTrustName("Trust Name");
        trustDataDao.setChReference("_ecba-4TzUTXaln-g8daGtvS4a0");
        trustDataDao.setCreationDate(LocalDate.of(1990, 1, 1));
        trustDataDao.setTrustId("TrustID");
        trustDataDao.setUnableToObtainAllTrustInfo(false);

        List<HistoricalBeneficialOwnerDao> historicalBeneficialOwnerDaos = new ArrayList<>();
        HistoricalBeneficialOwnerDao historicalBeneficialOwnerDao = getHistoricalBeneficialOwnerDao();
        historicalBeneficialOwnerDaos.add(historicalBeneficialOwnerDao);
        historicalBeneficialOwnerDaos.add(getHistoricalBeneficialOwnerCorporateDao());
        trustDataDao.setHistoricalBeneficialOwners(historicalBeneficialOwnerDaos);

        List<TrustIndividualDao> trustIndividualDaos = new ArrayList<>();
        TrustIndividualDao trustIndividualDao = getIndividualDao();
        trustIndividualDaos.add(trustIndividualDao);
        trustDataDao.setIndividuals(trustIndividualDaos);

        List<TrustCorporateDao> trustCorporateDaos = new ArrayList<>();
        TrustCorporateDao trustCorporateDao = getTrustCorporateDao();
        trustCorporateDaos.add(trustCorporateDao);
        trustDataDao.setCorporates(trustCorporateDaos);
    }

    public static TrustDataDao getTrustDataDao() {
        TrustDataDao trustData = new TrustDataDao();
        populateCommonTrustDataDao(trustData);
        return trustData;
    }

    public static TrustDataToReviewDao getTrustDataToReviewDao() {
        TrustDataToReviewDao trustData = new TrustDataToReviewDao();
        populateCommonTrustDataDao(trustData);
        trustData.setReviewStatus(getTrustReviewStatusDao());
        return trustData;
    }

    public static HistoricalBeneficialOwnerDao getHistoricalBeneficialOwnerDao() {
        HistoricalBeneficialOwnerDao dao = new HistoricalBeneficialOwnerDao();
        dao.setForename("Test");
        dao.setOtherForenames("Other");
        dao.setSurname("Hbo");
        dao.setCeasedDate(LocalDate.of(1990, 1, 1));
        dao.setNotifiedDate(LocalDate.of(1990, 1, 1));
        dao.setCorporateIndicator(false);

        return dao;
    }

    public static HistoricalBeneficialOwnerDao getHistoricalBeneficialOwnerCorporateDao() {
        HistoricalBeneficialOwnerDao dao = new HistoricalBeneficialOwnerDao();
        dao.setCorporateName("Test Company");
        dao.setCorporateIndicator(true);
        dao.setCeasedDate(LocalDate.of(1990, 1, 1));
        dao.setNotifiedDate(LocalDate.of(1990, 1, 1));

        return dao;
    }

    public static TrustIndividualDao getIndividualDao() {
        TrustIndividualDao dao = new TrustIndividualDao();
        dao.setForename("Forename");
        dao.setOtherForenames("Middle Name");
        dao.setSurname("Surname");
        dao.setDateOfBirth(LocalDate.of(1960, 1, 1));
        dao.setNationality("Nationality");
        dao.setSecondNationality("My Dual Nationality");
        dao.setServiceAddress(AddressMock.getAddressDao());
        dao.setServiceAddressSameAsUsualResidentialAddress(false);
        dao.setUsualResidentialAddress(AddressMock.getAddressDao());
        dao.setDateBecameInterestedPerson(LocalDate.of(1990, 1, 1));
        dao.setType("Grantor");

        return dao;
    }

    public static TrustCorporateDao getTrustCorporateDao() {
        TrustCorporateDao dao = new TrustCorporateDao();
        dao.setName("Name");
        dao.setDateBecameInterestedPerson(LocalDate.of(1990, 1, 1));
        dao.setRegisteredOfficeAddress(AddressMock.getAddressDao());
        dao.setServiceAddress(AddressMock.getAddressDao());
        dao.setServiceAddressSameAsPrincipalAddress(false);
        dao.setIdentificationCountryRegistration("Identification Country");
        dao.setIdentificationLegalAuthority("Identification Legal Authority");
        dao.setIdentificationLegalForm("Identification Legal Form");
        dao.setIdentificationPlaceRegistered("Identification Place Registered");
        dao.setIdentificationRegistrationNumber("Registration Number");
        dao.setType("Beneficiary");
        dao.setOnRegisterInCountryFormedIn(true);

        return dao;
    }

    public static TrustReviewStatusDao getTrustReviewStatusDao() {
        TrustReviewStatusDao review = new TrustReviewStatusDao();
        review.setInReview(true);
        review.setReviewedTrustDetails(true);
        review.setReviewedFormerBOs(false);
        review.setReviewedIndividuals(true);
        review.setReviewedLegalEntities(false);
        return review;
    }

    private static void populateCommonTrustDataDto(TrustDataDto trustDataDto) {
        trustDataDto.setTrustName("Trust Name");
        trustDataDto.setChReference("_ecba-4TzUTXaln-g8daGtvS4a0");
        trustDataDto.setCreationDate(LocalDate.of(1990, 1, 1));
        trustDataDto.setTrustId("TrustID");
        trustDataDto.setTrustStillInvolvedInOverseasEntity(true);
        trustDataDto.setUnableToObtainAllTrustInfo(false);

        List<HistoricalBeneficialOwnerDto> historicalBeneficialOwnerDtos = new ArrayList<>();
        HistoricalBeneficialOwnerDto historicalBeneficialOwnerDto = getHistoricalBeneficialOwnerDto();
        historicalBeneficialOwnerDtos.add(historicalBeneficialOwnerDto);
        historicalBeneficialOwnerDtos.add(getHistoricalBeneficialOwnerCorporateDto());
        trustDataDto.setHistoricalBeneficialOwners(historicalBeneficialOwnerDtos);

        List<TrustIndividualDto> trustIndividualDtos = new ArrayList<>();
        TrustIndividualDto trustIndividualDto = getIndividualDto();
        trustIndividualDtos.add(trustIndividualDto);
        trustDataDto.setIndividuals(trustIndividualDtos);

        List<TrustCorporateDto> trustCorporateDtos = new ArrayList<>();
        TrustCorporateDto trustCorporateDto = getTrustCorporateDto();
        trustCorporateDtos.add(trustCorporateDto);
        trustDataDto.setCorporates(trustCorporateDtos);
    }

    public static TrustDataDto getTrustDataDto() {
        TrustDataDto trustData = new TrustDataDto();
        populateCommonTrustDataDto(trustData);
        return trustData;
    }

    public static TrustDataToReviewDto getTrustDataToReviewDto() {
        TrustDataToReviewDto trustData = new TrustDataToReviewDto();
        populateCommonTrustDataDto(trustData);
        trustData.setReviewStatus(getTrustReviewStatusDto());
        return trustData;
    }

    public static HistoricalBeneficialOwnerDto getHistoricalBeneficialOwnerDto() {
        HistoricalBeneficialOwnerDto Dto = new HistoricalBeneficialOwnerDto();
        Dto.setForename("Test");
        Dto.setOtherForenames("Other");
        Dto.setSurname("Hbo");
        Dto.setCeasedDate(LocalDate.of(1990, 1, 1));
        Dto.setNotifiedDate(LocalDate.of(1990, 1, 1));
        Dto.setCorporateIndicator(false);

        return Dto;
    }

    public static HistoricalBeneficialOwnerDto getHistoricalBeneficialOwnerCorporateDto() {
        HistoricalBeneficialOwnerDto Dto = new HistoricalBeneficialOwnerDto();
        Dto.setCorporateName("Test Company");
        Dto.setCeasedDate(LocalDate.of(1990, 1, 1));
        Dto.setNotifiedDate(LocalDate.of(1990, 1, 1));
        Dto.setCorporateIndicator(true);

        return Dto;
    }

    public static TrustIndividualDto getIndividualDto() {
        TrustIndividualDto Dto = new TrustIndividualDto();
        Dto.setForename("Forename");
        Dto.setOtherForenames("Middle Name");
        Dto.setSurname("Surname");
        Dto.setDateOfBirth(LocalDate.of(1960, 1, 1));
        Dto.setNationality("Nationality");
        Dto.setSecondNationality("My Dual Nationality");
        Dto.setServiceAddress(AddressMock.getAddressDto());
        Dto.setServiceAddressSameAsUsualResidentialAddress(false);
        Dto.setUsualResidentialAddress(AddressMock.getAddressDto());
        Dto.setDateBecameInterestedPerson(LocalDate.of(1990, 1, 1));
        Dto.setType("Grantor");

        return Dto;
    }

    public static TrustCorporateDto getTrustCorporateDto() {
        TrustCorporateDto Dto = new TrustCorporateDto();
        Dto.setName("Name");
        Dto.setDateBecameInterestedPerson(LocalDate.of(1990, 1, 1));
        Dto.setRegisteredOfficeAddress(AddressMock.getAddressDto());
        Dto.setServiceAddressSameAsPrincipalAddress(false);
        Dto.setServiceAddress(AddressMock.getAddressDto());
        Dto.setIdentificationCountryRegistration("Identification Country");
        Dto.setIdentificationLegalAuthority("Identification Legal Authority");
        Dto.setIdentificationLegalForm("Identification Legal Form");
        Dto.setIdentificationPlaceRegistered("Identification Place Registered");
        Dto.setIdentificationRegistrationNumber("Registration Number");
        Dto.setType("Beneficiary");
        Dto.setOnRegisterInCountryFormedIn(true);
        Dto.setCorporateStillInvolvedInTrust(Boolean.TRUE);

        return Dto;
    }

    public static TrustReviewStatusDto getTrustReviewStatusDto() {
        TrustReviewStatusDto review = new TrustReviewStatusDto();
        review.setInReview(true);
        review.setReviewedTrustDetails(true);
        review.setReviewedFormerBOs(false);
        review.setReviewedIndividuals(true);
        review.setReviewedLegalEntities(false);
        return review;
    }
}
