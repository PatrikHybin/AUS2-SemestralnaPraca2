package data;

import java.io.*;

public class BTreeNode<T extends IRecord<T>> implements IRecord<BTreeNode<T>> {
    private static long nullValue = -100;

    private boolean isValid = true;
    private long parent = nullValue;
    private long leftSon = nullValue;
    private long centerSon = nullValue;
    private long rightSon = nullValue;
    //private T leftData;
    private long leftData = nullValue;
    //private T rightData;
    private long rightData = nullValue;
    private Class<T> gClass;

    private long address = nullValue;

    public BTreeNode() {

    }

    public BTreeNode(Class<T> gClass) {
        this.gClass = gClass;
    }

    public BTreeNode(T data, Class<T> gClass) {
        this.leftData = data.getAddress();
        this.gClass = gClass;
    }


    public boolean isLeaf() {
        return leftSon == nullValue && centerSon == nullValue && rightSon == nullValue;
    }

    public long getParent() {
        return parent;
    }

    public void setParent(long parent) {
        this.parent = parent;
    }

    public long getLeftSon() {
        return leftSon;
    }

    public void setLeftSon(long leftSon) {
        this.leftSon = leftSon;
    }

    public long getRightSon() {
        return rightSon;
    }

    public void setRightSon(long rightSon) {
        this.rightSon = rightSon;
    }

    public long getCenterSon() {
        return centerSon;
    }

    public void setCenterSon(long centerSon) {
        this.centerSon = centerSon;
    }

    public long getLeftData() {
        return leftData;
    }

    public void setLeftData(long leftData) {
        this.leftData = leftData;
    }

    public long getRightData() {
        return rightData;
    }

    public void setRightData(long rightData) {
        this.rightData = rightData;
    }

    public long getAddress() {
        return address;
    }

    public void setAddress(long address) {
        this.address = address;
    }

    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        try {
            dataOutputStream.writeBoolean(this.isValid);
            dataOutputStream.writeLong(this.parent);

            dataOutputStream.writeLong(this.leftSon);
            dataOutputStream.writeLong(this.centerSon);
            dataOutputStream.writeLong(this.rightSon);

            dataOutputStream.writeLong(this.leftData);
            dataOutputStream.writeLong(this.rightData);

            dataOutputStream.writeLong(this.address);

            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] array) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(array);
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        try {
            this.isValid = dataInputStream.readBoolean();
            this.parent = dataInputStream.readLong();

            this.leftSon = dataInputStream.readLong();
            this.centerSon = dataInputStream.readLong();
            this.rightSon = dataInputStream.readLong();

            this.leftData = dataInputStream.readLong();
            this.rightData = dataInputStream.readLong();

            this.address = dataInputStream.readLong();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getSize() {
        return 1 + 7 * Long.BYTES;
    }

    @Override
    public BTreeNode<T> createClass() {
        return new BTreeNode<>();
    }


    @Override
    public boolean isValid() {
        return this.isValid;
    }

    @Override
    public void setValid(boolean valid) {
        this.isValid = valid;
    }

    @Override
    public int compareTo(BTreeNode<T> o) {
        return 0;
    }

    @Override
    public String toString() {
        return "BTreeNode " + "parent=" + parent + ", leftSon=" + leftSon + ", centerSon=" + centerSon + ", rightSon=" + rightSon + ", leftData=" + leftData + ", rightData=" + rightData + ", address=" + address;
    }

    public String[] data() {
        return new String[] {this.parent+"",this.leftSon+"",this.centerSon+"",this.rightSon+"",this.leftData+"",this.rightData+"",this.address+"",this.isValid+""};
    }
}
