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

    public static OverseasEntitySubmissionDto buildSubmissionDtoWithBoIndividualTrust() {
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = buildSubmissionDto();
        List<TrustDataDto> trustDataInFiling = buildTrustDataInFiling(1);
        overseasEntitySubmissionDto.setTrusts(trustDataInFiling);
        overseasEntitySubmissionDto.getBeneficialOwnersIndividual().get(0).setTrustIds(new ArrayList<>(List.of("1")));
        return overseasEntitySubmissionDto;
    }

    public static OverseasEntitySubmissionDto buildSubmissionDtoWithBoIndividualWithThreeTrusts() {
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = buildSubmissionDto();
        List<TrustDataDto> trustDataInFiling = buildTrustDataInFiling(3);
        overseasEntitySubmissionDto.setTrusts(trustDataInFiling);
        overseasEntitySubmissionDto.getBeneficialOwnersIndividual().get(0).setTrustIds(new ArrayList<>(List.of("1", "2", "3")));
        return overseasEntitySubmissionDto;
    }

    public static OverseasEntitySubmissionDto buildSubmissionDtoWithThreeBoIndividualTrusts() {
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = buildSubmissionDto();
        List<BeneficialOwnerIndividualDto> beneficialOwnersIndividualInFiling = buildThreeBeneficialOwnersIndividualInFiling();
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(beneficialOwnersIndividualInFiling);

        List<TrustDataDto> trustDataInFiling = buildTrustDataInFiling(3);
        overseasEntitySubmissionDto.setTrusts(trustDataInFiling);
        overseasEntitySubmissionDto.getBeneficialOwnersIndividual().get(0).setTrustIds(new ArrayList<>(List.of("1")));
        overseasEntitySubmissionDto.getBeneficialOwnersIndividual().get(1).setTrustIds(new ArrayList<>(List.of("2")));
        overseasEntitySubmissionDto.getBeneficialOwnersIndividual().get(2).setTrustIds(new ArrayList<>(List.of("3")));
        return overseasEntitySubmissionDto;
    }

    public static OverseasEntitySubmissionDto buildSubmissionDtoWithBoIndividualWithTwoTrustsSameID() {
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = buildSubmissionDto();
        List<TrustDataDto> trustDataInFiling = buildTrustDataInFiling(2);
        overseasEntitySubmissionDto.setTrusts(trustDataInFiling);
        overseasEntitySubmissionDto.getBeneficialOwnersIndividual().get(0).setTrustIds(new ArrayList<>(List.of("1")));

        // Set both trusts to have same ID
        overseasEntitySubmissionDto.getTrusts().get(0).setTrustId("1");
        overseasEntitySubmissionDto.getTrusts().get(1).setTrustId("1");

        return overseasEntitySubmissionDto;
    }

    public static OverseasEntitySubmissionDto buildSubmissionDtoWithBoIndividualNoTrustExists() {
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = buildSubmissionDto();
        List<TrustDataDto> trustDataInFiling = buildTrustDataInFiling(1);
        overseasEntitySubmissionDto.setTrusts(trustDataInFiling);
        overseasEntitySubmissionDto.getBeneficialOwnersIndividual().get(0).setTrustIds(new ArrayList<>(List.of("2")));

        return overseasEntitySubmissionDto;
    }

    public static OverseasEntitySubmissionDto buildSubmissionDtoWithBoIndividualTrustDataIsEmpty() {
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = buildSubmissionDto();
        overseasEntitySubmissionDto.getBeneficialOwnersIndividual().get(0).setTrustIds(new ArrayList<>(List.of("1")));
        overseasEntitySubmissionDto.getBeneficialOwnersIndividual().get(0).setTrustIds(new ArrayList<>(List.of("1")));

        return overseasEntitySubmissionDto;
    }

    public static OverseasEntitySubmissionDto buildSubmissionDtoWithBoCorporateTrust() {
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = buildSubmissionDto();
        List<TrustDataDto> trustDataInFiling = buildTrustDataInFiling(1);
        overseasEntitySubmissionDto.setTrusts(trustDataInFiling);
        overseasEntitySubmissionDto.getBeneficialOwnersCorporate().get(0).setTrustIds(new ArrayList<>(List.of("1")));
        return overseasEntitySubmissionDto;
    }

    public static OverseasEntitySubmissionDto buildSubmissionDtoWithBoCorporateWithThreeTrusts() {
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = buildSubmissionDto();
        List<TrustDataDto> trustDataInFiling =buildTrustDataInFiling(3);
        overseasEntitySubmissionDto.setTrusts(trustDataInFiling);
        overseasEntitySubmissionDto.getBeneficialOwnersCorporate().get(0).setTrustIds(new ArrayList<>(List.of("1", "2", "3")));
        return overseasEntitySubmissionDto;
    }

    public static OverseasEntitySubmissionDto buildSubmissionDtoWithThreeBoCorporateTrusts() {
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = buildSubmissionDto();
        List<BeneficialOwnerCorporateDto> beneficialOwnersCorporateInFiling = buildThreeBeneficialOwnersCorporateInFiling();
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnersCorporateInFiling);

        List<TrustDataDto> trustDataInFiling = buildTrustDataInFiling(3);
        overseasEntitySubmissionDto.setTrusts(trustDataInFiling);
        overseasEntitySubmissionDto.getBeneficialOwnersCorporate().get(0).setTrustIds(new ArrayList<>(List.of("1")));
        overseasEntitySubmissionDto.getBeneficialOwnersCorporate().get(1).setTrustIds(new ArrayList<>(List.of("2")));
        overseasEntitySubmissionDto.getBeneficialOwnersCorporate().get(2).setTrustIds(new ArrayList<>(List.of("3")));
        return overseasEntitySubmissionDto;
    }

    public static OverseasEntitySubmissionDto buildSubmissionDtoWithThreeBoCorporateTrustsAndThreeIndividualTrust() {
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = buildSubmissionDto();
        List<BeneficialOwnerIndividualDto> beneficialOwnersIndividualInFiling = buildThreeBeneficialOwnersIndividualInFiling();
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(beneficialOwnersIndividualInFiling);
        List<BeneficialOwnerCorporateDto> beneficialOwnersCorporateInFiling = buildThreeBeneficialOwnersCorporateInFiling();
        overseasEntitySubmissionDto.setBeneficialOwnersCorporate(beneficialOwnersCorporateInFiling);

        List<TrustDataDto> trustDataInFiling = buildTrustDataInFiling(3);
        overseasEntitySubmissionDto.setTrusts(trustDataInFiling);
        overseasEntitySubmissionDto.getBeneficialOwnersCorporate().get(0).setTrustIds(new ArrayList<>(List.of("1")));
        overseasEntitySubmissionDto.getBeneficialOwnersCorporate().get(1).setTrustIds(new ArrayList<>(List.of("2")));
        overseasEntitySubmissionDto.getBeneficialOwnersCorporate().get(2).setTrustIds(new ArrayList<>(List.of("3")));

        overseasEntitySubmissionDto.getBeneficialOwnersIndividual().get(0).setTrustIds(new ArrayList<>(List.of("1")));
        overseasEntitySubmissionDto.getBeneficialOwnersIndividual().get(1).setTrustIds(new ArrayList<>(List.of("2")));
        overseasEntitySubmissionDto.getBeneficialOwnersIndividual().get(2).setTrustIds(new ArrayList<>(List.of("3")));
        return overseasEntitySubmissionDto;
    }

    public static OverseasEntitySubmissionDto buildSubmissionDtoWithBoCorporateWithTwoTrustsSameID() {
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = buildSubmissionDto();
        List<TrustDataDto> trustDataInFiling = buildTrustDataInFiling(2);
        overseasEntitySubmissionDto.setTrusts(trustDataInFiling);
        overseasEntitySubmissionDto.getBeneficialOwnersCorporate().get(0).setTrustIds(new ArrayList<>(List.of("1")));

        // Set both trusts to have same ID
        overseasEntitySubmissionDto.getTrusts().get(0).setTrustId("1");
        overseasEntitySubmissionDto.getTrusts().get(1).setTrustId("1");

        return overseasEntitySubmissionDto;
    }

    public static OverseasEntitySubmissionDto buildSubmissionDtoWithBoCorporateNoTrustExists() {
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = buildSubmissionDto();
        List<TrustDataDto> trustDataInFiling = buildTrustDataInFiling(1);
        overseasEntitySubmissionDto.setTrusts(trustDataInFiling);
        overseasEntitySubmissionDto.getBeneficialOwnersCorporate().get(0).setTrustIds(new ArrayList<>(List.of("2")));

        return overseasEntitySubmissionDto;
    }

    public static OverseasEntitySubmissionDto buildSubmissionDtoWithBoCorporateTrustDataIsEmpty() {
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = buildSubmissionDto();
        overseasEntitySubmissionDto.getBeneficialOwnersIndividual().get(0).setTrustIds(new ArrayList<>(List.of("1")));
        overseasEntitySubmissionDto.getBeneficialOwnersCorporate().get(0).setTrustIds(new ArrayList<>(List.of("1")));

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

    private static List<BeneficialOwnerIndividualDto> buildThreeBeneficialOwnersIndividualInFiling() {
        List<BeneficialOwnerIndividualDto> beneficialOwnersIndividualInFiling = new ArrayList<>();
        BeneficialOwnerIndividualDto beneficialOwnerIndividualDto1 = new BeneficialOwnerIndividualDto();
        beneficialOwnerIndividualDto1.setFirstName("Jack");
        beneficialOwnerIndividualDto1.setLastName("Jones");
        beneficialOwnerIndividualDto1.setNationality("Welsh");
        beneficialOwnersIndividualInFiling.add(beneficialOwnerIndividualDto1);

        BeneficialOwnerIndividualDto beneficialOwnerIndividualDto2 = new BeneficialOwnerIndividualDto();
        beneficialOwnerIndividualDto2.setFirstName("John");
        beneficialOwnerIndividualDto2.setLastName("Smith");
        beneficialOwnerIndividualDto1.setNationality("English");
        beneficialOwnersIndividualInFiling.add(beneficialOwnerIndividualDto2);

        BeneficialOwnerIndividualDto beneficialOwnerIndividualDto3 = new BeneficialOwnerIndividualDto();
        beneficialOwnerIndividualDto3.setFirstName("Jane");
        beneficialOwnerIndividualDto3.setLastName("Doe");
        beneficialOwnerIndividualDto3.setNationality("Scottish");
        beneficialOwnersIndividualInFiling.add(beneficialOwnerIndividualDto3);
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

    private static List<BeneficialOwnerCorporateDto> buildThreeBeneficialOwnersCorporateInFiling() {
        List<BeneficialOwnerCorporateDto> beneficialOwnersCorporateInFiling = new ArrayList<>();
        BeneficialOwnerCorporateDto beneficialOwnerCorporateDto = new BeneficialOwnerCorporateDto();
        beneficialOwnerCorporateDto.setLegalForm("Top Class");
        beneficialOwnerCorporateDto.setOnRegisterInCountryFormedIn(true);
        beneficialOwnerCorporateDto.setStartDate(LocalDate.of(2020, 4, 23));
        beneficialOwnersCorporateInFiling.add(beneficialOwnerCorporateDto);

        BeneficialOwnerCorporateDto beneficialOwnerCorporateDto2 = new BeneficialOwnerCorporateDto();
        beneficialOwnerCorporateDto2.setLegalForm("Bottom Class");
        beneficialOwnerCorporateDto2.setOnRegisterInCountryFormedIn(false);
        beneficialOwnerCorporateDto2.setStartDate(LocalDate.of(2021, 4, 23));
        beneficialOwnersCorporateInFiling.add(beneficialOwnerCorporateDto2);

        BeneficialOwnerCorporateDto beneficialOwnerCorporateDto3 = new BeneficialOwnerCorporateDto();
        beneficialOwnerCorporateDto3.setLegalForm("Middle Class");
        beneficialOwnerCorporateDto3.setOnRegisterInCountryFormedIn(true);
        beneficialOwnerCorporateDto3.setStartDate(LocalDate.of(2022, 4, 23));
        beneficialOwnersCorporateInFiling.add(beneficialOwnerCorporateDto3);

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

    private static List<TrustDataDto> buildTrustDataInFiling(int numberOfTrusts) {
        List<TrustDataDto> trustDataDtos = new ArrayList<>();

        for (int i = 1; i <= numberOfTrusts; i++) {
            TrustDataDto trustDataDto = new TrustDataDto();
            trustDataDto.setTrustName("Trust Name " + i);
            trustDataDto.setTrustId(String.valueOf(i));
            trustDataDto.setCreationDate(LocalDate.of(2020, 4, i));
            trustDataDtos.add(trustDataDto);
        }

        return trustDataDtos;
    }
}
