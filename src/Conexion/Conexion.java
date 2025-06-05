/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author franc
 */
public class Conexion {

    static Connection conectar = null;
    static String usuario = "root";
    static String pass = "";
    static String puerto = "3307";
    static String db = "BDintegrador";
    static String ip = "localhost";
    static String cadena = "jdbc:mysql://" + ip + ":" + puerto + "/" + db;

    // conexion
    public static Connection ObtenerConexion() {
        try {
            // registrar driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            conectar = DriverManager.getConnection(cadena, usuario, pass);
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error cn: " + e.getMessage());
        }
        return conectar;
    }

    public void CerrarConexion() {
        try {
            conectar.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

}
