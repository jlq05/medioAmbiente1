import model.ContrsenaInvalidaException;
import model.CriterioTop10k;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestCriterioTop10k {
  @Test
  public void esContrasenaComun() {
    iniciarValidacion("123456qWe@", "Contraseña invalida");

  }

  private void iniciarValidacion(String contrasena, String msj) {

    CriterioTop10k validacion = new CriterioTop10k();

    ContrsenaInvalidaException thrown = Assertions
        .assertThrows(ContrsenaInvalidaException.class, () -> {
          validacion.validar(contrasena);
        });
    Assertions
        .assertEquals(msj,
            thrown.getMessage());
  }
}