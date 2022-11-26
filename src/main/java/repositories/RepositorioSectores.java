package repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import model.Organizacion;
import model.Sector;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

public class RepositorioSectores implements WithGlobalEntityManager {
  public static final  RepositorioSectores instancia = new RepositorioSectores();

  public Sector buscar(long id) {
    return entityManager().find(Sector.class, id);
  }

  public List<Sector> getSectoresPorUsuario(int organizacionId, int usuarioId) {
    List<Sector> sectoresPorUsuario = new ArrayList<>();
    Organizacion organizacion = RepositorioOrganizaciones.instancia
        .buscar(organizacionId);
    if (organizacion == null) {
      return  sectoresPorUsuario;
    }
    List<Sector> sectores = organizacion.getSectores();
    sectoresPorUsuario = sectores.stream().filter(sector -> {
      return sector.getMiembros().stream().anyMatch(miembro -> {
        return miembro.getPersona().getId() == usuarioId;
      });
    }).collect(Collectors.toList());
    return sectoresPorUsuario;
  }
}