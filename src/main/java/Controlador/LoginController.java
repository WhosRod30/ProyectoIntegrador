/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import DAO.Autenticar;
import Vista.Login;
import Vista.UI_Dashboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author franc
 */
public class LoginController implements ActionListener {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private static final Logger securityLogger = LoggerFactory.getLogger("SECURITY");

    Login vLogin;
    UI_Dashboard dashboard = new UI_Dashboard();

    public LoginController(Login vLogin) {
        this.vLogin = vLogin;
        this.vLogin.btnIngresar.addActionListener(this);
        vLogin.txtUser.putClientProperty("JTextField.placeholderText", "Ingresa tu usuario");
        vLogin.txtPass.putClientProperty("JTextField.placeholderText", "Ingresa tu contraseña");

        // Test de logging al iniciar
        logger.info("=== SISTEMA AVÍCOLA INICIADO ===");
        securityLogger.info("SYSTEM_START: Sistema de gestión avícola iniciado");

        Run();
    }

    void Run() {
        logger.info("Iniciando pantalla de login del Sistema Avícola");
        vLogin.setLocationRelativeTo(null);
        vLogin.setVisible(true);
        vLogin.setTitle("Sistema Avícola - Login");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vLogin.btnIngresar) {
            String usuario = vLogin.txtUser.getText();
            String password = new String(vLogin.txtPass.getPassword());

            logger.info("Intento de login para usuario: {}", usuario);

            // Usar las credenciales reales del formulario
            if (Autenticar.autenticar(usuario, password)) {
                securityLogger.info("LOGIN_SUCCESS: usuario={}", usuario);
                logger.info("Login exitoso, abriendo dashboard");

                DashboardController controllerDash = new DashboardController(dashboard);
                vLogin.dispose();
            } else {
                securityLogger.warn("LOGIN_FAILED: usuario={}", usuario);
                logger.warn("Intento fallido de login para usuario: {}", usuario);

                vLogin.txtUser.setText("");
                vLogin.txtPass.setText("");
                vLogin.txtUser.requestFocus();
                JOptionPane.showMessageDialog(vLogin, "Usuario o contraseña incorrectos", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
