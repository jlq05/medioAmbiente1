package controllers;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import model.DatosActividad;
import model.Organizacion;
import model.ProcesadorCsv;
import model.TipoConsumo;
import model.TipoPeriodicidad;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import repositories.RepositorioDatosActividad;
import repositories.RepositorioTipoDeConsumo;
import repositories.RepositorioTrayecto;
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


  public ModelAndView cargarArchivo(Request request, Response response)
      throws IOException, ServletException {
    if (!sessionValidator.tienePermisosAdmin(request)) {
      return new ModelAndView(null, "sinAcceso.html.hbs");
    }
    
    String path = "archivoCargado.csv";
    request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
    InputStream is = request.raw().getPart("archivoParaSubir").getInputStream();
    File archivoASubir = new File(path);
    FileUtils.copyInputStreamToFile(is, archivoASubir);
    int organizacionId = request.session().attribute("organizacion_id");
    long longOrganizacionId = organizacionId;
    Organizacion organizacion = getOrganizacionBy(longOrganizacionId);
    List<DatosActividad> actividades =  organizacion.cargarConsumos(archivoASubir);
    withTransaction(() -> {
      organizacion.actividades.addAll(actividades);
    });
    response.redirect("/home/medicion");
    return null;
  }

  public ModelAndView crearMedicion(Request request, Response response)
      throws ServletException, IOException {

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

  public Organizacion getOrganizacionBy(Long idOrganizacion) {
    List<Organizacion> organizaciones =  entityManager()
        .createQuery("from Organizacion where id = :id")
        .setParameter("id", idOrganizacion)
        .getResultList();
    return organizaciones.stream().findFirst().get();
  }

}

