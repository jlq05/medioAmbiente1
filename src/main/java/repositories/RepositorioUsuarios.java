package repositories;

import excepciones.BusquedaEnBaseDeDatosException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import model.Miembro;
import model.Persona;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

public class RepositorioUsuarios implements WithGlobalEntityManager {
  private static final RepositorioUsuarios INSTANCE = new RepositorioUsuarios();

  public static RepositorioUsuarios instance() {
    return INSTANCE;
  }

  public Miembro buscarUsuario(String nombreUsuario, String contrasenia) {
    EntityManager entityManager = this.entityManager();
    try {
      return entityManager.createQuery("from Miembro where nombreUsuario = "
          + ":username and contrasenia = :password "
          + " and rol = :rol ", Miembro.class)
          .setParameter("username", nombreUsuario)
          .setParameter("password", contrasenia)
          .getSingleResult();
    }  catch (NoResultException e) {
      throw new BusquedaEnBaseDeDatosException("No se ha encontrado un usuario registrado con los "
          + "datos de inicio de sesión aportados. "
          + "Por favor, ingrese un usuario y contraseña válidos");
    }
  }

  public Miembro buscarMiembroPorId(long id) {
    return entityManager().createQuery("from Miembro where id =:id ", Miembro.class)
        .setParameter("id", id).getSingleResult();
  }

  public List<Persona> listar() {
    return entityManager()
        .createQuery("from Persona", Persona.class)
        .getResultList();
  }

  public Persona buscarPorUsuarioYContrasenia(String username, String password) {
    List<Persona> personas = listar();
    List<Persona> miembrosFiltrados;
    try {
      miembrosFiltrados = personas.stream()
          .filter(u -> u.getContrasenia().equals(password)
              && u.getNombreUsuario().equals(username)).collect(Collectors.toList());
    } catch (Exception ex) {
      throw new BusquedaEnBaseDeDatosException("No se encontró usuario");
    }
    return miembrosFiltrados.size() > 0 ?  miembrosFiltrados.get(0) : null;
  }

  public Persona buscar(long id) {
    return entityManager()
        .createQuery("from Persona where id = :id", Persona.class)
        .setParameter("id", id).getSingleResult();
  }
}
