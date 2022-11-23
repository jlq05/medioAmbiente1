package model;

public class ParadaNoExisteException extends RuntimeException {
  public ParadaNoExisteException(String causa) {
    super(causa);
  }
}
