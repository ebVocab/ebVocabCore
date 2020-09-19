package de.ebuchner.vocab.model.nui;

import de.ebuchner.toolbox.lang.Equals;
import de.ebuchner.toolbox.lang.HashCode;
import de.ebuchner.vocab.model.editor.EditorWindowParameter;
import de.ebuchner.vocab.tools.AvailableCheck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WindowType {

    public static final WindowType EDITOR_WINDOW = new WindowType(
            "editor",
            "de.ebuchner.vocab.{0}.editor.EditorWindow",
            AvailableCheck.ALWAYS_AVAILABLE,
            MaxInstances.MULTIPLE,
            ExtraKeyboard.ENABLED,
            MenuType.FILE,
            TextFieldContextMenu.OFF
    ) {
        @Override
        public List<NuiWindowParameter> nuiWindowParameters() {
            return Arrays.asList(
                    new EditorWindowParameter(EditorWindowParameter.Action.NEW),
                    new EditorWindowParameter(EditorWindowParameter.Action.OPEN)
            );
        }
    };
    /**
     * On Screen Keyboard
     */
    public static final WindowType KEYBOARD_WINDOW = new WindowType(
            "keyboard",
            "de.ebuchner.vocab.{0}.keyboard.KeyboardWindow",
            "de.ebuchner.vocab.model.keyboard.KeyboardAvailable",
            MaxInstances.SINGLE,
            ExtraKeyboard.NONE,
            MenuType.TEXT,
            TextFieldContextMenu.ON
    );

    /**
     * Transliteration
     */
    public static final WindowType TRANSLITERATION_WINDOW = new WindowType(
            "transliteration",
            "de.ebuchner.vocab.{0}.transliteration.TransliterationWindow",
            "de.ebuchner.vocab.model.transliteration.TransliterationAvailable",
            MaxInstances.SINGLE,
            ExtraKeyboard.NONE,
            MenuType.TEXT,
            TextFieldContextMenu.ON
    );
    /**
     * Practice Window
     */
    public static final WindowType PRACTICE_WINDOW = new WindowType(
            "practice",
            "de.ebuchner.vocab.{0}.practice.PracticeWindow",
            AvailableCheck.ALWAYS_AVAILABLE,
            MaxInstances.SINGLE,
            ExtraKeyboard.NONE,
            MenuType.FILE,
            TextFieldContextMenu.OFF
    );
    /**
     * Lesson selection dialog
     */
    public static final WindowType LESSONS_WINDOW = new WindowType(
            "lesson",
            "de.ebuchner.vocab.{0}.lessons.LessonWindow",
            AvailableCheck.ALWAYS_AVAILABLE,
            MaxInstances.SINGLE,
            ExtraKeyboard.NONE,
            MenuType.FILE,
            TextFieldContextMenu.OFF
    );
    /**
     * Font selection dialog
     */
    public static final WindowType FONT_SELECTION_WINDOW = new WindowType(
            "font",
            "de.ebuchner.vocab.{0}.font.FontSelectionWindow",
            AvailableCheck.ALWAYS_AVAILABLE,
            MaxInstances.SINGLE,
            ExtraKeyboard.NONE,
            MenuType.OPTIONS,
            TextFieldContextMenu.OFF
    );
    /**
     * Analysis dialog
     */
    public static final WindowType ANALYSIS_WINDOW = new WindowType(
            "analysis",
            "de.ebuchner.vocab.{0}.analysis.AnalysisWindow",
            AvailableCheck.ALWAYS_AVAILABLE,
            MaxInstances.SINGLE,
            ExtraKeyboard.ENABLED,
            MenuType.TEXT,
            TextFieldContextMenu.ON
    );
    /**
     * Alphabet dialog
     */
    public static final WindowType ALPHABET_WINDOW = new WindowType(
            "alphabet",
            "de.ebuchner.vocab.{0}.alphabet.AlphabetWindow",
            "de.ebuchner.vocab.model.alphabet.AlphabetAvailable",
            MaxInstances.SINGLE,
            ExtraKeyboard.NONE,
            MenuType.TOOLS,
            TextFieldContextMenu.OFF
    );
    /**
     * Currency dialog
     */
    public static final WindowType CURRENCY_WINDOW = new WindowType(
            "currency",
            "de.ebuchner.vocab.{0}.currency.CurrencyWindow",
            AvailableCheck.ALWAYS_AVAILABLE,
            MaxInstances.SINGLE,
            ExtraKeyboard.NONE,
            MenuType.TOOLS,
            TextFieldContextMenu.OFF
    );
    /**
     * Timezone dialog
     */
    public static final WindowType TIMEZONE_WINDOW = new WindowType(
            "timezone",
            "de.ebuchner.vocab.{0}.timezone.TimezoneWindow",
            AvailableCheck.ALWAYS_AVAILABLE,
            MaxInstances.SINGLE,
            ExtraKeyboard.NONE,
            MenuType.TOOLS,
            TextFieldContextMenu.OFF
    );
    /**
     * About dialog
     */
    public static final WindowType ABOUT_WINDOW = new WindowType(
            "about",
            "de.ebuchner.vocab.{0}.about.AboutWindow",
            AvailableCheck.ALWAYS_AVAILABLE,
            MaxInstances.SINGLE,
            ExtraKeyboard.NONE,
            MenuType.HELP,
            TextFieldContextMenu.OFF
    );
    /**
     * Search dialog
     */
    public static final WindowType SEARCH_WINDOW = new WindowType(
            "search",
            "de.ebuchner.vocab.{0}.search.SearchWindow",
            AvailableCheck.ALWAYS_AVAILABLE,
            MaxInstances.SINGLE,
            ExtraKeyboard.ENABLED,
            MenuType.SEARCH,
            TextFieldContextMenu.ON
    );
    /*
     * Project selection
     */
    public static final WindowType PROJECT_WINDOW = new WindowType(
            "project",
            "de.ebuchner.vocab.{0}.project.ProjectWindow",
            AvailableCheck.ALWAYS_AVAILABLE,
            MaxInstances.SINGLE,
            ExtraKeyboard.NONE,
            MenuType.FILE,
            TextFieldContextMenu.OFF
    );

    public static final WindowType CLOUD_LOGIN_WINDOW = new WindowType(
            "cloudLogin",
            "de.ebuchner.vocab.{0}.cloud.CloudLoginWindow",
            AvailableCheck.ALWAYS_AVAILABLE,
            MaxInstances.SINGLE,
            ExtraKeyboard.NONE,
            MenuType.NONE,
            TextFieldContextMenu.OFF
    );

    /**
     * Cloud dialog
     */
    public static final WindowType CLOUD_WINDOW = new WindowType(
            "cloud",
            "de.ebuchner.vocab.{0}.cloud.CloudWindow",
            AvailableCheck.ALWAYS_AVAILABLE,
            MaxInstances.SINGLE,
            ExtraKeyboard.NONE,
            MenuType.FILE,
            TextFieldContextMenu.OFF
    );

    public static final WindowType REPETITION_LOAD_WINDOW = new WindowType(
            "repetitionLoad",
            "de.ebuchner.vocab.{0}.practice.RepetitionLoadWindow",
            AvailableCheck.ALWAYS_AVAILABLE,
            MaxInstances.SINGLE,
            ExtraKeyboard.NONE,
            MenuType.HIDDEN,
            TextFieldContextMenu.OFF
    );

    public static final WindowType ALL_TYPES[] = new WindowType[]{
            PROJECT_WINDOW,
            LESSONS_WINDOW,
            EDITOR_WINDOW,
            PRACTICE_WINDOW,
            FONT_SELECTION_WINDOW,
            KEYBOARD_WINDOW,
            TRANSLITERATION_WINDOW,
            SEARCH_WINDOW,
            ANALYSIS_WINDOW,
            ALPHABET_WINDOW,
            CURRENCY_WINDOW,
            TIMEZONE_WINDOW,
            ABOUT_WINDOW,
            CLOUD_WINDOW
    };

    private String token;
    private String windowClassName;
    private MaxInstances maxInstances;
    private ExtraKeyboard extraKeyboard;
    private AvailableCheck availableCheck;
    private MenuType menuType;
    private TextFieldContextMenu textFieldContextMenu;

    private WindowType(
            String token,
            String windowClassName,
            AvailableCheck availableCheck,
            MaxInstances maxInstances,
            ExtraKeyboard extraKeyboard,
            MenuType menuType,
            TextFieldContextMenu textFieldContextMenu
    ) {
        this.token = token;
        this.windowClassName = windowClassName;
        this.availableCheck = availableCheck;
        this.maxInstances = maxInstances;
        this.extraKeyboard = extraKeyboard;
        this.menuType = menuType;
        this.textFieldContextMenu = textFieldContextMenu;
    }

    private WindowType(
            String token,
            String windowClassName,
            String availableCheckClassName,
            MaxInstances maxInstances,
            ExtraKeyboard extraKeyboard,
            MenuType menuType,
            TextFieldContextMenu textFieldContextMenu
    ) {
        this(
                token,
                windowClassName,
                instantiateAvailableCheck(availableCheckClassName),
                maxInstances,
                extraKeyboard,
                menuType,
                textFieldContextMenu
        );
    }

    // test only
    public WindowType(String windowClassName) {
        this.windowClassName = windowClassName;
        this.maxInstances = MaxInstances.SINGLE;
    }

    private static AvailableCheck instantiateAvailableCheck(String availableCheckClassName) {
        try {
            return (AvailableCheck) Class.forName(availableCheckClassName).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<WindowType> getAvailableWindowTypes(MenuType menuType, WindowTypeFilter filter) {
        List<WindowType> windowTypes = new ArrayList<WindowType>();
        for (WindowType windowType : ALL_TYPES) {
            if (!filter.accept(windowType))
                continue;

            if (!windowType.isAvailable())
                continue;

            if (windowType.getMenuType() == menuType)
                windowTypes.add(windowType);
        }
        return windowTypes;
    }

    public static List<WindowType> getAvailableWindowTypes(WindowTypeFilter filter) {
        List<WindowType> windowTypes = new ArrayList<WindowType>();
        for (WindowType windowType : ALL_TYPES) {
            if (!filter.accept(windowType))
                continue;

            if (!windowType.isAvailable())
                continue;

            windowTypes.add(windowType);
        }
        return windowTypes;
    }

    public boolean isAvailable() {
        return availableCheck == null || availableCheck.isAvailable();
    }

    public TextFieldContextMenu getTextFieldContextMenu() {
        return textFieldContextMenu;
    }

    public String getToken() {
        return token;
    }

    public ExtraKeyboard getExtraKeyboard() {
        return extraKeyboard;
    }

    public String getWindowClassName() {
        return windowClassName;
    }

    public MaxInstances getMaxInstances() {
        return maxInstances;
    }

    public MenuType getMenuType() {
        return menuType;
    }

    @Override
    public boolean equals(Object o) {
        Equals equals = new Equals(this);
        return equals.compareWith(o);
    }

    @Override
    public int hashCode() {
        HashCode hashCode = new HashCode(this);
        hashCode.addObject(windowClassName);
        return hashCode.getResult();
    }

    public List<NuiWindowParameter> nuiWindowParameters() {
        return Collections.singletonList(NuiWindowParameter.EMPTY);
    }

    public enum MaxInstances {
        SINGLE, MULTIPLE
    }

    public enum MenuType {
        FILE, SEARCH, TEXT, TOOLS, OPTIONS, NONE, HIDDEN, HELP
    }

    public enum ExtraKeyboard {
        ENABLED, NONE
    }

    public enum TextFieldContextMenu {
        ON, OFF
    }
}
