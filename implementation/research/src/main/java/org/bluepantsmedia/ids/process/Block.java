package org.bluepantsmedia.ids.process;

import java.io.Serializable;

/**
 * Project IDS - Identity Streams
 * Author Gary Gerber
 * Email garygerber@bluepantsmedia.com
 * Date 3/12/2022 5:50 PM
 * Copyright 2020 by Bluepants Media, LLC
 * <p>
 * Chunk is a wrapper class for extracting 7 bit numbers, leaving the msb as terminating.
 */

public class Block implements Serializable {
    private final Byte aByte;

    /**
     * 7 bit data and msb terminator
     *
     * @param aByte    <= 127 (7 bit number)
     * @param terminal indicates if this is a terminating chunk
     */
    public Block(final Byte aByte, final Boolean terminal) {
        if (aByte > 127) {
            throw new IllegalArgumentException("Chunk byte size is larger than a 7 bit number (127).");
        }
        if (terminal) {
            // flip MSB
            this.aByte = (byte) (aByte & 0x80);
        } else {
            this.aByte = aByte;
        }
    }

    public Byte getByte() {
        return aByte;
    }

    public String getBitString() {
        return String.format("%8s", Integer.toBinaryString(aByte & 0xFF)).replace(' ', '0');
    }
}
