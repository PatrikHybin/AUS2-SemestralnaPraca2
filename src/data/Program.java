package data;

import java.util.*;

public class Program {

    private TwoThreeTree<Person> personTree = new TwoThreeTree<>();
    private TwoThreeTree<PCRTestUUID> pcrTestUUIDTree = new TwoThreeTree<>();
    private TwoThreeTree<PCRTestDate> pcrTestDateTree = new TwoThreeTree<>();
    private TwoThreeTree<PCRTestDate> pcrTestPositiveDateTree = new TwoThreeTree<>();

    private TwoThreeTree<Workplace> workplaceTree = new TwoThreeTree<>();
    private TwoThreeTree<District> districtTree = new TwoThreeTree<>();
    private TwoThreeTree<Region> regionTree = new TwoThreeTree<>();

    private Generator generator = new Generator();

    private int countTests = 0;
    private int countPersons = 0;

    private String[] pcrTestColumnNames = {"Test code","Day and time","Result","Person","Workplace code","District code","Region Code","Note"};
    private String[] personColumnNames = {"Name","Surname","Identification number","Birthday"};
    private String[] regionColumnNames = {"Region Code","Number of ill persons"};
    private String[] districtColumnNames = {"District Code","Number of ill persons"};

    public Program() {

        for (Region region : generator.getRegions()){
            regionTree.insert(region);
        }
        for (District district : generator.getDistricts()){
            districtTree.insert(district);
        }
        for (Workplace workplace : generator.getWorkplaces()){
            workplaceTree.insert(workplace);
        }
    }

    public void generatePCRTests(String numberToGenerate) {
        generator.generatePCRTests(numberToGenerate, this);
    }

    public void generatePersons(String number) {
        generator.generatePersons(number, this);
    }

    public TwoThreeTree<Person> getPersonTree() {
        return personTree;
    }

    public Generator getGenerator() {
        return generator;
    }

    public TwoThreeTree<District> getDistrictTree() {
        return districtTree;
    }

    public TwoThreeTree<Region> getRegionTree() {
        return regionTree;
    }

    public TwoThreeTree<Workplace> getWorkplaceTree() {
        return workplaceTree;
    }

    public TwoThreeTree<PCRTestUUID> getPcrTestUUIDTree() {
        return pcrTestUUIDTree;
    }

    public TwoThreeTree<PCRTestDate> getPcrTestDateTree() {
        return pcrTestDateTree;
    }

    public TwoThreeTree<PCRTestDate> getPcrTestPositiveDateTree() {
        return pcrTestPositiveDateTree;
    }

    public String[] getPcrTestColumnNames() {
        return pcrTestColumnNames;
    }

    public String[] getPersonColumnNames() {
        return personColumnNames;
    }

    public String[] getRegionColumnNames() {
        return regionColumnNames;
    }

    public String[] getDistrictColumnNames() {
        return districtColumnNames;
    }
}
