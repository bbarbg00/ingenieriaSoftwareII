package colegio.gui;

import colegio.BBDD;

import java.awt.*;
import javax.swing.*;

public class Ventana extends JFrame {

  private BarraDeMenu barraDeMenu;

  private JLabel fondo = new JLabel(new ImageIcon("../img/fondo.jpg"));

  // Estos son los componentes graficos que se muestran en el 
  // panel de contenido de la ventana:

  // Tabla de usuarios (visible para administradores)
  public JScrollPane panelTablaUsuarios;
  private TablaUsuarios tablaUsuarios;

  public Ventana() {

    // cerrar todo al cerrar la ventana
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    // cambiar el look and feel
    this.usarLookAndFeelAlternativo();

    // sin barra de botones
    this.setUndecorated(true);

    // vetana maximizada
    this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
    this.setMinimumSize(this.getSize());

    // Color de fondo
    this.getContentPane().setBackground(Color.BLACK);
    this.setLayout(new BorderLayout());

    // centrar ventana (no hace falta si esta maximizada)
    // this.setPostionRelativeTo(null);

    // barra de menús (de  momento sin los menús)
    barraDeMenu = new BarraDeMenu(this);
    this.setJMenuBar(barraDeMenu);
    barraDeMenu.setVisible(true);

    // dibujar fondo
    this.dibujarFondo();
    
    // Cargar tabla de usuarios
    this.cargarTablaUsuarios();    

    // hacer visible la ventana
    this.pack();
    this.setVisible(true);

    // pedir loging al usuario
    this.pedirLogin();

  }
  
  public void cargarTablaUsuarios () {
  	
    // Tabla con todos los usuarios del Colegio (administradores,
    // profesores y alumnos).
    // Es visible para los usuarios que son administradores.
  	this.tablaUsuarios = new TablaUsuarios(this);
    // lo que se mete en la ventana no es la tabla en si.
    // Se almacena un panel con barras de desplazamiento que la contiene.
    this.panelTablaUsuarios = this.tablaUsuarios.getTablaEnPanel();
    //this.layeredPane.add(this.panelTablaUsuarios, new Integer(2));
    this.panelTablaUsuarios.setVisible(true);
  	
  }

  public void mostrarTablaUsuarios() {

    this.add(this.panelTablaUsuarios);
   
    this.ocultarFondo();

    this.revalidate();
    this.repaint(); 

  }

  private void ocultarTablaUsuarios() {

    this.remove(this.panelTablaUsuarios);

  }

  public void ocultarTodo() {

    this.ocultarTablaUsuarios();

    this.barraDeMenu.ocultarMenusDeUsuario();

    this.dibujarFondo();

    this.revalidate();
    this.repaint();

  }

  private void dibujarFondo() {

    this.getContentPane().add(fondo);

  }

  private void ocultarFondo() {

    this.getContentPane().remove(fondo);

  }

  public void pedirLogin() {

    LoginDialog loginDialog = new LoginDialog(this);

    // una vez ha entrada el usuario,
    // se muestran los menus
    barraDeMenu.mostrarMenusDeUsuario();

  }

  // Muestra la ayuda de la aplicacion 
  // (La ayuda cambia segun la parte de la aplicacion en la que estemos)
  public void mostrarVentanaAyuda() {
    
    //this.mostrarVentanaAviso("TODO");

  }

  // Muestra una ventana de aviso con un mensaje y un boton para cerrarla.
  public void mostrarVentanaAviso (String mensaje) {

    boolean conBotonCancelar = false;

    WarningDialog aviso = 
      new WarningDialog(this, mensaje, conBotonCancelar);
  }

  // Muestra una ventana de aviso con un mensaje, un boton aceptar y un boton cancelar.
  // Devuelve 'true' si el usuario acepta el aviso 
  public boolean mostrarVentanaAvisoyComprobarAceptacion (String mensaje) {

    boolean conBotonCancelar = true;

    WarningDialog aviso = 
      new WarningDialog(this, mensaje, conBotonCancelar);

    // Si se pidio mostrar boton de cancelar y el usuario cancelo la operacion,
    // hay que indicarlo.
    if (aviso.accionAceptada())
      return true;
    else
      return false;    

  }

  // Para cambiar el tema (theme) por defecto 
  private void usarLookAndFeelAlternativo () {

    try {
        // Ponemos el mismo L&F que tenga puesto el usuario
        // en el sistem aque ejecuta la aplicacion
        UIManager.setLookAndFeel(
        UIManager.getSystemLookAndFeelClassName());
    } 
    catch (UnsupportedLookAndFeelException e) {
       // handle exception
    }
    catch (ClassNotFoundException e) {
       // handle exception
    }
    catch (InstantiationException e) {
       // handle exception
    }
    catch (IllegalAccessException e) {
       // handle exception
    }

  }

}
