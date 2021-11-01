package com.company;

import com.github.lgooddatepicker.components.DateTimePicker;

import javax.swing.*;
import java.awt.event.*;

public class PCRTestForm extends JDialog {
    private JPanel pcrTestForm;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField idNumberOfPatientInput;
    private JTextField workplaceCodeInput;
    private JTextField districtCodeInput;
    private JComboBox testResultInput;
    private JComboBox regionCodeInput;
    private DateTimePicker dateAndTimeOfTestInput;
    private JTextArea noteInput;
    private JLabel dateAndTimeOfTest;
    private JLabel testResult;
    private JLabel idNumberOfPatient;
    private JLabel workplaceCode;
    private JLabel districtCode;
    private JLabel regionCode;
    private JLabel note;
    private String[] inputs = {null,null,null,null,null,null,null};

    public PCRTestForm(Program program) {
        setContentPane(pcrTestForm);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

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
        pcrTestForm.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK(Program program) {
        System.out.println(getDateAndTimeOfTestInput());
        inputs[0] = getDateAndTimeOfTestInput();
        inputs[1] = getTestResultInput();
        inputs[2] = getIdNumberOfPatientInput();
        inputs[3] = getWorkplaceCodeInput();
        inputs[4] = getDistrictCodeInput();
        inputs[5] = getRegionCodeInput();
        inputs[6] = getNoteInput();

        if (!inputs[0].equals("")) {
            program.addPCRTest(inputs);
            dispose();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public String getDateAndTimeOfTestInput() {
        return dateAndTimeOfTestInput + "";
    }

    public String getTestResultInput() {
        return String.valueOf(testResultInput.getSelectedItem());
    }

    public String getIdNumberOfPatientInput() {
        return idNumberOfPatientInput.getText();
    }

    public String getWorkplaceCodeInput() {
        return workplaceCodeInput.getText();
    }

    public String getDistrictCodeInput() {
        return districtCodeInput.getText();
    }

    public String getRegionCodeInput() {
        return String.valueOf(regionCodeInput.getSelectedItem());
    }

    public String getNoteInput() {
        return noteInput.getText();
    }
}
