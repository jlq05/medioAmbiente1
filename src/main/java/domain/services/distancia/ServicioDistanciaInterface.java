package domain.services.distancia;

import domain.services.distancia.entities.Ubicacion;
import java.io.IOException;

public interface ServicioDistanciaInterface {
  float obtenerDistancia(Ubicacion origen, Ubicacion destino) throws IOException;
}