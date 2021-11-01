package com.company;

public class PCRTestTEST implements Comparable<PCRTestTEST> {

    private Integer testCode;

    public PCRTestTEST(int testCode) {
        this.testCode = testCode;
    }
    @Override
    public int compareTo(PCRTestTEST pcrTest) {
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

    public int getTestCode() {
        return  testCode;
    }
}
