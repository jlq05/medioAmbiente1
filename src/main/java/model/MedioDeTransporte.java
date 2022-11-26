package model;

import domain.services.distancia.ServicioDistancia;
import domain.services.distancia.entities.Ubicacion;
import java.io.IOException;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class MedioDeTransporte extends PersistentEntity {

  public MedioDeTransporte() {
  }

  public float obtenerDistancia(
      Ubicacion origen,
      Ubicacion destino,
      ServicioDistancia servicioDistancia
  ) throws IOException {
    return 0;
  }

  public float obtenerConsumoDeCombustiblePorKm() {
    return 0;
  }

  public float obtenerFE() {
    return 0;
  }

  public String getMedioDeTransporte() {
    return "";
  }
}