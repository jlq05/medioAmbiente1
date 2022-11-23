import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import domain.services.distancia.ServicioDistancia;
import domain.services.distancia.entities.Ubicacion;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

public class TestHuellaDeCarbonoMiembro {

  private ServicioDistancia servicioDistancia;

  @BeforeEach
  void initServicioDistancia() {
    servicioDistancia = mock(ServicioDistancia.class);
  }

  @Test
  public void calcularLaHuellaDeCarbonoDeUnTramo() throws IOException {
    when(servicioDistancia.obtenerDistancia(any(), any())).thenReturn(2F);

    Tramo unTramo = new Tramo (casa(), empresa(), unAutoANafta());

    //assertEquals(unTramo.obtenerCalculoHC(), 50);
  }

  @Test
  public void calcularLaHuellaDeCarbonoDeUnMiembro() throws IOException {
    when(servicioDistancia.obtenerDistancia(any(), any())).thenReturn(2F);

    Tramo unTramo = new Tramo (casa(), empresa(), unAutoANafta());
    Trayecto unTrayecto = new Trayecto(new ArrayList<>());
    unTrayecto.agregarTramo(unTramo);
    unTrayecto.agregarTramo(unTramo);

    Miembro unMiembro = new Miembro("pepe", "lopez", TipoDocumento.DNI, 34839128, new ArrayList<>());
    unMiembro.agregarTrayecto(unTrayecto);

    //assertEquals(unMiembro.obtenerCalculoHC(), 100);
  }

  private Vehiculo unAutoANafta() {
    Vehiculo unAuto = new Vehiculo(TipoVehiculo.AUTO, consumoNafta(), true, servicioDistancia);
    unAuto.setConsumoDeCombustiblePorKm(5);
    return unAuto;
  }

  private Ubicacion casa() {
    return new Ubicacion(457, "O'Higgins", 200);
  }

  private Ubicacion empresa() {
    return new Ubicacion(3, "Medrano", 2123);
  }

  private TipoConsumo consumoNafta() {
    return new TipoConsumo(5, TipoUnidad.lt, "nana", "abc", TipoAlcance.EmisionIndirectaOtros);
  }
}
