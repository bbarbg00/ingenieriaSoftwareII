package colegio.gui;

import colegio.BBDD;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
 
// Codigo sacado y adaptado de:
// http://www.zentut.com/java-swing/simple-login-dialog/
public class LoginDialog extends JDialog {
 
    private JTextField campoTextoNIF;
    private JPasswordField campoPassword;
    private JLabel etiquetaNIF;
    private JLabel etiquetaPassword;
    private JButton botonLogin;
    private JButton botonCancelar;
    private boolean loginCorrecto;
 
    public LoginDialog(final Frame parent) {

        super(parent, "Login (NIF de pruebas: 71232321H, pass: 1234)", true);

        // Si se cierra esta ventana, se cierra la aplicacion
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent arg0) {
             dispose();
             System.exit(0);
          }

        });

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
 
        cs.fill = GridBagConstraints.HORIZONTAL;
 
        etiquetaNIF = new JLabel("NIF: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(etiquetaNIF, cs);
 
        campoTextoNIF = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(campoTextoNIF, cs);
 
        etiquetaPassword = new JLabel("Contraseña: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(etiquetaPassword, cs);
 
        campoPassword = new JPasswordField(20);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(campoPassword, cs);
        panel.setBorder(new LineBorder(Color.GRAY));
 
        botonLogin = new JButton("Login");
 
        botonLogin.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e) {

                if (BBDD.validarLogin(getNIF(), getPassword())) {

                    /*JOptionPane.showMessageDialog(LoginDialog.this,
                            "Hola " + BBDD.getUsuarioConectado().getNombre() + "! bienvenido al colegio!",
                            "Login",
                            JOptionPane.INFORMATION_MESSAGE);*/

                    loginCorrecto = true;
                    dispose();

                } else {

                    JOptionPane.showMessageDialog(LoginDialog.this,
                            "Nombre de usuario o contraseña erróneo",
                            "Login",
                            JOptionPane.ERROR_MESSAGE);
                    // reset username and password
                    campoTextoNIF.setText("");
                    campoPassword.setText("");
                    loginCorrecto = false;
 
                }
            }
        });

        // boton cancelar (cierra la ventan de login y la aplicacion
        botonCancelar = new JButton("Salir");
        botonCancelar.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e) {
                dispose();
                System.exit(0);
            }
        });
        JPanel bp = new JPanel();
        bp.add(botonLogin);
        bp.add(botonCancelar);
 
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);
 
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);

        this.setVisible(true);

    }
 
    private String getNIF() {
        return campoTextoNIF.getText().trim();
    }
 
    private String getPassword() {
        return new String(campoPassword.getPassword());
    }
 
    private boolean loginCorrecto() {
        return loginCorrecto;
    }
}
