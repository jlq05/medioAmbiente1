package model;

import domain.services.distancia.entities.Ubicacion;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "paradas")
public class Parada extends PersistentEntity {
  private String nombre;
  private float distanciaProxima;
  @Embedded
  private Ubicacion ubicacion;

  private Parada() {
  }

  public Parada(String nombre, float distanciaProxima, Ubicacion ubicacion) {
    this.nombre = nombre;
    this.distanciaProxima = distanciaProxima;
    this.ubicacion = ubicacion;
  }

  public String getNombre() {
    return nombre;
  }

  public float getDistanciaProxima() {
    return distanciaProxima;
  }

  public Ubicacion getUbicacion() {
    return ubicacion;
  }
}