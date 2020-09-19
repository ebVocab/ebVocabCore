package de.ebuchner.vocab.model.nui.platform;

import java.util.logging.Level;
import java.util.logging.Logger;

public class UIPlatformFactory {

    public static final String PLATFORM_PROPERTY = UIPlatformFactory.class.getName() + ".platform";
    public static final String PLATFORM_PROPERTY_FX = "FX";
    private static final String MOBILE_UI_PLATFORM = "de.ebuchner.vocab.mobile.platform.MobileUIPlatform";
    private static final String WEB_UI_PLATFORM = "de.ebuchner.vocab.web.platform.WebUIPlatform";
    private static final String FX_UI_PLATFORM = "de.ebuchner.vocab.fx.platform.FxUIPlatform";
    private static final Logger LOGGER = Logger.getLogger(UIPlatformFactory.class.getName());
    private static UIPlatform platform = null;

    private UIPlatformFactory() {

    }

    public static UIPlatform getUIPlatform() {
        if (platform == null) {
            if (PLATFORM_PROPERTY_FX.equals(System.getProperty(PLATFORM_PROPERTY))) {
                try {
                    platform = (UIPlatform) Class.forName(FX_UI_PLATFORM).newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                    try {
                        platform = (UIPlatform) Class.forName(MOBILE_UI_PLATFORM).newInstance();
                    } catch (Exception e2) {
                        try {
                            platform = (UIPlatform) Class.forName(WEB_UI_PLATFORM).newInstance();
                        } catch (Exception e3) {
                            LOGGER.log(Level.WARNING, "No UI platform found. Using batch platform");
                            return new SimpleBatchPlatform();
                        }
                    }
            }
            platform.initializeUISystem();
        }
        return platform;
    }
}
