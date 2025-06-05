/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Vista.UI_Dashboard;
import Vista.UI_Inicio;

/**
 *
 * @author franc
 */
public class UI_HomeController extends PanelController {
  UI_Inicio vistaInicio;

  UI_HomeController(UI_Inicio vistaInicio, UI_Dashboard vista) {
    super(vistaInicio, vista);
    this.vistaInicio = vistaInicio;
    super.showWindow(vistaInicio);
  }

    @Override
    protected void addListeners() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected void reloadWindow() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
