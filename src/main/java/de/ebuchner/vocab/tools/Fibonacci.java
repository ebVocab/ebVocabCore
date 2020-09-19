package de.ebuchner.vocab.tools;

import java.util.ArrayList;
import java.util.List;

public class Fibonacci {

    private List<Integer> fibonacciNumbers = new ArrayList<Integer>();

    public Fibonacci(int limit) {
        int f1 = 0, f2 = 1;
        int f = f1 + f2;
        while (f <= limit) {
            fibonacciNumbers.add(f);
            f1 = f2;
            f2 = f;
            f = f1 + f2;
        }
    }

    public int nextFibonacci(int value) {
        int next = 0;
        for (int f : fibonacciNumbers) {
            if (f <= value)
                next = f;
        }
        return next;
    }

    public Integer[] fibonacciNumbers() {
        return fibonacciNumbers.toArray(new Integer[fibonacciNumbers.size()]);
    }


}
