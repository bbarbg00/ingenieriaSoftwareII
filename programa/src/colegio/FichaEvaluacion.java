package colegio;

public class FichaEvaluacion {

  private Contenido contenido;
  private int trimestre;
  private int nota;

  public FichaEvaluacion(Contenido contenido) {

  }

  public FichaEvaluacion(Contenido contenido, int trimestre, int nota) {

  }

  public void setNota(int nota) {

    this.nota = nota;

  }

  public void setTrimestre (int trimestre) {


  }

  public void setContenido(Contenido contenido) {


  }

  public int getNota(){

    return this.nota;

  }


  public int getTrimestre() {

    return this.trimestre;

  }


  public Contenido getContenido() {

    return this.contenido;

  }
  

}
