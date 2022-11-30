package controllers;

import model.Organizacion;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import repositories.RepositorioOrganizaciones;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import utils.SessionValidator;

public class VinculacionController implements WithGlobalEntityManager, TransactionalOps {

  SessionValidator sessionValidator = new SessionValidator();

  public ModelAndView lectura(Request request, Response response) {
    if (!sessionValidator.estaLogueado(request)) {
      response.redirect("/loginUsuarioSinLogueo");
      return null;
    } else {
      if (sessionValidator.estaLogueado(request)
          && sessionValidator.tienePermisosUsuario(request)) {
        return new ModelAndView(null, "vinculacion.html.hbs");
      } else {
        return new ModelAndView(null, "sinAcceso.html.hbs");
      }
    }
  }

  public Void buscar(Request request, Response response) {
    Organizacion organizacion = RepositorioOrganizaciones.instancia
        .buscarPorNombre(request.queryParams("nombre")).get(0);

    System.out.println(organizacion);

    response.redirect("/home/" + organizacion.getId() + "/sectores/vinculacion");

    return null;
  }
}