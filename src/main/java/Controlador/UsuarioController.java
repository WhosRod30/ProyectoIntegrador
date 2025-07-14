package Controlador;

import DAO.UsuarioDAO;
import Modelo.Usuario;
import Vista.Modal.UsuarioModal;
import Vista.UI_Usuario;
import Vista.UI_Dashboard;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controlador para gestionar las acciones relacionadas con los usuarios.
 * Maneja la interacción entre la vista y el modelo.
 * 
 * @author franc
 */
public class UsuarioController extends PanelController implements ActionListener {

  private final UI_Usuario vistaUsuario;
  private final UI_Dashboard vistaDashboard;

  public static final String[] TITULOS_USUARIO = { "ID", "Email", "Password" };

  /**
   * Constructor del controlador de usuario.
   * 
   * @param vistaUsuario   Vista de usuario.
   * @param vistaDashboard Vista del dashboard principal.
   */
  public UsuarioController(UI_Usuario vistaUsuario, UI_Dashboard vistaDashboard) {
    super(vistaUsuario, vistaDashboard);
    this.vistaUsuario = vistaUsuario;
    this.vistaDashboard = vistaDashboard;

    showWindow(vistaUsuario);
    addListeners();
    cargarTablaUsuarios();
  }

  /**
   * Agrega los listeners a los botones de la vista.
   */
  @Override
  protected void addListeners() {
    vistaUsuario.btnBuscar.addActionListener(this);
    vistaUsuario.btnEditar.addActionListener(this);
    vistaUsuario.btnEliminar.addActionListener(this);
    vistaUsuario.btnNuevo.addActionListener(this);
  }

  /**
   * Maneja los eventos de los botones de la vista.
   * 
   * @param e Evento de acción.
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == vistaUsuario.btnBuscar) {
      handleBuscar();
    } else if (e.getSource() == vistaUsuario.btnEditar) {
      handleEditar();
    } else if (e.getSource() == vistaUsuario.btnEliminar) {
      handleEliminar();
    } else if (e.getSource() == vistaUsuario.btnNuevo) {
      handleNuevo();
    }
  }

  /**
   * Maneja la acción de buscar un usuario por ID.
   */
  private void handleBuscar() {
    String id = JOptionPane.showInputDialog(vistaUsuario, "Ingrese el ID del usuario a buscar:");

    if (id != null && !id.isEmpty()) {
      try {
        int idUsuario = Integer.parseInt(id);
        Usuario usuario = UsuarioDAO.buscarUsuario(idUsuario);

        if (usuario != null) {
          JOptionPane.showMessageDialog(vistaUsuario, "Detalles del Usuario:\n" +
              "ID: " + usuario.getId() + "\n" +
              "Email: " + usuario.getEmail() + "\n" +
              "Password: •••••••••");
        } else {
          JOptionPane.showMessageDialog(vistaUsuario, "No se encontró el usuario con ID: " + id);
        }
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(vistaUsuario, "ID no válido");
      }
    } else {
      JOptionPane.showMessageDialog(vistaUsuario, "ID no válido");
    }
  }

  /**
   * Maneja la acción de editar un usuario.
   */
  private void handleEditar() {
    String id = JOptionPane.showInputDialog(vistaUsuario, "Ingrese el ID del usuario a editar:");

    if (id != null && !id.isEmpty()) {
      try {
        int idUsuario = Integer.parseInt(id);
        Usuario usuario = UsuarioDAO.buscarUsuario(idUsuario);

        if (usuario != null) {
          abrirModalUsuario(usuario, true);
        } else {
          JOptionPane.showMessageDialog(vistaUsuario, "No se encontró el usuario con ID: " + id);
        }
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(vistaUsuario, "ID no válido");
      }
    } else {
      JOptionPane.showMessageDialog(vistaUsuario, "ID no válido");
    }
  }

  /**
   * Maneja la acción de eliminar un usuario.
   */
  private void handleEliminar() {
    String id = JOptionPane.showInputDialog(vistaUsuario, "Ingrese el ID del usuario a eliminar:");

    if (id != null && !id.isEmpty()) {
      try {
        int idUsuario = Integer.parseInt(id);

        // Confirmar eliminación
        int respuesta = JOptionPane.showConfirmDialog(vistaUsuario,
            "¿Está seguro de que desea eliminar este usuario?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION);

        if (respuesta == JOptionPane.YES_OPTION) {
          UsuarioDAO.eliminarUsuario(idUsuario);
          cargarTablaUsuarios();
          JOptionPane.showMessageDialog(vistaUsuario, "Usuario eliminado con éxito");
        }
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(vistaUsuario, "ID no válido");
      }
    } else {
      JOptionPane.showMessageDialog(vistaUsuario, "ID no válido");
    }
  }

  /**
   * Maneja la acción de crear un nuevo usuario.
   */
  private void handleNuevo() {
    abrirModalUsuario(null, false);
  }

  /**
   * Abre el modal de usuario para crear o editar.
   * 
   * @param usuario   Objeto de usuario a editar (null para nuevo).
   * @param isEditing Indica si es modo edición.
   */
  private void abrirModalUsuario(Usuario usuario, boolean isEditing) {
    UsuarioModal usuarioModal = new UsuarioModal(vistaDashboard, true);
    usuarioModal.setLocationRelativeTo(vistaDashboard);
    usuarioModal.setTitle(isEditing ? "Editar Usuario" : "Nuevo Usuario");
    usuarioModal.setEditMode(isEditing);

    if (isEditing && usuario != null) {
      usuarioModal.setUsuario(usuario);
    } else {
      usuarioModal.limpiarCampos();
    }

    usuarioModal.setVisible(true);

    if (usuarioModal.isSaved()) {
      if (isEditing) {
        actualizarUsuario(usuarioModal, usuario.getId());
      } else {
        guardarNuevoUsuario(usuarioModal);
      }
    }
  }

  /**
   * Guarda un nuevo usuario en la base de datos.
   * 
   * @param usuarioModal Modal con los datos del nuevo usuario.
   */
  private void guardarNuevoUsuario(UsuarioModal usuarioModal) {
    try {
      // Verificar si el email ya existe
      Usuario usuarioExistente = UsuarioDAO.buscarUsuarioPorEmail(usuarioModal.getEmail());
      if (usuarioExistente != null) {
        JOptionPane.showMessageDialog(vistaUsuario, "Ya existe un usuario con ese email");
        return;
      }

      Usuario nuevoUsuario = new Usuario();
      nuevoUsuario.setEmail(usuarioModal.getEmail());
      nuevoUsuario.setPassword(usuarioModal.getPassword());

      UsuarioDAO.guardarUsuario(nuevoUsuario);
      cargarTablaUsuarios();
      JOptionPane.showMessageDialog(vistaUsuario, "Usuario guardado con éxito");
    } catch (Exception e) {
      JOptionPane.showMessageDialog(vistaUsuario, "Error al guardar el usuario: " + e.getMessage());
    }
  }

  /**
   * Actualiza un usuario existente en la base de datos.
   * 
   * @param usuarioModal Modal con los datos actualizados.
   * @param id           ID del usuario a actualizar.
   */
  private void actualizarUsuario(UsuarioModal usuarioModal, int id) {
    try {
      // Verificar si el email ya existe en otro usuario
      Usuario usuarioExistente = UsuarioDAO.buscarUsuarioPorEmail(usuarioModal.getEmail());
      if (usuarioExistente != null && usuarioExistente.getId() != id) {
        JOptionPane.showMessageDialog(vistaUsuario, "Ya existe otro usuario con ese email");
        return;
      }

      Usuario usuarioActualizado = new Usuario();
      usuarioActualizado.setId(id);
      usuarioActualizado.setEmail(usuarioModal.getEmail());

      // Solo actualizar contraseña si se proporcionó una nueva
      if (usuarioModal.shouldUpdatePassword()) {
        usuarioActualizado.setPassword(usuarioModal.getPassword());
        UsuarioDAO.editarUsuario(usuarioActualizado);
      } else {
        // Actualizar solo el email
        UsuarioDAO.editarUsuarioSinPassword(usuarioActualizado);
      }

      cargarTablaUsuarios();
      JOptionPane.showMessageDialog(vistaUsuario, "Usuario editado con éxito");
    } catch (Exception e) {
      JOptionPane.showMessageDialog(vistaUsuario, "Error al editar el usuario: " + e.getMessage());
    }
  }

  /**
   * Carga los datos de los usuarios en la tabla de la vista.
   */
  private void cargarTablaUsuarios() {
    vistaUsuario.tbContent.setModel(UsuarioDAO.listarUsuarios());
  }
}
