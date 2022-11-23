package repositories;

import java.util.ArrayList;
import java.util.List;
import model.Miembro;
import model.Organizacion;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

public class RepositorioOrganizaciones implements WithGlobalEntityManager {

  public static final RepositorioOrganizaciones instancia = new RepositorioOrganizaciones();

  public List<Organizacion> listar() {
    return entityManager()//
        .createQuery("from Organizacion", Organizacion.class)
        .getResultList();
  }

  public List<Organizacion> listarSegunEmpleados() {
    return entityManager()//
        .createQuery("from Consultora order by cantidad_empleados desc", Organizacion.class) //
        .getResultList();
  }


  public Organizacion buscar(long id) {
    return entityManager().find(Organizacion.class, id);
  }

  public void agregar(Organizacion organizacion) {
    entityManager().persist(organizacion);
  }

  public List<Organizacion> buscarPorNombre(String nombre) {
    return entityManager() //
        .createQuery("from Organizacion c where c.razonSocial like :nombre", Organizacion.class) //
        .setParameter("nombre", "%" + nombre + "%") //
        .getResultList();
  }

  public List<Miembro> listarPostulantes(long id) {
    Organizacion organizacion = this.buscar(id);
    List<Miembro> postulantes = new ArrayList<>();

    organizacion.getSectores().forEach(sector -> {
      sector.getPostulantes().addAll(postulantes);
    });

    return postulantes;
  }
}