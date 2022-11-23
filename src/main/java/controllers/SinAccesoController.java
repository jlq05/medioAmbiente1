package controllers;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class SinAccesoController {
  public ModelAndView lectura(Request request, Response response) {
    return new ModelAndView(null, "sinAcceso.html.hbs");
  }
}
