package uk.gov.companieshouse.overseasentitiesapi.utils;

import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.overseasentitiesapi.model.dto.AddressDto;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OverseasEntityChangeUtilsTest {
    private final String CHANGE_ENTITY_NAME = "changeOfEntityName";
    private final String CHANGE_PRINCIPAL_ADDRESS = "changeOfRoa";
    private final String CHANGE_CORRESPONDENCE_ADDRESS = "changeOfServiceAddress";
    private final String CHANGE_COMPANY_IDENTIFICATION = "changeOfCompanyIdentification";
    private final String CHANGE_ENTITY_EMAIL_ADDRESS = "entityEmailAddress";
    private final String TEST_EXISTING_VALUE = "TEST";
    private OverseasEntityChangeUtils overseasEntityChangeUtils = new OverseasEntityChangeUtils();

    @Test
    void testVerifyEntityNameChangeDifferentValueReturnsObject() {
        var updated = "New name";

        var result = overseasEntityChangeUtils.verifyEntityNameChange(TEST_EXISTING_VALUE, updated);
        assertNotNull(result);
        assertEquals(CHANGE_ENTITY_NAME, result.getChangeName());
        assertEquals(updated, result.getProposedCorporateBodyName());
    }

    @Test
    void testVerifyEntityNameChangeSameValueReturnsNull() {
        var result = overseasEntityChangeUtils.verifyEntityNameChange(TEST_EXISTING_VALUE, TEST_EXISTING_VALUE);

        assertNull(result);
    }

    @Test
    void testVerifyPrincipalAddressChangeDifferentValueReturnsObject() {
        var existing = new AddressDto(){{ setCountry("Ireland"); }};
        var updated = new AddressDto(){{ setCountry("England"); }};

        var result = overseasEntityChangeUtils.verifyPrincipalAddressChange(existing, updated);
        assertNotNull(result);
        assertEquals(CHANGE_PRINCIPAL_ADDRESS, result.getChangeName());
        assertEquals("England", result.getAddress().getCountry());
    }

    @Test
    void testVerifyPrincipalAddressChangeSameValueReturnsNull() {
        var existing = new AddressDto(){{ setCountry("Ireland"); }};
        var updated = new AddressDto(){{ setCountry("Ireland"); }};

        var result = overseasEntityChangeUtils.verifyPrincipalAddressChange(existing, updated);
        assertNull(result);
    }

    @Test
    void testVerifyCorrespondenceAddressChangeDifferentValueReturnsObject() {
        var existing = new AddressDto(){{ setCountry("Ireland"); }};
        var updated = new AddressDto(){{ setCountry("England"); }};

        var result = overseasEntityChangeUtils.verifyCorrespondenceAddressChange(existing, updated);
        assertNotNull(result);
        assertEquals(CHANGE_CORRESPONDENCE_ADDRESS, result.getChangeName());
        assertEquals("England", result.getAddress().getCountry());
    }

    @Test
    void testVerifyCorrespondenceAddressChangeSameValueReturnsNull() {
        var existing = new AddressDto(){{ setCountry("Ireland"); }};
        var updated = new AddressDto(){{ setCountry("Ireland"); }};

        var result = overseasEntityChangeUtils.verifyCorrespondenceAddressChange(existing, updated);
        assertNull(result);
    }

    @Test
    void testVerifyCompanyIdentificationChangeDifferentLegalFormReturnsObject() {
        var updatedLegalForm = "New legal form";

        var result = overseasEntityChangeUtils.verifyCompanyIdentificationChange(
                TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                updatedLegalForm, TEST_EXISTING_VALUE,
                TEST_EXISTING_VALUE, TEST_EXISTING_VALUE);
        assertNotNull(result);
        assertEquals(CHANGE_COMPANY_IDENTIFICATION, result.getChangeName());
        assertEquals(updatedLegalForm, result.getProposedLegalForm());
        assertEquals(TEST_EXISTING_VALUE, result.getProposedGoverningLaw());
        assertEquals(TEST_EXISTING_VALUE, result.getProposedRegisterLocation());
        assertEquals(TEST_EXISTING_VALUE, result.getProposedCompanyIdentification());
    }

    @Test
    void testVerifyCompanyIdentificationChangeDifferentGoverningLawReturnsObject() {
        var updatedGoverningLaw = "New governing law";

        var result = overseasEntityChangeUtils.verifyCompanyIdentificationChange(
                TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                TEST_EXISTING_VALUE, updatedGoverningLaw,
                TEST_EXISTING_VALUE, TEST_EXISTING_VALUE);
        assertNotNull(result);
        assertEquals(CHANGE_COMPANY_IDENTIFICATION, result.getChangeName());
        assertEquals(TEST_EXISTING_VALUE, result.getProposedLegalForm());
        assertEquals(updatedGoverningLaw, result.getProposedGoverningLaw());
        assertEquals(TEST_EXISTING_VALUE, result.getProposedRegisterLocation());
        assertEquals(TEST_EXISTING_VALUE, result.getProposedCompanyIdentification());
    }

    @Test
    void testVerifyCompanyIdentificationChangeDifferentRegisterLocationReturnsObject() {
        var updatedRegisterLocation = "New register location";

        var result = overseasEntityChangeUtils.verifyCompanyIdentificationChange(
                TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                updatedRegisterLocation, TEST_EXISTING_VALUE);
        assertNotNull(result);
        assertEquals(CHANGE_COMPANY_IDENTIFICATION, result.getChangeName());
        assertEquals(TEST_EXISTING_VALUE, result.getProposedLegalForm());
        assertEquals(TEST_EXISTING_VALUE, result.getProposedGoverningLaw());
        assertEquals(updatedRegisterLocation, result.getProposedRegisterLocation());
        assertEquals(TEST_EXISTING_VALUE, result.getProposedCompanyIdentification());
    }

    @Test
    void testVerifyCompanyIdentificationChangeDifferentCompanyIdentificationReturnsObject() {
        var updatedCompanyIdentification = "New company identification";

        var result = overseasEntityChangeUtils.verifyCompanyIdentificationChange(
                TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                TEST_EXISTING_VALUE, updatedCompanyIdentification);
        assertNotNull(result);
        assertEquals(CHANGE_COMPANY_IDENTIFICATION, result.getChangeName());
        assertEquals(TEST_EXISTING_VALUE, result.getProposedLegalForm());
        assertEquals(TEST_EXISTING_VALUE, result.getProposedGoverningLaw());
        assertEquals(TEST_EXISTING_VALUE, result.getProposedRegisterLocation());
        assertEquals(updatedCompanyIdentification, result.getProposedCompanyIdentification());
    }

    @Test
    void testVerifyCompanyIdentificationChangeAllSameValueReturnsNull() {
        var result = overseasEntityChangeUtils.verifyCompanyIdentificationChange(
                TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                TEST_EXISTING_VALUE, TEST_EXISTING_VALUE,
                TEST_EXISTING_VALUE, TEST_EXISTING_VALUE);

        assertNull(result);
    }

    @Test
    void testVerifyEntityEmailAddressChangeDifferentValueReturnsObject() {
        var updatedEmailAddress = "new-email@test.com";

        var result = overseasEntityChangeUtils.verifyEntityEmailAddressChange(TEST_EXISTING_VALUE, updatedEmailAddress);
        assertNotNull(result);
        assertEquals(CHANGE_ENTITY_EMAIL_ADDRESS, result.getChangeName());
        assertEquals(updatedEmailAddress, result.getProposedNewEmailAddress());
    }

    @Test
    void testVerifyEntityEmailAddressChangeSameValueReturnsNull() {
        var result = overseasEntityChangeUtils.verifyEntityEmailAddressChange(TEST_EXISTING_VALUE, TEST_EXISTING_VALUE);

        assertNull(result);
    }
}
