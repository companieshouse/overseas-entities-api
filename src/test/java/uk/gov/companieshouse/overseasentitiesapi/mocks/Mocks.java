package uk.gov.companieshouse.overseasentitiesapi.mocks;

import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.PresenterDto;

import java.util.ArrayList;
import java.util.List;

public class Mocks {

    public static OverseasEntitySubmissionDto buildSubmissionDto() {
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        EntityDto entity = buildEntityDto();
        overseasEntitySubmissionDto.setEntity(entity);
        PresenterDto presenter = PresenterMock.getPresenterDto();
        overseasEntitySubmissionDto.setPresenter(presenter);
        List<BeneficialOwnerIndividualDto> beneficialOwnersIndividualInFiling = buildBeneficialOwnersIndividualInFiling();
        overseasEntitySubmissionDto.setBeneficialOwnersIndividual(beneficialOwnersIndividualInFiling);
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
}
