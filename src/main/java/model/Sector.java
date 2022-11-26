package model;

import domain.services.distancia.ServicioDistancia;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "sectores")
public class Sector extends PersistentEntity {

  private String nombre;

  @ManyToMany
  private List<Miembro> miembros;

  @ManyToMany
  @JoinTable(name = "sectores_postulantes")
  private List<Miembro> postulantes;

  public Sector() {
  }

  public Sector(String nombre, ArrayList<Miembro> miembros, ArrayList<Miembro> postulantes) {
    this.nombre = nombre;
    this.miembros = miembros;
    this.postulantes = postulantes;
  }

  public void agregarMiembro(Miembro miembro) {
    this.miembros.add(miembro);
  }

  public void agregarPostulante(Miembro postulante) {
    this.postulantes.add(postulante);
  }

  public List<Miembro> getMiembros() {
    return miembros;
  }

  public List<Miembro> getPostulantes() {
    return postulantes;
  }

  public float cantidadDeMiembros() {
    return miembros.size();
  }

  public float obtenerIndicador(ServicioDistancia servicioDistancia) {
    return obtenerCalculoHC(servicioDistancia) / cantidadDeMiembros();
  }

  public String getNombre() {
    return nombre;
  }

  public List<Trayecto> obtenerTrayectos() {
    return this.miembros.stream()
        .map(miembro -> miembro.getTrayectos())
        .flatMap(Collection::stream)
        .distinct()
        .collect(Collectors.toList());
  }
    
  public float obtenerCalculoHC(ServicioDistancia servicioDistancia) {
    return this.obtenerTrayectos().stream()
        .map(trayecto -> trayecto.obtenerCalculoHC(servicioDistancia))
        .reduce(0f, (hc, otroHC) -> hc + otroHC);
  }

  public void aceptarVinculacion(Miembro miembro) {
    this.postulantes.remove(miembro);
    this.miembros.add(miembro);
  }

  public void rechazarVinculacion(Miembro miembro) {
    this.postulantes.remove(miembro);
  }

}