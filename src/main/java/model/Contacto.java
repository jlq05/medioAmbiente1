package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "contactos")
public class Contacto extends PersistentEntity {
  @Column
  String nombre;

  @Column
  String telefono;

  @Column
  String mail;

  public Contacto() {
  }

  public Contacto(String nombre, String telefono, String mail) {
    this.nombre = nombre;
    this.telefono = telefono;
    this.mail = mail;
  }

  public String getNombre() {
    return nombre;
  }

  public String getTelefono() {
    return telefono;
  }

  public String getMail() {
    return mail;
  }
}
