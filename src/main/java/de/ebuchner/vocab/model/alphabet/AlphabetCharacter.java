package de.ebuchner.vocab.model.alphabet;

public class AlphabetCharacter {
    private final String character;
    private final String description;

    public AlphabetCharacter(String character, String description) {
        this.character = character;
        this.description = description;
    }

    public String getCharacter() {
        return character;
    }

    public String getDescription() {
        return description;
    }
}
