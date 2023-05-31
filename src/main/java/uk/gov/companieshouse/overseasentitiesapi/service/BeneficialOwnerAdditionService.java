package uk.gov.companieshouse.overseasentitiesapi.service;

import org.springframework.stereotype.Service;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerCorporateDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerGovernmentOrPublicAuthorityDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.PersonName;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions.Addition;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions.CorporateEntityBeneficialOwnerAddition;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions.IndividualBeneficialOwnerAddition;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions.LegalPersonBeneficialOwnerAddition;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static uk.gov.companieshouse.overseasentitiesapi.utils.TypeConverter.addressDtoToAddress;
import static uk.gov.companieshouse.overseasentitiesapi.utils.NatureOfControlTypeMapping.collectAllNatureOfControlsIntoSingleList;

@Service
public class BeneficialOwnerAdditionService {
    public List<Addition> beneficialOwnerAdditions(OverseasEntitySubmissionDto overseasEntitySubmissionDto) {
        List<Addition> additions = new ArrayList<>();

        getIndividualBeneficialOwnerAdditions(additions, overseasEntitySubmissionDto);
        getCorporateEntityBeneficialOwnerAdditions(additions, overseasEntitySubmissionDto);
        getLegalPersonBeneficialOwnerAdditions(additions, overseasEntitySubmissionDto);

        return additions;
    }

    private void getIndividualBeneficialOwnerAdditions(
            List<Addition> additions, OverseasEntitySubmissionDto overseasEntitySubmissionDto) {
        var beneficialOwnersIndividual = overseasEntitySubmissionDto.getBeneficialOwnersIndividual();
        if (beneficialOwnersIndividual != null) {
            beneficialOwnersIndividual.stream()
                    .filter(beneficialOwner -> (beneficialOwner.getChipsReference() == null))
                    .map(this::getIndividualBeneficialOwnerAddition)
                    .forEachOrdered(additions::add);
        }
    }

    private void getCorporateEntityBeneficialOwnerAdditions(
            List<Addition> additions, OverseasEntitySubmissionDto overseasEntitySubmissionDto) {
        var beneficialOwnersCorporateEntity = overseasEntitySubmissionDto.getBeneficialOwnersCorporate();
        if (beneficialOwnersCorporateEntity != null) {
            beneficialOwnersCorporateEntity.stream()
                    .filter(beneficialOwner -> (beneficialOwner.getChipsReference() == null))
                    .map(this::getCorporateEntityBeneficialOwnerAddition)
                    .forEachOrdered(additions::add);
        }
    }

    private void getLegalPersonBeneficialOwnerAdditions(
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

        var individualBeneficialOwnerAddition =
                new IndividualBeneficialOwnerAddition(
                        actionDate,
                        ceasedDate,
                        addressDtoToAddress(serviceAddress),
                        addressDtoToAddress(residentialAddress),
                        natureOfControls);

        individualBeneficialOwnerAddition.setPersonName(new PersonName(bo.getFirstName(), bo.getLastName()));
        individualBeneficialOwnerAddition.setBirthDate(bo.getDateOfBirth());

        if (Objects.isNull(bo.getSecondNationality())) {
            individualBeneficialOwnerAddition.setNationalityOther(bo.getNationality());
        } else {
            individualBeneficialOwnerAddition.setNationalityOther(
                    String.format("%s, %s", bo.getNationality(), bo.getSecondNationality()));
        }

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

        var corporateEntityBeneficialOwnerAddition =
                new CorporateEntityBeneficialOwnerAddition(
                        actionDate,
                        ceasedDate,
                        addressDtoToAddress(serviceAddress),
                        addressDtoToAddress(residentialAddress),
                        natureOfControls);

        corporateEntityBeneficialOwnerAddition.setCorporateName(bo.getName());
        corporateEntityBeneficialOwnerAddition.setLegalForm(bo.getLegalForm());
        corporateEntityBeneficialOwnerAddition.setGoverningLaw(bo.getLawGoverned());
        corporateEntityBeneficialOwnerAddition.setRegisterLocation(bo.getPublicRegisterName());
        corporateEntityBeneficialOwnerAddition.setRegistrationNumber(bo.getRegistrationNumber());

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

        var legalPersonBeneficialOwnerAddition =
                new LegalPersonBeneficialOwnerAddition(
                        actionDate,
                        ceasedDate,
                        addressDtoToAddress(serviceAddress),
                        addressDtoToAddress(residentialAddress),
                        natureOfControls);

        legalPersonBeneficialOwnerAddition.setCorporateSoleName(bo.getName());
        legalPersonBeneficialOwnerAddition.setLegalForm(bo.getLegalForm());
        legalPersonBeneficialOwnerAddition.setGoverningLaw(bo.getLawGoverned());

        return legalPersonBeneficialOwnerAddition;
    }
}
