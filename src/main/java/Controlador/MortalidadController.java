package Controlador;

import DAO.MortalidadDAO;
import DAO.GranjaDAO;
import Modelo.ControlMortalidad;
import Modelo.Granja;
import Vista.Modal.MortalidadModal;
import Vista.UI_mortalidad;
import Vista.UI_Dashboard;
import Util.MortalidadPDFExporter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

/**
 * Controlador para gestionar las acciones relacionadas con el control de
 * mortalidad.
 * Maneja la interacción entre la vista y el modelo.
 * 
 * @author franc
 */
public class MortalidadController extends PanelController implements ActionListener {

  private final UI_mortalidad vistaMortalidad;
  private final UI_Dashboard vistaDashboard;

  public static final String[] TITULOS_MORTALIDAD = { "ID", "Fecha", "Granja", "Galpón", "Cantidad", "Sexo", "Lote",
      "Observaciones" };

  /**
   * Constructor del controlador de mortalidad.
   * 
   * @param vistaMortalidad Vista de mortalidad.
   * @param vistaDashboard  Vista del dashboard principal.
   */
  public MortalidadController(UI_mortalidad vistaMortalidad, UI_Dashboard vistaDashboard) {
    super(vistaMortalidad, vistaDashboard);
    this.vistaMortalidad = vistaMortalidad;
    this.vistaDashboard = vistaDashboard;

    showWindow(vistaMortalidad);
    addListeners();
    cargarTablaMortalidades();
  }

  /**
   * Agrega los listeners a los botones de la vista.
   */
  @Override
  protected void addListeners() {
    vistaMortalidad.btnBuscar.addActionListener(this);
    vistaMortalidad.btnEditar.addActionListener(this);
    vistaMortalidad.btnEliminar.addActionListener(this);
    vistaMortalidad.btnNuevo.addActionListener(this);

    // Agregar listener para el botón de exportar PDF (si existe en la vista)
    try {
      java.lang.reflect.Field btnExportarField = vistaMortalidad.getClass().getField("btnExportar");
      JButton btnExportar = (JButton) btnExportarField.get(vistaMortalidad);
      btnExportar.addActionListener(this);
    } catch (Exception e) {
      // El botón no existe, lo crearemos programáticamente
      agregarBotonExportar();
    }
  }

  /**
   * Agrega un botón de exportar PDF programáticamente si no existe en la vista.
   */
  private void agregarBotonExportar() {
    JButton btnExportar = new JButton("Exportar PDF");
    btnExportar.setFont(new Font("Roboto", Font.BOLD, 14));
    btnExportar.setForeground(Color.WHITE);
    btnExportar.setBackground(new Color(45, 90, 39)); // Verde avícola
    btnExportar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    btnExportar.setFocusPainted(false);
    btnExportar.addActionListener(this);

    // Agregar el botón al panel superior si existe
    try {
      Container panel = vistaMortalidad.btnNuevo.getParent();
      panel.add(btnExportar);
      panel.revalidate();
      panel.repaint();
    } catch (Exception e) {
      System.err.println("No se pudo agregar el botón de exportar: " + e.getMessage());
    }
  }

  /**
   * Maneja los eventos de los botones de la vista.
   * 
   * @param e Evento de acción.
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    String buttonText = ((JButton) e.getSource()).getText();

    if (e.getSource() == vistaMortalidad.btnBuscar) {
      handleBuscar();
    } else if (e.getSource() == vistaMortalidad.btnEditar) {
      handleEditar();
    } else if (e.getSource() == vistaMortalidad.btnEliminar) {
      handleEliminar();
    } else if (e.getSource() == vistaMortalidad.btnNuevo) {
      handleNuevo();
    } else if ("Exportar PDF".equals(buttonText)) {
      handleExportarPDF();
    }
  }

  /**
   * Maneja la acción de buscar un control de mortalidad por ID.
   */
  private void handleBuscar() {
    String id = JOptionPane.showInputDialog(vistaMortalidad, "Ingrese el ID del control de mortalidad a buscar:");

    if (id != null && !id.isEmpty()) {
      try {
        int idMortalidad = Integer.parseInt(id);
        ControlMortalidad mortalidad = MortalidadDAO.buscarMortalidad(idMortalidad);

        if (mortalidad != null) {
          JOptionPane.showMessageDialog(vistaMortalidad, "Detalles del Control de Mortalidad:\n" +
              "Fecha: " + mortalidad.getFecha() + "\n" +
              "Granja: " + mortalidad.getGranja().getNombre() + "\n" +
              "Galpón: " + mortalidad.getNumeroGalpon() + "\n" +
              "Cantidad: " + mortalidad.getCantidad() + "\n" +
              "Sexo: " + mortalidad.getSexo() + "\n" +
              "Lote: " + mortalidad.getLote() + "\n" +
              "Observaciones: " + mortalidad.getObservaciones());
        } else {
          JOptionPane.showMessageDialog(vistaMortalidad, "No se encontró el control de mortalidad con ID: " + id);
        }
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(vistaMortalidad, "ID no válido");
      }
    } else {
      JOptionPane.showMessageDialog(vistaMortalidad, "ID no válido");
    }
  }

  /**
   * Maneja la acción de editar un control de mortalidad.
   */
  private void handleEditar() {
    String id = JOptionPane.showInputDialog(vistaMortalidad, "Ingrese el ID del control de mortalidad a editar:");

    if (id != null && !id.isEmpty()) {
      try {
        int idMortalidad = Integer.parseInt(id);
        ControlMortalidad mortalidad = MortalidadDAO.buscarMortalidad(idMortalidad);

        if (mortalidad != null) {
          abrirModalMortalidad(mortalidad, true);
        } else {
          JOptionPane.showMessageDialog(vistaMortalidad, "No se encontró el control de mortalidad con ID: " + id);
        }
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(vistaMortalidad, "ID no válido");
      }
    } else {
      JOptionPane.showMessageDialog(vistaMortalidad, "ID no válido");
    }
  }

  /**
   * Maneja la acción de eliminar un control de mortalidad.
   */
  private void handleEliminar() {
    String id = JOptionPane.showInputDialog(vistaMortalidad, "Ingrese el ID del control de mortalidad a eliminar:");

    if (id != null && !id.isEmpty()) {
      try {
        int idMortalidad = Integer.parseInt(id);
        MortalidadDAO.eliminarMortalidad(idMortalidad);
        cargarTablaMortalidades();
        JOptionPane.showMessageDialog(vistaMortalidad, "Control de mortalidad eliminado con éxito");
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(vistaMortalidad, "ID no válido");
      }
    } else {
      JOptionPane.showMessageDialog(vistaMortalidad, "ID no válido");
    }
  }

  /**
   * Maneja la acción de crear un nuevo control de mortalidad.
   */
  private void handleNuevo() {
    abrirModalMortalidad(null, false);
  }

  /**
   * Abre el modal de mortalidad para crear o editar un control.
   * 
   * @param mortalidad Objeto de mortalidad a editar (null para nuevo).
   * @param isEditing  Indica si es modo edición.
   */
  private void abrirModalMortalidad(ControlMortalidad mortalidad, boolean isEditing) {
    MortalidadModal mortalidadModal = new MortalidadModal(vistaDashboard, true);
    mortalidadModal.setLocationRelativeTo(vistaDashboard);
    mortalidadModal.setTitle(isEditing ? "Editar Control de Mortalidad" : "Nuevo Control de Mortalidad");

    // Cargar granjas en el ComboBox
    cargarGranjasEnModal(mortalidadModal);

    if (isEditing) {
      // Cargar los datos del control de mortalidad en el modal
      mortalidadModal.setEditMode(true);
      mortalidadModal.setControlMortalidad(mortalidad);
    }

    mortalidadModal.setVisible(true);

    if (mortalidadModal.isSaved()) {
      if (isEditing) {
        actualizarMortalidad(mortalidadModal, mortalidad.getId());
      } else {
        guardarNuevaMortalidad(mortalidadModal);
      }
    }
  }

  /**
   * Carga las granjas en el ComboBox del modal.
   * 
   * @param modal Modal de mortalidad.
   */
  private void cargarGranjasEnModal(MortalidadModal modal) {
    List<Granja> granjas = GranjaDAO.obtenerTodasLasGranjas();
    DefaultComboBoxModel<Object> modeloCombo = new DefaultComboBoxModel<>();

    // Agregar placeholder como primer elemento
    modeloCombo.addElement("[Seleccionar Granja]");

    for (Granja granja : granjas) {
      modeloCombo.addElement(granja);
    }

    modal.cbGranja.setModel(modeloCombo);
  }

  /**
   * Guarda un nuevo control de mortalidad en la base de datos.
   * 
   * @param mortalidadModal Modal con los datos del nuevo control.
   */
  private void guardarNuevaMortalidad(MortalidadModal mortalidadModal) {
    ControlMortalidad nuevaMortalidad = construirMortalidadDesdeModal(mortalidadModal);
    MortalidadDAO.guardarMortalidad(nuevaMortalidad);
    cargarTablaMortalidades();
    JOptionPane.showMessageDialog(vistaMortalidad, "Control de mortalidad guardado con éxito");
  }

  /**
   * Actualiza un control de mortalidad existente en la base de datos.
   * 
   * @param mortalidadModal Modal con los datos actualizados.
   * @param id              ID del control a actualizar.
   */
  private void actualizarMortalidad(MortalidadModal mortalidadModal, int id) {
    ControlMortalidad mortalidadActualizada = construirMortalidadDesdeModal(mortalidadModal);
    mortalidadActualizada.setId(id);
    MortalidadDAO.editarMortalidad(mortalidadActualizada);
    cargarTablaMortalidades();
    JOptionPane.showMessageDialog(vistaMortalidad, "Control de mortalidad editado con éxito");
  }

  /**
   * Construye un objeto ControlMortalidad a partir de los datos del modal.
   * 
   * @param mortalidadModal Modal con los datos del control.
   * @return Objeto ControlMortalidad.
   */
  private ControlMortalidad construirMortalidadDesdeModal(MortalidadModal mortalidadModal) {
    ControlMortalidad mortalidad = new ControlMortalidad();
    mortalidad.setFecha(java.sql.Date.valueOf(mortalidadModal.getFecha()));
    mortalidad.setGranja((Granja) mortalidadModal.cbGranja.getSelectedItem());
    mortalidad.setNumeroGalpon(Integer.parseInt(mortalidadModal.getGalpon()));
    mortalidad.setCantidad(Integer.parseInt(mortalidadModal.getCantidad()));
    mortalidad.setSexo((String) mortalidadModal.Sexo.getSelectedItem());
    mortalidad.setLote(mortalidadModal.getAlimento()); // getAlimento() retorna el lote
    mortalidad.setObservaciones(mortalidadModal.getObservaciones());
    return mortalidad;
  }

  /**
   * Maneja la exportación del reporte de mortalidad a PDF.
   */
  private void handleExportarPDF() {
    try {
      // Crear el selector de archivos
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setDialogTitle("Guardar Reporte de Mortalidad");
      fileChooser.setSelectedFile(new File("Reporte_Mortalidad_" +
          new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) + ".pdf"));

      // Filtro para archivos PDF
      FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos PDF (*.pdf)", "pdf");
      fileChooser.setFileFilter(filter);

      // Mostrar el diálogo
      int result = fileChooser.showSaveDialog(vistaMortalidad);

      if (result == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();

        // Asegurar que tenga extensión .pdf
        String filePath = selectedFile.getAbsolutePath();
        if (!filePath.toLowerCase().endsWith(".pdf")) {
          filePath += ".pdf";
          selectedFile = new File(filePath);
        }

        // Generar el reporte
        MortalidadPDFExporter.exportarReporte(filePath);

        // Mostrar mensaje de éxito
        JOptionPane.showMessageDialog(vistaMortalidad,
            "Reporte exportado exitosamente en:\n" + filePath,
            "Exportación Exitosa",
            JOptionPane.INFORMATION_MESSAGE);

        // Preguntar si desea abrir el archivo
        int openResult = JOptionPane.showConfirmDialog(vistaMortalidad,
            "¿Desea abrir el archivo generado?",
            "Abrir Archivo",
            JOptionPane.YES_NO_OPTION);

        if (openResult == JOptionPane.YES_OPTION) {
          try {
            Desktop.getDesktop().open(selectedFile);
          } catch (Exception ex) {
            JOptionPane.showMessageDialog(vistaMortalidad,
                "No se pudo abrir el archivo automáticamente.\nUbicación: " + filePath,
                "Información",
                JOptionPane.INFORMATION_MESSAGE);
          }
        }
      }
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(vistaMortalidad,
          "Error al exportar el reporte: " + ex.getMessage(),
          "Error de Exportación",
          JOptionPane.ERROR_MESSAGE);
      ex.printStackTrace();
    }
  }

  /**
   * Carga los datos de los controles de mortalidad en la tabla de la vista.
   */
  private void cargarTablaMortalidades() {
    vistaMortalidad.tbContent.setModel(MortalidadDAO.listarMortalidades());
  }
}
