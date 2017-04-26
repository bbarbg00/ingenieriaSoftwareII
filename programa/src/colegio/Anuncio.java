package colegio;

public class Anuncio {

  private String texto;

  private String fecha;

  public Anuncio (String texto, String fecha) {

    this.texto = texto;
    this.fecha = fecha;
    
  }

  public String getFecha() {

    return this.fecha;

  }

  public String getTexto () {

    return this.texto;

  }

  public void setFecha(String fecha) {

    this.fecha = fecha;

  }

  public void setTexto (String texto) {

    this.texto = texto;

  }


}
