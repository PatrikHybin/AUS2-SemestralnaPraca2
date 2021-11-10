package com.company;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Main {

    public static void main(String[] args) {
        Program program = new Program();
        Controller controller = new Controller(program);
        AppGUI appGUI = new AppGUI(program);

        TwoThreeTreeTester tester = new TwoThreeTreeTester();
        tester.testInsertDelete();
        tester.testIntervalSearch();

    }
}
