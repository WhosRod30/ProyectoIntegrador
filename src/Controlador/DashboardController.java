/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Vista.UI_Consumo;
import Vista.UI_Dashboard;
import Vista.UI_Granja;
import Vista.UI_Ingreso;
import Vista.UI_Inicio;
import Vista.UI_mortalidad;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;

/**
 *
 * @author franc
 */
public class DashboardController implements ActionListener {
    public static UI_Dashboard vista;
    UI_Consumo vistaConsumo = null;
    UI_Granja vistaGranja = null;
    UI_Ingreso vistaIngreso = null;
    UI_Inicio vistaInicio = null;
    UI_mortalidad vistaMortalidad = null;

    public DashboardController(UI_Dashboard vista) {
        this.vista = vista;

        vista.btnConsumo.addActionListener(this);
        vista.btnGranja.addActionListener(this);
        vista.btnIngreso.addActionListener(this);
        vista.btnInicio.addActionListener(this);
        vista.btnMortalidad.addActionListener(this);

        launchApp();
    }

    void launchApp() {
        vista.getRootPane().putClientProperty("JRootPane.titleBarForeground", Color.WHITE);
        vista.getRootPane().putClientProperty("JRootPane.titleBarBackground", new Color(23, 180, 252));
        vista.setLocationRelativeTo(null);
        // ChangePanel(home);
        showHome();
        vista.setVisible(true);
    }

    void showHome() {
        vistaInicio = new UI_Inicio();
        UI_HomeController controllerHome = new UI_HomeController(vistaInicio, vista);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnConsumo) {
            vistaConsumo = new UI_Consumo();
            ConsumoController controllerConsumo = new ConsumoController(vistaConsumo, vista);
        } else if (e.getSource() == vista.btnGranja) {
            vistaGranja = new UI_Granja();
            GranjaController controllerGranja = new GranjaController(vistaGranja, vista);
        } else if (e.getSource() == vista.btnIngreso) {
            vistaIngreso = new UI_Ingreso();
            IngresoController controllerIngreso = new IngresoController(vistaIngreso, vista);
        } else if (e.getSource() == vista.btnInicio) {
            showHome();
        } else if (e.getSource() == vista.btnMortalidad) {
            vistaMortalidad = new UI_mortalidad();
            MortalidadController controllerMortalidad = new MortalidadController(vistaMortalidad, vista);
        }

    }

}
