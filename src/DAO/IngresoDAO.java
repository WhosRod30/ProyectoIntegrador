/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import javax.swing.table.DefaultTableModel;

import Conexion.Conexion;
import Modelo.ControlIngreso;

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

  public static void guardarIngreso(ControlIngreso ingreso) {
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
    } catch (Exception e) {
      System.out.println("Error al guardar el ingreso: " + e.getMessage());
    }
  }

  public static void eliminarIngreso(int id) {
    String sql = "DELETE FROM ControlIngreso WHERE id = ?";
    try (var connection = ObtenerConexion();
        var preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setInt(1, id);
      preparedStatement.executeUpdate();
    } catch (Exception e) {
      System.out.println("Error al eliminar el ingreso: " + e.getMessage());
    }
  }

  public static void editarIngreso(ControlIngreso ingreso) {
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
    } catch (Exception e) {
      System.out.println("Error al editar el ingreso: " + e.getMessage());
    }
  }

  public static ControlIngreso buscarIngreso(int id) {
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
        return ingreso;
      }
    } catch (Exception e) {
      System.out.println("Error al buscar el ingreso: " + e.getMessage());
    }
    return null;
  }

  public static DefaultTableModel listarIngresos() {
    String[] columnas = { "ID", "Fecha", "Granja", "Número Galpón", "Cantidad", "Sexo", "Lote", "Observaciones" };
    String sql = "SELECT * FROM ControlIngreso";
    DefaultTableModel modelo = new DefaultTableModel(null, columnas);

    try (var connection = ObtenerConexion();
        var statement = connection.createStatement();
        var resultSet = statement.executeQuery(sql)) {
      while (resultSet.next()) {
        Object[] fila = new Object[columnas.length];
        for (int i = 0; i < columnas.length; i++) {
          fila[i] = resultSet.getObject(i + 1);
        }
        modelo.addRow(fila);
      }
    } catch (Exception e) {
      System.out.println("Error al listar los ingresos: " + e.getMessage());
    }
    return modelo;
  }
}
