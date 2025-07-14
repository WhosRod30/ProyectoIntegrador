package DAO;

import Conexion.Conexion;
import Modelo.ControlMortalidad;
import Modelo.Granja;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar las operaciones de Control de Mortalidad
 * 
 * CREATE TABLE ControlMortalidad
 * (
 * id INT AUTO_INCREMENT PRIMARY KEY,
 * fecha DATE NOT NULL,
 * id_granja INT NOT NULL,
 * numero_galpon INT NOT NULL,
 * cantidad INT NOT NULL CHECK (cantidad >= 0),
 * sexo ENUM ('M','F') NOT NULL,
 * lote VARCHAR(50),
 * observaciones TEXT,
 * FOREIGN KEY (id_granja) REFERENCES Granja (id)
 * );
 * 
 * @author franc
 */
public class MortalidadDAO extends Conexion {
  private static final Logger logger = LoggerFactory.getLogger(MortalidadDAO.class);

  public static void guardarMortalidad(ControlMortalidad mortalidad) {
    logger.info("Guardando control de mortalidad - Granja: {}, Galpón: {}, Cantidad: {}",
        mortalidad.getGranja().getNombre(), mortalidad.getNumeroGalpon(), mortalidad.getCantidad());

    String sql = "INSERT INTO ControlMortalidad (fecha, id_granja, numero_galpon, cantidad, sexo, lote, observaciones) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (var connection = ObtenerConexion();
        var preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setDate(1, new Date(mortalidad.getFecha().getTime()));
      preparedStatement.setInt(2, mortalidad.getGranja().getId());
      preparedStatement.setInt(3, mortalidad.getNumeroGalpon());
      preparedStatement.setInt(4, mortalidad.getCantidad());
      preparedStatement.setString(5, mortalidad.getSexo());
      preparedStatement.setString(6, mortalidad.getLote());
      preparedStatement.setString(7, mortalidad.getObservaciones());
      preparedStatement.executeUpdate();

      logger.info("Control de mortalidad guardado exitosamente");
    } catch (Exception e) {
      logger.error("Error al guardar el control de mortalidad: {}", e.getMessage(), e);
      System.out.println("Error al guardar el control de mortalidad: " + e.getMessage());
    }
  }

  public static void eliminarMortalidad(int id) {
    logger.info("Eliminando control de mortalidad con ID: {}", id);

    String sql = "DELETE FROM ControlMortalidad WHERE id = ?";
    try (var connection = ObtenerConexion();
        var preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setInt(1, id);
      preparedStatement.executeUpdate();

      logger.info("Control de mortalidad eliminado exitosamente");
    } catch (Exception e) {
      logger.error("Error al eliminar el control de mortalidad: {}", e.getMessage(), e);
      System.out.println("Error al eliminar el control de mortalidad: " + e.getMessage());
    }
  }

  public static void editarMortalidad(ControlMortalidad mortalidad) {
    logger.info("Editando control de mortalidad ID: {} - Granja: {}, Galpón: {}",
        mortalidad.getId(), mortalidad.getGranja().getNombre(), mortalidad.getNumeroGalpon());

    String sql = "UPDATE ControlMortalidad SET fecha = ?, id_granja = ?, numero_galpon = ?, cantidad = ?, sexo = ?, lote = ?, observaciones = ? WHERE id = ?";
    try (var connection = ObtenerConexion();
        var preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setDate(1, new Date(mortalidad.getFecha().getTime()));
      preparedStatement.setInt(2, mortalidad.getGranja().getId());
      preparedStatement.setInt(3, mortalidad.getNumeroGalpon());
      preparedStatement.setInt(4, mortalidad.getCantidad());
      preparedStatement.setString(5, mortalidad.getSexo());
      preparedStatement.setString(6, mortalidad.getLote());
      preparedStatement.setString(7, mortalidad.getObservaciones());
      preparedStatement.setInt(8, mortalidad.getId());
      preparedStatement.executeUpdate();

      logger.info("Control de mortalidad editado exitosamente");
    } catch (Exception e) {
      logger.error("Error al editar el control de mortalidad: {}", e.getMessage(), e);
      System.out.println("Error al editar el control de mortalidad: " + e.getMessage());
    }
  }

  public static ControlMortalidad buscarMortalidad(int id) {
    logger.info("Buscando control de mortalidad con ID: {}", id);

    String sql = "SELECT cm.*, g.nombre as granja_nombre, g.codigo as granja_codigo " +
        "FROM ControlMortalidad cm " +
        "INNER JOIN Granja g ON cm.id_granja = g.id " +
        "WHERE cm.id = ?";

    try (var connection = ObtenerConexion();
        var preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setInt(1, id);
      var resultSet = preparedStatement.executeQuery();

      if (resultSet.next()) {
        ControlMortalidad mortalidad = new ControlMortalidad();
        mortalidad.setId(resultSet.getInt("id"));
        mortalidad.setFecha(resultSet.getDate("fecha"));
        mortalidad.setNumeroGalpon(resultSet.getInt("numero_galpon"));
        mortalidad.setCantidad(resultSet.getInt("cantidad"));
        mortalidad.setSexo(resultSet.getString("sexo"));
        mortalidad.setLote(resultSet.getString("lote"));
        mortalidad.setObservaciones(resultSet.getString("observaciones"));

        // Crear objeto Granja
        Granja granja = new Granja();
        granja.setId(resultSet.getInt("id_granja"));
        granja.setNombre(resultSet.getString("granja_nombre"));
        granja.setCodigo(resultSet.getString("granja_codigo"));
        mortalidad.setGranja(granja);

        logger.info("Control de mortalidad encontrado exitosamente");
        return mortalidad;
      }
    } catch (Exception e) {
      logger.error("Error al buscar el control de mortalidad: {}", e.getMessage(), e);
      System.out.println("Error al buscar el control de mortalidad: " + e.getMessage());
    }

    logger.warn("No se encontró control de mortalidad con ID: {}", id);
    return null;
  }

  public static DefaultTableModel listarMortalidades() {
    logger.info("Listando todos los controles de mortalidad");

    DefaultTableModel modelo = new DefaultTableModel();
    modelo.addColumn("ID");
    modelo.addColumn("Fecha");
    modelo.addColumn("Granja");
    modelo.addColumn("Galpón");
    modelo.addColumn("Cantidad");
    modelo.addColumn("Sexo");
    modelo.addColumn("Lote");
    modelo.addColumn("Observaciones");

    String sql = "SELECT cm.*, g.nombre as granja_nombre " +
        "FROM ControlMortalidad cm " +
        "INNER JOIN Granja g ON cm.id_granja = g.id " +
        "ORDER BY cm.fecha DESC";

    try (var connection = ObtenerConexion();
        var preparedStatement = connection.prepareStatement(sql);
        var resultSet = preparedStatement.executeQuery()) {

      int count = 0;
      while (resultSet.next()) {
        modelo.addRow(new Object[] {
            resultSet.getInt("id"),
            resultSet.getDate("fecha"),
            resultSet.getString("granja_nombre"),
            resultSet.getInt("numero_galpon"),
            resultSet.getInt("cantidad"),
            resultSet.getString("sexo"),
            resultSet.getString("lote"),
            resultSet.getString("observaciones")
        });
        count++;
      }

      logger.info("Se listaron {} controles de mortalidad exitosamente", count);
    } catch (Exception e) {
      logger.error("Error al listar los controles de mortalidad: {}", e.getMessage(), e);
      System.out.println("Error al listar los controles de mortalidad: " + e.getMessage());
    }

    return modelo;
  }

  /**
   * Obtiene todas las mortalidades con información completa de granja
   * Para uso en reportes PDF
   */
  public static List<ControlMortalidad> obtenerTodasLasMortalidades() {
    logger.info("Obteniendo todas las mortalidades para reporte");

    List<ControlMortalidad> mortalidades = new ArrayList<>();
    String sql = "SELECT cm.*, g.nombre as granja_nombre, g.codigo as granja_codigo " +
        "FROM ControlMortalidad cm " +
        "INNER JOIN Granja g ON cm.id_granja = g.id " +
        "ORDER BY cm.fecha DESC, g.nombre";

    try (var connection = ObtenerConexion();
        var preparedStatement = connection.prepareStatement(sql);
        var resultSet = preparedStatement.executeQuery()) {

      while (resultSet.next()) {
        ControlMortalidad mortalidad = new ControlMortalidad();
        mortalidad.setId(resultSet.getInt("id"));
        mortalidad.setFecha(resultSet.getDate("fecha"));
        mortalidad.setNumeroGalpon(resultSet.getInt("numero_galpon"));
        mortalidad.setCantidad(resultSet.getInt("cantidad"));
        mortalidad.setSexo(resultSet.getString("sexo"));
        mortalidad.setLote(resultSet.getString("lote"));
        mortalidad.setObservaciones(resultSet.getString("observaciones"));

        // Crear objeto Granja completo
        Granja granja = new Granja();
        granja.setId(resultSet.getInt("id_granja"));
        granja.setNombre(resultSet.getString("granja_nombre"));
        granja.setCodigo(resultSet.getString("granja_codigo"));
        mortalidad.setGranja(granja);

        mortalidades.add(mortalidad);
      }

      logger.info("Se obtuvieron {} mortalidades para reporte exitosamente", mortalidades.size());
    } catch (Exception e) {
      logger.error("Error al obtener las mortalidades para reporte: {}", e.getMessage(), e);
      System.out.println("Error al obtener las mortalidades para reporte: " + e.getMessage());
    }

    return mortalidades;
  }
}
