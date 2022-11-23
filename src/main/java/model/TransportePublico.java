package model;

import domain.services.distancia.entities.Ubicacion;
import java.io.IOException;
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

  @ManyToOne
  Linea lineaDeTransporte;

  float consumoDeCombustiblePorKm;

  private TransportePublico() {
  }

  public TransportePublico(TipoTransportePublico tipoDeTransportePublico,
                           Linea lineaDeTransporte) {

    this.tipoDeTransportePublico = tipoDeTransportePublico;
    this.lineaDeTransporte = lineaDeTransporte;
  }

  public TipoTransportePublico getTipoDeTransportePublico() {
    return tipoDeTransportePublico;
  }

  public void setConsumoDeCombustiblePorKm(float consumoDeCombustiblePorKm) {
    this.consumoDeCombustiblePorKm = consumoDeCombustiblePorKm;
  }

  public float obtenerDistancia(Ubicacion origen, Ubicacion destino)  throws IOException {
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

}