package model;

public class ContrsenaInvalidaException extends RuntimeException {
  public ContrsenaInvalidaException(String causa) {
    super(causa);
  }
}