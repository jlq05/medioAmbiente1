package utils;

import enums.Rol;
import spark.Request;

public class SessionValidator {
  public boolean estaLogueado(Request request) {
    return request.session().attribute("user_id") != null;
  }

  public boolean tienePermisosUsuario(Request request) {
    Rol rolId = request.session().attribute("rol_id");
    return estaLogueado(request) && rolId == Rol.USUARIO;
  }

  public boolean tienePermisosAdmin(Request request) {
    Rol rolId = request.session().attribute("rol_id");
    return estaLogueado(request) && rolId == Rol.ADMINISTRADOR;
  }
}
