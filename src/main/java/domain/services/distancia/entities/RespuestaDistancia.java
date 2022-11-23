package domain.services.distancia.entities;

public class RespuestaDistancia {
  String valor;

  RespuestaDistancia(String valor) {
    this.valor = valor;
  }

  public float getValorFloat() {
    return Float.parseFloat(valor);
  }
}