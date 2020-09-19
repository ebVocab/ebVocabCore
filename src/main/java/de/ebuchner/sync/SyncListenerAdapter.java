package de.ebuchner.sync;

public class SyncListenerAdapter implements SyncListener {
    @Override
    public void onNodesCompare(SyncNode source, SyncNode target, SyncNodeComparator.ResultType resultType) {

    }

    @Override
    public void onSourceOnly(SyncNode source) {

    }

    @Override
    public void onTargetOnly(SyncNode target) {

    }

    @Override
    public void onSyncStarted() {

    }

    @Override
    public void onSyncFinished() {

    }

    @Override
    public void onNodeContainersCompare(SyncNodeContainer source, SyncNodeContainer target) {

    }
}
