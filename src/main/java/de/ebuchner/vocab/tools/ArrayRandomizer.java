package de.ebuchner.vocab.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ArrayRandomizer<T> {

    private static Random random = new Random(System.currentTimeMillis());

    public List<T> randomize(List<T> list) {
        List<T> copyList = new ArrayList<T>();
        List<T> newList = new ArrayList<T>();

        newList.clear();
        copyList.clear();
        copyList.addAll(list);

        while (!copyList.isEmpty()) {
            newList.add(copyList.remove(random.nextInt(copyList.size())));
        }

        return newList;
    }
}
