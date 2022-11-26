package repositories;

import java.util.List;
import model.TransportePublico;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

public class RepositorioTransportePublico implements WithGlobalEntityManager {
  public static final RepositorioTransportePublico instancia = new RepositorioTransportePublico();

  public TransportePublico buscar(long id) {
    return entityManager().find(TransportePublico.class, id);
  }

  public List<TransportePublico> getTransportesPublicos() {
    return entityManager()
        .createQuery("from TransportePublico", TransportePublico.class)
        .getResultList();
  }
}
