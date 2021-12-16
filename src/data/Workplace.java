package data;

import java.io.*;

public class Workplace implements IRecord<Workplace> {
    private long nullValue = -100;

    private BTree<PCRTestDate> pcrTestDateTree;
    private BTree<PCRTestDate> pcrTestDatePositiveTree;

    private District district;

    private boolean isValid = true;
    private Integer workplaceCode;
    private long districtAddress = nullValue;
    private long address = nullValue;



    public Workplace() {

    }

    public Workplace(int workplaceCode) {
        this.workplaceCode = workplaceCode;
    }

    public Workplace(int workplaceCode, UFile<PCRTestDate> pcrTestDateFile, UFile<PCRTestDate> pcrTestDatePositiveFile) {
        this.workplaceCode = workplaceCode;
        loadTree(pcrTestDateFile);
        loadPositiveTree(pcrTestDatePositiveFile);
    }

    private void loadTree(UFile<PCRTestDate> pcrTestDateFile) {
        if (pcrTestDateTree == null) {
            this.pcrTestDateTree =  new BTree<>(PCRTestDate.class,"files/workplaces/workplace_" + workplaceCode +".txt","files/workplaces/workplaceFS_" + workplaceCode +".txt", pcrTestDateFile);
        }
    }

    private void loadPositiveTree(UFile<PCRTestDate> pcrTestDateFile) {
        if (this.pcrTestDatePositiveTree == null) {
            this.pcrTestDatePositiveTree  = new BTree<>(PCRTestDate.class,"files/workplaces/workplacePositive_" + workplaceCode +".txt","files/workplaces/workplacePositiveFS_" + workplaceCode +".txt", pcrTestDateFile);
        }
    }


    @Override
    public int compareTo(Workplace workplace) {
        if (this.workplaceCode.compareTo(workplace.workplaceCode) == 0) {
            return 0;
        } else {
            if (this.workplaceCode.compareTo(workplace.workplaceCode) < 0) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    public int getWorkplaceCode() {
        return workplaceCode;
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


    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
        this.districtAddress = district.getAddress();
    }

    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        try {
            dataOutputStream.writeBoolean(this.isValid);
            dataOutputStream.writeInt(this.workplaceCode);
            dataOutputStream.writeLong(this.districtAddress);
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
            this.workplaceCode = dataInputStream.readInt();
            this.districtAddress = dataInputStream.readLong();
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
    public Workplace createClass() {
        return new Workplace();
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

    public long getDistrictAddress() {
        return this.districtAddress;
    }
}
