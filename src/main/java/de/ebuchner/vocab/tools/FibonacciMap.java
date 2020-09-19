package de.ebuchner.vocab.tools;

import java.util.*;

public class FibonacciMap<T extends FibonacciMap.IntValueHaving> {

    public Map<Integer, List<T>> mapFrom(List<T> elements) {
        if (elements == null)
            return Collections.emptyMap();

        int limit = 0;
        for (IntValueHaving intValue : elements) {
            int value = intValue.value();
            if (value > limit)
                limit = value;
        }

        Fibonacci fibonacci = new Fibonacci(limit);

        Map<Integer, List<T>> map = new HashMap<Integer, List<T>>();

        for (T intValue : elements) {
            int keyVal = fibonacci.nextFibonacci(intValue.value());
            List<T> listVal = map.get(keyVal);
            if (listVal == null) {
                listVal = new ArrayList<T>();
                map.put(keyVal, listVal);
            }
            listVal.add(intValue);
        }

        return map;
    }

    public static interface IntValueHaving {
        int value();
    }
}
