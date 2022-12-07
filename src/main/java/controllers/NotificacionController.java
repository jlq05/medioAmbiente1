package controllers;

import java.util.List;
import model.Organizacion;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import repositories.RepositorioOrganizaciones;
import spark.Request;
import spark.Response;
import utils.SessionValidator;

public class NotificacionController implements WithGlobalEntityManager, TransactionalOps {
  public NotificacionController() {
  }

  public Void enviarNotificaciones(Request request, Response response) {
    System.out.println("Envío de notificaciones");
    RepositorioOrganizaciones db = new RepositorioOrganizaciones();
    List<Organizacion> organizaciones = db.listar();
    organizaciones.forEach(
        organizacion ->
            organizacion.notificar("hola mundo!", "Asunto del mensaje"));
    return null;
  }
}
