package de.ebuchner.toolbox.lang;

public class HashCode {

    private int c;
    private int result;

    public HashCode(Object owner) {
        this.c = 0;
        this.result = owner.getClass().hashCode();
    }

    public void addBoolean(boolean value) {
        c = c + (value ? 1 : 0);
    }

    public void addBooleanArray(boolean[] values) {
        if (values == null)
            return;
        HashCode hc = new HashCode(values);
        for (boolean value : values)
            hc.addBoolean(value);
        c = c + hc.getResult();
    }

    public void addByte(byte value) {
        c = c + (int) value;
    }

    public void addByteArray(byte[] values) {
        if (values == null)
            return;
        HashCode hc = new HashCode(values);
        for (byte value : values)
            hc.addByte(value);
        c = c + hc.getResult();
    }

    public void addChar(char value) {
        c = c + (int) value;
    }

    public void addCharArray(char[] values) {
        if (values == null)
            return;
        HashCode hc = new HashCode(values);
        for (char value : values)
            hc.addChar(value);
        c = c + hc.getResult();
    }

    public void addShort(short value) {
        c = c + (int) value;
    }

    public void addShortArray(short[] values) {
        if (values == null)
            return;
        HashCode hc = new HashCode(values);
        for (short value : values)
            hc.addShort(value);
        c = c + hc.getResult();
    }

    public void addInt(int value) {
        c = c + value;
    }

    public void addIntArray(int[] values) {
        if (values == null)
            return;
        HashCode hc = new HashCode(values);
        for (int value : values)
            hc.addInt(value);
        c = c + hc.getResult();
    }

    public void addLong(long value) {
        c = c + (int) (value ^ (value >>> 32));
    }

    public void addLongArray(long[] values) {
        if (values == null)
            return;
        HashCode hc = new HashCode(values);
        for (long value : values)
            hc.addLong(value);
        c = c + hc.getResult();
    }

    public void addFloat(float value) {
        c = c + Float.floatToIntBits(value);
    }

    public void addFloatArray(float[] values) {
        if (values == null)
            return;
        HashCode hc = new HashCode(values);
        for (float value : values)
            hc.addFloat(value);
        c = c + hc.getResult();
    }

    public void addDouble(double value) {
        addLong(Double.doubleToLongBits(value));
    }

    public void addDoubleArray(double[] values) {
        if (values == null)
            return;
        HashCode hc = new HashCode(values);
        for (double value : values)
            hc.addDouble(value);
        c = c + hc.getResult();
    }

    public void addObject(Object value) {
        if (value != null)
            c = c + value.hashCode();
    }

    public void addObjectArray(Object[] values) {
        if (values == null)
            return;
        HashCode hc = new HashCode(values);
        for (Object value : values)
            hc.addObject(value);
        c = c + hc.getResult();
    }

    public int getResult() {
        return 37 * result + c;
    }
}
