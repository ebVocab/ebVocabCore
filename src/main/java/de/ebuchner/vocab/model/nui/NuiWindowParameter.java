package de.ebuchner.vocab.model.nui;

public interface NuiWindowParameter {

    NuiWindowParameter EMPTY = new NuiWindowParameter() {
        public String getToken() {
            return null;
        }
    };

    String getToken();
}
