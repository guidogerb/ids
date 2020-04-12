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

/**
 * The <b>EscapedByteLookupCharset</b> class handles the encoding and
 * decoding of simple charsets where the byte-to-char conversion
 * is performed using a simple lookup table, with the addition of a special
 * escape byte, such that the single byte following it is converted using
 * an alternate lookup table.
 *
 * Author Amichai Rothman
 * @since  2007-03-26
 */
public abstract class EscapedByteLookupCharset extends Charset {

    final int[] byteToChar;
    final int[] byteToCharEscaped;
    final int[][] charToByte;
    final int[][] charToByteEscaped;
    final byte escapeByte;

    /**
     * Initializes a new charset with the given canonical name and alias
     * set, and byte-to-char/char-to-byte lookup tables.
     *
     * @param canonicalName the canonical name of this charset
     * @param aliases an array of this charset's aliases, or null if it has no aliases
     * @param escapeByte the special escape byte value
     * @param byteToChar a byte-to-char conversion table for this charset
     * @param byteToCharEscaped a byte-to-char conversion table for this charset
     *        for the escaped characters
     * @param charToByte a char-to-byte conversion table for this charset. It can
     *        be generated on-the-fly by calling
     *        {@link ByteLookupCharset#createInverseLookupTable
     *        createInverseLookupTable(byteToChar)}.
     * @param charToByteEscaped a char-to-byte conversion table for this charset
     *        for the escaped characters
     * @throws java.nio.charset.IllegalCharsetNameException
     *         if the canonical name or any of the aliases are illegal
     */
    protected EscapedByteLookupCharset(String canonicalName, String[] aliases,
            byte escapeByte, int[] byteToChar, int[] byteToCharEscaped,
            int[][] charToByte, int[][] charToByteEscaped) {
        super(canonicalName, aliases);
        this.escapeByte = escapeByte;
        this.byteToChar = byteToChar;
        this.charToByte = charToByte;
        this.byteToCharEscaped = byteToCharEscaped;
        this.charToByteEscaped = charToByteEscaped;
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
     * charset using the lookup tables.
     */
    protected class Encoder extends CharsetEncoder {

        /**
         * Constructs an Encoder.
         *
         * @param charset the charset that created this encoder
         */
        protected Encoder(Charset charset) {
            super(charset, 1f, 2f);
        }

        /**
         * Constructs an Encoder.
         *
         * @param charset the charset that created this encoder
         * @param averageBytesPerChar a positive float value indicating the expected
         *        number of bytes that will be produced for each input character
         *
         * @param maxBytesPerChar a positive float value indicating the maximum
         *        number of bytes that will be produced for each input character
         */
        protected Encoder(Charset charset, float averageBytesPerChar, float maxBytesPerChar) {
            super(charset, averageBytesPerChar, maxBytesPerChar);
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
            byte escape = escapeByte; // getfield bytecode optimization
            int[][] lookup = charToByte; // getfield bytecode optimization
            int[][] lookupEscaped = charToByteEscaped; // getfield bytecode optimization
            int remainingIn = in.remaining();
            int remainingOut = out.remaining();
            while (remainingIn-- > 0) {
                // make sure we have room for output
                if (remainingOut-- < 1)
                    return CoderResult.OVERFLOW;
                // get next char
                int c = in.get();
                // look for corresponding regular byte
                int[] table = lookup[c >> 8];
                int b = table == null ? -1 : table[c & 0xFF];
                if (b == -1) {
                    // look for corresponding escaped byte
                    table = lookupEscaped[c >> 8];
                    b = table == null ? -1 : table[c & 0xFF];
                    if (b == -1) {
                        // there's no regular nor escaped byte - it's unmappable
                        in.position(in.position() - 1); // unread the char
                        return CoderResult.unmappableForLength(1);
                    }
                    // it's an escapable char, make sure we have room for extra output
                    if (remainingOut-- < 1) {
                        in.position(in.position() - 1); // unread the char
                        return CoderResult.OVERFLOW;
                    }
                    // write the escape byte (output byte will follow)
                    out.put(escape);
                }
                // write the output byte
                out.put((byte)(b & 0xFF));
            }
            // no more input available
            return CoderResult.UNDERFLOW;
        }

    }

    /**
     * The <b>Decoder</b> inner class handles the decoding of the
     * charset using the inverse lookup tables.
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
         * Constructs a Decoder.
         *
         * @param charset the charset that created this decoder
         * @param averageCharsPerByte a positive float value indicating the expected
         *        number of characters that will be produced for each input byte
         * @param maxCharsPerByte a positive float value indicating the maximum
         *        number of characters that will be produced for each input byte
         */
        protected Decoder(Charset charset, float averageCharsPerByte, float maxCharsPerByte) {
            super(charset, averageCharsPerByte, maxCharsPerByte);
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
            byte escape = escapeByte; // getfield bytecode optimization
            int[] lookup = byteToChar; // getfield bytecode optimization
            int[] lookupEscaped = byteToCharEscaped; // getfield bytecode optimization
            int remainingIn = in.remaining();
            int remainingOut = out.remaining();
            while (remainingIn-- > 0) {
                // make sure we have room for output
                if (remainingOut-- < 1)
                    return CoderResult.OVERFLOW;
                // get next byte
                int c;
                int b = in.get();
                if (b == escape) {
                    // it's the escape byte - make sure we have the next byte
                    if (remainingIn-- < 1) {
                        in.position(in.position() - 1); // unread the byte
                        return CoderResult.UNDERFLOW;
                    }
                    // get next byte
                    b = in.get();
                    // look for corresponding escaped char
                    c = lookupEscaped[b & 0xFF];
                } else {
                    // look for corresponding regular char
                    c = lookup[b & 0xFF];
                }

                if (c == -1) {
                    // there's no regular nor escaped char - it's malformed
                    in.position(in.position() - 1); // unread the byte
                    return CoderResult.malformedForLength(1);
                }
                // write the output char
                out.put((char)c);
            }
            // no more input available
            return CoderResult.UNDERFLOW;
        }
    }

}
