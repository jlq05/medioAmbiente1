package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import model.Organizacion;
import model.Persona;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import repositories.RepositorioOrganizaciones;
import repositories.RepositorioUsuarios;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class LoginController implements WithGlobalEntityManager, TransactionalOps {

  public ModelAndView lectura(Request request, Response response) {

    if (request.session().attribute("user_id") != null) {
      response.redirect("/");
      return null;
    }

    Map<String, Object> modelo = new HashMap<>();
    modelo.put("sesionIniciada", request.session().attribute("user_id") != null);
    return new ModelAndView(modelo, "login.html.hbs");
  }

  public Void crearSesion(Request request, Response response) {
    try {
      RepositorioOrganizaciones repositorioOrganizaciones = new RepositorioOrganizaciones();
      Persona usuario = RepositorioUsuarios.instance().buscarPorUsuarioYContrasenia(
          request.queryParams("username"),
          request.queryParams("password"));
      if (usuario == null) {
        response.redirect("home/sinAcceso");
        return null;
      }
      List<Organizacion> organizacionesAsociadas =
          repositorioOrganizaciones.getOrganizacionesPorUsuarioId(usuario.getId().intValue());
      int organizacionId = organizacionesAsociadas.size() > 0
          ? organizacionesAsociadas.get(0).getId().intValue()
          : -1;

      request.session().attribute("user_id", usuario.getId().intValue());
      request.session().attribute("rol_id", usuario.getRol());
      request.session().attribute("organizacion_id", organizacionId);

      response.redirect("/");
      return null;
    } catch (NoSuchElementException e) {
      response.redirect("/login");
      return null;
    }
  }

  public Void cerrarSesion(Request request, Response response) {
    request.session().removeAttribute("user_id");
    request.session().removeAttribute("rol_id");
    request.session().removeAttribute("organizacion_id");
    request.session(true).invalidate();
    response.redirect("/");
    return null;
  }
}
