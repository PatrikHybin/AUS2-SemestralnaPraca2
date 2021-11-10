package com.company;

public class Workplace implements Comparable<Workplace> {
    private Integer workplaceCode;
    private TwoThreeTree<PCRTestDate> pcrTestDateTree = new TwoThreeTree<>();
    private TwoThreeTree<PCRTestDate> prcTestDatePositiveTree = new TwoThreeTree<>();
    private District district;

    Workplace(int workplaceCode) {
        this.workplaceCode = workplaceCode;
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

    public TwoThreeTree<PCRTestDate> getPcrTestDateTree() {
        return pcrTestDateTree;
    }

    public TwoThreeTree<PCRTestDate> getPcrTestDatePositiveTree() {
        return prcTestDatePositiveTree;
    }

    public void insertPCRTestDate(PCRTestDate pcrTest) {
        if (pcrTest.getPcrTestData().getResult().equals("Positive")) {
            prcTestDatePositiveTree.insert(pcrTest);
            pcrTestDateTree.insert(pcrTest);
        } else {
            pcrTestDateTree.insert(pcrTest);
        }
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }
}
