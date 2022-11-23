package main;

import java.util.HashMap;
import java.util.Map;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import utils.SessionValidator;

public class HomeController implements WithGlobalEntityManager, TransactionalOps {

  SessionValidator sessionValidator = new SessionValidator();

  public ModelAndView getHome(Request request, Response response) {
    if (!sessionValidator.estaLogueado(request)) {
      response.redirect("/loginUsuarioSinLogueo");
      return null;
    } else {
      Map<String, Object> modelo = new HashMap<>();
      modelo.put("sesionIniciada", request.session().attribute("user_id") != null);
      return new ModelAndView(modelo, "index.html.hbs");

    }
  }
}
