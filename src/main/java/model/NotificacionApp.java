package model;

import java.util.List;
import repositories.RepositorioOrganizacion;

public class NotificacionApp {

  public static void main(String[] arg) {
    RepositorioOrganizacion db = new RepositorioOrganizacion();
    List<Organizacion> listOrg = db.getAll();
    listOrg.forEach(o -> o.notificar("hola mundo!", "Asunto del mensaje"));

  }

}