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
 * The <b>ISO646USCharset</b> class handles the encoding and decoding of the
 * ISO646-US national variant of the ISO/IEC 646 charset.
 *
 * Author Amichai Rothman
 * @since  2015-08-18
 */
public class ISO646USCharset extends ByteLookupCharset {

    static final String NAME = "ISO646-US";

    static final String[] ALIASES = { "ISO-IR-6", "ISO_646.irv:1991" };

    static final int[] BYTE_TO_CHAR;

    static {
        BYTE_TO_CHAR = createTable();
        for (int i = 0; i < 128; i++)
            BYTE_TO_CHAR[i] = i;
    }

    static final int[][] CHAR_TO_BYTE = createInverseLookupTable(BYTE_TO_CHAR);

    /**
     * Constructs an instance of the ISO646USCharset.
     */
    public ISO646USCharset() {
        super(NAME, ALIASES, BYTE_TO_CHAR, CHAR_TO_BYTE);
    }

}
