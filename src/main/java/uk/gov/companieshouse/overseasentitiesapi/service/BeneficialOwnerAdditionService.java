package uk.gov.companieshouse.overseasentitiesapi.service;

import org.springframework.stereotype.Service;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerGovernmentOrPublicAuthorityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.CompanyIdentification;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.PersonName;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions.Addition;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions.CorporateEntityBeneficialOwnerAddition;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions.IndividualBeneficialOwnerAddition;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions.LegalPersonBeneficialOwnerAddition;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static uk.gov.companieshouse.overseasentitiesapi.utils.NationalityOtherMapping.generateNationalityOtherField;
import static uk.gov.companieshouse.overseasentitiesapi.utils.TypeConverter.addressDtoToAddress;
import static uk.gov.companieshouse.overseasentitiesapi.utils.NatureOfControlTypeMapping.collectAllNatureOfControlsIntoSingleList;

@Service
public class BeneficialOwnerAdditionService {
    public List<Addition> beneficialOwnerAdditions(OverseasEntitySubmissionDto overseasEntitySubmissionDto) {
        List<Addition> additions = new ArrayList<>();

        addIndividualBeneficialOwnerAdditions(additions, overseasEntitySubmissionDto);
        addCorporateEntityBeneficialOwnerAdditions(additions, overseasEntitySubmissionDto);
        addLegalPersonBeneficialOwnerAdditions(additions, overseasEntitySubmissionDto);

        return additions;
    }

    private void addIndividualBeneficialOwnerAdditions(
            List<Addition> additions, OverseasEntitySubmissionDto overseasEntitySubmissionDto) {
        var beneficialOwnersIndividual = overseasEntitySubmissionDto.getBeneficialOwnersIndividual();
        if (beneficialOwnersIndividual != null) {
            beneficialOwnersIndividual.stream()
                    .filter(beneficialOwner -> (beneficialOwner.getChipsReference() == null))
                    .map(this::getIndividualBeneficialOwnerAddition)
                    .forEachOrdered(additions::add);
        }
    }

    private void addCorporateEntityBeneficialOwnerAdditions(
            List<Addition> additions, OverseasEntitySubmissionDto overseasEntitySubmissionDto) {
        var beneficialOwnersCorporateEntity = overseasEntitySubmissionDto.getBeneficialOwnersCorporate();
        if (beneficialOwnersCorporateEntity != null) {
            beneficialOwnersCorporateEntity.stream()
                    .filter(beneficialOwner -> (beneficialOwner.getChipsReference() == null))
                    .map(this::getCorporateEntityBeneficialOwnerAddition)
                    .forEachOrdered(additions::add);
        }
    }

    private void addLegalPersonBeneficialOwnerAdditions(
            List<Addition> additions, OverseasEntitySubmissionDto overseasEntitySubmissionDto) {
        var beneficialOwnersLegalPerson = overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority();
        if (beneficialOwnersLegalPerson != null) {
            beneficialOwnersLegalPerson.stream()
                    .filter(beneficialOwner -> (beneficialOwner.getChipsReference() == null))
                    .map(this::getLegalPersonBeneficialOwnerAddition)
                    .forEachOrdered(additions::add);
        }
    }

    private IndividualBeneficialOwnerAddition getIndividualBeneficialOwnerAddition(
            BeneficialOwnerIndividualDto bo) {
        var actionDate = bo.getStartDate();
        var ceasedDate = bo.getCeasedDate();
        var serviceAddress = bo.getServiceAddress();
        var residentialAddress = bo.getUsualResidentialAddress();
        var natureOfControls = collectAllNatureOfControlsIntoSingleList(
                bo.getBeneficialOwnerNatureOfControlTypes(),
                bo.getTrusteesNatureOfControlTypes(),
                bo.getNonLegalFirmMembersNatureOfControlTypes()
        );
        var isOnSanctionsList = bo.getOnSanctionsList();

        var individualBeneficialOwnerAddition =
                new IndividualBeneficialOwnerAddition(
                        actionDate,
                        ceasedDate,
                        addressDtoToAddress(residentialAddress),
                        addressDtoToAddress(serviceAddress),
                        natureOfControls,
                        isOnSanctionsList);

        individualBeneficialOwnerAddition.setPersonName(new PersonName(bo.getFirstName(), bo.getLastName()));
        individualBeneficialOwnerAddition.setBirthDate(bo.getDateOfBirth());

        individualBeneficialOwnerAddition.setNationalityOther(
                generateNationalityOtherField(bo.getNationality(), bo.getSecondNationality()));

        return individualBeneficialOwnerAddition;
    }

    private CorporateEntityBeneficialOwnerAddition getCorporateEntityBeneficialOwnerAddition(
            BeneficialOwnerCorporateDto bo) {
        var actionDate = bo.getStartDate();
        var ceasedDate = bo.getCeasedDate();
        var serviceAddress = bo.getServiceAddress();
        var residentialAddress = bo.getPrincipalAddress();
        var natureOfControls = collectAllNatureOfControlsIntoSingleList(
                bo.getBeneficialOwnerNatureOfControlTypes(),
                bo.getTrusteesNatureOfControlTypes(),
                bo.getNonLegalFirmMembersNatureOfControlTypes()
        );
        var isOnSanctionsList = bo.getOnSanctionsList();

        var corporateEntityBeneficialOwnerAddition =
                new CorporateEntityBeneficialOwnerAddition(
                        actionDate,
                        ceasedDate,
                        addressDtoToAddress(residentialAddress),
                        addressDtoToAddress(serviceAddress),
                        natureOfControls,
                        isOnSanctionsList);

        corporateEntityBeneficialOwnerAddition.setCorporateName(bo.getName());

        var legalForm = bo.getLegalForm();
        var governingLaw = bo.getLawGoverned();
        var placeRegistered = bo.getPublicRegisterName();
        var registrationNumber = bo.getRegistrationNumber();

        var identification = new CompanyIdentification();
        identification.setLegalForm(legalForm);
        identification.setGoverningLaw(governingLaw);
        identification.setPlaceRegistered(placeRegistered);
        identification.setRegistrationNumber(registrationNumber);
        corporateEntityBeneficialOwnerAddition.setCompanyIdentification(identification);

        return corporateEntityBeneficialOwnerAddition;
    }

    private LegalPersonBeneficialOwnerAddition getLegalPersonBeneficialOwnerAddition(
            BeneficialOwnerGovernmentOrPublicAuthorityDto bo) {
        var actionDate = bo.getStartDate();
        var ceasedDate = bo.getCeasedDate();
        var serviceAddress = bo.getServiceAddress();
        var residentialAddress = bo.getPrincipalAddress();
        var natureOfControls = collectAllNatureOfControlsIntoSingleList(
                bo.getBeneficialOwnerNatureOfControlTypes(),
                null,
                bo.getNonLegalFirmMembersNatureOfControlTypes()
        );
        var isOnSanctionsList = bo.getOnSanctionsList();

        var legalPersonBeneficialOwnerAddition =
                new LegalPersonBeneficialOwnerAddition(
                        actionDate,
                        ceasedDate,
                        addressDtoToAddress(residentialAddress),
                        addressDtoToAddress(serviceAddress),
                        natureOfControls,
                        isOnSanctionsList);

        legalPersonBeneficialOwnerAddition.setCorporateSoleName(bo.getName());

        var identification = new CompanyIdentification();
        identification.setLegalForm(bo.getLegalForm());
        identification.setGoverningLaw(bo.getLawGoverned());
        legalPersonBeneficialOwnerAddition.setCompanyIdentification(identification);

        return legalPersonBeneficialOwnerAddition;
    }
}
