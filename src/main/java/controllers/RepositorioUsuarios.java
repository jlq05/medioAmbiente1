package controllers;

import excepciones.BusquedaEnBaseDeDatosException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import model.Usuario;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

public class RepositorioUsuarios implements WithGlobalEntityManager {
  private static final RepositorioUsuarios INSTANCE = new RepositorioUsuarios();

  public static RepositorioUsuarios instance() {
    return INSTANCE;
  }

  public Usuario buscarUsuario(String nombreUsuario, String contrasenia, String rol) {
    EntityManager entityManager = this.entityManager();
    try {
      return /*(Usuario)*/ entityManager.createQuery("from Usuario where nombreusuario = "
          + ":username and contrasena = :password "
          + " and rol = :rol ", Usuario.class)
          .setParameter("username", nombreUsuario)
          .setParameter("password", contrasenia)
          .setParameter("rol", rol)
          .getSingleResult();


    }  catch (NoResultException e) {
      throw new BusquedaEnBaseDeDatosException("No se ha encontrado un usuario registrado con los "
          + "datos de inicio de sesión aportados. "
          + "Por favor, ingrese un usuario y contraseña válidos");
    }
  }

  public Usuario buscarUsuarioPorId(long id) {
    return /*(Usuario)*/ entityManager().createQuery("from Usuario where id =:id ", Usuario.class)
        .setParameter("id", id).getSingleResult();

  }

  public List<Usuario> listar() {
    return entityManager()//
        .createQuery("from Usuario", Usuario.class) //
        .getResultList();
  }

  public Usuario buscarPorUsuarioYContrasenia(String username, String password) {
    return listar().stream()
        .filter(u -> u.getContrasena().equals(password)
            && u.getNombreUsuario().equals(username)).findFirst().get();
  }
}
