/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Vista.*;
import javax.swing.JButton;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author franc
 */
public class DashboardController implements ActionListener {
    private final UI_Dashboard vista;
    UI_Consumo vistaConsumo = null;
    UI_Granja vistaGranja = null;
    UI_Ingreso vistaIngreso = null;
    UI_Inicio vistaInicio = null;
    UI_mortalidad vistaMortalidad = null;
    UI_Usuario vistaUsuario = null;

    // Nueva paleta de colores del sistema (definidos también en Main.java)
    private final Color COLOR_ACENTO = new Color(255, 140, 66); // naranja acento
    private final Color COLOR_PRIMARIO = new Color(45, 90, 39); // verde principal

    public DashboardController(UI_Dashboard vista) {
        this.vista = vista;

        vista.btnConsumo.addActionListener(this);
        vista.btnGranja.addActionListener(this);
        vista.btnIngreso.addActionListener(this);
        vista.btnInicio.addActionListener(this);
        vista.btnMortalidad.addActionListener(this);
        vista.btnUsuarios.addActionListener(this);

        configurarBotones();
        launchApp();
    }

    // Configuración simplificada de botones (usa estilos globales de Main.java)
    private void configurarBotones() {
        JButton[] botones = new JButton[] { vista.btnConsumo, vista.btnGranja, vista.btnIngreso, vista.btnInicio,
                vista.btnMortalidad, vista.btnUsuarios };
        for (JButton btn : botones) {
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }

    void launchApp() {
        vista.setLocationRelativeTo(null);
        showHome();
        vista.setVisible(true);
    }

    private void setBotonActivo(JButton botonActivo) {
        JButton[] botones = new JButton[] { vista.btnConsumo, vista.btnGranja, vista.btnIngreso, vista.btnInicio,
                vista.btnMortalidad, vista.btnUsuarios };
        for (JButton btn : botones) {
            if (btn == botonActivo) {
                btn.setBackground(COLOR_ACENTO);
                btn.setForeground(COLOR_PRIMARIO);
            } else {
                btn.setBackground(COLOR_PRIMARIO);
                btn.setForeground(Color.WHITE);
            }
        }
    }

    void showHome() {
        vistaInicio = new UI_Inicio();
        UI_HomeController controllerHome = new UI_HomeController(vistaInicio, vista);
        setBotonActivo(vista.btnInicio);
        vista.setTitle("Inicio - Integrador Avícola");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnConsumo) {
            vistaConsumo = new UI_Consumo();
            ConsumoController controllerConsumo = new ConsumoController(vistaConsumo, vista);
            setBotonActivo(vista.btnConsumo);
            vista.setTitle("Consumo - Integrador Avícola");
        } else if (e.getSource() == vista.btnGranja) {
            vistaGranja = new UI_Granja();
            GranjaController controllerGranja = new GranjaController(vistaGranja, vista);
            setBotonActivo(vista.btnGranja);
            vista.setTitle("Granja - Integrador Avícola");
        } else if (e.getSource() == vista.btnIngreso) {
            vistaIngreso = new UI_Ingreso();
            IngresoController controllerIngreso = new IngresoController(vistaIngreso, vista);
            setBotonActivo(vista.btnIngreso);
            vista.setTitle("Ingreso - Integrador Avícola");
        } else if (e.getSource() == vista.btnInicio) {
            showHome();
        } else if (e.getSource() == vista.btnMortalidad) {
            vistaMortalidad = new UI_mortalidad();
            MortalidadController controllerMortalidad = new MortalidadController(vistaMortalidad, vista);
            setBotonActivo(vista.btnMortalidad);
            vista.setTitle("Mortalidad - Integrador Avícola");
        } else if (e.getSource() == vista.btnUsuarios) {
            vistaUsuario = new UI_Usuario();
            UsuarioController controllerUsuario = new UsuarioController(vistaUsuario, vista);
            setBotonActivo(vista.btnUsuarios);
            vista.setTitle("Usuarios - Integrador Avícola");
        }
    }

}
