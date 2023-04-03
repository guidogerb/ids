package org.bluepantsmedia.test;


/**
 * Project Identity Streams
 * Author Gary Gerber
 * Email garygerber@bluepantsmedia.com
 * Date 4/8/2020 10:05 AM
 * Copyright 2020 by Bluepants Media, LLC
 * <p>
 * Bit length smaller than a byte (7 or less) indicates first 64 idsChar
 * (identity order is determined by how frequently a basic lingual character and punctuation is used)
 * and which case.
 * Case becomes a state that characters case and an alternate punctuation have in the idsChar charset
 * (when leading bit is set)
 * leading bit = case, trailing identity of idsChar
 * i.e. 11 (base 2) = capital 'A' and 01(base 2) = 'a' - assuming 'a' is idsChar 1 (base 2)
 * 1001 (base 2) = '.' and 0001 (base 2) = '!'
 * 101 (base 2) = 'G' and 001 (base 2) = 'g'
 */
public class TestCharacterValues {
    public static final String convertCharacterFromNumber(short in) {
        return Character.toString((char) in);
    }

    public static final void main(String[] args) {
        // 2^5 (5 bits) = 32 chars with leading one bit switch = 6 bits
        // [a, A], [b,B],[c,C]...[z,Z]
        short a = 97;
        short z = 122;
        // [.?],[],[],[]

        System.out.println(convertCharacterFromNumber(a) + " = " + a);
        System.out.println(convertCharacterFromNumber(z) + " = " + z);
    }
}
