package com.bluepantsmedia.dev.ids.utils;

/**
 * Project Identity Streams
 * Author Gary Gerber
 * Email garygerber@bluepantsmedia.com
 * Date 4/7/2020 5:42 PM
 * Copyright 2020 by Bluepants Media, LLC
 * From comments by phatfingers and CookieNinja on stackoverflow
 * https://stackoverflow.com/questions/15736626/java-how-to-create-and-manipulate-a-bit-array-with-length-of-10-million-bits
 */

public class BitArray {

    private static final int MASK = 63;
    private final long len;
    private long bits[] = null;

    public BitArray(long size) {
        if ((((size-1)>>6) + 1) > 2147483647) {
            throw new IllegalArgumentException(
                    "Field size to large, max size = 137438953408");
        }else if (size < 1) {
            throw new IllegalArgumentException(
                    "Field size to small, min size = 1");
        }
        len = size;
        bits = new long[(int) (((size-1)>>6) + 1)];
    }

    public boolean getBit(long pos) {
        return (bits[(int)(pos>>6)] & (1L << (pos&MASK))) != 0;
    }

    public void setBit(long pos, boolean b) {
        if (getBit(pos) != b) { bits[(int)(pos>>6)] ^= (1L << (pos&MASK)); }
    }

    public long getLength() {
        return len;
    }
}