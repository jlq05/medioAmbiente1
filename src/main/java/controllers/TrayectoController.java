package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import utils.SessionValidator;

public class TrayectoController implements WithGlobalEntityManager, TransactionalOps {

  SessionValidator sessionValidator = new SessionValidator();

  public ModelAndView lectura(Request request, Response response) {
    if (!sessionValidator.estaLogueado(request)) {
      response.redirect("/loginUsuarioSinLogueo");
      return null;
    } else {
      if (sessionValidator.estaLogueado(request)
          && sessionValidator.tienePermisosUsuario(request)) {
        Map model = new HashMap();
        model.put("trayectos",  new ArrayList<>());
        return new ModelAndView(model, "trayecto.html.hbs");
      } else {
        //response.redirect("/SinAcceso");
        return new ModelAndView(null, "sinAcceso.html.hbs");
        //return null;
      }
    }
  }

  public ModelAndView nuevo(Request request, Response response) {
    if (request.session().attribute("user_id") == null) {
      response.redirect("/");
      return null;
    } else {
      return new ModelAndView(null, "nuevotrayecto.html.hbs");
    }
  }

  public ModelAndView tramonuevo(Request request, Response response) {
    if (request.session().attribute("user_id") == null) {
      response.redirect("/loginUsuarioSinLogueo");
      return null;
    } else {
      return new ModelAndView(null, "nuevotramo.html.hbs");
    }
  }
}
