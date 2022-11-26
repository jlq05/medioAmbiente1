package view;

import java.time.YearMonth;

public class TrayectoVista {
  public int id;
  public int cantidadTramos;
  public YearMonth periodicidadDeImputacion;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getCantidadTramos() {
    return cantidadTramos;
  }

  public void setCantidadTramos(int cantidadTramos) {
    this.cantidadTramos = cantidadTramos;
  }

  public YearMonth getPeriodicidadDeImputacion() {
    return periodicidadDeImputacion;
  }

  public void setPeriodicidadDeImputacion(YearMonth periodicidadDeImputacion) {
    this.periodicidadDeImputacion = periodicidadDeImputacion;
  }
}
