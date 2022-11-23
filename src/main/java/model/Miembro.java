package model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "miembros")
public class Miembro extends PersistentEntity {

  @Column
  private String nombre;

  @Column
  private String apellido;

  @Enumerated(EnumType.ORDINAL)
  private TipoDocumento tipo;

  @Column
  private int documento;

  @ManyToMany(cascade = {CascadeType.ALL})
  private List<Trayecto> trayectos;

  @Column
  private String nombreUsuario;

  @Column
  private String contrasenia;

  @Transient
  List<Validacion> validaciones = this.cargarValidacionBase();

  public Miembro() {

  }

  public Miembro(String nombre,
                 String apellido,
                 TipoDocumento tipo,
                 int documento,
                 List<Trayecto> trayectos) {
    this.nombre = nombre;
    this.apellido = apellido;
    this.tipo = tipo;
    this.documento = documento;
    this.trayectos = trayectos;
  }

  public TipoDocumento getTipo() {
    return tipo;
  }

  public int getDocumento() {
    return documento;
  }

  public List<Trayecto> getTrayectos() {
    return trayectos;
  }

  public  String getNombre() {
    return this.nombre;
  }

  public  String getApellido() {
    return this.apellido;
  }
  
  public void agregarTrayecto(Trayecto unTrayecto) {
    this.trayectos.add(unTrayecto);
  }
    
  public float obtenerCalculoHC() {
    float valorInicial = 0;
    return this.trayectos.stream()
        .map(trayecto -> trayecto.obtenerCalculoHC())
        .reduce(valorInicial, (hc, otroHC) -> hc + otroHC);
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

}