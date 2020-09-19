package de.ebuchner.vocab.model.practice;

public abstract class RandomCategorizedPracticeStrategy extends RandomPracticeStrategy {
    @Override
    protected boolean isCacheVocabList() {
        // if randomize is dependant on age or frequency it makes no sense to save the whole
        // list, just save the position (approx. the same)
        return false;
    }
}
