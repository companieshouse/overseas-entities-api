package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels;

import java.util.Objects;

public class CompanyIdentification {
    private String legalForm;

    private String governingLaw;

    private String registerLocation;

    private String placeRegistered;

    private String registrationNumber;

    public CompanyIdentification() {}

    public CompanyIdentification(String legalForm, String governingLaw, String registerLocation, String placeRegistered, String registrationNumber) {
        this.legalForm = legalForm;
        this.governingLaw = governingLaw;
        this.registerLocation = registerLocation;
        this.placeRegistered = placeRegistered;
        this.registrationNumber = registrationNumber;
    }

    public String getLegalForm() {
        return legalForm;
    }

    public void setLegalForm(String legalForm) {
        this.legalForm = legalForm;
    }

    public String getGoverningLaw() {
        return governingLaw;
    }

    public void setGoverningLaw(String governingLaw) {
        this.governingLaw = governingLaw;
    }

    public String getRegisterLocation() {
        return registerLocation;
    }

    public void setRegisterLocation(String registerLocation) {
        this.registerLocation = registerLocation;
    }

    public String getPlaceRegistered() {
        return placeRegistered;
    }

    public void setPlaceRegistered(String placeRegistered) {
        this.placeRegistered = placeRegistered;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyIdentification that = (CompanyIdentification) o;
        return Objects.equals(legalForm, that.legalForm)
                && Objects.equals(governingLaw, that.governingLaw)
                && Objects.equals(registerLocation, that.registerLocation)
                && Objects.equals(placeRegistered, that.placeRegistered)
                && Objects.equals(registrationNumber, that.registrationNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(legalForm, governingLaw, registerLocation, placeRegistered, registrationNumber);
    }
}
