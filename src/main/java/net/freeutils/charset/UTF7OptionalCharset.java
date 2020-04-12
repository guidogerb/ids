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

/**
 * The <b>UTF7OptionalCharset</b> class handles the encoding and decoding of the
 * UTF-7 charset.
 * <p>
 * The O-Set characters are encoded directly
 * (this is optional according to the RFC).
 * <p>
 * The encoding and decoding are based on RFC 2152
 * (http://www.ietf.org/rfc/rfc2152.txt)
 *
 * Author Amichai Rothman
 * @since  2005-06-10
 */
public class UTF7OptionalCharset extends UTF7Charset {

    static final String NAME = "UTF-7-OPTIONAL";

    static final String[] ALIASES = { "UTF-7O", "UTF7O", "UTF-7-O" };

    /**
     * Constructs an instance of the UTF7OptionalCharset.
     * <p>
     * O-set characters are directly encoded.
     */
    public UTF7OptionalCharset() {
        super(NAME, ALIASES, true);
    }

}
