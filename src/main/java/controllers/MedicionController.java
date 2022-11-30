package controllers;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.DatosActividad;
import model.TipoConsumo;
import model.TipoPeriodicidad;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import repositories.RepositorioDatosActividad;
import repositories.RepositorioTipoDeConsumo;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import utils.SessionValidator;

public class MedicionController implements WithGlobalEntityManager, TransactionalOps {
  SessionValidator sessionValidator = new SessionValidator();

  public ModelAndView getFormularioMedicion(Request request, Response response) {
    if (!sessionValidator.tienePermisosAdmin(request)) {
      return new ModelAndView(null, "sinAcceso.html.hbs");
    }
    List<TipoConsumo> tiposConsumo = RepositorioTipoDeConsumo.instancia.getTiposDeConsumo();
    Map model = new HashMap();
    model.put("tiposConsumo",  tiposConsumo);
    model = new SessionValidator().setearPermisosParaTemplate(model, request);
    return new ModelAndView(model, "medicion-nuevo.html.hbs");
  }

  public ModelAndView getOpcionesDeRegistroDeMedicion(Request request, Response response) {
    if (!sessionValidator.tienePermisosAdmin(request)) {
      return new ModelAndView(null, "sinAcceso.html.hbs");
    }
    Map model = new HashMap();
    model = new SessionValidator().setearPermisosParaTemplate(model, request);
    return new ModelAndView(model, "medicion-opciones.html.hbs");
  }



  public ModelAndView getPantallaCargarArchivo(Request request, Response response) {
    if (!sessionValidator.tienePermisosAdmin(request)) {
      return new ModelAndView(null, "sinAcceso.html.hbs");
    }
    Map model = new HashMap();
    model = new SessionValidator().setearPermisosParaTemplate(model, request);
    return new ModelAndView(model, "medicion-archivo.html.hbs");
  }




  public ModelAndView crearMedicion(Request request, Response response) {
    if (!sessionValidator.tienePermisosAdmin(request)) {
      return new ModelAndView(null, "sinAcceso.html.hbs");
    }
    int tipoConsumoId = Integer.parseInt(request.queryParams("tipoDeConsumo"));
    TipoConsumo tipoConsumo = RepositorioTipoDeConsumo.instancia.get(tipoConsumoId);
    float valorDatosActividad = Float.parseFloat(request.queryParams("valorDatosActividad"));
    TipoPeriodicidad tipoPeriodicidad = request.queryParams("tipoDePeriodicidad").equals("anual")
        ? TipoPeriodicidad.Anual : TipoPeriodicidad.Mensual;
    YearMonth periodicidad = YearMonth.of(2022, 6); //TODO: deshardcodearlo

    DatosActividad datosActividad = new DatosActividad(
        tipoConsumo, valorDatosActividad, tipoPeriodicidad, periodicidad
    );
    withTransaction(() -> {
      RepositorioDatosActividad.instancia.agregar(datosActividad);
    });
    response.redirect("/home/medicion");
    return null;
  }
}

