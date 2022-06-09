package uk.gov.companieshouse.overseasentitiesapi.mocks;

import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.BeneficialOwnerType;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.TrustCorporateDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.HistoricalBeneficialOwnerDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.TrustIndividualDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.trust.TrustDataDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.HistoricalBeneficialOwnerDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustDataDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TrustMock {

    public static List<TrustDataDao> getTrustsDao() {
        List<TrustDataDao> trusts = new ArrayList<>();
        trusts.add(getTrustDataDao());
        return trusts;
    }

    public static TrustDataDao getTrustDataDao() {
        TrustDataDao trustData = new TrustDataDao();
        trustData.setTrustId("abc123");
        trustData.setTrustName("name");
        trustData.setCreationDate(LocalDate.of(2021,12,31));
        trustData.setUnableToObtainAllTrustInfo(false);
        trustData.setHistoricalBeneficialOwners(getHistoricalBeneficialOwnersDao());
        trustData.setIndividuals(getTrustIndividualsDao());
        trustData.setCorporates(getTrustCorporatesDao());
        return trustData;
    }

    public static List<HistoricalBeneficialOwnerDao> getHistoricalBeneficialOwnersDao() {
        HistoricalBeneficialOwnerDao historicalBo = new HistoricalBeneficialOwnerDao();
        historicalBo.setForename("test_forename");
        historicalBo.setOtherForenames("test_other_forenames");
        historicalBo.setSurname("test_surname");
        historicalBo.setCeasedDate(LocalDate.of(2021, 12, 31));
        historicalBo.setNotifiedDate(LocalDate.of(2021, 12, 31));
        List<HistoricalBeneficialOwnerDao> historicalBos = new ArrayList<>();
        historicalBos.add(historicalBo);
        return historicalBos;
    }

    public static List<TrustIndividualDao> getTrustIndividualsDao() {
        TrustIndividualDao individual = new TrustIndividualDao();
        individual.setType(BeneficialOwnerType.GRANTOR.getValue());
        individual.setForename("test_individual_forename");
        individual.setOtherForenames("test_individual_other_forenames");
        individual.setSurname("test_individual_surnames");
        individual.setDateOfBirth(LocalDate.of(2021, 12, 31));
        individual.setNationality("Welsh");
        individual.setServiceAddress(AddressMock.getAddressDao());
        individual.setUsualResidentialAddress(AddressMock.getAddressDao());
        individual.setDateBecameInterestedPerson(LocalDate.of(2021, 12, 31));
        List<TrustIndividualDao> individuals = new ArrayList<>();
        individuals.add(individual);
        return individuals;
    }

    public static List<TrustCorporateDao> getTrustCorporatesDao() {
        TrustCorporateDao corporate = new TrustCorporateDao();
        corporate.setType(BeneficialOwnerType.SETTLER.getValue());
        corporate.setName("test_name");
        corporate.setRegisteredOfficeAddress(AddressMock.getAddressDao());
        corporate.setServiceAddress(AddressMock.getAddressDao());
        corporate.setIdentificationCountryRegistration("registration");
        corporate.setIdentificationLegalAuthority("authority");
        corporate.setIdentificationLegalForm("form");
        corporate.setIdentificationPlaceRegistered("place");
        corporate.setIdentificationRegistrationNumber("number");
        corporate.setDateBecameInterestedPerson(LocalDate.of(2021, 12, 31));
        List<TrustCorporateDao> corporates = new ArrayList<>();
        corporates.add(corporate);
        return corporates;
    }

    public static List<TrustDataDto> getTrustsDto() {
        List<TrustDataDto> trusts = new ArrayList<>();
        trusts.add(getTrustDto());
        return trusts;
    }

    public static TrustDataDto getTrustDto() {
        TrustDataDto trust = new TrustDataDto();
        trust.setTrustId("abc1234");
        trust.setTrustName("name");
        trust.setCreationDate(LocalDate.of(2021, 12, 31));
        trust.setUnableToObtainAllTrustInfo(true);
        trust.setIndividuals(getTrustIndividualsDto());
        trust.setCorporates(getTrustCorporatesDto());
        trust.setHistoricalBeneficialOwners(getHistoricalBeneficialOwnersDto());
        return trust;
    }

    public static List<TrustIndividualDto> getTrustIndividualsDto() {
        TrustIndividualDto individual = new TrustIndividualDto();
        individual.setType(BeneficialOwnerType.BENEFICIARY.getValue());
        individual.setForename("forename");
        individual.setOtherForenames("other");
        individual.setSurname("surname");
        individual.setDateOfBirth(LocalDate.of(2021, 12, 31));
        individual.setNationality("British");

        individual.setSaAddressLine1("1");
        individual.setSaAddressLine2("2");
        individual.setSaAddressCareOf("co");
        individual.setSaAddressCountry("Wales");
        individual.setSaAddressLocality("Cardiff");
        individual.setSaAddressPoBox("123");
        individual.setSaAddressPostalCode("CF14 3UZ");
        individual.setSaAddressPremises("3");
        individual.setSaAddressRegion("Region");

        individual.setUraAddressLine1("1");
        individual.setUraAddressLine2("2");
        individual.setUraAddressCareOf("co");
        individual.setUraAddressCountry("Wales");
        individual.setUraAddressLocality("Cardiff");
        individual.setUraAddressPoBox("123");
        individual.setUraAddressPostalCode("CF14 3UZ");
        individual.setUraAddressPremises("3");
        individual.setUraAddressRegion("Region");

        individual.setDateBecameInterestedPerson(LocalDate.of(2021, 12, 31));

        List<TrustIndividualDto> individuals = new ArrayList<>();
        individuals.add(individual);
        return individuals;
    }

    public static List<TrustCorporateDto> getTrustCorporatesDto() {
        TrustCorporateDto corporate = new TrustCorporateDto();
        corporate.setType(BeneficialOwnerType.INTERESTED_PERSON.getValue());
        corporate.setName("corporate_name");
        corporate.setDateBecameInterestedPerson(LocalDate.of(2021, 12, 31));

        corporate.setRoAddressLine1("1");
        corporate.setRoAddressLine2("2");
        corporate.setRoAddressCareOf("co");
        corporate.setRoAddressCountry("Wales");
        corporate.setRoAddressLocality("Cardiff");
        corporate.setRoAddressPoBox("123");
        corporate.setRoAddressPostalCode("CF14 3UZ");
        corporate.setRoAddressPremises("3");
        corporate.setRoAddressRegion("Region");

        corporate.setSaAddressLine1("1");
        corporate.setSaAddressLine2("2");
        corporate.setSaAddressCareOf("co");
        corporate.setSaAddressCountry("Wales");
        corporate.setSaAddressLocality("Cardiff");
        corporate.setSaAddressPoBox("123");
        corporate.setSaAddressPostalCode("CF14 3UZ");
        corporate.setSaAddressPremises("3");
        corporate.setSaAddressRegion("Region");

        corporate.setIdentificationCountryRegistration("registration");
        corporate.setIdentificationLegalAuthority("authority");
        corporate.setIdentificationLegalForm("form");
        corporate.setIdentificationPlaceRegistered("place");
        corporate.setIdentificationRegistrationNumber("789");

        List<TrustCorporateDto> corporates = new ArrayList<>();
        corporates.add(corporate);
        return corporates;
    }

    public static List<HistoricalBeneficialOwnerDto> getHistoricalBeneficialOwnersDto() {
        HistoricalBeneficialOwnerDto historicalBo = new HistoricalBeneficialOwnerDto();
        historicalBo.setForename("historical_forename");
        historicalBo.setOtherForenames("historical_other_forenames");
        historicalBo.setSurname("historical_surname");
        historicalBo.setCeasedDate(LocalDate.of(2021, 12, 31));
        historicalBo.setNotifiedDate(LocalDate.of(2021, 12, 31));
        List<HistoricalBeneficialOwnerDto> historicalBos = new ArrayList<>();
        historicalBos.add(historicalBo);
        return historicalBos;
    }
}
