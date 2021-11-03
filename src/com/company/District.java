package com.company;

import java.util.ArrayList;

public class District implements Comparable<District> {
    private Integer districtCode;
    private TwoThreeTree<Workplace> workplaceTree = new TwoThreeTree<>();
    private ArrayList<Workplace> workplaces = new ArrayList<>();
    private Region region;

    District(int districtCode) {
        this.districtCode = districtCode;
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

    public TwoThreeTree<Workplace> getWorkPlaceTree() {
        return workplaceTree;
    }

    public void insertWorkplace(Workplace workplace) {
        workplaceTree.insert(workplace);
    }

    public ArrayList<Workplace> getWorkplaces() {
        return workplaces;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
}
