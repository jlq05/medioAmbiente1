package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
  RepositorioOrganizaciones repositorioOrganizaciones = RepositorioOrganizaciones.instancia;

  public ModelAndView lectura(Request request, Response response) {
    if (!sessionValidator.estaLogueado(request)) {
      response.redirect("/loginUsuarioSinLogueo");
      return null;
    } else {
      if (sessionValidator.estaLogueado(request)
          && sessionValidator.tienePermisosUsuario(request)) {

        String nombreBuscado = request.queryParams("nombre");

        Map<String, Object> modelo = new HashMap<>();

        List<Organizacion> organizaciones = nombreBuscado != null
            ? repositorioOrganizaciones.buscarPorNombre(nombreBuscado) :
            repositorioOrganizaciones.listar();

        modelo.put("organizaciones", organizaciones);

        return new ModelAndView(modelo, "vinculacion.html.hbs");
      } else {
        return new ModelAndView(null, "sinAcceso.html.hbs");
      }
    }
  }

  public Void buscar(Request request, Response response) {

    Boolean existe = RepositorioOrganizaciones.instancia
        .existeOrganizacion(request.queryParams("nombre"));
    System.out.println(existe);

    if (existe) {
      Organizacion organizacion = RepositorioOrganizaciones.instancia
          .buscarPorNombre(request.queryParams("nombre")).get(0);

      response.redirect("/home/" + organizacion.getId() + "/sectores/vinculacion");

    } else {
      response.redirect("/home/errores");
    }
    return null;
  }
}