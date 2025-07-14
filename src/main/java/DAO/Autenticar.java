package DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase para autenticar usuarios con contraseñas encriptadas.
 *
 * @author franc
 */
public class Autenticar {
  private static final Logger logger = LoggerFactory.getLogger(Autenticar.class);

  /*
   * CREATE TABLE usuario (
   * id INT AUTO_INCREMENT PRIMARY KEY,
   * email VARCHAR(100) NOT NULL UNIQUE,
   * password VARCHAR(255) NOT NULL
   * );
   */

  /**
   * Autentica un usuario verificando email y contraseña encriptada.
   * 
   * @param email    Email del usuario
   * @param password Contraseña en texto plano
   * @return true si las credenciales son válidas, false en caso contrario
   */
  public static boolean autenticar(String email, String password) {
    logger.info("Intento de autenticación para email: {}", email);
    boolean resultado = UsuarioDAO.autenticarUsuario(email, password);

    if (resultado) {
      logger.info("Autenticación exitosa para: {}", email);
    } else {
      logger.warn("Autenticación fallida para: {}", email);
    }

    return resultado;
  }
}
