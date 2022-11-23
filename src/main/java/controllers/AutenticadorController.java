package controllers;

import enums.Rol;
import model.Usuario;
import spark.Request;
import spark.Response;

public class AutenticadorController {

  public void autenticarUsuario(Request request, Response response) {
    String nombreDeUsuario = request.queryParams("username");
    String contrasenia = request.queryParams("password");
    Rol rol = Rol.valueOf(request.queryParams("rol"));

    Usuario usuario = null;

    if (rol.equals(Rol.ADMINISTRADOR)) {
      usuario = RepositorioUsuarios.instance()
          .buscarUsuario(nombreDeUsuario, contrasenia, "Administrador");
    }

    if (rol.equals(Rol.USUARIO)) {
      usuario = RepositorioUsuarios.instance()
          .buscarUsuario(nombreDeUsuario, contrasenia, "Usuario");
    }

    this.crearSessionUsuario(request, usuario.getId(), rol);
  }

  public void crearSessionUsuario(Request request, long idUsuario, Rol rol) {
    request.session().attribute("idUsuario", idUsuario);
    request.session().attribute("rol", rol);
  }

  public boolean usuarioAutenticado(Request request) {
    return request.session().attribute("idUsuario") != null;
  }

}
