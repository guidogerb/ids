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

package net.freeutils.charset;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.util.Arrays;

/**
 * The <b>ByteLookupCharset</b> class handles the encoding and decoding of
 * single-byte charsets where the byte-to-char conversion is performed
 * using a simple lookup table.
 *
 * Author Amichai Rothman
 * @since  2005-06-30
 */
public abstract class ByteLookupCharset extends Charset {

    final int[] byteToChar;
    final int[][] charToByte;

    /**
     * Creates a new lookup table with 256 elements, all initialized to -1.
     *
     * @return the new table
     */
    public static int[] createTable() {
        int[] table = new int[256];
        Arrays.fill(table, -1);
        return table;
    }

    /**
     * Returns a copy of the given array in which several items
     * are modified.
     *
     * @param src the array to mutate
     * @param indices the array of indices at which the values will be modified
     * @param values the respective values to place in these indices
     * @return the mutated array
     */
    public static int[] mutate(int[] src, int[] indices, int[] values) {
        int[] mutated = new int[src.length];
        System.arraycopy(src, 0, mutated, 0, src.length);
        for (int i = 0; i < indices.length; i++)
            mutated[indices[i]] = values[i];
        return mutated;
    }

    /**
     * Creates an inverse lookup table for the given byte-to-char lookup table.
     * <p>
     * The returned table contains 256 tables, one per high-order byte of a
     * potential character to be converted (unused ones are null), and each
     * such table can be indexed using the character's low-order byte, to
     * obtain the actual converted byte value.
     * A null table in the top level table, or a -1 within a lower level table,
     * both indicate that there is no legal mapping for the given character.
     *
     * @param chars a lookup table which holds the character value
     *        that each byte value (0-255) is converted to
     * @return the created inverse lookup (char-to-byte) table
     */
    public static int[][] createInverseLookupTable(int[] chars) {
        int[][] tables = new int[256][];
        for (int i = 0; i < 256; i++) {
            int c = chars[i];
            if (c > -1)
                updateInverseLookupTable(tables, c, i);
        }
        return tables;
    }

    /**
     * Updates an inverse lookup table with an additional mapping,
     * replacing a previous mapping of the same value if it exists.
     *
     * @param tables the inverse lookup table to update
     *        (see {@link #createInverseLookupTable})
     * @param c the character to map
     * @param b the byte value to which c is mapped, or -1 to mark an illegal mapping
     * @return the updated inverse lookup (char-to-byte) table
     */
    public static int[][] updateInverseLookupTable(int[][] tables, int c, int b) {
        int high = (c >>> 8) & 0xFF;
        int low = c & 0xFF;
        int[] table = tables[high];
        if (table != null) {
            table[low] = b;
        } else if (b > -1) {
            table = createTable();
            tables[high] = table;
            table[low] = b;
        }
        return tables;
    }

    /**
     * Updates an inverse lookup table with additional mappings,
     * replacing previous mappings of the same values if they exists.
     *
     * @param tables the inverse lookup table to update
     *        (see {@link #createInverseLookupTable})
     * @param chars the characters to map
     * @param bytes the respective byte values to which the chars are mapped,
     *        or -1 to mark an illegal mapping
     * @return the updated inverse lookup (char-to-byte) table
     */
    public static int[][] updateInverseLookupTable(int[][] tables, int[] chars, int[] bytes) {
        for (int i = 0; i < chars.length; i++)
            updateInverseLookupTable(tables, chars[i], bytes[i]);
        return tables;
    }

    /**
     * Initializes a new charset with the given canonical name and alias
     * set, and byte-to-char/char-to-byte lookup tables.
     *
     * @param canonicalName the canonical name of this charset
     * @param aliases an array of this charset's aliases, or null if it has no aliases
     * @param byteToChar a byte-to-char conversion table for this charset
     * @param charToByte a char-to-byte conversion table for this charset. It can
     *        be generated on-the-fly by calling createInverseLookupTable(byteToChar).
     * @throws java.nio.charset.IllegalCharsetNameException
     *         if the canonical name or any of the aliases are illegal
     */
    protected ByteLookupCharset(String canonicalName, String[] aliases,
            int[] byteToChar, int[][] charToByte) {
        super(canonicalName, aliases);
        this.byteToChar = byteToChar;
        this.charToByte = charToByte;
    }

    /**
     * Tells whether or not this charset contains the given charset.
     *
     * <p> A charset <i>C</i> is said to <i>contain</i> a charset <i>D</i> if,
     * and only if, every character representable in <i>D</i> is also
     * representable in <i>C</i>.  If this relationship holds then it is
     * guaranteed that every string that can be encoded in <i>D</i> can also be
     * encoded in <i>C</i> without performing any replacements.
     *
     * <p> That <i>C</i> contains <i>D</i> does not imply that each character
     * representable in <i>C</i> by a particular byte sequence is represented
     * in <i>D</i> by the same byte sequence, although sometimes this is the
     * case.
     *
     * <p> Every charset contains itself.
     *
     * <p> This method computes an approximation of the containment relation:
     * If it returns <tt>true</tt> then the given charset is known to be
     * contained by this charset; if it returns <tt>false</tt>, however, then
     * it is not necessarily the case that the given charset is not contained
     * in this charset.
     *
     * @param charset the given charset
     *
     * @return <tt>true</tt> if the given charset is contained in this charset
     */
    @Override
    public boolean contains(Charset charset) {
        return this.getClass().isInstance(charset);
    }

    /**
     * Constructs a new decoder for this charset.
     *
     * @return a new decoder for this charset
     */
    @Override
    public CharsetDecoder newDecoder() {
        return new Decoder(this);
    }

    /**
     * Constructs a new encoder for this charset.
     *
     * @return a new encoder for this charset
     */
    @Override
    public CharsetEncoder newEncoder() {
        return new Encoder(this);
    }

    /**
     * The <b>Encoder</b> inner class handles the encoding of the
     * charset using the lookup table.
     */
    protected class Encoder extends CharsetEncoder {

        /**
         * Constructs an Encoder.
         *
         * @param charset the charset that created this encoder
         */
        protected Encoder(Charset charset) {
            super(charset, 1f, 1f);
        }

        /**
         * Encodes one or more characters into one or more bytes.
         *
         * @param in the input character buffer
         * @param out the output byte buffer
         * @return a coder-result object describing the reason for termination
         */
        @Override
        protected CoderResult encodeLoop(CharBuffer in, ByteBuffer out) {
            int[][] lookup = charToByte; // getfield bytecode optimization
            int remainingIn = in.remaining();
            int remainingOut = out.remaining();
            while (remainingIn-- > 0) {
                if (remainingOut-- < 1)
                    return CoderResult.OVERFLOW; // we need exactly one byte per char
                int c = in.get();
                int[] table = lookup[c >>> 8];
                int b = table == null ? -1 : table[c & 0xFF];
                if (b == -1) {
                    in.position(in.position() - 1);
                    return CoderResult.unmappableForLength(1);
                }
                out.put((byte)(b & 0xFF));
            }
            return CoderResult.UNDERFLOW;
        }

    }

    /**
     * The <b>Decoder</b> inner class handles the decoding of the
     * charset using the inverse lookup table.
     */
    protected class Decoder extends CharsetDecoder {

        /**
         * Constructs a Decoder.
         *
         * @param charset the charset that created this decoder
         */
        protected Decoder(Charset charset) {
            super(charset, 1f, 1f);
        }

        /**
         * Decodes one or more bytes into one or more characters.
         *
         * @param in the input byte buffer
         * @param out the output character buffer
         * @return a coder-result object describing the reason for termination
         */
        @Override
        protected CoderResult decodeLoop(ByteBuffer in, CharBuffer out) {
            int[] lookup = byteToChar; // getfield bytecode optimization
            int remainingIn = in.remaining();
            int remainingOut = out.remaining();
            while (remainingIn-- > 0) {
                if (remainingOut-- < 1)
                    return CoderResult.OVERFLOW; // we need exactly one char per byte
                int c = lookup[in.get() & 0xFF];
                if (c == -1) {
                    in.position(in.position() - 1);
                    return CoderResult.malformedForLength(1);
                }
                out.put((char)c);
            }
            return CoderResult.UNDERFLOW;
        }
    }

}
