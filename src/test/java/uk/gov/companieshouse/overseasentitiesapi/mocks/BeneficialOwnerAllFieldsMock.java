package uk.gov.companieshouse.overseasentitiesapi.mocks;

import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.BeneficialOwnerCorporateDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.BeneficialOwnerGovernmentOrPublicAuthorityDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dao.BeneficialOwnerIndividualDao;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerGovernmentOrPublicAuthorityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BeneficialOwnerAllFieldsMock {

    public static BeneficialOwnerIndividualDao getBeneficialOwnerIndividualDao()  {
        BeneficialOwnerIndividualDao dao = new BeneficialOwnerIndividualDao();
        dao.setFirstName("Test");
        dao.setLastName("Bo");
        dao.setDateOfBirth(LocalDate.of(1990,1,1));
        dao.setNationality("Utopian");
        dao.setServiceAddressSameAsUsualResidentialAddress(true);
        dao.setStartDate(LocalDate.of(2020,1,1));
        List<NatureOfControlType> naturesOfControl = new ArrayList<>();
        naturesOfControl.add(NatureOfControlType.APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS);
        naturesOfControl.add(NatureOfControlType.SIGNIFICANT_INFLUENCE_OR_CONTROL);
        naturesOfControl.add(NatureOfControlType.OVER_25_PERCENT_OF_SHARES);
        dao.setBeneficialOwnerNatureOfControlTypes(naturesOfControl);
        dao.setTrusteesNatureOfControlTypes(naturesOfControl);
        dao.setNonLegalFirmMembersNatureOfControlTypes(naturesOfControl);
        dao.setOnSanctionsList(false);
        return dao;
    }

    public static BeneficialOwnerCorporateDao getBeneficialOwnerCorporateDao()  {
        BeneficialOwnerCorporateDao dao = new BeneficialOwnerCorporateDao();
        dao.setName("TestBo");
        dao.setServiceAddressSameAsPrincipalAddress(true);
        dao.setLegalForm("Test Legal");
        dao.setLawGoverned("Test Laws");
        dao.setOnRegisterInCountryFormedIn(true);
        dao.setPublicRegisterName("Test Register");
        dao.setRegistrationNumber("abc123");
        dao.setStartDate(LocalDate.of(2020,1,1));
        List<NatureOfControlType> naturesOfControl = new ArrayList<>();
        naturesOfControl.add(NatureOfControlType.APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS);
        naturesOfControl.add(NatureOfControlType.SIGNIFICANT_INFLUENCE_OR_CONTROL);
        naturesOfControl.add(NatureOfControlType.OVER_25_PERCENT_OF_SHARES);
        dao.setBeneficialOwnerNatureOfControlTypes(naturesOfControl);
        dao.setTrusteesNatureOfControlTypes(naturesOfControl);
        dao.setNonLegalFirmMembersNatureOfControlTypes(naturesOfControl);
        dao.setOnSanctionsList(false);
        return dao;
    }

    public static BeneficialOwnerGovernmentOrPublicAuthorityDao getBeneficialOwnerGovernmentOrPublicAuthorityDao()  {
        BeneficialOwnerGovernmentOrPublicAuthorityDao dao = new BeneficialOwnerGovernmentOrPublicAuthorityDao();
        dao.setName("TestBo");
        dao.setServiceAddressSameAsPrincipalAddress(true);
        dao.setLegalForm("Test Legal");
        dao.setLawGoverned("Test Laws");
        dao.setStartDate(LocalDate.of(2020,1,1));
        List<NatureOfControlType> naturesOfControl = new ArrayList<>();
        naturesOfControl.add(NatureOfControlType.APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS);
        naturesOfControl.add(NatureOfControlType.SIGNIFICANT_INFLUENCE_OR_CONTROL);
        naturesOfControl.add(NatureOfControlType.OVER_25_PERCENT_OF_SHARES);
        dao.setBeneficialOwnerNatureOfControlTypes(naturesOfControl);
        dao.setNonLegalFirmMembersNatureOfControlTypes(naturesOfControl);
        return dao;
    }

    public static BeneficialOwnerIndividualDto getBeneficialOwnerIndividualDto()  {
        BeneficialOwnerIndividualDto dto = new BeneficialOwnerIndividualDto();
        dto.setFirstName("Test");
        dto.setLastName("Bo");
        dto.setDateOfBirth(LocalDate.of(1990,1,1));
        dto.setNationality("Utopian");
        dto.setServiceAddressSameAsUsualResidentialAddress(true);
        dto.setStartDate(LocalDate.of(2020,1,1));
        List<NatureOfControlType> naturesOfControl = new ArrayList<>();
        naturesOfControl.add(NatureOfControlType.APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS);
        naturesOfControl.add(NatureOfControlType.SIGNIFICANT_INFLUENCE_OR_CONTROL);
        naturesOfControl.add(NatureOfControlType.OVER_25_PERCENT_OF_SHARES);
        dto.setBeneficialOwnerNatureOfControlTypes(naturesOfControl);
        dto.setTrusteesNatureOfControlTypes(naturesOfControl);
        dto.setNonLegalFirmMembersNatureOfControlTypes(naturesOfControl);
        dto.setOnSanctionsList(false);
        return dto;
    }

    public static BeneficialOwnerCorporateDto getBeneficialOwnerCorporateDto()  {
        BeneficialOwnerCorporateDto dto = new BeneficialOwnerCorporateDto();
        dto.setName("TestBo");
        dto.setServiceAddressSameAsPrincipalAddress(true);
        dto.setLegalForm("Test Legal");
        dto.setLawGoverned("Test Laws");
        dto.setOnRegisterInCountryFormedIn(true);
        dto.setPublicRegisterName("Test Register");
        dto.setRegistrationNumber("abc123");
        dto.setStartDate(LocalDate.of(2020,1,1));
        List<NatureOfControlType> naturesOfControl = new ArrayList<>();
        naturesOfControl.add(NatureOfControlType.APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS);
        naturesOfControl.add(NatureOfControlType.SIGNIFICANT_INFLUENCE_OR_CONTROL);
        naturesOfControl.add(NatureOfControlType.OVER_25_PERCENT_OF_SHARES);
        dto.setBeneficialOwnerNatureOfControlTypes(naturesOfControl);
        dto.setTrusteesNatureOfControlTypes(naturesOfControl);
        dto.setNonLegalFirmMembersNatureOfControlTypes(naturesOfControl);
        dto.setOnSanctionsList(false);
        return dto;
    }

    public static BeneficialOwnerGovernmentOrPublicAuthorityDto getBeneficialOwnerGovernmentOrPublicAuthorityDto()  {
        BeneficialOwnerGovernmentOrPublicAuthorityDto dto = new BeneficialOwnerGovernmentOrPublicAuthorityDto();
        dto.setName("TestBo");
        dto.setServiceAddressSameAsPrincipalAddress(true);
        dto.setLegalForm("Test Legal");
        dto.setLawGoverned("Test Laws");
        dto.setStartDate(LocalDate.of(2020,1,1));
        List<NatureOfControlType> naturesOfControl = new ArrayList<>();
        naturesOfControl.add(NatureOfControlType.APPOINT_OR_REMOVE_MAJORITY_BOARD_DIRECTORS);
        naturesOfControl.add(NatureOfControlType.SIGNIFICANT_INFLUENCE_OR_CONTROL);
        naturesOfControl.add(NatureOfControlType.OVER_25_PERCENT_OF_SHARES);
        dto.setBeneficialOwnerNatureOfControlTypes(naturesOfControl);
        dto.setNonLegalFirmMembersNatureOfControlTypes(naturesOfControl);
        return dto;
    }
}
