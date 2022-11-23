package domain.services.distancia.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Embeddable
public class Ubicacion {

  int localidadId;

  String calle;

  int altura;

  private Ubicacion() {
  }

  public Ubicacion(int localidadId, String calle, int altura) {
    this.localidadId = localidadId;
    this.calle = calle;
    this.altura = altura;
  }

  public int getLocalidadId() {
    return localidadId;
  }

  public String getCalle() {
    return calle;
  }

  public int getAltura() {
    return altura;
  }

}

