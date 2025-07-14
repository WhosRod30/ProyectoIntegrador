/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 * Modelo para la entidad Usuario
 * 
 * CREATE TABLE usuario
 * (
 * id INT AUTO_INCREMENT PRIMARY KEY,
 * email VARCHAR(100) NOT NULL UNIQUE,
 * password VARCHAR(255) NOT NULL
 * );
 * 
 * @author franc
 */
public class Usuario {
  private int id;
  private String email;
  private String password;

  public Usuario() {
  }

  public Usuario(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return email; // Para mostrar en ComboBox si es necesario
  }
}
