package data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class Region implements Comparable<Region> {
    private Integer regionCode;
    //private TwoThreeTree<District> districtTree = new TwoThreeTree<>();
    private ArrayList<District> districts = new ArrayList<>();
    private TwoThreeTree<PCRTestDate> pcrTestDateTree = new TwoThreeTree<>();
    private TwoThreeTree<PCRTestDate> pcrTestDatePositiveTree = new TwoThreeTree<>();
    private int numberOfIllPersons = -1;

    public Region(int regionCode) {
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

    /*public TwoThreeTree<District> getDistrictTree() {
        return districtTree;
    }

    public void insertDistrict(District district) {
        districtTree.insert(district);
    }*/

    public ArrayList<District> getDistricts() {
        return districts;
    }

    public int getNumberOfIllPersons(String[] inputs) {
        if (numberOfIllPersons == -1) {
            LocalDateTime personIllFrom = LocalDateTime.parse(inputs[6]);
            personIllFrom = personIllFrom.minusDays(Integer.parseInt(inputs[7]));
            //ArrayList<Workplace> workplaces = new ArrayList<>();
            ArrayList<PCRTestDate> tests = new ArrayList<>(pcrTestDatePositiveTree.getInterval(new PCRTestDate(new PCRTestData(personIllFrom, UUID.randomUUID())), new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]), UUID.randomUUID()))));

            /*for (District district : districtTree.inOrder()) {
                workplaces.addAll(district.getWorkplaces());
            }

            for (Workplace workplace : workplaces) {
                tests.addAll(workplace.getPcrTestDatePositiveTree().getInterval(new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]))), new PCRTestDate(new PCRTestData(personIllTo))));
            }*/
            numberOfIllPersons = tests.size();
        }

        return numberOfIllPersons;
    }

    public void setNumberOfIllPersons() {
        this.numberOfIllPersons = -1;
    }

    public void insertPCRTestDate(PCRTestDate pcrTest) {
        if (pcrTest.getPcrTestData().getResult().equals("Positive")) {
            pcrTestDatePositiveTree.insert(pcrTest);
        }
        pcrTestDateTree.insert(pcrTest);
    }

    public TwoThreeTree<PCRTestDate> getPcrTestDateTree() {
        return pcrTestDateTree;
    }

    public TwoThreeTree<PCRTestDate> getPcrTestDatePositiveTree() {
        return pcrTestDatePositiveTree;
    }
}
