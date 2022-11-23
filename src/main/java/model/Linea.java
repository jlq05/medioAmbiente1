package model;

import domain.services.distancia.entities.Ubicacion;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "lineas")
public class Linea extends PersistentEntity {
  @ManyToMany
  private List<Parada> paradasIda;
  @ManyToMany
  private List<Parada> paradasVuelta;

  private Linea() {
  }

  public Linea(List<Parada> paradasIda, List<Parada> paradasVuelta) {
    this.paradasIda = paradasIda;
    this.paradasVuelta = paradasVuelta;
  }

  public void agregarParadaIda(Parada unaParada) {
    this.paradasIda.add(unaParada);
  }

  public void agregarParadaVuelta(Parada unaParada) {
    this.paradasVuelta.add(unaParada);
  }

  public List<Parada> getParadasIda() {
    return this.paradasIda;
  }

  public List<Parada> getParadasVuelta() {
    return this.paradasVuelta;
  }

  public float obtenerDistancia(Ubicacion origen, Ubicacion destino) {
    if (!existeParada(origen) || !existeParada(destino)) {
      throw new ParadaNoExisteException("La Parada no se encuentra en la linea seleccionada");
    }
    return distanciaEntreParadas(listaACalcular(origen, destino), origen, destino);
  }

  public float distanciaEntreParadas(List<Parada> paradas, Ubicacion origen, Ubicacion destino) {
    return (float)paradas.stream().filter(parada ->
        entre(
            indexParada(paradas, origen),
            indexParada(paradas, destino),
            indexParada(paradas, parada.getUbicacion())
        ))
        .mapToDouble(parada -> parada.getDistanciaProxima()).sum();
  }

  public boolean existeUbicacion(List<Parada> paradas, Ubicacion ubicacion) {
    return paradas.stream().map(parada -> parada.getUbicacion()).anyMatch(ubicacionParada -> {
      return ubicacionParada.getCalle().equals(ubicacion.getCalle())
          && ubicacionParada.getAltura() == ubicacion.getAltura();
    });
  }

  public int indexParada(List<Parada> paradas, Ubicacion ubicacion) {
    return paradas
        .stream()
        .map(parada -> parada.getUbicacion())
        .collect(Collectors.toList())
        .indexOf(ubicacion);
  }

  public boolean entre(int min, int max, int numero) {
    return numero >= min && numero < max;
  }

  public boolean existeParada(Ubicacion ubicacion) {
    return existeUbicacion(this.paradasIda, ubicacion)
        || existeUbicacion(this.paradasVuelta, ubicacion);
  }

  public List<Parada> listaACalcular(Ubicacion origen, Ubicacion destino) {
    if (indexParada(paradasIda, origen) < indexParada(paradasIda, destino)) {
      return paradasIda;
    }
    return paradasVuelta;
  }

}