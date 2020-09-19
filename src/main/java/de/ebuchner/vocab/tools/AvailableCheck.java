package de.ebuchner.vocab.tools;

public interface AvailableCheck {

    AvailableCheck ALWAYS_AVAILABLE = new AvailableCheck() {
        public boolean isAvailable() {
            return true;
        }
    };

    boolean isAvailable();
}
