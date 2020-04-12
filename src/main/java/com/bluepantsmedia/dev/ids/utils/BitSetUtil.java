package com.bluepantsmedia.dev.ids.utils;

import lombok.NonNull;

import java.util.BitSet;

/**
 * Project Identity Streams
 * Author Gary Gerber
 * Email garygerber@bluepantsmedia.com
 * Date 4/9/2020 6:17 AM
 * Copyright 2020 by Bluepants Media, LLC
 */
public class BitSetUtil {

    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static void main(String[] args) {
        BitSet bits1 = fromString("1000001");
        BitSet bits2 = fromString("1111111");

        System.out.println(toString(bits1)); // prints 1000001
        System.out.println(toString(bits2)); // prints 1111111

        bits2.and(bits1);

        System.out.println(toString(bits2)); // prints 1000001

        String bin = "101010";
        System.out.println(bin + " as an integer is " + Integer.valueOf(bin, 2));
        int i = 42;
        System.out.println(i + " as binary digits (bits) is " + Integer.toBinaryString(i));

        String longBin = "101001111011011101111010101010111000100111010100101010101010001";
        System.out.println(longBin + " as an long is " + Long.valueOf(longBin, 2));
        long longL =9223372036854775807L;
        System.out.println(longL + " as binary digits (bits) is " + Long.toBinaryString(longL));

    }

    private static BitSet fromString(@NonNull final String s) {
        return BitSet.valueOf(new long[]{Long.parseLong(s, 2)});
    }

    private static String toString(@NonNull BitSet bs) {
        return Long.toString(bs.toLongArray()[0], 2);
    }

    public static String bytesToHex(@NonNull byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String bitSetToHex(@NonNull BitSet bitSet) {
        return bytesToHex(bitSet.toByteArray());
    }

    // Returns a bitset containing the values in bytes.
    public static BitSet fromByteArray(@NonNull byte[] bytes) {
        BitSet bits = new BitSet();
        for (int i = 0; i < bytes.length * 8; i++) {
            if ((bytes[bytes.length - i / 8 - 1] & (1 << (i % 8))) > 0) {
                bits.set(i);
            }
        }
        return bits;
    }
}