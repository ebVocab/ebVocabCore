package de.ebuchner.vocab.config;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VocabEnvironment {

    // also upgrade AndroidManifest!
    public static final String APP_VERSION = "1.2.7";
    public static final String APP_TITLE = "ebVocab " + APP_VERSION;
    public static final String APP_WEBSITE = Keys.getKey("about.website");
    public static final String APP_TITLE_SHORT = "ebVocab";

    private VocabEnvironment() {

    }

    public static String javaRuntimeName() {
        return System.getProperty("java.version");
    }

    public static String os() {
        return System.getProperty("os.name");
    }

    public static OSType osType() {
        if (os().toLowerCase().contains("windows"))
            return OSType.WINDOWS;
        else if (os().toLowerCase().contains("linux"))
            return OSType.LINUX;

        return OSType.UNKNOWN;
    }

    public static String cmdLine(String cmd) {
        return cmdLine(cmd, false);
    }

    public static String cmdLine(String cmd, boolean quote) {
        if (quote)
            cmd = "\"" + cmd + "\"";
        String cmdLine = cmd;

        switch (osType()) {
            case WINDOWS:
                cmdLine = "cmd /c start " + cmd;
                break;
            case LINUX:
                cmdLine = cmd;
                break;
        }
        System.out.println(cmdLine);
        return cmdLine;
    }

    public static void openBrowser(URL url) throws IOException {
        List<String> commands = new ArrayList<>();
        switch (osType()) {
            case WINDOWS:
                commands.addAll(Arrays.asList("cmd", "/c", "start"));
                break;
            case LINUX:
                commands.add("firefox");
                break;
        }

        commands.add(url.toExternalForm());

        new ProcessBuilder(commands).start();
    }

    public enum OSType {
        WINDOWS, LINUX, UNKNOWN
    }
}
