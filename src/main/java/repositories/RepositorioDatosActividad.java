package repositories;

import java.util.List;
import model.DatosActividad;
import model.TipoConsumo;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

public class RepositorioDatosActividad implements WithGlobalEntityManager {
  public static final RepositorioDatosActividad instancia = new RepositorioDatosActividad();

  public void agregar(DatosActividad datosActividad) {
    entityManager().persist(datosActividad);
  }

  public List<TipoConsumo> buscarDatos() {
    List<TipoConsumo> tiposConsumo = entityManager()
        .createQuery("from TipoConsumo", TipoConsumo.class)
        .getResultList();
    return tiposConsumo;
  }
}
