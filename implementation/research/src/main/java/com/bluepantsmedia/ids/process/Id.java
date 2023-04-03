package com.bluepantsmedia.ids.process;

import lombok.NonNull;
import org.apache.commons.lang3.ArrayUtils;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Project IDS - Identity Streams
 * Author Gary Gerber
 * Email garygerber@bluepantsmedia.com
 * Date 3/12/2022 6:35 PM
 * Copyright 2020 by Bluepants Media, LLC
 */
public class Id implements Serializable {
    private List<Block> blocks;

    public Id(@NonNull BigInteger val) {
        this.blocks = composeBlocks(val);
    }

    public Id(@NonNull byte[] val) {
        this.blocks = composeBlocks(new BigInteger(val));
    }

    public BigInteger get() {
        StringBuilder value = new StringBuilder();
        for (Block block: blocks) {
            char[] val = block.getBitString().toCharArray();
            for(int i = 7; i > 0; i--) {
                value.append(val[i]);
            }
        }
        return new BigInteger(String.valueOf(value.reverse().toString()), 2);
    }

    private List<Block> composeBlocks(BigInteger val) {
        List<Block> chunks = new ArrayList<>();
        final char[] valBitStr = new StringBuilder(val.toString(2)).reverse().toString().toCharArray();
        int valBitStrPosition = 0;
        boolean hasMore = true;
        while(hasMore) {
            int bitIndex = 7;
            hasMore = valBitStr.length - valBitStrPosition > 7;
            char[] byteString = "00000000".toCharArray();
            while(bitIndex > 0 && valBitStr.length - valBitStrPosition > 0) {
                byteString[bitIndex--] = valBitStr[valBitStrPosition++];
            }
            chunks.add(new Block(Byte.parseByte(String.valueOf(byteString), 2), !hasMore));
        }
        return chunks;
    }

    private BitSet fromString(final String s) {
        return BitSet.valueOf(new long[]{Long.parseLong(s, 2)});
    }


    public List<Byte> getByteList() {
        final List<Byte> byteList = new ArrayList<Byte>();
        for(Block block: blocks) {
            byteList.add(block.getByte());
        }
        return byteList;
    }

    public byte[] getBytes() {
        final List<Byte> byteList = new ArrayList<Byte>();
        for(Block block: blocks) {
            byteList.add(block.getByte());
        }
        Byte[] bytes = byteList.toArray(new Byte[byteList.size()]);
        return ArrayUtils.toPrimitive(bytes);
    }

}
