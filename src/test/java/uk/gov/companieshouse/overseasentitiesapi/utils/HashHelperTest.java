package uk.gov.companieshouse.overseasentitiesapi.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.Test;

class HashHelperTest {

    private HashHelper hashHelper = new HashHelper("foryoureyesonly");

    @Test
    void hashTest() throws NoSuchAlgorithmException {
        String hashed = hashHelper.encode("testing123");
        assertEquals("T-wBcLrkMcrUIMEpL6w_96pjUdw", hashed);
    }

    @Test
    void testStripEquals() {
        String str = hashHelper.stripEquals("==abc=123=");
        assertEquals("abc123", str);
    }
}
