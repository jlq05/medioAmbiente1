package controllers;

import domain.services.distancia.ServicioDistancia;
import domain.services.distancia.entities.Ubicacion;
import java.io.IOException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import model.Bicicleta;
import model.Miembro;
import model.Pie;
import model.Sector;
import model.Tramo;
import model.Trayecto;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import repositories.RepositorioSectores;
import repositories.RepositorioTrayecto;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import utils.SessionValidator;
import view.TramoVista;
import view.TrayectoVista;

public class TrayectoController implements WithGlobalEntityManager, TransactionalOps {
  boolean isTesting = true;
  SessionValidator sessionValidator = new SessionValidator();

  public TrayectoController() {
  }

  public boolean procesarPermisos(Request request, Response response) {
    if (!sessionValidator.tienePermisosUsuario(request) && !isTesting) {
      response.redirect("/loginUsuarioSinLogueo");
      return false;
    }
    return true;
    //return new ModelAndView(null, "sinAcceso.html.hbs");
  }

  public Miembro getMiembroSeleccionado(Request request) {
    RepositorioSectores repositorioSectores = new RepositorioSectores();
    int sectorId = Integer.parseInt(request.params(":sectorId"));
    int usuarioId = request.session().attribute("user_id");
    Sector sector = repositorioSectores.buscar(sectorId);
    Miembro miembroSeleccionado = sector.getMiembros().stream().filter(miembro -> {
      return miembro.getPersona().getId() == usuarioId;
    }).collect(Collectors.toList()).get(0);
    return miembroSeleccionado;
  }

  public ModelAndView getTrayectos(Request request, Response response) {
    if (!procesarPermisos(request, response)) {
      //return new ModelAndView(null, "sinAcceso.html.hbs");
      return null;
    }
    int sectorId = Integer.parseInt(request.params(":sectorId"));
    Miembro miembroSeleccionado = getMiembroSeleccionado(request);
    List<Trayecto> trayectos = miembroSeleccionado.getTrayectos();
    List<TrayectoVista> trayectosVista = trayectos.stream().map(trayecto -> {
      return trayecto.convertirATrayectoVista();
    }).collect(Collectors.toList());
    Map model = new HashMap();
    model.put("trayectos",  trayectosVista);
    model.put("sectorId",  sectorId);
    model = new SessionValidator().setearPermisosParaTemplate(model, request);
    return new ModelAndView(model, "trayecto/trayectos.html.hbs");
  }

  public ModelAndView getTrayecto(Request request, Response response) {
    if (!procesarPermisos(request, response)) {
      return null;
    }
    Miembro miembroSeleccionado = getMiembroSeleccionado(request);
    int trayectoId = Integer.parseInt(request.params(":trayectoId"));
    Trayecto trayecto = miembroSeleccionado.getTrayectos().stream().filter(item -> {
      return item.getId() == trayectoId;
    }).findFirst().get();
    List<TramoVista> tramos = trayecto.getTramos()
        .stream()
        .filter(tramo -> {
          return tramo.getMedioDeTransporte() != null;
        })
        .map(tramo -> {
          try {
            return tramo.convertirATramoVista();
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
      ).collect(Collectors.toList());
    Map model = new HashMap();
    model.put("periodicidadDeImputacion",  trayecto.getPeriodicidadDeImputacion());
    model.put("tramos",  tramos);
    model = new SessionValidator().setearPermisosParaTemplate(model, request);
    return new ModelAndView(model, "trayecto/trayecto-detalle.html.hbs");
  }

  public ModelAndView getFormularioTrayecto(Request request, Response response) {
    if (!procesarPermisos(request, response)) {
      return null;
    }
    int sectorId = Integer.parseInt(request.params(":sectorId"));
    Map model = new HashMap();
    model.put("sectorId",  sectorId);
    model = new SessionValidator().setearPermisosParaTemplate(model, request);
    return new ModelAndView(model, "trayecto/trayecto-nuevo.html.hbs");
  }

  public ModelAndView getFormularioTramo(Request request, Response response) {
    if (!procesarPermisos(request, response)) {
      return null;
    }
    int sectorId = Integer.parseInt(request.params(":sectorId"));
    int trayectoId = Integer.parseInt(request.params(":trayectoId"));
    Map model = new HashMap();
    model.put("sectorId",  sectorId);
    model.put("trayectoId",  trayectoId);
    model = new SessionValidator().setearPermisosParaTemplate(model, request);
    return new ModelAndView(model, "trayecto/tramo-nuevo.html.hbs");
  }

  public ModelAndView crearTrayecto(Request request, Response response) {
    if (!procesarPermisos(request, response)) {
      return null;
    }
    final int sectorId = Integer.parseInt(request.params(":sectorId"));
    String periodicidadDeImputacion =
        (request.queryParams("periodicidadDeImputacion")).substring(0, 7);
    periodicidadDeImputacion =
        periodicidadDeImputacion.substring(2, periodicidadDeImputacion.length());
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yy-MM");
    YearMonth date = YearMonth.parse(periodicidadDeImputacion, dateTimeFormatter);

    Trayecto trayecto = new Trayecto(new ArrayList<>());
    trayecto.setPeriodicidadDeImputacion(date);

    Miembro miembroSeleccionado = getMiembroSeleccionado(request);
    withTransaction(() -> {
      RepositorioTrayecto.instancia.agregar(trayecto);
      miembroSeleccionado.agregarTrayecto(trayecto);
    });

    String urlForCreatingSector =
        "/home/sector/" + sectorId + "/trayecto/" +  trayecto.getId() + "/tramo/nuevo";
    response.redirect(urlForCreatingSector);
    return null;
  }

  public ModelAndView crearTramo(Request request, Response response) {
    if (!procesarPermisos(request, response)) {
      return null;
    }
    final int sectorId = Integer.parseInt(request.params(":sectorId"));
    final int trayectoId = Integer.parseInt(request.params(":trayectoId"));
    final String tipoDeTransporte = request.queryParams("tipoDeTransporte");
    Trayecto trayecto = RepositorioTrayecto.instancia.get(trayectoId);
    Ubicacion origen = new Ubicacion(
        Integer.parseInt(request.queryParams("localidadOrigen")),
        request.queryParams("calleOrigen"),
        Integer.parseInt(request.queryParams("alturaOrigen"))
    );
    Ubicacion destino = new Ubicacion(
        Integer.parseInt(request.queryParams("localidadDestino")),
        request.queryParams("calleDestino"),
        Integer.parseInt(request.queryParams("alturaDestino"))
    );
    Tramo tramo = new Tramo(origen, destino);
    if (tipoDeTransporte.equals("pie")) {
      tramo.setMedioDeTransporte(new Pie(ServicioDistancia.getInstancia()));
    }
    if (tipoDeTransporte.equals("bicicleta")) {
      tramo.setMedioDeTransporte(new Bicicleta(ServicioDistancia.getInstancia()));
    }
    withTransaction(() -> {
      RepositorioTrayecto.instancia.agregarTramo(tramo);
      trayecto.agregarTramo(tramo);
    });
    String urlARedigir = "/home/sector/" + sectorId + "/trayecto/"
        + trayectoId + "/tramo/" + tramo.getId() + "/" + tipoDeTransporte;
    response.redirect(urlARedigir);
    return null;
  }
}
