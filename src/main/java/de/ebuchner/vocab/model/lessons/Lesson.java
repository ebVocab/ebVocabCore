package de.ebuchner.vocab.model.lessons;

import java.io.File;

public class Lesson {

    private LessonContainer parent;
    private File fileRef;
    private LessonState state = LessonState.UN_SELECTED;

    public Lesson(LessonContainer parent, File fileRef) {
        if (fileRef == null)
            throw new IllegalArgumentException("parameter fileRef may not be null");
        this.parent = parent;
        this.fileRef = fileRef;
    }

    protected Lesson(LessonContainer parent, File fileRef, LessonState state) {
        this.parent = parent;
        this.fileRef = fileRef;
        this.state = state;
    }

    public static Lesson createFrom(LessonContainer newParent, Lesson other) {
        return new Lesson(
                newParent,
                other.getFileRef(),
                other.getState()
        );
    }

    public File getFileRef() {
        return fileRef;
    }

    public LessonState getState() {
        return state;
    }

    public void setState(LessonState state) {
        this.state = state;
    }

    public LessonContainer getParent() {
        return parent;
    }

    public String getName() {
        String name = fileRef.getName();
        if (!name.contains("."))
            return name;
        return name.substring(0, name.lastIndexOf("."));
    }

    @Override
    public int hashCode() {
        return fileRef.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Lesson))
            return false;
        return fileRef.equals(((Lesson) o).getFileRef());
    }

    public boolean isContainer() {
        return false;
    }

    public LessonContainer asContainer() {
        if (!isContainer())
            throw new UnsupportedOperationException("not a container");
        return (LessonContainer) this;
    }

    @Override
    public String toString() {
        return "Lesson " + getName();
    }

}
