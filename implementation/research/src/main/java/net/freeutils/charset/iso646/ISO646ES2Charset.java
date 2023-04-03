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

package net.freeutils.charset.iso646;

import net.freeutils.charset.ByteLookupCharset;

/**
 * The <b>ISO646ES2Charset</b> class handles the encoding and decoding of the
 * ISO646-ES2 national variant of the ISO/IEC 646 charset.
 *
 * Author Amichai Rothman
 * @since  2015-08-18
 */
public class ISO646ES2Charset extends ByteLookupCharset {

    static final String NAME = "ISO646-ES2";

    static final String[] ALIASES = { "ISO-IR-85" };

    static final int[] BYTE_TO_CHAR = mutate(ISO646USCharset.BYTE_TO_CHAR,
        new int[] { 0x40,   0x5B, 0x5C, 0x5D, 0x5E, 0x7B, 0x7C, 0x7D, 0x7E },
        new int[] { 0x2022, 0xA1, 0xD1, 0xC7, 0xBF, 0xB4, 0xF1, 0xE7, 0xA8 });

    static final int[][] CHAR_TO_BYTE = createInverseLookupTable(BYTE_TO_CHAR);

    /**
     * Constructs an instance of the ISO646ES2Charset.
     */
    public ISO646ES2Charset() {
        super(NAME, ALIASES, BYTE_TO_CHAR, CHAR_TO_BYTE);
    }

}
