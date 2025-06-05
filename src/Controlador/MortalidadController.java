/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Vista.UI_Dashboard;
import Vista.UI_mortalidad;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author franc
 */
public class MortalidadController extends PanelController implements ActionListener{
  UI_mortalidad vistaMortalidad;

  public MortalidadController(UI_mortalidad vistaMortalidad, UI_Dashboard vista) {
    super(vistaMortalidad, vista);
    this.vistaMortalidad = vistaMortalidad;
    showWindow(vistaMortalidad);
  }

  @Override
  protected void addListeners() {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                   // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

  @Override
  protected void reloadWindow() {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                   // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
