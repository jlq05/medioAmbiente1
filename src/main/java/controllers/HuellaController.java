package controllers;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import model.Intervalo;
import model.Organizacion;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import repositories.RepositorioOrganizaciones;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import utils.SessionValidator;

public class HuellaController implements WithGlobalEntityManager, TransactionalOps {

  public ModelAndView lectura(Request request, Response response) {

    SessionValidator sessionValidator = new SessionValidator();

    if (!sessionValidator.estaLogueado(request)) {
      response.redirect("/loginUsuarioSinLogueo");
      return null;
    } else {
      if (sessionValidator.estaLogueado(request)
            && sessionValidator.tienePermisosAdmin(request)) {
        Map<String, Object> modelo = new HashMap<>();
        String id = request.params(":organizacion_id");
        Organizacion organizacion = RepositorioOrganizaciones.instancia.buscar(Long.parseLong(id));
        float hcTrayecto = organizacion.obtenerCalculoHcPorTrayectos();
        float hcActividad = organizacion.obtenerCalculoHcPorActividades(
            new Intervalo(YearMonth.of(1994, 5), YearMonth.now()));
        float hcTotal = hcTrayecto + hcActividad;

        modelo.put("hcTrayecto", hcTrayecto);
        modelo.put("hcActividad", hcActividad);
        modelo.put("hcTotal", hcTotal);
        modelo.put("razonSocial", organizacion.getRazonSocial());
        return new ModelAndView(modelo, "huella.html.hbs");
      } else {
        return new ModelAndView(null, "sinAcceso.html.hbs");
      }
    }
  }
}
