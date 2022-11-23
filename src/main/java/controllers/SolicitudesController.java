package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Miembro;
import model.Organizacion;
import model.PostulanteDto;
import model.Sector;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import repositories.RepositorioMiembros;
import repositories.RepositorioOrganizaciones;
import repositories.RepositorioSectores;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import utils.SessionValidator;

public class SolicitudesController implements WithGlobalEntityManager, TransactionalOps {

  public ModelAndView lectura(Request request, Response response) {

    SessionValidator sessionValidator = new SessionValidator();

    if (!sessionValidator.estaLogueado(request)) {
      response.redirect("/loginUsuarioSinLogueo");
      return null;
    } else {
      if (sessionValidator.estaLogueado(request)
          && sessionValidator.tienePermisosAdmin(request)) {
        Map<String, Object> modelo = new HashMap<>();
        String id = request.params(":organizacion_id");

        Organizacion organizacion = RepositorioOrganizaciones
            .instancia
            .buscar(Long.parseLong(id));

        List<PostulanteDto> encontrados = organizacion.getPostulantes();

        modelo.put("postulantes", encontrados);
        modelo.put("organizacionId", id);

        return new ModelAndView(modelo, "solicitudes.html.hbs");
      } else {
        return new ModelAndView(null, "sinAcceso.html.hbs");
      }
    }
  }

  public Void vincular(Request request, Response response) {
    withTransaction(() -> {
      Miembro postulante = RepositorioMiembros.instancia
          .buscar(Long.parseLong(request.queryParams("id")));

      Sector sector = RepositorioSectores.instancia
          .buscar(Long.parseLong(request.queryParams("sectorId")));

      //System.out.println(postulante);

      if (request.queryParams("aceptar") != null) {
        sector.aceptarVinculacion(postulante);
      } else if (request.queryParams("rechazar") != null) {
        sector.rechazarVinculacion(postulante);
      }
    });

    response
        .redirect("/" + request.queryParams("organizacionId") + "/solicitudes");

    return null;
  }
}