package com.bluepantsmedia.dev.ids.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.UUID;

/**
 * Project Identity Streams
 * Author Gary Gerber
 * Email garygerber@bluepantsmedia.com
 * Date 4/10/2020 5:34 AM
 * Copyright 2020 by Bluepants Media, LLC
 */
public class UUIDUtil {

    private static Logger LOG = LoggerFactory.getLogger(UUIDUtil.class);

    /**
     * Predefined UUIDs for name spaces
     */
    private static final String NAMESPACE_DNS = "6ba7b810-9dad-11d1-80b4-00c04fd430c8";
    private static final String NAMESPACE_URL = "6ba7b811-9dad-11d1-80b4-00c04fd430c8";
    private static final String NAMESPACE_OID = "6ba7b812-9dad-11d1-80b4-00c04fd430c8";
    private static final String NAMESPACE_X500 = "6ba7b814-9dad-11d1-80b4-00c04fd430c8";

    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static void main(String[] args) {
        System.out.println("Type 5 : " + createType5UUID(NAMESPACE_URL, "bluepantsmedia.com"));
        System.out.println("Unique key  : " + generateKey());
    }

    /**
     * Unique Keys Generation Using Message Digest and Type 4 UUID
     * 64 hexidecimal chars (32 hexidecimal pairs) = 256 bits
     * @return bytes to hex string
     *
     */
    public static String generateKey() {
        try {
            MessageDigest salt = MessageDigest.getInstance("SHA-256");
            salt.update(UUID.randomUUID().toString().getBytes("UTF-8"));
            return BitSetUtil.bytesToHex(salt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * UUIDv5 Generator
     * 32 hexidecimal chars (0x00000000-0x0000-0x0000-0x0000-0x000000000000) = 128 bits
     * @param namespace
     * @param name
     * @return type 5 UUID
     *
     */
    public static UUID createType5UUID(String namespace, String name) {
        try {
            String source = namespace + name;
            byte[] bytes = source.getBytes("UTF-8");
            return getUUIDFromBytes(bytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static UUID getUUIDFromBytes(byte[] name) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new InternalError("SHA-1 not supported", e);
        }
        byte[] bytes = Arrays.copyOfRange(md.digest(name), 0, 16);
        bytes[6] &= 0x0f; /* clear version        */
        bytes[6] |= 0x50; /* set to version 5     */
        bytes[8] &= 0x3f; /* clear variant        */
        bytes[8] |= 0x80; /* set to IETF variant  */
        return getType5UUIDFromByteArray(bytes);
    }

    private static UUID getType5UUIDFromByteArray(byte[] data) {
        long msb = 0;
        long lsb = 0;
        assert data.length == 16 : "data must be 16 bytes in length";

        for (int i = 0; i < 8; i++)
            msb = (msb << 8) | (data[i] & 0xff);

        for (int i = 8; i < 16; i++)
            lsb = (lsb << 8) | (data[i] & 0xff);
        return new UUID(msb, lsb);
    }


}
