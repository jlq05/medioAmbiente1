package model;

import domain.services.distancia.ServicioDistancia;
import java.io.IOException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import view.TrayectoVista;

@Entity
@Table(name = "trayectos")
public class Trayecto extends PersistentEntity {
  @OneToMany
  @JoinColumn(name = "trayectoId")
  private List<Tramo> tramos;
  private YearMonth periodicidadDeImputacion;

  private Trayecto() {
  }

  public Trayecto(List<Tramo> tramos) {
    this.tramos = tramos;
  }

  public YearMonth getPeriodicidadDeImputacion() {
    return periodicidadDeImputacion;
  }

  public void setPeriodicidadDeImputacion(YearMonth periodicidadDeImputacion) {
    this.periodicidadDeImputacion = periodicidadDeImputacion;
  }

  public List<Tramo> getTramos() {
    return tramos;
  }

  public void agregarTramo(Tramo unTramo) {
    this.tramos.add(unTramo);
  }

  public float obtenerDistancia(ServicioDistancia servicioDistancia) {
    float valorInicial = 0;
    return this.tramos
        .stream()
        .map(tramo -> {
          try {
            return tramo.obtenerDistancia(servicioDistancia);
          } catch (IOException e) {
            e.printStackTrace();
            return valorInicial;
          }
        })
        .reduce(valorInicial, (distancia, otroDistancia) -> distancia + otroDistancia);
  }

  public float obtenerCalculoHC(ServicioDistancia servicioDistancia) {
    float valorInicial = 0;
    return this.tramos.stream().map(tramo -> {
      try {
        return tramo.obtenerCalculoHC(servicioDistancia);
      } catch (IOException e) {
        e.printStackTrace();
      }
      return null;
    })
    .reduce(valorInicial, (hc, otroHC) -> hc + otroHC);
  }

  public boolean esCompartido(ArrayList<Trayecto> trayectos) {
    return trayectos.stream().anyMatch(trayecto -> trayecto.getId().equals(getId()));
  }

  public TrayectoVista convertirATrayectoVista() {
    TrayectoVista trayectoVista = new TrayectoVista();
    trayectoVista.id = this.id.intValue();
    trayectoVista.cantidadTramos = this.tramos.stream().filter(tramo -> {
      return tramo.getMedioDeTransporte() != null;
    }).collect(Collectors.toList()).size();
    trayectoVista.periodicidadDeImputacion = this.periodicidadDeImputacion;
    return trayectoVista;
  }
}
