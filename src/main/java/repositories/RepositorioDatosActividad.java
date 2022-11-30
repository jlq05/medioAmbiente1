package repositories;

import model.DatosActividad;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

public class RepositorioDatosActividad implements WithGlobalEntityManager {
  public static final RepositorioDatosActividad instancia = new RepositorioDatosActividad();

  public void agregar(DatosActividad datosActividad) {
    entityManager().persist(datosActividad);
  }
}
