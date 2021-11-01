package com.company;

import java.util.ArrayList;

public class Region implements Comparable<Region> {
    private Integer regionCode;
    private TwoThreeTree<District> districtTree = new TwoThreeTree<>();
    private ArrayList<District> districts = new ArrayList<>();

    Region(int regionCode) {
        this.regionCode = regionCode;
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

    public TwoThreeTree<District> getDistrictTree() {
        return districtTree;
    }

    public void insertDistrict(District district) {
        districtTree.insert(district);
    }

    public ArrayList<District> getDistricts() {
        return districts;
    }
}
