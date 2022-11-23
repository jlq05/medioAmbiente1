import domain.services.distancia.ServicioDistancia;
import domain.services.distancia.entities.Ubicacion;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.*;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestTrayecto {

  private ServicioDistancia servicioDistancia;

  @BeforeEach
  void initServicioDistancia() {
    servicioDistancia = mock(ServicioDistancia.class);
  }
  @Test
  public void verificarCreacionDeTrayecto() {
    Trayecto trayecto = new Trayecto(new ArrayList<>());
    Tramo tramo = new Tramo(
        new Ubicacion(1, "Casa", 1234),
        new Ubicacion(1, "Empresa", 1234),
        unAutoANafta()
    );
    trayecto.agregarTramo(tramo);
  }

  @Test
  public void calcularLaDistanciaTotalDeUnTrayecto() throws IOException {
    when(servicioDistancia.obtenerDistancia(any(), any())).thenReturn(2F);
    Trayecto trayecto = new Trayecto(new ArrayList<>());
    Tramo tramoAPie = new Tramo(new Ubicacion(1, "maipu", 100), new Ubicacion(457, "O'Higgins", 200), new Pie(servicioDistancia));

    Linea unaLinea = new Linea(new ArrayList<>(), new ArrayList<>());
    Parada higgins = new Parada("higgins", 300, new Ubicacion(457, "O'Higgins", 200));
    Parada medrano = new Parada("medrano", 600, new Ubicacion(1, "medrano", 123));
    Parada retiro = new Parada("retiro", 0, new Ubicacion(1, "calle", 123));

    unaLinea.agregarParadaIda(higgins);
    unaLinea.agregarParadaIda(medrano);
    unaLinea.agregarParadaIda(retiro);

    TransportePublico unSubte = new TransportePublico(TipoTransportePublico.SUBTE, unaLinea);
    Tramo tramoEnSubte = new Tramo (higgins.getUbicacion(), retiro.getUbicacion(), unSubte);
    trayecto.agregarTramo(tramoAPie);
    trayecto.agregarTramo(tramoEnSubte);

    assertEquals(trayecto.obtenerDistancia(), 902);

  }

  @Test
  public void verificarCreacionTransportePublico() {
    TransportePublico transportePublico = new TransportePublico(TipoTransportePublico.SUBTE, lineaE());
    assertEquals(transportePublico.getTipoDeTransportePublico(), TipoTransportePublico.SUBTE);
    assertEquals(transportePublico.getLineaDeTransporte().getParadasIda().size(), 3);
  }

  @Test
  public void verificarCreacionVehiculoCotratado() {
    Vehiculo remis = new Vehiculo(TipoVehiculo.AUTO, consumoNafta(), false,ServicioDistancia.getInstancia());
    //assertEquals(remis.getTransporte(), "Vehiculo");
    assertEquals(remis.getTipoDeVehiculo(), TipoVehiculo.AUTO);
  }

  @Test
  public void verificarDistanciaEntreParadas() {
    Linea unaLinea = new Linea(new ArrayList<>(), new ArrayList<>());
    Parada virreyes = new Parada("virreyes", 300, new Ubicacion(1, "eva peron", 123));
    Parada jujuy = new Parada("jujuy", 600, new Ubicacion(1, "jujuy", 123));
    Parada retiro = new Parada("retiro", 0, new Ubicacion(1, "calle", 123));

    unaLinea.agregarParadaIda(virreyes);
    unaLinea.agregarParadaIda(jujuy);
    unaLinea.agregarParadaIda(retiro);

    assertEquals(unaLinea.obtenerDistancia(virreyes.getUbicacion(), retiro.getUbicacion()), 900);

  }

  @Test
  public void SiLaParadaNoPerteneceALaLineaTiraError() {
    Linea unaLinea = new Linea(new ArrayList<>(), new ArrayList<>());
    Parada virreyes = new Parada("virreyes", 300, new Ubicacion(1, "eva peron", 123));
    Parada jujuy = new Parada("jujuy", 600, new Ubicacion(1, "jujuy", 123));
    Parada retiro = new Parada("retiro", 0, new Ubicacion(1, "calle", 123));

    unaLinea.agregarParadaIda(virreyes);
    unaLinea.agregarParadaIda(jujuy);

    assertThrows(ParadaNoExisteException.class, () -> unaLinea.obtenerDistancia(retiro.getUbicacion(), jujuy.getUbicacion()));

  }

  private Linea lineaE() {
    Linea lineaE = new Linea(new ArrayList<>(), new ArrayList<>());
    lineaE.agregarParadaIda(new Parada("virreyes", 300, new Ubicacion(1, "eva peron", 123)));
    lineaE.agregarParadaIda(new Parada("jujuy", 300, new Ubicacion(1, "jujuy", 123)));
    lineaE.agregarParadaIda(new Parada("retiro", 300, new Ubicacion(1, "calle", 123)));
    return lineaE;
  }

  @Test
  public void esTrayectoCompartido() {
    Vehiculo vehiculo = new Vehiculo(TipoVehiculo.AUTO, consumoNafta(), true,ServicioDistancia.getInstancia());
    assertTrue(vehiculo.esCompartible());
  }

  @Test
  public void noEsTrayectoCompartido(){
    Vehiculo vehiculo = new Vehiculo(TipoVehiculo.MOTOCICLETA, consumoNafta(), true,ServicioDistancia.getInstancia());
    assertFalse(vehiculo.esCompartible());
  }

  private Vehiculo unAutoANafta() {
    return new Vehiculo(TipoVehiculo.AUTO, consumoNafta(), true,ServicioDistancia.getInstancia());
  }

  private TipoConsumo consumoNafta() {
    return new TipoConsumo(5, TipoUnidad.lt, "nana", "abc", TipoAlcance.EmisionIndirectaOtros);
  }

}
