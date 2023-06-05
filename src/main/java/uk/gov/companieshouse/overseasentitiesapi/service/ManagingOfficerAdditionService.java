package uk.gov.companieshouse.overseasentitiesapi.service;

import org.springframework.stereotype.Service;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.ManagingOfficerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions.Addition;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions.CorporateManagingOfficerAddition;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions.IndividualManagingOfficerAddition;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.CompanyIdentification;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.PersonName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static uk.gov.companieshouse.overseasentitiesapi.utils.NationalityOtherMapping.generateNationalityOtherField;
import static uk.gov.companieshouse.overseasentitiesapi.utils.TypeConverter.addressDtoToAddress;

@Service
public class ManagingOfficerAdditionService {
    public List<Addition> managingOfficerAdditions(OverseasEntitySubmissionDto overseasEntitySubmissionDto) {
        List<Addition> additions = new ArrayList<>();

        addIndividualManagingOfficerAdditions(additions, overseasEntitySubmissionDto);
        addCorporateManagingOfficerAdditions(additions, overseasEntitySubmissionDto);

        return additions;
    }

    private void addIndividualManagingOfficerAdditions(
            List<Addition> additions, OverseasEntitySubmissionDto overseasEntitySubmissionDto) {
        var managingOfficerIndividual = overseasEntitySubmissionDto.getManagingOfficersIndividual();
        if (managingOfficerIndividual != null) {
            managingOfficerIndividual.stream()
                    .filter(managingOfficer -> (managingOfficer.getChipsReference() == null))
                    .map(this::getIndividualManagingOfficerAddition)
                    .forEachOrdered(additions::add);
        }
    }

    private IndividualManagingOfficerAddition getIndividualManagingOfficerAddition(ManagingOfficerIndividualDto mo) {
        var actionDate = mo.getStartDate();
        var residentialAddress = mo.getUsualResidentialAddress();
        var serviceAddress = mo.getServiceAddress();
        var resignedOn = mo.getResignedOn();

        var individualManagingOfficerAddition =
                new IndividualManagingOfficerAddition(
                        actionDate,
                        addressDtoToAddress(residentialAddress),
                        addressDtoToAddress(serviceAddress),
                        resignedOn);

        individualManagingOfficerAddition.setPersonName(new PersonName(mo.getFirstName(), mo.getLastName()));
        individualManagingOfficerAddition.setFormerNames(mo.getFormerNames());
        individualManagingOfficerAddition.setBirthDate(mo.getDateOfBirth());
        individualManagingOfficerAddition.setRole(mo.getRoleAndResponsibilities());
        individualManagingOfficerAddition.setOccupation(mo.getOccupation());

        individualManagingOfficerAddition.setNationalityOther(
                generateNationalityOtherField(mo.getNationality(), mo.getSecondNationality()));

        return individualManagingOfficerAddition;
    }

    private void addCorporateManagingOfficerAdditions(
            List<Addition> additions, OverseasEntitySubmissionDto overseasEntitySubmissionDto) {
        var managingOfficerCorporate = overseasEntitySubmissionDto.getManagingOfficersCorporate();
        if (managingOfficerCorporate != null) {
            managingOfficerCorporate.stream()
                    .filter(managingOfficer -> (managingOfficer.getChipsReference() == null))
                    .map(this::getCorporateManagingOfficerAddition)
                    .forEachOrdered(additions::add);
        }
    }

    private CorporateManagingOfficerAddition getCorporateManagingOfficerAddition(ManagingOfficerCorporateDto mo) {
        var actionDate = mo.getStartDate();
        var residentialAddress = mo.getPrincipalAddress();
        var serviceAddress = mo.getServiceAddress();
        var resignedOn = mo.getResignedOn();

        var corporateManagingOfficerAddition =
                new CorporateManagingOfficerAddition(
                        actionDate,
                        addressDtoToAddress(residentialAddress),
                        addressDtoToAddress(serviceAddress),
                        resignedOn);

        corporateManagingOfficerAddition.setName(mo.getName());
        corporateManagingOfficerAddition.setContactName(mo.getContactFullName());
        corporateManagingOfficerAddition.setContactEmail(mo.getContactEmail());

        var legalForm = mo.getLegalForm();
        var governingLaw = mo.getLawGoverned();
        var registerLocation = mo.getPublicRegisterName();
        var registrationNumber = mo.getRegistrationNumber();

        var identification = new CompanyIdentification();
        identification.setLegalForm(legalForm);
        identification.setGoverningLaw(governingLaw);
        identification.setRegisterLocation(registerLocation);
        identification.setRegistrationNumber(registrationNumber);
        corporateManagingOfficerAddition.setIdentification(identification);

        return corporateManagingOfficerAddition;
    }
}
