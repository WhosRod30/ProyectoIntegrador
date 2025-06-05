package Controlador;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

import DAO.ConsumoDao;
import Modelo.ControlConsumo;
import Vista.UI_Consumo;
import Vista.UI_Dashboard;
import Vista.Modal.ConsumoModal;

/**
 * Controlador para gestionar las acciones relacionadas con el consumo.
 * Maneja la interacción entre la vista y el modelo.
 * 
 * @author franc
 */
public class ConsumoController extends PanelController implements ActionListener {

  private final UI_Consumo vistaConsumo;
  private final UI_Dashboard vistaDashboard;

  public static final String[] TITULOS_CONSUMO = { "ID", "Fecha", "Alimento", "Cantidad", "Observaciones", "Granja" };

  /**
   * Constructor del controlador de consumo.
   * 
   * @param vistaConsumo   Vista de consumo.
   * @param vistaDashboard Vista del dashboard principal.
   */
  public ConsumoController(UI_Consumo vistaConsumo, UI_Dashboard vistaDashboard) {
    super(vistaConsumo, vistaDashboard);
    this.vistaConsumo = vistaConsumo;
    this.vistaDashboard = vistaDashboard;

    showWindow(vistaConsumo);
    addListeners();
    cargarTablaConsumos();
  }

  /**
   * Agrega los listeners a los botones de la vista.
   */
  @Override
  protected void addListeners() {
    vistaConsumo.btnBuscar.addActionListener(this);
    vistaConsumo.btnEditar.addActionListener(this);
    vistaConsumo.btnEliminar.addActionListener(this);
    vistaConsumo.btnNuevo.addActionListener(this);
  }

  /**
   * Recarga la ventana (pendiente de implementación).
   */
  @Override
  protected void reloadWindow() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   * Maneja los eventos de los botones de la vista.
   * 
   * @param e Evento de acción.
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == vistaConsumo.btnBuscar) {
      handleBuscar();
    } else if (e.getSource() == vistaConsumo.btnEditar) {
      handleEditar();
    } else if (e.getSource() == vistaConsumo.btnEliminar) {
      handleEliminar();
    } else if (e.getSource() == vistaConsumo.btnNuevo) {
      handleNuevo();
    }
  }

  /**
   * Maneja la acción de buscar un consumo por ID.
   */
  private void handleBuscar() {
    String id = JOptionPane.showInputDialog(vistaConsumo, "Ingrese el ID del consumo a buscar:");

    if (id != null && !id.isEmpty()) {
      try {
        int idConsumo = Integer.parseInt(id);
        ControlConsumo consumo = new ConsumoDao().buscarConsumo(idConsumo);

        if (consumo != null) {
          JOptionPane.showMessageDialog(vistaConsumo, "Detalles del Consumo:\n" + consumo);
        } else {
          JOptionPane.showMessageDialog(vistaConsumo, "No se encontró el consumo con ID: " + id);
        }
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(vistaConsumo, "ID no válido");
      }
    } else {
      JOptionPane.showMessageDialog(vistaConsumo, "ID no válido");
    }
  }

  /**
   * Maneja la acción de editar un consumo.
   */
  private void handleEditar() {
    String id = JOptionPane.showInputDialog(vistaConsumo, "Ingrese el ID del consumo a editar:");

    if (id != null && !id.isEmpty()) {
      try {
        int idConsumo = Integer.parseInt(id);
        ControlConsumo consumo = new ConsumoDao().buscarConsumo(idConsumo);

        if (consumo != null) {
          abrirModalConsumo(consumo, true);
        } else {
          JOptionPane.showMessageDialog(vistaConsumo, "No se encontró el consumo con ID: " + id);
        }
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(vistaConsumo, "ID no válido");
      }
    } else {
      JOptionPane.showMessageDialog(vistaConsumo, "ID no válido");
    }
  }

  /**
   * Maneja la acción de eliminar un consumo.
   */
  private void handleEliminar() {
    String id = JOptionPane.showInputDialog(vistaConsumo, "Ingrese el ID del consumo a eliminar:");

    if (id != null && !id.isEmpty()) {
      try {
        int idConsumo = Integer.parseInt(id);
        new ConsumoDao().eliminarConsumo(idConsumo);
        cargarTablaConsumos();
        JOptionPane.showMessageDialog(vistaConsumo, "Consumo eliminado con éxito");
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(vistaConsumo, "ID no válido");
      }
    } else {
      JOptionPane.showMessageDialog(vistaConsumo, "ID no válido");
    }
  }

  /**
   * Maneja la acción de crear un nuevo consumo.
   */
  private void handleNuevo() {
    abrirModalConsumo(null, false);
  }

  /**
   * Abre el modal de consumo para crear o editar un consumo.
   * 
   * @param consumo   Objeto de consumo a editar (null para nuevo).
   * @param isEditing Indica si es modo edición.
   */
  private void abrirModalConsumo(ControlConsumo consumo, boolean isEditing) {
    ConsumoModal consumoModal = new ConsumoModal(vistaDashboard, true);
    consumoModal.setLocationRelativeTo(vistaDashboard);
    consumoModal.setTitle(isEditing ? "Editar Consumo" : "Nuevo Consumo");
    consumoModal.setEditMode(isEditing);

    if (isEditing) {
      consumoModal.setControlConsumo(consumo);
    }

    consumoModal.setVisible(true);

    if (consumoModal.isSaved()) {
      if (isEditing) {
        actualizarConsumo(consumoModal, consumo.getId());
      } else {
        guardarNuevoConsumo(consumoModal);
      }
    }
  }

  /**
   * Guarda un nuevo consumo en la base de datos.
   * 
   * @param consumoModal Modal con los datos del nuevo consumo.
   */
  private void guardarNuevoConsumo(ConsumoModal consumoModal) {
    ControlConsumo nuevoConsumo = construirConsumoDesdeModal(consumoModal);
    new ConsumoDao().guardarConsumo(nuevoConsumo);
    cargarTablaConsumos();
  }

  /**
   * Actualiza un consumo existente en la base de datos.
   * 
   * @param consumoModal Modal con los datos actualizados.
   * @param id           ID del consumo a actualizar.
   */
  private void actualizarConsumo(ConsumoModal consumoModal, int id) {
    ControlConsumo consumoActualizado = construirConsumoDesdeModal(consumoModal);
    consumoActualizado.setId(id);
    new ConsumoDao().editarConsumo(consumoActualizado);
    cargarTablaConsumos();
    JOptionPane.showMessageDialog(vistaConsumo, "Consumo editado con éxito");
  }

  /**
   * Construye un objeto ControlConsumo a partir de los datos del modal.
   * 
   * @param consumoModal Modal con los datos del consumo.
   * @return Objeto ControlConsumo.
   */
  private ControlConsumo construirConsumoDesdeModal(ConsumoModal consumoModal) {
    ControlConsumo consumo = new ControlConsumo();
    consumo.setFecha(java.sql.Date.valueOf(consumoModal.getFecha()));
    consumo.setTipoAlimento(consumoModal.getAlimento());
    consumo.setCantidad(Double.parseDouble(consumoModal.getCantidad()));
    consumo.setObservaciones(consumoModal.getObservaciones());
    consumo.setGranja(consumoModal.getGranja());
    consumo.setNumeroGalpon(Integer.parseInt(consumoModal.getGalpon().toString()));
    return consumo;
  }

  /**
   * Carga los datos de los consumos en la tabla de la vista.
   */
  private void cargarTablaConsumos() {
    vistaConsumo.tbContent.setModel(ConsumoDao.listarConsumos());
  }
}
