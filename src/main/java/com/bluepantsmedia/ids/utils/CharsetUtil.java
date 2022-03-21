package com.bluepantsmedia.ids.utils;

import com.bluepantsmedia.ids.enums.CharsetNames;
import com.bluepantsmedia.ids.charset.CharsetProviderIDS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.charset.spi.CharsetProvider;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Project Identity Streams
 * Author Gary Gerber
 * Email garygerber@bluepantsmedia.com
 * Date 4/10/2020 7:14 AM
 * Copyright 2020 by Bluepants Media, LLC
 */
public class CharsetUtil {

    private static Logger LOG = LoggerFactory.getLogger(CharsetUtil.class);

    public static final void main(String[] args) {
        transcodeEbcdicTest();
        System.setProperty("ibm.swapLF", "true");
        transcodeEbcdicTest();
        System.setProperty("ibm.swapLF", "false");
        LOG.info(String.valueOf(Charset.defaultCharset()));
        LOG.info("10111011 01111001 = " + convertToBinary("語", "Big5"));
    }

    public static void transcodeEbcdicTest() {
        byte EBCDIC_NL = 0x15; //next line
        byte EBCDIC_LF = 0x25; //line feed
        byte EBCDIC_CR = 0x0D; //carriage return

        LOG.info(ebcdicToUtf16(EBCDIC_NL));
        LOG.info(ebcdicToUtf16(EBCDIC_LF));
        LOG.info(ebcdicToUtf16(EBCDIC_CR));

        LOG.info(utf16ToEbcdic("\u0085")); //next line
        LOG.info(utf16ToEbcdic("\n")); //line feed
        LOG.info(utf16ToEbcdic("\r")); //carriage return
    }

    public static String ebcdicToUtf16(byte... b) {
        String utf16 = new String(b, Charset.forName("IBM500"));
        return String.format("%02x -> %04x%n", b[0] & 0xFF, utf16.charAt(0) & 0xFFFF);
    }

    public static String utf16ToEbcdic(String s) {
        byte[] b = s.getBytes(Charset.forName("IBM500"));
        return String.format("%04x -> %02x%n", s.charAt(0) & 0xFFFF, b[0] & 0xFF);
    }

    public static byte[] getByteArrayFromString(String input, CharsetNames charsetNames) {
        final Charset charset =  getCharset(charsetNames);
        return input.getBytes(charset);
    }

    public static Charset getCharset(CharsetNames charsetNames)  {
        Charset charset = null;
        try {
            charset = Charset.forName(charsetNames.label);
        } catch (Exception ex) {
            for (CharsetProvider charsetProvider : ServiceLoader.load(CharsetProviderIDS.class)) {
                charset  = charsetProvider.charsetForName(charsetNames.label);
                if (charset != null) {
                    break;
                }
            }
            if (charset == null) {
                CharsetProviderIDS providerIDS = new CharsetProviderIDS();
                charset = providerIDS.charsetForName(charsetNames.label);
            }
        }
        if (charset == null) {
            throw new UnsupportedCharsetException("Unknown charset : " + charsetNames.label);
        }
        return charset;
    }

    public static String decodeText(String input, String encoding) {
        try {
            return
                    new BufferedReader(
                            new InputStreamReader(
                                    new ByteArrayInputStream(input.getBytes()),
                                    Charset.forName(encoding)))
                            .readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String convertToBinary(String input, String encoding) {
        byte[] encoded_input = Charset.forName(encoding)
                .encode(input)
                .array();
        return IntStream.range(0, encoded_input.length)
                .map(i -> encoded_input[i])
                .mapToObj(e -> Integer.toBinaryString(e ^ 255))
                .map(e -> String.format("%1$" + Byte.SIZE + "s", e).replace(" ", "0"))
                .collect(Collectors.joining(" "));
    }

}
