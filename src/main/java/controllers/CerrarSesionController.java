package controllers;

import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import spark.Request;
import spark.Response;

public class CerrarSesionController implements WithGlobalEntityManager, TransactionalOps {

  public Void cerrarSesion(Request request, Response response) {
    request.session().removeAttribute("user_id");
    request.session().removeAttribute("rol_id");
    request.session().removeAttribute("organizacion_id");
    request.session(true).invalidate();
    response.redirect("/login");
    return null;
  }
}
