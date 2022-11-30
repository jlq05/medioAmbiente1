package model;

public class EvolucionHc {
  private float hcValor;
  private String fecha;

  public EvolucionHc(float hcValor, String fecha) {
    this.hcValor = hcValor;
    this.fecha = fecha;
  }

  public String getFecha() {
    return fecha;
  }

  public void setFecha(String fecha) {
    this.fecha = fecha;
  }

  public float getHcValor() {
    return hcValor;
  }

  public void setHcValor(float valor) {
    this.hcValor = valor;
  }
}
