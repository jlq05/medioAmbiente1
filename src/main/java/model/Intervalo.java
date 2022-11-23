package model;

import java.time.LocalDate;
import java.time.YearMonth;

public class Intervalo {
  YearMonth fechaDesde;
  YearMonth fechaHasta;

  public Intervalo(YearMonth fechaDesde, YearMonth fechaHasta) {
    this.fechaDesde = fechaDesde;
    this.fechaHasta = fechaHasta;
  }

  public YearMonth getFechaDesde() {
    return fechaDesde;
  }

  public void setFechaDesde(YearMonth fechaDesde) {
    this.fechaDesde = fechaDesde;
  }

  public YearMonth getFechaHasta() {
    return fechaHasta;
  }

  public void setFechaHasta(YearMonth fechaHasta) {
    this.fechaHasta = fechaHasta;
  }
}
