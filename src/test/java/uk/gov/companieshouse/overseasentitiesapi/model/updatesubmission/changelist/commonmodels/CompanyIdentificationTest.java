package uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CompanyIdentificationTest {

  @Test
  void testGettersAndSetters() {
    String legalForm = "LLC";
    String governingLaw = "Some Law";
    String registerLocation = "Some Location";
    String placeRegistered = "Some Place";
    String registerJurisdiction = "Jurisdiction";
    String registrationNumber = "123456789";

    CompanyIdentification companyIdentification = new CompanyIdentification(legalForm, governingLaw,
        registerLocation, placeRegistered,registerJurisdiction, registrationNumber);

    String actualLegalForm = companyIdentification.getLegalForm();
    String actualGoverningLaw = companyIdentification.getGoverningLaw();
    String actualRegisterLocation = companyIdentification.getRegisterLocation();
    String actualPlaceRegistered = companyIdentification.getPlaceRegistered();
    String actualRegisterJurisdiction = companyIdentification.getPlaceRegisteredJurisdiction();
    String actualRegistrationNumber = companyIdentification.getRegistrationNumber();

    assertEquals(legalForm, actualLegalForm);
    assertEquals(governingLaw, actualGoverningLaw);
    assertEquals(registerLocation, actualRegisterLocation);
    assertEquals(placeRegistered, actualPlaceRegistered);
    assertEquals(registerJurisdiction, actualRegisterJurisdiction);
    assertEquals(registrationNumber, actualRegistrationNumber);

    String newLegalForm = "Corporation";
    String newGoverningLaw = "New Law";
    String newRegisterLocation = "New Location";
    String newPlaceRegistered = "New Place";
    String newRegistrationNumber = "987654321";

    companyIdentification.setLegalForm(newLegalForm);
    companyIdentification.setGoverningLaw(newGoverningLaw);
    companyIdentification.setRegisterLocation(newRegisterLocation);
    companyIdentification.setPlaceRegistered(newPlaceRegistered);
    companyIdentification.setRegistrationNumber(newRegistrationNumber);

    assertEquals(newLegalForm, companyIdentification.getLegalForm());
    assertEquals(newGoverningLaw, companyIdentification.getGoverningLaw());
    assertEquals(newRegisterLocation, companyIdentification.getRegisterLocation());
    assertEquals(newPlaceRegistered, companyIdentification.getPlaceRegistered());
    assertEquals(newRegistrationNumber, companyIdentification.getRegistrationNumber());
  }

  @Test
  void testEqualsAndHashCode() {

    String legalForm = "LLC";
    String governingLaw = "Some Law";
    String registerLocation = "Some Location";
    String placeRegistered = "Some Place";
    String registerJurisdiction = "Jurisdiction";
    String registrationNumber = "123456789";

    CompanyIdentification companyIdentification1 = new CompanyIdentification(legalForm,
        governingLaw,
        registerLocation, placeRegistered, registerJurisdiction, registrationNumber);
    CompanyIdentification companyIdentification2 = new CompanyIdentification(legalForm,
        governingLaw,
        registerLocation, placeRegistered, registerJurisdiction, registrationNumber);

    assertEquals(companyIdentification1, companyIdentification2);
    assertEquals(companyIdentification1.hashCode(), companyIdentification2.hashCode());
  }

  @Test
  void testMockitoVerification() {

    CompanyIdentification mockCompanyIdentification = Mockito.mock(CompanyIdentification.class);

    when(mockCompanyIdentification.getLegalForm()).thenReturn("LLC");

    assertEquals("LLC", mockCompanyIdentification.getLegalForm());
    verify(mockCompanyIdentification).getLegalForm();
  }
}
