/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Vista.UI_Dashboard;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author franc
 */
public abstract class PanelController {
    JPanel panel;
    UI_Dashboard app;

    public PanelController(JPanel panel, UI_Dashboard app) {
        this.panel = panel;
        this.app = app;
        showWindow(panel);
    }

    void showWindow(JPanel panel) {
        panel.setPreferredSize(new Dimension(1000, 500)); // Tamaño inicial
        app.jpInternal.removeAll();
        app.jpInternal.setLayout(new BorderLayout());
        app.jpInternal.add(panel, BorderLayout.CENTER);
        app.jpInternal.revalidate();
        app.jpInternal.repaint();
    }

    protected abstract void addListeners();
}
