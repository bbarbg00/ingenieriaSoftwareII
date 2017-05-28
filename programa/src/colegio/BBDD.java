package colegio;

// para poder acceder a la BBDD
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

// para poder leer las claves encriptadas
import java.security.*;

import static java.nio.file.StandardCopyOption.*;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BBDD{
	
	
	static final String NOMBRE_BD_MYSQL     = "colegio";
	static final String PASSWORD_ROOT_MYSQL = "1234";

  private static Connection conexion;

  // Listas de datos cargados de las BBDD MySQL
  private static ArrayList<Usuario> listaUsuarios = null;

  private static ArrayList<Curso> listaCursos = null;

  private static ArrayList<TablonAnuncios> tablonesAnuncios = null;

  // Usuario actualmente logeado
  private static Usuario usuarioConectado = null;

  public BBDD() {
  	
  	listaUsuarios = null;
    listaCursos = null;
    tablonesAnuncios = null;
    usuarioConectado = null;

    try {
      // Conectamos a la BBDD externa
      this.connectToMySQL("root", PASSWORD_ROOT_MYSQL, "");

      crearBaseDeDatos ();
      crearTablasVacias();
      incluirUsuarioPorDefecto();
     
      // Obtenemos los datos
      cargarUsuarios();
    }
    catch (Exception ex){

    }

  }

  private static void imprimirErrorSQLySalir (SQLException ex) {

    System.out.println("");
    System.out.println("** SQLException: " + ex.getMessage());
    System.out.println("** SQLState: " + ex.getSQLState());
    System.out.println("** VendorError: " + ex.getErrorCode());

    System.exit(0);

  }

  private void connectToMySQL (String usuario, String pass, String nombreBD) throws Exception {

    try {
    
      System.out.print("Estableciendo conexion con BBDD MySQL... ");

      Class.forName("com.mysql.jdbc.Driver");
      conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + nombreBD, usuario, pass);
      
      System.out.println("Ok");
    
    }
    catch (SQLException ex) {
		
      imprimirErrorSQLySalir(ex);

    }
    
  }

  private static ResultSet executeSQLQuery (String query) {

    Statement SQLStatement = null;
    ResultSet queryResult = null;

    try {

      SQLStatement = conexion.createStatement();
      queryResult = SQLStatement.executeQuery(query);      

    }
    catch (SQLException ex) {

      imprimirErrorSQLySalir(ex);

    }
   
    return queryResult;

  }

  private static int executeSQLUpdate (String query) {

    Statement SQLStatement = null;
    int updateResult = -1;

    try {

      SQLStatement = conexion.createStatement();
      updateResult = SQLStatement.executeUpdate(query);      

    }
    catch (SQLException ex) {

      imprimirErrorSQLySalir(ex);

    }
   
    return updateResult;

  }

  // Cargar usuarios
  // Devuelve false si algo falla
  private static boolean cargarUsuarios () {

    ResultSet queryResult = null;

    // inicializamos la lista
    listaUsuarios = new ArrayList<Usuario> ();

    Usuario usuario = null;

    System.out.print("Cargando datos de USUARIOS de BBDD... ");
   
    queryResult = executeSQLQuery("SELECT * FROM usuarios");

    try {

      // Obtenemos todos los datos (usuarios)
      while (queryResult.next()) {

        usuario = new Usuario (
          queryResult.getString("nif"),
          queryResult.getString("nombre"),
          queryResult.getString("apellido1"),
          queryResult.getString("apellido2"),
          queryResult.getString("telefono"),
          queryResult.getString("email"),
          queryResult.getString("direccion"),
          queryResult.getString("esAdministrador"),
          queryResult.getString("esProfesor"),
          queryResult.getString("esAlumno"),
	  queryResult.getString("esSecretario"),
	  queryResult.getString("esEncargadoCocina"));

        // Imprimimos el usuario (para ver que lo ha cargado)
        //usuario.println();

        listaUsuarios.add(usuario);

      }

    } 
    catch (SQLException ex) {
		
      imprimirErrorSQLySalir(ex);

    }

    System.out.println("Ok");

    // devolver true si todo fue bien
    return true;
   
  }

  private static void cargarTablonesDeAnuncios() {

    tablonesAnuncios = new ArrayList<TablonAnuncios>();

    // TODO

  }

  public static TablonAnuncios getTablonAnunciosParaCurso(Curso curso) {

    // TODO

    return null;

  }

  // ** Backup
  // Codigo sacado de http://chuwiki.chuidiang.org/index.php?title=Backup_de_MySQL_con_Java
  // Devuelve true si pudo crear el backup
  public static boolean hacerBackup (String rutaAbsolutaFicheroSinExtension) {
  	
  	// Incluimos extension si no la tiene ya
  	String rutaAbsolutaFichero = rutaAbsolutaFicheroSinExtension;
  	
  	if (!rutaAbsolutaFicheroSinExtension.endsWith(".sql"))	   
  	  rutaAbsolutaFichero = rutaAbsolutaFicheroSinExtension + ".sql";
	  
	  try {
	  	
			Process p = Runtime
			          .getRuntime()
			          .exec("mysqldump -u root -p" + PASSWORD_ROOT_MYSQL + " " + NOMBRE_BD_MYSQL);
			
			InputStream is = p.getInputStream();
      FileOutputStream fos = 
      		new FileOutputStream(rutaAbsolutaFichero);
      byte[] buffer = new byte[1000];
 
      int leido = is.read(buffer);
      while (leido > 0) {
         fos.write(buffer, 0, leido);
         leido = is.read(buffer);
      }
 
      fos.close();
      return true;
			
		} catch (IOException e) {
			
			e.printStackTrace();
			return false;
			
		}
	  
	  
  }

  // Codigo sacado de http://chuwiki.chuidiang.org/index.php?title=Backup_de_MySQL_con_Java
  // Devuelve true si pudo restaurar la BD desde el backup
  public static boolean restaurarDesdeBackup (String rutaAbsolutaFichero) {

    try {
    	  	 	
    	// Restauramos    	
      Process p = Runtime
            .getRuntime()
            .exec("mysql -u root -p" + PASSWORD_ROOT_MYSQL + " " + NOMBRE_BD_MYSQL);
 
      OutputStream os = p.getOutputStream();
      FileInputStream fis = new FileInputStream(rutaAbsolutaFichero);
      byte[] buffer = new byte[1000];
 
      int leido = fis.read(buffer);
      while (leido > 0) {
         os.write(buffer, 0, leido);
         leido = fis.read(buffer);
      }
 
      os.flush();
      os.close();
      fis.close();
 
   } catch (Exception e) {
      e.printStackTrace();
      return false;
   }
    
  
    
    return true;

  }

  public static boolean validarLogin (String nif, String password) {

    // Para que el usuario sea valido, la lista no puede estar vacia
    if ((listaUsuarios == null) ||
         listaUsuarios.isEmpty()) {

      System.out.println("No hay usuarios en la Base de Datos. Terminando aplicación...");
      System.exit(0);

    }
      
    int i = 0;
    Usuario usuario = null;

    while (i < listaUsuarios.size()) {
    
      usuario = listaUsuarios.get(i);
   
      if (usuario.getId().equals(nif))
      {
        // El usuario esta en la lista.
        // Por seguridad, no se almacenan las contraseñas en Java,
        // solo en MySQL.
        // Comprobamos en la BBDD que el usuario existe

        try {

          ResultSet queryResult  = 
            executeSQLQuery("SELECT nif FROM usuarios WHERE password=MD5('" + password + "') AND nif='"+ nif +"'");

          // Comprobamos que:
          // - Se encontro algun usuario
          // - Coincide el NIF  
          if (queryResult.next() &&
              (queryResult.getString("nif").equals(nif))) {

            usuarioConectado = listaUsuarios.get(i);
            return true;

          }
         
        }
        catch (SQLException ex){

            imprimirErrorSQLySalir(ex);

        }
      }
      
      i++;

    }

    return false;

  }

  public static Usuario getUsuarioConectado() {

    return usuarioConectado;

  }

  public static void logoutUsuarioConectado() {

    if (hayUsuarioConectado())
      usuarioConectado = null;

  }

  public static boolean hayUsuarioConectado() {

    return (usuarioConectado != null);

  }

  public static ArrayList<Usuario> getListaUsuarios() {

    return listaUsuarios;

  }

  // Devuelve true si consigue borrar al usuario
  public static boolean borrarUsuario (Usuario usuarioABorrar) {

    // Para que el usuario sea valido, la lista no puede estar vacia
    if ((listaUsuarios == null) ||
         listaUsuarios.isEmpty())
       return false;

    // Si el usuario es el actual no se deja borrarlo
    if (BBDD.getUsuarioConectado().getId() == usuarioABorrar.getId())
      return false;

    int i = 0;
    Usuario usuario = null;

    while (i < listaUsuarios.size()) {
    
      usuario = listaUsuarios.get(i);
   
      if (usuario.getId().equals(usuarioABorrar.getId()))
      {
        // Se borra de la lista de usuarios
        listaUsuarios.remove(i);

        // Se borra de la BBDD de MySQL
        executeSQLUpdate("DELETE FROM usuarios WHERE nif='"+ usuarioABorrar.getId() +"'");

        return true;         
     
      }

      i++;

    }

    // si llegamos aqui, habia usuarios pero no encontro el que se queria borrar
    return false;

  }

  // Lo mismo que la anterior pero para borrar por NIF
  public static boolean borrarUsuario (String nifUsuarioABorrar) {
 
    Usuario usuario = new Usuario(nifUsuarioABorrar);

    return borrarUsuario(usuario);

  }


  // Devuelve un puntero al usuario con nif facilitado
  // en la lista this.listaUsuarios.
  private static Usuario getUsuarioConNIF (String nif) {

    if ((listaUsuarios == null) ||
         listaUsuarios.isEmpty())
      return null;

    int i = 0;
    Usuario usuario = null;

    while (i < listaUsuarios.size()) {

      usuario = listaUsuarios.get(i);
   
      if (usuario.getId().equals(nif))
        return usuario;
      
      i++;

    }

    return null;

  }

  // Se comprueba si ya hay un usuario con ese NIF
  // en la lista cargada de la BBDD MySQL
  public static boolean existeUsuarioConNIF (String nif) {

    return (getUsuarioConNIF(nif) != null);

  }

  public static boolean esNIFValido (String nif) {

    return (!nif.isEmpty() && !existeUsuarioConNIF(nif));

  }

  public static void cambiarPasswordUsuarioConNif (String nuevaPassword, String nifUsuario) {

    String sqlQuery = 
      "UPDATE usuarios SET password =MD5('" + nuevaPassword + "') WHERE nif='" + nifUsuario + "'";

    //System.out.println(sqlQuery);

    executeSQLUpdate(sqlQuery);


  }
 
  public static void cambiarPasswordUsuarioActual (String nuevaPassword) {

    if (!hayUsuarioConectado())
      return;

    cambiarPasswordUsuarioConNif(nuevaPassword, usuarioConectado.getId());

  }


  public static void actualizarCampoDeUsuario
      (String nifEnBBDD, String nombreCampo, String nuevoValorCampo) {

    Usuario punteroAUsuario = getUsuarioConNIF(nifEnBBDD);

    if (punteroAUsuario == null)
      return;

    // Si estamos aqui, se encontro el usuario.
    // Se modifica el campo primero en la lista y luego en la BBDD.
    punteroAUsuario.modificarCampo(nombreCampo, nuevoValorCampo);

    String sqlQuery = "UPDATE usuarios SET " + nombreCampo.toLowerCase() + "='" + punteroAUsuario.getCampo(nombreCampo) + "' WHERE nif='" + nifEnBBDD + "'";

    //System.out.println(sqlQuery);

    executeSQLUpdate(sqlQuery);

  }

  // Devuelve la cadena "1" si el valor es true (boolean), "0" en otro caso.
  // Es necesario porque en MySQL los boolean se almacenan como "1" o "0".
  public static String booleanToIntString (boolean valor) {

    if (valor)
      return "1";
    else
      return "0";

  }

  public static void insertarUsuario (Usuario usuario) {

    if (!esNIFValido(usuario.getId()))
      return;

    // Insertamos el usuario
    String sqlQuery = 
    "INSERT INTO usuarios VALUES ("  +
    "'"     + usuario.getId()        + "'," +
    "MD5('" + "1234"                 + "'),"+
    "'"     + usuario.getNombre()    + "'," +
    "'"     + usuario.getApellido1() + "'," +
    "'"     + usuario.getApellido2() + "'," +
    "'"     + usuario.getEmail()     + "'," + 
    "'"     + usuario.getTelefono()  + "'," +
    "'"     + usuario.getDireccion() + "'," +
    "'"     + booleanToIntString(usuario.getEsAdministrador()) + "'," +
    "'"     + booleanToIntString(usuario.getEsProfesor()) + "'," +
    "'"     + booleanToIntString(usuario.getEsAlumno()) + "'," +
    "'"     + booleanToIntString(usuario.getEsSecretario()) + "'," +
    "'"     + booleanToIntString(usuario.getEsEncargadoCocina()) + "'"  +
    ");";

    executeSQLUpdate(sqlQuery);

    listaUsuarios.add(usuario);

  }

  // ***** Cursos *****

 // Devuelve true si consigue borrar al usuario
  public static boolean borrarCurso (Curso cursoABorrar) {

    // Para que el usuario sea valido, la lista no puede estar vacia
    if ((listaCursos == null) ||
         listaCursos.isEmpty())
       return false;

    int i = 0;
    Curso curso = null;

    while (i < listaCursos.size()) {
    
      curso = listaCursos.get(i);
   
      if (curso.getId().equals(cursoABorrar.getId()))
      {
        // Se borra de la lista de cursos
        listaCursos.remove(i);

        // Se borra de la BBDD de MySQL
        executeSQLUpdate("DELETE FROM cursos WHERE id='"+ cursoABorrar.getId() +"'");

        return true;         
     
      }

      i++;

    }

    // si llegamos aqui, habia cursos pero no encontro el que se queria borrar
    return false;

  }

  // Lo mismo que la anterior pero para borrar por id
  public static boolean borrarCurso (String idCursoABorrar) {
 
    Curso curso = new Curso(idCursoABorrar);

    return borrarCurso(curso);

  }

  public static ArrayList<Curso> getListaCursos() {

    return listaCursos;

  }

  // Devuelve un puntero al curso con id facilitado
  // en la lista this.listaCursos.
  private static Curso getCursoConId (String id) {

    if ((listaCursos == null) ||
         listaCursos.isEmpty())
      return null;

    int i = 0;
    Curso curso = null;

    while (i < listaCursos.size()) {

      curso = listaCursos.get(i);
   
      if (curso.getId().equals(id))
        return curso;
      
      i++;

    }

    return null;

  }

  public static boolean existeCursoConId (String id) {

    return (getCursoConId(id) != null);

  }

  public static boolean esIDCursoValido (String id) {

    return (!id.isEmpty() && !existeCursoConId(id));

  }
   
  public static void insertarCurso (Curso curso) {

    if (!esNIFValido(curso.getId()))
      return;

    // Insertamos el usuario
    String sqlQuery = "";
    /*"INSERT INTO cursos VALUES ("  +
    "'"     + usuario.getId()        + "'," +
    "MD5('" + "1234"                 + "'),"+
    "'"     + usuario.getNombre()    + "'," +
    "'"     + usuario.getApellido1() + "'," +
    "'"     + usuario.getApellido2() + "'," +
    "'"     + usuario.getEmail()     + "'," + 
    "'"     + usuario.getTelefono()  + "'," +
    "'"     + usuario.getDireccion() + "'," +
    "'"     + booleanToIntString(usuario.getEsAdministrador()) + "'," +
    "'"     + booleanToIntString(usuario.getEsProfesor()) + "'," +
    "'"     + booleanToIntString(usuario.getEsAlumno()) + "'," +
    "'"     + booleanToIntString(usuario.getEsSecretario()) + "'," +
    "'"     + booleanToIntString(usuario.getEsEncargadoCocina()) + "'"  +
    ");";*/

    executeSQLUpdate(sqlQuery);

    listaCursos.add(curso);

  }

  // *** Metodo para crear BBDD y tablas vacias, por si no existieran
  // Crear la BBDD si no existe
  private void crearBaseDeDatos () {

    executeSQLUpdate("CREATE DATABASE IF NOT EXISTS " + NOMBRE_BD_MYSQL);

    // Seleccionar la BBDD creada
    executeSQLUpdate("USE " + NOMBRE_BD_MYSQL);
  }

  private static void crearTablasVacias() {

    // Crear las tablas
    executeSQLUpdate
      ("CREATE TABLE IF NOT EXISTS usuarios (" +
          "nif             VARCHAR(9)," +
          "password        VARCHAR(32)," +
          "nombre          VARCHAR(25)," +
          "apellido1       VARCHAR(25)," +
          "apellido2       VARCHAR(25)," +
          "email           VARCHAR(25)," +
          "telefono        VARCHAR(20)," +
          "direccion       VARCHAR(50)," +
          "esAdministrador BOOLEAN," +
          "esProfesor      BOOLEAN," +
          "esAlumno        BOOLEAN," +
          "esSecretario    BOOLEAN," +
          "esEncargadoCocina BOOLEAN," +
          "PRIMARY KEY (nif))"
        );

    executeSQLUpdate
      ("CREATE TABLE IF NOT EXISTS cursos (" +
          "id          VARCHAR(25)," +
          "PRIMARY KEY (id))"
        );

    executeSQLUpdate
      ("CREATE TABLE IF NOT EXISTS contenidos (" +
          "id              VARCHAR(25)," +
          "horasSemanales  TINYINT," +
          "PRIMARY KEY (id))"
        );

    executeSQLUpdate
      (" CREATE TABLE IF NOT EXISTS planDeEstudios (" +
          "idContenido     VARCHAR(25)," +
          "idCurso         VARCHAR(25)," +
          "PRIMARY KEY (idContenido, idCurso))"
        );

    executeSQLUpdate
      ("CREATE TABLE IF NOT EXISTS planDocente (" +
          "nifProfesor     VARCHAR(9)," +
          "idCurso         VARCHAR(25)," +
          "idContenido     VARCHAR(25)," +
          "PRIMARY KEY (nifProfesor, idCurso, idContenido))"
        );

    executeSQLUpdate
      ("CREATE TABLE IF NOT EXISTS listasDeClase (" +
          "nifAlumno       VARCHAR(9)," +
          "idCurso         VARCHAR(25)," +
          "PRIMARY KEY (nifAlumno, idCurso))"
        );

    executeSQLUpdate
      ("CREATE TABLE IF NOT EXISTS agendas (" +
          "nifAlumno       VARCHAR(9)," +
          "fecha           DATE," +
          "comioBien       BOOLEAN," +
          "nDesposiciones  TINYINT," +
          "durmioSiesta    BOOLEAN," +
          "otrosComentarios VARCHAR(250)," +
          "PRIMARY KEY (nifAlumno, fecha))"
        );

    executeSQLUpdate
      ("CREATE TABLE IF NOT EXISTS tablonesDeAnuncios (" +
          "idCurso         VARCHAR(25)," +
          "fecha           DATE," +
          "textoAnuncio    VARCHAR(250)," +
          "PRIMARY KEY (idCurso, fecha))"
        );

    executeSQLUpdate
      ("CREATE TABLE IF NOT EXISTS fichasDeEvaluacion (" +
          "idCurso         VARCHAR(25)," +
          "idContenido     VARCHAR(25)," +
          "nifAlumno       VARCHAR(9)," +
          "trimestre       TINYINT," +
          "nota            TINYINT," +
          "PRIMARY KEY (idCurso, idContenido, nifAlumno))"
        );

  }

  private static void incluirUsuarioPorDefecto() {

    Statement SQLStatement = null;
    int updateResult = -1;

    try {

      String query= "INSERT INTO usuarios VALUES (" +
      "'71232321H'," +
      "MD5('1234')," +
      "'Antonio'," +
      "'Anuncios'," +
      "'MuchosAnuncios'," +
      "'sinemail@no.com'," +
      "'(+0033)611875632'," +
      "'sin direccion'," +
      "'1'," +
      "'1'," +
      "'1'," +
      "'1'," +
      "'1')";

      SQLStatement = conexion.createStatement();
      updateResult = SQLStatement.executeUpdate(query);      
    }
    catch(Exception ex) {}
  }

}
