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

/**
 * The <b>CRSCPackedGSMCharset</b> class handles the encoding and decoding of the
 * GSM default encoding charset. In this variant, byte 0x09 is mapped
 * to the LATIN SMALL LETTER C WITH CEDILLA character.
 * It also uses {@link PackedGSMCharset CR-padding} instead of
 * zero-padding to avoid ambiguous interpretation of an '@' character.
 * <p>
 * The encoding and decoding are based on the mapping at
 * http://www.unicode.org/Public/MAPPINGS/ETSI/GSM0338.TXT
 *
 * Author Amichai Rothman
 * @since  2019-03-31
 */
public class CRSCPackedGSMCharset extends PackedGSMCharset {

    static final String NAME = "CRSCPGSM";

    static final String[] ALIASES = {};

    /**
     * Constructs an instance of the CRSCPackedGSMCharset.
     */
    public CRSCPackedGSMCharset() {
        super(NAME, ALIASES,
            BYTE_TO_CHAR_SMALL_C_CEDILLA, BYTE_TO_CHAR_ESCAPED_DEFAULT,
            CHAR_TO_BYTE_SMALL_C_CEDILLA, CHAR_TO_BYTE_ESCAPED_DEFAULT,
            true);
    }

}
