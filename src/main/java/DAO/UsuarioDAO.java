package DAO;

import Conexion.Conexion;
import Modelo.Usuario;
import Util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar las operaciones CRUD de Usuario
 * 
 * CREATE TABLE usuario
 * (
 * id INT AUTO_INCREMENT PRIMARY KEY,
 * email VARCHAR(100) NOT NULL UNIQUE,
 * password VARCHAR(255) NOT NULL
 * );
 * 
 * @author franc
 */
public class UsuarioDAO extends Conexion {
  private static final Logger logger = LoggerFactory.getLogger(UsuarioDAO.class);

  public static void guardarUsuario(Usuario usuario) {
    String sql = "INSERT INTO usuario (email, password) VALUES (?, ?)";
    try (var connection = ObtenerConexion();
        var preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, usuario.getEmail());
      // Encriptar la contraseña antes de guardarla
      String hashedPassword = PasswordUtil.encryptPassword(usuario.getPassword());
      preparedStatement.setString(2, hashedPassword);
      preparedStatement.executeUpdate();
    } catch (Exception e) {
      System.out.println("Error al guardar el usuario: " + e.getMessage());
    }
  }

  public static void eliminarUsuario(int id) {
    String sql = "DELETE FROM usuario WHERE id = ?";
    try (var connection = ObtenerConexion();
        var preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setInt(1, id);
      preparedStatement.executeUpdate();
    } catch (Exception e) {
      System.out.println("Error al eliminar el usuario: " + e.getMessage());
    }
  }

  public static void editarUsuario(Usuario usuario) {
    String sql = "UPDATE usuario SET email = ?, password = ? WHERE id = ?";
    try (var connection = ObtenerConexion();
        var preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, usuario.getEmail());
      // Encriptar la contraseña antes de actualizarla
      String hashedPassword = PasswordUtil.encryptPassword(usuario.getPassword());
      preparedStatement.setString(2, hashedPassword);
      preparedStatement.setInt(3, usuario.getId());
      preparedStatement.executeUpdate();
    } catch (Exception e) {
      System.out.println("Error al editar el usuario: " + e.getMessage());
    }
  }

  /**
   * Edita un usuario actualizando solo el email, manteniendo la contraseña
   * actual.
   * 
   * @param usuario Usuario con ID y email a actualizar
   */
  public static void editarUsuarioSinPassword(Usuario usuario) {
    String sql = "UPDATE usuario SET email = ? WHERE id = ?";
    try (var connection = ObtenerConexion();
        var preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, usuario.getEmail());
      preparedStatement.setInt(2, usuario.getId());
      preparedStatement.executeUpdate();
    } catch (Exception e) {
      System.out.println("Error al editar el usuario sin contraseña: " + e.getMessage());
    }
  }

  public static Usuario buscarUsuario(int id) {
    String sql = "SELECT * FROM usuario WHERE id = ?";
    try (var connection = ObtenerConexion();
        var preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setInt(1, id);
      var resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        Usuario usuario = new Usuario();
        usuario.setId(resultSet.getInt("id"));
        usuario.setEmail(resultSet.getString("email"));
        usuario.setPassword(resultSet.getString("password"));
        return usuario;
      }
    } catch (Exception e) {
      System.out.println("Error al buscar el usuario: " + e.getMessage());
    }
    return null;
  }

  public static Usuario buscarUsuarioPorEmail(String email) {
    String sql = "SELECT * FROM usuario WHERE email = ?";
    try (var connection = ObtenerConexion();
        var preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, email);
      var resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        Usuario usuario = new Usuario();
        usuario.setId(resultSet.getInt("id"));
        usuario.setEmail(resultSet.getString("email"));
        usuario.setPassword(resultSet.getString("password"));
        return usuario;
      }
    } catch (Exception e) {
      System.out.println("Error al buscar el usuario por email: " + e.getMessage());
    }
    return null;
  }

  /**
   * Autentica un usuario verificando email y contraseña.
   * 
   * @param email         Email del usuario
   * @param plainPassword Contraseña en texto plano
   * @return true si las credenciales son válidas, false en caso contrario
   */
  public static boolean autenticarUsuario(String email, String plainPassword) {
    Usuario usuario = buscarUsuarioPorEmail(email);
    if (usuario != null) {
      return PasswordUtil.verifyPassword(plainPassword, usuario.getPassword());
    }
    return false;
  }

  public static DefaultTableModel listarUsuarios() {
    String[] columnas = { "ID", "Email", "Password" };
    String sql = "SELECT * FROM usuario ORDER BY email";
    DefaultTableModel modelo = new DefaultTableModel(null, columnas);

    try (var connection = ObtenerConexion();
        var statement = connection.createStatement();
        var resultSet = statement.executeQuery(sql)) {
      while (resultSet.next()) {
        Object[] fila = new Object[3];
        fila[0] = resultSet.getInt("id");
        fila[1] = resultSet.getString("email");
        fila[2] = "•••••••••"; // Ocultar password en la tabla
        modelo.addRow(fila);
      }
    } catch (Exception e) {
      System.out.println("Error al listar los usuarios: " + e.getMessage());
    }
    return modelo;
  }

  public static List<Usuario> obtenerTodosLosUsuarios() {
    List<Usuario> usuarios = new ArrayList<>();
    String sql = "SELECT * FROM usuario ORDER BY email";

    try (var connection = ObtenerConexion();
        var statement = connection.createStatement();
        var resultSet = statement.executeQuery(sql)) {
      while (resultSet.next()) {
        Usuario usuario = new Usuario();
        usuario.setId(resultSet.getInt("id"));
        usuario.setEmail(resultSet.getString("email"));
        usuario.setPassword(resultSet.getString("password"));
        usuarios.add(usuario);
      }
    } catch (Exception e) {
      System.out.println("Error al obtener los usuarios: " + e.getMessage());
    }
    return usuarios;
  }
}
