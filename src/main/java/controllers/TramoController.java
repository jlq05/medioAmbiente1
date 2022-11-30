package controllers;

import domain.services.distancia.ServicioDistancia;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.TipoConsumo;
import model.TipoVehiculo;
import model.Tramo;
import model.TransportePublico;
import model.Vehiculo;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import repositories.RepositorioTipoDeConsumo;
import repositories.RepositorioTransportePublico;
import repositories.RepositorioTrayecto;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import utils.SessionValidator;

public class TramoController implements WithGlobalEntityManager, TransactionalOps {
  boolean isTesting = true;
  SessionValidator sessionValidator = new SessionValidator();

  public TramoController() {
  }

  public boolean procesarPermisos(Request request, Response response) {
    if (!sessionValidator.tienePermisosUsuario(request) && !isTesting) {
      response.redirect("/loginUsuarioSinLogueo");
      return false;
    }
    return true;
  }

  public ModelAndView getFormularioSiguienteTramo(Request request, Response response) {
    if (!procesarPermisos(request, response)) {
      return null;
    }
    int sectorId = Integer.parseInt(request.params(":sectorId"));
    int trayectoId = Integer.parseInt(request.params(":trayectoId"));
    Map model = new HashMap();
    model.put("sectorId",  sectorId);
    model.put("trayectoId",  trayectoId);
    model = new SessionValidator().setearPermisosParaTemplate(model, request);
    return new ModelAndView(model, "trayecto/tramo-siguiente.html.hbs");
  }

  public ModelAndView getFormularioTramoParaTransportePublico(Request request, Response response) {
    if (!procesarPermisos(request, response)) {
      return null;
    }
    int sectorId = Integer.parseInt(request.params(":sectorId"));
    int trayectoId = Integer.parseInt(request.params(":trayectoId"));
    int tramoId = Integer.parseInt(request.params(":tramoId"));
    List<TipoConsumo> tiposConsumo = RepositorioTipoDeConsumo.instancia.getTiposDeConsumo();
    List<TransportePublico> transportesPublicos =
        RepositorioTransportePublico.instancia.getTransportesPublicos();
    Map model = new HashMap();
    model.put("sectorId",  sectorId);
    model.put("trayectoId",  trayectoId);
    model.put("tramoId",  tramoId);
    model.put("tiposConsumo",  tiposConsumo);
    model.put("transportesPublicos",  transportesPublicos);
    model = new SessionValidator().setearPermisosParaTemplate(model, request);
    return new ModelAndView(model, "trayecto/tramo-transporte-publico.html.hbs");
  }

  public ModelAndView getFormularioTramoParaAutomovil(Request request, Response response) {
    if (!procesarPermisos(request, response)) {
      return null;
    }
    int sectorId = Integer.parseInt(request.params(":sectorId"));
    int trayectoId = Integer.parseInt(request.params(":trayectoId"));
    int tramoId = Integer.parseInt(request.params(":tramoId"));
    List<TipoConsumo> tiposConsumo = RepositorioTipoDeConsumo.instancia.getTiposDeConsumo();
    Map model = new HashMap();
    model.put("sectorId",  sectorId);
    model.put("trayectoId",  trayectoId);
    model.put("tramoId",  tramoId);
    model.put("tiposConsumo",  tiposConsumo);
    model = new SessionValidator().setearPermisosParaTemplate(model, request);
    return new ModelAndView(model, "trayecto/tramo-automovil.html.hbs");
  }

  public ModelAndView actualizarTramoPorAutomovil(Request request, Response response) {
    final int sectorId = Integer.parseInt(request.params(":sectorId"));
    final int trayectoId = Integer.parseInt(request.params(":trayectoId"));
    int tramoId = Integer.parseInt(request.params(":tramoId"));
    int tipoConsumoId = Integer.parseInt(request.queryParams("tipoDeConsumo"));
    float consumoCombustiblePorKm = Float.parseFloat(request.queryParams("consumoCombustible"));
    String tipoVehiculoForm = request.queryParams("tipoDeAuto");
    boolean esPrivado = request.queryParams("esPrivado") != null;
    TipoVehiculo tipoVehiculo;
    switch (tipoVehiculoForm) {
      case "auto":
        tipoVehiculo = TipoVehiculo.AUTO;
        break;
      case "moto":
        tipoVehiculo = TipoVehiculo.MOTO;
        break;
      case "camioneta":
        tipoVehiculo = TipoVehiculo.CAMIONETA;
        break;
      default:
        tipoVehiculo = TipoVehiculo.SERVICIOCONTRATADO;
        break;
    }
    Tramo tramo = RepositorioTrayecto.instancia.getTramo(tramoId);
    TipoConsumo tipoConsumo = RepositorioTipoDeConsumo.instancia.get(tipoConsumoId);
    Vehiculo vehiculo = new Vehiculo(
        tipoVehiculo, tipoConsumo, esPrivado,
        ServicioDistancia.getInstancia()
    );
    vehiculo.setConsumoDeCombustiblePorKm(consumoCombustiblePorKm);
    withTransaction(() -> {
      tramo.setMedioDeTransporte(vehiculo);
    });
    String urlARedigir = "/home/sector/" + sectorId + "/trayecto/"
        + trayectoId + "/tramo/" + tramoId + "/siguiente";
    response.redirect(urlARedigir);
    return null;
  }

  public ModelAndView actualizarTramoPorTranportePublico(Request request, Response response) {
    final int sectorId = Integer.parseInt(request.params(":sectorId"));
    final int trayectoId = Integer.parseInt(request.params(":trayectoId"));
    int tramoId = Integer.parseInt(request.params(":tramoId"));
    int transportePublicoId = Integer.parseInt(request.queryParams("transportePublico"));
    TransportePublico transportePublico =
        RepositorioTransportePublico.instancia.buscar(transportePublicoId);
    Tramo tramo = RepositorioTrayecto.instancia.getTramo(tramoId);
    withTransaction(() -> {
      tramo.setMedioDeTransporte(transportePublico);
    });
    String urlARedigir = "/home/sector/" + sectorId + "/trayecto/"
        + trayectoId + "/tramo/" + tramoId + "/siguiente";
    response.redirect(urlARedigir);
    return null;
  }
}
