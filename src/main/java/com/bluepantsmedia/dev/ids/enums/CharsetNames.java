package com.bluepantsmedia.dev.ids.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Project Identity Streams
 * Author Gary Gerber
 * Email garygerber@bluepantsmedia.com
 * Date 4/10/2020 7:20 AM
 * Copyright 2020 by Bluepants Media, LLC
 */
public enum CharsetNames  implements ILabeled {
    UTF_8("UTF-8","13.0",1)
    , UTF_16("UTF-16","13.0",2)
    , UTF_32("UTF-32","13.0",3)
    , IDS_64("IDS-64","0.0.1-SNAPSHOT",4);

    public final String label;
    public final String version;
    public final int index;

    private CharsetNames(String label, String version, int index) {
        this.label = label;
        this.version = version;
        this.index = index;
    }

    @Override
    public String label() {
        return label;
    }

    @Override
    public String toString() {
        return this.name() + "(\"" + this.label + "\"," + this.version + "," + this.index + ")";
    }

    public static CharsetNames valueOfLabel(String label) {
        return BY_LABEL.get(label);
    }

    public static CharsetNames valueOfIndex(Integer index) {
        return BY_INDEX.get(index);
    }

    private static final Map<String, CharsetNames> BY_LABEL = new HashMap<>();
    private static final Map<Integer, CharsetNames> BY_INDEX = new HashMap<>();

    static {
        for (CharsetNames e: values()) {
            BY_LABEL.put(e.label, e);
            BY_INDEX.put(e.index, e);
        }
    }
}
