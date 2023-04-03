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

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

/**
 * The <b>PackedGSMCharset</b> class handles the encoding and decoding of the
 * GSM default encoding charset, with packing as per GSM 03.38 / ETSI TS 123 038 spec.
 * <p>
 * When there are 8*n-1 encoded bytes, there is ambiguity
 * since it's impossible to distinguish whether the final byte
 * contains a trailing '@' character (which is mapped to 0)
 * or 7 zero bits of padding following 7 data bytes.
 * <p>
 * When decoding, we opt for the latter interpretation
 * since it's far more likely, at the cost of losing a
 * trailing '@' character in strings whose unpacked size
 * is a multiple of 8, and whose last character is '@'.
 * <p>
 * An application that wishes to handle this rare case
 * properly must disambiguate this case externally, such
 * as by obtaining the original string length, and
 * appending the trailing '@' if the length
 * shows that there is one character missing.
 * <p>
 * Alternatively, the spec supports replacing the zero
 * padding in such a case with a CR character, which is
 * then removed by the receiver, but is harmless also on
 * devices that display it as-is since a CR is invisible.
 * This implementation has configurable support for CR padding.
 * <p>
 * However, this CR padding introduces a new ambiguity, with
 * a string that really does end with a CR character on an
 * 8-byte boundary, so in this case an extra CR is appended
 * to it, and due to the semantics of CR in the spec, a double
 * CR is equivalent to a single CR, so this is harmless as well.
 * <p>
 * The encoding and decoding are based on the mapping at
 * http://www.unicode.org/Public/MAPPINGS/ETSI/GSM0338.TXT
 *
 * Author Amichai Rothman
 * @since  2007-03-20
 */
public class PackedGSMCharset extends GSMCharset {

    static final int BUFFER_SIZE = 256;
    static final byte CR = 0x0D;

    /**
     * Specifies whether to use CR padding instead of zero padding
     * when encoding/decoding in order to disambiguate the 7 padding
     * zero bits in strings whose length is 8*n-1 bytes from a
     * trailing '@' character in strings of length 8*n.
     */
    final boolean padWithCR;

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
     * @param padWithCR specifies whether to apply {@link PackedGSMCharset CR padding}
     *        or the original (but ambiguous) zero padding
     * @throws java.nio.charset.IllegalCharsetNameException
     *         if the canonical name or any of the aliases are illegal
     */
    protected PackedGSMCharset(String canonicalName, String[] aliases,
            int[] byteToChar, int[] byteToCharEscaped,
            int[][] charToByte, int[][] charToByteEscaped,
            boolean padWithCR) {
        super(canonicalName, aliases,
            byteToChar, byteToCharEscaped, charToByte, charToByteEscaped);
        this.padWithCR = padWithCR;
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
     * Packed GSM default encoding charset.
     */
    protected class Encoder extends GSMCharset.Encoder {

        int bitpos;
        byte current;
        ByteBuffer buf;

        /**
         * Constructs an Encoder.
         *
         * @param charset the charset that created this encoder
         */
        protected Encoder(Charset charset) {
            super(charset, 7 / 8f, 2f);
            buf = ByteBuffer.allocate(BUFFER_SIZE);
            implReset();
        }

        /**
         * Resets this encoder, clearing any charset-specific internal state.
         */
        @Override
        protected void implReset() {
            bitpos = 0;
            current = 0;
            buf.limit(0);
        }

        /**
         * Flushes this encoder.
         *
         * @param out the output byte buffer
         *
         * @return a coder-result object, either {@link CoderResult#UNDERFLOW} or
         *         {@link CoderResult#OVERFLOW}
         */
        @Override
        protected CoderResult implFlush(ByteBuffer out) {
            // flush buffer
            CoderResult result = pack(buf, out);
            // handle CR padding if necessary
            if (padWithCR && bitpos <= 1) { // bitpos is 0 or 1
                if (bitpos == 1) {
                    // if the output is 8*n-1 bytes long, the last byte has 7 padding zero
                    // bits which may be ambiguously interpreted as an '@' character,
                    // so in this case we replace the padding with a harmless CR
                    current |= (CR << 1);
                } else if (out.position() > 0 && out.get(out.position() - 1) >>> 1 == CR) {
                    // if the output is 8*n bytes long and really does end with a CR,
                    // we need to disambiguate this from the CR padding,
                    // so we add an extra CR (due to the spec's definition of CR,
                    // this is equivalent to a single CR and thus also harmless)
                    current = CR;
                    bitpos = 7;
                }
            }
            // flush last (current) partial byte if it exists
            if (bitpos != 0) {
                if (!out.hasRemaining())
                    return CoderResult.OVERFLOW;
                out.put(current); // write final leftover byte
            }
            return result;
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
            CoderResult result;
            while (true) {
                // output buffered data
                if (buf.hasRemaining()) {
                    result = pack(buf, out);
                    if (result == CoderResult.OVERFLOW)
                        return result;
                }
                // process new data into buffer
                buf.clear();
                result = super.encodeLoop(in, buf);
                buf.flip();
                // stop if out of input or error
                if (!buf.hasRemaining() || result.isError())
                    return result;
            }
        }

        /**
         * Packs the given data into full bytes.
         *
         * @param in the input byte buffer
         * @param out the output byte buffer
         * @return a coder-result object, either {@link CoderResult#UNDERFLOW} or
         *         {@link CoderResult#OVERFLOW}
         */
        protected CoderResult pack(ByteBuffer in, ByteBuffer out) {
            int remaining = in.remaining();
            while (remaining-- > 0) {
                if (!out.hasRemaining())
                    return CoderResult.OVERFLOW;
                byte b = (byte)(in.get() & 0x7F); // remove top bit
                // assign first group of partial bits
                current |= b << bitpos;
                // assign second group of partial bits (if exist)
                if (bitpos > 0) { // if packed byte is full
                    out.put(current);
                    current = (byte)(b >> (8 - bitpos)); // keep left-over bits (if any)
                }
                bitpos = (bitpos + 7) % 8;
            }
            return CoderResult.UNDERFLOW;
        }

    }

    /**
     * The <b>Decoder</b> inner class handles the decoding of the
     * Packed GSM default encoding charset.
     */
    protected class Decoder extends GSMCharset.Decoder {

        int bitpos;
        byte current;
        byte prev;
        int unpackedCount;
        ByteBuffer buf;

        /**
         * Constructs a Decoder.
         *
         * @param charset the charset that created this decoder
         */
        protected Decoder(Charset charset) {
            super(charset, 8 / 7f, 2f);
            buf = ByteBuffer.allocate(BUFFER_SIZE);
            implReset();
        }

        /**
         * Resets this decoder, clearing any charset-specific internal state.
         */
        @Override
        protected void implReset() {
            bitpos = 0;
            current = 0;
            prev = 0;
            unpackedCount = 0;
            buf.limit(0);
        }

        /**
         * Flushes this decoder.
         *
         * @param out the output character buffer
         *
         * @return a coder-result object, either {@link CoderResult#UNDERFLOW} or
         *         {@link CoderResult#OVERFLOW}
         */
        @Override
        protected CoderResult implFlush(CharBuffer out) {
            // fix output edge cases caused by ambiguous padding,
            // depending on the CR padding configuration:
            // either remove a trailing '@' character if the string length is 8*n,
            // or remove a trailing CR character if the string length is 8*n
            // or if the string length is 8*n+1 and it ends with two CR characters
            int mod = unpackedCount % 8;
            if (mod <= 1) { // mod is 0 or 1
                int pos = out.position() - 1;
                if (pos > 0) {
                    char c = out.get(pos);
                    if (c == '@' && !padWithCR && mod == 0 ||
                        c == CR && padWithCR && (mod == 0 || out.get(pos - 1) == CR))
                            out.position(pos); // remove last character
                }
            }
            return CoderResult.UNDERFLOW;
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
            while (true) {
                // unpack input data into buffer
                unpackedCount -= buf.remaining(); // it will be counted again after unpack
                buf.compact(); // move data to beginning and prepare to write more
                CoderResult unpackResult = unpack(in, buf);
                buf.flip(); // prepare to read
                if (!buf.hasRemaining())
                    return unpackResult; // underflow
                unpackedCount += buf.remaining();
                // decode buffered unpacked data to output
                CoderResult decodeResult = super.decodeLoop(buf, out);
                // handle out of output space and buffer still has data in it
                if (buf.hasRemaining() || decodeResult.isError()) {
                    if (decodeResult.isUnderflow()) { // last byte is escape byte
                        // if there's more input or at least another unpacked byte
                        // (the 8th doesn't require reading from input), just continue
                        if (in.hasRemaining() || unpackResult.isOverflow())
                            continue;
                        // otherwise we really need more input, so undo the last byte
                        // (escape sequence which was cut in middle) so caller can
                        // properly handle malformed input if there is no more input
                        in.position(in.position() - 1); // unread the byte
                        bitpos = (bitpos + 9) % 8; // undo its unpacking too
                        current = prev;
                        buf.limit(buf.position());
                        unpackedCount--;
                    }
                    return decodeResult;
                }
            }
        }

        /**
         * Unpacks the given data into original bytes.
         *
         * @param in the input byte buffer
         * @param out the output byte buffer
         * @return a coder-result object, either {@link CoderResult#UNDERFLOW} or
         *         {@link CoderResult#OVERFLOW}
         */
        protected CoderResult unpack(ByteBuffer in, ByteBuffer out) {
            int remaining = out.remaining();
            while (remaining-- > 0) {
                if (!in.hasRemaining() && bitpos != 1)
                    return CoderResult.UNDERFLOW;
                if (bitpos == 0) {
                    prev = current;
                    current = in.get();
                }
                // remove top bit and assign first group of partial bits
                byte b = (byte)(((current & 0xFF) >> bitpos) & 0x7F);
                // remove top bit and assign second group of partial bits (if exist)
                if (bitpos >= 2) {
                    prev = current;
                    current = in.get();
                    b |= (byte)((current << (8 - bitpos)) & 0x7F);
                }
                bitpos = (bitpos + 7) % 8;
                out.put(b);
            }
            return CoderResult.OVERFLOW;
        }
    }

    /**
     * Unpacks the given data into original bytes.
     * <p>
     * This is an external utility method and is not used
     * internally by the Charset implementation.
     *
     * @param in the input bytes
     * @return the unpacked output bytes
     */
    public static byte[] unpack(byte[] in) {
        byte[] out = new byte[(in.length * 8) / 7];
        int len = out.length;
        int current = 0;
        int bitpos = 0;
        for (int i = 0; i < len; i++) {
            // remove top bit and assign first group of partial bits
            out[i] = (byte)(((in[current] & 0xFF) >> bitpos) & 0x7F);
            // remove top bit and assign second group of partial bits (if exist)
            if (bitpos > 1)
                out[i] |= (byte)((in[++current] << (8 - bitpos)) & 0x7F);
            else if (bitpos == 1)
                current++;
            bitpos = (bitpos + 7) % 8;
        }
        // this fixes an ambiguity bug in the specs
        // where the last of 8 packed bytes is 0
        // and it's impossible to distinguish whether it is a
        // trailing '@' character (which is mapped to 0)
        // or extra zero-bit padding for 7 actual data bytes.
        //
        // we opt for the latter, since it's far more likely,
        // at the cost of losing a trailing '@' character
        // in strings whose unpacked size modulo 8 is 0,
        // and whose last character is '@'.
        //
        // an application that wishes to handle this rare case
        // properly must disambiguate this case externally, such
        // as by obtaining the original string length, and
        // appending the trailing '@' if the length
        // shows that there is one character missing.
        if (len % 8 == 0 && len > 0 && out[len - 1] == 0) {
            byte[] fixed = new byte[len - 1];
            System.arraycopy(out, 0, fixed, 0, len - 1);
            out = fixed;
        }
        return out;
    }

    /**
     * Packs the given data into full bytes.
     * <p>
     * This is an external utility method and is not used
     * internally by the Charset implementation.
     *
     * @param in the input bytes
     * @return the packed output bytes
     */
    public static byte[] pack(byte[] in) {
        byte[] out = new byte[(int)Math.ceil((in.length * 7) / 8f)];
        int current = 0;
        int bitpos = 0;
        for (byte b : in) {
            b &= 0x7F; // remove top bit
            // assign first group of partial bits
            out[current] |= b << bitpos;
            // assign second group of partial bits (if exist)
            if (bitpos > 1)
                out[++current] |= b >> 8 - bitpos;
            else if (bitpos == 1) // packed byte is full (but no left-over bits)
                current++;
            bitpos = (bitpos + 7) % 8;
        }
        return out;
    }

}
