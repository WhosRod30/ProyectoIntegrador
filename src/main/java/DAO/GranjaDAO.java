package DAO;

import Conexion.Conexion;
import Modelo.Granja;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * private int id;
 * private String nombre;
 * private String codigo;
 * 
 * CREATE TABLE Granja (
 * id INT AUTO_INCREMENT PRIMARY KEY,
 * nombre VARCHAR(100),
 * codigo VARCHAR(50) UNIQUE
 * );
 * 
 * @author franc
 */
public class GranjaDAO extends Conexion {
  private static final Logger logger = LoggerFactory.getLogger(GranjaDAO.class);

  public static void guardarGranja(Granja granja) {
    logger.info("Guardando granja: {} - Código: {}", granja.getNombre(), granja.getCodigo());

    String sql = "INSERT INTO Granja (nombre, codigo) VALUES (?, ?)";
    try (var connection = ObtenerConexion();
        var preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, granja.getNombre());
      preparedStatement.setString(2, granja.getCodigo());
      preparedStatement.executeUpdate();

      logger.info("Granja guardada exitosamente");
    } catch (Exception e) {
      logger.error("Error al guardar la granja: {}", e.getMessage(), e);
      System.out.println("Error al guardar la granja: " + e.getMessage());
    }
  }

  public static void eliminarGranja(int id) {
    String sql = "DELETE FROM Granja WHERE id = ?";
    try (var connection = ObtenerConexion();
        var preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setInt(1, id);
      preparedStatement.executeUpdate();
    } catch (Exception e) {
      System.out.println("Error al eliminar la granja: " + e.getMessage());
    }
  }

  public static void editarGranja(Granja granja) {
    String sql = "UPDATE Granja SET nombre = ?, codigo = ? WHERE id = ?";
    try (var connection = ObtenerConexion();
        var preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, granja.getNombre());
      preparedStatement.setString(2, granja.getCodigo());
      preparedStatement.setInt(3, granja.getId());
      preparedStatement.executeUpdate();
    } catch (Exception e) {
      System.out.println("Error al editar la granja: " + e.getMessage());
    }
  }

  public static Granja buscarGranja(int id) {
    String sql = "SELECT * FROM Granja WHERE id = ?";
    try (var connection = ObtenerConexion();
        var preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setInt(1, id);
      var resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        Granja granja = new Granja();
        granja.setId(resultSet.getInt("id"));
        granja.setNombre(resultSet.getString("nombre"));
        granja.setCodigo(resultSet.getString("codigo"));
        return granja;
      }
    } catch (Exception e) {
      System.out.println("Error al buscar la granja: " + e.getMessage());
    }
    return null;
  }

  public static DefaultTableModel listarGranjas() {
    String[] columnas = { "ID", "Nombre", "Código" };
    String sql = "SELECT * FROM Granja";
    DefaultTableModel modelo = new DefaultTableModel(null, columnas);

    try (var connection = ObtenerConexion();
        var statement = connection.createStatement();
        var resultSet = statement.executeQuery(sql)) {
      while (resultSet.next()) {
        Object[] fila = new Object[3];
        fila[0] = resultSet.getInt("id");
        fila[1] = resultSet.getString("nombre");
        fila[2] = resultSet.getString("codigo");
        modelo.addRow(fila);
      }
    } catch (Exception e) {
      System.out.println("Error al listar las granjas: " + e.getMessage());
    }
    return modelo;
  }

  public static List<Granja> obtenerTodasLasGranjas() {
    List<Granja> granjas = new ArrayList<>();
    String sql = "SELECT * FROM Granja ORDER BY nombre";

    try (var connection = ObtenerConexion();
        var statement = connection.createStatement();
        var resultSet = statement.executeQuery(sql)) {
      while (resultSet.next()) {
        Granja granja = new Granja();
        granja.setId(resultSet.getInt("id"));
        granja.setNombre(resultSet.getString("nombre"));
        granja.setCodigo(resultSet.getString("codigo"));
        granjas.add(granja);
      }
    } catch (Exception e) {
      System.out.println("Error al obtener las granjas: " + e.getMessage());
    }
    return granjas;
  }
}
