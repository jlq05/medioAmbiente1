import domain.services.distancia.entities.Ubicacion;
import model.*;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class TestOrganizaciones {

  @Test
  public void verificarCreacionDeUnaOrganizacion() {

    Sector sector = crearSectorVacio();
    List<Sector> sectores = new ArrayList<Sector>() ;
    List<Contacto> contactos = new ArrayList<Contacto>();
    List<Notificador> tiposNotificacion = new ArrayList<Notificador>();
    Organizacion organizacion = crearOrganizacion(
        "12345678",
        getUbicacionArgentina(),
        TipoOrganizacion.EMPRESA,
        Clasificacion.UNIVERSIDAD,
        sectores,
        contactos,
        tiposNotificacion);
    organizacion.agregarSector(new Sector("compras", new ArrayList<Miembro>(), new ArrayList<Miembro>()));

    assertEquals(organizacion.getRazonSocial(),"12345678" );
    assertEquals(organizacion.getUbicacionGeografica().getCalle(),"CABA" );
    assertEquals(organizacion.getTipo(), TipoOrganizacion.EMPRESA);
    assertEquals(organizacion.getClasificacion(),Clasificacion.UNIVERSIDAD);
    assertEquals(organizacion.getSectores().size(),1);

  }

  @Test
  public void verificarCreacionDeUnaPersona() {
    List<Trayecto> trayectos = new ArrayList<Trayecto>();
    Miembro miembro = crearMiembro("Jose", "Pepe", TipoDocumento.DNI, 1111111, trayectos );
    assertEquals(miembro.getPersona().getNombre(), "Jose");
    assertEquals(miembro.getPersona().getApellido(), "Pepe");
    assertEquals(miembro.getPersona().getTipo(), TipoDocumento.DNI);
    assertEquals(miembro.getPersona().getDocumento(), 1111111);
    assertEquals(miembro.getTrayectos().size(),0);
  }


  @Test
  public void verificarCreacionDeUnSectorEnUnaOrganizacion() {

    Sector sector = crearSectorVacio();
    Contacto contacto = crearUnContacto();
    List<Contacto> contactos = new ArrayList<Contacto>();
    contactos.add(contacto);
    List<Sector> sectores = new ArrayList<Sector>() ;
    sectores.add(sector);
    List<Notificador> tiposNotificacion = new ArrayList<Notificador>();
    Organizacion organizacion = crearOrganizacion(
        "12345678",
        getUbicacionArgentina(),
        TipoOrganizacion.EMPRESA,
        Clasificacion.MINISTERIO,
        sectores,
        contactos,
        tiposNotificacion);

    assertEquals(organizacion.sectores.size(), 1);

  }

  @Test
  public void verificarCreacionDeUnaPersonaEnUnSector() {
    List<Trayecto> trayectos = null;
    Miembro miembro = crearMiembro("Jose", "Pepe", TipoDocumento.DNI, 1111111,trayectos);

    Sector sector = new Sector("Compras", new ArrayList<Miembro>(), new ArrayList<Miembro>());
    sector.agregarMiembro(miembro);
    List<Sector> sectores = new ArrayList<Sector>() ;
    sectores.add(sector);

    Contacto otroContacto = crearUnContacto();
    List<Contacto> contactos = new ArrayList<Contacto>();
    contactos.add(otroContacto);
    List<Notificador> tiposNotificacion = new ArrayList<Notificador>();

    Organizacion organizacion = crearOrganizacion(
        "12345678",
        getUbicacionArgentina(),
        TipoOrganizacion.EMPRESA,
        Clasificacion.MINISTERIO,
        sectores,
        contactos,
        tiposNotificacion);

    assertEquals(
        organizacion.sectores.get(0).getMiembros().get(0).getPersona().getNombre(),
        "Jose");

  }

  @Test
  public void verificarCreacionDeUnPostulanteEnUnSector() {
    Miembro miembro2 = crearMiembro(
        "Roli", "Rolando", TipoDocumento.DNI, 222222, new ArrayList<Trayecto>()
    );

    Sector sector = crearSectorVacio();
    sector.agregarPostulante(miembro2);
    List<Sector> sectores = new ArrayList<Sector>() ;
    sectores.add(sector);

    Contacto otroContacto = crearUnContacto();
    List<Contacto> contactos = new ArrayList<Contacto>();
    contactos.add(otroContacto);
    List<Notificador> tiposNotificacion = new ArrayList<Notificador>();
    Organizacion organizacion = crearOrganizacion(
        "12345678",
        getUbicacionArgentina(),
        TipoOrganizacion.EMPRESA,
        Clasificacion.MINISTERIO,
        sectores,
        contactos,
        tiposNotificacion);

    assertEquals(
        organizacion.sectores.get(0).getPostulantes().get(0).getPersona().getNombre(),
        "Roli"
    );
  }

  @Test
  public void verificarIntervaloDeOrganizacion() {
    List<Sector> sectores = new ArrayList<Sector>() ;
    Organizacion organizacion = crearOrganizacion(
        "12345678",
        getUbicacionArgentina(),
        TipoOrganizacion.EMPRESA,
        Clasificacion.MINISTERIO,
        sectores,
        new ArrayList<>(),
        new ArrayList<>());

    Intervalo intervalo = organizacion.getIntervalo(TipoPeriodicidad.Mensual, YearMonth.of(2020, 2));
    assertEquals(intervalo.getFechaDesde(), intervalo.getFechaHasta());

    Intervalo intervalo_anual = organizacion.getIntervalo(TipoPeriodicidad.Anual, YearMonth.of(2020, 1));
    assertEquals(intervalo_anual.getFechaDesde(), YearMonth.of(2020, 01));
    assertEquals(intervalo_anual.getFechaHasta(), YearMonth.of(2020, 12));
  }

  @Test
  public void notificarOk() {
    WhatsApp apiWhatapp = mock(WhatsApp.class);
    Contacto otroContacto = crearUnContacto();
    List<Contacto> contactos = new ArrayList<Contacto>();
    contactos.add(otroContacto);
    List<Notificador> tiposNotificacion = setNotificaciones(new NotificacionWhatsApp(apiWhatapp));
    Organizacion organizacion = crearOrganizacion(
        "12345678",
        getUbicacionArgentina(),
        TipoOrganizacion.EMPRESA,
        Clasificacion.UNIVERSIDAD,
        null,
        contactos,
        tiposNotificacion);
    organizacion.notificar("test","Asunto del mensaje");
    verify(apiWhatapp, times(1)).enviar(anyString(), anyString());
  }

  private Ubicacion getUbicacionArgentina(){
    return new Ubicacion(1, "CABA", 2);
  }

  private Sector crearSectorVacio() {
    Sector sector = new Sector("Compras", new ArrayList<Miembro>(),new ArrayList<Miembro>());
    return sector;
  }

  private Miembro crearMiembro(String nombre, String apellido, TipoDocumento tipo, int documento,List<Trayecto> trayectos) {

    Miembro miembro = new Miembro(
        new Persona(
            nombre,
            apellido,
            tipo,
            documento
        ),
        trayectos
    );
    return miembro;
  }

  private Organizacion crearOrganizacion(String razonSocial, Ubicacion ubicacion, TipoOrganizacion tipo, Clasificacion clasificacion, List<Sector> sectores, List<Contacto> contactos, List<Notificador> tiposDeNotificacion) {
    Organizacion organizacion = new Organizacion(
        razonSocial,
        ubicacion,
        tipo,
        clasificacion,
        sectores,
        contactos,
        new ArrayList<>(),
        tiposDeNotificacion
    );
    return organizacion;
  }

  private Contacto crearUnContacto() {
    Contacto contacto = new Contacto("Pepe", "1149505844", "pepe2022@gmail.com");
    return contacto;
  }

  private List<Notificador> setNotificaciones(Notificador notificacion) {
    List<Notificador> notis = new ArrayList<>();
    notis.add(notificacion);
    return notis;
  }
}