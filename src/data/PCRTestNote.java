package data;

public class PCRTestNote implements Comparable<PCRTestNote> {

    private PCRTestData pcrTestData;

    public PCRTestNote(PCRTestData data) {
        pcrTestData = data;
    }
    @Override
    public int compareTo(PCRTestNote pcrTest) {
        if (this.pcrTestData.note.compareTo(pcrTest.pcrTestData.note) == 0) {
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
            if (this.pcrTestData.note.compareTo(pcrTest.pcrTestData.note) < 0) {
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