package controllers;

import domain.services.distancia.ServicioDistancia;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import model.Clasificacion;
import model.EvolucionHc;
import model.Organizacion;
import model.OrganizacionComposicion;
import model.SectorTerritorial;
import model.SectorTerritorialComposicion;
import model.TipoPeriodicidad;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import repositories.RepositorioOrganizaciones;
import repositories.RepositorioReportes;
import repositories.RepositorioSectorTerritorial;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import utils.SessionValidator;
import view.OrganizacionVista;

public class ReportesController implements WithGlobalEntityManager, TransactionalOps {

  SessionValidator sessionValidator = new SessionValidator();

  public ModelAndView getOpcionesDeReportes(Request request, Response response) {
    if (!sessionValidator.tienePermisosAdmin(request)) {
      return new ModelAndView(null, "sinAcceso.html.hbs");
    }
    Map model = new HashMap();
    model = new SessionValidator().setearPermisosParaTemplate(model, request);
    return new ModelAndView(model, "reporte/reportes.html.hbs");
  }

  public ModelAndView getReporteHcPorTipoOrganizacion(Request request, Response response) {
    if (!sessionValidator.tienePermisosAdmin(request)) {
      return new ModelAndView(null, "sinAcceso.html.hbs");
    }
    Map model = new HashMap();
    model = new SessionValidator().setearPermisosParaTemplate(model, request);
    return new ModelAndView(model, "reporte/reporte-por-tipo-organizacion.html.hbs");
  }

  public ModelAndView calcularHcPorTipoOrganizacion(Request request, Response response) {
    if (!sessionValidator.tienePermisosAdmin(request)) {
      return new ModelAndView(null, "sinAcceso.html.hbs");
    }
    String paramTipoDeOrganizacion = request.queryParams("tipoClasificacion");
    Map<String, Clasificacion> diccionarioClasificaciones = new HashMap<>();
    diccionarioClasificaciones.put("escuela", Clasificacion.ESCUELA);
    diccionarioClasificaciones.put("ministerio", Clasificacion.MINISTERIO);
    diccionarioClasificaciones.put("sectorPrimario", Clasificacion.SECTOR_PRIMARIO);
    diccionarioClasificaciones.put("sectorSecundario", Clasificacion.SECTOR_SECUNDARIO);
    diccionarioClasificaciones.put("universidad", Clasificacion.UNIVERSIDAD);
    Clasificacion clasificacion = diccionarioClasificaciones.get(paramTipoDeOrganizacion);
    List<OrganizacionVista> organizacionVistas =
        RepositorioOrganizaciones.instancia.getOrganizacionesPorClasificacion(
            clasificacion, TipoPeriodicidad.Anual, YearMonth.of(2022, 11)
        );
    float valorInicial = 0;
    float hcTotal = organizacionVistas.stream()
        .map(organizacionVista -> {
          return organizacionVista.hc;
        })
        .reduce(valorInicial, (subtotal, element) -> subtotal + element);
    Map model = new HashMap();
    model.put("organizacionVistas", organizacionVistas);
    model.put("hcTotal", hcTotal);
    model = new SessionValidator().setearPermisosParaTemplate(model, request);
    return new ModelAndView(model, "reporte/reporte-organizaciones-hc.html.hbs");
  }

  public ModelAndView getReporteHcPorSectorTerritorial(Request request, Response response) {
    if (!sessionValidator.tienePermisosAdmin(request)) {
      return new ModelAndView(null, "sinAcceso.html.hbs");
    }
    List<SectorTerritorial> sectoresTerritoriales =
        RepositorioSectorTerritorial.instancia.getSectorTerritoriales();
    Map model = new HashMap();
    model.put("sectoresTerritoriales", sectoresTerritoriales);
    model = new SessionValidator().setearPermisosParaTemplate(model, request);
    return new ModelAndView(model, "reporte/reporte-sectores-territoriales.html.hbs");
  }

  public ModelAndView calcularHcPorSectorTerritorial(
      Request request, Response response
  ) {
    if (!sessionValidator.tienePermisosAdmin(request)) {
      return new ModelAndView(null, "sinAcceso.html.hbs");
    }
    int sectorId = Integer.parseInt(request.queryParams("sectorTerritorial"));
    SectorTerritorial sectorTerritorial =
        RepositorioSectorTerritorial.instancia.get(sectorId);
    Map model = new HashMap();
    model.put("nombre", sectorTerritorial.getNombre());
    model.put("hc", sectorTerritorial.obtenerCalculoHC(
        TipoPeriodicidad.Anual, YearMonth.of(2022, 11),
        ServicioDistancia.getInstancia())
    );
    model = new SessionValidator().setearPermisosParaTemplate(model, request);
    return new ModelAndView(model, "reporte/reporte-sector-territorial.html.hbs");
  }

  public ModelAndView getReporteHcComposicionPorSectorTerritorial(
      Request request, Response response
  ) {
    if (!sessionValidator.tienePermisosAdmin(request)) {
      return new ModelAndView(null, "sinAcceso.html.hbs");
    }
    List<SectorTerritorial> sectoresTerritoriales =
        RepositorioSectorTerritorial.instancia.getSectorTerritoriales();
    Map model = new HashMap();
    model.put("sectoresTerritoriales", sectoresTerritoriales);
    model = new SessionValidator().setearPermisosParaTemplate(model, request);
    return new ModelAndView(model,
        "reporte/reporte-composicion-sectores-territoriales.html.hbs");
  }

  public ModelAndView calcularHcComposicionPorSectorTerritorial(
      Request request, Response response
  ) {
    if (!sessionValidator.tienePermisosAdmin(request)) {
      return new ModelAndView(null, "sinAcceso.html.hbs");
    }
    Long sectorId = Long.parseLong(request.queryParams("sectorTerritorial"));
    SectorTerritorial sectorTerritorial =
        RepositorioSectorTerritorial.instancia.get(sectorId.intValue());
    RepositorioReportes repositorioReportes = new RepositorioReportes();
    List<SectorTerritorialComposicion> sectoresTerritorialesComposicion =
        repositorioReportes.getHCsectorTerritorialComposicion(sectorId);
    String nombreSector = sectorTerritorial.getNombre();
    Map model = new HashMap();
    model.put("nombreSector", nombreSector);
    model.put("sectoresTerritorialesComposicion", sectoresTerritorialesComposicion);
    model = new SessionValidator().setearPermisosParaTemplate(model, request);
    return new ModelAndView(model,
        "reporte/reporte-composicion-sector-territorial.html.hbs");
  }

  public ModelAndView getReporteHcComposicionPorOrganizacion(
      Request request, Response response
  ) {
    if (!sessionValidator.tienePermisosAdmin(request)) {
      return new ModelAndView(null, "sinAcceso.html.hbs");
    }
    RepositorioOrganizaciones repositorioOrganizacion = new RepositorioOrganizaciones();
    List<OrganizacionVista> organizaciones =
        repositorioOrganizacion.listar().stream()
          .map(organizacion -> {
            return organizacion.convertirAOrganizacionVista(
                TipoPeriodicidad.Anual, YearMonth.of(2022, 11)
            );
          }).collect(Collectors.toList());
    Map model = new HashMap();
    model.put("organizaciones", organizaciones);
    model = new SessionValidator().setearPermisosParaTemplate(model, request);
    return new ModelAndView(model,
        "reporte/reporte-composicion-organizaciones.html.hbs");
  }

  public ModelAndView calcularReporteHcComposicionPorOrganizacion(
      Request request, Response response
  ) {
    if (!sessionValidator.tienePermisosAdmin(request)) {
      return new ModelAndView(null, "sinAcceso.html.hbs");
    }
    Long organizacionId = Long.parseLong(request.queryParams("organizacion"));
    RepositorioReportes repositorioReportes = new RepositorioReportes();
    List<OrganizacionComposicion> organizacionComposiciones =
        repositorioReportes.getHCorganizacionComposicion(organizacionId);
    String organizacion = organizacionComposiciones.get(0).getNombre();
    Map model = new HashMap();
    model.put("organizacion", organizacion);
    model.put("organizacionComposiciones", organizacionComposiciones);
    model = new SessionValidator().setearPermisosParaTemplate(model, request);
    return new ModelAndView(model,
        "reporte/reporte-composicion-organizacion.html.hbs");
  }

  public ModelAndView getReporteHcEvolucionPorSectorTerritorial(
      Request request, Response response
  ) {
    if (!sessionValidator.tienePermisosAdmin(request)) {
      return new ModelAndView(null, "sinAcceso.html.hbs");
    }
    List<SectorTerritorial> sectoresTerritoriales =
        RepositorioSectorTerritorial.instancia.getSectorTerritoriales();
    Map model = new HashMap();
    model.put("sectoresTerritoriales", sectoresTerritoriales);
    model = new SessionValidator().setearPermisosParaTemplate(model, request);
    return new ModelAndView(model,
        "reporte/reporte-evolucion-sectores-territoriales.html.hbs");
  }

  public ModelAndView calcularReporteHcEvolucionPorSectorTerritorial(
      Request request, Response response
  ) {
    if (!sessionValidator.tienePermisosAdmin(request)) {
      return new ModelAndView(null, "sinAcceso.html.hbs");
    }
    Long sectorId = Long.parseLong(request.queryParams("sectorTerritorial"));
    SectorTerritorial sectorTerritorial =
        RepositorioSectorTerritorial.instancia.get(sectorId.intValue());
    RepositorioReportes repositorioReportes = new RepositorioReportes();
    List<EvolucionHc> sectoresTerritorialesEvolucion =
        repositorioReportes.getReportSectorTerritorialAnual(sectorId, 2016, 2022);
    String nombreSector = sectorTerritorial.getNombre();
    Map model = new HashMap();
    model.put("nombreSector", nombreSector);
    model.put("sectoresTerritorialesEvolucion", sectoresTerritorialesEvolucion);
    model = new SessionValidator().setearPermisosParaTemplate(model, request);
    return new ModelAndView(model,
        "reporte/reporte-evolucion-sector-territorial.html.hbs");
  }

  public ModelAndView getReporteHcEvolucionPorOrganizacion(
      Request request, Response response
  ) {
    if (!sessionValidator.tienePermisosAdmin(request)) {
      return new ModelAndView(null, "sinAcceso.html.hbs");
    }
    RepositorioOrganizaciones repositorioOrganizacion = new RepositorioOrganizaciones();
    List<OrganizacionVista> organizaciones =
        repositorioOrganizacion.listar().stream()
            .map(organizacion -> {
              return organizacion.convertirAOrganizacionVista(
                  TipoPeriodicidad.Anual, YearMonth.of(2022, 11)
              );
            }).collect(Collectors.toList());
    Map model = new HashMap();
    model.put("organizaciones", organizaciones);
    model = new SessionValidator().setearPermisosParaTemplate(model, request);
    return new ModelAndView(model,
        "reporte/reporte-evolucion-organizaciones.html.hbs");
  }

  public ModelAndView calcularReporteHcEvolucionPorOrganizacion(
      Request request, Response response
  ) {
    if (!sessionValidator.tienePermisosAdmin(request)) {
      return new ModelAndView(null, "sinAcceso.html.hbs");
    }
    RepositorioOrganizaciones repositorioOrganizaciones =
        new RepositorioOrganizaciones();
    Long organizacionId = Long.parseLong(request.queryParams("organizacion"));
    Organizacion organizacion = repositorioOrganizaciones.buscar(organizacionId);
    RepositorioReportes repositorioReportes = new RepositorioReportes();
    List<EvolucionHc> organizacionEvoluciones =
        repositorioReportes.getReportOrganizacionAnual(
            organizacionId, 2016, 2022
        );
    String nombreOrganizacion = organizacion.getRazonSocial();
    Map model = new HashMap();
    model.put("organizacion", nombreOrganizacion);
    model.put("organizacionEvoluciones", organizacionEvoluciones);
    model = new SessionValidator().setearPermisosParaTemplate(model, request);
    return new ModelAndView(model,
        "reporte/reporte-evolucion-organizacion.html.hbs");
  }
}
