import domain.services.distancia.ServicioDistancia;
import domain.services.distancia.entities.Ubicacion;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.YearMonth;
import java.util.*;

import static org.apache.commons.io.FileUtils.getFile;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class TestSectorTerritorial {

    private ServicioDistancia servicioDistancia;

    @BeforeEach
    void initServicioDistancia() {
    servicioDistancia = mock(ServicioDistancia.class);
  }

    @Test
    public void verificarCreacionDeUnSectorTerritorial() {

      SectorTerritorial cordoba = crearSectorTerritorial(
          "Cordoba",
          TipoSectorTerritorial.PROVINCIA
         );
      SectorTerritorial villaMaria = crearSectorTerritorial(
          "Villa Maria",
          TipoSectorTerritorial.MUNICIPIO
      );


      assertEquals(cordoba.getTipo(),TipoSectorTerritorial.PROVINCIA);
      assertEquals(villaMaria.getTipo(),TipoSectorTerritorial.MUNICIPIO);
      assertEquals(cordoba.getOrganizaciones().size(),1);
      assertEquals(villaMaria.getOrganizaciones().size(),1);
      //assertEquals(villaMaria.obtenerCalculoHC(TipoPeriodicidad.Anual, YearMonth.now()),38);
    }

  private SectorTerritorial crearSectorTerritorial(String nombre, TipoSectorTerritorial tipo) {
    SectorTerritorial sectorTerritorial = new SectorTerritorial(
        nombre ,
        tipo
    );

    Organizacion unaOrganizacion = this.unaOrganizacionConDatos();
    sectorTerritorial.agregarOrganizacion(unaOrganizacion);
    return sectorTerritorial;
  }

  private  Organizacion unaOrganizacionConDatos(){
    File file = getFile("/test/consumos.csv");
    ProcesadorCsv proceso = new ProcesadorCsv();
    proceso.procesarArchivo(file);
    ArrayList<DatosActividad> actividades = new ArrayList<DatosActividad>();
    actividades.addAll(proceso.getdatos());

    ArrayList<Trayecto> trayectos = new ArrayList<>();
    trayectos.add(unTrayecto());
    trayectos.add(unTrayecto());

    ArrayList<Miembro> miembros = new ArrayList<>();
    miembros.add(unMiembro(trayectos));

    ArrayList<Sector> sectores = new ArrayList<>();
    sectores.add(new Sector("Compras", miembros, new ArrayList<>()));
    return unaOrganizacion(sectores, actividades);
  }

  private Trayecto unTrayecto(){
    Tramo unTramo = new Tramo (casa(), empresa(), unAutoANafta());
    ArrayList<Tramo> tramos = new ArrayList<>();
    tramos.add(unTramo);
    Trayecto trayecto = new Trayecto(tramos);
    return trayecto;
  }

  private Miembro unMiembro(ArrayList<Trayecto> trayectos){
    Miembro miembro = new Miembro(
        "Test",
        "Apellido",
        TipoDocumento.DNI,
        11222333,
        trayectos
    );
    return miembro;
  }

  private Organizacion unaOrganizacion(ArrayList<Sector> sectores, ArrayList<DatosActividad> actividades){
    Organizacion organizacion = new Organizacion(
        "TestCompany",
        new Ubicacion(1, "CABA", 2),
        TipoOrganizacion.EMPRESA,
        Clasificacion.SECTOR_PRIMARIO,
        sectores,
        new ArrayList<>(),
        actividades,
        new ArrayList<>()
    );
    return organizacion;
  }

  private Ubicacion casa() {
    return new Ubicacion(457, "O'Higgins", 200);
  }

  private Ubicacion empresa() {
    return new Ubicacion(3, "Medrano", 2123);
  }

  private TipoConsumo consumoNafta() {
    return new TipoConsumo(5, TipoUnidad.lt, "nana", "abc", TipoAlcance.EmisionIndirectaOtros);
  }

  private Vehiculo unAutoANafta() {
    Vehiculo unAuto = new Vehiculo(TipoVehiculo.AUTO, consumoNafta(), true, servicioDistancia);
    unAuto.setConsumoDeCombustiblePorKm(5);
    return unAuto;
  }

  private File getFile(String base) {
    Path path = Paths.get("");
    String ruta = path.toAbsolutePath().toString() + base;
    File file = new File(ruta);
    return file;
  }

}
