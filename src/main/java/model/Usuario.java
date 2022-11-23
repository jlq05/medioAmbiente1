package model;

import enums.Rol;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  //@GeneratedValue(generator = "UUID")
  //@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDDGenerator")
  private long id;

  public String nombreUsuario;
  public String contrasena;
  public String nombre;
  public String apellido;
  public Rol rol;

  private Usuario() {
  }

  public Usuario(String usuario, String contrasenia, String nombre, String apellido, Rol rol) {

    this.contrasena = contrasenia;
    this.nombreUsuario = usuario;
    this.nombre = nombre;
    this.apellido = apellido;
    this.rol = rol;
  }

  public String getNombreUsuario() {
    return this.nombreUsuario;
  }

  public String getContrasena() {
    return this.contrasena;
  }

  public String getNombre() {
    return this.nombre;
  }

  public String getApellido() {
    return this.apellido;
  }

  public long getId() {
    return id;
  }

  public Rol getRol() {
    return rol;
  }
}
