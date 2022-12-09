package main;

import domain.services.distancia.ServicioDistancia;
import domain.services.distancia.entities.Ubicacion;
import enums.Rol;
import java.io.IOException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import model.Clasificacion;
import model.Contacto;
import model.Linea;
import model.Miembro;
import model.NotificacionMail;
import model.Notificador;
import model.Organizacion;
import model.Parada;
import model.Persona;
import model.ProcesadorCsv;
import model.Sector;
import model.SectorTerritorial;
import model.TipoAlcance;
import model.TipoConsumo;
import model.TipoDocumento;
import model.TipoOrganizacion;
import model.TipoSectorTerritorial;
import model.TipoTransportePublico;
import model.TipoUnidad;
import model.TipoVehiculo;
import model.Tramo;
import model.TransportePublico;
import model.Trayecto;
import model.Vehiculo;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import repositories.RepositorioTipoDeConsumo;

public class Bootstrap implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

  public static void main(String[] args) {
    new Bootstrap().run();
  }

  TransportePublico crearSubte() {
    final TipoConsumo tipoConsumo = RepositorioTipoDeConsumo.instancia.getTiposDeConsumo().get(0);
    List<Parada> paradasIda = new ArrayList<>();
    Parada paradaFlores = new Parada(
        "Flores", 10,
        new Ubicacion(1, "Av. Rivadavia", 100)
    );
    Parada paradaRetiro = new Parada(
        "Retiro", 10,
        new Ubicacion(2, "Av. Rivadavia", 10000)
    );
    persist(paradaFlores);
    persist(paradaRetiro);
    paradasIda.add(paradaFlores);
    paradasIda.add(paradaRetiro);
    List<Parada> paradasVuelta = new ArrayList<>();
    paradasVuelta.add(paradaRetiro);
    paradasVuelta.add(paradaFlores);
    Linea lineaA = new Linea(paradasIda, paradasVuelta);
    TransportePublico subteA = new TransportePublico(
        TipoTransportePublico.SUBTE, lineaA, tipoConsumo
    );
    subteA.setNombre("Subte A");
    subteA.setConsumoDeCombustiblePorKm(100);
    return subteA;
  }

  TransportePublico crearTren() {
    final TipoConsumo tipoConsumo = RepositorioTipoDeConsumo.instancia.getTiposDeConsumo().get(0);
    List<Parada> paradasIda = new ArrayList<>();
    Parada paradaOnce = new Parada(
        "Flores", 10,
        new Ubicacion(1, "Av. Rivadavia", 100)
    );
    Parada paradaMerlo = new Parada(
        "Boedo", 10,
        new Ubicacion(2, "Av. Rivadavia", 10000)
    );
    persist(paradaMerlo);
    persist(paradaOnce);
    paradasIda.add(paradaOnce);
    paradasIda.add(paradaMerlo);
    List<Parada> paradasVuelta = new ArrayList<>();
    paradasVuelta.add(paradaMerlo);
    paradasVuelta.add(paradaOnce);
    Linea lineaTren = new Linea(paradasIda, paradasVuelta);
    TransportePublico trenSarmiento = new TransportePublico(
        TipoTransportePublico.TREN, lineaTren, tipoConsumo
    );
    trenSarmiento.setNombre("Tren sarmiento");
    trenSarmiento.setConsumoDeCombustiblePorKm(200);
    return trenSarmiento;
  }

  public void run() {
    withTransaction(() -> {
      Persona personaIvan = new Persona(
          "Ivan",
          "Borgognoni",
          TipoDocumento.DNI,
          32312323
      );
      personaIvan.setNombreUsuario("IBorgognoni");
      personaIvan.setContrasenia("11057");
      personaIvan.setRol(Rol.USUARIO);
      final Miembro ivan = new Miembro(
          personaIvan,
          new ArrayList<>()
      );

      Persona personaRoberto = new Persona(
          "Roberto",
          "Lopez",
          TipoDocumento.DNI,
          11223344
      );
      personaRoberto.setNombreUsuario("RLopez");
      personaRoberto.setContrasenia("11056");
      personaRoberto.setRol(Rol.USUARIO);
      final Miembro roberto = new Miembro(
          personaRoberto,
          new ArrayList<>()
      );

      Persona personaPepe = new Persona(
          "Pepe",
          "Argento",
          TipoDocumento.DNI,
          444899123
      );
      personaPepe.setNombreUsuario("PArgento");
      personaPepe.setContrasenia("11056");
      personaPepe.setRol(Rol.ADMINISTRADOR);
      final Miembro pepe = new Miembro(
          personaPepe,
          new ArrayList<>()
      );

      Persona personaJose = new Persona(
          "Jose",
          "Argento",
          TipoDocumento.DNI,
          444899123
      );
      personaJose.setNombreUsuario("JArgento");
      personaJose.setContrasenia("11056");
      personaJose.setRol(Rol.USUARIO);
      final Miembro jose = new Miembro(
          personaJose,
          new ArrayList<>()
      );

      persist(ivan);
      persist(roberto);
      persist(pepe);
      persist(jose);

      Sector ventas = new Sector("Ventas", new ArrayList<>(), new ArrayList<>());
      Sector marketing = new Sector("Marketing", new ArrayList<>(), new ArrayList<>());
      persist(marketing);
      Sector contaduria = new Sector("Contaduria", new ArrayList<>(), new ArrayList<>());
      ventas.agregarMiembro(ivan);
      ventas.agregarPostulante(roberto);
      contaduria.agregarMiembro(pepe);
      persist(ventas);
      persist(contaduria);

      List<Contacto> contactos = new ArrayList<>();
      contactos.add(
          new Contacto("Ariel Choque", "5491166854239", "ariel0choque0mamani@gmail.com")
      );
      List<Notificador> notificaciones = new ArrayList<>();
      notificaciones.add(new NotificacionMail());
      Organizacion organizacion = new Organizacion(
          "Gugle",
          new Ubicacion(1, "Csstreet", 55),
          TipoOrganizacion.EMPRESA,
          Clasificacion.SECTOR_PRIMARIO,
          new ArrayList<>(),
          contactos,
          new ArrayList<>(),
          notificaciones
      );

      organizacion.agregarSector(ventas);
      organizacion.agregarSector(marketing);
      organizacion.agregarSector(contaduria);

      persist(organizacion);
      final Organizacion organizacion2 = new Organizacion(
          "Apple",
          new Ubicacion(1, "Csstreet", 55),
          TipoOrganizacion.EMPRESA,
          Clasificacion.SECTOR_PRIMARIO,
          new ArrayList<>(),
          new ArrayList<>(),
          new ArrayList<>(),
          new ArrayList<>()
      );

      // ORGANIZACION DE PRUEBAS
      Sector ventas2 = new Sector("Ventas", new ArrayList<>(), new ArrayList<>());
      ventas2.agregarPostulante(roberto);
      persist(ventas2);
      organizacion2.agregarSector(ventas2);
      persist(organizacion2);

      //
      SectorTerritorial cordoba = new SectorTerritorial(
          "Cordoba", TipoSectorTerritorial.PROVINCIA
      );
      SectorTerritorial chubut = new SectorTerritorial(
          "Chubut", TipoSectorTerritorial.PROVINCIA
      );
      cordoba.agregarOrganizacion(organizacion);
      persist(cordoba);
      persist(chubut);
      //TRAYECTOS
      TipoConsumo nafta = new TipoConsumo(5, TipoUnidad.lt,
          "Combustible test", "abc", TipoAlcance.EmisionIndirectaOtros);
      persist(nafta);

      Vehiculo unAuto = new Vehiculo(
          TipoVehiculo.AUTO, nafta, true, ServicioDistancia.getInstancia()
      );
      unAuto.setConsumoDeCombustiblePorKm(5);
      persist(unAuto);

      Tramo unTramo = new Tramo(
          new Ubicacion(4, "falsa", 123),
          new Ubicacion(3, "falsa", 224),
          unAuto
      );

      persist(unTramo);
      try {
        System.out.println(unTramo.obtenerDistancia(ServicioDistancia.getInstancia()));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      Trayecto trayecto = new Trayecto(new ArrayList<>());
      trayecto.agregarTramo(unTramo);
      trayecto.setPeriodicidadDeImputacion(YearMonth.of(2022, 10));
      persist(trayecto);
      ivan.agregarTrayecto(trayecto);

      ProcesadorCsv procesador = new ProcesadorCsv();
      List<TipoConsumo> consumosCargados = procesador.getTipoConsumos();
      consumosCargados
              .stream()
              .forEach(consumo -> persist(consumo));

      //Se cargan transportes publicos
      TransportePublico subte = crearSubte();
      persist(subte);
      TransportePublico tren = crearTren();
      persist(tren);

    });
  }
}
