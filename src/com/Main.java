package com;

import controller.Controller;
import data.Program;
import gui.AppGUI;

public class Main {

    public static void main(String[] args) {
        Program program = new Program();
        Controller controller = new Controller(program);
        AppGUI appGUI = new AppGUI();

        TwoThreeTreeTester tester = new TwoThreeTreeTester();
        tester.testInsertDelete();
        tester.testIntervalSearch();


    }
}