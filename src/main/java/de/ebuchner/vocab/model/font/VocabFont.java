package de.ebuchner.vocab.model.font;

import de.ebuchner.toolbox.lang.Equals;
import de.ebuchner.toolbox.lang.HashCode;

public class VocabFont {

    private String name;
    private VocabFontStyle style;
    private int size;

    public VocabFont(String name, VocabFontStyle style, int size) {
        this.name = name;
        this.style = style;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public VocabFontStyle getStyle() {
        return style;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "Font " + name + " style=" + style.name() + " size=" + size;
    }

    @Override
    public int hashCode() {
        HashCode hashCode = new HashCode(this);
        hashCode.addObject(name);
        hashCode.addObject(style);
        hashCode.addInt(size);
        return hashCode.getResult();
    }

    @Override
    public boolean equals(Object o) {
        Equals equals = new Equals(this);
        return equals.compareWith(o);
    }
}
