package uk.gov.companieshouse.overseasentitiesapi.mocks;

import uk.gov.companieshouse.overseasentitiesapi.model.BeneficialOwnersStatementType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerGovernmentOrPublicAuthorityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.PresenterDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.trust.TrustDataDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Mocks {

    public static OverseasEntitySubmissionDto buildSubmissionDto() {
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        EntityDto entity = buildEntityDto();
        overseasEntitySubmissionDto.setEntity(entity);
        PresenterDto presenter = PresenterMock.getPresenterDto();
        overseasEntitySubmissionDto.setPresenter(presenter);
        BeneficialOwnersStatementType beneficialOwnersStatement = BeneficialOwnersStatementType.ALL_IDENTIFIED_ALL_DETAILS;
        overseasEntitySubmissionDto.setBeneficialOwnersStatement(beneficialOwnersStatement);
        List<BeneficialOwnerIndividualDto> beneficialOwnersIndividualInFiling = buildBeneficialOwnersIndividualInFiling();
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(beneficialOwnersIndividualInFiling);
        List<BeneficialOwnerGovernmentOrPublicAuthorityDto> beneficialOwnerGovernmentOrPublicAuthorityInFiling = buildBeneficialOwnerGovernmentOrPublicAuthorityInFiling();
        overseasEntitySubmissionDto.setBeneficialOwnersGovernmentOrPublicAuthority(beneficialOwnerGovernmentOrPublicAuthorityInFiling);
        List<BeneficialOwnerCorporateDto> beneficialOwnersCorporateInFiling = buildBeneficialOwnersCorporateInFiling();
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnersCorporateInFiling);
        List<ManagingOfficerIndividualDto> managingOfficersIndividualInFiling = buildManagingOfficersIndividualInFiling();
        overseasEntitySubmissionDto.setManagingOfficersIndividual(managingOfficersIndividualInFiling);
        List<ManagingOfficerCorporateDto> managingOfficersCorporateInFiling = buildManagingOfficersCorporateInFiling();
        overseasEntitySubmissionDto.setManagingOfficersCorporate(managingOfficersCorporateInFiling);
        return overseasEntitySubmissionDto;
    }

    public static OverseasEntitySubmissionDto buildSubmissionDtoWithTrusts() {
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = buildSubmissionDto();
        List<TrustDataDto> trustDataInFiling = buildTrustDataInFiling();
        overseasEntitySubmissionDto.setTrusts(trustDataInFiling);
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
        entityDto.setOnRegisterInCountryFormedIn(true);
        entityDto.setPrincipalAddress(AddressMock.getAddressDto());
        entityDto.setServiceAddressSameAsPrincipalAddress(true);
        return entityDto;
    }

    private static List<BeneficialOwnerIndividualDto> buildBeneficialOwnersIndividualInFiling() {
        List<BeneficialOwnerIndividualDto> beneficialOwnersIndividualInFiling = new ArrayList<>();
        BeneficialOwnerIndividualDto beneficialOwnerIndividualDto = new BeneficialOwnerIndividualDto();
        beneficialOwnerIndividualDto.setFirstName("Jack");
        beneficialOwnerIndividualDto.setLastName("Jones");
        beneficialOwnerIndividualDto.setNationality("Welsh");
        beneficialOwnersIndividualInFiling.add(beneficialOwnerIndividualDto);
        return beneficialOwnersIndividualInFiling;
    }

    private static List<BeneficialOwnerGovernmentOrPublicAuthorityDto> buildBeneficialOwnerGovernmentOrPublicAuthorityInFiling() {
        List<BeneficialOwnerGovernmentOrPublicAuthorityDto> beneficialOwnersGovernmentOrPublicAuthorityInFiling = new ArrayList<>();
        BeneficialOwnerGovernmentOrPublicAuthorityDto beneficialOwnerGovernmentOrPublicAuthorityDto = new BeneficialOwnerGovernmentOrPublicAuthorityDto();
        beneficialOwnerGovernmentOrPublicAuthorityDto.setName("The Government");
        beneficialOwnerGovernmentOrPublicAuthorityDto.setLawGoverned("The Law");
        beneficialOwnerGovernmentOrPublicAuthorityDto.setLegalForm("Legal form");
        beneficialOwnersGovernmentOrPublicAuthorityInFiling.add(beneficialOwnerGovernmentOrPublicAuthorityDto);
        return beneficialOwnersGovernmentOrPublicAuthorityInFiling;
    }

    private static List<BeneficialOwnerCorporateDto> buildBeneficialOwnersCorporateInFiling() {
        List<BeneficialOwnerCorporateDto> beneficialOwnersCorporateInFiling = new ArrayList<>();
        BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = new BeneficialOwnerCorporateDto();
        beneficialOwnerCorporateDto.setLegalForm("Top Class");
        beneficialOwnerCorporateDto.setOnRegisterInCountryFormedIn(true);
        beneficialOwnerCorporateDto.setStartDate(LocalDate.of(2020, 4, 23));
        beneficialOwnersCorporateInFiling.add(beneficialOwnerCorporateDto);
        return beneficialOwnersCorporateInFiling;
    }

    private static List<ManagingOfficerIndividualDto> buildManagingOfficersIndividualInFiling() {
        List<ManagingOfficerIndividualDto> managingOfficersIndividualInFiling = new ArrayList<ManagingOfficerIndividualDto>();
        ManagingOfficerIndividualDto managingOfficerIndividualDto = new ManagingOfficerIndividualDto();
        managingOfficerIndividualDto.setFirstName("Walter");
        managingOfficerIndividualDto.setLastName("Blanc");
        managingOfficerIndividualDto.setNationality("French");
        managingOfficersIndividualInFiling.add(managingOfficerIndividualDto);
        return managingOfficersIndividualInFiling;
    }

    private static List<ManagingOfficerCorporateDto> buildManagingOfficersCorporateInFiling() {
        List<ManagingOfficerCorporateDto> managingOfficersCorporateInFiling = new ArrayList<>();
        ManagingOfficerCorporateDto managingOfficerCorporateDto = new ManagingOfficerCorporateDto();
        managingOfficerCorporateDto.setName("Corporate Man");
        managingOfficerCorporateDto.setLawGoverned("The Law");
        managingOfficerCorporateDto.setLegalForm("Legal FM");
        managingOfficersCorporateInFiling.add(managingOfficerCorporateDto);
        return managingOfficersCorporateInFiling;
    }

    private static List<TrustDataDto> buildTrustDataInFiling() {
        List<TrustDataDto> trustDataDtos = new ArrayList<>();
        TrustDataDto trustDataDto = new TrustDataDto();
        trustDataDto.setTrustName("Name");
        trustDataDto.setTrustId("ID");
        trustDataDto.setCreationDate(LocalDate.of(2020, 4, 23));
        trustDataDtos.add(trustDataDto);
        return trustDataDtos;
    }
}
