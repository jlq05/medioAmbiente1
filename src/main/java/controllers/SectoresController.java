package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Miembro;
import model.Sector;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import repositories.RepositorioMiembros;
import repositories.RepositorioOrganizaciones;
import repositories.RepositorioSectores;
import spark.ModelAndView;
import spark.Request;
import spark.Response;


public class SectoresController implements WithGlobalEntityManager, TransactionalOps {
  public ModelAndView listar(Request request, Response response) {
    Map<String, Object> modelo = new HashMap<>();
    String id = request.params(":organizacion_id");

    List<Sector> sectores = RepositorioOrganizaciones.instancia
        .buscar(Long.parseLong(id)).getSectores();

    modelo.put("sectores", sectores);

    return new ModelAndView(modelo, "sectores.html.hbs");
  }

  public Void vincularse(Request request, Response response) {
    Long sectorId = Long.parseLong(request.queryParams("sector"));

    withTransaction(() -> {
      Miembro postulante = RepositorioMiembros.instancia.buscar(1618);
      Sector sector = RepositorioSectores.instancia.buscar(sectorId);

      sector.agregarPostulante(postulante);
    });

    response.redirect("/");

    return null;
  }
}
