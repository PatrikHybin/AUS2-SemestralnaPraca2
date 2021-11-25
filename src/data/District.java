package data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class District implements Comparable<District> {

    private Integer districtCode;
    //private TwoThreeTree<Workplace> workplaceTree = new TwoThreeTree<>();
    private TwoThreeTree<PCRTestDate> pcrTestDateTree = new TwoThreeTree<>();
    private TwoThreeTree<PCRTestDate> pcrTestDatePositiveTree = new TwoThreeTree<>();
    private TwoThreeTree<PCRTestNote> pcrTestNote = new TwoThreeTree<>();
    private ArrayList<Workplace> workplaces = new ArrayList<>();
    private Region region;
    private int numberOfIllPersons = -1;

    public District(int districtCode) {
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

    /*public TwoThreeTree<Workplace> getWorkPlaceTree() {
        return workplaceTree;
    }

    public void insertWorkplace(Workplace workplace) {
        workplaceTree.insert(workplace);
    }*/

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
        if (numberOfIllPersons == -1) {
            LocalDateTime personIllFrom = LocalDateTime.parse(inputs[6]);
            personIllFrom = personIllFrom.minusDays(Integer.parseInt(inputs[7]));
            ArrayList<PCRTestDate> tests = new ArrayList<>(pcrTestDatePositiveTree.getInterval(new PCRTestDate(new PCRTestData(personIllFrom, UUID.randomUUID())), new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]), UUID.randomUUID()))));
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

    public TwoThreeTree<PCRTestNote> getPcrTestNoteTree() {
        return pcrTestNote;
    }
}
