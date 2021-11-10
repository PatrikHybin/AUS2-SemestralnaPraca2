package com.company;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class AppGUI extends JFrame {
    private JPanel mainPanel;
    private JPanel rightPanel;
    private JScrollPane leftPanel;
    private JButton addPCRTestButton;
    private JButton addPersonButton;
    private JTable table;
    private JButton showPCRTests;
    private JButton showPersons;
    private JButton searchButton;
    private JButton generateButton;
    private JButton deletePCRTestButton;
    private JButton deletePersonButton;
    private JButton saveButton;
    private JButton loadButton;

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

        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null);
        table.setCellSelectionEnabled(true);

        addPCRTestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddPCRTestForm addPCRTestForm = new AddPCRTestForm();
                addPCRTestForm.setTitle("PCRTest Form");
                addPCRTestForm.pack();
                addPCRTestForm.setLocationRelativeTo(null);
                addPCRTestForm.setVisible(true);
                table.setModel(Controller.getPCRTestsTable());
            }
        });

        addPersonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddPersonForm addPersonForm = new AddPersonForm();
                addPersonForm.setTitle("Person Form");
                addPersonForm.pack();
                addPersonForm.setLocationRelativeTo(null);
                addPersonForm.setVisible(true);
                table.setModel(Controller.getPersonsTable(null));

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
                table.setModel(Controller.getPCRTestsTable());
            }
        });

        showPersons.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                table.setModel(Controller.getPersonsTable(null));
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SearchForm searchForm = new SearchForm();
                searchForm.setTitle("PCRTest Form");
                searchForm.pack();
                searchForm.setLocationRelativeTo(null);
                searchForm.setVisible(true);
                if (Controller.getSearchedTable() != null) {
                    table.setModel(Controller.getSearchedTable());
                }
            }
        });
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GenerateForm generateForm = new GenerateForm();
                generateForm.setTitle("Generate Form");
                generateForm.pack();
                generateForm.setLocationRelativeTo(null);
                generateForm.setVisible(true);

            }
        });
        deletePCRTestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeletePCRTestForm deletePCRTestForm = new DeletePCRTestForm();
                deletePCRTestForm.setTitle("Delete Form");
                deletePCRTestForm.pack();
                deletePCRTestForm.setLocationRelativeTo(null);
                deletePCRTestForm.setVisible(true);
                table.setModel(Controller.getPCRTestsTable());
            }
        });
        deletePersonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeletePersonForm deletePersonForm = new DeletePersonForm();
                deletePersonForm.setTitle("Delete Form");
                deletePersonForm.pack();
                deletePersonForm.setLocationRelativeTo(null);
                deletePersonForm.setVisible(true);
                table.setModel(Controller.getPersonsTable(null));
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.save();
            }
        });
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.load();
            }
        });
    }

}
