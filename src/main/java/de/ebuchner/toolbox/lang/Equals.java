package de.ebuchner.toolbox.lang;

public class Equals {
    Class ownerClass;
    int ownerHashCode;

    public Equals(Object owner) {
        assert owner != null;
        this.ownerClass = owner.getClass();
        this.ownerHashCode = owner.hashCode();
    }

    public boolean compareWith(Object target) {
        if (target == null)
            return false;

        if (target.getClass().equals(ownerClass))
            return ownerHashCode == target.hashCode();

        return false;
    }

}
