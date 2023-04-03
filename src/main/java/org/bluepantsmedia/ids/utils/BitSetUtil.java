package org.bluepantsmedia.ids.utils;

import lombok.NonNull;
import org.apache.commons.lang3.ArrayUtils;
import org.bluepantsmedia.ids.process.Id;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Project Identity Streams
 * Author Gary Gerber
 * Email garygerber@bluepantsmedia.com
 * Date 4/9/2020 6:17 AM
 * Copyright 2020 by Bluepants Media, LLC
 */
public class BitSetUtil {

    public static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    private BitSetUtil() {
    }

    public static List<Id> buildIdList(byte[] bytes) {
        List<Id> ids = new ArrayList<>();
        List<Byte> id = new ArrayList<>();
        for (byte b : bytes) {
            if (b > 127) {
                // flip MSB
                id.add((byte) (b & 0x80));
                Byte[] bytesArray = id.toArray(new Byte[id.size()]);
                ids.add(new Id(ArrayUtils.toPrimitive(bytesArray)));
                id = new ArrayList<>();
            } else {
                id.add(b);
            }
        }
        Byte[] bytesArray = id.toArray(new Byte[id.size()]);
        ids.add(new Id(ArrayUtils.toPrimitive(bytesArray)));
        return ids;
    }

    public static byte[] composeBlockArray(List<Id> ids) {
        List<Byte> byteList = new ArrayList<>();
        for (Id id : ids) {
            byteList.addAll(id.getByteList());
        }
        Byte[] bytes = byteList.toArray(new Byte[byteList.size()]);
        return ArrayUtils.toPrimitive(bytes);
    }

    public static BitSet fromString(@NonNull final String s) {
        //return BitSet.valueOf(new long[]{Long.parseLong(s, 2)});
        return BitSet.valueOf((new BigInteger(s, 2)).toByteArray());
    }

    public static BitSet getLongBitset() {
        StringBuilder s = new StringBuilder();
        for (long i = 1; i < 9223372036854775800l; i++) {
            s.append("1");
        }
        return BitSet.valueOf(new long[]{Long.parseLong(s.toString(), 2)});
    }

    public static String toString(@NonNull BitSet bs) {
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
            System.out.println(i);
            if ((bytes[bytes.length - i / 8 - 1] & (1 << (i % 8))) > 0) {
                System.out.println("Set bits[" + i + "]");
                bits.set(i);
            }
        }
        return bits;
    }
}