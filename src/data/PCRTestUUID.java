package data;

public class PCRTestUUID implements Comparable<PCRTestUUID> {

    private PCRTestData pcrTestData;

    public PCRTestUUID(PCRTestData data) {
        pcrTestData = data;
    }

    @Override
    public int compareTo(PCRTestUUID pcrTest) {
        if (this.pcrTestData.testCode.compareTo(pcrTest.pcrTestData.testCode) == 0) {
            return 0;
        } else {
            if (this.pcrTestData.testCode.compareTo(pcrTest.pcrTestData.testCode) < 0) {
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
