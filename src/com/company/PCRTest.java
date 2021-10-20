package com.company;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class PCRTest implements Comparable<PCRTest> {
    private LocalDateTime dateAndTimeOfTest;
    private Person patient;
    private UUID testCode;
    private int workplaceCode;
    private int districtCode;
    private int regionCode;
    private boolean result;
    private String note;

    public PCRTest(UUID testCode) {
        this.testCode = testCode;
    }

    @Override
    public int compareTo(PCRTest pcrTest) {
        if (this.testCode.compareTo(pcrTest.testCode) == 0) {
            return 0;
        } else {
            if (this.testCode.compareTo(pcrTest.testCode) < 0) {
                return -1;
            } else {
                return 1;
            }
        }
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

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


}
