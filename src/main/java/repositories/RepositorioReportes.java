package repositories;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import model.EvolucionHc;
import model.Organizacion;
import model.OrganizacionComposicion;
import model.SectorTerritorial;
import model.SectorTerritorialComposicion;
import model.TipoPeriodicidad;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

public class RepositorioReportes extends AbstractPersistenceTest
    implements WithGlobalEntityManager {

  @SuppressWarnings("unchecked")
  public SectorTerritorial getSectorTerritorial(Long idSectorTerritorial) {
    List<SectorTerritorial> sectores =  entityManager()
        .createQuery("from SectorTerritorial where id = :id")
        .setParameter("id", idSectorTerritorial)
        .getResultList();
    SectorTerritorial sector = sectores.stream().findFirst().get();
    return sector;
  }

  @SuppressWarnings("unchecked")
  //TODO: pass to unit test
  public float getHCsectorTerritorial(
      Long sectorTerritorialId, TipoPeriodicidad periodicidad, YearMonth periodo
  ) {
    SectorTerritorial sector = getSectorTerritorial(sectorTerritorialId);
    return sector.obtenerCalculoHC(periodicidad, periodo);
  }

  @SuppressWarnings("unchecked")
  //TODO: quitar sectorId
  public float getHCorganizacion(Long sectorTerritorialId,
                                 Long organizacionId,
                                 TipoPeriodicidad periodicidad,
                                 YearMonth periodo) {

    SectorTerritorial sector = getSectorTerritorial(sectorTerritorialId);
    Organizacion organizacion = sector.getOrganizaciones().stream().filter(item -> {
      return item.getId().equals(organizacionId);
    }).findFirst().get();
    return organizacion.obtenerCalculoHC(periodicidad, periodo);
  }

  @SuppressWarnings("unchecked")
  public List<SectorTerritorialComposicion> getHCsectorTerritorialComposicion(
      Long idSectorTerritorial
  ) {

    SectorTerritorial sector = this.getSectorTerritorial(idSectorTerritorial);
    List<SectorTerritorialComposicion> reporte = new ArrayList<>();
    sector.getOrganizaciones().forEach(
        organizacion -> {
          reporte.add(
              new SectorTerritorialComposicion(
                sector.getNombre(),
                organizacion.getRazonSocial(),
                organizacion.obtenerCalculoHC(TipoPeriodicidad.Anual, YearMonth.now())));
        }
    );
    return reporte;
  }

  @SuppressWarnings("unchecked")
  public List<OrganizacionComposicion> getHCorganizacionComposicion(Long idOrganizacion) {

    List<Organizacion> organizaciones =  entityManager()
        .createQuery("from organizaciones where id = :id")
        .setParameter("id", idOrganizacion)
        .getResultList();

    Organizacion organizacion = organizaciones.stream().findFirst().get();
    List<OrganizacionComposicion> reporte = new ArrayList<>();
    organizacion.getSectores().forEach(
        sector -> {
          reporte.add(
              new OrganizacionComposicion(
                  organizacion.getRazonSocial(),
                  sector.obtenerCalculoHC()
              )
          );
        }
    );
    return reporte;
  }

  public Organizacion getOrganizacionBy(Long idOrganizacion) {
    List<Organizacion> organizaciones =  entityManager()
        .createQuery("from organizaciones where id = :id")
        .setParameter("id", idOrganizacion)
        .getResultList();
    return organizaciones.stream().findFirst().get();

  }
  
  public List<EvolucionHc> getReportSectorTerritorialAnual(Long idSectorTerritorial,
                                                           int inicio, int fin) {

    SectorTerritorial sector = getSectorTerritorial(idSectorTerritorial);
    int diff = fin - inicio;
    int fecha = 0;
    List<EvolucionHc> report = new ArrayList<>();
    for (int i = 0; i < diff; i++) {
      fecha = fecha + i;
      float hcValor = sector.obtenerCalculoHC(TipoPeriodicidad.Anual, YearMonth.of(fecha, 1));
      EvolucionHc result = new EvolucionHc(hcValor, String.valueOf(fecha));
      report.add(result);
    }
    return report;
  }

  public List<EvolucionHc> getReportOrganizacionAnual(
      Long idOrganizacion,
      int inicio,
      int fin
  ) {
    Organizacion organizacion = getOrganizacionBy(idOrganizacion);
    int diff = fin - inicio;
    int fecha = 0;
    List<EvolucionHc> report = new ArrayList<>();
    for (int i = 0; i < diff; i++) {
      fecha = fecha + i;
      float hcValor = organizacion.obtenerCalculoHC(TipoPeriodicidad.Anual, YearMonth.of(fecha, 1));
      EvolucionHc result = new EvolucionHc(hcValor, String.valueOf(fecha));
      report.add(result);
    }
    return report;
  }

  public List<EvolucionHc> getReportSectorTerritorialMensual(
      Long idSectorTerritorial, YearMonth inicio, YearMonth fin
  ) {
    SectorTerritorial sector = getSectorTerritorial(idSectorTerritorial);
    YearMonth fecha = inicio;
    List<EvolucionHc> report = new ArrayList<>();
    while (fecha.isBefore(fin)) {
      float hcValor = sector.obtenerCalculoHC(TipoPeriodicidad.Mensual, fecha);
      EvolucionHc result = new EvolucionHc(hcValor, String.valueOf(fecha));
      report.add(result);
      fecha.plusMonths(1);
    }
    return report;
  }

  public List<EvolucionHc> getReportOrganizacionMensual(
      Long idOrganizacion, YearMonth inicio, YearMonth fin
  ) {
    Organizacion organizacion = getOrganizacionBy(idOrganizacion);
    YearMonth fecha = inicio;
    List<EvolucionHc> report = new ArrayList<>();
    while (fecha.isBefore(fin)) {
      float hcValor = organizacion.obtenerCalculoHC(TipoPeriodicidad.Mensual, fecha);
      EvolucionHc result = new EvolucionHc(hcValor, String.valueOf(fecha));
      report.add(result);
      fecha.plusMonths(1);
    }
    return report;
  }
}
