package model;

import domain.services.distancia.ServicioDistancia;
import domain.services.distancia.entities.Ubicacion;
import java.io.IOException;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TransportePublico")
@DiscriminatorValue("TransportePublico")
public class TransportePublico extends MedioDeTransporte {
  @Enumerated(EnumType.ORDINAL)
  @Column(name = "tipo_transporte_publico")
  TipoTransportePublico tipoDeTransportePublico;

  @ManyToOne(cascade = {CascadeType.ALL})
  Linea lineaDeTransporte;

  @ManyToOne
  TipoConsumo tipoConsumo;

  @Column
  float consumoDeCombustiblePorKm;

  @Column
  String nombre;

  private TransportePublico() {
  }

  @Override
  public String getMedioDeTransporte() {
    return "Transporte público";
  }

  public TransportePublico(TipoTransportePublico tipoDeTransportePublico,
                           Linea lineaDeTransporte, TipoConsumo tipoConsumo) {

    this.tipoDeTransportePublico = tipoDeTransportePublico;
    this.lineaDeTransporte = lineaDeTransporte;
    this.tipoConsumo = tipoConsumo;
  }

  public TipoTransportePublico getTipoDeTransportePublico() {
    return tipoDeTransportePublico;
  }

  public void setConsumoDeCombustiblePorKm(float consumoDeCombustiblePorKm) {
    this.consumoDeCombustiblePorKm = consumoDeCombustiblePorKm;
  }

  @Override
  public float obtenerDistancia(
      Ubicacion origen,
      Ubicacion destino,
      ServicioDistancia servicioDistancia
  )  throws IOException {
    return lineaDeTransporte.obtenerDistancia(origen, destino);
  }

  @Override
  public float obtenerConsumoDeCombustiblePorKm() {
    return consumoDeCombustiblePorKm;
  }

  @Override
  public float obtenerFE() {
    return 0;
  }

  public Linea getLineaDeTransporte() {
    return lineaDeTransporte;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public TipoConsumo getTipoConsumo() {
    return tipoConsumo;
  }

  public void setTipoConsumo(TipoConsumo tipoConsumo) {
    this.tipoConsumo = tipoConsumo;
  }
}