package colegio;

public class PaginaAgenda {

  private String fecha="";

  private boolean comioBien;
  private int nDeposiciones;
  private boolean durmioSiesta;
  String otrosComentarios;

  public PaginaAgenda(String fecha) {

    this.fecha = fecha;

  }

  public PaginaAgenda
           (String fecha,
            boolean comioBien,
            int nDeposiciones,
            boolean durmioSiesta,
            String otrosComentarios) {

    this.fecha = fecha;

    this.comioBien = comioBien;
    this.nDeposiciones = nDeposiciones;
    this.durmioSiesta  = durmioSiesta;
    this.otrosComentarios = otrosComentarios;

  }

  public String getFecha() {

    return this.fecha;

  }

  public boolean getComioBien() {
  
    return this.comioBien;

  }

  public int getNDeposiciones() {

    return this.nDeposiciones;

  }

  public boolean getDurmioSiesta() {

    return this.durmioSiesta;

  }

  public String getOtrosComentarios() {

    return this.otrosComentarios;

  }

  public void setFecha(String fecha) {

    this.fecha = fecha;

  }

  public void setComioBien(boolean comioBien) {
  
    this.comioBien = comioBien;

  }

  public void setNDeposiciones(int nDeposiciones) {

    this.nDeposiciones = nDeposiciones;

  }

  public void setDurmioSiesta(boolean durmioSiesta) {

    this.durmioSiesta = durmioSiesta;

  }

  public void setOtrosComentarios(String otrosComentarios) {

    this.otrosComentarios = otrosComentarios;

  }

}
