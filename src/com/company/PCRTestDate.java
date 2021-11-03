package com.company;

public class PCRTestDate implements Comparable<PCRTestDate> {

    private PCRTestData pcrTestData;

    public PCRTestDate(PCRTestData data) {
        pcrTestData = data;
    }
    @Override
    public int compareTo(PCRTestDate pcrTest) {
        if (this.pcrTestData.dateAndTimeOfTest.compareTo(pcrTest.pcrTestData.dateAndTimeOfTest) == 0) {
            if (this.pcrTestData.testCode.compareTo(pcrTest.pcrTestData.testCode) == 0) {
                return 0;
            } else {
                if (this.pcrTestData.testCode.compareTo(pcrTest.pcrTestData.testCode) < 0) {
                    return -1;
                } else {
                    return 1;
                }
            }
        } else {
            if (this.pcrTestData.dateAndTimeOfTest.compareTo(pcrTest.pcrTestData.dateAndTimeOfTest) < 0) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    public PCRTestData getPcrTestData() {
        return pcrTestData;
    }
}
