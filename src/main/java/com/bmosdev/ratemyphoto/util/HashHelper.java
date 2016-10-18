package com.bmosdev.ratemyphoto.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.bmosdev.ratemyphoto.util.StringUtil.byteArrayToStringRepresentation;
import static com.bmosdev.ratemyphoto.util.StringUtil.getLatin1Bytes;

public class HashHelper {

    public static String calculateMd5(String string) {
        return DigestUtils.md5Hex(string);
    }

    public static String calculateSha256(String string) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] bytes = string.getBytes();
            byte[] digest = sha256.digest(bytes);
            return String.valueOf(Hex.encodeHex(digest));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String calculateHmacSha1(String input, String macKey) {
        try {
            byte[] macKeyBytes = getLatin1Bytes(macKey);
            Key hmacKey = new SecretKeySpec(macKeyBytes, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(hmacKey);
            final byte[] inputBytes = getLatin1Bytes(input);
            final byte[] hash = mac.doFinal(inputBytes);
            return byteArrayToStringRepresentation(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
