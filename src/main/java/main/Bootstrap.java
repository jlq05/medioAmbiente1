package main;

import domain.services.distancia.ServicioDistancia;
import domain.services.distancia.entities.Ubicacion;
import enums.Rol;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import model.Clasificacion;
import model.MedioDeTransporte;
import model.Miembro;
import model.Organizacion;
import model.ProcesadorCsv;
import model.Sector;
import model.TipoAlcance;
import model.TipoConsumo;
import model.TipoDocumento;
import model.TipoOrganizacion;
import model.TipoUnidad;
import model.TipoVehiculo;
import model.Tramo;
import model.Trayecto;
import model.Usuario;
import model.Vehiculo;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

public class Bootstrap implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

  public static void main(String[] args) {
    new Bootstrap().run();
  }

  //EntityManager entityManager = this.entityManager();
  //EntityTransaction transaction = entityManager.getTransaction();

  public void run() {
    withTransaction(() -> {

      Miembro ivan = new Miembro(
          "Ivan",
          "Borgognoni",
          TipoDocumento.DNI,
          32312323,
          new ArrayList<>()
      );

      Miembro roberto = new Miembro(
          "Roberto",
          "Lopez",
          TipoDocumento.DNI,
          11223344,
          new ArrayList<>()
      );

      Miembro pepe = new Miembro(
          "Pepe",
          "Argento",
          TipoDocumento.DNI,
          444899123,
          new ArrayList<>()
      );

      persist(ivan);
      persist(roberto);
      persist(pepe);

      Sector ventas = new Sector("Ventas", new ArrayList<>(), new ArrayList<>());
      Sector marketing = new Sector("Marketing", new ArrayList<>(), new ArrayList<>());
      persist(marketing);
      Sector contaduria = new Sector("Contaduria", new ArrayList<>(), new ArrayList<>());
      ventas.agregarPostulante(ivan);
      ventas.agregarPostulante(roberto);
      contaduria.agregarPostulante(pepe);
      persist(ventas);
      persist(contaduria);

      Organizacion organizacion = new Organizacion(
          "Gugle",
          new Ubicacion(1, "Csstreet", 55),
          TipoOrganizacion.EMPRESA,
          Clasificacion.SECTOR_PRIMARIO,
          new ArrayList<>(),
          new ArrayList<>(),
          new ArrayList<>(),
          new ArrayList<>()
      );

      organizacion.agregarSector(ventas);
      organizacion.agregarSector(marketing);
      organizacion.agregarSector(contaduria);

      persist(organizacion);

      //TRAYECTOS

      TipoConsumo nafta = new TipoConsumo(5, TipoUnidad.lt,
          "nana", "abc", TipoAlcance.EmisionIndirectaOtros);
      persist(nafta);

      Vehiculo unAuto = new Vehiculo(TipoVehiculo.AUTO, nafta,
          true, ServicioDistancia.getInstancia());
      unAuto.setConsumoDeCombustiblePorKm(5);
      persist(unAuto);

      Tramo unTramo = new Tramo(
          new Ubicacion(4, "falsa", 123),
          new Ubicacion(3, "falsa", 224),
          unAuto
      );

      persist(unTramo);
      try {
        System.out.println(unTramo.obtenerDistancia());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      Trayecto trayecto = new Trayecto(new ArrayList<>());
      trayecto.agregarTramo(unTramo);

      persist(trayecto);
      ProcesadorCsv procesador = new ProcesadorCsv();
      List<TipoConsumo> consumosCargados = procesador.getTipoConsumos();

      consumosCargados
              .stream()
              .forEach(consumo -> persist(consumo));
     

      ivan.agregarTrayecto(trayecto);

      System.out.println(ivan.obtenerCalculoHC());

      persist(new Usuario("JMendoza", "11056", "jose", "Quisbert", Rol.USUARIO));
      persist(new Usuario("SeniorX", "11056", "juan", "Perez", Rol.ADMINISTRADOR));

    });
  }
}
