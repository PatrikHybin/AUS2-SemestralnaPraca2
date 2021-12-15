package data;

import com.IRecord;

import java.io.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;
import java.util.UUID;

public class PCRTestDate implements IRecord<PCRTestDate> {

    private static long nullValue = -100;

    private LocalDateTime dateAndTimeOfTest =  LocalDateTime.now();
    private UUID testCode;


    private boolean isValid = true;
    private long pcrTestData = nullValue;
    private long address;

    private static final int UUIDLength = 36;

    public PCRTestDate() {
    }

    public PCRTestDate(LocalDateTime dateAndTimeOfTest, UUID testCode) {
        this.dateAndTimeOfTest = dateAndTimeOfTest;
        this.testCode = testCode;
    }

    public PCRTestDate(long dataAddress, LocalDateTime dateAndTimeOfTest, UUID testCode) {
        this.pcrTestData = dataAddress;
        this.dateAndTimeOfTest = dateAndTimeOfTest;
        this.testCode = testCode;
    }

    @Override
    public int compareTo(PCRTestDate pcrTest) {
        if (this.dateAndTimeOfTest.compareTo(pcrTest.dateAndTimeOfTest) == 0) {
            if (this.testCode.compareTo(pcrTest.testCode) == 0) {
                return 0;
            } else {
                if (this.testCode.compareTo(pcrTest.testCode) < 0) {
                    return -1;
                } else {
                    return 1;
                }
            }
        } else {
            if (this.dateAndTimeOfTest.compareTo(pcrTest.dateAndTimeOfTest) < 0) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    public long getPcrTestData() {
        return pcrTestData;
    }

    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        try {
            dataOutputStream.writeBoolean(this.isValid);

            dataOutputStream.writeChars(this.testCode + "");
            //System.out.println("Date zapis pre add " + this.address + "  " + testCode);
            dataOutputStream.writeLong(this.dateAndTimeOfTest.atZone(ZoneId.systemDefault()).toEpochSecond());

            dataOutputStream.writeLong(this.pcrTestData);
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

            String uuid = "";
            for (int i = 0; i < UUIDLength; i++) {
                uuid += dataInputStream.readChar();
            }
            //System.out.println("Date citanie pre add " + this.address + "  " + uuid);
            this.testCode = UUID.fromString(uuid);
            this.dateAndTimeOfTest = LocalDateTime.ofInstant(Instant.ofEpochSecond(dataInputStream.readLong()), TimeZone.getDefault().toZoneId());

            this.pcrTestData = dataInputStream.readLong();
            this.address = dataInputStream.readLong();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getSize() {
        return 1 + 3 * Long.BYTES + Character.BYTES * UUIDLength;
    }

    @Override
    public PCRTestDate createClass() {
        return new PCRTestDate();
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

    public LocalDateTime getDateAndTimeOfTest() {
        return dateAndTimeOfTest;
    }

    public UUID getTestCode() {
        return testCode;
    }
}
