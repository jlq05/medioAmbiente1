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

  public String getNombreSector() {
    return nombreSector;
  }

  public void setNombreSector(String nombreSector) {
    this.nombreSector = nombreSector;
  }

  public String getNombreOrganizacion() {
    return nombreOrganizacion;
  }

  public void setNombreOrganizacion(String nombreOrganizacion) {
    this.nombreOrganizacion = nombreOrganizacion;
  }

  public float getHc() {
    return hc;
  }

  public void setHc(float hc) {
    this.hc = hc;
  }
}
