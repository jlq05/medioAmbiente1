package model;

public class OrganizacionComposicion {
  private String razonSocial;
  private String nombre;
  private float hcSector;

  public OrganizacionComposicion(String razonSocial, String nombre, float hcSector) {
    this.razonSocial = razonSocial;
    this.nombre = nombre;
    this.hcSector = hcSector;
  }

  public float getHcSector() {
    return hcSector;
  }

  public String getRazonSocial() {
    return razonSocial;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }
}
