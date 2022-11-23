import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestAdministrador {
  @Test
  public void crearUsuarioInvalido() {
   /* validacion(
        "fp09",
        "111123Y",
        "La contraseña no cumple el formato"
    );*/

  }
  /*
  private Administrador iniciarUsuario(String usuario, String contrasena) {
    Administrador nuevoAdministrador = new Administrador(usuario, contrasena);
    nuevoAdministrador.setNombre("Ariel");
    nuevoAdministrador.setApellido("Test");
    return nuevoAdministrador;
  }

  private void validacion(String usuario, String contrasena, String msj) {
    ContrsenaInvalidaException thrown = Assertions
        .assertThrows(ContrsenaInvalidaException.class, () -> {
          iniciarUsuario(usuario, contrasena);
        });
    Assertions
        .assertEquals(msj,
            thrown.getMessage());
  }*/
}