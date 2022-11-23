package model;

public class PostulanteDto {
  private long id;
  private int dni;
  private String nombre;
  private String apellido;
  private long sectorId;

  private String sectorNombre;

  public PostulanteDto(
      long id,
      int dni,
      String nombre,
      String apellido,
      long sectorId,
      String sectorNombre) {

    this.id = id;
    this.dni = dni;
    this.nombre = nombre;
    this.apellido = apellido;
    this.sectorId = sectorId;
    this.sectorNombre = sectorNombre;
  }

  public String getApellido() {
    return apellido;
  }

  public String getNombre() {
    return nombre;
  }

  public int getDni() {
    return dni;
  }

  public long getSectorId() {
    return sectorId;
  }

  public long getId() {
    return id;
  }

  public String getSectorNombre() {
    return sectorNombre;
  }
}
