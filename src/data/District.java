package data;

import com.UFile;
import com.IRecord;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class District implements IRecord<District> {
    private long nullValue = -100;

    private BTree<PCRTestDate> pcrTestDateTree;
    private BTree<PCRTestDate> pcrTestDatePositiveTree;
    private ArrayList<Workplace> workplaces = new ArrayList<>();

    private Region region;

    private boolean isValid = true;
    private Integer districtCode;
    private long regionAddress = nullValue;
    private long address = nullValue;

    private int numberOfIllPersons = -1;


    public District() {

    }

    public District(int districtCode) {
        this.districtCode = districtCode;
    }

    public District(int districtCode, UFile<PCRTestDate> pcrTestDateFile, UFile<PCRTestDate> pcrTestDatePositiveFile) {
        this.districtCode = districtCode;
        loadTree(pcrTestDateFile);
        loadPositiveTree(pcrTestDatePositiveFile);
    }

    private void loadTree(UFile<PCRTestDate> pcrTestDateFile) {
        if (pcrTestDateTree == null) {
            this.pcrTestDateTree = new BTree<>(PCRTestDate.class,"files/districts/district_" + districtCode +".txt","files/districts/districtFS_" + districtCode +".txt", pcrTestDateFile);
        }
    }

    private void loadPositiveTree(UFile<PCRTestDate> pcrTestDateFile) {
        if (this.pcrTestDatePositiveTree == null) {
            this.pcrTestDatePositiveTree = new BTree<>(PCRTestDate.class, "files/districts/districtPositive_" + districtCode +".txt","files/districts/districtPositiveFS_" + districtCode +".txt", pcrTestDateFile);
        }
    }

    @Override
    public int compareTo(District district) {
        if (this.districtCode.compareTo(district.districtCode) == 0) {
            return 0;
        } else {
            if (this.districtCode.compareTo(district.districtCode) < 0) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    public int getDistrictCode() {
        return districtCode;
    }

    public ArrayList<Workplace> getWorkplaces() {
        return workplaces;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
        this.regionAddress = region.getAddress();
    }

    public int getNumberOfIllPersons(String[] inputs, UFile<PCRTestDate> pcrTestDatePositiveFile) {
        loadPositiveTree(pcrTestDatePositiveFile);
        if (numberOfIllPersons == -1) {
            LocalDateTime personIllFrom = LocalDateTime.parse(inputs[6]);
            personIllFrom = personIllFrom.minusDays(Integer.parseInt(inputs[7]));
            ArrayList<PCRTestDate> tests = new ArrayList<>(pcrTestDatePositiveTree.getInterval(new PCRTestDate(personIllFrom, UUID.randomUUID()), new PCRTestDate(LocalDateTime.parse(inputs[6]), UUID.randomUUID())));
            /*for (Workplace workplace : workplaces) {
                tests.addAll(workplace.getPcrTestDatePositiveTree().getInterval(new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]))), new PCRTestDate(new PCRTestData(personIllTo))));
            }*/
            numberOfIllPersons = tests.size();
        }

        return numberOfIllPersons;
    }

    public void setNumberOfIllPersons() {
        this.numberOfIllPersons = -1;
    }

    public void insertPCRTestDate(PCRTestDate pcrTest, boolean result, UFile<PCRTestDate> pcrTestDateFile) {
        loadTree(pcrTestDateFile);
        pcrTestDateTree.insert(pcrTest);
    }

    public void insertPCRTestDatePositive(PCRTestDate pcrTest, boolean result, UFile<PCRTestDate> pcrTestDateFile) {
        loadPositiveTree(pcrTestDateFile);
        pcrTestDatePositiveTree.insert(pcrTest);
    }

    public BTree<PCRTestDate> getPcrTestDateTree(UFile<PCRTestDate> pcrTestDateFile) {
        loadTree(pcrTestDateFile);
        return pcrTestDateTree;
    }

    public BTree<PCRTestDate> getPcrTestDatePositiveTree(UFile<PCRTestDate> pcrTestDateFile) {
        loadPositiveTree(pcrTestDateFile);
        return pcrTestDatePositiveTree;
    }

    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        try {
            dataOutputStream.writeBoolean(this.isValid);
            dataOutputStream.writeInt(this.districtCode);
            dataOutputStream.writeLong(this.regionAddress);
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
            this.districtCode = dataInputStream.readInt();
            this.regionAddress = dataInputStream.readLong();
            this.address = dataInputStream.readLong();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getSize() {
        return 1 + 2 * Long.BYTES + Integer.BYTES;
    }

    @Override
    public District createClass() {
        return new District();
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

    public long getRegionAddress() {
        return this.regionAddress;
    }
}
