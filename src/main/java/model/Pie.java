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
@Table(name = "pies")
@DiscriminatorValue("Pie")
public class Pie extends MedioDeTransporte {
  @Transient
  ServicioDistancia servicio;

  private Pie() {
  }


  public Pie(ServicioDistancia servicio) {

    this.servicio = servicio;
  }

  @Override
  public float obtenerDistancia(Ubicacion origen, Ubicacion destino) throws IOException {
    //this.servicio = ServicioDistancia.getInstancia();
    return servicio.obtenerDistancia(origen, destino);
  }

  public float obtenerConsumoDeCombustiblePorKm() {
    return 0;
  }

  public float obtenerFE() {
    return 0;
  }
}