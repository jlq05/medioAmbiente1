package model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CriterioFormato implements Validacion {

  public void validar(String contrasena) {
    String regex = "^(?=.*[0-9])"
          + "(?=.*[a-z])(?=.*[A-Z])"
          + "(?=.*[@#$%^&+=])"
          + "(?=\\S+$).{8,20}$";
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(contrasena);

    if (!m.matches()) {
      throw  new ContrsenaInvalidaException("La contraseña no cumple el formato");
    }
  }
}
