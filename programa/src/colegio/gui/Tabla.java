package colegio.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

// Tabla abstracta que luego extienden la tabla de usuarios y la de cursos
public abstract class Tabla extends JTable {

  // Este es el panel que se debe mostrar.
  // Es accesible con el metodo getTablaEnPanel (que es publico)
  private JScrollPane scrollPane;

  // private JTable tabla;
  protected DefaultTableModel tableModel;

  // Incluimos un menu popup (menu que sale al dar al hacer click boton derecho)
  // en la tabla 
  final protected JPopupMenu popupMenu;

  protected Ventana ventanaEnLaQueEstaLaTabla;

  // Textos para mostrar en el menu de tipo popup
  // que se muestra al hacer click al boton derecho.
  // (Las clases que extienden esta los pueden cambiar
  // haciendo @Override del metodo setTextosMenuPopup)
  protected String textoAniadir;
  protected String textoBorrar;

  public Tabla (Ventana parent) {

    this.ventanaEnLaQueEstaLaTabla = parent;

    // Detectamos los cambios en las celdas de la tabla
    // (para modificar los datos en la BBDD)
    this.activarDeteccionModificacciones();

    // Incluimos un menu popup en la tabla para poder:
    // - aniadir elementos
    // - borrar elementos
    popupMenu = new JPopupMenu();

    this.setTextosMenuPopup();

    JMenuItem addItem = new JMenuItem(this.textoAniadir);
    addItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        addFila();
      }
    });

    JMenuItem deleteItem = new JMenuItem(this.textoBorrar);
    deleteItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        deleteFilaSeleccionada();
      }
    });

    popupMenu.add(addItem);
    popupMenu.add(deleteItem);

    // Se incluyen otros posibles menus
    // (esto metodo se define en cada clase que extienda a esta)
    this.incluirMenusAdicionalesEnMenuPopup();
    
    // Se incluye el menu popup
    this.setComponentPopupMenu(popupMenu);

    // Este es el panel (que contien la tabla)
    // con barras de desplazamiento que
    // se debe mostrar en la Venana principal
    this.scrollPane = new JScrollPane(this);
    this.setFillsViewportHeight(true);

    this.scrollPane.setHorizontalScrollBarPolicy(
      JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    this.scrollPane.setVerticalScrollBarPolicy(
      JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

  }

  protected void setTextosMenuPopup() {

    this.textoAniadir = "Añadir";
    this.textoBorrar  = "Borrar";

  }

  // Esto es para poder ver esta tabla en la ventana
  // (la tabla se mete en el atributo this.scrollPane)
  public JScrollPane getTablaEnPanel() {

    return this.scrollPane;

  }

  // Código sacado enteramente de:
  //https://tips4java.wordpress.com/2009/06/07/table-cell-listener/
  protected void activarDeteccionModificacciones (){

    Action action = new AbstractAction()
    {
      public void actionPerformed(ActionEvent e)
      {
        TableCellListener tcl = (TableCellListener)e.getSource();

        gestionarCambiosRealizados(tcl);

      }
    };

    TableCellListener tcl = new TableCellListener(this, action);

  }

  // Las clases que extiendan esta deben hacer @Override en este metodo
  // para gestionar adecuadamente la modificacion de los datos de la tabla.
  // Se deja este codigo aqui
  // (sacado de https://tips4java.wordpress.com/2009/06/07/table-cell-listener/)
  // a modo de ejemplo:
   /* System.out.println("Row   : " + tcl.getRow());
    System.out.println("Column: " + tcl.getColumn());
    System.out.println("Old   : " + tcl.getOldValue());
    System.out.println("New   : " + tcl.getNewValue());*/
  abstract void gestionarCambiosRealizados(TableCellListener tcl);

  // Estos son los metodos, junto con el anterior y con el constructor,
  // que habria que rellenar en las clases que extiendan esta
  abstract void incluirMenusAdicionalesEnMenuPopup();
  abstract void rellenar ();
  abstract void addFila();
  abstract void deleteFilaSeleccionada();

}
