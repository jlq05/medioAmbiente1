package model;

import java.time.YearMonth;
import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import utils.YearMonthDateAttributeConverter;

@Entity
@Table(name = "datosActividades")
public class DatosActividad extends PersistentEntity {
  @ManyToOne(cascade = {CascadeType.ALL})
  private TipoConsumo tipoConsumo;

  private float valor;
  @Enumerated(EnumType.ORDINAL)
  private TipoPeriodicidad tipoPeriodicidad;

  @Convert(converter = YearMonthDateAttributeConverter.class)
  private YearMonth periodicidadDeImputacion;

  private DatosActividad() {
  }

  public DatosActividad(
      TipoConsumo tipoConsumo, float valor, TipoPeriodicidad tipoPeriodicidad,
      YearMonth periodicidadDeImputacion) {
    this.tipoConsumo = tipoConsumo;
    this.valor = valor;
    this.tipoPeriodicidad = tipoPeriodicidad;
    this.periodicidadDeImputacion = periodicidadDeImputacion;
  }

  public TipoConsumo getTipoConsumo() {
    return tipoConsumo;
  }

  public float getValor() {
    return valor;
  }

  public TipoPeriodicidad getPeriodicidad() {
    return tipoPeriodicidad;
  }

  public float obtenerCalculoHC() {
    return this.valor * this.tipoConsumo.getFactorEmision();
  }

  public boolean estaEnElIntervalo(Intervalo intervalo) {
    YearMonth fechaDesde = intervalo.getFechaDesde();
    YearMonth fechaHasta = intervalo.getFechaHasta();
    return
      periodicidadDeImputacion.compareTo(fechaDesde) >= 0
      && periodicidadDeImputacion.compareTo(fechaHasta) <= 0;
  }
}