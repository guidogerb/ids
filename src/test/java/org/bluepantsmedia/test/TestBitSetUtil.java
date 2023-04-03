package org.bluepantsmedia.test;

import lombok.extern.slf4j.Slf4j;
import org.bluepantsmedia.ids.process.Block;
import org.bluepantsmedia.ids.process.Id;
import org.bluepantsmedia.ids.utils.BitSetUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Project ids
 * Author Gary Gerber
 * Email garygerber@bluepantsmedia.com
 * Date 3/30/2022 1:31 PM
 * Copyright 2020 by Bluepants Media, LLC
 */
@Slf4j
public class TestBitSetUtil {

    final static BigInteger b1 = new BigInteger("1");
    final static BigInteger b2 = new BigInteger("222222222");
    final static BigInteger b3 = new BigInteger("333333333333333");
    final static BigInteger b4 = new BigInteger("444444444444444444444");
    final static BigInteger b5 = new BigInteger("555555555555555555555555555555");
    final static BigInteger b6 = new BigInteger("66666666666666666666666666666666666666666");
    final static BigInteger b7 = new BigInteger("7777777777777777777777777777777777777777777777777777");
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
    private static final String NYMS = "http://www.fun-with-words.com/nym_words.html";
    private static final String ALEX_PREDICTIONS = "https://banned.video/channel/alex-jones-predictions";
    private static final String ACCEPT = "application/signed-exchange;v=b3;q=0.7,*/*;q=0.8";
    private static final String ENCODING = "gzip, deflate, br";
    private static final String LANGUAGE = "en-US,en;q=0.9";
    //private static final String CONNECTION = "";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.84 Safari/537.36";
    private static final String IF_NONE_MATCH = "af20-r+FOBGdrNwsWQ1eXAN8skBMgPPM";
    private static final String REFERER = "https://banned.video/channel/alex-jones-predictions";
    private static final String COOKIE = "_ga=GA1.2.2041808645.1643049934; _gid=GA1.2.1792403784.1648761433; __cf_bm=6vCC7hP6tb3ym8GlV_nIhshnuJcE3Kae_YDjjZN3rHI-1648762341-0-AcW08sgXQFwaLSTKbIoYsLvrHi2q8HHbqREgcMRCGO9gQ6+y8AORqQfXmG8EUTj0brcKhvPGiKaaBuLMujout/8u0QMDKWbij8/OSFe1VSomGDWJMoLNn2oEuRu869jlymo4Yjn6l7kxMvOq2t2Hnuw1k72eY0JZgp0Krdmt4USj";
    private static final String fullWikitionary = System.getProperty("user.dir") + "\\largeFiles\\dictionaries\\wiktionary\\raw-wiktextract-data.json";
    private static final String categoryWikitionary = System.getProperty("user.dir") + "\\largeFiles\\dictionaries\\wiktionary\\wiktionary-categories.json";

    public static void main(String[] args) {
        //getHTML5();
        testIDs();
        //doTests();
        //Character
    }

    private static void testIDs() {
        final List<Id> ids = new ArrayList<>();
        ids.add(new Id(b1));
        ids.add(new Id(b2));
        ids.add(new Id(b3));
        ids.add(new Id(b4));
        ids.add(new Id(b5));
        ids.add(new Id(b6));
        ids.add(new Id(b7));
        byte[] bytes = BitSetUtil.composeBlockArray(ids);
        List<Id> retrievedIds = BitSetUtil.buildIdList(bytes);
        if (retrievedIds.size() != 7) {
            log.error("retrievedIds should be 7 but found " + retrievedIds.size());
        }
        if (retrievedIds.get(0).get() != b1) {
            System.out.print("b1 found");
        }
        System.out.println("done.");
    }

    private static void doTests() {
        Block chunk = new Block(new Byte((byte) 1), true);
        System.out.println(chunk.getBitString());
        chunk = new Block(new Byte((byte) 1), false);
        System.out.println(chunk.getBitString());
        chunk = new Block(new Byte((byte) 127), false);
        System.out.println(chunk.getBitString());
        chunk = new Block(new Byte((byte) 127), true);
        System.out.println(chunk.getBitString());
        StringBuilder bigNumberString = new StringBuilder();
        for (long i = 1; i < 9999L; i++) {
            bigNumberString.append("9");
        }
        BigInteger bi = new BigInteger(bigNumberString.toString());
        final String binaryString = bi.toString(2);
        System.out.println(binaryString);
        //BitSet testHugeBitSet = BitSet.valueOf(bi.toByteArray());
        BitSet testHugeBitSet = BitSetUtil.fromString(binaryString);
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
        BitSet binBitSet = BitSetUtil.fromString(binStr);
        System.out.println(binBitSet.toString());
        System.out.println(new BigInteger(binBitSet.toByteArray()).toString(10));
        System.out.println(decStr);

        BitSet bits1 = BitSetUtil.fromString("1000001");
        BitSet bits2 = BitSetUtil.fromString("1111111");

        System.out.println(BitSetUtil.toString(bits1)); // prints 1000001
        System.out.println(BitSetUtil.toString(bits2)); // prints 1111111

        bits2.and(bits1);

        System.out.println(BitSetUtil.toString(bits2)); // prints 1000001

        String bin = "101010";
        System.out.println(bin + " as an integer is " + Integer.valueOf(bin, 2));
        int ii = 42;
        System.out.println(ii + " as binary digits (bits) is " + Integer.toBinaryString(ii));

        String longBin = "101001111011011101111010101010111000100111010100101010101010001";
        System.out.println(longBin + " as an long is " + Long.valueOf(longBin, 2));
        long longL = 9223372036854775807L;
        System.out.println(longL + " as binary digits (bits) is " + Long.toBinaryString(longL));

        StringBuilder sb = new StringBuilder();
        for (long i = 1; i < 9999L; i++) {
            sb.append("9");
        }
        BigInteger bii = new BigInteger(sb.toString());
        final String binaryString2 = bii.toString(2);
        System.out.println("hugeBinaryString\n" + binaryString2);
        BigInteger backFrom = new BigInteger(binaryString2, 2);
        System.out.println("backFrom\n" + backFrom);
        final byte[] testBytes = bi.toByteArray();
        System.out.println("testBytes[].length = " + testBytes.length);
        BitSet testHugeBitSet2 = BitSet.valueOf(testBytes);
        System.out.println("Here we go.....");
        System.out.println(testHugeBitSet2.toString());
        BigInteger bi22 = new BigInteger(testHugeBitSet2.toByteArray());
        System.out.println(bi22);
    }

    private static void getHTML5() {
        try {
            Document document = Jsoup.connect(ALEX_PREDICTIONS)
                    .header("Accept", ACCEPT)
                    .header("Accept-Encoding", ENCODING)
                    .header("Accept-Language", LANGUAGE)
                    //.header("Connection", CONNECTION)
                    .header("User-Agent", USER_AGENT)
                    .header("if-none-match", IF_NONE_MATCH)
                    .header("Referer", REFERER)
                    .header("cookie", COOKIE)
                    .get();
            Document page = Jsoup.connect(ALEX_PREDICTIONS).get();
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
    }

}
