package de.ebuchner.sync;

public interface SyncListener {
    void onNodesCompare(SyncNode source, SyncNode target, SyncNodeComparator.ResultType resultType);

    void onSourceOnly(SyncNode source);

    void onTargetOnly(SyncNode target);

    void onSyncStarted();

    void onSyncFinished();

    void onNodeContainersCompare(SyncNodeContainer source, SyncNodeContainer target);
}
