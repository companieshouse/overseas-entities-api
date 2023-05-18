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
    void testCompareCompanyIdentificationChangeDifferentLegalFormReturnsObject() {
        var updatedLegalForm = "New legal form";
        var existing = new CompanyIdentification(TEST_EXISTING_VALUE, TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                TEST_EXISTING_VALUE, TEST_EXISTING_VALUE);
        var updated = new CompanyIdentification(updatedLegalForm, TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                TEST_EXISTING_VALUE, TEST_EXISTING_VALUE);

        var result = overseasEntityChangeComparator.compareCompanyIdentification(existing, updated);

        assertNotNull(result);
        assertEquals(CHANGE_COMPANY_IDENTIFICATION, result.getChangeName());
        assertEquals(updatedLegalForm, result.getProposedLegalForm());
        assertEquals(TEST_EXISTING_VALUE, result.getProposedGoverningLaw());
        assertEquals(TEST_EXISTING_VALUE, result.getProposedRegisterLocation());
        assertEquals(TEST_EXISTING_VALUE, result.getProposedPlaceRegistered());
        assertEquals(TEST_EXISTING_VALUE, result.getProposedRegistrationNumber());
    }

    @Test
    void testCompareCompanyIdentificationChangeDifferentGoverningLawReturnsObject() {
        var updatedGoverningLaw = "New governing law";
        var existing = new CompanyIdentification(TEST_EXISTING_VALUE, TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                TEST_EXISTING_VALUE, TEST_EXISTING_VALUE);
        var updated = new CompanyIdentification(TEST_EXISTING_VALUE, updatedGoverningLaw, TEST_EXISTING_VALUE,
                TEST_EXISTING_VALUE, TEST_EXISTING_VALUE);

        var result = overseasEntityChangeComparator.compareCompanyIdentification(existing, updated);

        assertNotNull(result);
        assertEquals(CHANGE_COMPANY_IDENTIFICATION, result.getChangeName());
        assertEquals(TEST_EXISTING_VALUE, result.getProposedLegalForm());
        assertEquals(updatedGoverningLaw, result.getProposedGoverningLaw());
        assertEquals(TEST_EXISTING_VALUE, result.getProposedRegisterLocation());
        assertEquals(TEST_EXISTING_VALUE, result.getProposedPlaceRegistered());
        assertEquals(TEST_EXISTING_VALUE, result.getProposedRegistrationNumber());
    }

    @Test
    void testCompareCompanyIdentificationChangeDifferentRegisterLocationReturnsObject() {
        var updatedRegisterLocation = "New register location";
        var existing = new CompanyIdentification(TEST_EXISTING_VALUE, TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                TEST_EXISTING_VALUE, TEST_EXISTING_VALUE);
        var updated = new CompanyIdentification(TEST_EXISTING_VALUE, TEST_EXISTING_VALUE, updatedRegisterLocation,
                TEST_EXISTING_VALUE, TEST_EXISTING_VALUE);

        var result = overseasEntityChangeComparator.compareCompanyIdentification(existing, updated);

        assertNotNull(result);
        assertEquals(CHANGE_COMPANY_IDENTIFICATION, result.getChangeName());
        assertEquals(TEST_EXISTING_VALUE, result.getProposedLegalForm());
        assertEquals(TEST_EXISTING_VALUE, result.getProposedGoverningLaw());
        assertEquals(updatedRegisterLocation, result.getProposedRegisterLocation());
        assertEquals(TEST_EXISTING_VALUE, result.getProposedPlaceRegistered());
        assertEquals(TEST_EXISTING_VALUE, result.getProposedRegistrationNumber());
    }

    @Test
    void testCompareCompanyIdentificationChangeDifferentPlaceRegisteredReturnsObject() {
        var updatedPlaceRegistered = "New place registered";
        var existing = new CompanyIdentification(TEST_EXISTING_VALUE, TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                TEST_EXISTING_VALUE, TEST_EXISTING_VALUE);
        var updated = new CompanyIdentification(TEST_EXISTING_VALUE, TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                updatedPlaceRegistered, TEST_EXISTING_VALUE);

        var result = overseasEntityChangeComparator.compareCompanyIdentification(existing, updated);

        assertNotNull(result);
        assertEquals(CHANGE_COMPANY_IDENTIFICATION, result.getChangeName());
        assertEquals(TEST_EXISTING_VALUE, result.getProposedLegalForm());
        assertEquals(TEST_EXISTING_VALUE, result.getProposedGoverningLaw());
        assertEquals(TEST_EXISTING_VALUE, result.getProposedRegisterLocation());
        assertEquals(updatedPlaceRegistered, result.getProposedPlaceRegistered());
        assertEquals(TEST_EXISTING_VALUE, result.getProposedRegistrationNumber());
    }

    @Test
    void testCompareCompanyIdentificationChangeDifferentRegistrationNumberReturnsObject() {
        var updatedRegistrationNumber = "New registration number";
        var existing = new CompanyIdentification(TEST_EXISTING_VALUE, TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                TEST_EXISTING_VALUE, TEST_EXISTING_VALUE);
        var updated = new CompanyIdentification(TEST_EXISTING_VALUE, TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                TEST_EXISTING_VALUE, updatedRegistrationNumber);

        var result = overseasEntityChangeComparator.compareCompanyIdentification(existing, updated);

        assertNotNull(result);
        assertEquals(CHANGE_COMPANY_IDENTIFICATION, result.getChangeName());
        assertEquals(TEST_EXISTING_VALUE, result.getProposedLegalForm());
        assertEquals(TEST_EXISTING_VALUE, result.getProposedGoverningLaw());
        assertEquals(TEST_EXISTING_VALUE, result.getProposedRegisterLocation());
        assertEquals(TEST_EXISTING_VALUE, result.getProposedPlaceRegistered());
        assertEquals(updatedRegistrationNumber, result.getProposedRegistrationNumber());
    }

    @Test
    void testCompareCompanyIdentificationChangeAllSameValueReturnsNull() {
        var existing = new CompanyIdentification(TEST_EXISTING_VALUE, TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                TEST_EXISTING_VALUE, TEST_EXISTING_VALUE);
        var updated = new CompanyIdentification(TEST_EXISTING_VALUE, TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                TEST_EXISTING_VALUE, TEST_EXISTING_VALUE);
        var result = overseasEntityChangeComparator.compareCompanyIdentification(existing, updated);

        assertNull(result);
    }

    @Test
    void testCompareCompanyIdentificationChangeNullUpdateValueReturnsNull() {
        var existing = new CompanyIdentification(TEST_EXISTING_VALUE, TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                TEST_EXISTING_VALUE, TEST_EXISTING_VALUE);
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
