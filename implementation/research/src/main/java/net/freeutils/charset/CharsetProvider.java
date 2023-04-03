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

import net.freeutils.charset.gsm.*;
import net.freeutils.charset.iso646.*;

import java.nio.charset.Charset;
import java.util.*;

/**
 * The <b>CharsetProvider</b> class is a Charset Provider implementation.
 *
 * Author Amichai Rothman
 * @since  2005-06-10
 */
public class CharsetProvider extends java.nio.charset.spi.CharsetProvider {

    static Map<String, Charset> nameToCharset;
    static Collection<Charset> charsets;

    /**
     * Retrieves a charset for the given charset name.
     *
     * @param charsetName the name of the requested charset;
     *        may be either a canonical name or an alias
     *
     * @return a charset object for the named charset,
     *         or <tt>null</tt> if the named charset
     *         is not supported by this provider
     */
    @Override
    public Charset charsetForName(String charsetName) {
        if (nameToCharset == null)
            init();

        // get charset instance for given name (case insensitive)
        Charset charset = nameToCharset.get(charsetName.toLowerCase());
        if (charset != null) {
            try {
                return charset.getClass().newInstance();
            } catch (Exception ignore) {
                // if we can't create an instance, we don't
            }
        }
        return null;
    }

    /**
     * Creates an iterator that iterates over the charsets supported by this
     * provider.  This method is used in the implementation of the {@link
     * java.nio.charset.Charset#availableCharsets Charset.availableCharsets}
     * method.
     *
     * @return the new iterator
     */
    @Override
    public Iterator<Charset> charsets() {
        if (charsets == null)
            init();

        return charsets.iterator();
    }

    /**
     * Initializes this charset provider's data.
     */
    void init() {
        // prepare supported charsets
        Charset[] allCharsets = {
            new UTF7Charset(),
            new UTF7OptionalCharset(),
            new SCGSMCharset(),
            new CCGSMCharset(),
            new SCPackedGSMCharset(),
            new CCPackedGSMCharset(),
            new CRSCPackedGSMCharset(),
            new CRCCPackedGSMCharset(),
            new HPRoman8Charset(),
            new KOI8UCharset(),
            new KZ1048Charset(),
            new ISO88598Charset(),
            new ISO88596Charset(),
            new MIKCharset(),
            new ISO646CACharset(),
            new ISO646CA2Charset(),
            new ISO646CHCharset(),
            new ISO646CNCharset(),
            new ISO646CUCharset(),
            new ISO646DECharset(),
            new ISO646DKCharset(),
            new ISO646ESCharset(),
            new ISO646ES2Charset(),
            new ISO646FISECharset(),
            new ISO646FRCharset(),
            new ISO646FR1Charset(),
            new ISO646GBCharset(),
            new ISO646HUCharset(),
            new ISO646IECharset(),
            new ISO646INVCharset(),
            new ISO646IRVCharset(),
            new ISO646ISCharset(),
            new ISO646ITCharset(),
            new ISO646JAOCharset(),
            new ISO646JPCharset(),
            new ISO646KRCharset(),
            new ISO646MTCharset(),
            new ISO646NO2Charset(),
            new ISO646NOCharset(),
            new ISO646PTCharset(),
            new ISO646PT2Charset(),
            new ISO646SE2Charset(),
            new ISO646T61Charset(),
            new ISO646TWCharset(),
            new ISO646USCharset(),
            new ISO646YUCharset(),
        };

        // initialize charset collection
        charsets = Collections.unmodifiableCollection(Arrays.asList(allCharsets));

        // initialize name to charset map
        Map<String, Charset> map = new HashMap<String, Charset>();
        for (Charset charset : allCharsets) {
            map.put(charset.name().toLowerCase(), charset);
            for (String alias : charset.aliases())
                map.put(alias.toLowerCase(), charset);
        }
        nameToCharset = map;
    }

}
