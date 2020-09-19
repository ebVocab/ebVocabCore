package de.ebuchner.vocab.model.core;

public class ModelChangeEvent implements ModelEvent {
    private AbstractModel source;

    public ModelChangeEvent(AbstractModel source) {
        this.source = source;
    }

    public AbstractModel getSource() {
        return source;
    }

}
