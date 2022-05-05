package uk.gov.companieshouse.overseasentitiesapi.mocks;

import uk.gov.companieshouse.overseasentitiesapi.model.dto.EntityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.PresenterDto;

public class Mocks {

    public static OverseasEntitySubmissionDto buildSubmissionDto() {
        OverseasEntitySubmissionDto overseasEntitySubmissionDto = new OverseasEntitySubmissionDto();
        EntityDto entity = buildEntityDto();
        overseasEntitySubmissionDto.setEntity(entity);
        PresenterDto presenter = buildPresenterDto();
        overseasEntitySubmissionDto.setPresenter(presenter);
        return overseasEntitySubmissionDto;
    }

    private static EntityDto buildEntityDto() {
        EntityDto entityDto = new EntityDto();
        entityDto.setName("Joe Bloggs Ltd");
        entityDto.setEmail("example@test123.co.uk");
        entityDto.setIncorporationCountry("Eutopia");
        entityDto.setLawGoverned("The law");
        entityDto.setLegalForm("Legal form");
        entityDto.setRegisterName("JB");
        entityDto.setOnRegisterInCountryFormedIn(true);
        entityDto.setPrincipalAddress(AddressMock.getAddressDto());
        entityDto.setServiceAddressSameAsPrincipalAddress(true);
        return entityDto;
    }

    private static PresenterDto buildPresenterDto() {
        PresenterDto presenterDto = new PresenterDto();
        presenterDto.setFullName("Joe Bloggs");
        presenterDto.setPhoneNumber("01234 567890");
        presenterDto.setAntiMoneyLaunderingRegistrationNumber("999");
        presenterDto.setRole("Coder");
        presenterDto.setRoleTitle("Digital developer");
        return presenterDto;
    }
}
