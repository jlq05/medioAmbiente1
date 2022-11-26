package controllers;

import enums.Rol;
import model.Miembro;
import repositories.RepositorioUsuarios;
import spark.Request;
import spark.Response;

public class AutenticadorController {

  public void autenticarUsuario(Request request, Response response) {
    String nombreDeUsuario = request.queryParams("username");
    String contrasenia = request.queryParams("password");
    Miembro usuario = null;
    usuario = RepositorioUsuarios.instance()
        .buscarUsuario(nombreDeUsuario, contrasenia);
    this.crearSessionUsuario(request, usuario.getId(), usuario.getPersona().getRol());
  }

  public void crearSessionUsuario(Request request, long idUsuario, Rol rol) {
    request.session().attribute("idUsuario", idUsuario);
    request.session().attribute("rol", rol);
  }

  public boolean usuarioAutenticado(Request request) {
    return request.session().attribute("idUsuario") != null;
  }

}
