package uk.gov.companieshouse.overseasentitiesapi.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class HashHelper {

    private String salt;

    public HashHelper(String salt) {
        this.salt = salt;
    }

    public String encode(String plain) throws NoSuchAlgorithmException {
        return base64Encode(sha1Digest(plain)).replace("=", "");
    }

    private String base64Encode(final byte[] bytes) {
        return Base64.getUrlEncoder().encodeToString(bytes);
    }

    private byte[] sha1Digest(final String plain) throws NoSuchAlgorithmException {
        return MessageDigest.getInstance("SHA-1").digest((plain + salt).getBytes(
                StandardCharsets.UTF_8));
    }
}
