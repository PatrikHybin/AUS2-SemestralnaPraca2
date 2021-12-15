package com;

public interface IRecord<T> extends Comparable<T> {

    public byte[] toByteArray();
    public void fromByteArray(byte[] array);
    public int getSize();
    public T createClass();
    public boolean isValid();
    public void setValid(boolean valid);
    public long getAddress();
    public void setAddress(long address);
}
