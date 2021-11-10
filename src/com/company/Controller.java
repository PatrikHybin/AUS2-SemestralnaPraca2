package com.company;

import javax.swing.table.TableModel;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Controller {

    private static Program program;

    public Controller(Program prog) {
        program = prog;
    }

    public static void addPerson(String[] inputs) {
        program.addPerson(inputs);
    }

    public static void generatePersons(String number) {
        program.generatePersons(number);
    }

    public static void generatePCRTest(String numberToGenerate) {
        program.generatePCRTest(numberToGenerate);
    }

    public static int getNumberOfPersonsInSystem() {
        return program.getGenerator().getPersonsSize();
    }

    public static boolean checkNumberOfPersons() {
        if (getNumberOfPersonsInSystem() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public static void addPCRTest(String[] inputs) {
        program.addPCRTest(inputs);
    }

    public static boolean checkIfPersonExists(String input) {
        return program.checkIfPersonExists(input);
    }

    public static void deletePCRTest(String pcrTestCode) {
        program.deletePCRTest(pcrTestCode);
    }

    public static void deletePerson(String personId) {
        program.deletePerson(personId);
    }

    public static void search(String taskNumber, String[] inputs) {
        program.search(taskNumber, inputs);
    }

    public static TableModel getPCRTestsTable() {
        return  program.getPCRTestsTable();
    }

    public static TableModel getPersonsTable(ArrayList<Person> people) {
        return program.getPersonsTable(people);
    }

    public static TableModel getSearchedTable() {
        return program.getSearchedTable();
    }

    public static void save() {
        program.save();
    }

    public static void load() {
        try {
            program.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
