package com.company;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class PCRTestData {
    protected LocalDateTime dateAndTimeOfTest;
    protected Person patient;
    protected UUID testCode;
    protected int workplaceCode;
    protected int districtCode;
    protected int regionCode;
    protected String result;
    protected String note;

    public PCRTestData(UUID testCode) {
        this.testCode = testCode;
    }

    public PCRTestData(UUID testCode, LocalDateTime dateAndTimeOfTest, String result , Person patient, int workplaceCode, int districtCode, int regionCode, String note) {
        this.dateAndTimeOfTest = dateAndTimeOfTest;
        this.patient = patient;
        this.testCode = testCode;
        this.workplaceCode = workplaceCode;
        this.districtCode = districtCode;
        this.regionCode = regionCode;
        this.result = result;
        this.note = note;
    }

    public PCRTestData(LocalDateTime date) {
        this.dateAndTimeOfTest = date;
    }

    public LocalDateTime getDateAndTimeOfTest() {
        return dateAndTimeOfTest;
    }

    public void setDateAndTimeOfTest(LocalDateTime dateAndTimeOfTest) {
        this.dateAndTimeOfTest = dateAndTimeOfTest;
    }

    public Person getPatient() {
        return patient;
    }

    public void setPatient(Person patient) {
        this.patient = patient;
    }

    public UUID getTestCode() {
        return testCode;
    }

    public void setTestCode(UUID testCode) {
        this.testCode = testCode;
    }

    public int getWorkplaceCode() {
        return workplaceCode;
    }

    public void setWorkplaceCode(int workplaceCode) {
        this.workplaceCode = workplaceCode;
    }

    public int getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(int districtCode) {
        this.districtCode = districtCode;
    }

    public int getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(int regionCode) {
        this.regionCode = regionCode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


}
