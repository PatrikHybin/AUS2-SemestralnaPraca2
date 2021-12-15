package com;

import data.*;

import java.time.LocalDate;
import java.util.*;

public class Tester {
    private BTree<PCRTestUUID> tree = new BTree<>(PCRTestUUID.class);

    public static TwoThreeTree<Person> treePersons = new TwoThreeTree<>();
    private ArrayList<PCRTestUUID> pcrTests = new ArrayList<>();
    private ArrayList<PCRTestUUID> inOrderTests = new ArrayList<>();

    public static ArrayList<Long> addresses = new ArrayList<>();
    private static Hashtable<Long, TestClass> testing = new Hashtable<>();

    public Tester() {

    }

    /*public void testInsertDelete() {
        Random r = new Random();
        //r.setSeed(100);
        //r.nextInt() % (100 - 1) + 1

        int count = 0;
        int chance = 100;
        while (count != 200000) {
            if (count == 100000) {
                System.out.println("100k");
                chance = 30;
            }
            count++;
            //insert
            if (r.nextInt(100) + 1 <= chance) {
                PCRTestData test = new PCRTestData(UUID.randomUUID());
                PCRTestUUID pcrTestUUID = new PCRTestUUID(test);
                pcrTests.add(pcrTestUUID);
                tree.insert(pcrTestUUID);
            } //delete
            else {
                if (pcrTests.size() != 0) {
                    int index = r.nextInt(pcrTests.size());
                    tree.delete(pcrTests.get(index));
                    pcrTests.remove(index);
                }
            }


        }

        System.out.println(tree.dataNum + " tree");
        System.out.println(pcrTests.size() + " list");

        Collections.sort(pcrTests);
        inOrderTests = tree.inOrder();
        if (inOrderTests.size() != pcrTests.size()) {
            System.out.println("sizes dont match");
        }
        for (int i = 0; i < inOrderTests.size(); i++) {
            if (!pcrTests.get(i).equals(inOrderTests.get(i))) {
                System.out.println(pcrTests.get(i).getPcrTestData().getTestCode());
                System.out.println(inOrderTests.get(i).getPcrTestData().getTestCode());
                System.out.println("ERROR");
                break;
            }
        }

    }

    public void testIntervalSearch() {
        Random r = new Random();
        int min = r.nextInt(pcrTests.size());
        PCRTestUUID pcrTestUUIDmin = pcrTests.get(min);
        int max = r.nextInt(pcrTests.size() - min);
        PCRTestUUID pcrTestUUIDmax = pcrTests.get(min + max);

        ArrayList<PCRTestUUID> inOrderIntervalTests;
        inOrderIntervalTests = tree.getInterval(pcrTestUUIDmin, pcrTestUUIDmax);
        int index = inOrderTests.indexOf(inOrderIntervalTests.get(0));
        int count = 0;
        for (int i = 0; i < inOrderIntervalTests.size(); i++) {
            if (inOrderIntervalTests.get(i).getPcrTestData().getTestCode() != inOrderTests.get(i + index).getPcrTestData().getTestCode()) {
                System.out.println("Bad interval search");
            } else {
                count++;
                //System.out.println(inOrderIntervalTests.get(i).getPcrTestData().getTestCode());
            }
        }

    }*/

    public void testFile() {
        UFile<TestClass> file = new UFile<>("test.txt", "testOpMem.txt", TestClass.class);

        Random r = new Random();
        long seed = r.nextLong();
        r.setSeed(seed);

        //TestClass test = new TestClass("Patrik", "Hybin", 50, 0.5);
        //long myAdd = file.insert(test);


        double chance = 1;
        for (int i = 0; i < 10000; i++) {
            TestClass testClass;
            double rand = r.nextDouble();
            if (rand < chance) {
                testClass = new TestClass(i + "N", i + "S", i + 100 , (double)i/10 );

                long address;
                address = file.insert(testClass);

                testing.put(address, testClass);
                addresses.add(address);
                if (i == 5000) {
                    chance = 0.5;
                }
            } else {
                if (addresses.size() == 0) {
                    continue;
                }
                long deleteAdd = addresses.get(r.nextInt(0, addresses.size()));

                if (file.delete(deleteAdd)) {
                    addresses.remove(deleteAdd);
                    testing.remove(deleteAdd);
                }
            }

        }

        for (int j = 0; j < testing.size(); j++) {
            long findAdd = addresses.get(j);
            TestClass findClass = file.find(findAdd);
            TestClass testClass = testing.get(findAdd);

            if (!findClass.isValid()) {
                System.out.println("ERROR valid");
            }
            //System.out.println(testClass.getName() + "  "  + findClass.getName());
            if(!testClass.getName().equals(findClass.getName())) {
                System.out.println("ERROR name");
            }
            //System.out.println(testClass.getSurname() + "  "  + findClass.getSurname());
            if(!testClass.getSurname().equals(findClass.getSurname())) {
                System.out.println("ERROR surname");
            }
            //System.out.println(testClass.getNumber() + "  "  + findClass.getNumber());
            if(testClass.getNumber() != findClass.getNumber()) {
                System.out.println("ERROR number");
            }
            //System.out.println(testClass.getDoubleNum() + "  "  + findClass.getDoubleNum()); System.out.println();
            if(testClass.getDoubleNum() != findClass.getDoubleNum()) {
                System.out.println("ERROR double");
            }
        }
        //System.out.println(seed);
        System.out.println("pocet v testovacom file " + addresses.size());
        file.clearFile();
        //file.saveFreeSpaces();
    }

    public void testBTree() {
        //person.getSize() - 125
        UFile<Person> personFile = new UFile<>("files/testPersonsData.txt", "files/testPersonsDataFreeSpace.txt", Person.class);
        BTree<Person> tree = new BTree<>(Person.class,"files/testPersons.txt", "files/testPersonsFreeSpace.txt", personFile);

        ArrayList<Person> persons = new ArrayList<>();

        Random r = new Random();
        int seed = r.nextInt();
        System.out.println("seed: " + seed);
        r.setSeed(seed);
        double chance = 1;
        int countIns = 0;
        int countDel = 0;
        int countSame = 0;
        String operation;

        for (int i = 1; i <= 1000; i++) {
            Person person;
            double rand = r.nextDouble();
            if (i == 500) {
                chance = 0;
            }

            //System.out.println(i + ": operacia");
            int num;
            if (rand < chance) {
                num = r.nextInt(100000);
                person = new Person(num + "", i + "S",   num + "" , LocalDate.now(), num, (double)num/10);

                System.out.println(i + ": insert");
                if (i > 25) {
                    //tree.getFileSequence();
                }
                //System.out.println();
                personFile.insert(person);
                if (tree.insert(person)) {
                    treePersons.insert(person);
                    persons.add(person);
                    //tree.getFileSequence();
                    countIns++;
                } else {
                    personFile.delete(person.getAddress());
                    countSame++;
                }

            } else {
                if (persons.size() == 0) {
                    continue;
                }

                System.out.println(i + ": delete");
                //tree.getFileSequence();
                //System.out.println();
                int deleted = r.nextInt(persons.size());
                //System.out.println(persons.get(deleted).getIdNumber());
                treePersons.delete(persons.get(deleted));
                tree.delete(persons.get(deleted));
                personFile.delete(persons.get(deleted).getAddress());
                persons.remove(deleted);
                countDel++;
                //tree.getFileSequence();
            }
            //tree.inOrder();
        }
        //Person ja = new Person("Patrik", "Hybin", 2+ "", LocalDate.now(), 2 , 0);

        /*personFile.insert(ja);
        tree.insert(ja);
        persons.add(ja);

        System.out.println(tree.find(ja).getName());*/

        System.out.println();
        System.out.println("seed " + seed);
        Collections.sort(persons);

        ArrayList<Person> inOrderPersons = tree.inOrder();

        System.out.println("inOrderPersons count " + inOrderPersons.size() + " a v testovacom array " + persons.size());
        if (persons.size() != inOrderPersons.size()) {
            System.out.println("Wrong sizes");
            System.out.println(persons.size() + " velkost testovacej");
            System.out.println(inOrderPersons.size() + " velkost v subore");
        }

        for (int z = 0; z < persons.size(); z++) {
            if (!persons.get(z).getName().equals(personFile.find(inOrderPersons.get(z).getAddress()).getName())) {
                System.out.println("ERROR neni dobre");
                System.out.println(persons.get(z).getName() + "  " + personFile.find(inOrderPersons.get(z).getAddress()).getName());
            }
        }

        System.out.println();

        System.out.println(treePersons.inOrder().size() + " 2-3 strom");
        System.out.println(tree.inOrder().size() + " b-strom");

        System.out.println("insert: " + countIns + " delete: " + countDel + " same: " + countSame);


        tree.getFileSequentialListing();
        System.out.println(persons.size() + " size");


        System.out.println(personFile.getLength());
        System.out.println();


        System.out.println(tree.getFile().queueDeleted.size() + " + queue node deleted");
        System.out.println(tree.getFile().queueDeletedReverse.size() + " + queueReverse node deleted");

        tree.saveFreeSpaces();
        //tree.clearFiles();
        personFile.saveFreeSpaces();
        //personFile.clearFiles();

    }

    void metoda() {

        UFile<Person> personFile = new UFile<>("files/testPersonsData.txt", "files/testPersonsDataFreeSpace.txt", Person.class);
        System.out.println(personFile.getLength());
        /*BTree<Person> tree = new BTree<>(Person.class,"files/testPersons.txt", "files/testPersonsFreeSpace.txt", personFile);
        tree.getFileSequence();*/
        /*Person ja = new Person("Pafdsfdsds", "Hybfdsdsa", 2+ "", LocalDate.now(), 2 , 0);
        Person ty = new Person("Pafdsfdsds", "Hybfdsdsa", 4+ "", LocalDate.now(), 2 , 0);
        ArrayList<Person> persons = tree.getInterval(ja, ty);
        for (Person person : persons) {
            System.out.println(person.getIdNumber());
        }*/
        /*int countValid = 0;
        for (Person person: personFile.writeOutSequence(0)) {
            if (person.isValid()) {
                countValid++;
                //System.out.println("f " + person.getIdNumber());
            }
        }

        System.out.println();
        System.out.println(countValid + "  valid v subore " + tree.inOrder().size() +  " v strome");
        System.out.println();

        while (personFile.getLength() > 8) {
            tree.delete(personFile.find(personFile.getLength() - new Person().getSize()));
            personFile.delete(personFile.getLength() - new Person().getSize());
        }

        System.out.println(personFile.getLength());

        countValid = 0;
        for (Person person: personFile.writeOutSequence(0)) {
            if (person.isValid()) {
                countValid++;
                //System.out.println("f " + person.getIdNumber());
            }
        }

        System.out.println();
        System.out.println(countValid + "  valid v subore po delete " + tree.inOrder().size() +  " v strome po delete");
        System.out.println();*/
    }

}
