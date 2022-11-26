package repositories;

import java.util.List;
import model.Miembro;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

public class RepositorioMiembros implements WithGlobalEntityManager {
  public static final RepositorioMiembros instancia = new RepositorioMiembros();

  public Miembro buscar(long id) {
    return entityManager().find(Miembro.class, id);
  }

  public void agregar(Miembro miembro) {
    entityManager().persist(miembro);
  }

  public List<Miembro> miembrosPorUsuario(long usuarioId) {
    return entityManager()
        .createQuery("from Miembro where persona_id = :usuarioId", Miembro.class)
        .setParameter("usuarioId", usuarioId)
        .getResultList();
  }
}
