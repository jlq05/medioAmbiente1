package model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "tiposDeConsumo")
public class TipoConsumo extends PersistentEntity {
  private float factorEmision;
  @Enumerated(EnumType.STRING)
  private TipoUnidad tipoUnidad;
  private String tipoDescripcion;
  private String tipoActividad;
  @Enumerated(EnumType.STRING)
  private TipoAlcance tipoAlcance;

  private TipoConsumo() {
  }

  public TipoConsumo(
      float factorEmision, TipoUnidad tipoUnidad,
      String tipoDescripcion,
      String tipoActividad, TipoAlcance tipoAlcance) {
    this.factorEmision = factorEmision;
    this.tipoUnidad = tipoUnidad;
    this.tipoDescripcion = tipoDescripcion;
    this.tipoActividad = tipoActividad;
    this.tipoAlcance = tipoAlcance;
  }

  public float getFactorEmision() {
    return factorEmision;
  }

  public TipoUnidad getTipoUnidad() {
    return tipoUnidad;
  }

  public String getTipoDescripcion() {
    return tipoDescripcion;
  }

  public String getTipoActividad() {
    return tipoActividad;
  }

  public TipoAlcance getTipoAlcance() {
    return tipoAlcance;
  }

  public void setFactorEmision(float factorEmision) {
    this.factorEmision = factorEmision;
  }
}
