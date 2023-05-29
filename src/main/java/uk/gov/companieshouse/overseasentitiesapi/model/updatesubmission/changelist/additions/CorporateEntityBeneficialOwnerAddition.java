package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.additions;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.Address;

import java.time.LocalDate;
import java.util.List;

public class CorporateEntityBeneficialOwnerAddition extends BeneficialOwnerAddition {
    private static final String APPOINTMENT_TYPE = "OE OLE BO";

    @JsonProperty("corporateName")
    private String corporateName;

    @JsonProperty("legalForm")
    private String legalForm;

    @JsonProperty("governingLaw")
    private String governingLaw;

    @JsonProperty("registerLocation")
    private String registerLocation;

    @JsonProperty("registrationNumber")
    private String registrationNumber;

    public CorporateEntityBeneficialOwnerAddition(LocalDate actionDate, LocalDate ceasedDate, Address residentialAddress,
                                                  Address serviceAddress, List<String> natureOfControls) {
        super(actionDate, ceasedDate, residentialAddress, serviceAddress, natureOfControls);
        setAppointmentType(APPOINTMENT_TYPE);
    }

    public String getCorporateName() {
        return corporateName;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
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

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
}
