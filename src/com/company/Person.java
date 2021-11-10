package com.company;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class Person implements Comparable<Person> {
    private String name;
    private String surname;
    private String idNumber;
    private LocalDate dateOfBirth;
    private TwoThreeTree<PCRTestDate> pcrTestDateTree = new TwoThreeTree<>();
    private TwoThreeTree<PCRTestUUID> pcrTestUUIDTree = new TwoThreeTree<>();

    public Person(String name, String surname, String idNumber, LocalDate dateOfBirth) {
        this.name = name;
        this.surname = surname;
        this.idNumber = idNumber;
        this.dateOfBirth = dateOfBirth;
    }

    public Person(String idNumber) {
        this.idNumber = idNumber;
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
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public TwoThreeTree<PCRTestDate> getPcrTestDateTree() {
        return pcrTestDateTree;
    }

    public boolean insertPCRTestDate(PCRTestDate pcrTestDate) {
        return pcrTestDateTree.insert(pcrTestDate);
    }

    public TwoThreeTree<PCRTestUUID> getPcrTestUUIDTree() {
        return pcrTestUUIDTree;
    }

    public boolean insertPCRTestUUID(PCRTestUUID pcrTestUUID) {
        return pcrTestUUIDTree.insert(pcrTestUUID);
    }

}
