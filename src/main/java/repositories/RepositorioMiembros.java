package repositories;

import model.Miembro;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

public class RepositorioMiembros implements WithGlobalEntityManager {
  public static final RepositorioMiembros instancia = new RepositorioMiembros();

  public Miembro buscar(long id) {
    return entityManager().find(Miembro.class, id);
  }
}
