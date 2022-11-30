package utils;

import enums.Rol;
import java.util.Map;
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

  public Map<String, Object> setearPermisosParaTemplate(
      Map<String, Object> input, Request request
  ) {
    Rol rolUsuario = request.session().attribute("rol_id");
    input.put("sesionIniciada", request.session().attribute("user_id") != null);
    input.put("Usuario", rolUsuario == Rol.USUARIO);
    input.put("Administrador", rolUsuario == Rol.ADMINISTRADOR);
    return input;
  }
}
