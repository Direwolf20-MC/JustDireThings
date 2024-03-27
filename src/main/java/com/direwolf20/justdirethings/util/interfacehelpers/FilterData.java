package com.direwolf20.justdirethings.util.interfacehelpers;

import com.direwolf20.justdirethings.util.ItemStackKey;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;

import java.util.Map;

public class FilterData {
    public boolean allowlist = false, compareNBT = false;
    public int blockItemFilter = -1;

    //This is not saved in NBT, and is recreated as needed on demand
    public final Map<ItemStackKey, Boolean> filterCache = new Object2BooleanOpenHashMap<>();

    public FilterData() {

    }

    public FilterData(boolean allowlist, boolean compareNBT, int blockItemFilter) {
        this.allowlist = allowlist;
        this.compareNBT = compareNBT;
        this.blockItemFilter = blockItemFilter;
    }
}
