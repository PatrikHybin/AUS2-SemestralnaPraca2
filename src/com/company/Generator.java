package com.company;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class Generator {
    private ArrayList<Region> regions = new ArrayList<>();
    private ArrayList<District> districts = new ArrayList<>();
    private ArrayList<Workplace> workplaces = new ArrayList<>();
    private ArrayList<Person> persons = new ArrayList<>();
    private ArrayList<PCRTestData> pcrTestsData = new ArrayList<>();
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

    public Generator() {
        generateRegions();
        generateDistricts();
        generateWorkplaces();
    }

    private void generateRegions() {
        for (int i = 0; i < 8; i++) {
            regions.add(new Region(i + 1));
        }

    }

    private void generateDistricts() {
        int tmp;
        for (int i = 0; i < regions.size(); i++) {
            for (int j = 0; j < 9; j++) {
                tmp = j + 1;
                District district = new District(Integer.parseInt(regions.get(i).getRegionCode() + "" + tmp));
                districts.add(district);
                regions.get(i).insertDistrict(district);
                regions.get(i).getDistricts().add(district);
                district.setRegion(regions.get(i));
            }
        }
    }

    private void generateWorkplaces() {
        int tmp;
        for (int i = 0; i < districts.size(); i++) {
            for (int j = 0; j < 3; j++) {
                tmp = j + 1;
                Workplace workplace = new Workplace(Integer.parseInt(districts.get(i).getDistrictCode() + "" + tmp));
                workplaces.add(workplace);
                districts.get(i).insertWorkplace(workplace);
                districts.get(i).getWorkplaces().add(workplace);
                workplace.setDistrict(districts.get(i));
            }
        }
    }

    public ArrayList<Person> generatePersons(String number) {
        ArrayList<Person> tmp = new ArrayList<>();
        int generateNum;
        if (number.equals("")) {
            generateNum = 0;
        } else {
            generateNum = Integer.parseInt(number);
        }
        Random r = new Random();
        LocalDate baseDate = LocalDate.of(1950, 1, 1);
        String name;
        String surname;
        int randomDays;
        int maxDays = 55*365;
        LocalDate birthday;
        //r.nextInt() % (100 - 1) + 1

        for (int i = 0; i < generateNum; i++) {
            name = names[r.nextInt(names.length)];
            surname = names[r.nextInt(names.length)];
            randomDays = (int)(maxDays*Math.random());
            birthday = baseDate.plusDays(randomDays);
            String idNumber = DateTimeFormatter.ofPattern("yyMMdd").format(birthday) + "/" + UUID.randomUUID().toString().substring(0,4);
            Person person = new Person(name, surname, idNumber ,birthday);
            persons.add(person);
            tmp.add(person);
        }
        return tmp;
    }



    public ArrayList<PCRTestData> generatePCRTestsData(String number) {
        ArrayList<PCRTestData> tmp = new ArrayList<>();
        int generateNum;
        if (number.equals("")) {
            generateNum = 0;
        } else {
            generateNum = Integer.parseInt(number);
        }
        Random r = new Random();
        Person person;

        LocalDate baseDate = LocalDate.of(2019, 1, 1);
        LocalDate localDate;
        int randomDays;
        int maxDays = 2*365;
        LocalDateTime timeOfTest;

        String hour;
        String minutes;
        String result;
        Workplace workplace;
        District district;
        Region region;

        for (int i = 0; i < generateNum; i++) {
            person = persons.get(r.nextInt(persons.size()));
            randomDays = (int)(maxDays*Math.random());
            localDate = baseDate.plusDays(randomDays);
            timeOfTest = LocalDateTime.parse(localDate + "T" + String.format("%02d", r.nextInt(24)) + ":" + String.format("%02d",  r.nextInt(2) * 30));
            if (r.nextInt(100) < 50) {
                result = "Positive";
            } else {
                result = "Negative";
            }
            region = regions.get(r.nextInt(regions.size()));
            district = region.getDistricts().get(r.nextInt(region.getDistricts().size()));
            workplace = district.getWorkplaces().get(r.nextInt(district.getWorkplaces().size()));

            PCRTestData pcrTestData = new PCRTestData(UUID.randomUUID(), timeOfTest, result, person, workplace.getWorkplaceCode(), district.getDistrictCode() ,region.getRegionCode(), "Generated");
            tmp.add(pcrTestData);
            pcrTestsData.add(pcrTestData);
        }
        return tmp;
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

    public void deletePCRTestDataFromList(PCRTestData pcrTestData) {
        pcrTestsData.remove(pcrTestData);
    }
}
