package controllers;

import enums.Rol;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Miembro;
import model.Persona;
import model.Sector;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import repositories.RepositorioMiembros;
import repositories.RepositorioOrganizaciones;
import repositories.RepositorioSectores;
import repositories.RepositorioUsuarios;
import spark.ModelAndView;
import spark.Request;
import spark.Response;


public class SectorController implements WithGlobalEntityManager, TransactionalOps {

  public ModelAndView getSectoresPorUsuarioId(Request request, Response response) {
    int organizacionId = request.session().attribute("organizacion_id");
    int usuarioId = request.session().attribute("user_id");
    RepositorioSectores repositorioSectores = new RepositorioSectores();
    List<Sector> sectores = repositorioSectores.getSectoresPorUsuario(organizacionId, usuarioId);

    Map<String, Object> modelo = new HashMap<>();
    Rol rolUsuario = request.session().attribute("rol_id");
    modelo.put("sectores", sectores);
    modelo.put("sesionIniciada", request.session().attribute("user_id") != null);
    modelo.put("Usuario", rolUsuario == Rol.USUARIO);
    modelo.put("Administrador", rolUsuario == Rol.ADMINISTRADOR);
    return new ModelAndView(
        modelo, "sectoresPorUsuario.html.hbs"
    );
  }

  public ModelAndView listar(Request request, Response response) {
    Map<String, Object> modelo = new HashMap<>();
    String id = request.params(":organizacion_id");

    List<Sector> sectores = RepositorioOrganizaciones.instancia
        .buscar(Long.parseLong(id)).getSectores();

    modelo.put("sectores", sectores);

    return new ModelAndView(modelo, "sectores.html.hbs");
  }

  public Void vincularse(Request request, Response response) {
    Long sectorId = Long.parseLong(request.queryParams("sector"));
    int usuarioId = request.session().attribute("user_id");

    System.out.println("hola estoy aca");

    withTransaction(() -> {
      RepositorioUsuarios repoUsuarios = new RepositorioUsuarios();
      Persona persona = repoUsuarios.buscar(usuarioId);
      Miembro nuevoPostulante = new Miembro(persona, new ArrayList<>());
      Sector sector = RepositorioSectores.instancia.buscar(sectorId);
      RepositorioMiembros repositorioMiembros = new RepositorioMiembros();
      repositorioMiembros.agregar(nuevoPostulante);
      sector.agregarPostulante(nuevoPostulante);
    });

    response.redirect("/home/sectoresPorUsuario");

    return null;
  }

}
