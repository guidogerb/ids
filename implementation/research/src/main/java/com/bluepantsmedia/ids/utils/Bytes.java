package com.bluepantsmedia.ids.utils;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Project IDS - Identity Streams
 * Author Gary Gerber
 * Email garygerber@bluepantsmedia.com
 * Date 3/12/2022 12:03 PM
 * Copyright 2020 by Bluepants Media, LLC
 *
 * Utility class to support byte[] entity identities
 */
public final class Bytes implements Serializable {
    private final byte[] _bytes;
    public Bytes(byte[] bytes) {
        _bytes=bytes;
    }
    public boolean equals(Object other) {
        if (other==null || !(other instanceof Bytes) ) return false;
        return Arrays.equals( ( (Bytes) other )._bytes, _bytes );
    }
    public byte[] getBytes() {
        byte[] clone = new byte[_bytes.length];
        System.arraycopy(_bytes, 0, clone, 0, _bytes.length);
        return clone;
    }
    public String toString() {
        //well worth implementing.....
        return "todo";
    }
}
