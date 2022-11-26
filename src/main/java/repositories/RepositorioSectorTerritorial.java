package repositories;

import java.util.List;
import model.SectorTerritorial;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

public class RepositorioSectorTerritorial implements WithGlobalEntityManager {
  public static final RepositorioSectorTerritorial instancia = new RepositorioSectorTerritorial();

  public List<SectorTerritorial> getSectorTerritoriales() {
    return entityManager()
        .createQuery("from SectorTerritorial", SectorTerritorial.class)
        .getResultList();
  }

  public SectorTerritorial get(int id) {
    long sectorTerritorialId = id;
    return entityManager().createQuery(
        "from SectorTerritorial where id =:id ", SectorTerritorial.class
        )
        .setParameter("id", sectorTerritorialId).getSingleResult();
  }
}
