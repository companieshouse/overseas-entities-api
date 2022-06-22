package uk.gov.companieshouse.overseasentitiesapi.model.dto.trust;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;

import java.time.LocalDate;

public class TrustCorporateDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("type")
    private String type;

    @JsonProperty("name")
    private String name;

    @JsonProperty("date_became_interested_person")
    private LocalDate dateBecameInterestedPerson;

    @JsonProperty("registered_office_address")
    private AddressDto registeredOfficeAddress;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("ro_address_line_1")
    private String roAddressLine1;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("ro_address_line_2")
    private String roAddressLine2;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("ro_address_care_of")
    private String roAddressCareOf;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("ro_address_country")
    private String roAddressCountry;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("ro_address_locality")
    private String roAddressLocality;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("ro_address_po_box")
    private String roAddressPoBox;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("ro_address_postal_code")
    private String roAddressPostalCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("ro_address_premises")
    private String roAddressPremises;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("ro_address_region")
    private String roAddressRegion;

    @JsonProperty("service_address")
    private AddressDto serviceAddress;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("sa_address_line_1")
    private String saAddressLine1;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("sa_address_line_2")
    private String saAddressLine2;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("sa_address_care_of")
    private String saAddressCareOf;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("sa_address_country")
    private String saAddressCountry;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("sa_address_locality")
    private String saAddressLocality;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("sa_address_po_box")
    private String saAddressPoBox;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("sa_address_postal_code")
    private String saAddressPostalCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("sa_address_premises")
    private String saAddressPremises;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("sa_address_region")
    private String saAddressRegion;

    @JsonProperty("identification_country_registration")
    private String identificationCountryRegistration;

    @JsonProperty("identification_legal_authority")
    private String identificationLegalAuthority;

    @JsonProperty("identification_legal_form")
    private String identificationLegalForm;

    @JsonProperty("identification_place_registered")
    private String identificationPlaceRegistered;

    @JsonProperty("identification_registration_number")
    private String identificationRegistrationNumber;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateBecameInterestedPerson() {
        return dateBecameInterestedPerson;
    }

    public void setDateBecameInterestedPerson(LocalDate dateBecameInterestedPerson) {
        this.dateBecameInterestedPerson = dateBecameInterestedPerson;
    }

    public String getRoAddressLine1() {
        return roAddressLine1;
    }

    public void setRoAddressLine1(String roAddressLine1) {
        this.roAddressLine1 = roAddressLine1;
    }

    public String getRoAddressLine2() {
        return roAddressLine2;
    }

    public void setRoAddressLine2(String roAddressLine2) {
        this.roAddressLine2 = roAddressLine2;
    }

    public String getRoAddressCareOf() {
        return roAddressCareOf;
    }

    public void setRoAddressCareOf(String roAddressCareOf) {
        this.roAddressCareOf = roAddressCareOf;
    }

    public String getRoAddressCountry() {
        return roAddressCountry;
    }

    public void setRoAddressCountry(String roAddressCountry) {
        this.roAddressCountry = roAddressCountry;
    }

    public String getRoAddressLocality() {
        return roAddressLocality;
    }

    public void setRoAddressLocality(String roAddressLocality) {
        this.roAddressLocality = roAddressLocality;
    }

    public String getRoAddressPoBox() {
        return roAddressPoBox;
    }

    public void setRoAddressPoBox(String roAddressPoBox) {
        this.roAddressPoBox = roAddressPoBox;
    }

    public String getRoAddressPostalCode() {
        return roAddressPostalCode;
    }

    public void setRoAddressPostalCode(String roAddressPostalCode) {
        this.roAddressPostalCode = roAddressPostalCode;
    }

    public String getRoAddressPremises() {
        return roAddressPremises;
    }

    public void setRoAddressPremises(String roAddressPremises) {
        this.roAddressPremises = roAddressPremises;
    }

    public String getRoAddressRegion() {
        return roAddressRegion;
    }

    public void setRoAddressRegion(String roAddressRegion) {
        this.roAddressRegion = roAddressRegion;
    }

    public AddressDto getRegisteredOfficeAddress() {
        // If the address is being set from the ro fields generate the address
        if (roAddressPostalCode != null) {
            registeredOfficeAddress = new AddressDto();
            registeredOfficeAddress.setPropertyNameNumber(saAddressPremises);
            registeredOfficeAddress.setLine1(saAddressLine1);
            registeredOfficeAddress.setLine2(saAddressLine2);
            registeredOfficeAddress.setCounty(saAddressRegion);
            registeredOfficeAddress.setLocality(saAddressLocality);
            registeredOfficeAddress.setCountry(saAddressCountry);
            registeredOfficeAddress.setCareOf(saAddressCareOf);
            registeredOfficeAddress.setPoBox(saAddressPoBox);
            registeredOfficeAddress.setPostcode(saAddressPostalCode);
        }
        return registeredOfficeAddress;
    }

    public void setRegisteredOfficeAddress(AddressDto registeredOfficeAddress) {
        this.registeredOfficeAddress = registeredOfficeAddress;
    }

    public String getSaAddressLine1() {
        return saAddressLine1;
    }

    public void setSaAddressLine1(String saAddressLine1) {
        this.saAddressLine1 = saAddressLine1;
    }

    public String getSaAddressLine2() {
        return saAddressLine2;
    }

    public void setSaAddressLine2(String saAddressLine2) {
        this.saAddressLine2 = saAddressLine2;
    }

    public String getSaAddressCareOf() {
        return saAddressCareOf;
    }

    public void setSaAddressCareOf(String saAddressCareOf) {
        this.saAddressCareOf = saAddressCareOf;
    }

    public String getSaAddressCountry() {
        return saAddressCountry;
    }

    public void setSaAddressCountry(String saAddressCountry) {
        this.saAddressCountry = saAddressCountry;
    }

    public String getSaAddressLocality() {
        return saAddressLocality;
    }

    public void setSaAddressLocality(String saAddressLocality) {
        this.saAddressLocality = saAddressLocality;
    }

    public String getSaAddressPoBox() {
        return saAddressPoBox;
    }

    public void setSaAddressPoBox(String saAddressPoBox) {
        this.saAddressPoBox = saAddressPoBox;
    }

    public String getSaAddressPostalCode() {
        return saAddressPostalCode;
    }

    public void setSaAddressPostalCode(String saAddressPostalCode) {
        this.saAddressPostalCode = saAddressPostalCode;
    }

    public String getSaAddressPremises() {
        return saAddressPremises;
    }

    public void setSaAddressPremises(String saAddressPremises) {
        this.saAddressPremises = saAddressPremises;
    }

    public String getSaAddressRegion() {
        return saAddressRegion;
    }

    public void setSaAddressRegion(String saAddressRegion) {
        this.saAddressRegion = saAddressRegion;
    }

    public AddressDto getServiceAddress() {
        // If the address is being set from the sa fields generate the address
        if (saAddressPostalCode != null) {
            serviceAddress = new AddressDto();
            serviceAddress.setPropertyNameNumber(saAddressPremises);
            serviceAddress.setLine1(saAddressLine1);
            serviceAddress.setLine2(saAddressLine2);
            serviceAddress.setCounty(saAddressRegion);
            serviceAddress.setLocality(saAddressLocality);
            serviceAddress.setCountry(saAddressCountry);
            serviceAddress.setCareOf(saAddressCareOf);
            serviceAddress.setPoBox(saAddressPoBox);
            serviceAddress.setPostcode(saAddressPostalCode);
        }
        return serviceAddress;
    }

    public void setServiceAddress(AddressDto serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public String getIdentificationCountryRegistration() {
        return identificationCountryRegistration;
    }

    public void setIdentificationCountryRegistration(String identificationCountryRegistration) {
        this.identificationCountryRegistration = identificationCountryRegistration;
    }

    public String getIdentificationLegalAuthority() {
        return identificationLegalAuthority;
    }

    public void setIdentificationLegalAuthority(String identificationLegalAuthority) {
        this.identificationLegalAuthority = identificationLegalAuthority;
    }

    public String getIdentificationLegalForm() {
        return identificationLegalForm;
    }

    public void setIdentificationLegalForm(String identificationLegalForm) {
        this.identificationLegalForm = identificationLegalForm;
    }

    public String getIdentificationPlaceRegistered() {
        return identificationPlaceRegistered;
    }

    public void setIdentificationPlaceRegistered(String identificationPlaceRegistered) {
        this.identificationPlaceRegistered = identificationPlaceRegistered;
    }

    public String getIdentificationRegistrationNumber() {
        return identificationRegistrationNumber;
    }

    public void setIdentificationRegistrationNumber(String identificationRegistrationNumber) {
        this.identificationRegistrationNumber = identificationRegistrationNumber;
    }
}
