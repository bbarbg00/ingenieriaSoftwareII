package colegio;

import java.util.ArrayList;

public class Usuario {

  protected String id;
  protected String nombre;
  protected String apellido1;
  protected String apellido2;
  protected String telefono;
  protected String email;
  protected String direccion;
  protected boolean esAdministrador;
  protected boolean esProfesor;
  protected boolean esAlumno;
  protected boolean esSecretario;
  protected boolean esEncargadoCocina;

  // Solo para alumnos
  protected ArrayList<PaginaAgenda> agenda;

  public Usuario (String id) {

    this.id = id;

  }

  public Usuario 
           (String id,
            String nombre,
            String apellido1,
            String apellido2,
            String telefono,
            String email,
            String direccion,
            String esAdministrador,
            String esProfesor,
            String esAlumno,
	    String esSecretario,
	    String esEncargadoCocina) {

    this.setId(id);
    this.setNombre(nombre);
    this.setApellido1(apellido1);
    this.setApellido2(apellido2);
    this.setTelefono(telefono);
    this.setEmail(email);
    this.setDireccion(direccion);
    this.setEsAdministrador(esAdministrador);
    this.setEsProfesor(esProfesor);
    this.setEsAlumno(esAlumno);
    this.setEsSecretario(esSecretario);
    this.setEsEncargadoCocina(esEncargadoCocina);

    if (this.getEsAlumno())
      agenda = new ArrayList<PaginaAgenda>();

  }

  protected void cargarDeBBDD () {


  }

  public void modificarCampo (String nombreCampo, String nuevoValor) {

    String nombreEnMinusculas = nombreCampo.toLowerCase();

    if (nombreEnMinusculas.equals("nif"))
      this.setNombre(nuevoValor);

    if (nombreEnMinusculas.equals("nombre"))
      this.setNombre(nuevoValor);

    if (nombreEnMinusculas.equals("apellido1"))
      this.setApellido1(nuevoValor);

    if (nombreEnMinusculas.equals("apellido2"))
      this.setApellido2(nuevoValor);

    if (nombreEnMinusculas.equals("telefono"))
      this.setTelefono(nuevoValor);

    if (nombreEnMinusculas.equals("email"))
      this.setEmail(nuevoValor);

    if (nombreEnMinusculas.equals("direccion"))
      this.setDireccion(nuevoValor);

    if (nombreEnMinusculas.equals("esadministrador"))
      this.setEsAdministrador(nuevoValor);

    if (nombreEnMinusculas.equals("esprofesor"))
      this.setEsProfesor(nuevoValor);

    if (nombreEnMinusculas.equals("esalumno"))
      this.setEsAlumno(nuevoValor);

    if (nombreEnMinusculas.equals("essecretario"))
      this.setEsSecretario(nuevoValor);

    if (nombreEnMinusculas.equals("esencargadococina"))
      this.setEsEncargadoCocina(nuevoValor);

  }

  // Metodos get

  public String getCampo (String nombreCampo) {

    String nombreEnMinusculas = nombreCampo.toLowerCase();

    if (nombreEnMinusculas.equals("nif"))
      return this.getId();

    if (nombreEnMinusculas.equals("nombre"))
      return this.getNombre();

    if (nombreEnMinusculas.equals("apellido1"))
      return this.getApellido1();

    if (nombreEnMinusculas.equals("apellido2"))
      return this.getApellido2();

    if (nombreEnMinusculas.equals("telefono"))
      return this.getTelefono();

    if (nombreEnMinusculas.equals("email"))
      return this.getEmail();

    if (nombreEnMinusculas.equals("direccion"))
      return this.getDireccion();

    if (nombreEnMinusculas.equals("esadministrador"))
      return BBDD.booleanToIntString(this.getEsAdministrador());

    if (nombreEnMinusculas.equals("esprofesor"))
      return BBDD.booleanToIntString(this.getEsProfesor());

    if (nombreEnMinusculas.equals("esalumno"))
      return BBDD.booleanToIntString(this.getEsAlumno());

    if (nombreEnMinusculas.equals("essecretario"))
      return BBDD.booleanToIntString(this.getEsSecretario());

    if (nombreEnMinusculas.equals("esencargadococina"))
      return BBDD.booleanToIntString(this.getEsEncargadoCocina());
   
    // si no es ninguno de los validos...
    return "";

  }

  public String getNombre () {

    return this.nombre;

  }

  public String getId() {

    return this.id;

  }

  public String getNombreyApellidos() {

    return this.nombre + " " + this.apellido1 + " " + this.apellido2;

  }

  public String getApellido1 () {

    return this.apellido1;

  }

  public String getApellido2 () {

    return this.apellido2;

  }

  public String getTelefono () {

    return this.telefono;

  }

  public String getEmail () {

    return this.email;

  }

  public String getDireccion () {

    return this.direccion;

  }

  public boolean getEsAdministrador() {

    return this.esAdministrador;

  }

  public boolean getEsProfesor() {

    return this.esProfesor;

  }

  public boolean getEsAlumno() {

    return this.esAlumno;

  }

  public boolean getEsSecretario() {

    return this.esSecretario;

  }

  public boolean getEsEncargadoCocina() {

    return this.esEncargadoCocina;

  }

  public ArrayList<PaginaAgenda> getAgenda() {

    if (this.getEsAlumno())
      return this.agenda;
    else
      return null;

  }
  
  // Metodos set

  public void setNombre (String nombre) {

    this.nombre = nombre;

  }

  public void setId(String id) {

    this.id = id;

  }

  public void setApellido1 (String apellido1) {

    this.apellido1 = apellido1;

  }

  public void setApellido2 (String apellido2) {

    this.apellido2 = apellido2;

  }

  public void setTelefono (String telefono) {

    this.telefono = telefono;

  }

  public void setEmail (String email) {

    this.email = email;

  }

  public void setDireccion (String direccion) {

    this.direccion = direccion;

  }

  public void setEsAdministrador (boolean valor) {

    this.esAdministrador = valor;

  }

  public void setEsProfesor (boolean valor) {

    this.esProfesor = valor;

  }

  public void setEsAlumno (boolean valor) {

    this.esAlumno = valor;

    if ((this.esAlumno ) && (agenda == null))
      agenda = new ArrayList<PaginaAgenda>();

  }

  public void setEsSecretario (boolean valor) {

    this.esSecretario = valor;

  }

  public void setEsEncargadoCocina (boolean valor) {

    this.esEncargadoCocina = valor;

  }

  public void setEsAdministrador (String valor) {

    this.esAdministrador = false;

    if (valor != null) {

      if ((valor.toLowerCase().equals("true")) ||
          (valor.equals("1")))
        this.esAdministrador = true;

    }
      
  }

  public void setEsProfesor (String valor) {

    this.esProfesor = false;

    if (valor != null) {

      if ((valor.toLowerCase().equals("true")) ||
          (valor.equals("1")))
        this.esProfesor = true;

    }

  }

  public void setEsAlumno (String valor) {

    this.esAlumno = false;

    if (valor != null) {

      if ((valor.toLowerCase().equals("true")) ||
          (valor.equals("1"))) {

        this.setEsAlumno(true);
      }

    }

  }

  public void setEsSecretario (String valor) {

    this.esSecretario = false;

    if (valor != null) {

      if ((valor.toLowerCase().equals("true")) ||
          (valor.equals("1"))) {

        this.setEsSecretario(true);
      }

    }

  }

  public void setEsEncargadoCocina (String valor) {

    this.esEncargadoCocina = false;

    if (valor != null) {

      if ((valor.toLowerCase().equals("true")) ||
          (valor.equals("1"))) {

        this.setEsEncargadoCocina(true);
      }

    }

  }

  public void println () {

    System.out.println(this.id + " - " +
                       this.nombre + " - " +
                       this.apellido1 + " - " +
                       this.apellido2 + " - " +
                       this.telefono + " - " +
                       this.email + " - " +
                       this.direccion);

  }

}
