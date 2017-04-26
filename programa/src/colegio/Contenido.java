package colegio;

public class Contenido {

  private String nombre;
  private int horasSemanales;

  public Contenido() {

  }

  public Contenido (String nombre, int horasSemanales) {


    this.nombre = nombre;
    this.horasSemanales = horasSemanales;

  }

  public String getNombre() {

    return this.nombre;

  }

  public int getHorasSemanales() {

    return this.horasSemanales;

  }

  public void setNombre(String nombre) {


    this.nombre = nombre;

  }

  public void setHorasSemanales(int horas) {

    this.horasSemanales = horas;

  }


}
