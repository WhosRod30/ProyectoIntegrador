/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Conexion.Conexion;
import Modelo.ControlConsumo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.DefaultTableModel;

public class ConsumoDao extends Conexion {

  private static final Logger logger = LoggerFactory.getLogger(ConsumoDao.class);

  public void guardarConsumo(ControlConsumo consumo) {
    logger.info("Iniciando guardado de consumo para granja {} galpon {}",
        consumo.getGranja(), consumo.getNumeroGalpon());
    String sql = "INSERT INTO ControlConsumo (fecha, id_granja, numero_galpon, tipo_alimento, cantidad, observaciones) VALUES (?, ?, ?, ?, ?, ?)";
    try (var connection = ObtenerConexion();
        var preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, consumo.getFecha().toString());
      preparedStatement.setInt(2, consumo.getGranja());
      preparedStatement.setInt(3, consumo.getNumeroGalpon());
      preparedStatement.setString(4, consumo.getTipoAlimento());
      preparedStatement.setDouble(5, consumo.getCantidad());
      preparedStatement.setString(6, consumo.getObservaciones());
      preparedStatement.executeUpdate();
      logger.info("Consumo guardado exitosamente para granja {} galpon {}",
          consumo.getGranja(), consumo.getNumeroGalpon());
    } catch (Exception e) {
      logger.error("Error al guardar el consumo: {}", e.getMessage(), e);
      System.out.println("Error al guardar el consumo: " + e.getMessage());
    }
  }

  public void eliminarConsumo(int id) {
    logger.info("Iniciando eliminacion de consumo con ID: {}", id);
    String sql = "DELETE FROM ControlConsumo WHERE id = ?";
    try (var connection = ObtenerConexion();
        var preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setInt(1, id);
      preparedStatement.executeUpdate();
      logger.info("Consumo eliminado exitosamente. ID: {}", id);
    } catch (Exception e) {
      logger.error("Error al eliminar el consumo con ID {}: {}", id, e.getMessage(), e);
      System.out.println("Error al eliminar el consumo: " + e.getMessage());
    }
  }

  public void editarConsumo(ControlConsumo consumo) {
    logger.info("Iniciando edicion de consumo. ID: {}", consumo.getId());
    String sql = "UPDATE ControlConsumo SET fecha = ?, id_granja = ?, numero_galpon = ?, tipo_alimento = ?, cantidad = ?, observaciones = ? WHERE id = ?";
    try (var connection = ObtenerConexion();
        var preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, consumo.getFecha().toString());
      preparedStatement.setInt(2, consumo.getGranja());
      preparedStatement.setInt(3, consumo.getNumeroGalpon());
      preparedStatement.setString(4, consumo.getTipoAlimento());
      preparedStatement.setDouble(5, consumo.getCantidad());
      preparedStatement.setString(6, consumo.getObservaciones());
      preparedStatement.setInt(7, consumo.getId());
      preparedStatement.executeUpdate();
      logger.info("Consumo editado exitosamente. ID: {}", consumo.getId());
    } catch (Exception e) {
      logger.error("Error al editar el consumo con ID {}: {}", consumo.getId(), e.getMessage(), e);
      System.out.println("Error al editar el consumo: " + e.getMessage());
    }
  }

  public ControlConsumo buscarConsumo(int id) {
    logger.info("Buscando consumo con ID: {}", id);
    String sql = "SELECT * FROM ControlConsumo WHERE id = ?";
    try (var connection = ObtenerConexion();
        var preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setInt(1, id);
      var resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        // Procesar los resultados
        ControlConsumo consumo = new ControlConsumo();
        consumo.setId(resultSet.getInt("id"));
        consumo.setFecha(resultSet.getDate("fecha"));
        consumo.setGranja(resultSet.getInt("id_granja"));
        consumo.setNumeroGalpon(resultSet.getInt("numero_galpon"));
        consumo.setTipoAlimento(resultSet.getString("tipo_alimento"));
        consumo.setCantidad(resultSet.getDouble("cantidad"));
        consumo.setObservaciones(resultSet.getString("observaciones"));
        logger.info("Consumo encontrado. ID: {}", id);
        return consumo; // Retornar el objeto ControlConsumo
      }
      logger.warn("No se encontro consumo con ID: {}", id);
    } catch (Exception e) {
      logger.error("Error al buscar el consumo con ID {}: {}", id, e.getMessage(), e);
      System.out.println("Error al buscar el consumo: " + e.getMessage());
    }
    return null; // Retornar null si no se encuentra el consumo
  }

  public static DefaultTableModel listarConsumos() {
    Logger logger = LoggerFactory.getLogger(ConsumoDao.class);
    logger.info("Listando todos los consumos");
    String[] columnas = { "ID", "Fecha", "ID Granja", "Número Galpón", "Tipo Alimento", "Cantidad", "Observaciones" };
    DefaultTableModel modelo = new DefaultTableModel(null, columnas);

    String sql = "SELECT * FROM ControlConsumo";
    try (var connection = ObtenerConexion();
        var preparedStatement = connection.prepareStatement(sql);
        var resultSet = preparedStatement.executeQuery()) {

      int count = 0;
      while (resultSet.next()) {
        // Crear una fila con los datos obtenidos de la base de datos
        Object[] fila = {
            resultSet.getInt("id"),
            resultSet.getString("fecha"),
            resultSet.getInt("id_granja"),
            resultSet.getInt("numero_galpon"),
            resultSet.getString("tipo_alimento"),
            resultSet.getDouble("cantidad"),
            resultSet.getString("observaciones")
        };
        modelo.addRow(fila); // Agregar la fila al modelo
        count++;
      }
      logger.info("Se cargaron {} registros de consumo", count);
    } catch (Exception e) {
      logger.error("Error al listar los consumos: {}", e.getMessage(), e);
      System.out.println("Error al listar los consumos: " + e.getMessage());
    }

    return modelo; // Retornar el modelo para el JTable
  }

}
