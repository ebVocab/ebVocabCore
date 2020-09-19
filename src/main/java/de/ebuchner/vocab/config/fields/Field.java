package de.ebuchner.vocab.config.fields;

public interface Field {

    /**
     * is the field editable in the vocab editor?
     */
    boolean editable();

    /**
     * is it a multi-line field like comment
     */
    boolean multiLine();

    /**
     * is it an optional field, i.e. it may be blank
     */
    boolean optional();

    /**
     * is the field used at all?
     */
    boolean active();

    /**
     * is the field hidden when practicing
     */
    boolean practiceHidden();

    /**
     * is this an internal field (like ID)
     */
    boolean internal();

    /**
     * is the field hidden when practicing in reverse order
     */
    boolean practiceReverseHidden();

    /**
     * The field's technical name, used in serialized form to identify the field
     */
    String name();

}