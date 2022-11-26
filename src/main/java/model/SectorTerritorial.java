package model;

import domain.services.distancia.ServicioDistancia;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "sectoresTerritoriales")
public class SectorTerritorial extends PersistentEntity {
  @Column
  String nombre;

  @Enumerated(EnumType.ORDINAL)
  TipoSectorTerritorial tipo;

  @OneToMany(cascade = {CascadeType.ALL})
  @JoinColumn(name = "sectorTerritorialId")
  List<Organizacion> organizaciones = new ArrayList<>();

  public SectorTerritorial(String nombre, TipoSectorTerritorial tipo) {
    this.nombre = nombre;
    this.tipo = tipo;
  }

  public SectorTerritorial() {
  }

  public void agregarOrganizacion(Organizacion org) {
    this.organizaciones.add(org);
  }

  public List<Organizacion> getOrganizaciones() {
    return organizaciones;
  }

  public float obtenerCalculoHC(
      TipoPeriodicidad tipoPeriodicidad,
      YearMonth periodicidadDeImputacion,
      ServicioDistancia servicioDistancia
  ) {
    return (float) this.organizaciones.stream()
        .mapToDouble(organizacion ->
            organizacion.obtenerCalculoHC(
                tipoPeriodicidad,
                periodicidadDeImputacion,
                servicioDistancia
            )
        ).sum();
  }

  public String getNombre() {
    return nombre;
  }

  public TipoSectorTerritorial getTipo() {
    return tipo;
  }
}
