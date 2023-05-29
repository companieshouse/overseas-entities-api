package uk.gov.companieshouse.overseasentitiesapi.service;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import uk.gov.companieshouse.overseasentitiesapi.model.NatureOfControlType;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.*;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.PersonName;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions.*;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;

import java.util.*;
import java.util.stream.Collectors;

import static uk.gov.companieshouse.overseasentitiesapi.utils.NatureOfControlTypeMapping.*;

@Service
public class BeneficialOwnerAdditionService {
    public List<Addition> beneficialOwnerAdditions(OverseasEntitySubmissionDto overseasEntitySubmissionDto) {
        List<Addition> additions = new ArrayList<>();

        additions.addAll(getIndividualBeneficialOwnerAdditions(overseasEntitySubmissionDto)
                .stream()
                .flatMap(Optional::stream)
                .collect(Collectors.toList()));

        additions.addAll(getCorporateEntityBeneficialOwnerAdditions(overseasEntitySubmissionDto)
                .stream()
                .flatMap(Optional::stream)
                .collect(Collectors.toList()));

        additions.addAll(getLegalPersonBeneficialOwnerAdditions(overseasEntitySubmissionDto)
                .stream()
                .flatMap(Optional::stream)
                .collect(Collectors.toList()));

        return additions;
    }

    private List<Optional<IndividualBeneficialOwnerAddition>> getIndividualBeneficialOwnerAdditions(
            OverseasEntitySubmissionDto overseasEntitySubmissionDto) {
        var beneficialOwnersIndividual = overseasEntitySubmissionDto.getBeneficialOwnersIndividual();
        return beneficialOwnersIndividual.stream()
                .filter(beneficialOwner -> (beneficialOwner.getChipsReference() == null))
                .map(beneficialOwner -> getIndividualBeneficialOwnerAddition(beneficialOwner))
                .collect(Collectors.toList());
    }

    private List<Optional<CorporateEntityBeneficialOwnerAddition>> getCorporateEntityBeneficialOwnerAdditions(
            OverseasEntitySubmissionDto overseasEntitySubmissionDto) {
        var beneficialOwnersCorporateEntity = overseasEntitySubmissionDto.getBeneficialOwnersCorporate();
        return beneficialOwnersCorporateEntity.stream()
                .filter(beneficialOwner -> (beneficialOwner.getChipsReference() == null))
                .map(beneficialOwner -> getCorporateEntityBeneficialOwnerAddition(beneficialOwner))
                .collect(Collectors.toList());
    }

    private List<Optional<LegalPersonBeneficialOwnerAddition>> getLegalPersonBeneficialOwnerAdditions(
            OverseasEntitySubmissionDto overseasEntitySubmissionDto) {
        var beneficialOwnersLegalPerson = overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority();
        return beneficialOwnersLegalPerson.stream()
                .filter(beneficialOwner -> (beneficialOwner.getChipsReference() == null))
                .map(beneficialOwner -> getLegalPersonBeneficialOwnerAddition(beneficialOwner))
                .collect(Collectors.toList());
    }

    private Optional<IndividualBeneficialOwnerAddition> getIndividualBeneficialOwnerAddition(
            BeneficialOwnerIndividualDto bo) {
        var actionDate = bo.getStartDate();
        var ceasedDate = bo.getCeasedDate();
        var serviceAddress = bo.getServiceAddress();
        var residentialAddress = bo.getUsualResidentialAddress();
        List<String> natureOfControls = new ArrayList<>();
        mapNatureOfControlsForOverseasEntity(
                natureOfControls, bo.getBeneficialOwnerNatureOfControlTypes(), OVERSEAS_ENTITIES_PERSON_MAP);
        mapNatureOfControlsForOverseasEntity(
                natureOfControls, bo.getNonLegalFirmMembersNatureOfControlTypes(), OVERSEAS_ENTITIES_FIRM_MAP);
        mapNatureOfControlsForOverseasEntity(
                natureOfControls, bo.getTrusteesNatureOfControlTypes(), OVERSEAS_ENTITIES_TRUST_MAP);

        var individualBeneficialOwnerAddition =
                new IndividualBeneficialOwnerAddition(
                        actionDate,
                        ceasedDate,
                        convertAddressDtoToAddressModel(serviceAddress),
                        convertAddressDtoToAddressModel(residentialAddress),
                        natureOfControls);

        individualBeneficialOwnerAddition.setPersonName(new PersonName(bo.getFirstName(), bo.getLastName()));
        individualBeneficialOwnerAddition.setBirthDate(bo.getDateOfBirth());

        if (Objects.isNull(bo.getSecondNationality())) {
            individualBeneficialOwnerAddition.setNationalityOther(bo.getNationality());
        } else {
            individualBeneficialOwnerAddition.setNationalityOther(
                    String.format("%s, %s", bo.getNationality(), bo.getSecondNationality()));
        }

        return Optional.of(individualBeneficialOwnerAddition);
    }

    private Optional<CorporateEntityBeneficialOwnerAddition> getCorporateEntityBeneficialOwnerAddition(
            BeneficialOwnerCorporateDto bo) {
        var actionDate = bo.getStartDate();
        var ceasedDate = bo.getCeasedDate();
        var serviceAddress = bo.getServiceAddress();
        var residentialAddress = bo.getPrincipalAddress();
        List<String> natureOfControls = new ArrayList<>();
        mapNatureOfControlsForOverseasEntity(
                natureOfControls, bo.getBeneficialOwnerNatureOfControlTypes(), OVERSEAS_ENTITIES_PERSON_MAP);
        mapNatureOfControlsForOverseasEntity(
                natureOfControls, bo.getNonLegalFirmMembersNatureOfControlTypes(), OVERSEAS_ENTITIES_FIRM_MAP);
        mapNatureOfControlsForOverseasEntity(
                natureOfControls, bo.getTrusteesNatureOfControlTypes(), OVERSEAS_ENTITIES_TRUST_MAP);

        var corporateEntityBeneficialOwnerAddition =
                new CorporateEntityBeneficialOwnerAddition(
                        actionDate,
                        ceasedDate,
                        convertAddressDtoToAddressModel(serviceAddress),
                        convertAddressDtoToAddressModel(residentialAddress),
                        natureOfControls);

        corporateEntityBeneficialOwnerAddition.setCorporateName(bo.getName());
        corporateEntityBeneficialOwnerAddition.setLegalForm(bo.getLegalForm());
        corporateEntityBeneficialOwnerAddition.setGoverningLaw(bo.getLawGoverned());
        corporateEntityBeneficialOwnerAddition.setRegisterLocation(bo.getPublicRegisterName());
        corporateEntityBeneficialOwnerAddition.setRegistrationNumber(bo.getRegistrationNumber());

        return Optional.of(corporateEntityBeneficialOwnerAddition);
    }

    private Optional<LegalPersonBeneficialOwnerAddition> getLegalPersonBeneficialOwnerAddition(
            BeneficialOwnerGovernmentOrPublicAuthorityDto bo) {
        var actionDate = bo.getStartDate();
        var ceasedDate = bo.getCeasedDate();
        var serviceAddress = bo.getServiceAddress();
        var residentialAddress = bo.getPrincipalAddress();
        List<String> natureOfControls = new ArrayList<>();
        mapNatureOfControlsForOverseasEntity(
                natureOfControls, bo.getBeneficialOwnerNatureOfControlTypes(), OVERSEAS_ENTITIES_PERSON_MAP);
        mapNatureOfControlsForOverseasEntity(
                natureOfControls, bo.getNonLegalFirmMembersNatureOfControlTypes(), OVERSEAS_ENTITIES_FIRM_MAP);

        var legalPersonBeneficialOwnerAddition =
                new LegalPersonBeneficialOwnerAddition(
                        actionDate,
                        ceasedDate,
                        convertAddressDtoToAddressModel(serviceAddress),
                        convertAddressDtoToAddressModel(residentialAddress),
                        natureOfControls);

        legalPersonBeneficialOwnerAddition.setCorporateSoleName(bo.getName());
        legalPersonBeneficialOwnerAddition.setLegalForm(bo.getLegalForm());
        legalPersonBeneficialOwnerAddition.setGoverningLaw(bo.getLawGoverned());

        return Optional.of(legalPersonBeneficialOwnerAddition);
    }

    private static void mapNatureOfControlsForOverseasEntity(List<String> natureOfControls,
                                                             List<NatureOfControlType> natureOfControlTypes,
                                                             Map<NatureOfControlType, String> overseasEntitiesNocMap) {
        if (!CollectionUtils.isEmpty(natureOfControlTypes)) {
            natureOfControlTypes.forEach(nocType -> natureOfControls.add(overseasEntitiesNocMap.get(nocType)));
        }
    }

    private Address convertAddressDtoToAddressModel(AddressDto addressDto) {
        var address = new Address();

        address.setHouseNameNum(addressDto.getPropertyNameNumber());
        address.setStreet(addressDto.getLine1());
        address.setArea(addressDto.getLine2());
        address.setPostTown(addressDto.getLocality());
        address.setRegion(addressDto.getCounty());
        address.setCountry(addressDto.getCountry());
        address.setPostCode(addressDto.getPostcode());
        address.setPoBox(addressDto.getPoBox());
        address.setCareOf(addressDto.getCareOf());

        return address;
    }
}
