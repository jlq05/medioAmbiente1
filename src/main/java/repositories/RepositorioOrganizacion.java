package repositories;

import domain.services.distancia.entities.Ubicacion;
import java.util.ArrayList;
import java.util.List;
import model.Clasificacion;
import model.Contacto;
import model.NotificacionMail;
import model.Notificador;
import model.Organizacion;
import model.Sector;
import model.TipoOrganizacion;

public class RepositorioOrganizacion {
  public List<Organizacion> getAll() {
    return valoresPrueba();
  }

  private static List<Organizacion> valoresPrueba() {
    Sector sector = new Sector("Ventas", new ArrayList<>(), new ArrayList<>());
    Contacto contacto = new Contacto("Pepe", "+5491167097068", "agustin.y@outlook.com");
    List<Contacto> contactos = new ArrayList<Contacto>();
    contactos.add(contacto);
    List<Notificador> notis = new ArrayList<Notificador>();
    notis.add(new NotificacionMail());
    List<Sector> sectores = new ArrayList<Sector>();
    sectores.add(sector);

    Organizacion org = new Organizacion(
        "12345678",
        new Ubicacion(123, "Av. Corrientes", 4500),
        TipoOrganizacion.EMPRESA,
        Clasificacion.MINISTERIO,
        sectores,
        contactos,
        new ArrayList<>(),
        notis);
    List<Organizacion> listOrg = new ArrayList<>();
    listOrg.add(org);

    return listOrg;
  }
}