package com.bluepantsmedia.ids.utils;

import com.bluepantsmedia.ids.process.Block;
import com.bluepantsmedia.ids.process.Id;
import lombok.NonNull;
import org.apache.commons.lang3.ArrayUtils;

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

    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static final String _26000_26FFF = "https://en.wikibooks.org/wiki/Unicode/Character_reference/26000-26FFF";
    private static final String wikiUnicode = "https://en.wikipedia.org/wiki/List_of_Unicode_characters";
    private static final String reference = "https://en.wikipedia.org/wiki/Unicode";

    private static final String MORPHEME_FORMS = "https://en.wiktionary.org/wiki/Category:English_morpheme_forms";
    private static final String AFFIXES = "https://en.wiktionary.org/wiki/Category:English_affixes";
    private static final String CIRCUMFIXES = "https://en.wiktionary.org/wiki/Category:English_circumfixes";
    private static final String CLITICS = "https://en.wiktionary.org/wiki/Category:English_clitics";
    private static final String INFIXES = "https://en.wiktionary.org/wiki/Category:English_infixes";
    private static final String INTERFIXES = "https://en.wiktionary.org/wiki/Category:English_interfixes";
    private static final String SUFFIXES1 = "https://en.wiktionary.org/wiki/Category:English_suffixes";
    private static final String PREFIXES1 = "https://en.wiktionary.org/wiki/Category:English_prefixes";

    private static final String fullWikitionary = System.getProperty("user.dir") + "\\files\\dictionaries\\wikidictionary\\raw-wiktextract-data.json";
    private static final String categoryWikitionary = System.getProperty("user.dir") + "\\files\\dictionaries\\wikidictionary\\wiktionary-categories.json";

    public static void main(String[] args) {
        final String file = fullWikitionary;
        JsonUtils.printJsonFile(file);

/*
        try {
            Document page = Jsoup.connect(PREFIXES1).get();
            page.select("a").forEach(System.out::println);
            //Elements pElements =  page.select("body");
            //pElements.forEach(el -> System.out.println("section: " + el));
            //page.select("a").forEach(System.out::println);
            //Document page = Jsoup.connect("https://").get();
            //page.select("a").forEach(System.out::println);
            //System.out.println(page);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final List<Id> ids = new ArrayList<>();
        String num = "1234";
        for(int i = 1; i <= 10; i++) {
            StringBuilder sb = new StringBuilder();
            for(int j = 1; j <= i; j++) {
                sb.append(num);
            }
            ids.add(new Id(new BigInteger(sb.toString())));
        }
        byte[] bytes = composeBlockArray(ids);
        List<Id> retrievedIds = buildIdList(bytes);
        retrievedIds.forEach(i -> System.out.println(i.get().toString(10)));
*/
        //doTests();
        //Character
    }

    private static List<Id> buildIdList(byte[] bytes) {
        List<Id> ids = new ArrayList<>();
        List<Byte> id = new ArrayList<>();
        for(byte b: bytes) {
            if(b > 127) {
                // flip MSB
                id.add((byte) (b & 0x80));
                Byte[] bytesArray = id.toArray(new Byte[id.size()]);
                ids.add(new Id(ArrayUtils.toPrimitive(bytesArray)));
                id = new ArrayList<>();
            }
            else {
                id.add(b);
            }
        }
        return ids;
    }

    private static byte[] composeBlockArray(List<Id> ids) {
        List<Byte> byteList = new ArrayList<>();
        for(Id id: ids) {
            byteList.addAll(id.getByteList());
        }
        Byte[] bytes = byteList.toArray(new Byte[byteList.size()]);
        return ArrayUtils.toPrimitive(bytes);
    }

    private static void doTests() {
        Block chunk = new Block(new Byte((byte) 1),true);
        System.out.println(chunk.getBitString());
        chunk = new Block(new Byte((byte) 1),false);
        System.out.println(chunk.getBitString());
        chunk = new Block(new Byte((byte) 127),false);
        System.out.println(chunk.getBitString());
        chunk = new Block(new Byte((byte) 127),true);
        System.out.println(chunk.getBitString());
        StringBuilder bigNumberString = new StringBuilder();
        for(long i = 1; i < 9999L; i++) {
            bigNumberString.append("9");
        }
        BigInteger bi = new BigInteger(bigNumberString.toString());
        final String binaryString = bi.toString(2);
        System.out.println(binaryString);
        //BitSet testHugeBitSet = BitSet.valueOf(bi.toByteArray());
        BitSet testHugeBitSet = fromString(binaryString);
        BigInteger bi2 = new BigInteger(testHugeBitSet.toByteArray());
        System.out.println("BigIntegers are equal: " + bi2.equals(bi));

        Id id1 = new Id(bi2);
        BigInteger id1val = new BigInteger(id1.get().toString());
        System.out.println("BigIntegers are still equal: " + id1.get().equals(id1val));

        Id id2 = new Id(new BigInteger("14"));
        Id id3 = new Id(new BigInteger("144"));

        System.out.println("14: " + id2.get().intValue());
        System.out.println("144: " + id3.get().intValueExact());

        BigInteger testHelix = new BigInteger("4323493");
        String binStr = testHelix.toString(2);
        String decStr = testHelix.toString(10);
        System.out.println(binStr);
        BitSet binBitSet = fromString(binStr);
        System.out.println(binBitSet.toString());
        System.out.println(new BigInteger(binBitSet.toByteArray()).toString(10));
        System.out.println(decStr);

        BitSet bits1 = fromString("1000001");
        BitSet bits2 = fromString("1111111");

        System.out.println(toString(bits1)); // prints 1000001
        System.out.println(toString(bits2)); // prints 1111111

        bits2.and(bits1);

        System.out.println(toString(bits2)); // prints 1000001

        String bin = "101010";
        System.out.println(bin + " as an integer is " + Integer.valueOf(bin, 2));
        int ii = 42;
        System.out.println(ii + " as binary digits (bits) is " + Integer.toBinaryString(ii));

        String longBin = "101001111011011101111010101010111000100111010100101010101010001";
        System.out.println(longBin + " as an long is " + Long.valueOf(longBin, 2));
        long longL =9223372036854775807L;
        System.out.println(longL + " as binary digits (bits) is " + Long.toBinaryString(longL));

        StringBuilder sb = new StringBuilder();
        for(long i = 1; i < 9999L; i++) {
            sb.append("9");
        }
        BigInteger bii = new BigInteger(sb.toString());
        final String binaryString2 = bii.toString(2);
        System.out.println("hugeBinaryString\n" + binaryString2);
        BigInteger backFrom = new BigInteger(binaryString2,2);
        System.out.println("backFrom\n" + backFrom);
        final byte[] testBytes = bi.toByteArray();
        System.out.println("testBytes[].length = " + testBytes.length);
        BitSet testHugeBitSet2 = BitSet.valueOf(testBytes);
        System.out.println("Here we go.....");
        System.out.println(testHugeBitSet2.toString());
        BigInteger bi22 = new BigInteger(testHugeBitSet2.toByteArray());
        System.out.println(bi22);
    }

    private static BitSet fromString(@NonNull final String s) {
        //return BitSet.valueOf(new long[]{Long.parseLong(s, 2)});
        return BitSet.valueOf((new BigInteger(s,2)).toByteArray());
    }

    private static BitSet getLongBitset() {
        StringBuilder s = new StringBuilder();
        for(long i = 1; i < 9223372036854775800l; i++) {
            s.append("1");
        }
        return BitSet.valueOf(new long[]{Long.parseLong(s.toString(), 2)});
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
            System.out.println(i);
            if ((bytes[bytes.length - i / 8 - 1] & (1 << (i % 8))) > 0) {
                System.out.println("Set bits[" + i + "]");
                bits.set(i);
            }
        }
        return bits;
    }
}