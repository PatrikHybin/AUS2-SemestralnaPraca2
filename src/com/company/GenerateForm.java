package com.company;

import javafx.scene.web.PromptData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GenerateForm extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField personsNumberInput;
    private JTextField testNumberInput;
    private JLabel zeroPersons;

    public GenerateForm(Program program) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        Dimension dim = new Dimension();
        dim.setSize(400, 155);
        setMinimumSize(dim);
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK(program);
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK(Program program) {
        program.generatePersons(getPersonsNumberInput());
        if (!program.checkNumberOfPatients()) {
            zeroPersons.setText("To generate PCRTests you need at least one person!");
            zeroPersons.setForeground(Color.red);
        } else {
            zeroPersons.setText("");
            zeroPersons.setForeground(Color.black);
            program.generatePCRTest(getTestNumberInput());
            dispose();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public String getPersonsNumberInput() {
        return personsNumberInput.getText();
    }

    public String getTestNumberInput() {
        return testNumberInput.getText();
    }
}
