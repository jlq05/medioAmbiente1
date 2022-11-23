package model;

import domain.services.distancia.ServicioDistancia;
import domain.services.distancia.entities.Ubicacion;
import java.io.IOException;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "vehiculos")
@DiscriminatorValue("Vehiculo")
public class Vehiculo extends MedioDeTransporte {
  @Enumerated(EnumType.ORDINAL)
  @Column(name = "tipo_vehiculo")
  TipoVehiculo tipoDeVehiculo;

  @ManyToOne
  TipoConsumo tipoConsumo;

  @Transient
  ServicioDistancia servicio;

  float consumoDeCombustiblePorKm;
  boolean esPrivado;

  private Vehiculo() {
  }

  public Vehiculo(TipoVehiculo tipoDeVehiculo,
                  TipoConsumo tipoConsumo, boolean esPrivado, ServicioDistancia servicio) {
    this.tipoDeVehiculo = tipoDeVehiculo;
    this.tipoConsumo = tipoConsumo;
    this.esPrivado = esPrivado;
    this.servicio = servicio;
  }

  public void setConsumoDeCombustiblePorKm(float consumoDeCombustiblePorKm) {
    this.consumoDeCombustiblePorKm = consumoDeCombustiblePorKm;
  }

  public boolean getPrivado() {
    return this.esPrivado;
  }

  public TipoVehiculo getTipoDeVehiculo() {
    return tipoDeVehiculo;
  }

  public TipoConsumo getTipoConsumo() {
    return tipoConsumo;
  }

  @Override
  public float obtenerDistancia(Ubicacion origen, Ubicacion destino) throws IOException {
    return ServicioDistancia.getInstancia().obtenerDistancia(origen, destino);
  }

  @Override
  public float obtenerConsumoDeCombustiblePorKm() {
    return consumoDeCombustiblePorKm;
  }

  @Override
  public float obtenerFE() {
    return tipoConsumo.getFactorEmision();
  }

  public boolean esCompartible() {

    return (this.tipoDeVehiculo == TipoVehiculo.AUTO)
        || (this.tipoDeVehiculo == TipoVehiculo.SERVICIOCONTRATADO);

  }
}
