package org.bluepantsmedia.test;

/**
 * Project IDS - Identity Streams
 * Author Gary Gerber
 * Email garygerber@bluepantsmedia.com
 * Date 3/12/2022 11:47 AM
 * Copyright 2020 by Bluepants Media, LLC
 */

import java.math.BigInteger;

public class TestBigInteger {

    public static void main(String[] args) {

        // create 2 BigInteger objects
        BigInteger bi1, bi2;

        // create 2 byte arrays
        byte b1[], b2[];

        // create and assign value to byte array b3
        byte b3[] = {0x1, 0x00, 0x00};

        bi1 = new BigInteger("16");
        bi2 = new BigInteger(b3); // using byte[] constructor of BigInteger

        // assign byte array representation of bi1, bi2 to b1, b2
        b1 = bi1.toByteArray();
        b2 = bi2.toByteArray();

        String str1 = "Byte array representation of " + bi1 + " is: ";

        System.out.println(str1);

        // print byte array b1 using for loop
        for (int i = 0; i < b1.length; i++) {
            System.out.format("0x%02X\n", b1[i]);
        }

        String str2 = "Byte array representation of " + bi2 + " is: ";

        System.out.println(str2);

        // print byte array b2 using for loop
        for (int j = 0; j < b2.length; j++) {
            System.out.format("0x%02X ", b2[j]);
        }
    }
}
