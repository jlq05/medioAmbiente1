import model.ContrsenaInvalidaException;
import model.CriterioFormato;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestCriterioFormato {
  @Test
  public void noEsContrasenaValidaSinLetras() {
    validar("111123Y!", "La contraseña no cumple el formato");
  }

  @Test
  public void noEsContrasenaValidaSinNumeros() {
    validar("agussssY!", "La contraseña no cumple el formato");
  }

  @Test
  public void noEsContrasenaValidaSinCaracterEspecial() {
    validar("agus123Yf", "La contraseña no cumple el formato");
  }

  @Test
  public void noEsContrasenaValidaCorta() {
    validar("agus123", "La contraseña no cumple el formato");
  }

  private void validar(String contrasena, String msj) {
    CriterioFormato validacion = new CriterioFormato();
    ContrsenaInvalidaException thrown = Assertions
        .assertThrows(ContrsenaInvalidaException.class, () -> {
          validacion.validar(contrasena);
        });
    Assertions
        .assertEquals(msj,
            thrown.getMessage());
  }

}