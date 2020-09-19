package de.ebuchner.vocab.model.nui.platform;

import de.ebuchner.vocab.model.io.ImageEncoderBehaviour;
import de.ebuchner.vocab.model.project.ProjectBean;
import de.ebuchner.vocab.nui.NuiDirector;

public interface UIPlatform {

    UIPlatformType getType();

    void initializeUISystem();

    ImageEncoderBehaviour newImageEncoder();

    String windowClassName(String windowID);

    String uiRuntimeName();

    NuiDirector getNuiDirector();

    ProjectBean getProjectBean();
}
