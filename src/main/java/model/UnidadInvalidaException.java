package model;

public class UnidadInvalidaException extends RuntimeException {
  public UnidadInvalidaException(String causa) {
    super(causa);
  }
}