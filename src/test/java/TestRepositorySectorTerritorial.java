import domain.services.distancia.ServicioDistancia;
import domain.services.distancia.entities.Ubicacion;
import model.*;
import org.junit.jupiter.api.*;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.apache.commons.io.FileUtils.getFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class TestRepositorySectorTerritorial extends AbstractPersistenceTest implements WithGlobalEntityManager {

  private ServicioDistancia servicioDistancia;

  @BeforeEach
  void initServicioDistancia() {
    servicioDistancia = mock(ServicioDistancia.class);
  }

  @BeforeEach
  public void setup() {
    this.beginTransaction();
  }

  @AfterEach
  public void tearDown() {
    this.rollbackTransaction();
  }

  @Test
  public void verificarCreacionDeSectorTerritorial() {
    /*entityManager().clear();
    entityManager().getTransaction().begin();
    SectorTerritorial item = crearSectorTerritorial("Test", TipoSectorTerritorial.MUNICIPIO);
    entityManager().persist(item);
    entityManager().getTransaction().commit();
    System.out.printf("%d\n", item.getId());
    assertNotNull(item.getId());*/
  }

  @Test
  public void verificarPersistenciaDeUnaOrganizacion() {
    entityManager().clear();
    //entityManager().getTransaction().begin();
    Organizacion organizacion = unaOrganizacionConDatos();
    entityManager().persist(organizacion);
    assertNotNull(organizacion.getId());
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

  private  Organizacion unaOrganizacionConDatos() {
    File file = getFile("/test/consumos.csv");
    ProcesadorCsv proceso = new ProcesadorCsv();
    proceso.procesarArchivo(file);
    ArrayList<DatosActividad> actividades = new ArrayList<DatosActividad>();
    actividades.addAll(proceso.getdatos());

    ArrayList<Sector> sectores = new ArrayList<>();
    sectores.add(nuevoSector());
    return unaOrganizacion(sectores, actividades);
  }

  private Sector nuevoSector() {
    ArrayList<Trayecto> trayectosMiembro = new ArrayList<>();
    trayectosMiembro.add(unTrayecto());
    trayectosMiembro.add(unTrayecto());

    ArrayList<Trayecto> trayectosPostulante = new ArrayList<>();
    trayectosPostulante.add(unTrayecto());
    trayectosPostulante.add(unTrayecto());

    ArrayList<Miembro> miembros = new ArrayList<>();
    miembros.add(unMiembro(trayectosMiembro));
    ArrayList<Miembro> postulantes = new ArrayList<>();
    postulantes.add(unMiembro(trayectosPostulante));
    postulantes.add(unMiembro(trayectosPostulante));
    postulantes.add(unMiembro(trayectosPostulante));
    postulantes.add(unMiembro(trayectosPostulante));

    return new Sector("Compras", miembros, postulantes);
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
        new ArrayList<Trayecto>()//trayectos
    );
    entityManager().persist(miembro);
    return miembro;
  }

  private Organizacion unaOrganizacion(ArrayList<Sector> sectores, ArrayList<DatosActividad> actividades){
    ArrayList<Contacto> contactos = new ArrayList<>();
    contactos.add(new Contacto("Ariel Choque", "1166854239", "test@gmail.com"));
    Organizacion organizacion = new Organizacion(
        "TestCompany",
        new Ubicacion(1, "CABA", 2),
        TipoOrganizacion.EMPRESA,
        Clasificacion.SECTOR_PRIMARIO,
        sectores,
        contactos,
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
