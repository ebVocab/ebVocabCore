package de.ebuchner.vocab.tools;

import java.util.Random;

public class RandomTools {

    private static Random random = new Random(System.currentTimeMillis());

    private RandomTools() {

    }

    public static String nextId() {
        return Integer.toHexString(random.nextInt());
    }

}
