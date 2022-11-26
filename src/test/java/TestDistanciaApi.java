import domain.services.distancia.entities.Ubicacion;
import model.Bicicleta;
import model.Tramo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import domain.services.distancia.ServicioDistancia;

import static org.mockito.Mockito.*;

public class TestDistanciaApi {
  //console.log
  private ServicioDistancia servicioDistancia;

  @BeforeEach
  void initServicioDistancia() {
    servicioDistancia = mock(ServicioDistancia.class);
  }
  @Test
  public void verificarDistancia() throws IOException {
    when(servicioDistancia.obtenerDistancia(any(), any())).thenReturn(2F);
    ServicioDistancia servicio = servicioDistancia;
    Ubicacion origen = ubicacionMaipu();
    Ubicacion destino = ubicacionHiggins();
    float respuesta = servicio.obtenerDistancia(
        origen,
        destino
    );
    assertEquals(respuesta, 2F);
  }

  @Test
  public void verificarServicio() throws IOException {
    Tramo tramo = new Tramo(ubicacionMaipu(), ubicacionHiggins(), new Bicicleta(ServicioDistancia.getInstancia()));
    assertNotNull(tramo.obtenerDistancia());
  }

  @Test
  public void verificarDistanciaDeTramo() throws IOException {
    when(servicioDistancia.obtenerDistancia(any(), any())).thenReturn(2F);
    Tramo tramo = new Tramo(ubicacionMaipu(), ubicacionHiggins(), new Bicicleta(servicioDistancia));
    assertEquals(tramo.obtenerDistancia(), 2F);
  }

  private Ubicacion ubicacionMaipu() {
    return new Ubicacion(1, "maipu", 100);
  }

  private Ubicacion ubicacionHiggins() {
    return new Ubicacion(457, "O'Higgins", 200);
  }
}