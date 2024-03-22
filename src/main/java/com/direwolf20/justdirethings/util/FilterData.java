package com.direwolf20.justdirethings.util;

import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;

import java.util.Map;

public class FilterData {
    public boolean allowlist = false, compareNBT = false;

    //This is not saved in NBT, and is recreated as needed on demand
    public final Map<ItemStackKey, Boolean> filterCache = new Object2BooleanOpenHashMap<>();

    public FilterData() {

    }
}
