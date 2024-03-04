package com.direwolf20.justdirethings.util;

import java.util.Random;

public class MiscHelpers {
    private static final Random rand = new Random();

    public static double nextDouble(double min, double max) {
        return min + (max - min) * rand.nextDouble();
    }
}
