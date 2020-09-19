package de.ebuchner.vocab.model.cloud;

public class CloudAccessFactory {

    public static CloudAccess createNew() {
        return new CloudAccessOkHttp();
    }

}
