package de.ebuchner.vocab.model;

import de.ebuchner.vocab.config.Config;
import de.ebuchner.vocab.config.preferences.PreferencesSupport;
import de.ebuchner.vocab.model.core.AbstractModel;

import java.util.HashMap;
import java.util.Map;

public final class VocabModel {

    private static VocabModel model = new VocabModel();
    private Map<String, AbstractModel> modelCache = new HashMap<String, AbstractModel>();

    public static VocabModel getInstance() {
        return model;
    }

    public final AbstractModel getOrCreateModel(Class<? extends AbstractModel> modelClass) {
        Config config = Config.instance();
        String cacheKey = cacheKey(modelClass);
        if (!modelCache.containsKey(cacheKey)) {
            AbstractModel abstractModel;
            try {
                abstractModel = modelClass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (Config.projectInitialized())
                abstractModel.restoreFromPreferences(config.preferences().getPreferenceValueList());
            abstractModel.reSynchronize();
            modelCache.put(cacheKey, abstractModel);
        }
        return modelCache.get(cacheKey);
    }

    private String cacheKey(Class<? extends AbstractModel> modelName) {
        return modelName.getName();
    }

    public void savePreferences() {
        // on selecting a new project after very first start
        if (!Config.projectInitialized())
            return;

        Config config = Config.instance();
        for (PreferencesSupport support : modelCache.values()) {
            support.saveToPreferences(config.preferences().getPreferenceValueList());
        }
    }

    public void shutDown() {
        savePreferences();
        modelCache.clear();
    }
}
