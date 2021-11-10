package com.company;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
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

    public int getNumberOfIllPersons(String[] inputs) {
        LocalDateTime personIllTo = LocalDateTime.parse(inputs[6]);
        personIllTo = personIllTo.plusDays(Integer.parseInt(inputs[8]));
        ArrayList<PCRTestDate> tests = new ArrayList<>();
        for (Workplace workplace : workplaces) {
            tests.addAll(workplace.getPcrTestDatePositiveTree().getInterval(new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]))), new PCRTestDate(new PCRTestData(personIllTo))));
        }

        return tests.size();
    }
}
