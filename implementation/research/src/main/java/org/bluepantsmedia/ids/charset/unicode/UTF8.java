package org.bluepantsmedia.ids.charset.unicode;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.IllegalCharsetNameException;

/**
 * Project Identity Streams
 * Author Gary Gerber
 * Email garygerber@bluepantsmedia.com
 * Date 4/10/2020 8:47 AM
 * Copyright 2020 by Bluepants Media, LLC
 */
public class UTF8 extends Charset {
    /**
     * Initializes a new charset with the given canonical name and alias
     * set.
     *
     * @param canonicalName The canonical name of this charset
     * @param aliases       An array of this charset's aliases, or null if it has no aliases
     * @throws IllegalCharsetNameException If the canonical name or any of the aliases are illegal
     */
    public UTF8(String canonicalName, String[] aliases) {
        super(canonicalName, aliases);
    }

    @Override
    public boolean contains(Charset cs) {
        return false;
    }

    @Override
    public CharsetDecoder newDecoder() {
        return null;
    }

    @Override
    public CharsetEncoder newEncoder() {
        return null;
    }
}
