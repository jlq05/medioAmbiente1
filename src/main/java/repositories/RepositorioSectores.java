package repositories;

import model.Sector;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

public class RepositorioSectores implements WithGlobalEntityManager {
  public static final  RepositorioSectores instancia = new RepositorioSectores();

  public Sector buscar(long id) {
    return entityManager().find(Sector.class, id);
  }
}