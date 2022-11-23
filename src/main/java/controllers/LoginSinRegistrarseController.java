package controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import model.Usuario;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class LoginSinRegistrarseController implements WithGlobalEntityManager, TransactionalOps {

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
      Usuario usuario = RepositorioUsuarios.instance().buscarPorUsuarioYContrasenia(
          request.queryParams("username"),
          request.queryParams("password"));

      request.session().attribute("user_id", usuario.getId());
      request.session().attribute("rol_id", usuario.getRol());

      response.redirect("/"); // TODO aca va a convenir leer el origen
      request.session().attribute("rol_id", usuario.getRol());
      return null;
    } catch (NoSuchElementException e) {
      response.redirect("/login"); // TODO redirigir agregando un mensaje de error
      return null;
    }
  }

  public Void cerrarSesion(Request request, Response response) {
    request.session().removeAttribute("user_id");
    request.session(true).invalidate();
    response.redirect("/");
    return null;
  }
}
