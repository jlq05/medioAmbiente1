import model.*;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.*;

//TODO: se creo este test innecesario por el tema de checkstyle, ver como solucionarlo

public class TestDatosActividad {
  @Test
  public void verificarDatosActividad() {
    DatosActividad datoActividad = unDatoActividad();
    TipoConsumo tipoConsumo = datoActividad.getTipoConsumo();
    assertEquals(datoActividad.getPeriodicidad(), TipoPeriodicidad.Anual);
    assertEquals(datoActividad.getValor(), 123);

    assertEquals(tipoConsumo.getFactorEmision(), 100);
    assertEquals(tipoConsumo.getTipoDescripcion(), "test");
    assertEquals(tipoConsumo.getTipoActividad(), "diesel");
    assertEquals(tipoConsumo.getTipoUnidad(), TipoUnidad.m3);
    assertEquals(tipoConsumo.getTipoAlcance(), TipoAlcance.EmisionDirecta);
  }

  private DatosActividad unDatoActividad() {
    return new DatosActividad(
        new TipoConsumo(100, TipoUnidad.m3, "test", "diesel", TipoAlcance.EmisionDirecta),
        123,
        TipoPeriodicidad.Anual,
        YearMonth.now()
    );
  }
}