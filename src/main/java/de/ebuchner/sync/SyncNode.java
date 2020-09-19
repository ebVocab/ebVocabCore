package de.ebuchner.sync;

public interface SyncNode {

    boolean isContainer();

    SyncNodeContainer asContainer();

    SyncNode getParentNode();
}
