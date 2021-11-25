package controller;

import data.*;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.DateFormatter;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Controller {

    private static Program program;
    private static DefaultTableModel searchTableModel;



    public Controller(Program prog) {
        program = prog;
    }

    public static void addPerson(String[] inputs) {
        if (inputs[3] == null) {
            return;
        }
        Person person = new Person(inputs[0], inputs[1], inputs[2], LocalDate.parse(inputs[3]));
        boolean insert = program.getPersonTree().insert(person);
        if (insert) {
            program.getGenerator().addPersonToList(person);
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
        PCRTestData pcrTestData = new PCRTestData(uuid, LocalDateTime.parse(inputs[0]), inputs[1], person, workplace.getWorkplaceCode(), workplace.getDistrict().getDistrictCode(), workplace.getDistrict().getRegion().getRegionCode(), inputs[4]);
        PCRTestUUID pcrTestUUID = new PCRTestUUID(pcrTestData);
        PCRTestDate pcrTestDate = new PCRTestDate(pcrTestData);
        PCRTestNote pcrTestNote = new PCRTestNote(pcrTestData);

        program.getPcrTestUUIDTree().insert(pcrTestUUID);
        program.getPcrTestDateTree().insert(pcrTestDate);
        if (pcrTestData.getResult().equals("Positive")) {
            if (pcrTestData.getTestCode() == null) {

            }
            program.getPcrTestPositiveDateTree().insert(pcrTestDate);
        }
        //program.getGenerator().addPCRTestDataToList(pcrTestData);

        person.insertPCRTestUUID(pcrTestUUID);
        person.insertPCRTestDate(pcrTestDate);

        workplace.insertPCRTestDate(pcrTestDate);

        District district = program.getDistrictTree().find(workplace.getDistrict());
        district.insertPCRTestDate(pcrTestDate);
        district.getPcrTestNoteTree().insert(pcrTestNote);

        Region region = program.getRegionTree().find(district.getRegion());
        region.insertPCRTestDate(pcrTestDate);
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
                testsBasedOnNote(inputs);
            default:
                break;
        }
    }
    //20
    private static void testsBasedOnNote(String[] inputs) {
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
            person = pcrTestData.getPerson();
            if (person != null) {
                data[i][3] = person.getName() + " " + person.getSurname() + " " + person.getIdNumber() + " " + person.getDateOfBirth();
            } else {
                System.out.println("Person null(DateData) big ERROR");
            }
            data[i][4] = pcrTestData.getWorkplaceCode() + "";
            data[i][5] = pcrTestData.getDistrictCode() + "";
            data[i][6] = pcrTestData.getRegionCode() + "";
            data[i][7] = pcrTestData.getNote();
        }

        searchTableModel = new DefaultTableModel(data, program.getPcrTestColumnNames());
    }

    //2
    private static void searchPCRTestCode(String[] inputs) {
        ArrayList<PCRTestUUID> tests = new ArrayList<>();
        PCRTestUUID pcrTestUUID = program.getPcrTestUUIDTree().find(new PCRTestUUID(new PCRTestData(UUID.fromString(inputs[0]))));
        if (pcrTestUUID != null) {
            tests.add(pcrTestUUID);
        }
        String[][] data = new String[tests.size()][8];
        Person person;
        PCRTestData pcrTestData;
        for (int i = 0; i < tests.size(); i++) {
            pcrTestData = tests.get(i).getPcrTestData();
            data[i][0] = pcrTestData.getTestCode() + "";
            data[i][1] = pcrTestData.getDateAndTimeOfTest() + "";
            data[i][2] = pcrTestData.getResult();
            person = pcrTestData.getPerson();
            if (person != null) {
                data[i][3] = person.getName() + " " + person.getSurname() + " " + person.getIdNumber() + " " + person.getDateOfBirth();
            } else {
                System.out.println("Person null(2,16) big ERROR");
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
        ArrayList<PCRTestDate> tests = person.getPcrTestDateTree().inOrder();

        searchTableModel = new DefaultTableModel(getPCRTestDateDataToTable(tests), program.getPcrTestColumnNames());
    }

    //4
    private static void searchPCRTestPositiveMinMaxDateAndDistrictCode(String[] inputs) {
        District district = program.getDistrictTree().find(new District(Integer.parseInt(inputs[3])));
        ArrayList<PCRTestDate> tests = district.getPcrTestDatePositiveTree().getInterval(new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[5]),UUID.randomUUID())), new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]),UUID.randomUUID())));

        searchTableModel = new DefaultTableModel(getPCRTestDateDataToTable(tests), program.getPcrTestColumnNames());
    }

    //5
    private static void searchPCRTestMinMaxDateAndDistrictCode(String[] inputs) {
        District district = program.getDistrictTree().find(new District(Integer.parseInt(inputs[3])));
        ArrayList<PCRTestDate> tests = district.getPcrTestDateTree().getInterval(new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[5]),UUID.randomUUID())), new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]),UUID.randomUUID())));

        /*for (Workplace workplace: district.getWorkPlaceTree().inOrder()) {
            tests.addAll(workplace.getPcrTestDateTree().getInterval(new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]))), new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[7])))));
        }*/
        searchTableModel = new DefaultTableModel(getPCRTestDateDataToTable(tests), program.getPcrTestColumnNames());
    }

    //6
    private static void searchPCRTestPositiveMinMaxDateAndRegionCode(String[] inputs) {
        //ArrayList<Workplace> workplaces = getRegionWorkplaces(inputs);
        Region region = program.getRegionTree().find(new Region(Integer.parseInt(inputs[2])));
        ArrayList<PCRTestDate> tests = region.getPcrTestDatePositiveTree().getInterval(new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[5]),UUID.randomUUID())), new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]),UUID.randomUUID())));

        /*for (Workplace workplace : workplaces) {
            tests.addAll(workplace.getPcrTestDatePositiveTree().getInterval(new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]))), new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[7])))));
        }*/
        searchTableModel = new DefaultTableModel(getPCRTestDateDataToTable(tests), program.getPcrTestColumnNames());
    }


    //7
    private static void searchPCRTestMinMaxDateAndRegionCode(String[] inputs) {
        //ArrayList<Workplace> workplaces = getRegionWorkplaces(inputs);
        Region region = program.getRegionTree().find(new Region(Integer.parseInt(inputs[2])));
        ArrayList<PCRTestDate> tests = region.getPcrTestDateTree().getInterval(new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[5]),UUID.randomUUID())), new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]),UUID.randomUUID())));

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
        ArrayList<PCRTestDate> tests = program.getPcrTestPositiveDateTree().getInterval(new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[5]),UUID.randomUUID())), new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]),UUID.randomUUID())));
        searchTableModel = new DefaultTableModel(getPCRTestDateDataToTable(tests), program.getPcrTestColumnNames());
    }

    //9
    private static void searchPCRTestDate(String[] inputs) {
        ArrayList<PCRTestDate> tests = program.getPcrTestDateTree().getInterval(new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[5]),UUID.randomUUID())), new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]),UUID.randomUUID())));
        searchTableModel = new DefaultTableModel(getPCRTestDateDataToTable(tests), program.getPcrTestColumnNames());
    }

    //10
    private static void searchIllPersonAndDistrictCode(String[] inputs) {
        LocalDateTime personIllFrom = LocalDateTime.parse(inputs[6]);
        personIllFrom = personIllFrom.minusDays(Integer.parseInt(inputs[7]));
        System.out.println(personIllFrom);
        System.out.println(LocalDateTime.parse(inputs[6]));
        District district = program.getDistrictTree().find(new District(Integer.parseInt(inputs[3])));
        ArrayList<PCRTestDate> tests = district.getPcrTestDatePositiveTree().getInterval(new PCRTestDate(new PCRTestData(personIllFrom,UUID.randomUUID())), new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]),UUID.randomUUID())));
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
            persons.add(pcrTestDate.getPcrTestData().getPerson());
        }
        searchTableModel = getPersonsTable(persons);
    }

    //11
    private static void searchIllPersonAndRegionCode(String[] inputs) {
        LocalDateTime personIllFrom = LocalDateTime.parse(inputs[6]);
        personIllFrom = personIllFrom.minusDays(Integer.parseInt(inputs[7]));
        //ArrayList<Workplace> workplaces = getRegionWorkplaces(inputs);
        Region region = program.getRegionTree().find(new Region(Integer.parseInt(inputs[2])));
        ArrayList<PCRTestDate> tests = region.getPcrTestDatePositiveTree().getInterval(new PCRTestDate(new PCRTestData(personIllFrom,UUID.randomUUID())), new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]),UUID.randomUUID())));
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
        ArrayList<PCRTestDate> tests = program.getPcrTestPositiveDateTree().getInterval(new PCRTestDate(new PCRTestData(personIllFrom, UUID.randomUUID())), new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]),UUID.randomUUID())));
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
        districts.sort(Comparator.comparingInt(o -> o.getNumberOfIllPersons(inputs)));
        Collections.reverse(districts);

        String[][] data = new String[districts.size()][2];
        for (int i = 0; i < districts.size(); i++) {
            data[i][0] = districts.get(i).getDistrictCode() + "";
            data[i][1] = districts.get(i).getNumberOfIllPersons(inputs) + "";
            districts.get(i).setNumberOfIllPersons();
        }

        searchTableModel = new DefaultTableModel(data, program.getDistrictColumnNames());
    }

    //14
    private static void searchCountIllPersonPerRegion(String[] inputs) {
        ArrayList<Region> regions = program.getRegionTree().inOrder();
        regions.sort(Comparator.comparingInt(o -> o.getNumberOfIllPersons(inputs)));
        Collections.reverse(regions);

        String[][] data = new String[regions.size()][2];
        for (int i = 0; i < regions.size(); i++) {
            data[i][0] = regions.get(i).getRegionCode() + "";
            data[i][1] = regions.get(i).getNumberOfIllPersons(inputs) + "";
            regions.get(i).setNumberOfIllPersons();
        }

        searchTableModel = new DefaultTableModel(data, program.getRegionColumnNames());
    }

    //15
    private static void searchPCRTestMinMaxDateAndWorkplaceCode(String[] inputs) {
        Workplace workplace = program.getWorkplaceTree().find(new Workplace(Integer.parseInt(inputs[4])));
        ArrayList<PCRTestDate> tests = workplace.getPcrTestDateTree().getInterval(new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[5]),UUID.randomUUID())), new PCRTestDate(new PCRTestData(LocalDateTime.parse(inputs[6]),UUID.randomUUID())));
        searchTableModel = new DefaultTableModel(getPCRTestDateDataToTable(tests), program.getPcrTestColumnNames());
    }

    private static String[][] getPCRTestDateDataToTable(ArrayList<PCRTestDate> tests) {
        String[][] data = new String[tests.size()][8];
        Person person;
        PCRTestData pcrTestData;
        for (int i = 0; i < tests.size(); i++) {
            pcrTestData = tests.get(i).getPcrTestData();
            data[i][0] = pcrTestData.getTestCode() + "";
            data[i][1] = pcrTestData.getDateAndTimeOfTest() + "";
            data[i][2] = pcrTestData.getResult();
            person = pcrTestData.getPerson();
            if (person != null) {
                data[i][3] = person.getName() + " " + person.getSurname() + " " + person.getIdNumber() + " " + person.getDateOfBirth();
            } else {
                System.out.println("Person null(DateData) big ERROR");
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
            if (i % 100000  == 0) {
                System.out.println(i);
            }
            pcrTestData = tests.get(i).getPcrTestData();
            data[i][0] = pcrTestData.getTestCode() + "";
            data[i][1] = pcrTestData.getDateAndTimeOfTest() + "";
            data[i][2] = pcrTestData.getResult();
            person = pcrTestData.getPerson();
            if (person != null) {
                data[i][3] = person.getName() + " " + person.getSurname() + " " + person.getIdNumber() + " " + person.getDateOfBirth();
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
        PCRTestData pcrTestData = program.getPcrTestUUIDTree().find(new PCRTestUUID(new PCRTestData(UUID.fromString(pcrTestToCode)))).getPcrTestData();
        deletePCRTest(pcrTestData);
    }

    private static void deletePCRTest(PCRTestData pcrTestData) {
        if (pcrTestData != null) {
            PCRTestUUID pcrTestUUID = new PCRTestUUID(pcrTestData);
            PCRTestDate pcrTestDate = new PCRTestDate(pcrTestData);
            PCRTestNote pcrTestNote = new PCRTestNote(pcrTestData);

            program.getPcrTestUUIDTree().delete(pcrTestUUID);
            program.getPcrTestDateTree().delete(pcrTestDate);

            Workplace workplace = program.getWorkplaceTree().find(new Workplace(pcrTestData.getWorkplaceCode()));
            workplace.getPcrTestDateTree().delete(pcrTestDate);

            Person person = pcrTestData.getPerson();
            person.getPcrTestDateTree().delete(pcrTestDate);
            person.getPcrTestUUIDTree().delete(pcrTestUUID);

            //program.getGenerator().deletePCRTestDataFromList(pcrTestData);

            District district = program.getDistrictTree().find(workplace.getDistrict());
            district.getPcrTestDateTree().delete(pcrTestDate);
            district.getPcrTestNoteTree().delete(pcrTestNote);

            Region region = program.getRegionTree().find(district.getRegion());
            region.getPcrTestDateTree().delete(pcrTestDate);

            if (pcrTestData.getResult().equals("Positive")) {
                workplace.getPcrTestDatePositiveTree().delete(pcrTestDate);
                district.getPcrTestDatePositiveTree().delete(pcrTestDate);
                region.getPcrTestDatePositiveTree().delete(pcrTestDate);
                program.getPcrTestPositiveDateTree().delete(pcrTestDate);
            }
        }
    }

    public static void deletePerson(String idNumber) {
        Person person = program.getPersonTree().find(new Person(idNumber));
        ArrayList<PCRTestUUID> pcrTestUUIDS = person.getPcrTestUUIDTree().inOrder();
        for (PCRTestUUID pcrTestUUID : pcrTestUUIDS) {
            deletePCRTest(pcrTestUUID.getPcrTestData());
        }
        program.getPersonTree().delete(person);

        program.getGenerator().deletePersonFromList(person);
    }

    public static TableModel getSearchedTable() {
        return searchTableModel;
    }

    public static void save() {
        savePersons();
        savePcrTests();
    }

    private static void savePersons() {
        try (PrintWriter writer = new PrintWriter("persons.csv")) {

            StringBuilder sb = new StringBuilder();
            for (String columnName: program.getPersonColumnNames()) {
                sb.append(columnName);
                sb.append(";");
            }
            sb.append('\n');

            /*sb.append("Name");
            sb.append(";");
            sb.append("Surname");
            sb.append(";");
            sb.append("ID number");
            sb.append(";");
            sb.append("Birthday");
            sb.append('\n');*/

            for (Person person : program.getPersonTree().inOrder()) {
                sb.append(person.getName());
                sb.append(";");
                sb.append(person.getSurname());
                sb.append(";");
                sb.append(person.getIdNumber());
                sb.append(";");
                sb.append(person.getDateOfBirth());
                sb.append(";");
                sb.append('\n');
            }

            writer.write(sb.toString());

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void savePcrTests() {
        try (PrintWriter writer = new PrintWriter("pcrTests.csv")) {

            StringBuilder sb = new StringBuilder();
            for (String columnName: program.getPcrTestColumnNames()) {
                sb.append(columnName);
                sb.append(";");
            }
            sb.append('\n');
            /*sb.append("TestCode");
            sb.append(";");
            sb.append("TestTime");
            sb.append(";");
            sb.append("Result");
            sb.append(";");
            sb.append("Person ID number");
            sb.append(";");
            sb.append("Workplace Code");
            sb.append(";");
            sb.append("District Code");
            sb.append(";");
            sb.append("Region Code");
            sb.append(";");
            sb.append("Note");
            sb.append('\n');*/

            for (PCRTestUUID pcrTestUUID : program.getPcrTestUUIDTree().inOrder()) {
                PCRTestData data = pcrTestUUID.getPcrTestData();
                sb.append(data.getTestCode());
                sb.append(";");
                sb.append(data.getDateAndTimeOfTest());
                sb.append(";");
                sb.append(data.getResult());
                sb.append(";");
                sb.append(data.getPerson().getIdNumber());
                sb.append(";");
                sb.append(data.getWorkplaceCode());
                sb.append(";");
                sb.append(data.getDistrictCode());
                sb.append(";");
                sb.append(data.getRegionCode());
                sb.append(";");
                if (data.getNote().equals("")) {
                    sb.append(data.getNote()).append(" ");
                } else {
                    sb.append(data.getNote());
                }
                sb.append(";");
                sb.append('\n');
            }

            writer.write(sb.toString());

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void load() {
        loadPersons();
        loadPcrTests();
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
}
