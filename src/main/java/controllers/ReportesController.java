package controllers;

import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import utils.SessionValidator;

public class ReportesController implements WithGlobalEntityManager, TransactionalOps {

  SessionValidator sessionValidator = new SessionValidator();

  public ModelAndView lectura(Request request, Response response) {
    if (!sessionValidator.estaLogueado(request)) {
      response.redirect("/loginUsuarioSinLogueo");
      return null;
    } else {
      if (sessionValidator.estaLogueado(request)
          && sessionValidator.tienePermisosAdmin(request)) {
        return new ModelAndView(null, "reportes.html.hbs");
      } else {
        return new ModelAndView(null, "sinAcceso.html.hbs");
      }
    }
  }
}
