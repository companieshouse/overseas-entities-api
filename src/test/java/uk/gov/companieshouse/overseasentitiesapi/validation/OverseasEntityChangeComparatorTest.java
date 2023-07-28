package uk.gov.companieshouse.overseasentitiesapi.validation;

import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.api.model.company.RegisteredOfficeAddressApi;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;
import uk.gov.companieshouse.overseasentitiesapi.model.updatesubmission.changelist.commonmodels.CompanyIdentification;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OverseasEntityChangeComparatorTest {
    private final String CHANGE_ENTITY_NAME = "changeOfEntityName";
    private final String CHANGE_PRINCIPAL_ADDRESS = "changeOfRoa";
    private final String CHANGE_CORRESPONDENCE_ADDRESS = "changeOfServiceAddress";
    private final String CHANGE_COMPANY_IDENTIFICATION = "changeOfCompanyIdentification";
    private final String CHANGE_ENTITY_EMAIL_ADDRESS = "entityEmailAddress";
    private final String TEST_EXISTING_VALUE = "TEST";
    private final String TEST_EXISTING_PLACE_REGISTERED_VALUE = "NAME";
    private final String TEST_EXISTING_PLACE_REGISTERED_JURISDICTION_VALUE = "JURISDICTION";
    private final String TEST_EXISTING_REGISTER_VALUE = "NAME,JURISDICTION";
    private final String UPDATED_LEGAL_FORM_VALUE = "NEW LEGAL FORM";
    private final String UPDATED_GOVERNING_LAW_VALUE = "NEW GOVERNING LAW";
    private final String UPDATED_REGISTER_LOCATION_VALUE = "NEW REGISTER LOCATION";
    private final String UPDATED_PLACE_REGISTERED_VALUE = "NEW NAME";
    private final String UPDATED_PLACE_REGISTERED_JURISDICTION_VALUE = "NEW JURISDICTION";
    private final String UPDATED_REGISTRATION_NUMBER_VALUE = "NEW REGISTRATION NUMBER";
    private OverseasEntityChangeComparator overseasEntityChangeComparator = new OverseasEntityChangeComparator();

    @Test
    void testCompareEntityNameChangeDifferentValueReturnsObject() {
        var updated = "New name";

        var result = overseasEntityChangeComparator.compareEntityName(TEST_EXISTING_VALUE, updated);

        assertNotNull(result);
        assertEquals(CHANGE_ENTITY_NAME, result.getChangeName());
        assertEquals(updated, result.getProposedCorporateBodyName());
    }

    @Test
    void testCompareEntityNameChangeSameValueReturnsNull() {
        var result = overseasEntityChangeComparator.compareEntityName(TEST_EXISTING_VALUE, TEST_EXISTING_VALUE);

        assertNull(result);
    }

    @Test
    void testCompareEntityNameChangeNullUpdateValueReturnsNull() {
        var result = overseasEntityChangeComparator.compareEntityName(TEST_EXISTING_VALUE, null);

        assertNull(result);
    }

    @Test
    void testComparePrincipalAddressChangeDifferentValueReturnsObject() {
        var existing = new RegisteredOfficeAddressApi();
        var updated = new AddressDto();
        existing.setCountry("Ireland");
        updated.setCountry("England");

        var result = overseasEntityChangeComparator.comparePrincipalAddress(existing, updated);

        assertNotNull(result);
        assertEquals(CHANGE_PRINCIPAL_ADDRESS, result.getChangeName());
        assertEquals("England", result.getProposedRegisteredOfficeAddress().getCountry());
    }

    @Test
    void testComparePrincipalAddressChangeSameValueReturnsNull() {
        var existing = new RegisteredOfficeAddressApi();
        var updated = new AddressDto();
        existing.setCountry("Ireland");
        updated.setCountry("Ireland");

        var result = overseasEntityChangeComparator.comparePrincipalAddress(existing, updated);

        assertNull(result);
    }

    @Test
    void testComparePrincipalAddressChangeNullUpdateValueReturnsNull() {
        var existing = new RegisteredOfficeAddressApi();
        existing.setCountry("Ireland");

        var result = overseasEntityChangeComparator.comparePrincipalAddress(existing, null);

        assertNull(result);
    }

    @Test
    void testComparePrincipalAddressChangeNullExistingValueReturnsObject() {
        var updated = new AddressDto();
        updated.setCountry("Ireland");

        var result = overseasEntityChangeComparator.comparePrincipalAddress(null, updated);

        assertNotNull(result);
        assertEquals(CHANGE_PRINCIPAL_ADDRESS, result.getChangeName());
        assertEquals("Ireland", result.getProposedRegisteredOfficeAddress().getCountry());
    }

    @Test
    void testComparePrincipalAddressChangeBothInputsNullReturnsNull() {
        var result = overseasEntityChangeComparator.comparePrincipalAddress(null, null);

        assertNull(result);
    }


    @Test
    void testComparePrincipleAddressChangeAddressLineWithExistingPremisesValueJoinedReturnsNull() {
        var existing = new RegisteredOfficeAddressApi();
        var updated = new AddressDto();
        existing.setPremises("123");
        existing.setAddressLine1("School lane");
        updated.setPropertyNameNumber("");
        updated.setLine1("123 School lane");

        var result = overseasEntityChangeComparator.comparePrincipalAddress(existing, updated);

        assertNull(result);
    }

    @Test
    void testComparePrincipalAddressChangeAddressLineWithUpdatePremisesValueJoinedReturnsNull() {
        var existing = new RegisteredOfficeAddressApi();
        var updated = new AddressDto();
        existing.setPremises(null);
        existing.setAddressLine1("123 School lane");
        updated.setPropertyNameNumber("123");
        updated.setLine1("School lane");

        var result = overseasEntityChangeComparator.comparePrincipalAddress(existing, updated);

        assertNull(result);
    }

    @Test
    void testComparePrincipalAddressChangeAddressLineWithUpdatePremisesValueJoinedButDifferentReturnsChange() {
        var existing = new RegisteredOfficeAddressApi();
        var updated = new AddressDto();
        existing.setPremises("123");
        existing.setAddressLine1("School lane");
        updated.setPropertyNameNumber("");
        updated.setLine1("100 School lane");

        var result = overseasEntityChangeComparator.comparePrincipalAddress(existing, updated);

        assertNotNull(result);
    }

    @Test
    void testComparePrincipalAddressChangeAddressLineWithExistingPremisesValueJoinedButDifferentReturnsChange() {
        var existing = new RegisteredOfficeAddressApi();
        var updated = new AddressDto();
        existing.setPremises(null);
        existing.setAddressLine1("123 School lane");
        updated.setPropertyNameNumber("100");
        updated.setLine1("School lane");

        var result = overseasEntityChangeComparator.comparePrincipalAddress(existing, updated);

        assertNotNull(result);
    }

    @Test
    void testCompareCorrespondenceAddressChangeDifferentValueReturnsObject() {
        var existing = new RegisteredOfficeAddressApi();
        var updated = new AddressDto();
        existing.setCountry("Ireland");
        updated.setCountry("England");

        var result = overseasEntityChangeComparator.compareCorrespondenceAddress(existing, updated);

        assertNotNull(result);
        assertEquals(CHANGE_CORRESPONDENCE_ADDRESS, result.getChangeName());
        assertEquals("England", result.getProposedServiceAddress().getCountry());
    }

    @Test
    void testCompareCorrespondenceAddressChangeSameValueReturnsNull() {
        var existing = new RegisteredOfficeAddressApi();
        var updated = new AddressDto();
        existing.setCountry("Ireland");
        updated.setCountry("Ireland");

        var result = overseasEntityChangeComparator.compareCorrespondenceAddress(existing, updated);

        assertNull(result);
    }

    @Test
    void testCompareCorrespondenceAddressChangeNullUpdateValueReturnsNull() {
        var existing = new RegisteredOfficeAddressApi();
        existing.setCountry("Ireland");
        var result = overseasEntityChangeComparator.compareCorrespondenceAddress(existing, null);

        assertNull(result);
    }

    @Test
    void testCompareCorrespondenceAddressChangeNullExistingValueReturnsObject() {
        var updated = new AddressDto();
        updated.setCountry("Ireland");

        var result = overseasEntityChangeComparator.compareCorrespondenceAddress(null, updated);

        assertNotNull(result);
        assertEquals(CHANGE_CORRESPONDENCE_ADDRESS, result.getChangeName());
        assertEquals("Ireland", result.getProposedServiceAddress().getCountry());
    }

    @Test
    void testCompareCorrespondenceAddressChangeAddressLineWithExistingPremisesValueJoinedReturnsNull() {
        var existing = new RegisteredOfficeAddressApi();
        var updated = new AddressDto();
        existing.setPremises("123");
        existing.setAddressLine1("School lane");
        updated.setPropertyNameNumber("");
        updated.setLine1("123 School lane");

        var result = overseasEntityChangeComparator.compareCorrespondenceAddress(existing, updated);

        assertNull(result);
    }

    @Test
    void testCompareCorrespondenceAddressChangeAddressLineWithUpdatePremisesValueJoinedReturnsNull() {
        var existing = new RegisteredOfficeAddressApi();
        var updated = new AddressDto();
        existing.setPremises(null);
        existing.setAddressLine1("123 School lane");
        updated.setPropertyNameNumber("123");
        updated.setLine1("School lane");

        var result = overseasEntityChangeComparator.compareCorrespondenceAddress(existing, updated);

        assertNull(result);
    }

    @Test
    void testCompareCorrespondenceAddressChangeAddressLineWithUpdatePremisesValueJoinedButDifferentReturnsChange() {
        var existing = new RegisteredOfficeAddressApi();
        var updated = new AddressDto();
        existing.setPremises("123");
        existing.setAddressLine1("School lane");
        updated.setPropertyNameNumber("");
        updated.setLine1("100 School lane");

        var result = overseasEntityChangeComparator.compareCorrespondenceAddress(existing, updated);

        assertNotNull(result);
    }

    @Test
    void testCompareCorrespondenceAddressChangeAddressLineWithExistingPremisesValueJoinedButDifferentReturnsChange() {
        var existing = new RegisteredOfficeAddressApi();
        var updated = new AddressDto();
        existing.setPremises(null);
        existing.setAddressLine1("123 School lane");
        updated.setPropertyNameNumber("100");
        updated.setLine1("School lane");

        var result = overseasEntityChangeComparator.compareCorrespondenceAddress(existing, updated);

        assertNotNull(result);
    }

    @Test
    void testCompareCorrespondenceAddressChangeBothInputsNullReturnsNull() {
        var result = overseasEntityChangeComparator.compareCorrespondenceAddress(null, null);

        assertNull(result);
    }

    @Test
    void testCompareCompanyIdentificationChangeAllFieldsDifferentReturnsObject() {
        var existing = new CompanyIdentification(TEST_EXISTING_VALUE, TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                TEST_EXISTING_VALUE, TEST_EXISTING_VALUE, TEST_EXISTING_VALUE);
        var updated = new CompanyIdentification(UPDATED_LEGAL_FORM_VALUE, UPDATED_GOVERNING_LAW_VALUE,
                UPDATED_REGISTER_LOCATION_VALUE, UPDATED_PLACE_REGISTERED_VALUE,
                UPDATED_PLACE_REGISTERED_JURISDICTION_VALUE, UPDATED_REGISTRATION_NUMBER_VALUE);

        var result = overseasEntityChangeComparator.compareCompanyIdentification(existing, updated);

        assertNotNull(result);
        assertEquals(CHANGE_COMPANY_IDENTIFICATION, result.getChangeName());
        assertEquals(UPDATED_LEGAL_FORM_VALUE, result.getProposedLegalForm());
        assertEquals(UPDATED_GOVERNING_LAW_VALUE, result.getProposedGoverningLaw());
        assertEquals(UPDATED_REGISTER_LOCATION_VALUE, result.getProposedRegisterLocation());
        assertEquals(UPDATED_PLACE_REGISTERED_VALUE, result.getProposedPlaceRegistered());
        assertEquals(UPDATED_REGISTRATION_NUMBER_VALUE, result.getProposedRegistrationNumber());
    }

    @Test
    void testCompareCompanyIdentificationChangeExistingValuesNullReturnsObject() {
        var existing = new CompanyIdentification(null, null, null, null, null, null);
        var updated = new CompanyIdentification(UPDATED_LEGAL_FORM_VALUE, UPDATED_GOVERNING_LAW_VALUE,
                UPDATED_REGISTER_LOCATION_VALUE, UPDATED_PLACE_REGISTERED_VALUE,
                UPDATED_PLACE_REGISTERED_JURISDICTION_VALUE, UPDATED_REGISTRATION_NUMBER_VALUE);

        var result = overseasEntityChangeComparator.compareCompanyIdentification(existing, updated);

        assertNotNull(result);
        assertEquals(CHANGE_COMPANY_IDENTIFICATION, result.getChangeName());
        assertEquals(UPDATED_LEGAL_FORM_VALUE, result.getProposedLegalForm());
        assertEquals(UPDATED_GOVERNING_LAW_VALUE, result.getProposedGoverningLaw());
        assertEquals(UPDATED_REGISTER_LOCATION_VALUE, result.getProposedRegisterLocation());
        assertEquals(UPDATED_PLACE_REGISTERED_VALUE, result.getProposedPlaceRegistered());
        assertEquals(UPDATED_REGISTRATION_NUMBER_VALUE, result.getProposedRegistrationNumber());
    }

    @Test
    void testCompareCompanyIdentificationChangeDifferentLegalFormReturnsObject() {
        var existing = new CompanyIdentification(TEST_EXISTING_VALUE, TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                TEST_EXISTING_REGISTER_VALUE, null, TEST_EXISTING_VALUE);
        var updated = new CompanyIdentification(UPDATED_LEGAL_FORM_VALUE, TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                TEST_EXISTING_PLACE_REGISTERED_VALUE, TEST_EXISTING_PLACE_REGISTERED_JURISDICTION_VALUE, TEST_EXISTING_VALUE);

        var result = overseasEntityChangeComparator.compareCompanyIdentification(existing, updated);

        assertNotNull(result);
        assertEquals(CHANGE_COMPANY_IDENTIFICATION, result.getChangeName());
        assertEquals(UPDATED_LEGAL_FORM_VALUE, result.getProposedLegalForm());
        assertNull(result.getProposedGoverningLaw());
        assertNull(result.getProposedRegisterLocation());
        assertNull(result.getProposedPlaceRegistered());
        assertNull(result.getProposedRegisterJurisdiction());
        assertNull(result.getProposedRegistrationNumber());
    }

    @Test
    void testCompareCompanyIdentificationChangeDifferentGoverningLawReturnsObject() {
        var existing = new CompanyIdentification(TEST_EXISTING_VALUE, TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                TEST_EXISTING_REGISTER_VALUE, null, TEST_EXISTING_VALUE);
        var updated = new CompanyIdentification(TEST_EXISTING_VALUE, UPDATED_GOVERNING_LAW_VALUE, TEST_EXISTING_VALUE,
                TEST_EXISTING_PLACE_REGISTERED_VALUE, TEST_EXISTING_PLACE_REGISTERED_JURISDICTION_VALUE, TEST_EXISTING_VALUE);

        var result = overseasEntityChangeComparator.compareCompanyIdentification(existing, updated);

        assertNotNull(result);
        assertEquals(CHANGE_COMPANY_IDENTIFICATION, result.getChangeName());
        assertNull(result.getProposedLegalForm());
        assertEquals(UPDATED_GOVERNING_LAW_VALUE, result.getProposedGoverningLaw());
        assertNull(result.getProposedRegisterLocation());
        assertNull(result.getProposedPlaceRegistered());
        assertNull(result.getProposedRegisterJurisdiction());
        assertNull(result.getProposedRegistrationNumber());
    }

    @Test
    void testCompareCompanyIdentificationChangeDifferentRegisterLocationReturnsObject() {
        var existing = new CompanyIdentification(TEST_EXISTING_VALUE, TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                TEST_EXISTING_REGISTER_VALUE, null, TEST_EXISTING_VALUE);
        var updated = new CompanyIdentification(TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                UPDATED_REGISTER_LOCATION_VALUE, TEST_EXISTING_PLACE_REGISTERED_VALUE,
                TEST_EXISTING_PLACE_REGISTERED_JURISDICTION_VALUE, TEST_EXISTING_VALUE);

        var result = overseasEntityChangeComparator.compareCompanyIdentification(existing, updated);

        assertNotNull(result);
        assertEquals(CHANGE_COMPANY_IDENTIFICATION, result.getChangeName());
        assertNull(result.getProposedLegalForm());
        assertNull(result.getProposedGoverningLaw());
        assertEquals(UPDATED_REGISTER_LOCATION_VALUE, result.getProposedRegisterLocation());
        assertNull(result.getProposedPlaceRegistered());
        assertNull(result.getProposedRegisterJurisdiction());
        assertNull(result.getProposedRegistrationNumber());
    }

    @Test
    void testCompareCompanyIdentificationChangeDifferentPlaceRegisteredReturnsObject() {
        var existing = new CompanyIdentification(TEST_EXISTING_VALUE, TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                TEST_EXISTING_REGISTER_VALUE, null, TEST_EXISTING_VALUE);
        var updated = new CompanyIdentification(TEST_EXISTING_VALUE, TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                UPDATED_PLACE_REGISTERED_VALUE, UPDATED_PLACE_REGISTERED_JURISDICTION_VALUE, TEST_EXISTING_VALUE);

        var result = overseasEntityChangeComparator.compareCompanyIdentification(existing, updated);

        assertNotNull(result);
        assertEquals(CHANGE_COMPANY_IDENTIFICATION, result.getChangeName());
        assertNull(result.getProposedLegalForm());
        assertNull(result.getProposedGoverningLaw());
        assertNull(result.getProposedRegisterLocation());
        assertEquals(UPDATED_PLACE_REGISTERED_VALUE, result.getProposedPlaceRegistered());
        assertNull(result.getProposedRegistrationNumber());
    }

    @Test
    void testCompareCompanyIdentificationChangeDifferentRegistrationNumberReturnsObject() {
        var existing = new CompanyIdentification(TEST_EXISTING_VALUE, TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                TEST_EXISTING_REGISTER_VALUE, null, TEST_EXISTING_VALUE);
        var updated = new CompanyIdentification(TEST_EXISTING_VALUE, TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                TEST_EXISTING_PLACE_REGISTERED_VALUE, TEST_EXISTING_PLACE_REGISTERED_JURISDICTION_VALUE, UPDATED_REGISTRATION_NUMBER_VALUE);

        var result = overseasEntityChangeComparator.compareCompanyIdentification(existing, updated);

        assertNotNull(result);
        assertEquals(CHANGE_COMPANY_IDENTIFICATION, result.getChangeName());
        assertNull(result.getProposedLegalForm());
        assertNull(result.getProposedGoverningLaw());
        assertNull(result.getProposedRegisterLocation());
        assertNull(result.getProposedPlaceRegistered());
        assertNull(result.getProposedRegisterLocation());
        assertEquals(UPDATED_REGISTRATION_NUMBER_VALUE, result.getProposedRegistrationNumber());
    }

    @Test
    void testCompareCompanyIdentificationChangeAllSameValueReturnsNull() {
        var existing = new CompanyIdentification(TEST_EXISTING_VALUE, TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                TEST_EXISTING_REGISTER_VALUE, null, TEST_EXISTING_VALUE);
        var updated = new CompanyIdentification(TEST_EXISTING_VALUE, TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                TEST_EXISTING_PLACE_REGISTERED_VALUE, TEST_EXISTING_PLACE_REGISTERED_JURISDICTION_VALUE, TEST_EXISTING_VALUE);
        var result = overseasEntityChangeComparator.compareCompanyIdentification(existing, updated);

        assertNull(result);
    }

    @Test
    void testCompareCompanyIdentificationChangeNullUpdateValueReturnsNull() {
        var existing = new CompanyIdentification(TEST_EXISTING_VALUE, TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                TEST_EXISTING_REGISTER_VALUE, null, TEST_EXISTING_VALUE);
        var result = overseasEntityChangeComparator.compareCompanyIdentification(existing, null);

        assertNull(result);
    }

    @Test
    void testCompareEntityEmailAddressChangeDifferentValueReturnsObject() {
        var updatedEmailAddress = "new-email@test.com";

        var result = overseasEntityChangeComparator.compareEntityEmailAddress(
                TEST_EXISTING_VALUE, updatedEmailAddress);

        assertNotNull(result);
        assertEquals(CHANGE_ENTITY_EMAIL_ADDRESS, result.getChangeName());
        assertEquals(updatedEmailAddress, result.getProposedEmailAddress());
    }

    @Test
    void testCompareEntityEmailAddressChangeSameValueReturnsNull() {
        var result = overseasEntityChangeComparator.compareEntityEmailAddress(
                TEST_EXISTING_VALUE, TEST_EXISTING_VALUE);

        assertNull(result);
    }

    @Test
    void testCompareEntityEmailAddressChangeNullUpdateValueReturnsNull() {
        var result = overseasEntityChangeComparator.compareEntityEmailAddress(TEST_EXISTING_VALUE, null);

        assertNull(result);
    }
}
