package uk.gov.companieshouse.overseasentitiesapi.mocks;

import uk.gov.companieshouse.overseasentitiesapi.model.dao.ManagingOfficerCorporateDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.ManagingOfficerIndividualDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerIndividualDto;

import java.time.LocalDate;

public class ManagingOfficerMock {
    public static ManagingOfficerIndividualDto getManagingOfficerIndividualDto()  {
        ManagingOfficerIndividualDto dto = new ManagingOfficerIndividualDto();
        dto.setFirstName("Test");
        dto.setLastName("Bo");
        dto.setHasFormerNames(true);
        dto.setFormerNames("Someone");
        dto.setDateOfBirth(LocalDate.of(1990,1,1));
        dto.setNationality("Utopian");
        dto.setUsualResidentialAddress(AddressMock.getAddressDto());
        dto.setServiceAddressSameAsUsualResidentialAddress(true);
        dto.setOccupation("Some occupation");
        dto.setRoleAndResponsibilities("Some role and responsibility");
        return dto;
    }

    public static ManagingOfficerIndividualDao getManagingOfficerIndividualDao()  {
        ManagingOfficerIndividualDao dao = new ManagingOfficerIndividualDao();
        dao.setFirstName("Test");
        dao.setLastName("Bo");
        dao.setHasFormerNames(true);
        dao.setFormerNames("Someone");
        dao.setDateOfBirth(LocalDate.of(1990,1,1));
        dao.setNationality("Utopian");
        dao.setUsualResidentialAddress(AddressMock.getAddressDao());
        dao.setServiceAddressSameAsUsualResidentialAddress(true);
        dao.setOccupation("Some occupation");
        dao.setRoleAndResponsibilities("Some role and responsibility");
        return dao;
    }

    public static ManagingOfficerCorporateDto getManagingOfficerCorporateDto()  {
        ManagingOfficerCorporateDto dto = new ManagingOfficerCorporateDto();
        dto.setName("Test");
        dto.setPrincipalAddress(AddressMock.getAddressDto());
        dto.setServiceAddress(AddressMock.getAddressDto());
        dto.setServiceAddressSameAsPrincipalAddress(false);
        dto.setLegalForm("LegalForm");
        dto.setLawGoverned("Law Governed");
        dto.setOnRegisterInCountryFormedIn(true);
        dto.setPublicRegisterName("Public register");
        dto.setRegistrationNumber("11223344");
        dto.setStartDate(LocalDate.of(2015,1,1));
        return dto;
    }

    public static ManagingOfficerCorporateDao getManagingOfficerCorporateDao()  {
        ManagingOfficerCorporateDao dao = new ManagingOfficerCorporateDao();
        dao.setName("Test");
        dao.setPrincipalAddress(AddressMock.getAddressDao());
        dao.setServiceAddress(AddressMock.getAddressDao());
        dao.setServiceAddressSameAsPrincipalAddress(false);
        dao.setLegalForm("LegalForm");
        dao.setLawGoverned("Law Governed");
        dao.setOnRegisterInCountryFormedIn(true);
        dao.setPublicRegisterName("Public register");
        dao.setRegistrationNumber("11223344");
        dao.setStartDate(LocalDate.of(2015,1,1));
        return dao;
    }
}
