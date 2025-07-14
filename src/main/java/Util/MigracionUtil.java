package Util;

import DAO.UsuarioDAO;
import Modelo.Usuario;

/**
 * Utilidad para migrar contraseñas existentes y crear usuarios de prueba.
 * 
 * @author franc
 */
public class MigracionUtil {

  /**
   * Crea un usuario administrador de prueba con contraseña encriptada.
   * Solo lo crea si no existe.
   */
  public static void crearUsuarioAdmin() {
    // Verificar si ya existe un usuario admin
    Usuario existente = UsuarioDAO.buscarUsuarioPorEmail("admin@avicola.com");
    if (existente == null) {
      Usuario admin = new Usuario();
      admin.setEmail("admin@avicola.com");
      admin.setPassword("admin123"); // Esta será encriptada automáticamente por UsuarioDAO

      UsuarioDAO.guardarUsuario(admin);
      System.out.println("Usuario administrador creado:");
      System.out.println("Email: admin@avicola.com");
      System.out.println("Password: admin123");
    } else {
      System.out.println("El usuario administrador ya existe.");
    }
  }

  /**
   * Ejecuta la migración de datos.
   */
  public static void main(String[] args) {
    System.out.println("Iniciando migración de usuarios...");
    crearUsuarioAdmin();
    System.out.println("Migración completada.");
  }
}
