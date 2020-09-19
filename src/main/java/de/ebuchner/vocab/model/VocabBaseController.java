package de.ebuchner.vocab.model;

import de.ebuchner.vocab.model.nui.NuiWindow;
import de.ebuchner.vocab.model.nui.NuiWindowParameter;
import de.ebuchner.vocab.model.nui.WindowType;
import de.ebuchner.vocab.model.nui.platform.UIPlatformFactory;

public class VocabBaseController {

    public NuiWindow onOpenWindowType(WindowType windowType) {
        return UIPlatformFactory.getUIPlatform().getNuiDirector().showWindow(windowType);
    }

    public NuiWindow onOpenWindowType(WindowType windowType, NuiWindowParameter parameter) {
        return UIPlatformFactory.getUIPlatform().getNuiDirector().showWindow(windowType, parameter);
    }

}
