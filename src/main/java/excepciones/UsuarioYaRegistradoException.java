package excepciones;

public class UsuarioYaRegistradoException extends RuntimeException {
  public UsuarioYaRegistradoException(String message) {
    super(message);
  }
}
