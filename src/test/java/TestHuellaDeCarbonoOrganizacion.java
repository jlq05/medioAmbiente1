import domain.services.distancia.ServicioDistancia;
import domain.services.distancia.entities.Ubicacion;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.YearMonth;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TestHuellaDeCarbonoOrganizacion {

  private ServicioDistancia servicioDistancia;

  @BeforeEach
  void initServicioDistancia() {
    servicioDistancia = mock(ServicioDistancia.class);
  }

  @Test
  public void calcularLaHuellaDeCarbonoDeUnaOrganizacion() throws IOException {
    when(servicioDistancia.obtenerDistancia(any(), any())).thenReturn(2F);
    Organizacion unaOrganizacionConDatos = unaOrganizacionConDatos();
    //assertEquals(unaOrganizacionConDatos.obtenerCalculoHC(TipoPeriodicidad.Anual, YearMonth.now()), 138);

    Organizacion unaOrganizacionConTrayectoCompartido = unaOrganizacionConTrayectosCompartidos();
    //assertEquals(unaOrganizacionConTrayectoCompartido.obtenerCalculoHC(TipoPeriodicidad.Anual, YearMonth.now()), 50);
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

  private  Organizacion unaOrganizacionConTrayectosCompartidos(){
    ArrayList<Trayecto> trayectos = new ArrayList<>();
    trayectos.add(unTrayecto());

    ArrayList<Miembro> miembros = new ArrayList<>();
    miembros.add(unMiembro(trayectos));
    miembros.add(unMiembro(trayectos));

    ArrayList<Sector> sectores = new ArrayList<>();
    sectores.add(new Sector("Compras", miembros, new ArrayList<>()));
    return unaOrganizacion(sectores, new ArrayList<>());
  }

  private Vehiculo unAutoANafta() {
    Vehiculo unAuto = new Vehiculo(TipoVehiculo.AUTO, consumoNafta(), true, servicioDistancia);
    unAuto.setConsumoDeCombustiblePorKm(5);
    return unAuto;
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

  private File getFile(String base) {
    Path path = Paths.get("");
    String ruta = path.toAbsolutePath().toString() + base;
    File file = new File(ruta);
    return file;
  }
}
