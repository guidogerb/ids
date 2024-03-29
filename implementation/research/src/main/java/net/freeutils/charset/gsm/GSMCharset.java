/*
 *  Copyright © 2005-2019 Amichai Rothman
 *
 *  This file is part of JCharset - the Java Charset package.
 *
 *  JCharset is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  JCharset is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with JCharset.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  For additional info see http://www.freeutils.net/source/jcharset/
 */

package net.freeutils.charset.gsm;

import static net.freeutils.charset.ByteLookupCharset.createInverseLookupTable;
import static net.freeutils.charset.ByteLookupCharset.mutate;
import net.freeutils.charset.EscapedByteLookupCharset;

/**
 * The <b>GSMCharset</b> class handles the encoding and decoding of the
 * GSM default encoding charset.
 * <p>
 * The encoding and decoding are based on the mapping at
 * http://www.unicode.org/Public/MAPPINGS/ETSI/GSM0338.TXT
 *
 * Author Amichai Rothman
 * @since  2005-05-26
 */
public class GSMCharset extends EscapedByteLookupCharset {

    static final byte ESCAPE = 0x1B;

    static final int[] BYTE_TO_CHAR_SMALL_C_CEDILLA = {
        0x0040, 0x00A3, 0x0024, 0x00A5, 0x00E8, 0x00E9, 0x00F9, 0x00EC,
        0x00F2, 0x00E7, 0x000A, 0x00D8, 0x00F8, 0x000D, 0x00C5, 0x00E5,
        0x0394, 0x005F, 0x03A6, 0x0393, 0x039B, 0x03A9, 0x03A0, 0x03A8,
        0x03A3, 0x0398, 0x039E,     -1, 0x00C6, 0x00E6, 0x00DF, 0x00C9,
        0x0020, 0x0021, 0x0022, 0x0023, 0x00A4, 0x0025, 0x0026, 0x0027,
        0x0028, 0x0029, 0x002A, 0x002B, 0x002C, 0x002D, 0x002E, 0x002F,
        0x0030, 0x0031, 0x0032, 0x0033, 0x0034, 0x0035, 0x0036, 0x0037,
        0x0038, 0x0039, 0x003A, 0x003B, 0x003C, 0x003D, 0x003E, 0x003F,
        0x00A1, 0x0041, 0x0042, 0x0043, 0x0044, 0x0045, 0x0046, 0x0047,
        0x0048, 0x0049, 0x004A, 0x004B, 0x004C, 0x004D, 0x004E, 0x004F,
        0x0050, 0x0051, 0x0052, 0x0053, 0x0054, 0x0055, 0x0056, 0x0057,
        0x0058, 0x0059, 0x005A, 0x00C4, 0x00D6, 0x00D1, 0x00DC, 0x00A7,
        0x00BF, 0x0061, 0x0062, 0x0063, 0x0064, 0x0065, 0x0066, 0x0067,
        0x0068, 0x0069, 0x006A, 0x006B, 0x006C, 0x006D, 0x006E, 0x006F,
        0x0070, 0x0071, 0x0072, 0x0073, 0x0074, 0x0075, 0x0076, 0x0077,
        0x0078, 0x0079, 0x007A, 0x00E4, 0x00F6, 0x00F1, 0x00FC, 0x00E0,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
    };

    static final int[] BYTE_TO_CHAR_ESCAPED_DEFAULT = {
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1, 0x000C,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1, 0x005E,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
        0x007B, 0x007D,     -1,     -1,     -1,     -1,     -1, 0x005C,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1, 0x005B, 0x007E, 0x005D,     -1,
        0x007C,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     0x20AC, -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
            -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
    };

    static final int[][] CHAR_TO_BYTE_SMALL_C_CEDILLA =
        createInverseLookupTable(BYTE_TO_CHAR_SMALL_C_CEDILLA);

    static final int[][] CHAR_TO_BYTE_ESCAPED_DEFAULT =
        createInverseLookupTable(BYTE_TO_CHAR_ESCAPED_DEFAULT);

    static final int[]   BYTE_TO_CHAR_CAPITAL_C_CEDILLA =
        mutate(BYTE_TO_CHAR_SMALL_C_CEDILLA, new int[] { 9 }, new int[] { 0x00C7 });

    static final int[][] CHAR_TO_BYTE_CAPITAL_C_CEDILLA =
        createInverseLookupTable(BYTE_TO_CHAR_CAPITAL_C_CEDILLA);

    /**
     * Initializes a new charset with the given canonical name and alias
     * set, and byte-to-char/char-to-byte lookup tables.
     *
     * @param canonicalName the canonical name of this charset
     * @param aliases an array of this charset's aliases, or null if it has no aliases
     * @param byteToChar a byte-to-char conversion table for this charset
     * @param byteToCharEscaped a byte-to-char conversion table for this charset
     *        for the escaped characters
     * @param charToByte a char-to-byte conversion table for this charset. It can
     *        be generated on-the-fly by calling createInverseLookupTable(byteToChar).
     * @param charToByteEscaped a char-to-byte conversion table for this charset
     *        for the escaped characters
     * @throws java.nio.charset.IllegalCharsetNameException
     *         if the canonical name or any of the aliases are illegal
     */
    protected GSMCharset(String canonicalName, String[] aliases,
        int[] byteToChar, int[] byteToCharEscaped,
        int[][] charToByte, int[][] charToByteEscaped) {
        super(canonicalName, aliases, ESCAPE,
            byteToChar, byteToCharEscaped, charToByte, charToByteEscaped);
    }

}
