package model;

import domain.services.distancia.entities.Ubicacion;
import java.io.IOException;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tramos")
public class Tramo extends PersistentEntity {

  @AttributeOverrides({
      @AttributeOverride(name = "localidadId", column = @Column(name = "origen_localidad_id")),
      @AttributeOverride(name = "calle", column = @Column(name = "origen_calle")),
      @AttributeOverride(name = "altura", column = @Column(name = "origen_altura")),
  })

  @Embedded
  private Ubicacion origen;

  @AttributeOverrides({
      @AttributeOverride(name = "localidadId", column = @Column(name = "destino_localidad_id")),
      @AttributeOverride(name = "calle", column = @Column(name = "destino_calle")),
      @AttributeOverride(name = "altura", column = @Column(name = "destino_altura")),
  })

  @Embedded
  private Ubicacion destino;

  @ManyToOne
  private MedioDeTransporte medioDeTransporte;

  private Tramo() {
  }

  public Tramo(Ubicacion origen, Ubicacion destino, MedioDeTransporte medioDeTransporte) {
    this.origen = origen;
    this.destino = destino;
    this.medioDeTransporte = (MedioDeTransporte) medioDeTransporte;
  }

  public String getDestino() {
    return destino.getCalle();
  }

  public String getOrigen() {
    return origen.getCalle();
  }

  public MedioDeTransporte getMedioDeTransporte() {
    return medioDeTransporte;
  }

  public float obtenerDistancia() throws IOException {
    return this.medioDeTransporte.obtenerDistancia(origen, destino);
  }

  public float obtenerCalculoHC() throws IOException {
    return this.obtenerConsumoDeCombustible() * medioDeTransporte.obtenerFE();
  }

  private float obtenerConsumoDeCombustible() throws IOException {
    return this.medioDeTransporte.obtenerConsumoDeCombustiblePorKm() * this.obtenerDistancia();
  }
}
