package de.ebuchner.vocab.config.fields;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FieldFactory {

    public static final String ID = "ID";
    public static final String FOREIGN = "Foreign";
    public static final String TRANSLITERATION = "Transliteration";
    public static final String TYPE = "Type";
    public static final String USER = "User";
    public static final String COMMENT = "Comment";
    private static List<GenericField> FIELD_LIST = Arrays.asList(
            new GenericField(
                    /*name*/ FOREIGN,
                    /*editable*/ true,
                    /*optional*/ false,
                    /*multiLine*/ false,
                    /*active*/ true,
                    /*practiceHidden*/ false,
                    /*practiceReverseHidden:*/ true,
                    /*internal:*/ false
            ),
            new GenericField(
                    /*name*/ TRANSLITERATION,
                    /*editable*/ true,
                    /*optional*/ true,
                    /*multiLine*/ false,
                    /*active*/ false,
                    /*practiceHidden*/ true,
                    /*practiceReverseHidden:*/ true,
                    /*internal:*/ false
            ),
            new GenericField(
                    /*name*/ TYPE,
                    /*editable*/ true,
                    /*optional*/ true,
                    /*multiLine*/ false,
                    /*active*/ true,
                    /*practiceHidden*/ true,
                    /*practiceReverseHidden:*/ true,
                    /*internal:*/ false
            ),
            new GenericField(
                    /*name*/ USER,
                    /*editable*/ true,
                    /*optional*/ false,
                    /*multiLine*/ false,
                    /*active*/ true,
                    /*practiceHidden*/ true,
                    /*practiceReverseHidden:*/ false,
                    /*internal:*/ false
            ),
            new GenericField(
                    /*name*/ COMMENT,
                    /*editable*/ true,
                    /*optional*/ true,
                    /*multiLine*/ true,
                    /*active*/ true,
                    /*practiceHidden*/ true,
                    /*practiceReverseHidden:*/ true,
                    /*internal:*/ false
            )
    );

    private FieldFactory() {

    }

    public static List<Field> getGenericFieldList() {
        List<Field> fieldList = new ArrayList<Field>();
        fieldList.addAll(FIELD_LIST);
        return fieldList;
    }

    public static List<String> getGenericFieldNameList() {
        List<String> nameList = new ArrayList<String>();
        for (Field field : getGenericFieldList()) {
            nameList.add(field.name());
        }
        return nameList;
    }

    public static Field getGenericField(String name) {
        for (Field field : FIELD_LIST) {
            if (field.name().equals(name))
                return field;
        }

        throw new IllegalArgumentException("No field with found with /*name*/ " + name);
    }
}
