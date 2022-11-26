package model;

import domain.services.distancia.ServicioDistancia;
import domain.services.distancia.entities.Ubicacion;
import java.io.File;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import view.OrganizacionVista;

@Entity
@Table(name = "organizaciones")
public class Organizacion extends PersistentEntity {
  @Column
  private String razonSocial;

  @Embedded
  private Ubicacion ubicacionGeografica;

  @Enumerated(EnumType.ORDINAL)
  public TipoOrganizacion tipo;

  @Enumerated(EnumType.ORDINAL)
  public Clasificacion clasificacion;

  @OneToMany(cascade = {CascadeType.ALL})
  @JoinColumn(name = "organizacionId")
  public List<Sector> sectores;

  @ManyToMany(cascade = {CascadeType.ALL})
  public List<Contacto> contactos;

  @OneToMany(cascade = {CascadeType.ALL})
  @JoinColumn(name = "organizacionId")
  public List<DatosActividad> actividades;

  @Transient
  public List<Notificador> tiposDeNotificacion;

  public Organizacion() {
  }

  public Organizacion(String razonSocial,
                      Ubicacion ubicacionGeografica,
                      TipoOrganizacion tipo,
                      Clasificacion clasificacion,
                      List<Sector> sectores,
                      List<Contacto> contactos,
                      List<DatosActividad> actividades,
                      List<Notificador> tiposDeNotificacion
  ) {
    this.razonSocial = razonSocial;
    this.ubicacionGeografica = ubicacionGeografica;
    this.tipo = tipo;
    this.clasificacion = clasificacion;
    this.sectores = sectores;
    this.contactos = contactos;
    this.tiposDeNotificacion = tiposDeNotificacion;
    this.actividades = actividades;
  }

  public void agregarSector(Sector sector) {
    this.sectores.add(sector);
  }

  public String getRazonSocial() {
    return razonSocial;
  }

  public Ubicacion getUbicacionGeografica() {
    return ubicacionGeografica;
  }

  public TipoOrganizacion getTipo() {
    return tipo;
  }

  public Clasificacion getClasificacion() {
    return clasificacion;
  }

  public  List<Sector> getSectores() {
    return sectores;
  }

  public List<Contacto> getContactos() {
    return contactos;
  }

  public List<Notificador> getTiposDeNotificacion() {
    return tiposDeNotificacion;
  }

  public Intervalo getIntervalo(TipoPeriodicidad tipoPeriodicidad, YearMonth periodoDeImputacion) {
    YearMonth fechaDesde;
    YearMonth fechaHasta;
    int year = periodoDeImputacion.getYear();
    int month = periodoDeImputacion.getMonthValue();
    if (tipoPeriodicidad == TipoPeriodicidad.Mensual) {
      fechaDesde = YearMonth.of(year, month);
      fechaHasta = YearMonth.of(year, month);
    } else {
      fechaDesde = YearMonth.of(year, 1);
      fechaHasta = YearMonth.of(year, 12);
    }
    return new Intervalo(fechaDesde, fechaHasta);
  }

  public float obtenerCalculoHcPorActividades(Intervalo intervalo) {
    return this.actividades.stream()
        .filter(actividad -> actividad.estaEnElIntervalo(intervalo))
        .map(actividad -> actividad.obtenerCalculoHC())
        .reduce((float) 0, (hc, otroHC) -> hc + otroHC);
  }

  public List<Trayecto> obtenerTrayectos() {
    return sectores.stream()
        .map(sector -> sector.obtenerTrayectos())
        .flatMap(Collection::stream)
        .distinct()
        .collect(Collectors.toList());
  }

  public float obtenerCalculoHcPorTrayectos(ServicioDistancia servicioDistancia) {
    return this.obtenerTrayectos().stream()
        .map(trayecto -> trayecto.obtenerCalculoHC(servicioDistancia))
        .reduce(0f, (hc, otroHC) -> hc + otroHC);
  }

  public float obtenerCalculoHC(
      TipoPeriodicidad tipoPeriodicidad,
      YearMonth periodoDeImputacion,
      ServicioDistancia servicioDistancia
  ) {
    Intervalo intervalo = getIntervalo(tipoPeriodicidad, periodoDeImputacion);
    return obtenerCalculoHcPorTrayectos(servicioDistancia)
        + obtenerCalculoHcPorActividades(intervalo);
  }

  public void cargarConsumos(File archivo) {
    ProcesadorCsv procesador = new ProcesadorCsv();
    procesador.procesarArchivo(archivo);
    //return procesador.get
  }

  public float obtenerResultadoPersonal(Miembro miembro, ServicioDistancia servicioDistancia) {
    /*Devuelve porcentaje*/
    return
        miembro.obtenerCalculoHC(servicioDistancia)
        / obtenerCalculoHcPorTrayectos(servicioDistancia) * 100;
  }
  
  public void agregarContactos(Contacto contacto) {
    this.contactos.add(contacto);
  }

  public void notificar(String mensaje,String asunto) {
    contactos
        .forEach(c -> tiposDeNotificacion.forEach(n -> n.enviarNotificacion(mensaje,c,asunto)));
  }

  public List<PostulanteDto> getPostulantes() {
    List<PostulanteDto> postulantes = new ArrayList<>();
    this.getSectores().forEach(sector -> {
      sector.getPostulantes().forEach(postulante -> {
        postulantes.add(new PostulanteDto(
            postulante.getId(),
            postulante.getPersona().getDocumento(),
            postulante.getPersona().getNombre(),
            postulante.getPersona().getApellido(),
            sector.getId(),
            sector.getNombre()));
      });
    });

    return postulantes;
  }

  public void setRazonSocial(String razonSocial) {
    this.razonSocial = razonSocial;
  }

  public OrganizacionVista convertirAOrganizacionVista(
      TipoPeriodicidad tipoDePeriodicidad, YearMonth yearMonth
  ) {
    Map<Clasificacion, String> diccionarioClasificaciones = new HashMap<>();
    diccionarioClasificaciones.put(Clasificacion.ESCUELA, "Escuela");
    diccionarioClasificaciones.put(Clasificacion.MINISTERIO, "Ministerio");
    diccionarioClasificaciones.put(Clasificacion.SECTOR_PRIMARIO, "Sector Primario");
    diccionarioClasificaciones.put(Clasificacion.SECTOR_SECUNDARIO, "Sector Secundario");
    diccionarioClasificaciones.put(Clasificacion.UNIVERSIDAD, "Universidad");
    OrganizacionVista organizacionVista = new OrganizacionVista();
    organizacionVista.id = this.id.intValue();
    organizacionVista.nombre = this.razonSocial;
    organizacionVista.hc =
        this.obtenerCalculoHC(
            tipoDePeriodicidad, yearMonth, ServicioDistancia.getInstancia()
        );
    organizacionVista.hclegible = Float.toString(organizacionVista.hc);
    organizacionVista.clasificacion = diccionarioClasificaciones.get(this.clasificacion);
    return organizacionVista;
  }
}