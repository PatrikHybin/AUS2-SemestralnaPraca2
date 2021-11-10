package com.company;

import java.time.LocalDateTime;
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

    public int getNumberOfIllPersons(String[] inputs) {
        LocalDateTime personIllTo = LocalDateTime.parse(inputs[6]);
        personIllTo = personIllTo.plusDays(Integer.parseInt(inputs[8]));
        ArrayList<Workplace> workplaces = new ArrayList<>();
        ArrayList<PCRTestDate> tests = new ArrayList<>();

        for (District district : districtTree.inOrder()) {
            workplaces.addAll(district.getWorkplaces());
        }

        for (Workplace workplace : workplaces) {
            tests.addAll(workplace.getPcrTestDatePositiveTree().getInterval(new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]))), new PCRTestDate(new PCRTestData(personIllTo))));
        }
        return tests.size();
    }
}
