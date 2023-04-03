package org.bluepantsmedia.test;

import java.nio.charset.StandardCharsets;

/**
 * Project IDS - Identity Streams
 * Author Gary Gerber
 * Email garygerber@bluepantsmedia.com
 * Date 1/16/2022 9:32 PM
 * Copyright 2020 by Bluepants Media, LLC
 */
public class TestByteOutputValues {
    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);

    public static void main(String[] args) {
        for (int i = 0; i < 256; i++) {
            System.out.println("{ \"\",      new Byte((byte) 0x" + String.format("%02x", (byte) i) + ") },");
        }
    }
}
