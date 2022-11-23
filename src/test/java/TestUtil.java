import org.junit.jupiter.api.Test;
import domain.services.distancia.ServicioDistancia;
import domain.services.distancia.entities.Ubicacion;
import org.junit.jupiter.api.Test;
import utils.YearMonthDateAttributeConverter;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestUtil {
  @Test
  public void convertirAColumnaSql() {
    YearMonthDateAttributeConverter utils = new YearMonthDateAttributeConverter();
    assertEquals(utils.convertToDatabaseColumn(YearMonth.of(2022, 3)), Date.valueOf("2022-03-01"));
  }

  @Test
  public void convertirAUnaEntidadAtributo() {
    YearMonthDateAttributeConverter utils = new YearMonthDateAttributeConverter();
    assertEquals(utils.convertToEntityAttribute(Date.valueOf("2022-03-01")), YearMonth.of(2022, 3));
  }
}
