package com;
import java.io.*;
import java.util.Random;

public class TestClass implements IRecord<TestClass> {

    private boolean isValid = true;
    private String name;
    private int nameLength;
    private final static int NAME_LENGTH = 20;

    private String surname;
    private int surnameLength;
    private final static int SURNAME_LENGTH = 20;

    private int number;
    private double doubleNum;

    public TestClass(String name, String surname, int number, double doubleNum) {
        this.name = name;
        this.nameLength = this.name.length();
        this.surname = surname;
        this.surnameLength = this.surname.length();
        this.number = number;
        this.doubleNum = doubleNum;
    }

    public TestClass() {
        this.name = "";
        this.nameLength = this.name.length();
        this.surname = "";
        this.surnameLength = this.surname.length();
        this.number = new Random().nextInt(100) + 1;
        this.doubleNum = new Random().nextDouble();
    }

    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        try {
            dataOutputStream.writeBoolean(this.isValid);
            String tmp = this.name;
            for (int i = 0; i < NAME_LENGTH - nameLength; i++) {
                tmp += "A";
            }
            dataOutputStream.writeInt(this.nameLength);
            dataOutputStream.writeChars(tmp);

            tmp = this.surname;
            for (int i = 0; i < SURNAME_LENGTH - surnameLength; i++) {
                tmp += "A";
            }

            dataOutputStream.writeInt(this.surnameLength);
            dataOutputStream.writeChars(tmp);

            dataOutputStream.writeInt(this.number);
            dataOutputStream.writeDouble(this.doubleNum);
            return outputStream.toByteArray();
        } catch (IOException e) {
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
            this.name = "";
            this.nameLength = dataInputStream.readInt();
            for (int i = 0; i < NAME_LENGTH; i++) {
                this.name += dataInputStream.readChar() + "";
            }
            this.name = this.name.substring(0, nameLength);

            this.surname = "";
            this.surnameLength = dataInputStream.readInt();
            for (int i = 0; i < SURNAME_LENGTH; i++) {
                this.surname += dataInputStream.readChar() + "";
            }
            this.surname = this.surname.substring(0, surnameLength);

            this.number = dataInputStream.readInt();

            this.doubleNum = dataInputStream.readDouble();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getSize() {
        //isValid + namelength + surnamelength + NAME_LENGTH + SURNAME_LENGTH + number + doubleNum
        return 1 + Integer.BYTES + Integer.BYTES + Character.BYTES * (NAME_LENGTH + SURNAME_LENGTH) + Integer.BYTES + Double.BYTES;
    }

    @Override
    public TestClass createClass() {
        return new TestClass();
    }

    @Override
    public boolean isValid() {
        return isValid;
    }
    @Override
    public void setValid(boolean valid) {
        isValid = valid;
    }

    @Override
    public long getAddress() {
        return 0;
    }

    @Override
    public void setAddress(long address) {

    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getNumber() {
        return number;
    }

    public double getDoubleNum() {
        return doubleNum;
    }

    @Override
    public int compareTo(TestClass o) {
        return 0;
    }
}