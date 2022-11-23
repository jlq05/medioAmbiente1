package controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class RecomendacionController {

  public ModelAndView lectura(Request request, Response response) {
    if (request.session().attribute("user_id") == null) {
      response.redirect("/loginUsuarioSinLogueo");
      return null;
    } else {
      Map<String, Object> modelo = new HashMap<>();
      modelo.put("fecha", new Date());
      return new ModelAndView(modelo, "recomendaciones.html.hbs");
    }
  }
}