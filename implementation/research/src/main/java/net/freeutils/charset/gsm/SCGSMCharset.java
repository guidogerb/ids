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
 * The <b>SCGSMCharset</b> class handles the encoding and decoding of the
 * GSM default encoding charset. In this variant, byte 0x09 is mapped
 * to the LATIN SMALL LETTER C WITH CEDILLA character.
 * <p>
 * The encoding and decoding are based on the mapping at
 * http://www.unicode.org/Public/MAPPINGS/ETSI/GSM0338.TXT
 *
 * Author Amichai Rothman
 * @since  2007-03-26
 */
public class SCGSMCharset extends GSMCharset {

    static final String NAME = "SCGSM";

    static final String[] ALIASES = {
        "GSM-DEFAULT-ALPHABET", "GSM_0338", "GSM_DEFAULT", "GSM7", "GSM-7BIT" };

    /**
     * Constructs an instance of the SCGSMCharset.
     */
    public SCGSMCharset() {
        super(NAME, ALIASES,
            BYTE_TO_CHAR_SMALL_C_CEDILLA, BYTE_TO_CHAR_ESCAPED_DEFAULT,
            CHAR_TO_BYTE_SMALL_C_CEDILLA, CHAR_TO_BYTE_ESCAPED_DEFAULT);
    }

}
