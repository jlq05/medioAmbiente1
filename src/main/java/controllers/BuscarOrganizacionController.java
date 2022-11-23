package controllers;

import model.Organizacion;
import repositories.RepositorioOrganizaciones;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class BuscarOrganizacionController {
  public ModelAndView lectura(Request request, Response response) {
    if (request.session().attribute("user_id") == null) {
      response.redirect("/loginUsuarioSinLogueo");
      return null;
    } else {
      return new ModelAndView(null, "filtroOrganizacion.html.hbs");
    }
  }

  public Void buscar(Request request, Response response) {
    Organizacion organizacion = RepositorioOrganizaciones.instancia
        .buscarPorNombre(request.queryParams("nombre")).get(0);

    System.out.println(organizacion);

    response.redirect("/homeUsuario/" + organizacion.getId() + "/trayecto");

    return null;
  }
}
