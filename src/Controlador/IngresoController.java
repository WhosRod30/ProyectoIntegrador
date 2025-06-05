/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Vista.UI_Dashboard;
import Vista.UI_Ingreso;
import Vista.Modal.GranjaModal;
import Vista.Modal.IngresosModal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import DAO.IngresoDAO;
import Modelo.ControlIngreso;

/**
 *
 * @author franc
 */
public class IngresoController extends PanelController implements ActionListener {
  UI_Ingreso vistaIngreso;
  UI_Dashboard vista;

  public IngresoController(UI_Ingreso vistaIngreso, UI_Dashboard vista) {
    super(vistaIngreso, vista);
    this.vistaIngreso = vistaIngreso;
    showWindow(vistaIngreso);
    addListeners();
    cargarTablaIngresos();
  }

  @Override
  protected void addListeners() {
    vistaIngreso.btnBuscar.addActionListener(this);
    vistaIngreso.btnEditar.addActionListener(this);
    vistaIngreso.btnEliminar.addActionListener(this);
    vistaIngreso.btnNuevo.addActionListener(this);
  }

  private void cargarTablaIngresos() {
    vistaIngreso.tbContent.setModel(DAO.IngresoDAO.listarIngresos());
  }

  @Override
  protected void reloadWindow() {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                   // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == vistaIngreso.btnBuscar) {
      handleBuscar();
    } else if (e.getSource() == vistaIngreso.btnEditar) {
      handleEditar();
    } else if (e.getSource() == vistaIngreso.btnEliminar) {
      handleEliminar();
    } else if (e.getSource() == vistaIngreso.btnNuevo) {
      handleNuevo();
    }
  }

  private void abrirModalIngreso(ControlIngreso ingreso, boolean isEdit) {
    IngresosModal modal = new IngresosModal(vista, true);
    modal.setLocationRelativeTo(vista);
    modal.setTitle(isEdit ? "Editar Ingreso" : "Nuevo Ingreso");

    if (isEdit) {
      modal.setControlIngreso(ingreso);
    }

    modal.setVisible(true);

    if (modal.isSaved()) {
      if (isEdit) {
        actualizarIngreso(modal, ingreso.getId());
      } else {
        guardarNuevoIngreso(modal);
      }
      cargarTablaIngresos();
    }

  }

  private void guardarNuevoIngreso(IngresosModal modal) {
    ControlIngreso nuevoIngreso = construirIngresoDesdeModal(modal);
    IngresoDAO.guardarIngreso(nuevoIngreso);
    cargarTablaIngresos();
    JOptionPane.showMessageDialog(vista, "Ingreso guardado correctamente");
  }

  private void actualizarIngreso(IngresosModal modal, int id) {
    ControlIngreso nuevIngreso = construirIngresoDesdeModal(modal);
    nuevIngreso.setId(id);
    IngresoDAO.editarIngreso(nuevIngreso);
    cargarTablaIngresos();
    JOptionPane.showMessageDialog(vista, "Ingreso actualizado correctamente");
  }

  private ControlIngreso construirIngresoDesdeModal(IngresosModal modal) {
    ControlIngreso ingreso = new ControlIngreso();
    ingreso.setFecha(java.sql.Date.valueOf(modal.getFecha()));
    ingreso.setGranja(modal.getGranja());
    ingreso.setNumeroGalpon(modal.getGalpon());
    ingreso.setCantidad(modal.getCantidad());
    ingreso.setSexo(modal.getSexo());
    ingreso.setLote(modal.getLote());
    ingreso.setObservaciones(modal.getObservaciones());

    return ingreso;
  }

  private void handleNuevo() {
    abrirModalIngreso(null, false);
  }

  private void handleEditar() {
    String id = JOptionPane.showInputDialog(vistaIngreso, "Ingrese el ID del ingreso a editar:");
    if (id != null && !id.isEmpty()) {
      try {
        int idIngreso = Integer.parseInt(id);
        ControlIngreso ingreso = IngresoDAO.buscarIngreso(idIngreso);

        if (ingreso != null) {
          abrirModalIngreso(ingreso, true);
        } else {
          JOptionPane.showMessageDialog(vistaIngreso, "Ingreso no encontrado");
        }
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(vistaIngreso, "ID inválido");
      }
    } else {
      JOptionPane.showMessageDialog(vistaIngreso, "ID inválido");
    }
  }

  private void handleEliminar() {
    String id = JOptionPane.showInputDialog(vistaIngreso, "Ingrese el ID del ingreso a eliminar:");
    if (id != null && !id.isEmpty()) {
      try {
        int idIngreso = Integer.parseInt(id);
        IngresoDAO.eliminarIngreso(idIngreso);
        cargarTablaIngresos();
        JOptionPane.showMessageDialog(vistaIngreso, "Ingreso eliminado correctamente");
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(vistaIngreso, "ID inválido");
      }
    } else {
      JOptionPane.showMessageDialog(vistaIngreso, "ID inválido");
    }
  }

  private void handleBuscar() {
    String id = JOptionPane.showInputDialog(vistaIngreso, "Ingrese el ID del ingreso a buscar:");
    if (id != null && !id.isEmpty()) {
      try {
        int idIngreso = Integer.parseInt(id);
        ControlIngreso ingreso = IngresoDAO.buscarIngreso(idIngreso);

        if (ingreso != null) {
          JOptionPane.showMessageDialog(vistaIngreso, "Ingreso encontrado: " + ingreso.getObservaciones());
        } else {
          JOptionPane.showMessageDialog(vistaIngreso, "Ingreso no encontrado");
        }
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(vistaIngreso, "ID inválido");
      }
    } else {
      JOptionPane.showMessageDialog(vistaIngreso, "ID inválido");
    }
  }

}
