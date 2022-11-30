package view;

public class OrganizacionVista {
  public int id;
  public String nombre;
  public float hc;
  public String hclegible;
  public String clasificacion;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public float getHC() {
    return hc;
  }

  public void setHC(float hc) {
    this.hc = hc;
  }

  public String getClasificacion() {
    return clasificacion;
  }

  public void setClasificacion(String clasificacion) {
    this.clasificacion = clasificacion;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getHclegible() {
    return hclegible;
  }

  public void setHclegible(String hclegible) {
    this.hclegible = hclegible;
  }
}
