/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import DAO.GranjaDAO;
import Modelo.Granja;
import Vista.Modal.GranjaModal;
import Vista.UI_Dashboard;
import Vista.UI_Granja;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author franc
 */
public class GranjaController extends PanelController implements ActionListener {

  UI_Granja vistaGranja;
  UI_Dashboard vista;

  public GranjaController(UI_Granja vistaGranja, UI_Dashboard vista) {
    super(vistaGranja, vista);
    this.vistaGranja = vistaGranja;
    this.vista = vista;
    showWindow(vistaGranja);
    addListeners();
    cargarTablaGranjas();
  }

  private void cargarTablaGranjas() {
    vistaGranja.tbContent.setModel(GranjaDAO.listarGranjas());
  }

  @Override
  protected void addListeners() {
    vistaGranja.btnBuscar.addActionListener(this);
    vistaGranja.btnEditar.addActionListener(this);
    vistaGranja.btnEliminar.addActionListener(this);
    vistaGranja.btnNuevo.addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == vistaGranja.btnBuscar) {
      handleBuscar();
    } else if (e.getSource() == vistaGranja.btnEditar) {
      handleEditar();
    } else if (e.getSource() == vistaGranja.btnEliminar) {
      handleEliminar();
    } else if (e.getSource() == vistaGranja.btnNuevo) {
      handleNuevo();
    }
  }

  private void abrirModalGranja(Granja granja, boolean isEdit) {
    GranjaModal modal = new GranjaModal(vista, true);
    modal.setLocationRelativeTo(vista);
    modal.setTitle(isEdit ? "Editar Granja" : "Nueva Granja");

    if (isEdit) {
      modal.setNombre(granja.getNombre());
      modal.setCodigo(granja.getCodigo());
    }

    modal.setVisible(true);

    if (modal.isSaved()) {
      if (isEdit) {
        actualizarGranja(modal, granja.getId());
      } else {
        guardarNuevaGranja(modal);
      }
      cargarTablaGranjas();
    }
  }

  private void guardarNuevaGranja(GranjaModal modal) {
    Granja nuevaGranja = construirGranjaDesdeModal(modal);
    GranjaDAO.guardarGranja(nuevaGranja);
    cargarTablaGranjas();
    JOptionPane.showMessageDialog(vista, "Granja guardada correctamente");
  }

  private void actualizarGranja(GranjaModal modal, int id) {
    Granja granjaActualizada = construirGranjaDesdeModal(modal);
    granjaActualizada.setId(id);
    GranjaDAO.editarGranja(granjaActualizada);
    cargarTablaGranjas();
    JOptionPane.showMessageDialog(vista, "Granja actualizada correctamente");
  }

  private Granja construirGranjaDesdeModal(GranjaModal modal) {
    Granja granja = new Granja();
    granja.setNombre(modal.getNombre());
    granja.setCodigo(modal.getCodigo());
    return granja;
  }

  private void handleNuevo() {
    abrirModalGranja(null, false);
  }

  private void handleEditar() {
    String id = JOptionPane.showInputDialog(vistaGranja, "Ingrese el ID de la granja a editar:");
    if (id != null && !id.isEmpty()) {
      try {
        int idGranja = Integer.parseInt(id);
        Granja granja = GranjaDAO.buscarGranja(idGranja);

        if (granja != null) {
          abrirModalGranja(granja, true);
        } else {
          JOptionPane.showMessageDialog(vistaGranja, "No se encontró la granja con ID: " + id);
        }
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(vistaGranja, "ID no válido");
      }
    } else {
      JOptionPane.showMessageDialog(vistaGranja, "ID no válido");
    }
  }

  private void handleEliminar() {
    String id = JOptionPane.showInputDialog(vistaGranja, "Ingrese el ID de la granja a eliminar:");
    if (id != null && !id.isEmpty()) {
      try {
        int idGranja = Integer.parseInt(id);
        GranjaDAO.eliminarGranja(idGranja);
        cargarTablaGranjas();
        JOptionPane.showMessageDialog(vista, "Granja eliminada correctamente");
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(vistaGranja, "ID no válido");
      }
    } else {
      JOptionPane.showMessageDialog(vistaGranja, "ID no válido");
    }
  }

  private void handleBuscar() {
    String id = JOptionPane.showInputDialog(vistaGranja, "Ingrese el ID de la granja a buscar:");
    if (id != null && !id.isEmpty()) {
      try {
        int idGranja = Integer.parseInt(id);
        Granja granja = GranjaDAO.buscarGranja(idGranja);

        if (granja != null) {
          JOptionPane.showMessageDialog(vistaGranja, "Granja encontrada: " + granja.getNombre());
        } else {
          JOptionPane.showMessageDialog(vistaGranja, "No se encontró la granja con ID: " + id);
        }
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(vistaGranja, "ID no válido");
      }
    } else {
      JOptionPane.showMessageDialog(vistaGranja, "ID no válido");
    }
  }
}
