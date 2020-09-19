package de.ebuchner.vocab.model.cloud;

import de.ebuchner.vocab.config.preferences.PasswordValue;
import de.ebuchner.vocab.config.preferences.PreferenceValueList;
import de.ebuchner.vocab.config.preferences.StringValue;
import de.ebuchner.vocab.model.VocabModel;
import de.ebuchner.vocab.model.commands.SimpleCommand;
import de.ebuchner.vocab.model.core.AbstractModel;
import de.ebuchner.vocab.model.core.ModelCommandManagerClearedEvent;
import de.ebuchner.vocab.model.lessons.LessonModel;

public class CloudModel extends AbstractModel<SimpleCommand, CloudModelListener> {
    private static final String LAST_USER_NAME = "LastUserName";
    private static final String LAST_PASSWORD = "LastPassword";
    private static final String LAST_SERVER = "LastServer";
    private long lastUpload = 0L;
    private String lastUserName;
    private String lastPassword;
    private String lastServer;

    public CloudModel() {
        final LessonModel lessonModel = LessonModel.getOrCreateLessonModel();
        this.addListener(new CloudModelListener() {
            @Override
            public void cloudModelChanged() {
                lessonModel.reSynchronize();
            }

            @Override
            public void modelCommandManagerCleared(ModelCommandManagerClearedEvent event) {

            }
        });
    }

    public static CloudModel getOrCreateCloudModel() {
        return (CloudModel) VocabModel.getInstance().getOrCreateModel(CloudModel.class);
    }

    public String getLastPassword() {
        return lastPassword;
    }

    public void setLastPassword(String lastPassword) {
        this.lastPassword = lastPassword;
    }

    public String getLastUserName() {
        return lastUserName;
    }

    public void setLastUserName(String lastUserName) {
        this.lastUserName = lastUserName;
    }

    @Override
    protected void fireModelChanged() {

    }

    @Override
    public void reSynchronize() {
    }

    public void restoreFromPreferences(PreferenceValueList preferences) {

        lastPassword = null;
        lastUserName = null;
        lastServer = null;

        StringValue lastUserNameValue = (StringValue) preferences.getName(CloudModel.class, LAST_USER_NAME);
        if (lastUserNameValue != null)
            lastUserName = lastUserNameValue.getString();

        PasswordValue lastPasswordValue = (PasswordValue) preferences.getName(CloudModel.class, LAST_PASSWORD);
        if (lastPasswordValue != null)
            lastPassword = lastPasswordValue.asUnencryptedString();

        StringValue lastServerValue = (StringValue) preferences.getName(CloudModel.class, LAST_SERVER);
        if (lastServerValue != null)
            lastServer = lastServerValue.getString();
    }

    public void saveToPreferences(PreferenceValueList preferences) {
            preferences.putName(CloudModel.class, LAST_USER_NAME, new StringValue(nullSafe(lastUserName)));
            PasswordValue passwordValue = new PasswordValue();
            passwordValue.setValueFromUnencrypted(nullSafe(lastPassword));
            preferences.putName(CloudModel.class, LAST_PASSWORD, passwordValue);
            preferences.putName(CloudModel.class, LAST_SERVER, new StringValue(nullSafe(lastServer)));
    }

    private String nullSafe(String value) {
        if (value == null)
            return "";
        return value;
    }

    public void uploadFinished() {
        lastUpload = System.currentTimeMillis();
    }

    public long getLastUpload() {
        return lastUpload;
    }

    public void localFilesChanged() {
        // avoid ConcurrentModificationException since LessonModels are listeners and create new models on the way
        CloudModelListener[] listenersArray = listeners.toArray(new CloudModelListener[listeners.size()]);
        for (CloudModelListener aListenersArray : listenersArray) {
            aListenersArray.cloudModelChanged();
        }
    }

    public String getLastServer() {
        return lastServer;
    }

    public void setLastServer(String lastServer) {
        this.lastServer = lastServer;
    }

    public boolean hasValues() {
        return !nullSafe(lastServer).isEmpty();
    }

    public CloudAccessParameters createAccessParameters() {
        CloudAccessParameters parameters = new CloudAccessParameters();
        parameters.serverUrl = getLastServer();
        parameters.userName = getLastUserName();
        parameters.secret = getLastPassword();
        return parameters;
    }
}
