package model;

import domain.services.distancia.ServicioDistancia;
import domain.services.distancia.entities.Ubicacion;
import java.io.IOException;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import view.TramoVista;

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

  @ManyToOne(cascade = {CascadeType.ALL})
  private MedioDeTransporte medioDeTransporte;

  private Tramo() {
  }

  public Tramo(Ubicacion origen, Ubicacion destino) {
    this.origen = origen;
    this.destino = destino;
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

  public void setMedioDeTransporte(MedioDeTransporte medioDeTransporte) {
    this.medioDeTransporte = medioDeTransporte;
  }

  public MedioDeTransporte getMedioDeTransporte() {
    return medioDeTransporte;
  }

  public float obtenerDistancia(ServicioDistancia servicioDistancia) throws IOException {
    return this.medioDeTransporte.obtenerDistancia(origen, destino, servicioDistancia);
  }

  public float obtenerCalculoHC(ServicioDistancia servicioDistancia) throws IOException {
    return this.obtenerConsumoDeCombustible(servicioDistancia) * medioDeTransporte.obtenerFE();
  }

  private float obtenerConsumoDeCombustible(
      ServicioDistancia servicioDistancia
  ) throws IOException {
    return this.medioDeTransporte.obtenerConsumoDeCombustiblePorKm()
        * this.obtenerDistancia(servicioDistancia);
  }

  public TramoVista convertirATramoVista() throws IOException {
    TramoVista tramoVista = new TramoVista();
    tramoVista.id = this.id.intValue();;
    tramoVista.origen = this.origen.getCalle() + " " + this.origen.getAltura();
    tramoVista.destino = this.destino.getCalle() + " " + this.destino.getAltura();
    tramoVista.medioDeTransporte = this.medioDeTransporte != null
        ? this.medioDeTransporte.getMedioDeTransporte() : "-";
    tramoVista.consumoDeCombustible = Float.toString(
        this.obtenerConsumoDeCombustible(ServicioDistancia.getInstancia())
    );
    return tramoVista;
  }
}
