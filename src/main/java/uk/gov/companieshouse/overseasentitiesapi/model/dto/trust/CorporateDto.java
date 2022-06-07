package uk.gov.companieshouse.overseasentitiesapi.model.dto.trust;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;

public class CorporateDto {
    @JsonProperty("type")
    private String type;

    @JsonProperty("name")
    private String name;

    @JsonProperty("date_became_interested_person_day")
    private String dateBecameInterestedPersonDay;

    @JsonProperty("date_became_interested_person_month")
    private String dateBecameInterestedPersonMonth;

    @JsonProperty("date_became_interested_person_year")
    private String dateBecameInterestedPersonYear;

    @JsonProperty("ro_address_line_1")
    private String roAddressLine1;

    @JsonProperty("ro_address_line_2")
    private String roAddressLine2;

    @JsonProperty("ro_address_care_of")
    private String roAddressCareOf;

    @JsonProperty("ro_address_country")
    private String roAddressCountry;

    @JsonProperty("ro_address_locality")
    private String roAddressLocality;

    @JsonProperty("ro_address_po_box")
    private String roAddressPoBox;

    @JsonProperty("ro_address_postal_code")
    private String roAddressPostalCode;

    @JsonProperty("ro_address_premises")
    private String roAddressPremises;

    @JsonProperty("ro_address_region")
    private String roAddressRegion;

    @JsonProperty("sa_address_line_1")
    private String saAddressLine1;

    @JsonProperty("sa_address_line_2")
    private String saAddressLine2;

    @JsonProperty("sa_address_care_of")
    private String saAddressCareOf;

    @JsonProperty("sa_address_country")
    private String saAddressCountry;

    @JsonProperty("sa_address_locality")
    private String saAddressLocality;

    @JsonProperty("sa_address_po_box")
    private String saAddressPoBox;

    @JsonProperty("sa_address_postal_code")
    private String saAddressPostalCode;

    @JsonProperty("sa_address_premises")
    private String saAddressPremises;

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

    public String getDateBecameInterestedPersonDay() {
        return dateBecameInterestedPersonDay;
    }

    public void setDateBecameInterestedPersonDay(String dateBecameInterestedPersonDay) {
        this.dateBecameInterestedPersonDay = dateBecameInterestedPersonDay;
    }

    public String getDateBecameInterestedPersonMonth() {
        return dateBecameInterestedPersonMonth;
    }

    public void setDateBecameInterestedPersonMonth(String dateBecameInterestedPersonMonth) {
        this.dateBecameInterestedPersonMonth = dateBecameInterestedPersonMonth;
    }

    public String getDateBecameInterestedPersonYear() {
        return dateBecameInterestedPersonYear;
    }

    public void setDateBecameInterestedPersonYear(String dateBecameInterestedPersonYear) {
        this.dateBecameInterestedPersonYear = dateBecameInterestedPersonYear;
    }

    public String getDateBecameInterestedPerson() {
        if (type.equals("Corporate Interested Person")
                && dateBecameInterestedPersonYear != null
                && dateBecameInterestedPersonMonth != null
                && dateBecameInterestedPersonDay != null
        ) {
            var dateFields = new String[]{dateBecameInterestedPersonYear, dateBecameInterestedPersonMonth, dateBecameInterestedPersonDay};
            return String.join("-", dateFields);
        }
        return null;
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
        var address = new AddressDto();
        address.setPropertyNameNumber(roAddressPremises);
        address.setLine1(roAddressLine1);
        address.setLine2(roAddressLine2);
        address.setCounty(roAddressRegion);
        address.setLocality(roAddressLocality);
        address.setCountry(roAddressCountry);
        address.setCareOf(roAddressCareOf);
        address.setPoBox(roAddressPoBox);
        address.setPostcode(roAddressPostalCode);
        return address;
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
        var serviceAddress = new AddressDto();
        serviceAddress.setPropertyNameNumber(saAddressPremises);
        serviceAddress.setLine1(saAddressLine1);
        serviceAddress.setLine2(saAddressLine2);
        serviceAddress.setCounty(saAddressRegion);
        serviceAddress.setLocality(saAddressLocality);
        serviceAddress.setCountry(saAddressCountry);
        serviceAddress.setCareOf(saAddressCareOf);
        serviceAddress.setPoBox(saAddressPoBox);
        serviceAddress.setPostcode(saAddressPostalCode);
        return serviceAddress;
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
