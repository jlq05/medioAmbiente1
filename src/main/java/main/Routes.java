package main;

import controllers.HuellaController;
import controllers.LoginController;
import controllers.MedicionController;
import controllers.RecomendacionController;
import controllers.ReportesController;
import controllers.SectorController;
import controllers.SinAccesoController;
import controllers.SolicitudesController;
import controllers.TramoController;
import controllers.TrayectoController;
import controllers.VinculacionController;
import javax.persistence.PersistenceException;
import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;
import spark.Spark;
import spark.debug.DebugScreen;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class Routes {
  static void procesarTrayectos(HandlebarsTemplateEngine engine) {
    SectorController sectorController = new SectorController();
    Spark.get("home/sectoresPorUsuario", sectorController::getSectoresPorUsuarioId, engine);

    TrayectoController trayectoController = new TrayectoController();
    TramoController tramoController = new TramoController();
    Spark.get("home/sector/:sectorId/trayectos", trayectoController::getTrayectos, engine);
    Spark.get(
        "home/sector/:sectorId/trayecto/:trayectoId",
        trayectoController::getTrayecto, engine);
    Spark.get("home/sector/:sectorId/trayecto-nuevo",
        trayectoController::getFormularioTrayecto, engine
    );
    Spark.get("/home/sector/:sectorId/trayecto/:trayectoId/tramo/nuevo",
        trayectoController::getFormularioTramo, engine
    );
    Spark.post("/home/sector/:sectorId/trayecto", trayectoController::crearTrayecto, engine);
    Spark.post("/home/sector/:sectorId/trayecto/:trayectoId/tramo",
        trayectoController::crearTramo, engine
    );
    Spark.get("/home/sector/:sectorId/trayecto/:trayectoId/tramo/:tramoId/pie",
        tramoController::getFormularioSiguienteTramo, engine
    );
    Spark.get("/home/sector/:sectorId/trayecto/:trayectoId/tramo/:tramoId/bicicleta",
        tramoController::getFormularioSiguienteTramo, engine
    );
    Spark.get("/home/sector/:sectorId/trayecto/:trayectoId/tramo/:tramoId/siguiente",
        tramoController::getFormularioSiguienteTramo, engine
    );
    Spark.get("/home/sector/:sectorId/trayecto/:trayectoId/tramo/:tramoId/transportePublico",
        tramoController::getFormularioTramoParaTransportePublico, engine
    );
    Spark.get("/home/sector/:sectorId/trayecto/:trayectoId/tramo/:tramoId/automovil",
        tramoController::getFormularioTramoParaAutomovil, engine
    );
    Spark.post("/home/sector/:sectorId/trayecto/:trayectoId/tramo/:tramoId/transportePublico",
        tramoController::actualizarTramoPorTranportePublico, engine
    );
    Spark.post("/home/sector/:sectorId/trayecto/:trayectoId/tramo/:tramoId/automovil",
        tramoController::actualizarTramoPorAutomovil, engine
    );
  }

  public static void main(String[] args) {
    System.out.println("Inicio");
    int puerto = 8080;//Integer.parseInt(args[0]);;
    Spark.port(puerto);
    System.out.println("Iniciando el servidor...");
    Spark.get("/", (req, res) -> "Hello Heroku World");
  }

  public static void main2(String[] args) {
    System.out.println("Pepe");
    int puerto = Integer.parseInt(args[0]);
    Spark.port(puerto);
    System.out.println("Iniciando el servidor...");
    System.out.println(puerto);
    Spark.staticFileLocation("/public");

    //System.out.println("Corriendo bootstrap...");
    //new Bootstrap().run();

    DebugScreen.enableDebugScreen();
    HandlebarsTemplateEngine engine = new HandlebarsTemplateEngine();
    System.out.println("Servidor iniciado!");

    //Parte del login
    HomeController homeController = new HomeController();
    Spark.get("/", homeController::getHome, engine);
    LoginController loginSinRegistrarseController =
        new LoginController();
    Spark.path("/loginUsuarioSinLogueo", () -> {
      Spark.get("", loginSinRegistrarseController::lectura, engine);
      Spark.post("", loginSinRegistrarseController::crearSesion);
      Spark.post("/logout", loginSinRegistrarseController::cerrarSesion);
    });

    //Menu de opciones de la aplicacion
    VinculacionController vinculacionController = new VinculacionController();
    SectorController sectorController = new SectorController();
    SolicitudesController solicitudesController = new SolicitudesController();
    MedicionController medicionController = new MedicionController();
    ReportesController reportesController = new ReportesController();
    SinAccesoController sinAccesoController = new SinAccesoController();
    HuellaController huellaController = new HuellaController();
    RecomendacionController recomendacionController = new RecomendacionController();
    Spark.path("/home", () -> {
      //Perfil usuario
      Spark.get("/vinculacion", vinculacionController::lectura, engine);
      Spark.post("/vinculacion", vinculacionController::buscar);
      Spark.get("/:organizacion_id/sectores/vinculacion", sectorController::listar, engine);
      Spark.post("/sectores/vinculacion", sectorController::vincularse);
      Spark.get("/:organizacion_id/solicitudes", solicitudesController::lectura, engine);
      Spark.post("/:organizacion_id/solicitudes", solicitudesController::vincular);
      //Perfil administrador
      //Spark.get("/:organizacion_id/huella", huellaController::lectura, engine);
      Spark.get("/huella", huellaController::lectura, engine);
      Spark.get("/solicitudes", solicitudesController::lectura, engine);
      Spark.post("/solicitudes", solicitudesController::vincular);
      Spark.get("/medicion", medicionController::getOpcionesDeRegistroDeMedicion, engine);
      Spark.get("/medicion/nuevo", medicionController::getFormularioMedicion, engine);
      Spark.get("/medicion/nuevo", medicionController::getFormularioMedicion, engine);
      Spark.get("/medicion/archivo", medicionController::getPantallaCargarArchivo, engine);
      // Spark.get("/medicion/archivo", medicionController::cargarArchivo, engine);
      Spark.post("/medicion", medicionController::crearMedicion, engine);
      Spark.path("/reportes", () -> {
        Spark.get("", reportesController::getOpcionesDeReportes, engine);
        Spark.get("/hc-por-tipo-organizacion",
            reportesController::getReporteHcPorTipoOrganizacion, engine);
        Spark.post("/hc-por-tipo-organizacion",
            reportesController::calcularHcPorTipoOrganizacion, engine);
        Spark.get("/hc-por-sector-territorial",
            reportesController::getReporteHcPorSectorTerritorial, engine);
        Spark.post("/hc-por-sector-territorial",
            reportesController::calcularHcPorSectorTerritorial, engine);
        Spark.get("/hc-composicion-sector-territorial",
            reportesController::getReporteHcComposicionPorSectorTerritorial, engine);
        Spark.post("/hc-composicion-sector-territorial",
            reportesController::calcularHcComposicionPorSectorTerritorial, engine);
        Spark.get("/hc-composicion-organizacion",
            reportesController::getReporteHcComposicionPorOrganizacion, engine);
        Spark.post("/hc-composicion-organizacion",
            reportesController::calcularReporteHcComposicionPorOrganizacion, engine);
        Spark.get("/hc-evolucion-sector-territorial",
            reportesController::getReporteHcEvolucionPorSectorTerritorial, engine);
        Spark.post("/hc-evolucion-sector-territorial",
            reportesController::calcularReporteHcEvolucionPorSectorTerritorial, engine);
        Spark.get("/hc-evolucion-organizacion",
            reportesController::getReporteHcEvolucionPorOrganizacion, engine);
        Spark.post("/hc-evolucion-organizacion",
            reportesController::calcularReporteHcEvolucionPorOrganizacion, engine);
      });
      
      Spark.get("/sinAcceso", sinAccesoController::lectura, engine);
      Spark.get("/recomendaciones", recomendacionController::lectura, engine);
    });
    procesarTrayectos(engine);
    Spark.before((request, response) -> {
      PerThreadEntityManagers.getEntityManager().clear();
    });

    Spark.before("/public", (request, response) -> {
      if (!request.pathInfo().startsWith("/loginUsuarioSinLogueo")
          && request.session().attribute("user_id") == null) {
        response.redirect("/");
      }
    });

    Spark.exception(PersistenceException.class, (e, request, response) -> {
      response.redirect("/500");
    });
  }
}
