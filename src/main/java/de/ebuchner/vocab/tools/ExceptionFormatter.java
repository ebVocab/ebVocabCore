package de.ebuchner.vocab.tools;

public class ExceptionFormatter {

    private ExceptionFormatter() {

    }

    public static String detailsOf(Throwable throwable) {
        final String nl = System.getProperty("line.separator");
        StringBuilder details = new StringBuilder();
        while (throwable != null) {
            details.append(throwable.toString()).append(nl);
            for (StackTraceElement element : throwable.getStackTrace()) {
                details.append("  ")
                        .append(element.getClassName())
                        .append(".")
                        .append(element.getMethodName());
                if (element.getLineNumber() > 0)
                    details.append(":").append(element.getLineNumber());
                details.append(nl);
            }
            throwable = throwable.getCause();
            if (throwable != null)
                details.append("Caused by: ");
        }
        return details.toString();
    }
}