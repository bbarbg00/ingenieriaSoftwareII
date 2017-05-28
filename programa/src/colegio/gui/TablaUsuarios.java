package colegio.gui;

import colegio.Usuario;
import colegio.BBDD;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

// Tabla interna que se rellena con los datos de BBDD.listaUsuarios
public class TablaUsuarios extends Tabla {

  public TablaUsuarios (Ventana parent) {

    // Llamamos al constructor de la clase Tabla (la que extiende esta)
    super(parent);

    // Cabeceras de la tabla
    // (la ultima en blanco es para un boton de borrado).
    // DEBEN coincidir con el nombre de las columnas en la BBDD MySQL!  
    String cabecerasColumnas[] = 
      {"NIF","Nombre","Apellido1", "Apellido2", "Telefono", "Email", 
             "Direccion", "EsAdministrador", "EsProfesor", "EsAlumno", "esSecretario", "esEncargadoCocina"};

    // El segundo argumento, 0, es para que el numero de filas sea variable
    this.tableModel = new DefaultTableModel(cabecerasColumnas, 0);

    this.setModel(tableModel);    

    // Intentamos rellenar la tabla
    this.rellenar();

  }

  private void cambiarPasswordUsuarioSeleccionado() {

    // Borra la fila seleccionada (si hay alguna)
    int selectedRow = this.getSelectedRow();
    
    if(selectedRow != -1) {

      // Se coge el valor del NIF del usuario de la fila selccionada.
      // El NIF estaba en la posicion 0.
      String nif = this.getValueAt(this.getSelectedRow(), 0).toString();

      // Se solicita el nuevo valor de contraseña
      ChangePasswordDialog changePasswordDialog =
       new ChangePasswordDialog(ventanaEnLaQueEstaLaTabla);

      // Si no se acepta el cambio, salimos
      if (!changePasswordDialog.getCambioAceptado())
        return;

      String nuevoPass = changePasswordDialog.getPassword();
      
      // Ultima confirmacion antes de hacer el cambio
      boolean avisoAceptado =
        ventanaEnLaQueEstaLaTabla.mostrarVentanaAvisoyComprobarAceptacion
          ("Vas a cambiar la contraseña del usuario con NIF " + nif + ".'\n" +
           "Esta operacion no se puede deshacer.\n" +
           "¿Desea continuar?");

      if (!avisoAceptado)
        return;

      // Se cambia la pass
      BBDD.cambiarPasswordUsuarioConNif(nuevoPass, nif);

    }
    else {
      ventanaEnLaQueEstaLaTabla.mostrarVentanaAviso("No hay ningun usuario seleccionado");
    }

  }

  @Override
  protected void incluirMenusAdicionalesEnMenuPopup() {

    JMenuItem changePassItem = new JMenuItem("Cambiar contraseña usuario");
    changePassItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        cambiarPasswordUsuarioSeleccionado();
      }
    });

    this.popupMenu.add(changePassItem);

  }

  @Override
  protected void setTextosMenuPopup() {

    this.textoAniadir     = "Añadir usuario";
    this.textoBorrar      = "Borrar usuario seleccionado";
//ventanaEnLaQueEstaLaBarra.mostrarVentanaCambioPassword();
  }

  @Override
  protected void gestionarCambiosRealizados(TableCellListener tcl){

    // La clave es el campo NIF, porque:
    // - No puede estar vacio
    // - Debe ser unico en la BBDD
   
    // Por ello, ante cualquier cambio de este campo, 
    // si el nuevo valor del campo NIF esta vacio o duplicado, 
    // se muestra un aviso de error y se cancela
    // el cambio

    // Obtenemos el NIF del usuario (siempre el antiguo,
    // porque es el que tenemos en la BBDD, si no no encontrariamos
    // al usuario al buscarlo para hacer los cambios.
    String nifEnBBDD = this.getValueAt(tcl.getRow(), 0).toString();

    String oldNIF = nifEnBBDD;
    String newNIF = nifEnBBDD;

    // Si el cambio afecta a la columna donde se almacena el NIF (la 0)...
    if (tcl.getColumn() == 0) {

      oldNIF = tcl.getOldValue().toString();
      newNIF = tcl.getNewValue().toString();

      // En este caso, como ha cambiado el nif, acutualizamos el valor
      // de la variable nifEnBBDD para asegurarnos que recoge el antiguo.
      nifEnBBDD = oldNIF;

      // Si el NIF nuevo esta en blanco o ya existe en la BBDD
      if (!BBDD.esNIFValido(newNIF)) {

        ventanaEnLaQueEstaLaTabla.mostrarVentanaAviso
          ("El campo NIF no puede estar en blanco ni puede existir ya en la BBDD");

        // Se restaura el valor antiguo
        this.setValueAt(tcl.getOldValue(), tcl.getRow(), tcl.getColumn());
        
        return;

      } 

      // Si llegamos hasta aqui, ha cambiado el campo NIF, y el cambio es correcto.
      // Por tanto, actualizamos el valor en la BBDD
      // (esto se hace fuera de este if porque afecta a todos los casos).  

    }

    // Si estamos aqui el cambio que ha habido es correcto,
    // por lo que podemos actualizar la BBDD.

    // Hay que tener en cuenta el caso particular de una insercion de nuevo usuario:
    // - Si el nifEnBBDD esta en blanco, el campo modificado es la columna 0 (es decir, el NIF),
    // y el campo nuevo es un nif valido, se hace una insercion.
    // - En otro caso, es modificacion.
    if ((nifEnBBDD.isEmpty()) &&
        (tcl.getColumn() == 0) &&
        (BBDD.esNIFValido(newNIF))) {

      Usuario nuevoUsuario = new Usuario
        (this.getValueAt(this.getSelectedRow(), 0).toString(),
         this.getValueAt(this.getSelectedRow(), 1).toString(),
         this.getValueAt(this.getSelectedRow(), 2).toString(),
         this.getValueAt(this.getSelectedRow(), 3).toString(),
         this.getValueAt(this.getSelectedRow(), 4).toString(),
         this.getValueAt(this.getSelectedRow(), 5).toString(),
         this.getValueAt(this.getSelectedRow(), 6).toString(),
         this.getValueAt(this.getSelectedRow(), 7).toString(),
         this.getValueAt(this.getSelectedRow(), 8).toString(),
         this.getValueAt(this.getSelectedRow(), 9).toString(),
         this.getValueAt(this.getSelectedRow(), 10).toString(),
         this.getValueAt(this.getSelectedRow(), 11).toString());
      
      BBDD.insertarUsuario(nuevoUsuario);

    }
    else {

      BBDD.actualizarCampoDeUsuario
        (nifEnBBDD,
         this.tableModel.getColumnName(tcl.getColumn()), 
         tcl.getNewValue().toString());
    }

  }

  @Override
  protected void rellenar () {

    ArrayList<Usuario> listaUsuarios = BBDD.getListaUsuarios();

    if ((listaUsuarios== null) ||
        (listaUsuarios.isEmpty()))
      return;

    // Si estamos aqui es porque hay usuarios:
    // los cargamos.
    int i = 0;
    Usuario usuario = null;
    Object[] fila = {"", "", "", "", "", "", "", "", "", "", "", ""};

    while (i < listaUsuarios.size()) {
    
      usuario = listaUsuarios.get(i);

      fila[0] = usuario.getId();
      fila[1] = usuario.getNombre();
      fila[2] = usuario.getApellido1();
      fila[3] = usuario.getApellido2();
      fila[4] = usuario.getTelefono();
      fila[5] = usuario.getEmail();
      fila[6] = usuario.getDireccion();
      fila[7] = Boolean.toString(usuario.getEsAdministrador());
      fila[8] = Boolean.toString(usuario.getEsProfesor());
      fila[9] = Boolean.toString(usuario.getEsAlumno());
      fila[10] = Boolean.toString(usuario.getEsSecretario());
      fila[11] = Boolean.toString(usuario.getEsEncargadoCocina());

      this.tableModel.addRow(fila);

      i++;

    }

  }

  @Override
  protected void addFila() {

    Object[] fila = {"", "", "", "", "", "", "", "", ""};
    tableModel.addRow(fila);

  }

  @Override
  protected void deleteFilaSeleccionada() {

    // Borra la fila seleccionada (si hay alguna)
    int selectedRow = this.getSelectedRow();
    
    if(selectedRow != -1) {

      // Se coge el valor del NIF del usuario de la fila selccionada.
      // El NIF estaba en la posicion 0.
      String nif = this.getValueAt(this.getSelectedRow(), 0).toString();

      // Si el usuario a eliminar es el que esta logeado, no lo permitimos
      if (nif.equals(BBDD.getUsuarioConectado().getId())) {

        ventanaEnLaQueEstaLaTabla.mostrarVentanaAviso
          ("No se puede eliminar el usuario actual");

        return;
      }
      
      // En principio, si llegamos aqui, el usuario se podria eliminar.
      // Pedimos confirmacion.
      String fraseNif ="";

      if (nif.equals(""))
        fraseNif = "Se va a borrar el usuario seleccionado";
      else
        fraseNif = "Se va a borrar el usuario con NIF " + nif;

      boolean avisoAceptado =
        ventanaEnLaQueEstaLaTabla.mostrarVentanaAvisoyComprobarAceptacion
          (fraseNif + ".\n" +
           "Esta operacion no se puede deshacer.\n" +
           "¿Desea continuar?");

      if (!avisoAceptado)
        return;

      // Se elimina el usuario de la BBDD y de la tabla
      boolean usuarioBorrado = BBDD.borrarUsuario(nif);

      // Si se pudo eliminar el usuario,
      // o si la fila estaba vacia (esto ulimo no se ha probado!), 
      // se elimina la fila
      if (usuarioBorrado || nif.equals(""))
        tableModel.removeRow(selectedRow);
    }
    else {
      ventanaEnLaQueEstaLaTabla.mostrarVentanaAviso("No hay ningun usuario seleccionado");
    }

  }

}
