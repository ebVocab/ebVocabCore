package de.ebuchner.vocab.model.alphabet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class RandomAlphabet implements Alphabet {
    private Alphabet alphabet;

    private List<AlphabetCharacter> randomList = new ArrayList<AlphabetCharacter>();

    private Random random = new Random(System.currentTimeMillis());

    public RandomAlphabet(Alphabet alphabet) {
        this.alphabet = alphabet;
        reshuffle();
    }

    void reshuffle() {
        randomList.clear();

        List<AlphabetCharacter> shuffleList = new ArrayList<AlphabetCharacter>();
        for (AlphabetCharacter character : alphabet) {
            shuffleList.add(character);
        }

        while (!shuffleList.isEmpty())
            randomList.add(shuffleList.remove(random.nextInt(shuffleList.size())));
    }

    public Iterator<AlphabetCharacter> iterator() {
        return randomList.iterator();
    }

    public int size() {
        return randomList.size();
    }
}
