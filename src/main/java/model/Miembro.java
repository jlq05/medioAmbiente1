package model;

import domain.services.distancia.ServicioDistancia;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "miembros")
public class Miembro extends PersistentEntity {
  @ManyToOne(cascade = {CascadeType.ALL})
  private Persona persona;

  @ManyToMany(cascade = {CascadeType.ALL})
  private List<Trayecto> trayectos;

  public Miembro() {

  }

  public Miembro(Persona persona,
                 List<Trayecto> trayectos) {
    this.persona = persona;
    this.trayectos = trayectos;
  }

  public List<Trayecto> getTrayectos() {
    return trayectos;
  }

  public Persona getPersona() {
    return persona;
  }

  public void setPersona(Persona persona) {
    this.persona = persona;
  }

  public void agregarTrayecto(Trayecto unTrayecto) {
    this.trayectos.add(unTrayecto);
  }
    
  public float obtenerCalculoHC(ServicioDistancia servicioDistancia) {
    float valorInicial = 0;
    return this.trayectos.stream()
        .map(trayecto -> trayecto.obtenerCalculoHC(servicioDistancia))
        .reduce(valorInicial, (hc, otroHC) -> hc + otroHC);
  }
}