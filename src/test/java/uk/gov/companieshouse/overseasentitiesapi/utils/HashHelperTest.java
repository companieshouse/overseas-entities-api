package uk.gov.companieshouse.overseasentitiesapi.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.Test;

class HashHelperTest {

    @Test
    void hashTest() throws NoSuchAlgorithmException {
        HashHelper hashHelper = new HashHelper();
        hashHelper.setSalt("foryoureyesonly");
        String hashed = hashHelper.encode("testing123");
        assertEquals("T-wBcLrkMcrUIMEpL6w_96pjUdw", hashed);
    }
}
