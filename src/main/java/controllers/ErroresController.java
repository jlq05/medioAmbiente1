package controllers;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class ErroresController {
  public ModelAndView lectura(Request request, Response response) {
    return new ModelAndView(null, "errores.html.hbs");
  }
}
