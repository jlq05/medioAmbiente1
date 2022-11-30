package model;

import enums.Rol;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "personas")
public class Persona extends PersistentEntity {
  @Column
  private String nombre;

  @Column
  private String apellido;

  @Enumerated(EnumType.ORDINAL)
  private TipoDocumento tipo;

  @Column
  private int documento;

  @Column
  private String nombreUsuario;

  @Column
  private String contrasenia;

  @Enumerated(EnumType.ORDINAL)
  public Rol rol;

  @Transient
  List<Validacion> validaciones = this.cargarValidacionBase();

  public Persona() {

  }

  public Persona(String nombre,
                 String apellido,
                 TipoDocumento tipo,
                 int documento) {
    this.nombre = nombre;
    this.apellido = apellido;
    this.tipo = tipo;
    this.documento = documento;
  }

  public TipoDocumento getTipo() {
    return tipo;
  }

  public int getDocumento() {
    return documento;
  }

  public  String getNombre() {
    return this.nombre;
  }

  public  String getApellido() {
    return this.apellido;
  }

  private void correrValidaciones(String contrasena) {
    validaciones
            .stream()
            .forEach(validacion -> validacion.validar(contrasena));
  }

  private  List<Validacion> cargarValidacionBase() {
    CriterioFormato formato = new CriterioFormato();
    CriterioTop10k top10k = new CriterioTop10k();
    List<Validacion> validaciones = new ArrayList<>();
    validaciones.add(formato);
    validaciones.add(top10k);
    return validaciones;
  }

  public String getNombreUsuario() {
    return nombreUsuario;
  }

  public void setNombreUsuario(String nombreUsuario) {
    this.nombreUsuario = nombreUsuario;
  }

  public String getContrasenia() {
    return contrasenia;
  }

  public void setContrasenia(String contrasenia) {
    this.contrasenia = contrasenia;
  }

  public Rol getRol() {
    return rol;
  }

  public void setRol(Rol rol) {
    this.rol = rol;
  }

}