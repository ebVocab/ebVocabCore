package de.ebuchner.sync;

public class Sync {

    private final SyncNode sourceRoot;
    private final SyncNode targetRoot;
    private SyncListener syncListener = new SyncListenerAdapter();

    public Sync(SyncNode sourceRoot, SyncNode targetRoot) {
        this.sourceRoot = sourceRoot;
        this.targetRoot = targetRoot;
    }

    public void setSyncListener(SyncListener syncListener) {
        this.syncListener = syncListener;
    }

    public void runCompare(SyncNodeComparator comparator) {
        syncListener.onSyncStarted();
        compareImpl(sourceRoot, targetRoot, comparator);
        syncListener.onSyncFinished();
    }

    private void compareImpl(SyncNode source, SyncNode target, SyncNodeComparator comparator) {
        if (source.isContainer() && target.isContainer()) {
            compareContainersImpl(source.asContainer(), target.asContainer(), comparator);
            syncListener.onNodeContainersCompare(source.asContainer(), target.asContainer());
        }
        else
            syncListener.onNodesCompare(source, target, comparator.compareNodes(source, target));
    }

    private void compareContainersImpl(SyncNodeContainer source, SyncNodeContainer target, SyncNodeComparator comparator) {
        for (String sourceToken : source.childTokens()) {
            if (target.findChildWithToken(sourceToken) == null) {
                syncListener.onSourceOnly(source.findChildWithToken(sourceToken));
            } else {
                compareImpl(
                        source.findChildWithToken(sourceToken),
                        target.findChildWithToken(sourceToken),
                        comparator
                );
            }
        }

        for (String targetToken : target.childTokens()) {
            if (source.findChildWithToken(targetToken) == null) {
                syncListener.onTargetOnly(target.findChildWithToken(targetToken));
            }
        }
    }
}
