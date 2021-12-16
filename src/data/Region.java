package data;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class Region implements IRecord<Region> {
    private long nullValue = -100;

    private BTree<PCRTestDate> pcrTestDateTree;
    private BTree<PCRTestDate> pcrTestDatePositiveTree;
    private ArrayList<District> districts = new ArrayList<>();

    private boolean isValid = true;
    private Integer regionCode;
    private long address = nullValue;

    private int numberOfIllPersons = -1;

    public Region() {
    }

    public Region(int regionCode) {
        this.regionCode = regionCode;
    }

    public Region(int regionCode, UFile<PCRTestDate> pcrTestDateFile, UFile<PCRTestDate> pcrTestDatePositiveFile) {
        this.regionCode = regionCode;
        loadTree(pcrTestDateFile);
        loadPositiveTree(pcrTestDatePositiveFile);
    }

    private void loadTree(UFile<PCRTestDate> pcrTestDateFile) {
        if (pcrTestDateTree == null) {
            pcrTestDateTree =  new BTree<>(PCRTestDate.class,"files/regions/region_" + regionCode +".txt","files/regions/regionFS_" + regionCode +".txt", pcrTestDateFile);
        }
    }

    private void loadPositiveTree(UFile<PCRTestDate> pcrTestDateFile) {
        if (this.pcrTestDatePositiveTree == null) {
            this.pcrTestDatePositiveTree = new BTree<>(PCRTestDate.class,"files/regions/regionPositive_" + regionCode +".txt","files/regions/regionPositiveFS_" + regionCode +".txt", pcrTestDateFile);
        }
    }

    @Override
    public int compareTo(Region district) {
        if (this.regionCode.compareTo(district.regionCode) == 0) {
            return 0;
        } else {
            if (this.regionCode.compareTo(district.regionCode) < 0) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    public int getRegionCode() {
        return regionCode;
    }

    public ArrayList<District> getDistricts() {
        return districts;
    }

    public int getNumberOfIllPersons(String[] inputs, UFile<PCRTestDate> pcrTestDatePositiveFile) {
        loadPositiveTree(pcrTestDatePositiveFile);
        if (numberOfIllPersons == -1) {
            LocalDateTime personIllFrom = LocalDateTime.parse(inputs[6]);
            personIllFrom = personIllFrom.minusDays(Integer.parseInt(inputs[7]));
            //ArrayList<Workplace> workplaces = new ArrayList<>();
            ArrayList<PCRTestDate> tests = new ArrayList<>(pcrTestDatePositiveTree.getInterval(new PCRTestDate(personIllFrom, UUID.randomUUID()), new PCRTestDate(LocalDateTime.parse(inputs[6]), UUID.randomUUID())));


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
            dataOutputStream.writeInt(this.regionCode);
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
            this.regionCode = dataInputStream.readInt();
            this.address = dataInputStream.readLong();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getSize() {
        return 1 + Long.BYTES + Integer.BYTES;
    }

    @Override
    public Region createClass() {
        return new Region();
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
}
