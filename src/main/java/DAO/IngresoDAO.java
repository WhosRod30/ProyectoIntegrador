/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Conexion.Conexion;
import Modelo.ControlIngreso;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author franc
 * 
 *         private int id;
 *         private Date fecha;
 *         private int granja;
 *         private int numeroGalpon;
 *         private int cantidad;
 *         private String sexo;
 *         private String lote;
 *         private String observaciones;
 *         CREATE TABLE ControlIngreso (
 *         id INT AUTO_INCREMENT PRIMARY KEY,
 *         fecha DATE,
 *         id_granja INT,
 *         numero_galpon INT,
 *         cantidad INT,
 *         sexo ENUM('M','F'),
 *         lote VARCHAR(50),
 *         observaciones TEXT,
 *         FOREIGN KEY (id_granja) REFERENCES Granja(id)
 *         );
 */
public class IngresoDAO extends Conexion {

  private static final Logger logger = LoggerFactory.getLogger(IngresoDAO.class);

  public static void guardarIngreso(ControlIngreso ingreso) {
    logger.info("Iniciando guardado de ingreso para granja {} galpon {} lote {}",
        ingreso.getGranja(), ingreso.getNumeroGalpon(), ingreso.getLote());
    String sql = "INSERT INTO ControlIngreso (fecha, id_granja, numero_galpon, cantidad, sexo, lote, observaciones) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (var connection = ObtenerConexion();
        var preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setDate(1, new java.sql.Date(ingreso.getFecha().getTime()));
      preparedStatement.setInt(2, ingreso.getGranja());
      preparedStatement.setInt(3, ingreso.getNumeroGalpon());
      preparedStatement.setInt(4, ingreso.getCantidad());
      preparedStatement.setString(5, ingreso.getSexo());
      preparedStatement.setString(6, ingreso.getLote());
      preparedStatement.setString(7, ingreso.getObservaciones());
      preparedStatement.executeUpdate();
      logger.info("Ingreso guardado exitosamente. Granja: {}, Galpon: {}, Cantidad: {}",
          ingreso.getGranja(), ingreso.getNumeroGalpon(), ingreso.getCantidad());
    } catch (Exception e) {
      logger.error("Error al guardar el ingreso: {}", e.getMessage(), e);
      System.out.println("Error al guardar el ingreso: " + e.getMessage());
    }
  }

  public static void eliminarIngreso(int id) {
    logger.info("Iniciando eliminacion de ingreso con ID: {}", id);
    String sql = "DELETE FROM ControlIngreso WHERE id = ?";
    try (var connection = ObtenerConexion();
        var preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setInt(1, id);
      preparedStatement.executeUpdate();
      logger.info("Ingreso eliminado exitosamente. ID: {}", id);
    } catch (Exception e) {
      logger.error("Error al eliminar el ingreso con ID {}: {}", id, e.getMessage(), e);
      System.out.println("Error al eliminar el ingreso: " + e.getMessage());
    }
  }

  public static void editarIngreso(ControlIngreso ingreso) {
    logger.info("Iniciando edicion de ingreso. ID: {}", ingreso.getId());
    String sql = "UPDATE ControlIngreso SET fecha = ?, id_granja = ?, numero_galpon = ?, cantidad = ?, sexo = ?, lote = ?, observaciones = ? WHERE id = ?";
    try (var connection = ObtenerConexion();
        var preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setDate(1, new java.sql.Date(ingreso.getFecha().getTime()));
      preparedStatement.setInt(2, ingreso.getGranja());
      preparedStatement.setInt(3, ingreso.getNumeroGalpon());
      preparedStatement.setInt(4, ingreso.getCantidad());
      preparedStatement.setString(5, ingreso.getSexo());
      preparedStatement.setString(6, ingreso.getLote());
      preparedStatement.setString(7, ingreso.getObservaciones());
      preparedStatement.setInt(8, ingreso.getId());
      preparedStatement.executeUpdate();
      logger.info("Ingreso editado exitosamente. ID: {}", ingreso.getId());
    } catch (Exception e) {
      logger.error("Error al editar el ingreso con ID {}: {}", ingreso.getId(), e.getMessage(), e);
      System.out.println("Error al editar el ingreso: " + e.getMessage());
    }
  }

  public static ControlIngreso buscarIngreso(int id) {
    logger.info("Buscando ingreso con ID: {}", id);
    String sql = "SELECT * FROM ControlIngreso WHERE id = ?";
    try (var connection = ObtenerConexion();
        var preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setInt(1, id);
      var resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        ControlIngreso ingreso = new ControlIngreso();
        ingreso.setId(resultSet.getInt("id"));
        ingreso.setFecha(resultSet.getDate("fecha"));
        ingreso.setGranja(resultSet.getInt("id_granja"));
        ingreso.setNumeroGalpon(resultSet.getInt("numero_galpon"));
        ingreso.setCantidad(resultSet.getInt("cantidad"));
        ingreso.setSexo(resultSet.getString("sexo"));
        ingreso.setLote(resultSet.getString("lote"));
        ingreso.setObservaciones(resultSet.getString("observaciones"));
        logger.info("Ingreso encontrado. ID: {}", id);
        return ingreso;
      }
      logger.warn("No se encontro ingreso con ID: {}", id);
    } catch (Exception e) {
      logger.error("Error al buscar el ingreso con ID {}: {}", id, e.getMessage(), e);
      System.out.println("Error al buscar el ingreso: " + e.getMessage());
    }
    return null;
  }

  public static DefaultTableModel listarIngresos() {
    logger.info("Listando todos los ingresos");
    String[] columnas = { "ID", "Fecha", "Granja", "Número Galpón", "Cantidad", "Sexo", "Lote", "Observaciones" };
    String sql = "SELECT * FROM ControlIngreso";
    DefaultTableModel modelo = new DefaultTableModel(null, columnas);

    try (var connection = ObtenerConexion();
        var statement = connection.createStatement();
        var resultSet = statement.executeQuery(sql)) {
      int count = 0;
      while (resultSet.next()) {
        Object[] fila = new Object[columnas.length];
        for (int i = 0; i < columnas.length; i++) {
          fila[i] = resultSet.getObject(i + 1);
        }
        modelo.addRow(fila);
        count++;
      }
      logger.info("Se cargaron {} registros de ingreso", count);
    } catch (Exception e) {
      logger.error("Error al listar los ingresos: {}", e.getMessage(), e);
      System.out.println("Error al listar los ingresos: " + e.getMessage());
    }
    return modelo;
  }
}
