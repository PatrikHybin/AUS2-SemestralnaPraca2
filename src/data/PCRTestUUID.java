package data;

import com.IRecord;

import java.io.*;
import java.util.UUID;

public class PCRTestUUID implements IRecord<PCRTestUUID> {

    private static long nullValue = -100;

    private UUID testCode;

    private boolean isValid = true;
    private long pcrTestData = nullValue;
    private long address;

    private static final int UUIDLength = 36;

    public PCRTestUUID() {

    }

    public PCRTestUUID(UUID testCode) {
        this.testCode = testCode;
    }

    public PCRTestUUID(long dataAddress, UUID testCode) {
        this.pcrTestData = dataAddress;
        this.testCode = testCode;
    }

    @Override
    public int compareTo(PCRTestUUID pcrTest) {
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

    public long getPcrTestData() {
        return pcrTestData;
    }

    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        try {
            dataOutputStream.writeBoolean(this.isValid);

            dataOutputStream.writeChars(testCode + "");
            //System.out.println("UUID zapis pre add " + this.address + "  " + testCode + "");
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
            //System.out.println("UUID citanie pre add " + this.address + "  " + uuid + "");
            this.testCode = UUID.fromString(uuid);

            this.pcrTestData = dataInputStream.readLong();
            this.address = dataInputStream.readLong();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getSize() {
        return 1 + 2 * Long.BYTES + Character.BYTES * UUIDLength;
    }

    @Override
    public PCRTestUUID createClass() {
        return new PCRTestUUID();
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

    public UUID getTestCode() {
        return testCode;
    }
}
