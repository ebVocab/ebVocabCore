package de.ebuchner.vocab.model.nui.platform;

import de.ebuchner.vocab.model.io.ImageEncoderBehaviour;
import de.ebuchner.vocab.model.project.ProjectBean;
import de.ebuchner.vocab.nui.NuiDirector;

class SimpleBatchPlatform implements UIPlatform {
    @Override
    public UIPlatformType getType() {
        return UIPlatformType.BATCH;
    }

    @Override
    public void initializeUISystem() {

    }

    @Override
    public ImageEncoderBehaviour newImageEncoder() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String windowClassName(String windowID) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String uiRuntimeName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NuiDirector getNuiDirector() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ProjectBean getProjectBean() {
        throw new UnsupportedOperationException();
    }
}
