package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Program {

    private TwoThreeTree<Person> personTree = new TwoThreeTree<>();
    private TwoThreeTree<PCRTestUUID> pcrTestUUIDTree = new TwoThreeTree<>();
    private TwoThreeTree<PCRTestDate> pcrTestDateTree = new TwoThreeTree<>();
    private TwoThreeTree<PCRTestDate> pcrTestPositiveDateTree = new TwoThreeTree<>();

    private TwoThreeTree<Workplace> workplaceTree = new TwoThreeTree<>();
    private TwoThreeTree<District> districtTree = new TwoThreeTree<>();
    private TwoThreeTree<Region> regionTree = new TwoThreeTree<>();

    private DefaultTableModel searchTableModel;
    private Generator generator = new Generator();

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

    public void addPCRTest(String[] inputs) {
        Workplace workplace = workplaceTree.find(new Workplace(Integer.parseInt(inputs[3])));
        Person person = personTree.find(new Person(inputs[2]));
        PCRTestData pcrTestData = new PCRTestData(UUID.randomUUID(), LocalDateTime.parse(inputs[0]), inputs[1], person, workplace.getWorkplaceCode(), workplace.getDistrict().getDistrictCode(), workplace.getDistrict().getRegion().getRegionCode(), inputs[4]);
        PCRTestUUID pcrTestUUID = new PCRTestUUID(pcrTestData);
        PCRTestDate pcrTestDate = new PCRTestDate(pcrTestData);

        pcrTestUUIDTree.insert(pcrTestUUID);
        pcrTestDateTree.insert(pcrTestDate);
        if (pcrTestData.getResult() == "Positive") {
            pcrTestPositiveDateTree.insert(pcrTestDate);
        }
        generator.addPCRTestDataToList(pcrTestData);

        person.insertPCRTestUUID(pcrTestUUID);
        person.insertPCRTestDate(pcrTestDate);

        workplace.insertPCRTestDate(pcrTestDate);
    }

    public void addPerson(String[] inputs) {
        Person person = new Person(inputs[0], inputs[1], inputs[2], LocalDate.parse(inputs[3]));
        boolean insert = personTree.insert(person);
        if (insert) {
            generator.addPersonToList(person);
        }
    }

    public void generatePCRTest(String numberToGenerate) {
        ArrayList<PCRTestData> pcrTestsData = generator.generatePCRTestsData(numberToGenerate);
        for (PCRTestData pcrTestData : pcrTestsData) {
            Person person = pcrTestData.getPerson();
            Workplace workplace = workplaceTree.find(new Workplace(pcrTestData.getWorkplaceCode()));

            PCRTestUUID pcrTestUUID = new PCRTestUUID(pcrTestData);
            PCRTestDate pcrTestDate = new PCRTestDate(pcrTestData);
            boolean outcome = pcrTestUUIDTree.insert(pcrTestUUID);
            pcrTestDateTree.insert(pcrTestDate);
            if (pcrTestData.getResult() == "Positive") {
                pcrTestPositiveDateTree.insert(pcrTestDate);
            }

            person.getPcrTestDateTree().insert(pcrTestDate);
            person.getPcrTestUUIDTree().insert(pcrTestUUID);

            workplace.insertPCRTestDate(pcrTestDate);

            if (!outcome) {
                generator.deletePCRTestDataFromList(pcrTestData);
            }
        }
    }

    public void generatePersons(String number) {
        ArrayList<Person> persons = generator.generatePersons(number);
        for (Person person : persons) {
            boolean outcome = personTree.insert(person);
            if (!outcome) {
                generator.deletePersonFromList(person);
            }
        }
    }

    public void search(String checkBoxText, String[] inputs) {
        switch (checkBoxText) {
            case "2":
            case "16":
                searchPCRTestCode(inputs);
                break;
            case "3":
                searchPersonPCRTests(inputs);
                break;
            case "4":
                searchPCRTestPositiveMinMaxDateAndDistrictCode(inputs);
                break;
            case "5":
                searchPCRTestMinMaxDateAndDistrictCode(inputs);
                break;
            case "6":
                searchPCRTestPositiveMinMaxDateAndRegionCode(inputs);
                break;
            case "7":
                searchPCRTestMinMaxDateAndRegionCode(inputs);
                break;
            case "8":
                searchPCRTestPositiveDate(inputs);
                break;
            case "9":
                searchPCRTestDate(inputs);
                break;
            case "10":
                searchIllPersonAndDistrictCode(inputs);
                break;
            case "11":
                searchIllPersonAndRegionCode(inputs);
                break;
            case "12":
                searchIllPerson(inputs);
                break;
            case "13":
                searchCountIllPersonPerDistrict(inputs);
                break;
            case "14":
                searchCountIllPersonPerRegion(inputs);
                break;
            case "15":
                searchPCRTestMinMaxDateAndWorkplaceCode(inputs);
                break;
            default:
                break;
        }
    }

    //2
    private void searchPCRTestCode(String[] inputs) {
        ArrayList<PCRTestUUID> test = new ArrayList<>();
        PCRTestUUID pcrTestUUID = pcrTestUUIDTree.find(new PCRTestUUID(new PCRTestData(UUID.fromString(inputs[1]))));
        if (pcrTestUUID != null) {
            test.add(pcrTestUUID);
        }
        Object[][] data = new Object[test.size()][8];
        Person person;
        PCRTestData pcrTestData;
        for (int i = 0; i < test.size(); i++) {
            pcrTestData = test.get(i).getPcrTestData();
            data[i][0] = pcrTestData.getTestCode();
            data[i][1] = pcrTestData.getDateAndTimeOfTest();
            data[i][2] = pcrTestData.getResult();
            person = pcrTestData.getPerson();
            if (person != null) {
                data[i][3] = person.getName() + " " + person.getSurname() + " " + person.getIdNumber() + " " + person.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            } else {
                System.out.println("Person null(2,16) big ERROR");
            }
            data[i][4] = pcrTestData.getWorkplaceCode();
            data[i][5] = pcrTestData.getDistrictCode();
            data[i][6] = pcrTestData.getRegionCode();
            data[i][7] = pcrTestData.getNote();
        }
        searchTableModel = new DefaultTableModel(data, pcrTestColumnNames);
    }

    //3
    private void searchPersonPCRTests(String[] inputs) {
        Person person = personTree.find(new Person(inputs[2]));
        ArrayList<PCRTestDate> tests = new ArrayList<>();
        tests.addAll(person.getPcrTestDateTree().inOrder());

        searchTableModel = new DefaultTableModel(getPCRTestDateDataToTable(tests), pcrTestColumnNames);
    }

    //4
    private void searchPCRTestPositiveMinMaxDateAndDistrictCode(String[] inputs) {
        District district = districtTree.find(new District(Integer.parseInt(inputs[4])));
        ArrayList<PCRTestDate> tests = new ArrayList<>();

        for (Workplace workplace: district.getWorkPlaceTree().inOrder()) {
            tests.addAll(workplace.getPcrTestDatePositiveTree().getInterval(new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]))), new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[7])))));
        }
        searchTableModel = new DefaultTableModel(getPCRTestDateDataToTable(tests), pcrTestColumnNames);
    }

    //5
    private void searchPCRTestMinMaxDateAndDistrictCode(String[] inputs) {
        District district = districtTree.find(new District(Integer.parseInt(inputs[4])));
        ArrayList<PCRTestDate> tests = new ArrayList<>();

        for (Workplace workplace: district.getWorkPlaceTree().inOrder()) {
            tests.addAll(workplace.getPcrTestDateTree().getInterval(new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]))), new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[7])))));
        }
        searchTableModel = new DefaultTableModel(getPCRTestDateDataToTable(tests), pcrTestColumnNames);
    }
    /*
    * inputs[0] = getSearchTestResultInput();
        inputs[1] = getTestCodeInput();
        inputs[2] = getPersonIdInput();
        inputs[3] = getRegionCodeInput();
        inputs[4] = getDistrictCodeInput();
        inputs[5] = getWorkplaceCodeInput();
        inputs[6] = getMinDateInput() + "T00:00";
        inputs[7] = getMaxDateInput() + "T23:59";
        inputs[8] = getIllTimeInput();*/

    //6
    private void searchPCRTestPositiveMinMaxDateAndRegionCode(String[] inputs) {
        ArrayList<Workplace> workplaces = getRegionWorkplaces(inputs);
        ArrayList<PCRTestDate> tests = new ArrayList<>();

        for (Workplace workplace : workplaces) {
            tests.addAll(workplace.getPcrTestDatePositiveTree().getInterval(new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]))), new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[7])))));
        }
        searchTableModel = new DefaultTableModel(getPCRTestDateDataToTable(tests), pcrTestColumnNames);
    }
    

    //7
    private void searchPCRTestMinMaxDateAndRegionCode(String[] inputs) {
        ArrayList<Workplace> workplaces = getRegionWorkplaces(inputs);
        ArrayList<PCRTestDate> tests = new ArrayList<>();

        for (Workplace workplace : workplaces) {
            tests.addAll(workplace.getPcrTestDateTree().getInterval(new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]))), new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[7])))));
        }
        searchTableModel = new DefaultTableModel(getPCRTestDateDataToTable(tests), pcrTestColumnNames);
    }

    private ArrayList<Workplace> getRegionWorkplaces(String[] inputs) {
        Region region = regionTree.find(new Region(Integer.parseInt(inputs[3])));
        ArrayList<District> districts = new ArrayList<>();
        districts.addAll(region.getDistricts());
        ArrayList<Workplace> workplaces = new ArrayList<>();

        for (District district : districts) {
            workplaces.addAll(district.getWorkplaces());
        }
        return  workplaces;
    }

    //8
    private void searchPCRTestPositiveDate(String[] inputs) {
        ArrayList<PCRTestDate> tests = new ArrayList<>();
        tests.addAll(pcrTestPositiveDateTree.getInterval(new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]))), new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[7])))));
        searchTableModel = new DefaultTableModel(getPCRTestDateDataToTable(tests), pcrTestColumnNames);
    }

    //9
    private void searchPCRTestDate(String[] inputs) {
        ArrayList<PCRTestDate> tests = new ArrayList<>();
        tests.addAll(pcrTestDateTree.getInterval(new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]))), new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[7])))));
        searchTableModel = new DefaultTableModel(getPCRTestDateDataToTable(tests), pcrTestColumnNames);
    }
    
    //10
    private void searchIllPersonAndDistrictCode(String[] inputs) {
        LocalDateTime personIllTo = LocalDateTime.parse(inputs[6]);
        personIllTo = personIllTo.plusDays(Integer.parseInt(inputs[8]));
        District district = districtTree.find(new District(Integer.parseInt(inputs[4])));
        ArrayList<PCRTestDate> tests = new ArrayList<>();

        for (Workplace workplace: district.getWorkPlaceTree().inOrder()) {
            tests.addAll(workplace.getPcrTestDatePositiveTree().getInterval(new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]))), new PCRTestDate(new PCRTestData(personIllTo))));
        }

        ArrayList<Person> persons = new ArrayList<>();
        for (PCRTestDate pcrTestDate : tests) {
            persons.add(pcrTestDate.getPcrTestData().getPerson());
        }
        searchTableModel = getPersonsTable(persons);
    }
    
    //11
    private void searchIllPersonAndRegionCode(String[] inputs) {
        LocalDateTime personIllTo = LocalDateTime.parse(inputs[6]);
        personIllTo = personIllTo.plusDays(Integer.parseInt(inputs[8]));
        ArrayList<Workplace> workplaces = getRegionWorkplaces(inputs);
        ArrayList<PCRTestDate> tests = new ArrayList<>();

        for (Workplace workplace : workplaces) {
            tests.addAll(workplace.getPcrTestDatePositiveTree().getInterval(new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]))), new PCRTestDate(new PCRTestData(personIllTo))));
        }

        ArrayList<Person> persons = new ArrayList<>();
        for (PCRTestDate pcrTestDate : tests) {
            persons.add(pcrTestDate.getPcrTestData().getPerson());
        }
        searchTableModel = getPersonsTable(persons);
    }

    //12
    private void searchIllPerson(String[] inputs) {
        LocalDateTime personIllTo = LocalDateTime.parse(inputs[6]);
        personIllTo = personIllTo.plusDays(Integer.parseInt(inputs[8]));
        ArrayList<PCRTestDate> tests = new ArrayList<>();
        tests.addAll(pcrTestPositiveDateTree.getInterval(new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]))), new PCRTestDate(new PCRTestData(personIllTo))));

        ArrayList<Person> persons = new ArrayList<>();
        for (PCRTestDate pcrTestDate : tests) {
            persons.add(pcrTestDate.getPcrTestData().getPerson());
        }

        searchTableModel = getPersonsTable(persons);
    }

    //13
    private void searchCountIllPersonPerDistrict(String[] inputs) {
        ArrayList<District> districts = districtTree.inOrder();
        districts.sort(Comparator.comparingInt(o -> o.getNumberOfIllPersons(inputs)));
        Collections.reverse(districts);

        Object[][] data = new Object[districts.size()][2];
        for (int i = 0; i < districts.size(); i++) {
            data[i][0] = districts.get(i).getDistrictCode();
            data[i][1] = districts.get(i).getNumberOfIllPersons(inputs);
        }

        searchTableModel = new DefaultTableModel(data, districtColumnNames);
    }

    //14
    private void searchCountIllPersonPerRegion(String[] inputs) {
        ArrayList<Region> regions = regionTree.inOrder();
        regions.sort(Comparator.comparingInt(o -> o.getNumberOfIllPersons(inputs)));
        Collections.reverse(regions);

        Object[][] data = new Object[regions.size()][2];
        for (int i = 0; i < regions.size(); i++) {
            data[i][0] = regions.get(i).getRegionCode();
            data[i][1] = regions.get(i).getNumberOfIllPersons(inputs);
        }

        searchTableModel = new DefaultTableModel(data, regionColumnNames);
    }

    //15
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
            person = pcrTestData.getPerson();
            if (person != null) {
                data[i][3] = person.getName() + " " + person.getSurname() + " " + person.getIdNumber() + " " + person.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            } else {
                System.out.println("Person null(DateData) big ERROR");
            }
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
            person = pcrTestData.getPerson();
            if (person != null) {
                data[i][3] = person.getName() + " " + person.getSurname() + " " + person.getIdNumber() + " " + person.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            } else {
                System.out.println("Person(Tests) null big ERROR");
            }
            data[i][4] = pcrTestData.getWorkplaceCode();
            data[i][5] = pcrTestData.getDistrictCode();
            data[i][6] = pcrTestData.getRegionCode();
            data[i][7] = pcrTestData.getNote();
        }
        return new DefaultTableModel(data, pcrTestColumnNames);
    }

    public DefaultTableModel getPersonsTable(ArrayList<Person> people) {
        ArrayList<Person> persons;
        if (people == null) {
            persons = personTree.inOrder();
        } else {
            persons = people;
        }

        Object[][] data = new Object[persons.size()][4];
        for (int i = 0; i < persons.size(); i++) {
            data[i][0] = persons.get(i).getName();
            data[i][1] = persons.get(i).getSurname();
            data[i][2] = persons.get(i).getIdNumber();
            data[i][3] = persons.get(i).getDateOfBirth();
        }
        return new DefaultTableModel(data, personColumnNames);
    }

    public DefaultTableModel getSearchedTable() {
        return searchTableModel;
    }

    public boolean checkIfPersonExists(String input) {
        if (personTree.find(new Person(input)) == null) {
            return false;
        } else {
            return true;
        }
    }

    public void deletePCRTest(String pcrTestToCode) {
        PCRTestData pcrTestData = pcrTestUUIDTree.find(new PCRTestUUID(new PCRTestData(UUID.fromString(pcrTestToCode)))).getPcrTestData();
        deletePCRTest(pcrTestData);
    }

    private void deletePCRTest(PCRTestData pcrTestData) {
        if (pcrTestData != null) {
            PCRTestUUID pcrTestUUID = new PCRTestUUID(pcrTestData);
            PCRTestDate pcrTestDate = new PCRTestDate(pcrTestData);
            pcrTestUUIDTree.delete(pcrTestUUID);

            Workplace workplace = workplaceTree.find(new Workplace(pcrTestData.getWorkplaceCode()));
            workplace.getPcrTestDateTree().delete(pcrTestDate);
            workplace.getPcrTestDatePositiveTree().delete(pcrTestDate);

            Person person = pcrTestData.getPerson();
            person.getPcrTestDateTree().delete(pcrTestDate);
            person.getPcrTestUUIDTree().delete(pcrTestUUID);

            generator.deletePCRTestDataFromList(pcrTestData);
        }
    }

    public void deletePerson(String idNumber) {
        Person person = personTree.find(new Person(idNumber));
        ArrayList<PCRTestUUID> pcrTestUUIDS = person.getPcrTestUUIDTree().inOrder();
        for (PCRTestUUID pcrTestUUID : pcrTestUUIDS) {
            deletePCRTest(pcrTestUUID.getPcrTestData());
        }
        personTree.delete(person);

        generator.deletePersonFromList(person);
    }

    public TwoThreeTree<Person> getPersonTree() {
        return personTree;
    }

    public Generator getGenerator() {
        return generator;
    }

    public void save() {

        try (PrintWriter writer = new PrintWriter("persons.csv")) {

            StringBuilder sb = new StringBuilder();
            sb.append("Name");
            sb.append(",");
            sb.append("Surname");
            sb.append(",");
            sb.append("ID number");
            sb.append(",");
            sb.append("Birthday");
            sb.append('\n');

            for (Person person : personTree.inOrder()) {
                sb.append(person.getName());
                sb.append(",");
                sb.append(person.getSurname());
                sb.append(",");
                sb.append(person.getIdNumber());
                sb.append(",");
                sb.append(person.getDateOfBirth());
                sb.append('\n');
            }

            writer.write(sb.toString());

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void load() throws IOException {
        String[] personData;
        String line;
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader("persons.csv"));
            reader.readLine();
            while ((line = reader.readLine()) != null)
            {
                personData = line.split(",");
                Person person = new Person(personData[0], personData[1], personData[2], LocalDate.parse(personData[3]));
                personTree.insert(person);
            }
        }
        catch (FileNotFoundException e)
        {
        }
    }
}
