package repositories;

import java.util.List;
import model.TipoConsumo;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

public class RepositorioTipoDeConsumo implements WithGlobalEntityManager {
  public static final RepositorioTipoDeConsumo instancia = new RepositorioTipoDeConsumo();

  public List<TipoConsumo> getTiposDeConsumo() {
    return entityManager()
        .createQuery("from TipoConsumo", TipoConsumo.class)
        .getResultList();
  }

  public TipoConsumo get(int id) {
    long tipoConsumoId = id;
    return entityManager().createQuery("from TipoConsumo where id =:id ", TipoConsumo.class)
        .setParameter("id", tipoConsumoId).getSingleResult();
  }
}
