package data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Generator {
    private ArrayList<Region> regions = new ArrayList<>();
    private ArrayList<District> districts = new ArrayList<>();
    private ArrayList<Workplace> workplaces = new ArrayList<>();
    private ArrayList<Person> persons = new ArrayList<>();
    private Program program;

    private String[] names = new String[] {"James", "John", "Robert", "Michael", "William", "David", "Richard", "Joseph", "Thomas", "Charles", "Christopher","Ashley","Jacob","Lisa",
            "Daniel", "Matthew", "Anthony", "Donald", "Mark", "Paul", "Steven", "Andrew", "Kenneth", "Joshua", "Kevin", "Brian", "Edward", "Ronald", "Timothy", "Jason", "Jeffrey", "Ryan",
            "Jonathan", "Stephen", "Larry", "Justin", "Scott", "Brandon", "Mary", "Patricia", "Jennifer", "Linda", "Elizabeth", "Barbara", "Susan", "Jessica", "Sarah", "Karen", "Nancy",
            "Dorothy", "Kimberly", "Emily", "Donna", "Michelle", "Carol", "Amanda", "Melissa", "Deborah", "Stephanie", "Rebecca", "Laura", "Sharon", "Cynthia", "Kathleen", "Amy", "Shirley",
            "Pamela", "Nicole", "Samantha", "James", "Robert", "John", "Michael", "William", "David", "Richard", "Joseph", "Thomas", "Charles", "Christopher", "Daniel", "Matthew", "Anthony",
            "Mark", "Donald", "Andrew", "Joshua", "Kenneth", "Kevin", "Brian", "George", "Edward", "Ronald", "Timothy", "Jason", "Jeffrey", "Ryan", "Jacob", "Gary", "Nicholas", "Eric",
            "Stephen", "Larry", "Justin", "Scott", "Brandon", "Benjamin", "Samuel", "Gregory", "Frank", "Alexander", "Raymond", "Patrick", "Jack", "Gary", "Nicholas", "Eric","Sandra",
            "Dennis", "Jerry", "Tyler", "Aaron", "Jose", "Adam", "Henry", "Nathan", "Douglas", "Zachary", "Peter", "Kyle", "Walter", "Ethan", "Jeremy", "Harold","Margaret", "Betty",
            "Keith", "Christian", "Roger", "Noah", "Gerald", "Carl", "Terry", "Sean", "Austin", "Arthur", "Lawrence", "Jesse", "Dylan", "Bryan", "Joe", "Jordan", "Angela", "Helen", "Anna",
            "Billy", "Bruce", "Albert", "Willie", "Gabriel", "Logan", "Alan", "Juan", "Wayne", "Roy", "Ralph", "Randy", "Eugene", "Vincent", "Russell", "Elijah","Brenda","Jonathan",
            "Louis", "Bobby", "Philip", "Johnny", "Mary", "Patricia", "Jennifer", "Linda", "Elizabeth", "Barbara", "Susan", "Jessica", "Sarah", "Karen", "Nancy", "Lisa", "Betty", "Margaret",
            "Sandra", "Ashley", "Kimberly", "Emily", "Donna", "Michelle", "Dorothy", "Carol", "Amanda", "Melissa", "Deborah", "Stephanie", "Rebecca", "Sharon", "Laura", "Cynthia", "Kathleen",
            "Amy", "Shirley", "Angela", "Helen", "Anna", "Brenda", "Pamela", "Nicole", "Emma", "Samantha", "Katherine", "Christine", "Debra", "Rachel", "Catherine", "Carolyn", "Janet",
            "Ruth", "Maria", "Heather", "Diane", "Virginia", "Julie", "Joyce", "Victoria", "Olivia", "Kelly", "Christina", "Lauren", "Joan", "Evelyn", "Judith", "Megan", "Cheryl", "Andrea",
            "Hannah", "Martha", "Jacqueline", "Frances", "Gloria", "Ann", "Teresa", "Kathryn", "Sara", "Janice", "Jean", "Alice", "Madison", "Doris", "Abigail", "Julia", "Judy", "Grace",
            "Denise", "Amber", "Marilyn", "Beverly", "Danielle", "Theresa", "Sophia", "Marie", "Diana", "Brittany", "Natalie", "Isabella", "Charlotte", "Rose", "Alexis", "Kayla"};

    public Generator(Program program) {
        this.program = program;
        generateRegions();
        generateDistricts();
        generateWorkplaces();
    }

    private void generateRegions() {
        if (this.program.getRegionFile().getLength() == 0) {
            for (int i = 0; i < 8; i++) {
                Region region = new Region(i + 1);
                regions.add(region);
                this.program.getRegionFile().insert(region);
                this.program.getRegionTree().insert(region);
            }

        }

    }

    private void generateDistricts() {
        if (this.program.getDistrictFile().getLength() == 0) {
            int tmp;
            for (int i = 0; i < regions.size(); i++) {
                for (int j = 0; j < 9; j++) {
                    tmp = j + 1;
                    District district = new District(Integer.parseInt(regions.get(i).getRegionCode() + "" + tmp));
                    districts.add(district);
                    regions.get(i).getDistricts().add(district);
                    district.setRegion(regions.get(i));
                    this.program.getDistrictFile().insert(district);
                    this.program.getDistrictTree().insert(district);
                }
            }

        }

    }

    private void generateWorkplaces() {
        if (this.program.getWorkplaceFile().getLength() == 0) {
            int tmp;
            for (int i = 0; i < districts.size(); i++) {
                for (int j = 0; j < 3; j++) {
                    tmp = j + 1;
                    Workplace workplace = new Workplace(Integer.parseInt(districts.get(i).getDistrictCode() + "" + tmp));
                    workplaces.add(workplace);
                    districts.get(i).getWorkplaces().add(workplace);
                    workplace.setDistrict(districts.get(i));
                    this.program.getWorkplaceFile().insert(workplace);
                    this.program.getWorkplaceTree().insert(workplace);
                }
            }

        }

    }

    public void generatePersons(String number) {
        if (persons.size() == 0) {
            persons.addAll(program.getPersonTree().inOrder());
            System.out.println(persons.size() + " v generatePerson");
        }
        int generateNum;
        if (number.equals("")) {
            generateNum = 0;
        } else {
            generateNum = Integer.parseInt(number);
            if (persons == null) {
                persons = new ArrayList<>(Integer.parseInt(number));
            }
        }
        Random r = new Random();
        LocalDate baseDate = LocalDate.of(1940, 1, 1);
        String name;
        String surname;
        int randomDays;
        int maxDays = 70*365;
        LocalDate birthday;
        //r.nextInt() % (100 - 1) + 1

        for (int i = 0; i < generateNum; i++) {
            name = names[r.nextInt(names.length)];
            surname = names[r.nextInt(names.length)];
            randomDays = (int)(maxDays*Math.random());
            birthday = baseDate.plusDays(randomDays);
            String idNumber = DateTimeFormatter.ofPattern("yyMMdd").format(birthday) + "-" + UUID.randomUUID().toString().substring(0,6);

            Person person = new Person(name, surname, idNumber ,birthday, program.getPcrTestDateFile());

            program.insertPerson(person);

            if (program.getPersonTree().insert(person)) {
                persons.add(person);
            } else {
                deletePersonFromList(person);
                program.deletePerson(person.getAddress());
            }
        }
    }


    public void generatePCRTests(String number) {
        int generateNum;
        if (number.equals("")) {
            generateNum = 0;
        } else {
            generateNum = Integer.parseInt(number);
        }
        Random r = new Random();
        Person person;

        LocalDateTime timeOfTest;
        LocalDate baseDate;

        LocalTime time;
        boolean result;
        Workplace workplace;
        District district;
        Region region;

        ArrayList<Person> treePersons = program.getPersonTree().inOrder();
        System.out.println(treePersons.size() + " pocet osob");
        for (int i = 0; i < generateNum; i++) {
            person = program.getPerson(treePersons.get(r.nextInt(treePersons.size())).getAddress());
            baseDate = LocalDate.now().minusDays(r.nextInt(1000));
            time = LocalTime.MIN.plusSeconds(r.nextLong());
            timeOfTest = LocalDateTime.parse(baseDate + "T" + time.toString().substring(0,5));

            //timeOfTest = LocalDateTime.parse(localDate + "T" + String.format("%02d", r.nextInt(24)) + ":" + String.format("%02d",  r.nextInt(2) * 30));
            if (r.nextInt(100) < 50) {
                result = true;
            } else {
                result = false;
            }

            //workplace = program.getWorkplaceTree().inOrder().get(r.nextInt(workplaces.size()));
            ArrayList<Workplace> workplaces = program.getWorkplaceTree().inOrder();

            workplace = workplaces.get(r.nextInt(workplaces.size()));
            district = program.getDistrictFile().find(workplace.getDistrictAddress());
            region = program.getRegionFile().find(district.getRegionAddress());


            PCRTestData pcrTestData = new PCRTestData(UUID.randomUUID(), timeOfTest, result, person.getAddress(), workplace.getWorkplaceCode(), district.getDistrictCode() ,region.getRegionCode(), "Generated");

            program.insertPcrTestData(pcrTestData);

            PCRTestUUID pcrTestUUID = new PCRTestUUID(pcrTestData.getAddress(), pcrTestData.getTestCode());
            PCRTestDate pcrTestDate = new PCRTestDate(pcrTestData.getAddress(), pcrTestData.getDateAndTimeOfTest(), pcrTestData.getTestCode());
            //PCRTestNote pcrTestNote = new PCRTestNote(pcrTestData);

            program.getPcrTestUUIDFile().insert(pcrTestUUID);
            program.getPcrTestUUIDTree().insert(pcrTestUUID);

            //program.getPcrTestDateFile().insert(pcrTestDate);
            program.getPcrTestDateFile().insert(pcrTestDate);
            program.getPcrTestDateTree().insert(pcrTestDate);

            person.insertPCRTestDate(pcrTestDate, program.getPcrTestDateFile());


            workplace.insertPCRTestDate(pcrTestDate, pcrTestData.getResult(), program.getPcrTestDateFile());
            district.insertPCRTestDate(pcrTestDate, pcrTestData.getResult(), program.getPcrTestDateFile());
            region.insertPCRTestDate(pcrTestDate, pcrTestData.getResult(), program.getPcrTestDateFile());


            if (result) {

                program.getPcrTestPositiveDateFile().insert(pcrTestDate);
                program.getPcrTestPositiveDateTree().insert(pcrTestDate);

                region.insertPCRTestDatePositive(pcrTestDate, pcrTestData.getResult(), program.getPcrTestPositiveDateFile());
                district.insertPCRTestDatePositive(pcrTestDate, pcrTestData.getResult(), program.getPcrTestPositiveDateFile());
                workplace.insertPCRTestDatePositive(pcrTestDate, pcrTestData.getResult(), program.getPcrTestPositiveDateFile());

            }
        }
    }

    public ArrayList<Region> getRegions() {
        return regions;
    }

    public ArrayList<District> getDistricts() {
        return districts;
    }

    public ArrayList<Workplace> getWorkplaces() {
        return workplaces;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void deletePersonFromList(Person person) {
        persons.remove(person);
    }

    public void addPersonToList(Person person) {
        persons.add(person);
    }

    public int getPersonsSize() {
        return persons.size();
    }

    /*public void deletePCRTestDataFromList(PCRTestData pcrTestData) {
        pcrTestsData.remove(pcrTestData);
    }

    public void addPCRTestDataToList(PCRTestData pcrTestData) {
        pcrTestsData.add(pcrTestData);
    }*/
}
