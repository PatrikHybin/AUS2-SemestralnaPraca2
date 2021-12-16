package data;

import java.io.*;
import java.time.LocalDate;

public class Person implements IRecord<Person> {

    private boolean isValid = true;

    private String name;
    private int nameLength;
    private static final int NAME_LENGTH = 15;

    private String surname;
    private int surnameLength;
    private static final int SURNAME_LENGTH = 20;

    private String idNumber;
    private int idNumberLength;
    private static final int IDNUMBER_LENGTH = 13;

    private LocalDate dateOfBirth;

    private int number = 0;
    private double doubleNum = 0;

    private long address;

    private BTree<PCRTestDate> pcrTestDateTree;


    public Person() {

    }

    public Person(String idNumber) {
        this.idNumber = idNumber;
    }

    public Person(String name, String surname, String idNumber, LocalDate dateOfBirth, UFile<PCRTestDate> pcrTestDateFile) {
        this.name = name;
        this.nameLength = this.name.length();
        this.surname = surname;
        this.surnameLength = this.surname.length();
        this.idNumber = idNumber;
        this.idNumberLength = this.idNumber.length();
        this.dateOfBirth = dateOfBirth;
        loadTree(pcrTestDateFile);
    }

    public Person(String name, String surname, String idNumber, LocalDate dateOfBirth, int number , double doubleNum) {
        this.name = name;
        this.nameLength = this.name.length();
        this.surname = surname;
        this.surnameLength = this.surname.length();
        this.idNumber = idNumber;
        this.idNumberLength = this.idNumber.length();
        this.dateOfBirth = dateOfBirth;
        this.number = number;
        this.doubleNum = doubleNum;
    }

    @Override
    public int compareTo(Person person) {
        if (this.idNumber.compareTo(person.idNumber) == 0) {
            return 0;
        } else {
            if (this.idNumber.compareTo(person.idNumber) < 0) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.substring(0, NAME_LENGTH);
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname.substring(0, SURNAME_LENGTH);
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber.substring(0, IDNUMBER_LENGTH);
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public BTree<PCRTestDate> getPcrTestDateTree(UFile<PCRTestDate> pcrTestDateFile) {
        loadTree(pcrTestDateFile);
        return pcrTestDateTree;
    }

    public boolean insertPCRTestDate(PCRTestDate pcrTestDate, UFile<PCRTestDate> pcrTestDateFile) {
        loadTree(pcrTestDateFile);
        return this.pcrTestDateTree.insert(pcrTestDate);
    }

    private void loadTree(UFile<PCRTestDate> pcrTestDateFile) {
        if (pcrTestDateTree == null) {
            this.pcrTestDateTree = new BTree<>(PCRTestDate.class,"files/persons/person_" + idNumber +".txt","files/persons/personFS_" + idNumber +".txt", pcrTestDateFile);
        }
    }


    public int getNumber() {
        return number;
    }

    public double getDoubleNum() {
        return doubleNum;
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
            dataOutputStream.writeChars(tmp.substring(0, NAME_LENGTH));

            tmp = this.surname;
            for (int i = 0; i < SURNAME_LENGTH - surnameLength; i++) {
                tmp += "A";
            }
            dataOutputStream.writeInt(this.surnameLength);
            dataOutputStream.writeChars(tmp.substring(0, SURNAME_LENGTH));

            tmp = this.idNumber;
            for (int i = 0; i < IDNUMBER_LENGTH - idNumberLength; i++) {
                tmp += "A";
            }
            dataOutputStream.writeInt(this.idNumberLength);
            dataOutputStream.writeChars(tmp.substring(0, IDNUMBER_LENGTH));
            if (this.dateOfBirth == null) {
                this.dateOfBirth = LocalDate.now();
            }
            dataOutputStream.writeLong(this.dateOfBirth.toEpochDay());

            dataOutputStream.writeInt(this.number);
            dataOutputStream.writeDouble(this.doubleNum);

            dataOutputStream.writeLong(this.address);

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

            this.idNumber = "";
            this.idNumberLength = dataInputStream.readInt();
            for (int i = 0; i < IDNUMBER_LENGTH; i++) {
                this.idNumber += dataInputStream.readChar() + "";
            }
            this.idNumber = this.idNumber.substring(0, idNumberLength);

            this.dateOfBirth = LocalDate.ofEpochDay(dataInputStream.readLong());

            this.number = dataInputStream.readInt();
            this.doubleNum = dataInputStream.readDouble();

            this.address = dataInputStream.readLong();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getSize() {
        return 1 + 3 * Integer.BYTES + Character.BYTES * (NAME_LENGTH + SURNAME_LENGTH + IDNUMBER_LENGTH) + Long.BYTES + Long.BYTES + Double.BYTES + Integer.BYTES;
    }

    @Override
    public Person createClass() {
        return new Person();
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    @Override
    public void setValid(boolean valid) {
        this.isValid = valid;
    }

    @Override
    public long getAddress() {
        return this.address;
    }

    @Override
    public void setAddress(long address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return " " + name + '\'' + " " + surname + '\'' + " " + idNumber + '\'' + " " + dateOfBirth;
    }
}
