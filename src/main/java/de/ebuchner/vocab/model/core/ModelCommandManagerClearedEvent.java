package de.ebuchner.vocab.model.core;

public class ModelCommandManagerClearedEvent implements ModelEvent {
    private AbstractModel model;

    public ModelCommandManagerClearedEvent(AbstractModel model) {
        this.model = model;
    }

    public AbstractModel getModel() {
        return model;
    }
}
