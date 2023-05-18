package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.changes.benificialowner;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.PersonName;

public class Psc {
  @JsonProperty("actionDate")
  private String actionDate;
  @JsonProperty("personName")
  private PersonName personName;
  @JsonProperty("secureIndicator")
  private String secureIndicator;
  @JsonProperty("superSecureIndicator")
  private String superSecureIndicator;
  @JsonProperty("serviceAddress")
  private ServiceAddress serviceAddress;
  @JsonProperty("residentialAddress")
  private ResidentialAddress residentialAddress;
  @JsonProperty("nationality")
  private String nationality;
  @JsonProperty("nationalityOther")
  private String nationalityOther;
  @JsonProperty("birthDate")
  private LocalDate birthDate;
  @JsonProperty("partialBirthDate")
  private String partialBirthDate;
  @JsonProperty("usualResidence")
  private String usualResidence;
  @JsonProperty("natureOfControls")
  private NatureOfControls natureOfControls;
  @JsonProperty("appointmentType")
  private String appointmentType;
  @JsonProperty("id")
  private String id;
  @JsonProperty("otherNames")
  private String otherNames;
  @JsonProperty("nationalityImageDesc")
  private String nationalityImageDesc;
  @JsonProperty("officerType")
  private String officerType;
  @JsonProperty("appointmentId")
  private String appointmentId;

  public String getActionDate() {
    return actionDate;
  }

  public void setActionDate(String actionDate) {
    this.actionDate = actionDate;
  }

  public PersonName getPersonName() {
    return personName;
  }

  public void setPersonName(
      PersonName personName) {
    this.personName = personName;
  }

  public String getSecureIndicator() {
    return secureIndicator;
  }

  public void setSecureIndicator(String secureIndicator) {
    this.secureIndicator = secureIndicator;
  }

  public String getSuperSecureIndicator() {
    return superSecureIndicator;
  }

  public void setSuperSecureIndicator(String superSecureIndicator) {
    this.superSecureIndicator = superSecureIndicator;
  }

  public ServiceAddress getServiceAddress() {
    return serviceAddress;
  }

  public void setServiceAddress(
      ServiceAddress serviceAddress) {
    this.serviceAddress = serviceAddress;
  }

  public ResidentialAddress getResidentialAddress() {
    return residentialAddress;
  }

  public void setResidentialAddress(
      ResidentialAddress residentialAddress) {
    this.residentialAddress = residentialAddress;
  }

  public String getNationality() {
    return nationality;
  }

  public void setNationality(String nationality) {
    this.nationality = nationality;
  }

  public String getNationalityOther() {
    return nationalityOther;
  }

  public void setNationalityOther(String nationalityOther) {
    this.nationalityOther = nationalityOther;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  public String getPartialBirthDate() {
    return partialBirthDate;
  }

  public void setPartialBirthDate(String partialBirthDate) {
    this.partialBirthDate = partialBirthDate;
  }

  public String getUsualResidence() {
    return usualResidence;
  }

  public void setUsualResidence(String usualResidence) {
    this.usualResidence = usualResidence;
  }

  public NatureOfControls getNatureOfControls() {
    return natureOfControls;
  }

  public void setNatureOfControls(
      NatureOfControls natureOfControls) {
    this.natureOfControls = natureOfControls;
  }

  public String getAppointmentType() {
    return appointmentType;
  }

  public void setAppointmentType(String appointmentType) {
    this.appointmentType = appointmentType;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getOtherNames() {
    return otherNames;
  }

  public void setOtherNames(String otherNames) {
    this.otherNames = otherNames;
  }

  public String getNationalityImageDesc() {
    return nationalityImageDesc;
  }

  public void setNationalityImageDesc(String nationalityImageDesc) {
    this.nationalityImageDesc = nationalityImageDesc;
  }

  public String getOfficerType() {
    return officerType;
  }

  public void setOfficerType(String officerType) {
    this.officerType = officerType;
  }

  public String getAppointmentId() {
    return appointmentId;
  }

  public void setAppointmentId(String appointmentId) {
    this.appointmentId = appointmentId;
  }
}
