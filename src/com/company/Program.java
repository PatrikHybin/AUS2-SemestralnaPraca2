package com.company;

import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Program {

    private TwoThreeTree<Person> personTree = new TwoThreeTree<>();
    private TwoThreeTree<PCRTestUUID> pcrTestUUIDTree = new TwoThreeTree<>();
    private TwoThreeTree<PCRTestDate> pcrTestDateTree = new TwoThreeTree<>();

    private TwoThreeTree<Workplace> workplaceTree = new TwoThreeTree<>();
    private TwoThreeTree<District> districtTree = new TwoThreeTree<>();
    private TwoThreeTree<Region> regionTree = new TwoThreeTree<>();

    private DefaultTableModel searchTableModel;
    Generator generator = new Generator();

    private String[] pcrTestColumnNames = {"Test code","Day and time","Result","Patient","Workplace code","District code","Region Code","Note"};
    private String[] personColumnNames = {"Name","Surname","Identification number","Birthday"};

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

    public void addPerson(String[] inputs) {
        Person person = new Person(inputs[0], inputs[1], inputs[2], LocalDate.parse(inputs[3]));
        boolean outcome = personTree.insert(person);
        if (!outcome) {
            generator.addPersonToList(person);
        }
    }

    public int getNumberOfPersonsInSystem() {
        return generator.getPersonsSize();
    }

    public void addPCRTest(String[] inputs) {
        Person person = personTree.find(new Person(inputs[2]));
        int regionCode = Integer.parseInt(inputs[5].split("[.]")[0]);
        PCRTestData pcrTest = new PCRTestData(UUID.randomUUID(), LocalDateTime.parse(inputs[0]), inputs[1], person, Integer.parseInt(inputs[3]), Integer.parseInt(inputs[4]), regionCode, inputs[6]);

        pcrTestUUIDTree.insert(new PCRTestUUID(pcrTest));

        Workplace workplace = workplaceTree.find(new Workplace(Integer.parseInt(inputs[3])));
        workplace.insertPCRTestDate(new PCRTestDate(pcrTest));
    }

    public void search(String checkBoxText, String[] inputs) {
        switch (checkBoxText) {
            case "2":
                searchPCRTestCode(inputs);
                break;
            case "3":
                break;
            case "4":
                searchPCRTestPositiveMinMaxDateAndDistrictCode(inputs);
                break;
            case "5":
                break;
            case "6":
                break;
            case "7":
                break;
            case "8":
                break;
            case "9":
                break;
            case "10":
                break;
            case "11":
                break;
            case "12":
                break;
            case "13":
                break;
            case "14":
                break;
            case "15":
                searchPCRTestMinMaxDateAndWorkplaceCode(inputs);
                break;
            default:
                break;
        }
    }

    private void searchPCRTestCode(String[] inputs) {
        ArrayList<PCRTestUUID> tests = new ArrayList<>();
        tests.add(pcrTestUUIDTree.find(new PCRTestUUID(new PCRTestData(UUID.fromString(inputs[1])))));

        Object[][] data = new Object[tests.size()][8];
        Person person;
        PCRTestData pcrTestData;
        for (int i = 0; i < tests.size(); i++) {
            pcrTestData = tests.get(i).getPcrTestData();
            data[i][0] = pcrTestData.getTestCode();
            data[i][1] = pcrTestData.getDateAndTimeOfTest();
            data[i][2] = pcrTestData.getResult();
            person = pcrTestData.getPatient();
            data[i][3] = person.getName() + " " + person.getSurname();
            data[i][4] = pcrTestData.getWorkplaceCode();
            data[i][5] = pcrTestData.getDistrictCode();
            data[i][6] = pcrTestData.getRegionCode();
            data[i][7] = pcrTestData.getNote();
        }
        searchTableModel = new DefaultTableModel(data, pcrTestColumnNames);
    }

    private void searchPCRTestPositiveMinMaxDateAndDistrictCode(String[] inputs) {
        District district = districtTree.find(new District(Integer.parseInt(inputs[4])));
        ArrayList<PCRTestDate> tests = new ArrayList<>();
        for (Workplace workplace: district.getWorkPlaceTree().inOrder()) {
            tests.addAll(workplace.getPcrTestPositiveTree().getInterval(new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]))), new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[7])))));
        }
        searchTableModel = new DefaultTableModel(getPCRTestDateDataToTable(tests), pcrTestColumnNames);
    }

    private void searchPCRTestMinMaxDateAndWorkplaceCode(String[] inputs) {
        ArrayList<PCRTestDate> tests = new ArrayList<>();
        Workplace workplace = workplaceTree.find(new Workplace(Integer.parseInt(inputs[5])));
        tests.addAll(workplace.getPcrTestDateTree().getInterval(new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]))), new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[7])))));
        searchTableModel = new DefaultTableModel(getPCRTestDateDataToTable(tests), pcrTestColumnNames);
    }

    private Object[][] getPCRTestDateDataToTable(ArrayList<PCRTestDate> tests) {
        Object[][] data = new Object[tests.size()][8];
        Person person;
        PCRTestData pcrTestData;
        for (int i = 0; i < tests.size(); i++) {
            pcrTestData = tests.get(i).getPcrTestData();
            data[i][0] = pcrTestData.getTestCode();
            data[i][1] = pcrTestData.getDateAndTimeOfTest();
            data[i][2] = pcrTestData.getResult();
            person = pcrTestData.getPatient();
            data[i][3] = person.getName() + " " + person.getSurname() + " " + person.getIdNumber() + " " + person.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            data[i][4] = pcrTestData.getWorkplaceCode();
            data[i][5] = pcrTestData.getDistrictCode();
            data[i][6] = pcrTestData.getRegionCode();
            data[i][7] = pcrTestData.getNote();
        }
        return data;
    }

    public DefaultTableModel getPCRTestsTable() {
        ArrayList<PCRTestUUID> tests = new ArrayList<>();
        tests.addAll(pcrTestUUIDTree.inOrder());

        Object[][] data = new Object[tests.size()][8];
        Person person;
        PCRTestData pcrTestData;
        for (int i = 0; i < tests.size(); i++) {
            pcrTestData = tests.get(i).getPcrTestData();
            data[i][0] = pcrTestData.getTestCode();
            data[i][1] = pcrTestData.getDateAndTimeOfTest();
            data[i][2] = pcrTestData.getResult();
            person = pcrTestData.getPatient();
            data[i][3] = person.getName() + " " + person.getSurname() + " " + person.getIdNumber() + " " + person.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            data[i][4] = pcrTestData.getWorkplaceCode();
            data[i][5] = pcrTestData.getDistrictCode();
            data[i][6] = pcrTestData.getRegionCode();
            data[i][7] = pcrTestData.getNote();
        }
        return new DefaultTableModel(data, pcrTestColumnNames);
    }

    public DefaultTableModel getPersonsTable() {
        ArrayList<Person> tests = personTree.inOrder();
        Object[][] data = new Object[tests.size()][7];
        for (int i = 0; i < tests.size(); i++) {
            data[i][0] = tests.get(i).getName();
            data[i][1] = tests.get(i).getSurname();
            data[i][2] = tests.get(i).getIdNumber();
            data[i][3] = tests.get(i).getDateOfBirth();
        }
        return new DefaultTableModel(data, personColumnNames);
    }

    public DefaultTableModel getSearchedTable() {
        return searchTableModel;
    }

    public void setSearchedTable() {
        searchTableModel = null;
    }

    public void generatePersons(String number) {
        ArrayList<Person> persons = generator.generatePersons(number);
        for (Person person : persons) {
            boolean outcome = personTree.insert(person);
            if (!outcome) {
                generator.deletePersonFromList(person);
                System.out.println("som tu");
            }
        }
    }

    public void generatePCRTest(String number) {
        ArrayList<PCRTestData> pcrTestsData = generator.generatePCRTestsData(number);
        for (PCRTestData pcrTestData : pcrTestsData) {
            Person person = pcrTestData.getPatient();
            Workplace workplace = workplaceTree.find(new Workplace(pcrTestData.getWorkplaceCode()));

            PCRTestUUID pcrTestUUID = new PCRTestUUID(pcrTestData);
            PCRTestDate pcrTestDate = new PCRTestDate(pcrTestData);
            boolean outcome = pcrTestUUIDTree.insert(pcrTestUUID);
            pcrTestDateTree.insert(pcrTestDate);

            person.getPcrTestDateTree().insert(pcrTestDate);
            person.getPcrTestUUIDTree().insert(pcrTestUUID);

            workplace.insertPCRTestDate(pcrTestDate);

            if (!outcome) {
                generator.deletePCRTestDataFromList(pcrTestData);
            }
        }
    }
}
