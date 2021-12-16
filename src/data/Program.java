package data;

import java.io.File;

public class Program {
    private static long nullValue = -100;

    private BTree<Person> personTree;
    private BTree<PCRTestUUID> pcrTestUUIDTree;
    private BTree<PCRTestDate> pcrTestDateTree;
    private BTree<PCRTestDate> pcrTestPositiveDateTree;

    private BTree<Workplace> workplaceTree;
    private BTree<District> districtTree;
    private BTree<Region> regionTree;

    private UFile<Person> personFile;
    private UFile<PCRTestData> pcrTestDataFile;
    private UFile<PCRTestDate> pcrTestDateFile;
    private UFile<PCRTestDate> pcrTestDatePositiveFile;
    private UFile<PCRTestUUID> pcrTestUUIDFile;

    private UFile<Workplace> workplaceFile;
    private UFile<District> districtFile;
    private UFile<Region> regionFile;

    private Generator generator;

    private int countTests = 0;
    private int countPersons = 0;

    private String[] pcrTestColumnNames = {"Test code","Day and time","Result","Person","Workplace code","District code","Region Code","Note"};
    private String[] personColumnNames = {"Name","Surname","Identification number","Birthday"};
    private String[] regionColumnNames = {"Region Code","Number of ill persons"};
    private String[] districtColumnNames = {"District Code","Number of ill persons"};
    private String[] treeColumnNames = {"Parent","Left son","Center son","Right son","Left data","Right data","Address","Valid"};

    public Program() {
        File file =  new File("files/persons");
        createDirs(file);
        file =  new File("files/regions");
        createDirs(file);
        file =  new File("files/districts");
        createDirs(file);
        file =  new File("files/workplaces");
        createDirs(file);


        personFile = new UFile<>("files/allPersons.txt", "files/allPersonsFS.txt", Person.class);
        pcrTestDataFile = new UFile<>("files/allPCRTestData.txt", "files/allPCRTestDataFS.txt", PCRTestData.class);
        pcrTestDateFile = new UFile<>("files/allPCRTestDate.txt", "files/allPCRTestDateFS.txt", PCRTestDate.class);
        pcrTestDatePositiveFile = new UFile<>("files/allPCRTestDatePositive.txt", "files/allPCRTestDatePositiveFS.txt", PCRTestDate.class);
        pcrTestUUIDFile = new UFile<>("files/allPCRTestUUID.txt", "files/allPCRTestUUIDFS.txt", PCRTestUUID.class);

        this.personTree = new BTree<>(Person.class,"files/personsNode.txt", "files/personsNodeFS.txt", personFile);
        this.pcrTestDateTree = new BTree<>(PCRTestDate.class, "files/ptdNode.txt","files/ptdNodeFS.txt", pcrTestDateFile);
        this.pcrTestPositiveDateTree = new BTree<>(PCRTestDate.class,"files/ptpdNode.txt","files/ptpdNodeFS.txt", pcrTestDatePositiveFile);
        this.pcrTestUUIDTree = new BTree<>(PCRTestUUID.class,"files/ptuNode.txt","files/ptuNodeFS.txt", pcrTestUUIDFile);

        regionFile = new UFile<>("files/allRegions.txt", "files/allRegionsFS.txt",  Region.class);
        districtFile = new UFile<>("files/allDistricts.txt", "files/allDistrictsFS.txt",  District.class);
        workplaceFile = new UFile<>("files/allWorkplaces.txt", "files/allWorkplacesFS.txt", Workplace.class);

        this.regionTree = new BTree<>(Region.class, "files/regionsNode.txt", "files/regionsNodeFS.txt", regionFile);
        this.districtTree = new BTree<>(District.class, "files/districtsNode.txt", "files/districtsNodeFS.txt", districtFile);
        this.workplaceTree = new BTree<>(Workplace.class, "files/workplacesNode.txt", "files/workplacesNodeFS.txt", workplaceFile);

        this.generator = new Generator(this);

    }

    private void createDirs(File file) {
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public void generatePCRTests(String numberToGenerate) {
        generator.generatePCRTests(numberToGenerate);
    }

    public void generatePersons(String number) {
        generator.generatePersons(number);
    }

    public BTree<Person> getPersonTree() {
        return personTree;
    }

    public Generator getGenerator() {
        return generator;
    }

    public BTree<District> getDistrictTree() {
        return districtTree;
    }

    public BTree<Region> getRegionTree() {
        return regionTree;
    }

    public BTree<Workplace> getWorkplaceTree() {
        return workplaceTree;
    }

    public BTree<PCRTestUUID> getPcrTestUUIDTree() {
        return pcrTestUUIDTree;
    }

    public BTree<PCRTestDate> getPcrTestDateTree() {
        return pcrTestDateTree;
    }

    public BTree<PCRTestDate> getPcrTestPositiveDateTree() {
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

    public void insertPcrTestData(PCRTestData pcrTestData) {
        this.pcrTestDataFile.insert(pcrTestData);
    }

    public PCRTestData getPcrTestData(long pcrTestDataAdd) {
        return this.pcrTestDataFile.find(pcrTestDataAdd);
    }

    public void deletePcrTestData(long pcrTestDataAdd) {
        this.pcrTestDataFile.delete(pcrTestDataAdd);
    }

    public void insertPerson(Person person) {
        this.personFile.insert(person);
    }

    public Person getPerson(long personAdd) {
        return this.personFile.find(personAdd);
    }

    public void deletePerson(long personAdd) {
        this.personFile.delete(personAdd);
    }

    public UFile<Person> getPersonsFile() {
        return this.personFile;
    }

    public UFile<PCRTestDate> getPcrTestDateFile() {
        return this.pcrTestDateFile;
    }

    public UFile<PCRTestDate> getPcrTestPositiveDateFile() {
        return this.pcrTestDatePositiveFile;
    }

    public UFile<PCRTestUUID> getPcrTestUUIDFile() {
        return this.pcrTestUUIDFile;
    }

    public UFile<Workplace> getWorkplaceFile() {
        return workplaceFile;
    }

    public UFile<District> getDistrictFile() {
        return districtFile;
    }

    public UFile<Region> getRegionFile() {
        return regionFile;
    }


    public void insertPcrTestDate(PCRTestDate pcrTestDate) {
        this.pcrTestDateFile.insert(pcrTestDate);
    }

    public UFile<PCRTestData> getPcrTestDataFile() {
        return this.pcrTestDataFile;
    }

    public String[] getTreeColumnNames() {
        return this.treeColumnNames;
    }
}
