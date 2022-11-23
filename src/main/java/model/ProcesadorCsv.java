package model;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ProcesadorCsv implements ProcesadorArchivos {

  List<TipoConsumo> tipoConsumos = this.cargarTiposConsumoBase();
  List<DatosActividad> datosActividad = new ArrayList<>();

  public List<TipoConsumo> getTipoConsumos() {
    return tipoConsumos;
  }

  public void procesarArchivo(File archivo) {
    try {
      InputStream inputStream = new FileInputStream(archivo);
      Reader fileReader = new InputStreamReader(inputStream, "UTF-8");
      CSVParser conPuntoComa = new CSVParserBuilder().withSeparator(';').build();
      CSVReader reader = new CSVReaderBuilder(fileReader).withCSVParser(conPuntoComa).build();
      validarArchivo(reader);
      List<String[]> listaDatos = reader.readAll();
      String[] nombreColum =  listaDatos.get(0);
      listaDatos.remove(0);

      for (int i = 0; i < listaDatos.size(); i++) {


        String[] columnas = listaDatos.get(i);
        this.validarColumna(columnas, nombreColum);
        this.validarConsumo(columnas);
        this.validarUnidadTipo(columnas);
        this.validarUnidad(columnas);
        this.validarFecha(columnas);
        this.cargarDato(columnas);
      }
      reader.close();
      fileReader.close();
    } catch (ValorNegativoInvalidoException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new ArchivoException(ex.getMessage());
    }
  }

  private void validarArchivo(CSVReader reader) {
    if (!reader.verifyReader()) {
      throw new ArchivoException("Ocurrio un error al querer leerlo");
    }
  }

  private void validarColumna(String[] columnas, String[] nombreColumnas) {
    Object[] sinEspacio = Arrays.stream(nombreColumnas)
        .filter(x -> !x.equals(""))
        .toArray();
    if (columnas.length != sinEspacio.length) {
      throw new ArchivoException("el excel tiene un formato incorrecto");
      //podria pasarle la fila y la cantidad de excepciones en la expcecion ??
    }
  }

  private void validarConsumo(String[] columnas) {

    if (!this.isMember(columnas[0])) {
      throw new ArchivoException("el tipo de consumo no esta definido");
    }
  }

  private void validarUnidadTipo(String[] columnas) {
    if (!isNumeric(columnas[1])) {
      throw new ArchivoException("no es un numero el campo");
    }
  }

  private void validarUnidad(String[] columnas) {

    if (Integer.parseInt(columnas[1]) <= 0) {
      throw new ValorNegativoInvalidoException();
    }
  }

  private void validarFecha(String[] columnas) {

    if ((columnas[2].toLowerCase(Locale.ROOT).equals("anual"))
        || (columnas[2].toLowerCase(Locale.ROOT).equals("mensual"))) {
      //revisar formato de fechas
      if (columnas[2].toLowerCase(Locale.ROOT).equals("anual")) {
        if (columnas[3].length() != 4) {
          throw new ArchivoException("No cumple con la fecha");
        }
        if (!isNumeric(columnas[3])) {
          throw new NumberFormatException();
        }
      } else {
        if (columnas[3].length() != 7) {
          throw new ArchivoException("No cumple con la fecha");
        }
        String[] fechas = columnas[3].split("/");
        if (fechas[0].length() != 2 || fechas[1].length() != 4) {
          throw new ArchivoException("No cumple con la fecha");
        }
        if (!isNumeric(fechas[0]) || !isNumeric(fechas[1])) {
          throw new NumberFormatException();
        }
      }
    } else {
      throw new ArchivoException("Periodicidad no definida");
    }
  }

  private static boolean isNumeric(String str) {
    return str != null && str.matches("[0-9.]+");
  }

  private boolean isMember(String name) {
    for (TipoConsumo tipoConsumo : tipoConsumos) {
      if (tipoConsumo.getTipoDescripcion().equals(name)) {
        return true;
      }
    }
    return false;
  }

  private void cargarDato(String[] columnas) {
    for (TipoConsumo tipoConsumo : tipoConsumos) {
      if (tipoConsumo.getTipoDescripcion().equals(columnas[0])) {

        if (columnas[2].toLowerCase(Locale.ROOT).equals("anual")) {

          DatosActividad unDato = new DatosActividad(tipoConsumo,
              Integer.parseInt(columnas[1]),
              TipoPeriodicidad.Anual,
              YearMonth.of(Integer.parseInt(columnas[3]),1)
          );
          datosActividad.add(unDato);
        } else {
          String[] fechas = columnas[3].split("/");
          DatosActividad unDato = new DatosActividad(
              tipoConsumo,
              Integer.parseInt(columnas[1]),
              TipoPeriodicidad.Mensual,
              YearMonth.of(Integer.parseInt(fechas[1]),Integer.parseInt(fechas[0]))
          );
          datosActividad.add(unDato);
        }

      }
    }
  }

  public List<DatosActividad> getdatos() {
    return this.datosActividad;
  }

  private  List<TipoConsumo> cargarTiposConsumoBase() {
    //al comienzo seteamos el valor de factorEmision en 0 luego cambiar
    List<TipoConsumo> tiposConsumos = new ArrayList<>();

    TipoConsumo gas = new TipoConsumo(1, TipoUnidad.m3, "Gas Natural",
        "Combustion fija", TipoAlcance.EmisionDirecta);
    TipoConsumo diesel = new TipoConsumo(0, TipoUnidad.lt, "Diesel",
        "Combustion fija", TipoAlcance.EmisionDirecta);
    TipoConsumo gasoil = new TipoConsumo(0, TipoUnidad.lt, "Gasoil",
        "Combustion fija", TipoAlcance.EmisionDirecta);
    TipoConsumo nafta = new TipoConsumo(2, TipoUnidad.lt, "Nafta",
        "Combustion fija", TipoAlcance.EmisionDirecta);
    TipoConsumo carbon = new TipoConsumo(4, TipoUnidad.kg, "Carbon",
        "Combustion fija", TipoAlcance.EmisionDirecta);
    TipoConsumo combustibleGasoil = new TipoConsumo(0, TipoUnidad.lt,
        "Combustible consumido Gasoil", "Combustion movil", TipoAlcance.EmisionDirecta);
    TipoConsumo combustibleNafta = new TipoConsumo(0, TipoUnidad.lt, "Combustible consumido Nafta",
        "Combustion movil", TipoAlcance.EmisionDirecta);
    TipoConsumo electricidad = new TipoConsumo(0, TipoUnidad.kwh, "Electricidad",
        "Electricidad adquirida y consumida", TipoAlcance.EmisionIndirectaElectricidad);
    TipoConsumo camionDeCarga = new TipoConsumo(0, TipoUnidad.ninguno, "Camion de carga",
        "Logística de productos y residuos", TipoAlcance.EmisionIndirectaOtros);
    TipoConsumo utilitarioLiviano = new TipoConsumo(0, TipoUnidad.ninguno, "Utilitario liviano",
        "Logística de productos y residuos", TipoAlcance.EmisionIndirectaOtros);
    TipoConsumo distancia = new TipoConsumo(0, TipoUnidad.km, "Distancia media recorrida",
        "Logística de productos y residuos", TipoAlcance.EmisionIndirectaOtros);

    tiposConsumos.add(gas);
    tiposConsumos.add(diesel);
    tiposConsumos.add(gasoil);
    tiposConsumos.add(nafta);
    tiposConsumos.add(carbon);
    tiposConsumos.add(combustibleGasoil);
    tiposConsumos.add(combustibleNafta);
    tiposConsumos.add(electricidad);
    tiposConsumos.add(camionDeCarga);
    tiposConsumos.add(utilitarioLiviano);
    tiposConsumos.add(distancia);
    return tiposConsumos;
  }

  public void agregarTipoConsumo(TipoConsumo consumo) {
    this.tipoConsumos.add(consumo);
  }
}