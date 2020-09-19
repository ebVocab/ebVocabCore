package de.ebuchner.vocab.model.analysis;

import de.ebuchner.vocab.model.VocabBaseController;

public class AnalysisController extends VocabBaseController {

    private AnalysisWindowBehaviour analysisWindow;

    public AnalysisController(AnalysisWindowBehaviour analysisWindow) {
        this.analysisWindow = analysisWindow;
    }

    public void onConvert(String input) {

        StringBuilder result = new StringBuilder();

        if (input != null && input.trim().length() > 0) {
            for (char c : input.trim().toCharArray()) {
                result.append(c).append(' ');
            }
        }
        analysisWindow.showResult(result.toString().trim());

        analysisWindow.showUnicode(toUnicode(input));
    }

    private String toUnicode(String input) {
        final String UNICODE_PREFIX = "\\u";
        StringBuilder output = new StringBuilder();

        if (input != null) {
            for (char c : input.trim().toCharArray()) {
                if (c == ' ') {
                    output.append(" -");
                    continue;
                }
                String hexValue = Integer.toHexString(c);
                if (output.length() > 0)
                    output.append(' ');
                output.append(UNICODE_PREFIX);
                for (int i = 0; i < 4 - hexValue.length(); i++)
                    output.append('0');
                output.append(hexValue);
            }
        }
        return output.toString().trim();
    }
}
