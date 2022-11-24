package main;

import controllers.HuellaController;
import controllers.LoginSinRegistrarseController;
import controllers.MedicionController;
import controllers.RecomendacionController;
import controllers.ReportesController;
import controllers.SectoresController;
import controllers.SinAccesoController;
import controllers.SolicitudesController;
import controllers.TrayectoController;
import controllers.VinculacionController;
import javax.persistence.PersistenceException;
import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;
import spark.Spark;
import spark.debug.DebugScreen;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class Routes {
  public static void main(String[] args) {
    Spark.port(getHerokuAssignedPort());
    Spark.staticFileLocation("/path/to/sources");
    System.out.println("Iniciando el servidor...");
    Spark.port(9001);
    Spark.staticFileLocation("/public");

    System.out.println("Corriendo bootstrap...");
    new Bootstrap().run();
    //Spark.get("/", ((request, response) -> "hola mundo"));

    DebugScreen.enableDebugScreen();

    HandlebarsTemplateEngine engine = new HandlebarsTemplateEngine();

    System.out.println("Servidor iniciado!");

    //Parte del login
    HomeController homeController = new HomeController();
    Spark.get("/", homeController::getHome, engine);

    LoginSinRegistrarseController loginSinRegistrarseController =
        new LoginSinRegistrarseController();
    Spark.path("/loginUsuarioSinLogueo", () -> {
      Spark.get("", loginSinRegistrarseController::lectura, engine);
      Spark.post("", loginSinRegistrarseController::crearSesion);
      Spark.post("/logout", loginSinRegistrarseController::cerrarSesion);
    });

    //Menu de opciones de la aplicacion
    TrayectoController trayectoController = new TrayectoController();
    VinculacionController vinculacionController = new VinculacionController();
    SectoresController sectoresController = new SectoresController();
    Spark.path("/", () -> {
      Spark.get("/trayecto", trayectoController::lectura, engine);
      Spark.get("/:organizacion_id/trayecto", trayectoController::lectura, engine);
      Spark.get("/trayecto/nuevo", trayectoController::nuevo, engine);
      Spark.get("/trayecto/tramo/nuevo", trayectoController::tramonuevo, engine);
      Spark.get("/vinculacion", vinculacionController::lectura, engine);
      Spark.post("/vinculacion", vinculacionController::buscar);
      Spark.get("/:organizacion_id/sectores/vinculacion", sectoresController::listar, engine);
      Spark.post("/sectores/vinculacion", sectoresController::vincularse);
    });

    HuellaController huellaController = new HuellaController();

    //Spark.get("/:organizacion_id/huella", huellaController::lectura, engine);
    SolicitudesController solicitudesController = new SolicitudesController();
    MedicionController medicionController = new MedicionController();
    ReportesController reportesController = new ReportesController();
    SinAccesoController sinAccesoController = new SinAccesoController();
    Spark.path("/", () -> {
      Spark.get("/huella", huellaController::lectura, engine);
      Spark.get("/:organizacion_id/solicitudes", solicitudesController::lectura, engine);
      Spark.post("/:organizacion_id/solicitudes", solicitudesController::vincular);
      Spark.post("/sectores/vinculacion", sectoresController::vincularse);
      Spark.get("/solicitudes", solicitudesController::lectura, engine);
      Spark.get("/medicion", medicionController::nuevo, engine);
      Spark.get("/nuevaMedicion", medicionController::nuevo2, engine);
      Spark.get("/reportes", reportesController::lectura, engine);
      Spark.get("/reportes", reportesController::lectura, engine);
      Spark.get("/sinAcceso", sinAccesoController::lectura, engine);
    });

    RecomendacionController recomendacionController = new RecomendacionController();
    Spark.get("/recomendaciones", recomendacionController::lectura, engine);

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
  
  public static int getHerokuAssignedPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
  }
}
