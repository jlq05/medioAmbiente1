package repositories;

import model.Miembro;
import model.Tramo;
import model.Trayecto;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

public class RepositorioTrayecto implements WithGlobalEntityManager {
  public static final RepositorioTrayecto instancia = new RepositorioTrayecto();

  public void agregar(Trayecto trayecto) {
    entityManager().persist(trayecto);
  }

  public Trayecto get(int id) {
    long trayectoId = id;
    return entityManager().createQuery("from Trayecto where id =:id ", Trayecto.class)
        .setParameter("id", trayectoId).getSingleResult();
  }

  public void agregarTramo(Tramo tramo) {
    entityManager().persist(tramo);
  }

  public Tramo getTramo(int id) {
    long tramoId = id;
    return entityManager().createQuery("from Tramo where id =:id ", Tramo.class)
        .setParameter("id", tramoId).getSingleResult();
  }
}
