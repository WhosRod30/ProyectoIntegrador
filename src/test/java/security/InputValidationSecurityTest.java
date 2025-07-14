package security;

import Util.PasswordUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de seguridad para validación de datos de entrada.
 * Verifica que el sistema maneja correctamente entradas maliciosas.
 * 
 * @author Sistema Avícola - Security Team
 */
public class InputValidationSecurityTest {

  @Test
  @DisplayName("Prueba de robustez del sistema de hash BCrypt")
  void testBCryptSecurityFeatures() {
    String testPassword = "MySecurePassword123!";

    // Verificar que BCrypt genera salts aleatorios
    String hash1 = PasswordUtil.encryptPassword(testPassword);
    String hash2 = PasswordUtil.encryptPassword(testPassword);

    assertNotEquals(hash1, hash2,
        "BCrypt debería generar hashes diferentes para la misma contraseña");

    // Verificar longitud mínima del hash BCrypt (debe ser 60 caracteres)
    assertTrue(hash1.length() == 60,
        "El hash BCrypt debería tener exactamente 60 caracteres");
    assertTrue(hash1.startsWith("$2"),
        "El hash BCrypt debería comenzar con $2");

    // Verificar que ambos hashes validan correctamente
    assertTrue(PasswordUtil.verifyPassword(testPassword, hash1));
    assertTrue(PasswordUtil.verifyPassword(testPassword, hash2));
  }

  @Test
  @DisplayName("Prueba de resistencia a ataques de timing")
  void testTimingAttackResistance() {
    String validPassword = "ValidPassword123!";
    String invalidPassword = "InvalidPassword456!";

    // Generar hash para la contraseña válida
    String validHash = PasswordUtil.encryptPassword(validPassword);

    // Medir tiempo de verificación para contraseña válida
    long startTime = System.nanoTime();
    PasswordUtil.verifyPassword(validPassword, validHash);
    long validTime = System.nanoTime() - startTime;

    // Medir tiempo de verificación para contraseña inválida
    startTime = System.nanoTime();
    PasswordUtil.verifyPassword(invalidPassword, validHash);
    long invalidTime = System.nanoTime() - startTime;

    // BCrypt debería tomar tiempo similar para ambos casos
    // Permitimos una diferencia máxima del 500% (BCrypt es naturalmente variable)
    double ratio = (double) Math.max(validTime, invalidTime) / Math.min(validTime, invalidTime);
    assertTrue(ratio < 5.0,
        "Diferencia de tiempo excesiva entre validación correcta e incorrecta: " + ratio);
  }

  @Test
  @DisplayName("Prueba de manejo de hashes malformados")
  void testMalformedHashHandling() {
    String[] malformedHashes = {
        "", // Hash vacío
        "plaintext", // Texto plano
        "$2a$10$invalid", // Hash BCrypt incompleto
        "$1$invalid$hash", // Formato MD5 (no BCrypt)
        "notahash", // Texto aleatorio
        null, // Hash nulo
        "$2a$10$" + "a".repeat(100) // Hash excesivamente largo
    };

    String testPassword = "TestPassword123!";

    for (String malformedHash : malformedHashes) {
      assertDoesNotThrow(() -> {
        boolean result = PasswordUtil.verifyPassword(testPassword, malformedHash);
        assertFalse(result,
            "Hash malformado fue aceptado como válido: " + malformedHash);
      }, "Excepción con hash malformado: " + malformedHash);
    }
  }

  @Test
  @DisplayName("Prueba de contraseñas extremadamente largas")
  void testExtremelyLongPasswords() {
    // BCrypt tiene un límite de 72 bytes para contraseñas
    String extremeLongPassword = "A".repeat(100);

    assertThrows(IllegalArgumentException.class, () -> {
      PasswordUtil.encryptPassword(extremeLongPassword);
    }, "BCrypt debe rechazar contraseñas que excedan 72 bytes");

    // Probar con una contraseña de 72 bytes (límite de BCrypt)
    String maxLengthPassword = "A".repeat(72);
    assertDoesNotThrow(() -> {
      String hash = PasswordUtil.encryptPassword(maxLengthPassword);
      assertTrue(PasswordUtil.verifyPassword(maxLengthPassword, hash),
          "Contraseña de longitud máxima debe funcionar");
    }, "Contraseña de 72 bytes debe ser válida");
  }

  @Test
  @DisplayName("Prueba de caracteres Unicode en contraseñas")
  void testUnicodePasswordHandling() {
    String[] unicodePasswords = {
        "Contraseña123!", // Español con ñ
        "пароль123", // Cirílico
        "密码123", // Chino
        "🔒🔑Password123", // Emojis
        "Café_Niño_Año", // Acentos españoles
        "𝕯𝖆𝖙𝖆𝖇𝖆𝖘𝖊", // Caracteres matemáticos Unicode
    };

    for (String unicodePassword : unicodePasswords) {
      assertDoesNotThrow(() -> {
        String hash = PasswordUtil.encryptPassword(unicodePassword);
        assertTrue(PasswordUtil.verifyPassword(unicodePassword, hash),
            "Contraseña Unicode no se verificó: " + unicodePassword);
      }, "Fallo al procesar contraseña Unicode: " + unicodePassword);
    }
  }

  @Test
  @DisplayName("Prueba de resistencia a ataques de diccionario")
  void testDictionaryAttackResistance() {
    // Lista de contraseñas comunes que deberían ser rechazadas por políticas
    String[] commonPasswords = {
        "password", "123456", "password123", "admin", "qwerty",
        "letmein", "welcome", "monkey", "1234567890", "abc123",
        "password1", "iloveyou", "princess", "rockyou", "12345678"
    };

    // Generar hash de una contraseña fuerte
    String strongPassword = "MyV3ryStr0ng!P@ssw0rd#2024";
    String strongHash = PasswordUtil.encryptPassword(strongPassword);

    // Verificar que las contraseñas comunes no funcionan contra el hash fuerte
    for (String weakPassword : commonPasswords) {
      boolean result = PasswordUtil.verifyPassword(weakPassword, strongHash);
      assertFalse(result,
          "Contraseña débil aceptada contra hash fuerte: " + weakPassword);
    }
  }

  @Test
  @DisplayName("Prueba de manejo de caracteres de control")
  void testControlCharacterHandling() {
    String basePassword = "TestPassword123!";

    // Probar con algunos caracteres de control específicos
    char[] controlChars = { 0, 1, 8, 9, 10, 13, 31 };

    for (char controlChar : controlChars) {
      String passwordWithControl = basePassword + controlChar;
      final char currentChar = controlChar; // Para usar en lambda

      assertDoesNotThrow(() -> {
        String hash = PasswordUtil.encryptPassword(passwordWithControl);
        assertTrue(PasswordUtil.verifyPassword(passwordWithControl, hash),
            "Contraseña con carácter de control falló: código " + (int) currentChar);
      }, "Excepción con carácter de control: código " + (int) currentChar);
    }
  }

  @Test
  @DisplayName("Prueba de consistencia de hash en múltiples verificaciones")
  void testHashConsistency() {
    String password = "ConsistencyTest123!";
    String hash = PasswordUtil.encryptPassword(password);

    // Verificar múltiples veces que el resultado es consistente
    for (int i = 0; i < 100; i++) {
      assertTrue(PasswordUtil.verifyPassword(password, hash),
          "Hash inconsistente en verificación #" + i);
      assertFalse(PasswordUtil.verifyPassword(password + "wrong", hash),
          "Hash aceptó contraseña incorrecta en verificación #" + i);
    }
  }

  @Test
  @DisplayName("Prueba de carga de trabajo de BCrypt")
  void testBCryptWorkload() {
    String password = "WorkloadTest123!";

    // Medir tiempo de hash (BCrypt debería ser costoso computacionalmente)
    long startTime = System.currentTimeMillis();
    String hash = PasswordUtil.encryptPassword(password);
    long hashTime = System.currentTimeMillis() - startTime;

    // BCrypt debería tomar al menos algunos milisegundos
    assertTrue(hashTime > 0, "El hash debería tomar tiempo medible");

    // Verificar que la verificación también toma tiempo
    startTime = System.currentTimeMillis();
    PasswordUtil.verifyPassword(password, hash);
    long verifyTime = System.currentTimeMillis() - startTime;

    assertTrue(verifyTime > 0, "La verificación debería tomar tiempo medible");
  }
}
