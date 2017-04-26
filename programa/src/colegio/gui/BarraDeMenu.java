package colegio.gui;

import colegio.Usuario;
import colegio.BBDD;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;

import javax.help.*;
//import javax.print.DocFlavor.URL;
import java.net.URL;

public class BarraDeMenu extends JMenuBar {

  private JMenu menuAdministrador;
  private JMenu menuProfesor;
  private JMenu menuAlumno;

  private JMenuItem menuItemDatosUsuario;

  private JMenu menuAyuda;
  
  // Los dos siguientes atributos son para poder usar
  // JavaHelp
  //private HelpBroker helpBroker;
  private HelpSet helpSet;
  // Ventana de ayuda JavaHelp en la que va la ayuda
  private javax.help.MainWindow ventanaAyuda;
  // Nombres que tienen las diferentes paginas de ayuda html
  // guardas en programa/javahelp/help y en los ficheros de
  // programa/javahelp que sirven de "mapa" a las mismas.
  private static final String JAVAHELP_INDICE = "indice";
  
  private static final String JAVAHELP_MENU_ADMINISTRADOR = "administrador";
  private static final String JAVAHELP_MENU_ADMINISTRADOR_USUARIOS = "administrador.usuarios";
  private static final String JAVAHELP_MENU_ADMINISTRADOR_CURSOS = "administrador.cursos";
  private static final String JAVAHELP_MENU_ADMINISTRADOR_HACER_BACKUP = "administrador.hacer_backup";
  private static final String JAVAHELP_MENU_ADMINISTRADOR_RESTAURAR_BACKUP = "administrador.restaurar_backup";
  
  private static final String JAVAHELP_MENU_PROFESOR = "profesor";
  private static final String JAVAHELP_MENU_PROFESOR_CURSOS = "profesor.cursos";
    
  private static final String JAVAHELP_MENU_ALUMNO = "alumno";
  private static final String JAVAHELP_MENU_ALUMNO_AGENDA = "alumno.agenda";
  private static final String JAVAHELP_MENU_ALUMNO_NOTAS = "alumno.notas";
  private static final String JAVAHELP_MENU_ALUMNO_TABLON_ANUNCIOS = "alumno.tablon_anuncios";
  
  // Pagina de las anteriores que habria que mostrar en un momento dado.
  // Se cambia cada vez que se pulsa el boton correspondiene de esta
  // clase BarraDeMenu.
  private String nombrePaginaAyudaActual = "indice";
  
  private JMenu menuLogout;

  private BackupFileChooser fileChooser;

  private Ventana ventanaEnLaQueEstaLaBarra;

  public BarraDeMenu (Ventana parent) {

    this.ventanaEnLaQueEstaLaBarra = parent;

    // JFileChooser para elegir directorio donde guardar
    // y del que cagar los Backup
    this.fileChooser = 
      new BackupFileChooser(this.ventanaEnLaQueEstaLaBarra);

    // Menus de usuario
    this.incluirMenusDeUsuario();

    // Colocado ahora a la derecha,
    // Nombre del usuario actual y
    // boton de logout
    this.add(Box.createGlue());

    this.incluirEtiquetaDatosUsuario();

    this.incluirMenuAyuda();

    this.incluirMenuLogOut();

  }

  private void incluirEtiquetaDatosUsuario() {

    String datosUsuario = "";

    if (BBDD.hayUsuarioConectado())
      datosUsuario = BBDD.getUsuarioConectado().getNombreyApellidos();

    menuItemDatosUsuario = new JMenuItem (datosUsuario);

    // No "destacamos" este boton al pasar el cursor por encima
    // (ya que es solo informativo)
    menuItemDatosUsuario.setEnabled(false);
    UIManager.put("MenuItem.disabledAreNavigable", Boolean.FALSE);

    this.add(menuItemDatosUsuario);

  }
  
  private void cargaAyuda() {
  	   
    try {
    	
      File fichero = new File("../javahelp/HELP.hs"); 
      URL helpSetURL = fichero.toURI().toURL(); 
      this.helpSet = new HelpSet(null, helpSetURL); 
      
    	this.ventanaAyuda  = 
        (MainWindow)MainWindow.getPresentation(helpSet,"");
    	
    	// Inicalmente no se muestra la ventana
    	// (se hace visible cuando se pulsa el boton de ver ayuda)
    	this.ventanaAyuda.setDisplayed(false);
      
    } 
   
    catch (Exception e) { 
      System.out.println("Ayuda no encontrada"); 
      return; 
    } 
   
    //******this.helpBroker = this.helpSet.createHelpBroker();    
    //******this.helpBroker.initPresentation();
	
  } 

  private void incluirMenuAyuda() {

  	this.menuAyuda = new JMenu("Ayuda");
    this.menuAyuda.setIcon(new ImageIcon("../img/bubbles_small.png"));
        
    this.cargaAyuda();            
        
    // al hacer click, mostrar menu de ayuda
    this.menuAyuda.addMouseListener(new MouseListener() {
    
      @Override
      public void mouseReleased(MouseEvent e) {
      }
             
      @Override
      public void mousePressed(MouseEvent e) {
      }
             
      @Override
      public void mouseExited(MouseEvent e) {
      }
             
      @Override
      public void mouseEntered(MouseEvent e) {
      }
             
      @Override
      public void mouseClicked(MouseEvent e) {
      
        // Se deselecciona el menu
        menuAyuda.setSelected(false);
        // Se muestra la ventana de ayuda
        //ventanaEnLaQueEstaLaBarra.mostrarVentanaAyuda();  
        
        //*****helpBroker.showID("welcome", "javax.help.MainWindow",  "main");
              	
      	ventanaAyuda.setCurrentID(nombrePaginaAyudaActual); 	
      	ventanaAyuda.setDisplayed(true);
      	
      	
      	//ventanaAyuda.setDestroyOnExit(true); 	   
      

      }
    });

        //Se muestra el menu de ayuda
	

    // inicalmente, este menu no es visible
    this.menuAyuda.setVisible(false);

    this.add(menuAyuda);


  }

  private void incluirMenuLogOut() {

    menuLogout = new JMenu("Logout");
    menuLogout.setIcon(new ImageIcon("../img/close_small.png"));

    // al hacer click, salir
    menuLogout.addMouseListener(new MouseListener() {
             
      @Override
      public void mouseReleased(MouseEvent e) {
      }
             
      @Override
      public void mousePressed(MouseEvent e) {
      }
             
      @Override
      public void mouseExited(MouseEvent e) {
      }
             
      @Override
      public void mouseEntered(MouseEvent e) {
      }
             
      @Override
      public void mouseClicked(MouseEvent e) {
      
        // Se ocultan todos los menus de usuario
        ocultarMenusDeUsuario();
        // Se oculta todo lo mostrado en la ventana
        ventanaEnLaQueEstaLaBarra.ocultarTodo();
        // Se deselecciona el menu
        menuLogout.setSelected(false);
        // Se resetea el usuario actual en la clase BBDD
        BBDD.logoutUsuarioConectado();
        // Volvemos a la pagina por defecto de la ayuda
        nombrePaginaAyudaActual = JAVAHELP_INDICE;
        // Se vuelve a mostrar el popup de login
        ventanaEnLaQueEstaLaBarra.pedirLogin();

      }
    });

    this.add(menuLogout);

  }

  public void incluirMenusDeUsuario() {

    // Menus para los diferentes tipos de usuario, a la izquierda
    // (inicialmente no seran visibles)

    // Menu ADMINISTRADOR
    menuAdministrador = new JMenu("Administrador");
    menuAdministrador.setIcon(new ImageIcon("../img/admin_small.png"));

    // Items del menu administrador
    JMenuItem menuItem = new JMenuItem ("Usuarios");

    menuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        
        nombrePaginaAyudaActual = JAVAHELP_MENU_ADMINISTRADOR_USUARIOS;
        ventanaEnLaQueEstaLaBarra.mostrarTablaUsuarios();
        
      }
    });

    menuAdministrador.add(menuItem);

    menuItem = new JMenuItem ("Cursos");
    menuAdministrador.add(menuItem);

    // Backup
    // BBDD -> Backup
    menuItem = new JMenuItem ("Hacer backup");

    menuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        
      	nombrePaginaAyudaActual = JAVAHELP_MENU_ADMINISTRADOR_HACER_BACKUP;
        hacerBackup();
      }
    });

    menuAdministrador.add(menuItem);

    // Backup -> BBDD
    menuItem = new JMenuItem ("Restaurar backup");

    menuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        
      	nombrePaginaAyudaActual = JAVAHELP_MENU_ADMINISTRADOR_RESTAURAR_BACKUP;
        restaurarBackup();
      }
    });

    menuAdministrador.add(menuItem);

    menuAdministrador.setVisible(false);
    this.add(menuAdministrador);

    // Menu PROFESOR
    menuProfesor = new JMenu("Profesor");
    menuProfesor.setIcon(new ImageIcon("../img/profesor_small.png"));

    // Items del menu profesor
    menuItem = new JMenuItem ("Cursos");
    menuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        
      	nombrePaginaAyudaActual = JAVAHELP_MENU_PROFESOR_CURSOS;
        // NYI
      }
    });
    menuProfesor.add(menuItem);

    menuProfesor.setVisible(false);
    this.add(menuProfesor);

    // Menu ALUMNO
    menuAlumno = new JMenu("Alumno");
    menuAlumno.setIcon(new ImageIcon("../img/alumno_small.png"));

    // Items del menu alumno
    menuItem = new JMenuItem ("Agenda");
    menuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        
      	nombrePaginaAyudaActual = JAVAHELP_MENU_ALUMNO_AGENDA;
        // NYI
      }
    });
    menuAlumno.add(menuItem);

    menuItem = new JMenuItem ("Notas");
    menuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        
      	nombrePaginaAyudaActual = JAVAHELP_MENU_ALUMNO_NOTAS;
        // NYI
      }
    });
    menuAlumno.add(menuItem);
    menuItem = new JMenuItem ("Tablon de anuncios");
    menuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        
      	nombrePaginaAyudaActual = JAVAHELP_MENU_ALUMNO_TABLON_ANUNCIOS;
        // NYI
      }
    });
    menuAlumno.add(menuItem);

    menuAlumno.setVisible(false);
    this.add(menuAlumno);

  }

  public void mostrarMenusDeUsuario() {

    Usuario usuarioActual = BBDD.getUsuarioConectado();

    if (usuarioActual != null) {

      menuAdministrador.setVisible(usuarioActual.getEsAdministrador());
      menuProfesor.setVisible(usuarioActual.getEsProfesor());
      menuAlumno.setVisible(usuarioActual.getEsAlumno());

    }

    // Se refresca ademas la etiqueta con los datos del usuario
    if (BBDD.hayUsuarioConectado())
      menuItemDatosUsuario.setText(BBDD.getUsuarioConectado().getNombreyApellidos());
    else
      menuItemDatosUsuario.setText("");

    // Se muestra el menu de ayuda
    menuAyuda.setVisible(true);

  }

  public void ocultarMenusDeUsuario() {

    menuAdministrador.setVisible(false);
    menuProfesor.setVisible(false);
    menuAlumno.setVisible(false);

    menuItemDatosUsuario.setText("");

    menuAyuda.setVisible(false);

  }

  private void hacerBackup() {

    fileChooser.hacerBackup();

  }

  private void restaurarBackup() {

    fileChooser.restaurarDesdeBackup();

  }

}
