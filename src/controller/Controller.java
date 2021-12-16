package controller;

import data.*;

import data.UFile;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Controller {

    private static Program program;
    private static DefaultTableModel searchTableModel;

    private static long nullValue = -100;
    public Controller(Program prog) {
        program = prog;
    }

    public static void addPerson(String[] inputs) {
        if (inputs[3] == null) {
            return;
        }
        Person person = new Person(inputs[0], inputs[1], inputs[2], LocalDate.parse(inputs[3]), program.getPcrTestDateFile());
        System.out.println(person.getName());
        program.insertPerson(person);

        if (program.getPersonTree().insert(person)) {
            program.getGenerator().addPersonToList(person);
        } else {
            program.deletePerson(person.getAddress());
        }
    }

    public static void addPCRTest(String[] inputs) {
        UUID uuid;
        if (inputs[5] == null) {
            uuid =  UUID.randomUUID();
        } else {
            uuid = UUID.fromString(inputs[5]);
        }
        Workplace workplace = program.getWorkplaceTree().find(new Workplace(Integer.parseInt(inputs[3])));
        Person person = program.getPersonTree().find(new Person(inputs[2]));
        District district = program.getDistrictFile().find(workplace.getDistrictAddress());
        Region region = program.getRegionFile().find(district.getRegionAddress());
        boolean result = false;
        if (inputs[1].equals("Positive")) {
            result = true;
        }

        PCRTestData pcrTestData = new PCRTestData(uuid, LocalDateTime.parse(inputs[0]), result, person.getAddress(), workplace.getWorkplaceCode(), district.getDistrictCode(), region.getRegionCode(), inputs[4]);

        program.insertPcrTestData(pcrTestData);
        PCRTestUUID pcrTestUUID = new PCRTestUUID(pcrTestData.getAddress(), pcrTestData.getTestCode());
        PCRTestDate pcrTestDate = new PCRTestDate(pcrTestData.getAddress(), pcrTestData.getDateAndTimeOfTest(), pcrTestData.getTestCode());
        //PCRTestNote pcrTestNote = new PCRTestNote(pcrTestData);

        program.getPcrTestUUIDFile().insert(pcrTestUUID);
        program.getPcrTestUUIDTree().insert(pcrTestUUID);

        program.getPcrTestDateFile().insert(pcrTestDate);
        program.getPcrTestDateTree().insert(pcrTestDate);

        person.insertPCRTestDate(pcrTestDate, program.getPcrTestDateFile());
        workplace.insertPCRTestDate(pcrTestDate, pcrTestData.getResult(), program.getPcrTestDateFile());
        district.insertPCRTestDate(pcrTestDate, pcrTestData.getResult(), program.getPcrTestDateFile());
        region.insertPCRTestDate(pcrTestDate, pcrTestData.getResult(), program.getPcrTestDateFile());

        if (pcrTestData.getResult()) {
            program.getPcrTestPositiveDateFile().insert(pcrTestDate);
            program.getPcrTestPositiveDateTree().insert(pcrTestDate);

            workplace.insertPCRTestDatePositive(pcrTestDate, pcrTestData.getResult(), program.getPcrTestPositiveDateFile());
            district.insertPCRTestDatePositive(pcrTestDate, pcrTestData.getResult(), program.getPcrTestPositiveDateFile());
            region.insertPCRTestDatePositive(pcrTestDate, pcrTestData.getResult(), program.getPcrTestPositiveDateFile());
        }
    }

    public static void generatePersons(String number) {
        program.generatePersons(number);
    }

    public static void generatePCRTests(String numberToGenerate) {
        program.generatePCRTests(numberToGenerate);
    }

    public static boolean checkNumberOfPersons() {
        if (program.getGenerator().getPersonsSize() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkIfPersonExists(String input) {
        if (program.getPersonTree().find(new Person(input)) == null) {
            return false;
        } else {
            return true;
        }
    }

    public static void search(String checkBoxText, String[] inputs) {
        switch (checkBoxText) {
            case "2":
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
            case "16":
                searchPCRTestCode(inputs);
                break;
            case "20":
                //testsBasedOnNote(inputs);
            default:
                break;
        }
    }
    //20
    /*private static void testsBasedOnNote(String[] inputs) {
        District district = program.getDistrictTree().find(new District(Integer.parseInt(inputs[3])));
        ArrayList<PCRTestNote> testsNote;
        testsNote =  district.getPcrTestNoteTree().inOrder();

        String[][] data = new String[testsNote.size()][8];
        Person person;
        PCRTestData pcrTestData;
        for (int i = 0; i < testsNote.size(); i++) {
            pcrTestData = testsNote.get(i).getPcrTestData();
            data[i][0] = pcrTestData.getTestCode() + "";
            data[i][1] = pcrTestData.getDateAndTimeOfTest() + "";
            data[i][2] = pcrTestData.getResult();
            person = program.getPerson(pcrTestData.getPerson());
            if (person != null) {
                data[i][3] = person.getName() + " " + person.getSurname() + " " + person.getIdNumber() + " " + person.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            } else {
                System.out.println("Person(UUID) null big ERROR");
            }
            data[i][4] = pcrTestData.getWorkplaceCode() + "";
            data[i][5] = pcrTestData.getDistrictCode() + "";
            data[i][6] = pcrTestData.getRegionCode() + "";
            data[i][7] = pcrTestData.getNote();
        }

        searchTableModel = new DefaultTableModel(data, program.getPcrTestColumnNames());
    }*/

    //2
    private static void searchPCRTestCode(String[] inputs) {
        ArrayList<PCRTestUUID> tests = new ArrayList<>();
        PCRTestUUID pcrTestUUID = program.getPcrTestUUIDTree().find(new PCRTestUUID(UUID.fromString(inputs[0])));
        if (pcrTestUUID != null) {
            tests.add(pcrTestUUID);
        }
        String[][] data = new String[tests.size()][8];
        Person person;
        PCRTestData pcrTestData;
        for (int i = 0; i < tests.size(); i++) {
            pcrTestData = program.getPcrTestData(pcrTestUUID.getPcrTestData());
            //pcrTestData = tests.get(i).getPcrTestData();
            data[i][0] = pcrTestData.getTestCode() + "";
            data[i][1] = pcrTestData.getDateAndTimeOfTest() + "";
            data[i][2] = pcrTestData.getResult() ? "Positive" : "Negative";
            person = program.getPerson(pcrTestData.getPerson());
            if (person != null) {
                data[i][3] = person.getName() + " " + person.getSurname() + " " + person.getIdNumber() + " " + person.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            } else {
                System.out.println("Person(UUID) null big ERROR");
            }
            data[i][4] = pcrTestData.getWorkplaceCode() + "";
            data[i][5] = pcrTestData.getDistrictCode() + "";
            data[i][6] = pcrTestData.getRegionCode() + "";
            data[i][7] = pcrTestData.getNote();
        }
        searchTableModel = new DefaultTableModel(data, program.getPcrTestColumnNames());
    }

    //3
    private static void searchPersonPCRTests(String[] inputs) {
        Person person = program.getPersonTree().find(new Person(inputs[1]));
        ArrayList<PCRTestDate> tests = person.getPcrTestDateTree(program.getPcrTestDateFile()).inOrder();

        searchTableModel = new DefaultTableModel(getPCRTestDateDataToTable(tests), program.getPcrTestColumnNames());
    }

    //4
    private static void searchPCRTestPositiveMinMaxDateAndDistrictCode(String[] inputs) {
        District district = program.getDistrictTree().find(new District(Integer.parseInt(inputs[3])));
        ArrayList<PCRTestDate> tests = district.getPcrTestDatePositiveTree(program.getPcrTestPositiveDateFile()).getInterval(new PCRTestDate(LocalDateTime.parse(inputs[5]),UUID.randomUUID()), new PCRTestDate(LocalDateTime.parse(inputs[6]),UUID.randomUUID()));

        searchTableModel = new DefaultTableModel(getPCRTestDateDataToTable(tests), program.getPcrTestColumnNames());
    }

    //5
    private static void searchPCRTestMinMaxDateAndDistrictCode(String[] inputs) {
        District district = program.getDistrictTree().find(new District(Integer.parseInt(inputs[3])));
        ArrayList<PCRTestDate> tests = district.getPcrTestDateTree(program.getPcrTestDateFile()).getInterval(new PCRTestDate(LocalDateTime.parse(inputs[5]),UUID.randomUUID()), new PCRTestDate(LocalDateTime.parse(inputs[6]),UUID.randomUUID()));

        /*for (Workplace workplace: district.getWorkPlaceTree().inOrder()) {
            tests.addAll(workplace.getPcrTestDateTree().getInterval(new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]))), new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[7])))));
        }*/
        searchTableModel = new DefaultTableModel(getPCRTestDateDataToTable(tests), program.getPcrTestColumnNames());
    }

    //6
    private static void searchPCRTestPositiveMinMaxDateAndRegionCode(String[] inputs) {
        //ArrayList<Workplace> workplaces = getRegionWorkplaces(inputs);
        Region region = program.getRegionTree().find(new Region(Integer.parseInt(inputs[2])));
        ArrayList<PCRTestDate> tests = region.getPcrTestDatePositiveTree(program.getPcrTestPositiveDateFile()).getInterval(new PCRTestDate(LocalDateTime.parse(inputs[5]),UUID.randomUUID()), new PCRTestDate(LocalDateTime.parse(inputs[6]),UUID.randomUUID()));

        /*for (Workplace workplace : workplaces) {
            tests.addAll(workplace.getPcrTestDatePositiveTree().getInterval(new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]))), new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[7])))));
        }*/
        searchTableModel = new DefaultTableModel(getPCRTestDateDataToTable(tests), program.getPcrTestColumnNames());
    }


    //7
    private static void searchPCRTestMinMaxDateAndRegionCode(String[] inputs) {
        //ArrayList<Workplace> workplaces = getRegionWorkplaces(inputs);
        Region region = program.getRegionTree().find(new Region(Integer.parseInt(inputs[2])));
        ArrayList<PCRTestDate> tests = region.getPcrTestDateTree(program.getPcrTestDateFile()).getInterval(new PCRTestDate(LocalDateTime.parse(inputs[5]),UUID.randomUUID()), new PCRTestDate(LocalDateTime.parse(inputs[6]),UUID.randomUUID()));

        /*for (Workplace workplace : workplaces) {
            tests.addAll(workplace.getPcrTestDateTree().getInterval(new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]))), new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[7])))));
        }*/
        searchTableModel = new DefaultTableModel(getPCRTestDateDataToTable(tests), program.getPcrTestColumnNames());
    }

    /*private static ArrayList<Workplace> getRegionWorkplaces(String[] inputs) {
        Region region = program.getRegionTree().find(new Region(Integer.parseInt(inputs[3])));
        ArrayList<District> districts = new ArrayList<>(region.getDistricts());
        ArrayList<Workplace> workplaces = new ArrayList<>();

        for (District district : districts) {
            workplaces.addAll(district.getWorkplaces());
        }
        return  workplaces;
    }*/

    //8
    private static void searchPCRTestPositiveDate(String[] inputs) {
        ArrayList<PCRTestDate> tests = program.getPcrTestPositiveDateTree().getInterval(new PCRTestDate(LocalDateTime.parse(inputs[5]),UUID.randomUUID()), new PCRTestDate(LocalDateTime.parse(inputs[6]),UUID.randomUUID()));
        searchTableModel = new DefaultTableModel(getPCRTestDateDataToTable(tests), program.getPcrTestColumnNames());
    }

    //9
    private static void searchPCRTestDate(String[] inputs) {
        ArrayList<PCRTestDate> tests = program.getPcrTestDateTree().getInterval(new PCRTestDate(LocalDateTime.parse(inputs[5]),UUID.randomUUID()), new PCRTestDate(LocalDateTime.parse(inputs[6]),UUID.randomUUID()));
        searchTableModel = new DefaultTableModel(getPCRTestDateDataToTable(tests), program.getPcrTestColumnNames());
    }

    //10
    private static void searchIllPersonAndDistrictCode(String[] inputs) {
        LocalDateTime personIllFrom = LocalDateTime.parse(inputs[6]);
        personIllFrom = personIllFrom.minusDays(Integer.parseInt(inputs[7]));
        System.out.println(personIllFrom);
        System.out.println(LocalDateTime.parse(inputs[6]));
        District district = program.getDistrictTree().find(new District(Integer.parseInt(inputs[3])));
        /*ArrayList<PCRTestDate> testDates = district.getPcrTestDatePositiveTree(program.getPcrTestPositiveDateFile()).inOrder();
        System.out.println();
        for (int i = 0; i < testDates.size(); i++) {
            System.out.println(testDates.get(i).getDateAndTimeOfTest());
        }*/

        ArrayList<PCRTestDate> tests = district.getPcrTestDatePositiveTree(program.getPcrTestPositiveDateFile()).getInterval(new PCRTestDate(personIllFrom,UUID.randomUUID()), new PCRTestDate(LocalDateTime.parse(inputs[6]),UUID.randomUUID()));
        getIllPersons(tests);

        /*for (Workplace workplace: district.getWorkPlaceTree().inOrder()) {
            tests.addAll(workplace.getPcrTestDatePositiveTree().getInterval(new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]))), new PCRTestDate(new PCRTestData(personIllFrom))));
        }*/

        /*ArrayList<Person> persons = new ArrayList<>();
        for (PCRTestDate pcrTestDate : tests) {
            persons.add(pcrTestDate.getPcrTestData().getPerson());
        }
        searchTableModel = getPersonsTable(persons);*/
    }

    private static void getIllPersons(ArrayList<PCRTestDate> tests) {
        ArrayList<Person> persons = new ArrayList<>();
        for (PCRTestDate pcrTestDate : tests) {
            persons.add(program.getPerson(program.getPcrTestData(pcrTestDate.getPcrTestData()).getPerson()));
        }
        searchTableModel = getPersonsTable(persons);
    }

    //11
    private static void searchIllPersonAndRegionCode(String[] inputs) {
        LocalDateTime personIllFrom = LocalDateTime.parse(inputs[6]);
        personIllFrom = personIllFrom.minusDays(Integer.parseInt(inputs[7]));
        //ArrayList<Workplace> workplaces = getRegionWorkplaces(inputs);
        Region region = program.getRegionTree().find(new Region(Integer.parseInt(inputs[2])));
        ArrayList<PCRTestDate> tests = region.getPcrTestDatePositiveTree(program.getPcrTestPositiveDateFile()).getInterval(new PCRTestDate(personIllFrom,UUID.randomUUID()), new PCRTestDate(LocalDateTime.parse(inputs[6]),UUID.randomUUID()));
        getIllPersons(tests);

        /*for (Workplace workplace : workplaces) {
            tests.addAll(workplace.getPcrTestDatePositiveTree().getInterval(new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]))), new PCRTestDate(new PCRTestData(personIllFrom))));
        }*/

        /*ArrayList<Person> persons = new ArrayList<>();
        for (PCRTestDate pcrTestDate : tests) {
            persons.add(pcrTestDate.getPcrTestData().getPerson());
        }
        searchTableModel = getPersonsTable(persons);*/
    }

    //12
    private static void searchIllPerson(String[] inputs) {
        LocalDateTime personIllFrom = LocalDateTime.parse(inputs[6]);
        personIllFrom = personIllFrom.minusDays(Integer.parseInt(inputs[7]));
        System.out.println(personIllFrom);
        System.out.println(LocalDateTime.parse(inputs[6]));
        ArrayList<PCRTestDate> tests = program.getPcrTestPositiveDateTree().getInterval(new PCRTestDate(personIllFrom, UUID.randomUUID()), new PCRTestDate(LocalDateTime.parse(inputs[6]),UUID.randomUUID()));
        System.out.println(tests.size());
        getIllPersons(tests);

        /*ArrayList<Person> persons = new ArrayList<>();
        for (PCRTestDate pcrTestDate : tests) {
            persons.add(pcrTestDate.getPcrTestData().getPerson());
        }

        searchTableModel = getPersonsTable(persons);*/
    }

    //13
    private static void searchCountIllPersonPerDistrict(String[] inputs) {
        ArrayList<District> districts = program.getDistrictTree().inOrder();
        districts.sort(Comparator.comparingInt(o -> o.getNumberOfIllPersons(inputs, program.getPcrTestPositiveDateFile())));
        Collections.reverse(districts);

        String[][] data = new String[districts.size()][2];
        for (int i = 0; i < districts.size(); i++) {
            data[i][0] = districts.get(i).getDistrictCode() + "";
            data[i][1] = districts.get(i).getNumberOfIllPersons(inputs, program.getPcrTestPositiveDateFile()) + "";
            districts.get(i).setNumberOfIllPersons();
        }

        searchTableModel = new DefaultTableModel(data, program.getDistrictColumnNames());
    }

    //14
    private static void searchCountIllPersonPerRegion(String[] inputs) {
        ArrayList<Region> regions = program.getRegionTree().inOrder();
        regions.sort(Comparator.comparingInt(o -> o.getNumberOfIllPersons(inputs, program.getPcrTestPositiveDateFile())));
        Collections.reverse(regions);

        String[][] data = new String[regions.size()][2];
        for (int i = 0; i < regions.size(); i++) {
            data[i][0] = regions.get(i).getRegionCode() + "";
            data[i][1] = regions.get(i).getNumberOfIllPersons(inputs, program.getPcrTestPositiveDateFile()) + "";
            regions.get(i).setNumberOfIllPersons();
        }

        searchTableModel = new DefaultTableModel(data, program.getRegionColumnNames());
    }

    //15
    private static void searchPCRTestMinMaxDateAndWorkplaceCode(String[] inputs) {
        Workplace workplace = program.getWorkplaceTree().find(new Workplace(Integer.parseInt(inputs[4])));
        ArrayList<PCRTestDate> tests = workplace.getPcrTestDateTree(program.getPcrTestDateFile()).getInterval(new PCRTestDate(LocalDateTime.parse(inputs[5]),UUID.randomUUID()), new PCRTestDate(LocalDateTime.parse(inputs[6]),UUID.randomUUID()));
        searchTableModel = new DefaultTableModel(getPCRTestDateDataToTable(tests), program.getPcrTestColumnNames());
    }

    private static String[][] getPCRTestDateDataToTable(ArrayList<PCRTestDate> tests) {
        //System.out.println(tests.size() + " getPCRTestDateDataToTable");
        String[][] data = new String[tests.size()][8];
        Person person;
        PCRTestData pcrTestData;
        for (int i = 0; i < tests.size(); i++) {
            pcrTestData = program.getPcrTestData(tests.get(i).getPcrTestData());
            //pcrTestData = tests.get(i).getPcrTestData();
            data[i][0] = pcrTestData.getTestCode() + "";
            data[i][1] = pcrTestData.getDateAndTimeOfTest() + "";
            data[i][2] = pcrTestData.getResult() ? "Positive" : "Negative";
            person = program.getPerson(pcrTestData.getPerson());
            if (person != null) {
                data[i][3] = person.getName() + " " + person.getSurname() + " " + person.getIdNumber() + " " + person.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            } else {
                System.out.println("Person(UUID) null big ERROR");
            }
            data[i][4] = pcrTestData.getWorkplaceCode() + "";
            data[i][5] = pcrTestData.getDistrictCode() + "";
            data[i][6] = pcrTestData.getRegionCode() + "";
            data[i][7] = pcrTestData.getNote();
        }
        return data;
    }

    public static DefaultTableModel getPCRTestsTable() {
        ArrayList<PCRTestUUID> tests = program.getPcrTestUUIDTree().inOrder();

        String[][] data = new String[tests.size()][8];
        Person person;
        PCRTestData pcrTestData;

        for (int i = 0; i < tests.size(); i++) {
            pcrTestData = program.getPcrTestData(tests.get(i).getPcrTestData());
            //pcrTestData = tests.get(i).getPcrTestData();
            data[i][0] = pcrTestData.getTestCode() + "";
            data[i][1] = pcrTestData.getDateAndTimeOfTest() + "";
            data[i][2] = pcrTestData.getResult() ? "Positive" : "Negative";
            person = program.getPerson(pcrTestData.getPerson());
            if (person != null) {
                data[i][3] = person.getName() + " " + person.getSurname() + " " + person.getIdNumber() + " " + person.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            } else {
                System.out.println("Person(UUID) null big ERROR");
            }
            data[i][4] = pcrTestData.getWorkplaceCode() + "";
            data[i][5] = pcrTestData.getDistrictCode() + "";
            data[i][6] = pcrTestData.getRegionCode() + "";
            data[i][7] = pcrTestData.getNote();
        }

        return new DefaultTableModel(data, program.getPcrTestColumnNames());
    }

    public static DefaultTableModel getPersonsTable(ArrayList<Person> people) {
        ArrayList<Person> persons;
        if (people == null) {
            persons = program.getPersonTree().inOrder();
        } else {
            persons = people;
        }

        String[][] data = new String[persons.size()][4];
        for (int i = 0; i < persons.size(); i++) {
            data[i][0] = persons.get(i).getName();
            data[i][1] = persons.get(i).getSurname();
            data[i][2] = persons.get(i).getIdNumber();
            data[i][3] = persons.get(i).getDateOfBirth() + "";
        }
        return new DefaultTableModel(data, program.getPersonColumnNames());
    }


    public static void deletePCRTest(String pcrTestToCode) {
        PCRTestUUID pcrTestUUID = program.getPcrTestUUIDTree().find(new PCRTestUUID(UUID.fromString(pcrTestToCode)));
        PCRTestData pcrTestData = program.getPcrTestData(pcrTestUUID.getPcrTestData());

        PCRTestDate pcrTestDate = program.getPcrTestDateTree().find(new PCRTestDate(pcrTestData.getDateAndTimeOfTest() , UUID.fromString(pcrTestToCode)));
        PCRTestDate pcrTestPositiveDate = program.getPcrTestPositiveDateTree().find(new PCRTestDate(pcrTestData.getDateAndTimeOfTest(), UUID.fromString(pcrTestToCode)));

        deletePCRTest(pcrTestData, pcrTestUUID, pcrTestDate, pcrTestPositiveDate);
    }

    private static void deletePCRTest(PCRTestData pcrTestData, PCRTestUUID pcrTestUUID, PCRTestDate pcrTestDate, PCRTestDate pcrTestPositiveDate) {
        if (pcrTestData != null) {
            //PCRTestNote pcrTestNote = new PCRTestNote(pcrTestData);

            Workplace workplace = program.getWorkplaceTree().find(new Workplace(pcrTestData.getWorkplaceCode()));
            workplace.getPcrTestDateTree(program.getPcrTestDateFile()).delete(pcrTestDate);

            //Person person = pcrTestData.getPerson();
            Person person = program.getPerson(pcrTestData.getPerson());
            person.getPcrTestDateTree(program.getPcrTestDateFile()).delete(pcrTestDate);
            //person.getPcrTestUUIDTree().delete(pcrTestUUID);

            //program.getGenerator().deletePCRTestDataFromList(pcrTestData);

            District district = program.getDistrictFile().find(workplace.getDistrictAddress());
            district.getPcrTestDateTree(program.getPcrTestDateFile()).delete(pcrTestDate);
            //district.getPcrTestNoteTree().delete(pcrTestNote);

            Region region = program.getRegionFile().find(district.getRegionAddress());
            region.getPcrTestDateTree(program.getPcrTestDateFile()).delete(pcrTestDate);

            program.getPcrTestUUIDTree().delete(pcrTestUUID);
            program.getPcrTestDateTree().delete(pcrTestDate);

            program.getPcrTestUUIDFile().delete(pcrTestUUID.getAddress());
            program.getPcrTestDateFile().delete(pcrTestDate.getAddress());

            if (pcrTestData.getResult()) {
                workplace.getPcrTestDatePositiveTree(program.getPcrTestPositiveDateFile()).delete(pcrTestPositiveDate);
                district.getPcrTestDatePositiveTree(program.getPcrTestPositiveDateFile()).delete(pcrTestPositiveDate);
                region.getPcrTestDatePositiveTree(program.getPcrTestPositiveDateFile()).delete(pcrTestPositiveDate);

                program.getPcrTestPositiveDateTree().delete(pcrTestPositiveDate);
                program.getPcrTestPositiveDateFile().delete(pcrTestPositiveDate.getAddress());
            }

            program.deletePcrTestData(pcrTestData.getAddress());
        }
    }

    public static void deletePerson(String idNumber) {
        Person person = program.getPersonTree().find(new Person(idNumber));
        ArrayList<PCRTestDate> pcrTestDates = person.getPcrTestDateTree(program.getPcrTestDateFile()).inOrder();
        for (PCRTestDate pcrTestDate : pcrTestDates) {
            deletePCRTest(pcrTestDate.getTestCode() + "");
        }
        program.getPersonTree().delete(person);

        program.deletePerson(person.getAddress());
        program.getGenerator().deletePersonFromList(person);
    }

    public static TableModel getSearchedTable() {
        return searchTableModel;
    }

    private static void loadPersons() {
        String[] personData;
        String line;
        String[] inputs = new String[4];

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader("persons.csv"));
            reader.readLine();
            while ((line = reader.readLine()) != null)
            {
                personData = line.split(";");
                inputs[0] = personData[0];
                inputs[1] = personData[1];
                inputs[2] = personData[2];
                inputs[3] = personData[3];
                addPerson(inputs);
            }
        }
        catch (IOException e)
        {
        }
    }

    private static void loadPcrTests() {
        String[] testData;
        String line;
        String[] inputs = new String[8];
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader("pcrTests.csv"));
            reader.readLine();
            while ((line = reader.readLine()) != null)
            {
                testData = line.split(";");
                inputs[5] = testData[0];  //UUID
                inputs[0] = testData[1];  //TestTime
                inputs[1] = testData[2];  //Result
                inputs[2] = testData[3];  //PersonID
                inputs[3] = testData[4];  //Workplace
                inputs[4] = testData[7];  //Note
                addPCRTest(inputs);
            }
        }
        catch (IOException e)
        {
        }
    }

    public static void sequentialListing(String selectedItem) {
        long tmp;

        if (selectedItem.equals("Person Tree")) {
            ArrayList<String[]> listing = program.getPersonTree().getSequentialListing();
            String[][] data = new String[listing.size()][8];

            for (int i = 0; i < listing.size(); i++) {
                data[i][0] = listing.get(i)[0];
                data[i][1] = listing.get(i)[1];
                data[i][2] = listing.get(i)[2];
                data[i][3] = listing.get(i)[3];
                tmp = Long.parseLong(listing.get(i)[4]);
                data[i][4] = tmp != nullValue ? program.getPerson(tmp).toString() : "";
                tmp = Long.parseLong(listing.get(i)[5]);
                data[i][5] = tmp != nullValue ? program.getPerson(tmp).toString() : "";
                data[i][6] = listing.get(i)[6];
                data[i][7] = listing.get(i)[7];
            }
            searchTableModel = new DefaultTableModel(data, program.getTreeColumnNames());
        }
        if (selectedItem.equals("PCRTestUUID Tree")) {
            ArrayList<String[]> listing = program.getPcrTestUUIDTree().getSequentialListing();
            String[][] data = new String[listing.size()][8];

            for (int i = 0; i < listing.size(); i++) {
                data[i][0] = listing.get(i)[0];
                data[i][1] = listing.get(i)[1];
                data[i][2] = listing.get(i)[2];
                data[i][3] = listing.get(i)[3];
                tmp = Long.parseLong(listing.get(i)[4]);
                data[i][4] = tmp != nullValue ? program.getPcrTestUUIDFile().find(tmp).toString() : "";
                tmp = Long.parseLong(listing.get(i)[5]);
                data[i][5] = tmp != nullValue ? program.getPcrTestUUIDFile().find(tmp).toString() : "";
                data[i][6] = listing.get(i)[6];
                data[i][7] = listing.get(i)[7];
            }
            searchTableModel = new DefaultTableModel(data, program.getTreeColumnNames());
        }
        if (selectedItem.equals("PCRTestDate Tree")) {
            ArrayList<String[]> listing = program.getPcrTestDateTree().getSequentialListing();
            String[][] data = new String[listing.size()][8];

            for (int i = 0; i < listing.size(); i++) {
                data[i][0] = listing.get(i)[0];
                data[i][1] = listing.get(i)[1];
                data[i][2] = listing.get(i)[2];
                data[i][3] = listing.get(i)[3];
                tmp = Long.parseLong(listing.get(i)[4]);
                data[i][4] = tmp != nullValue ? program.getPcrTestDateFile().find(tmp).toString() : "";
                tmp = Long.parseLong(listing.get(i)[5]);
                data[i][5] = tmp != nullValue ? program.getPcrTestDateFile().find(tmp).toString() : "";
                data[i][6] = listing.get(i)[6];
                data[i][7] = listing.get(i)[7];
            }
            searchTableModel = new DefaultTableModel(data, program.getTreeColumnNames());

        }
        if (selectedItem.equals("PCRTestPositiveDate Tree")) {

            ArrayList<String[]> listing = program.getPcrTestPositiveDateTree().getSequentialListing();
            String[][] data = new String[listing.size()][8];

            for (int i = 0; i < listing.size(); i++) {
                data[i][0] = listing.get(i)[0];
                data[i][1] = listing.get(i)[1];
                data[i][2] = listing.get(i)[2];
                data[i][3] = listing.get(i)[3];
                tmp = Long.parseLong(listing.get(i)[4]);
                data[i][4] = tmp != nullValue ? program.getPcrTestPositiveDateFile().find(tmp).toString() : "";
                tmp = Long.parseLong(listing.get(i)[5]);
                data[i][5] = tmp != nullValue ? program.getPcrTestPositiveDateFile().find(tmp).toString() : "";
                data[i][6] = listing.get(i)[6];
                data[i][7] = listing.get(i)[7];
            }
            searchTableModel = new DefaultTableModel(data, program.getTreeColumnNames());

        }
    }

    private static void showListing(ArrayList<String[]> sequentialListing) {

        String[][] data = new String[sequentialListing.size()][8];

        for (int i = 0; i < sequentialListing.size(); i++) {
            data[i][0] = sequentialListing.get(i)[0];
            data[i][1] = sequentialListing.get(i)[1];
            data[i][2] = sequentialListing.get(i)[2];
            data[i][3] = sequentialListing.get(i)[3];
            data[i][4] = sequentialListing.get(i)[4];
            data[i][5] = sequentialListing.get(i)[5];
            data[i][6] = sequentialListing.get(i)[6];
            data[i][7] = sequentialListing.get(i)[7];
        }
        searchTableModel = new DefaultTableModel(data, program.getTreeColumnNames());
    }
}
