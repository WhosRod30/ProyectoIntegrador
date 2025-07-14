/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CustomElements;

import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author franc
 */
public class DatePickerCustom extends DatePicker{
    public DatePickerCustom() {
        super();
        getTextField(this);
    }

    private JTextField getTextField(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JTextField) {
                JTextField textField = (JTextField) component;
                textField.setBorder(UIManager.getBorder("TextField.border"));
                textField.putClientProperty("JTextField.placeholderText", "Seleccione la fecha");
                textField.setMargin(new Insets(0, 6, 0, 6));
                return textField;
            } else if (component instanceof Container) {
                return getTextField((Container) component);
            }
        }
        return null;
    }
}
