package de.ebuchner.sync;

public interface SyncNodeComparator {
    ResultType compareNodes(SyncNode source, SyncNode target);

    enum ResultType {
        SAME, SOURCE_NEEDS_UPDATE, TARGET_NEEDS_UPDATE
    }

}
