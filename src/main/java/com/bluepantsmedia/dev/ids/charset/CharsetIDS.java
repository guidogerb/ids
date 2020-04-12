package com.bluepantsmedia.dev.ids.charset;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.IllegalCharsetNameException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Project Identity Streams
 * Author Gary Gerber
 * Email garygerber@bluepantsmedia.com
 * Date 4/10/2020 4:43 AM
 * Copyright 2020 by Bluepants Media, LLC
 */
public class CharsetIDS extends Charset {

    public static final String LABEL = "IDS-32";

    // HashMap's used for encoding and decoding
    protected static Map<String, Char> defaultEncodeMap = new HashMap<String, Char>();
    protected static Map<Char, String> defaultDecodeMap = new HashMap<Char, String>();
    protected static Map<String, BitSet> extEncodeMap = new HashMap<String, BitSet>();
    protected static Map<BitSet, String> extDecodeMap = new HashMap<BitSet, String>();

    // Data to populate the hashmaps with
    private static final Object[][] gsmCharacters = {
            { "\u03A9", new Byte((byte) 0x15) },
            { "\u03A0", new Byte((byte) 0x16) },
            { "+",      new Byte((byte) 0x2b) },
            { ",",      new Byte((byte) 0x2c) },
            { "-",      new Byte((byte) 0x2d) },
            { ".",      new Byte((byte) 0x2e) },
            { "/",      new Byte((byte) 0x2f) },
            { ":",      new Byte((byte) 0x3a) },
            { ";",      new Byte((byte) 0x3b) },
            { "<",      new Byte((byte) 0x3c) },
            { "=",      new Byte((byte) 0x3d) },
            { ">",      new Byte((byte) 0x3e) },
            { "a",      new Byte((byte) 0x61) },
            { "b",      new Byte((byte) 0x62) },
            { "c",      new Byte((byte) 0x63) },
            { "d",      new Byte((byte) 0x64) },
            { "e",      new Byte((byte) 0x65) },
            { "f",      new Byte((byte) 0x66) },
            { "g",      new Byte((byte) 0x67) },
            { "h",      new Byte((byte) 0x68) },
            { "i",      new Byte((byte) 0x69) },
            { "j",      new Byte((byte) 0x6a) },
            { "k",      new Byte((byte) 0x6b) },
            { "l",      new Byte((byte) 0x6c) },
            { "m",      new Byte((byte) 0x6d) },
            { "n",      new Byte((byte) 0x6e) },
            { "o",      new Byte((byte) 0x6f) },
            { "p",      new Byte((byte) 0x70) },
            { "q",      new Byte((byte) 0x71) },
            { "r",      new Byte((byte) 0x72) },
            { "s",      new Byte((byte) 0x73) },
            { "t",      new Byte((byte) 0x74) },
            { "u",      new Byte((byte) 0x75) },
            { "v",      new Byte((byte) 0x76) },
            { "w",      new Byte((byte) 0x77) },
            { "x",      new Byte((byte) 0x78) },
            { "y",      new Byte((byte) 0x79) },
            { "z",      new Byte((byte) 0x7a) },
            { "ä",      new Byte((byte) 0x7b) },
            { "ö",      new Byte((byte) 0x7c) },
            { "ñ",      new Byte((byte) 0x7d) },
            { "ü",      new Byte((byte) 0x7e) },
            { "à",      new Byte((byte) 0x7f) }
    };

    /*
        // UUIDv5: UUID = hash(NAMESPACE_IDENTIFIER + NAME)
        MessageDigest salt = MessageDigest.getInstance("SHA-256");
        salt.update(UUID.randomUUID().toString().getBytes("UTF-8"));
        String digest = bytesToHex(salt.digest());
    */

    private static final Object[][] gsmExtensionCharacters = {
            { "00000000-0000-0000-0000-000000000000", new BitSet(160) },
            { "87f859f9-beca-5e06-83e1-801cd8f4e090",  new BitSet(160) }
    };


    /**
     * Initializes a new charset with the given canonical name and alias
     * set.
     *
     * @param canonicalName The canonical name of this charset
     * @param aliases       An array of this charset's aliases, or null if it has no aliases
     * @throws IllegalCharsetNameException If the canonical name or any of the aliases are illegal
     */
    protected CharsetIDS(String canonicalName, String[] aliases) {
        super(canonicalName, aliases);
    }

    @Override
    public boolean contains(Charset cs) {
        return false;
    }

    @Override
    public CharsetDecoder newDecoder() {
        return null;
    }

    @Override
    public CharsetEncoder newEncoder() {
        return null;
    }
}
