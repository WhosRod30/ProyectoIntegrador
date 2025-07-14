package Util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utilidad para el manejo seguro de contraseñas.
 * Usa BCrypt para el hash de contraseñas.
 * 
 * @author yo
 */
public class PasswordUtil {

  private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  /**
   * Encripta una contraseña usando BCrypt.
   * 
   * @param plainPassword La contraseña en texto plano
   * @return La contraseña encriptada
   */
  public static String encryptPassword(String plainPassword) {
    return encoder.encode(plainPassword);
  }

  /**
   * Verifica si una contraseña en texto plano coincide con el hash.
   * 
   * @param plainPassword  La contraseña en texto plano
   * @param hashedPassword La contraseña encriptada
   * @return true si coinciden, false en caso contrario
   */
  public static boolean verifyPassword(String plainPassword, String hashedPassword) {
    return encoder.matches(plainPassword, hashedPassword);
  }
}
