package model;

public class SectorTerritorialComposicion {
  private String nombreSector;
  private String nombreOrganizacion;
  private float hc;

  public SectorTerritorialComposicion(String nombreSector, String nombreOrganizacion, float hc) {
    this.nombreSector = nombreSector;
    this.nombreOrganizacion = nombreOrganizacion;
    this.hc = hc;
  }

  public float getHc() {
    return hc;
  }

  public String getNombreSector() {
    return nombreSector;
  }

  public String getNombreOrganizacion() {
    return nombreOrganizacion;
  }
}
