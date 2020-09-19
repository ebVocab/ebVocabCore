package de.ebuchner.vocab.model.cloud;

import de.ebuchner.vocab.config.ProjectInfo;

public interface CloudWindowBehaviour {
    String getUserName();

    String getSecret();

    void updateResult(ProjectInfo projectInfo, CloudResult cloudResult);

    boolean confirmOverwrite(CloudTransfer cloudTransfer);
}
