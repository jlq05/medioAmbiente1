package controllers;

import domain.services.distancia.ServicioDistancia;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Intervalo;
import model.Miembro;
import model.Organizacion;
import model.Persona;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import repositories.RepositorioMiembros;
import repositories.RepositorioOrganizaciones;
import repositories.RepositorioUsuarios;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import utils.SessionValidator;

public class HuellaController implements WithGlobalEntityManager, TransactionalOps {

  SessionValidator sessionValidator = new SessionValidator();

  public ModelAndView lectura(Request request, Response response) {

    if (!sessionValidator.estaLogueado(request)) {
      response.redirect("/loginUsuarioSinLogueo");
      return null;
    } else {
      if (sessionValidator.estaLogueado(request)
            && sessionValidator.tienePermisosAdmin(request)) {
        Map<String, Object> modelo = new HashMap<>();

        int organizacionId = request.session().attribute("organizacion_id");

        Organizacion organizacion = RepositorioOrganizaciones
            .instancia
            .buscar(organizacionId);

        float hcTrayecto = organizacion
            .obtenerCalculoHcPorTrayectos(ServicioDistancia.getInstancia());

        float hcActividad = organizacion.obtenerCalculoHcPorActividades(
            new Intervalo(YearMonth.of(1994, 5), YearMonth.now()));

        float hcTotal = hcTrayecto + hcActividad;

        modelo.put("hcTrayecto", hcTrayecto);
        modelo.put("hcActividad", hcActividad);
        modelo.put("hcTotal", hcTotal);
        modelo.put("razonSocial", organizacion.getRazonSocial());
        modelo = new SessionValidator().setearPermisosParaTemplate(modelo, request);
        return new ModelAndView(modelo, "huella.html.hbs");
      } else if (sessionValidator.estaLogueado(request)
          && sessionValidator.tienePermisosUsuario(request)) {

        Map<String, Object> modelo = new HashMap<>();

        int usuarioId = request.session().attribute("user_id");
        Persona usuario = RepositorioUsuarios.instance().buscar(usuarioId);

        List<Miembro> miembros = RepositorioMiembros.instancia.miembrosPorUsuario(usuarioId);

        float sumaTotal = (float) miembros.stream().mapToDouble(
            miembro -> {
              return miembro.obtenerCalculoHC(ServicioDistancia.getInstancia());
            })
            .sum();


        modelo.put("nombre", usuario.getNombre());
        modelo.put("apellido", usuario.getApellido());
        modelo.put("totalHC", sumaTotal);
        modelo = new SessionValidator().setearPermisosParaTemplate(modelo, request);
        return new ModelAndView(modelo, "huellaPersonal.html.hbs");
      } else {
        return new ModelAndView(null, "sinAcceso.html.hbs");
      }
    }
  }

  public ModelAndView huellaUsuario(Request request, Response response) {

    if (!sessionValidator.estaLogueado(request)) {
      response.redirect("/loginUsuarioSinLogueo");
      return null;
    } else {
      if (sessionValidator.estaLogueado(request)
          && sessionValidator.tienePermisosAdmin(request)) {
        Map<String, Object> modelo = new HashMap<>();

        int usuarioId = request.session().attribute("user_id");
        Persona usuario = RepositorioUsuarios.instance().buscar(usuarioId);

        modelo.put("nombre", usuario.getNombre());
        modelo.put("apellido", usuario.getApellido());

        return new ModelAndView(modelo, "huellaPersonal.html.hbs");
      } else {
        return new ModelAndView(null, "sinAcceso.html.hbs");
      }
    }
  }
}
