package model;

import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class CriterioTop10k implements Validacion {
  public void validar(String contrasena) {
    try {
      URL url = new URL(Constantes.TOP_10000);
      InputStream open = url.openStream();
      Scanner lee = new Scanner(open,"UTF-8");
      String linea;

      while (lee.hasNextLine()) {
        linea = lee.nextLine();
        if (linea.contains(contrasena)) {
          throw new ContrsenaInvalidaException("Contraseña invalida");
        }
      }
      lee.close();
    } catch (Exception ex) {
      throw  new ContrsenaInvalidaException(ex.getMessage());
    }
  }
}