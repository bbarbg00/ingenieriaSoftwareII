package colegio;

import java.util.ArrayList;

public class Curso {

  private String id;

  private TablonAnuncios tablon;

  // Aunque en realidad todos los usuarios se almacenan en la lista
  // de usuarios y en la tabla "usuarios" de MySQL,
  // Se construyen estas liastas por comodidad.
  private ArrayList<Usuario> profesores;
  private ArrayList<Usuario> alumnos;
  private ArrayList<Contenido> contenidos;

  // Ficha de evaluacion del curso
  private ArrayList<FichaEvaluacion> fichasEvaluacion;

  public Curso(String id) {

    this.id = id;

    // Se recorre la lista de usuarios de la BBDD y se diferencia entre profesores y alumnos
    ArrayList<Usuario> listaUsuarios = BBDD.getListaUsuarios();

    if (listaUsuarios == null)
      return;

    int i = 0;
    Usuario usuario = null;

    while (i < listaUsuarios.size()) {
    
      usuario = listaUsuarios.get(i);
   
      if (usuario.getEsProfesor())
        profesores.add(usuario);

      if (usuario.getEsAlumno())
        alumnos.add(usuario);

      i++;

    }

    // Se obtiene el tablon de anuncios para este Curso
    this.tablon = BBDD.getTablonAnunciosParaCurso(this);

  }

  public ArrayList<Usuario> getProfesores() {

    return this.profesores;

  }

  public ArrayList<Usuario> getAlumnos() {

    return this.alumnos;

  }

  public ArrayList<Contenido> getContenidos() {

    return this.contenidos;

  }

  public String getId() {

    return this.id;

  }

  public TablonAnuncios getTablon() {

    return this.tablon;

  }

  public ArrayList<FichaEvaluacion> getFichasEvaluacion(boolean usuarioQueLoPideEsProfesor) {

    if (usuarioQueLoPideEsProfesor)
      return this.fichasEvaluacion;
    else
      return null;    

  }

  public FichaEvaluacion getFichaEvaluacionDeAlumno(Usuario alumno) {

    // TODO
      return null;    

  }

  public void ponerNotaAAlumno (Usuario alumno, Contenido contenido, int trimestre, int nota) {

    // TODO

  }

  public void incluirPaginaEnAgenda
        (Usuario alumno, String fecha,
         boolean comioBien,
         int nDeposiciones,
         boolean durmioSiesta,
         String otrosComentarios) {

    // TODO

  }

}
