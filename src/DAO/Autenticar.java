/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.sql.ResultSet;
import java.sql.SQLException;

import Conexion.Conexion;

/**
 *
 * @author franc
 */
public class Autenticar extends Conexion {
  /*
   * CREATE TABLE usuario (
   * id INT AUTO_INCREMENT PRIMARY KEY,
   * email VARCHAR(100) NOT NULL UNIQUE,
   * password VARCHAR(255) NOT NULL
   * );
   */

  public static boolean autenticar(String email, String password) {
    String sql = "SELECT * FROM usuario WHERE email = ? AND password = ?";
    try (var connection = ObtenerConexion();
        var preparedStatement = connection.prepareStatement(sql)) {

      // Configurar los parámetros antes de ejecutar la consulta
      preparedStatement.setString(1, email);
      preparedStatement.setString(2, password);

      // Ejecutar la consulta
      try (var resultSet = preparedStatement.executeQuery()) {
        return resultSet.next(); // Retorna true si se encuentra un registro
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
}
