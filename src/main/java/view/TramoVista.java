package view;

public class TramoVista {
  public int id;
  public String origen;
  public String destino;
  public String medioDeTransporte;
  public String consumoDeCombustible;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getOrigen() {
    return origen;
  }

  public void setOrigen(String origen) {
    this.origen = origen;
  }

  public String getDestino() {
    return destino;
  }

  public void setDestino(String destino) {
    this.destino = destino;
  }

  public String getMedioDeTransporte() {
    return medioDeTransporte;
  }

  public void setMedioDeTransporte(String medioDeTransporte) {
    this.medioDeTransporte = medioDeTransporte;
  }

  public String getConsumoDeCombustible() {
    return consumoDeCombustible;
  }

  public void setConsumoDeCombustible(String consumoDeCombustible) {
    this.consumoDeCombustible = consumoDeCombustible;
  }
}
