package org.bluepantsmedia.ids.enums;

import org.bluepantsmedia.ids.Version;

import java.util.HashMap;
import java.util.Map;

/**
 * Project Identity Streams
 * Author Gary Gerber
 * Email garygerber@bluepantsmedia.com
 * Date 4/10/2020 7:20 AM
 * Copyright 2020 by Bluepants Media, LLC
 */
public enum CharsetNames implements ILabeled {
    UTF_8("UTF-8", "13.0", 1), UTF_16("UTF-16", "13.0", 2), UTF_32("UTF-32", "13.0", 3), IDS_64("IDS-64", CharsetNames.VERSION, 4);

    // private static final String VERSION = "1.0.0-SNAPSHOT"; // use this if build was cleaned previous to package
    private static final String VERSION = Version.VERSION;
    private static final Map<String, CharsetNames> BY_LABEL = new HashMap<>();
    private static final Map<Integer, CharsetNames> BY_INDEX = new HashMap<>();

    static {
        for (CharsetNames e : values()) {
            BY_LABEL.put(e.label, e);
            BY_INDEX.put(e.index, e);
        }
    }

    public final String label;
    public final String version;
    public final int index;

    private CharsetNames(String label, String version, int index) {
        this.label = label;
        this.version = version;
        this.index = index;
    }

    public static CharsetNames valueOfLabel(String label) {
        return BY_LABEL.get(label);
    }

    public static CharsetNames valueOfIndex(Integer index) {
        return BY_INDEX.get(index);
    }

    @Override
    public String label() {
        return label;
    }

    @Override
    public String toString() {
        return this.name() + "(\"" + this.label + "\"," + this.version + "," + this.index + ")";
    }

}
