package gui;

import controller.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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
    private JLabel numberOfData;
    private JButton outputSek;
    private JButton seqListButton;

    Object[][] data;

    public AppGUI() {

        setContentPane(mainPanel);
        setTitle("Hello!");
        setSize(1024, 512);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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

                //table.setModel(new DefaultTableModel(0,0));
                table.setModel(Controller.getPCRTestsTable());
                WriteNumberOfData();
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
                WriteNumberOfData();
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
                //table.setModel(new DefaultTableModel(0,0));
                table.setModel(Controller.getPCRTestsTable());
                table.getColumn("Person").setPreferredWidth(350);
                WriteNumberOfData();
            }
        });

        showPersons.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                table.setModel(Controller.getPersonsTable(null));
                WriteNumberOfData();
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
                    WriteNumberOfData();
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
                WriteNumberOfData();
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
                WriteNumberOfData();
            }
        });

        seqListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SequentialListingForm treeOutput = new SequentialListingForm();
                treeOutput.pack();
                treeOutput.setLocationRelativeTo(null);
                treeOutput.setVisible(true);
                table.setModel(Controller.getSearchedTable());
            }
        });
    }

    private void WriteNumberOfData() {
        numberOfData.setText("Number of data: " + table.getModel().getRowCount());
    }

}
