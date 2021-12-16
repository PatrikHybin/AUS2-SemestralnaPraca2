package data;

import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;
import java.util.UUID;

public class PCRTestData implements IRecord<PCRTestData> {

    private boolean isValid = true;
    protected LocalDateTime dateAndTimeOfTest = LocalDateTime.now();
    protected long person;
    protected UUID testCode;
    private static final int UUIDLength = 36;
    protected int workplaceCode;
    protected int districtCode;
    protected int regionCode;
    protected boolean result;
    protected String note;
    private int noteLength;
    private final int NOTE_LENGTH = 20;
    private long address;



    public PCRTestData() {
    }

    public PCRTestData(UUID testCode) {
        this.testCode = testCode;
    }

    public PCRTestData(UUID testCode, LocalDateTime dateAndTimeOfTest, boolean result , long person, int workplaceCode, int districtCode, int regionCode, String note) {
        this.dateAndTimeOfTest = dateAndTimeOfTest;
        this.person = person;
        this.testCode = testCode;
        this.workplaceCode = workplaceCode;
        this.districtCode = districtCode;
        this.regionCode = regionCode;
        this.result = result;
        this.note = note;
        this.noteLength = this.note.length();
    }

    public PCRTestData(LocalDateTime date) {
        this.dateAndTimeOfTest = date;
    }

    public PCRTestData(LocalDateTime date, UUID testCode) {
        this.dateAndTimeOfTest = date;
        this.testCode = testCode;
    }

    public LocalDateTime getDateAndTimeOfTest() {
        return dateAndTimeOfTest;
    }

    public void setDateAndTimeOfTest(LocalDateTime dateAndTimeOfTest) {
        this.dateAndTimeOfTest = dateAndTimeOfTest;
    }

    public long getPerson() {
        return person;
    }

    public void setPerson(long person) {
        this.person = person;
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

    public boolean getResult() {
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

    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        try {
            dataOutputStream.writeBoolean(this.isValid);
            dataOutputStream.writeLong(this.dateAndTimeOfTest.atZone(ZoneId.systemDefault()).toEpochSecond());
            dataOutputStream.writeLong(this.person);
            dataOutputStream.writeChars(this.testCode + "");

            dataOutputStream.writeInt(this.workplaceCode);
            dataOutputStream.writeInt(this.districtCode);
            dataOutputStream.writeInt(this.regionCode);

            dataOutputStream.writeBoolean(this.result);

            String tmp = this.note;
            for (int i = 0; i < NOTE_LENGTH - noteLength; i++) {
                tmp += "A";
            }
            dataOutputStream.writeInt(this.noteLength);
            dataOutputStream.writeChars(tmp);
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
            this.dateAndTimeOfTest = LocalDateTime.ofInstant(Instant.ofEpochSecond(dataInputStream.readLong()), TimeZone.getDefault().toZoneId());
            this.person = dataInputStream.readLong();
            String tmp = "";
            for (int i = 0; i < UUIDLength; i++) {
                tmp += dataInputStream.readChar() + "";
            }
            this.testCode = UUID.fromString(tmp);

            this.workplaceCode = dataInputStream.readInt();
            this.districtCode = dataInputStream.readInt();
            this.regionCode = dataInputStream.readInt();

            this.result = dataInputStream.readBoolean();

            this.note = "";
            this.noteLength = dataInputStream.readInt();
            for (int i = 0; i < NOTE_LENGTH; i++) {
                this.note += dataInputStream.readChar() + "";
            }
            this.note = this.note.substring(0, noteLength);
            this.address = dataInputStream.readLong();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getSize() {

        return 2 +  4 * Integer.BYTES + Character.BYTES * (NOTE_LENGTH + UUIDLength) + 3 * Long.BYTES;
    }

    @Override
    public PCRTestData createClass() {
        return new PCRTestData();
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
    public long getAddress() {
        return this.address;
    }

    @Override
    public void setAddress(long address) {
        this.address = address;
    }

    @Override
    public int compareTo(PCRTestData o) {
        return 0;
    }
}
