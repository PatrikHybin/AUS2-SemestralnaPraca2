package com.company;

import com.github.lgooddatepicker.components.DatePickerSettings;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;


public class AppGUI extends JFrame {
    private JPanel mainPanel;
    private JPanel rightPanel;
    private JButton addPCRTestButton;
    private JButton addPersonButton;
    private JButton button3;
    private JList list1;
    private JScrollPane leftPanel;
    private JTable table;
    private JButton showPCRTests;
    private JButton showPersons;
    private JButton searchButton;
    private JButton generateButton;

    Object[][] data;

    public AppGUI(Program program) {

        setContentPane(mainPanel);
        setTitle("Hello!");
        setSize(1024, 512);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        /*Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2 - this.getSize().width/2, dim.height/2 - this.getSize().height/2);*/
        setLocationRelativeTo(null);
        setVisible(true);
        /*
        tableModel.addColumn(columnNames[0]);
        tableModel.addColumn(columnNames[1]);
        tableModel.addColumn(columnNames[2]);
        tableModel.addColumn(columnNames[3]);
        tableModel.addColumn(columnNames[4]);
        */

        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null);
        table.setCellSelectionEnabled(true);

        addPCRTestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PCRTestForm pcrTestFormDialog = new PCRTestForm(program);
                pcrTestFormDialog.setTitle("PCRTest Form");
                pcrTestFormDialog.pack();
                pcrTestFormDialog.setLocationRelativeTo(null);
                pcrTestFormDialog.setVisible(true);
                table.setModel(program.getPCRTestsTable());
            }
        });

        addPersonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PersonForm personFormDialog = new PersonForm(program);
                personFormDialog.setTitle("Person Form");
                personFormDialog.pack();
                personFormDialog.setLocationRelativeTo(null);
                personFormDialog.setVisible(true);
                table.setModel(program.getPersonsTable());

                /* cez list
                DefaultListModel listModel = new DefaultListModel();
                for (PCRTestTEST test : program.tree.inOrder()) {
                    listModel.addElement(test.getTestCode() + "\t" + test.getTestCode());
                }
                list1.setModel(listModel);*/
            }
        });

        showPCRTests.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                table.setModel(program.getPCRTestsTable());
            }
        });

        showPersons.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                table.setModel(program.getPersonsTable());
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SearchForm searchFormDialog = new SearchForm(program);
                searchFormDialog.setTitle("PCRTest Form");
                searchFormDialog.pack();
                searchFormDialog.setLocationRelativeTo(null);
                searchFormDialog.setVisible(true);
                if (program.getSearchedTable() != null) {
                    table.setModel(program.getSearchedTable());
                    program.setSearchedTable();
                }

            }
        });
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GenerateForm generateFormDialog = new GenerateForm();
                generateFormDialog.setTitle("Generate Form");
                generateFormDialog.pack();
                generateFormDialog.setLocationRelativeTo(null);
                generateFormDialog.setVisible(true);
                program.generatePersons(generateFormDialog.getPersonsNumberInput());
                program.generatePCRTest(generateFormDialog.getTestNumberInput());

            }
        });
    }

}
