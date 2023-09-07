package uk.gov.companieshouse.overseasentitiesapi.model.dto.trust;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;


public class TrustCorporateDto {

    public static final String NAME_FIELD = "name";
    public static final String TYPE_FIELD = "type";
    public static final String DATE_BECAME_INTERESTED_PERSON_FIELD = "date_became_interested_person";
    public static final String REGISTERED_OFFICE_ADDRESS_FIELD = "registered_office_address";
    public static final String IS_SERVICE_ADDRESS_SAME_AS_PRINCIPAL_ADDRESS_FIELD = "is_service_address_same_as_principal_address";
    public static final String SERVICE_ADDRESS_FIELD = "service_address";
    public static final String IDENTIFICATION_LEGAL_FORM_FIELD = "identification_legal_form";
    public static final String IDENTIFICATION_LEGAL_AUTHORITY_FIELD = "identification_legal_authority";
    public static final String IDENTIFICATION_PLACE_REGISTERED_FIELD = "identification_place_registered";
    public static final String IDENTIFICATION_COUNTRY_REGISTRATION_FIELD = "identification_country_registration";
    public static final String IDENTIFICATION_REGISTRATION_NUMBER_FIELD = "identification_registration_number";

    public static final String IS_ON_REGISTER_IN_COUNTRY_FORMED_IN = "is_on_register_in_country_formed_in";

    @JsonProperty(TYPE_FIELD)
    private String type;

    @JsonInclude(NON_NULL)
    @JsonProperty(NAME_FIELD)
    private String name;

    @JsonInclude(NON_NULL)
    @JsonProperty(DATE_BECAME_INTERESTED_PERSON_FIELD)
    private LocalDate dateBecameInterestedPerson;

    @JsonInclude(NON_NULL)
    @JsonProperty(REGISTERED_OFFICE_ADDRESS_FIELD)
    private AddressDto registeredOfficeAddress;

    @JsonInclude(NON_NULL)
    @JsonProperty("ro_address_line_1")
    private String roAddressLine1;

    @JsonInclude(NON_NULL)
    @JsonProperty("ro_address_line_2")
    private String roAddressLine2;

    @JsonInclude(NON_NULL)
    @JsonProperty("ro_address_care_of")
    private String roAddressCareOf;

    @JsonInclude(NON_NULL)
    @JsonProperty("ro_address_country")
    private String roAddressCountry;

    @JsonInclude(NON_NULL)
    @JsonProperty("ro_address_locality")
    private String roAddressLocality;

    @JsonInclude(NON_NULL)
    @JsonProperty("ro_address_po_box")
    private String roAddressPoBox;

    @JsonInclude(NON_NULL)
    @JsonProperty("ro_address_postal_code")
    private String roAddressPostalCode;

    @JsonInclude(NON_NULL)
    @JsonProperty("ro_address_premises")
    private String roAddressPremises;

    @JsonInclude(NON_NULL)
    @JsonProperty("ro_address_region")
    private String roAddressRegion;

    @JsonInclude(NON_NULL)
    @JsonProperty(SERVICE_ADDRESS_FIELD)
    private AddressDto serviceAddress;

    @JsonInclude(NON_NULL)
    @JsonProperty("sa_address_line_1")
    private String saAddressLine1;

    @JsonInclude(NON_NULL)
    @JsonProperty("sa_address_line_2")
    private String saAddressLine2;

    @JsonInclude(NON_NULL)
    @JsonProperty("sa_address_care_of")
    private String saAddressCareOf;

    @JsonInclude(NON_NULL)
    @JsonProperty("sa_address_country")
    private String saAddressCountry;

    @JsonInclude(NON_NULL)
    @JsonProperty("sa_address_locality")
    private String saAddressLocality;

    @JsonInclude(NON_NULL)
    @JsonProperty("sa_address_po_box")
    private String saAddressPoBox;

    @JsonInclude(NON_NULL)
    @JsonProperty("sa_address_postal_code")
    private String saAddressPostalCode;

    @JsonInclude(NON_NULL)
    @JsonProperty("sa_address_premises")
    private String saAddressPremises;

    @JsonInclude(NON_NULL)
    @JsonProperty("sa_address_region")
    private String saAddressRegion;

    @JsonInclude(NON_NULL)
    @JsonProperty(IS_SERVICE_ADDRESS_SAME_AS_PRINCIPAL_ADDRESS_FIELD)
    private Boolean isServiceAddressSameAsPrincipalAddress;

    @JsonInclude(NON_NULL)
    @JsonProperty(IDENTIFICATION_COUNTRY_REGISTRATION_FIELD)
    private String identificationCountryRegistration;

    @JsonInclude(NON_NULL)
    @JsonProperty(IDENTIFICATION_LEGAL_AUTHORITY_FIELD)
    private String identificationLegalAuthority;

    @JsonInclude(NON_NULL)
    @JsonProperty(IDENTIFICATION_LEGAL_FORM_FIELD)
    private String identificationLegalForm;

    @JsonInclude(NON_NULL)
    @JsonProperty(IDENTIFICATION_PLACE_REGISTERED_FIELD)
    private String identificationPlaceRegistered;

    @JsonInclude(NON_NULL)
    @JsonProperty(IDENTIFICATION_REGISTRATION_NUMBER_FIELD)
    private String identificationRegistrationNumber;

    @JsonInclude(NON_NULL)
    @JsonProperty(IS_ON_REGISTER_IN_COUNTRY_FORMED_IN)
    private Boolean onRegisterInCountryFormedIn;

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
        // When converting from DTO to DAO the individual address fields will be present and need to be converted to an address object during the mapping process
        // When converting from DAO to DTO the individual fields will not be populated and so the address object just needs to be returned
        if (areAnyStringsNonNull(roAddressPremises, roAddressLine1, roAddressLine2, roAddressRegion, roAddressLocality, roAddressCountry, roAddressCareOf, roAddressPoBox, roAddressPostalCode)) {
            registeredOfficeAddress = new AddressDto();
            registeredOfficeAddress.setPropertyNameNumber(roAddressPremises);
            registeredOfficeAddress.setLine1(roAddressLine1);
            registeredOfficeAddress.setLine2(roAddressLine2);
            registeredOfficeAddress.setCounty(roAddressRegion);
            registeredOfficeAddress.setLocality(roAddressLocality);
            registeredOfficeAddress.setTown(roAddressLocality);
            registeredOfficeAddress.setCountry(roAddressCountry);
            registeredOfficeAddress.setCareOf(roAddressCareOf);
            registeredOfficeAddress.setPoBox(roAddressPoBox);
            registeredOfficeAddress.setPostcode(roAddressPostalCode);
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
        // When converting from DTO to DAO the individual address fields will be present and need to be converted to an address object during the mapping process
        // When converting from DAO to DTO the individual fields will not be populated and so the address object just needs to be returned
        if (Boolean.TRUE.equals(isServiceAddressSameAsPrincipalAddress)) {
            return getRegisteredOfficeAddress();
        }
        if (areAnyStringsNonNull(saAddressPremises, saAddressLine1, saAddressLine2, saAddressRegion, saAddressLocality, saAddressCountry, saAddressCareOf, saAddressPoBox, saAddressPostalCode)) {
            serviceAddress = new AddressDto();
            serviceAddress.setPropertyNameNumber(saAddressPremises);
            serviceAddress.setLine1(saAddressLine1);
            serviceAddress.setLine2(saAddressLine2);
            serviceAddress.setCounty(saAddressRegion);
            serviceAddress.setLocality(saAddressLocality);
            serviceAddress.setTown(saAddressLocality);
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

    public Boolean getServiceAddressSameAsPrincipalAddress() {
        return isServiceAddressSameAsPrincipalAddress;
    }

    public void setServiceAddressSameAsPrincipalAddress(Boolean serviceAddressSameAsPrincipalAddress) {
        isServiceAddressSameAsPrincipalAddress = serviceAddressSameAsPrincipalAddress;
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

    public static boolean areAnyStringsNonNull(String... objects) {
        for (Object o: objects) {
            if (Objects.nonNull(o)) {
                return true;
            }
        }
        return false;
    }

    public Boolean getOnRegisterInCountryFormedIn() {
        return onRegisterInCountryFormedIn;
    }

    public void setOnRegisterInCountryFormedIn(Boolean onRegisterInCountryFormedIn) {
        this.onRegisterInCountryFormedIn = onRegisterInCountryFormedIn;
    }
}
