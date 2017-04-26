package colegio.gui;

import colegio.BBDD;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;

public class BackupFileChooser extends JFileChooser {

  private Ventana ventanaEnLaQueEstaElChooser;

  public BackupFileChooser (Ventana parent) {

    this.ventanaEnLaQueEstaElChooser = parent;
    
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "Ficheros de backup de BD de Colegio", "sql");
    this.setFileFilter(filter);
  
  }

  public void hacerBackup() {

    this.setDialogTitle("Indique drectorio y nombre del fichero destino");
   
    // El usuario elige directorio donde hacer el backup y
    // el nombre del fichero
    this.setFileSelectionMode(JFileChooser.FILES_ONLY);

    this.setDialogType(JFileChooser.SAVE_DIALOG);

    int returnVale = this.showOpenDialog(ventanaEnLaQueEstaElChooser);

    if(returnVale == JFileChooser.APPROVE_OPTION) {

       String rutaAbsolutaFicheroSinExtension = this.getSelectedFile().getAbsolutePath();

       boolean exito = BBDD.hacerBackup (rutaAbsolutaFicheroSinExtension);
       
       if (!exito) {
      	 
      	 ventanaEnLaQueEstaElChooser.mostrarVentanaAviso("Error al crear Backup");
      	 
       }

       /*System.out.println("You chose to open this file: " +
            this.getSelectedFile().getName());*/
    }

  }

  public void restaurarDesdeBackup() {

    this.setDialogTitle("Seleccione fichero de backup de BD MySQL");

    // El usuario elige directorio donde esta el fichero del backup,
    // y el nombre del fichero
    this.setFileSelectionMode(JFileChooser.FILES_ONLY);

    this.setDialogType(JFileChooser.OPEN_DIALOG);

    int returnVale = this.showOpenDialog(ventanaEnLaQueEstaElChooser);

    if(returnVale == JFileChooser.APPROVE_OPTION) {

       String rutaAbsolutaFichero = this.getSelectedFile().getAbsolutePath();

       // Para poder recargar los datos, se perderan las tablas de la BBDD actual en uso,
       // por lo que hay que avisar al usuario.
       boolean avisoAceptado =
         ventanaEnLaQueEstaElChooser.mostrarVentanaAvisoyComprobarAceptacion
           ("Para poder restaurar el backup, se borrarán todas las tablas de la BD MySQL en uso.\n"+
            "Esta operacion no se puede deshacer.\n" +
            "¿Desea continuar?");

       if (!avisoAceptado)
         return;

       boolean exito = BBDD.restaurarDesdeBackup (rutaAbsolutaFichero);
       
       if (!exito) {
      	 
      	 ventanaEnLaQueEstaElChooser.mostrarVentanaAviso("Error al restaurar BD");
      	 
       }
       
       ventanaEnLaQueEstaElChooser.mostrarVentanaAviso
         ("Debe reiniciar manualmente la aplicación para terminar el proceso de restauración");
       
       System.exit(0);
       
       /*
       // Se oculta todo lo mostrado en la ventana
       ventanaEnLaQueEstaElChooser.ocultarTodo();       
       // Se recargan las tablas gráficas (las JTable) de la aplicación
       ventanaEnLaQueEstaElChooser.cargarTablaUsuarios();
       // Se vuelve a mostrar el popup de login
       ventanaEnLaQueEstaElChooser.pedirLogin();*/

       
    }

  }

}
