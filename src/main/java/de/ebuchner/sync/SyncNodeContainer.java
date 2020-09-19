package de.ebuchner.sync;

import java.util.List;

public interface SyncNodeContainer extends SyncNode {

    List<? extends SyncNode> getChildren();

    Iterable<String> childTokens();

    SyncNode findChildWithToken(String token);
}
