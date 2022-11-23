import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestProcesadorCsv {

  ProcesadorCsv proceso = new ProcesadorCsv();
  @Test
  public void lecturaCsv() throws IOException {
    File file = iniciarValidacion("/test/consumos.csv");
    this.proceso.procesarArchivo(file);

    assertEquals(proceso.getdatos().size(), 3);
  }

  @Test
  public void lecturaSinCsv() throws IOException {
    File file = iniciarValidacion("/test/consumos.csv");
    Assertions.assertThrows( ArchivoException.class, () -> this.proceso.procesarArchivo(null));
  }

  @Test
  public void columnasIncorrectas()  {
    File file = iniciarValidacion("/test/consumosColumnasError.csv");
    validacionTest(file, "el excel tiene un formato incorrecto");
  }

  @Test
  public void tipoConsumoNoDefinido()  {
    File file = iniciarValidacion("/test/consumosError.csv");
    validacionTest(file, "el tipo de consumo no esta definido");
  }

  @Test
  public void numberFormat()  {
    File file = iniciarValidacion("/test/consumosValorError.csv");
    validacionTest(file, "no es un numero el campo");
  }

  @Test
  public void longitudFechaIncorrepta()  {
    File file = iniciarValidacion("/test/consumosFechaError.csv");
    validacionTest(file, "No cumple con la fecha");
  }

  private File iniciarValidacion(String base) {
    Path path = Paths.get("");
    String ruta = path.toAbsolutePath().toString() + base;
    File file = new File(ruta);
    return file;
  }

  private void validacionTest(File file, String msj) {
    ArchivoException thrown = Assertions
        .assertThrows(ArchivoException.class, () -> this.proceso.procesarArchivo(file));
    Assertions
        .assertEquals(msj,
            thrown.getMessage());
  }

  private DatosActividad unDatoActividad() {
    TipoConsumo gas = new TipoConsumo(0,TipoUnidad.m3,"Gas Natural","Combustion fija", TipoAlcance.EmisionDirecta);
    return new DatosActividad(
        gas,
        2,
        TipoPeriodicidad.Anual,
        YearMonth.now());
  }



}

