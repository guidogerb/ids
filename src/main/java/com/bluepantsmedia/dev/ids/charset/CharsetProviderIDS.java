package com.bluepantsmedia.dev.ids.charset;

import com.bluepantsmedia.dev.ids.enums.CharsetNames;
import com.bluepantsmedia.dev.ids.charset.unicode.UTF32;

import java.nio.charset.Charset;
import java.nio.charset.spi.CharsetProvider;
import java.util.*;

/**
 * Project Identity Streams
 * Author Gary Gerber
 * Email garygerber@bluepantsmedia.com
 * Date 4/10/2020 8:30 AM
 * Copyright 2020 by Bluepants Media, LLC
 */
public class CharsetProviderIDS extends CharsetProvider {
    static Map<String, Charset> nameToCharset;
    static Collection<Charset> charsets;

    /**
     * Retrieves a charset for the given charset name.
     *
     * @param charsetName the name of the requested charset;
     *        may be either a canonical name or an alias
     *
     * @return a charset
     *
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
                new CharsetIDS(CharsetNames.IDS_64.label,new String[]{"IDS-64", "bluepantsmedia-ids-32"}),
                new UTF32(CharsetNames.UTF_32.label,new String[]{"UTF_32", "UTF-32", "UTF32"})
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
