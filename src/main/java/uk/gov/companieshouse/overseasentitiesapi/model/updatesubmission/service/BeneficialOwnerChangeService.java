package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.BeneficialOwnerIndividualDto;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.OverseasEntitySubmissionDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.Change;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.benificialowner.BeneficialOwnerChange;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.benificialowner.Psc;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.benificialowner.ResidentialAddress;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.benificialowner.ServiceAddress;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.PersonName;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;

@Service
public class BeneficialOwnerChangeService {

  private final OverseasEntitySubmissionDto overseasEntitySubmissionDto;

  public BeneficialOwnerChangeService(OverseasEntitySubmissionDto overseasEntitySubmissionDto) {
    this.overseasEntitySubmissionDto = overseasEntitySubmissionDto;
  }

  public List<Change> benificialOwnerChanges() {
    List<Change> changes = new ArrayList<>();

    return changes;
  }

  private List<BeneficialOwnerChange> returnBenificialOwnerIndividualChanges() {
    var submissionBenificialOwners = overseasEntitySubmissionDto.getBeneficialOwnersIndividual();

  }

  private List<BeneficialOwnerChange> returnBenificialOwnerCorporateChanges() {
    var submissionBenificialOwners = overseasEntitySubmissionDto.getBeneficialOwnersCorporate()

  }

  private List<BeneficialOwnerChange> returnBenificialOwnerGovernmentOrPublicAuthorityChanges() {
    var submissionBenificialOwners = overseasEntitySubmissionDto.getBeneficialOwnersGovernmentOrPublicAuthority();

  }


  private BeneficialOwnerChange covertBenificialOwnerIndividualToChange(
      BeneficialOwnerIndividualDto beneficialOwnerIndividualDto) {

    var beneficialOwnerChange = new BeneficialOwnerChange();
    var psc = new Psc();

    beneficialOwnerChange.setPsc(psc);

    psc.setPersonName(
        new PersonName(
            beneficialOwnerIndividualDto.getFirstName(),
            beneficialOwnerIndividualDto.getLastName()
        )
    );
    psc.setNationality(beneficialOwnerIndividualDto.getNationality());
    psc.setBirthDate(beneficialOwnerIndividualDto.getDateOfBirth());
    psc.setNationalityOther(beneficialOwnerIndividualDto.getSecondNationality());
    psc.setUsualResidence(addressDtoToString(beneficialOwnerIndividualDto.getUsualResidentialAddress()));

    AddressDto serviceAddressSubmission = beneficialOwnerIndividualDto.getServiceAddress();
    ServiceAddress serviceAddress = new ServiceAddress();
    serviceAddress.setAddress(addressDtoToAddress(serviceAddressSubmission));

    psc.setServiceAddress(serviceAddress);

    AddressDto residentialAddressSubmission = beneficialOwnerIndividualDto.getUsualResidentialAddress();
    ResidentialAddress residentialAddress = new ResidentialAddress();
    residentialAddress.setAddress(addressDtoToAddress(residentialAddressSubmission));

    psc.setResidentialAddress(residentialAddress);

    return beneficialOwnerChange;
  }


  private String addressDtoToString(AddressDto address) {
    StringBuilder sb = new StringBuilder();

    if (address.getCareOf() != null) {
      sb.append("c/o ").append(address.getCareOf()).append(", ");
    }

    if (address.getPropertyNameNumber() != null) {
      sb.append(address.getPropertyNameNumber()).append(", ");
    }

    if (address.getLine1() != null) {
      sb.append(address.getLine1()).append(", ");
    }

    if (address.getLine2() != null) {
      sb.append(address.getLine2()).append(", ");
    }

    if (address.getTown() != null) {
      sb.append(address.getTown()).append(", ");
    }

    if (address.getCounty() != null) {
      sb.append(address.getCounty()).append(", ");
    }

    if (address.getLocality() != null) {
      sb.append(address.getLocality()).append(", ");
    }

    if (address.getPostcode() != null) {
      sb.append(address.getPostcode()).append(", ");
    }

    if (address.getCountry() != null) {
      sb.append(address.getCountry());
    }

    if (address.getPoBox() != null) {
      sb.append(", PO Box ").append(address.getPoBox());
    }

    // Remove the trailing comma, if there is one
    if (sb.length() > 0 && sb.charAt(sb.length() - 2) == ',') {
      sb.setLength(sb.length() - 2);
    }

    return sb.toString().trim();
  }

  private static Address addressDtoToAddress(AddressDto dto) {
    Address address = new Address();

    address.setCareOf(dto.getCareOf());
    address.setPoBox(dto.getPoBox());
    address.setHouseNameNum(dto.getPropertyNameNumber());
    address.setStreet(dto.getLine1());
    // Assuming "line2" maps to "area" in Address
    address.setArea(dto.getLine2());
    address.setPostTown(dto.getTown());
    // Assuming "county" maps to "region" in Address
    address.setRegion(dto.getCounty());
    address.setPostCode(dto.getPostcode());
    address.setCountry(dto.getCountry());

    // The following fields are not set as there is no direct equivalent in AddressDto:
    // id, careOfCompany, roAddressOverride, noAddressOverride

    return address;
  }

}
