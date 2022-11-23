package model;

import domain.services.distancia.ServicioDistancia;
import domain.services.distancia.ServicioDistanciaInterface;
import domain.services.distancia.entities.Ubicacion;
import java.io.IOException;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("Bicicleta")
@Table(name = "bicicletas")
public class Bicicleta extends MedioDeTransporte {
  @Transient
  ServicioDistancia servicio;

  private Bicicleta() {
  }

  public Bicicleta(ServicioDistancia servicio) {
    this.servicio = servicio;
  }

  @Override
  public float obtenerDistancia(Ubicacion origen, Ubicacion destino) throws IOException {

    //this.servicio = ServicioDistancia.getInstancia();
    return servicio.obtenerDistancia(origen, destino);
  }


  @Override
  public float obtenerConsumoDeCombustiblePorKm() {
    return 0;
  }

  @Override
  public float obtenerFE() {
    return 0;
  }
}