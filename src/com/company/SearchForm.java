package com.company;

import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class SearchForm extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JCheckBox task2;
    private JCheckBox task3;
    private JCheckBox task4;
    private JCheckBox task5;
    private JCheckBox task6;
    private JCheckBox task7;
    private JCheckBox task8;
    private JCheckBox task9;
    private JCheckBox task14;
    private JCheckBox task12;
    private JCheckBox task11;
    private JCheckBox task10;
    private JCheckBox task13;
    private JCheckBox task15;
    private JTextField testCodeInput;
    private JTextField personIdInput;
    private JLabel testCode;
    private JLabel personId;
    private JComboBox regionCodeInput;
    private JTextField districtCodeInput;
    private JTextField workplaceCodeInput;
    private JLabel dateMax;
    private JLabel dateFrom;
    private JTextField illTimeInput;
    private JComboBox searchTestResultInput;
    private JLabel testResult;
    private DatePicker maxDateInput;
    private DatePicker minDateInput;
    private JTextArea taskInfo;
    private JCheckBox task16;
    private ArrayList<JCheckBox> checkBoxes = new ArrayList<>();
    private String[] inputs = {null,null,null,null,null,null,null,null,null};

    public SearchForm() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        checkBoxes.add(task2);
        checkBoxes.add(task3);
        checkBoxes.add(task4);
        checkBoxes.add(task5);
        checkBoxes.add(task6);
        checkBoxes.add(task7);
        checkBoxes.add(task8);
        checkBoxes.add(task9);
        checkBoxes.add(task10);
        checkBoxes.add(task11);
        checkBoxes.add(task12);
        checkBoxes.add(task13);
        checkBoxes.add(task14);
        checkBoxes.add(task15);
        checkBoxes.add(task16);


        for (JCheckBox checkBox: checkBoxes) {
            checkBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    JCheckBox selectedCheckbox = checkBox;
                    if (selectedCheckbox.isSelected()) {
                        for (JCheckBox checkBox: checkBoxes){
                            if (checkBox != selectedCheckbox) {
                                checkBox.setSelected(false);
                            } else {
                                taskInfo.setText(checkBox.getToolTipText());
                            }
                        }
                    }
                }
            });
        }

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
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
    //TODO(check if there is correct input)
    private void onOK() {
        inputs[0] = getSearchTestResultInput();
        inputs[1] = getTestCodeInput();
        inputs[2] = getPersonIdInput();
        inputs[3] = getRegionCodeInput();
        inputs[4] = getDistrictCodeInput();
        inputs[5] = getWorkplaceCodeInput();
        inputs[6] = getMinDateInput() + "T00:00";
        inputs[7] = getMaxDateInput() + "T23:59";
        inputs[8] = getIllTimeInput();
        for (JCheckBox checkBox: checkBoxes){
            if (checkBox.isSelected()) {
                Controller.search(checkBox.getText(), inputs);
                dispose();
            }
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public String getTestCodeInput() {
        return testCodeInput.getText();
    }

    public String getPersonIdInput() {
        return personIdInput.getText();
    }

    public String getRegionCodeInput() {
        return regionCodeInput.getSelectedItem().toString();
    }

    public String getDistrictCodeInput() {
        return districtCodeInput.getText();
    }

    public String getWorkplaceCodeInput() {
        return workplaceCodeInput.getText();
    }

    public String getMaxDateInput() {
        return maxDateInput + "";
    }

    public String getMinDateInput() {
        return minDateInput + "";
    }

    public String getIllTimeInput() {
        return illTimeInput.getText();
    }

    public String getSearchTestResultInput() {
        return searchTestResultInput.toString();
    }
}
