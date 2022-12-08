package repositories;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import model.Clasificacion;
import model.Miembro;
import model.Organizacion;
import model.TipoPeriodicidad;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import view.OrganizacionVista;

public class RepositorioOrganizaciones implements WithGlobalEntityManager {

  public static final RepositorioOrganizaciones instancia = new RepositorioOrganizaciones();

  public List<Organizacion> listar() {
    return entityManager()
        .createQuery("from Organizacion", Organizacion.class)
        .getResultList();
  }

  public List<OrganizacionVista> getOrganizacionesPorClasificacion(
      Clasificacion clasificacion, TipoPeriodicidad tipoPeriodicidad, YearMonth yearMonth
  ) {
    List<Organizacion> organizaciones = listar();
    return organizaciones.stream()
        .filter(organizacion -> {
          return organizacion.clasificacion == clasificacion;
        })
        .map(organizacion -> {
          return organizacion.convertirAOrganizacionVista(tipoPeriodicidad, yearMonth);
        }).collect(Collectors.toList());
  }

  public List<Organizacion> getOrganizacionesPorUsuarioId(int usuarioId) {
    List<Organizacion> organizaciones = listar();
    return organizaciones.stream().filter(organizacion -> {
      return organizacion.sectores.stream().anyMatch(sector -> {
        boolean esPostulante = sector.getPostulantes().stream().anyMatch(miembro -> {
          return miembro.getPersona().getId() == usuarioId;
        });
        boolean esMiembro = sector.getMiembros().stream().anyMatch(miembro -> {
          return miembro.getPersona().getId() == usuarioId;
        });
        return esPostulante || esMiembro;
      });
    }).collect(Collectors.toList());
  }

  public Organizacion buscar(long id) {
    return entityManager().find(Organizacion.class, id);
  }

  public void agregar(Organizacion organizacion) {
    entityManager().persist(organizacion);
  }

  public List<Organizacion> buscarPorNombre(String nombre) {

    return entityManager()
          .createQuery("from Organizacion c where c.razonSocial like :nombre",
              Organizacion.class) //
          .setParameter("nombre", "%" + nombre + "%") //
          .getResultList();
  }

  public boolean existeOrganizacion(String nombre) {

    List<Organizacion> query = entityManager()
         .createQuery("from Organizacion o where o.razonSocial like :nombre",
             Organizacion.class)
        .setParameter("nombre",  "%" + nombre + "%")
        .getResultList();
    return query.stream().count() > 0;
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