package model;

public class OrganizacionComposicion {
  private String razonSocial;
  private float hcSector;

  public OrganizacionComposicion(String razonSocial, float hcSector) {
    this.razonSocial = razonSocial;
    this.hcSector = hcSector;
  }

  public float getHcSector() {
    return hcSector;
  }

  public String getRazonSocial() {
    return razonSocial;
  }
}
