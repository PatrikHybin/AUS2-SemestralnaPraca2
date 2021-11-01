package com.company;

import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class PersonForm extends JDialog {
    private JPanel personForm;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField nameInput;
    private JTextField surnameInput;
    private JTextField idNumberInput;
    private DatePicker birthdayInput;
    private JLabel name;
    private JLabel surname;
    private JLabel idNumber;
    private JLabel birthday;
    private String[] inputs = {null,null,null,null};

    public PersonForm(Program program) {
        setTitle("Person Form");
        setSize(300, 300);
        setContentPane(personForm);
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
        personForm.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK(Program program) {
        inputs[0] = getNameInput();
        inputs[1] = getSurnameInput();
        inputs[2] = getIdNumberInput();
        inputs[3] = getBirthdayInput();

        //TODO: one method ????
        if (inputs[0].equals("")) {
            name.setText("Name - Missing data");
            name.setForeground(Color.red);
        } else {
            name.setText("Name");
            name.setForeground(Color.black);
        }
        if (inputs[1].equals("")) {
            surname.setText("Surname - Missing data");
            surname.setForeground(Color.red);
        } else {
            surname.setText("Surname");
            surname.setForeground(Color.black);
        }
        if (inputs[2].equals("")) {
            idNumber.setText("Identification number - Missing data");
            idNumber.setForeground(Color.red);
        } else {
            idNumber.setText("Identification number");
            idNumber.setForeground(Color.black);
        }
        if (inputs[3].equals("")) {
            birthday.setText("Birthday - Missing data");
            birthday.setForeground(Color.red);
        } else {
            birthday.setText("Birthday");
            birthday.setForeground(Color.black);
        }
        if (inputs[0].equals("") || inputs[1].equals("") || inputs[2].equals("") || inputs[3].equals("")) {

        } else {
            program.addPerson(inputs);
            dispose();
        }

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public String getNameInput() {
        return nameInput.getText();
    }

    public String getSurnameInput() {
        return surnameInput.getText();
    }

    public String getIdNumberInput() {
        return idNumberInput.getText();
    }

    public String getBirthdayInput() {
        return birthdayInput + "";
    }
}
