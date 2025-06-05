/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Vista.Login;
import Vista.UI_Dashboard;
import DAO.Autenticar;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

/**
 *
 * @author franc
 */
public class LoginController implements ActionListener {
    Login vLogin;
    UI_Dashboard dashboard = new UI_Dashboard();

    public LoginController(Login vLogin) {
        this.vLogin = vLogin;
        this.vLogin.btnIngresar.addActionListener(this);
        vLogin.txtUser.putClientProperty("JTextField.placeholderText", "Ingresa tu usuario");
        vLogin.txtPass.putClientProperty("JTextField.placeholderText", "Ingresa tu contraseña");
        Run();
    }

    void Run() {
        vLogin.setLocationRelativeTo(null);
        vLogin.getRootPane().putClientProperty("JRootPane.titleBarForeground", Color.WHITE);
        vLogin.getRootPane().putClientProperty("JRootPane.titleBarBackground", new Color(23, 180, 252));
        vLogin.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vLogin.btnIngresar) {
            System.out.println("login");
            if (Autenticar.autenticar(vLogin.txtUser.getText(), new String(vLogin.txtPass.getPassword()))) {
                DashboardController controllerDash = new DashboardController(dashboard);
                vLogin.dispose();
            } else {
                vLogin.txtUser.setText("");
                vLogin.txtPass.setText("");
                vLogin.txtUser.requestFocus();
                JOptionPane.showMessageDialog(vLogin, "Usuario o contraseña incorrectos", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            // Autenticar(vLogin, dashboard);
        }
    }
}
