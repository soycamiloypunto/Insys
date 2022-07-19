/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package insys;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author cktv
 */
public class InicioSesion extends javax.swing.JFrame {

    private String HOME;

    /**
     * Creates new form InicioSesion
     */
    public InicioSesion() {
        initComponents();
        setIcon();
        //validarinicio();
    }
    
    private void setIcon() {
    setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
  }
  
  public void ValidarVersion() {
    try {
      (new Thread(new NuevaVersion())).start();
    } catch (Exception exception) {}
  }
  
//  void ValidarCHECK() {
//    if (this.InternetCHECK.isSelected()) {
////      Access_connection.TipoConexion = "Local";
//      System.out.println("Modo: Local");
//      String so = System.getProperty("os.name");
//      System.out.println("Sistema Operativo: " + so);
//      if (so.contains("Linux")) {
//        Access_connection.servidor = "jdbc:sqlite:" + this.HOME + "/Insys/funeral1_insys.db";
//      } else if (so.contains("Windows")) {
//        Access_connection.servidor = "jdbc:sqlite:C:\\SolucionesCYD\\Insys\\funeral1_insys.db";
//      } else if (so.contains("Mac")) {
//        Access_connection.servidor = "jdbc:sqlite:/Applications/Insys/funeral1_insys.db";
//      } 
//    } else {
//      System.out.println("Modo: Internet");
////      Access_connection.TipoConexion = "Internet";
//      Access_connection.servidor = "jdbc:mysql://funeralescasasagrada.com:3306/funeral1_insys";
//    } 
//  }
  
  private class NuevaVersion implements Runnable {
    private NuevaVersion() {}
    
    public void run() {
      InicioSesion.this.TraerNumeroVersionAcercade();
      InicioSesion.this.VerificarVersion();
    }
  }
  
  private void TraerNumeroVersionAcercade() {
    Acercade A = new Acercade();
    this.VersionLABEL.setText(A.Version.getText());
  }
  
  public void VerificarVersion() {
    Access_connection AC = new Access_connection();
    try {
      Statement stmt = AC.conn().createStatement();
      ResultSet rs = stmt.executeQuery("SELECT Version FROM App_Version WHERE IdVersion=1");
      while (rs.next()) {
        System.out.println("VersionLocal: " + this.VersionLABEL.getText() + " VersionActual: " + rs.getString("Version"));
        if (this.VersionLABEL.getText().equals(rs.getString("Version"))) {
          continue;
        } 
        int reply = JOptionPane.showConfirmDialog(null, "actualizar la aplicaci", 
                "Una nueva verside la APP estdisponible", 0);
        switch (reply) {
          case 0:
            try {
              String so = System.getProperty("os.name");
              if (so.contains("Linux")) {
                Desktop dk = Desktop.getDesktop();
                dk.browse(new URI("#"));
              } else if (so.contains("Windows")) {
                Desktop dk = Desktop.getDesktop();
                dk.browse(new URI("#"));
              } else if (so.contains("Mac")) {
                Desktop dk = Desktop.getDesktop();
                dk.browse(new URI("#"));
              } 
              System.exit(0);
            } catch (Exception e) {
              System.out.print("Error al abrir URL: " + e.getMessage());
            } 
            System.out.println("La actualizacise estdescargando");
          case -1:
            System.out.println("A cancelado la actualización");
            continue;
        } 
        System.out.println("A cancelado la actualización");
      } 
      System.out.println("Cargado ICO_Datos_Organizacion");
      AC.desconectar();
    } catch (Exception e) {
      System.out.println("No Cargado ICO_Datos_Organizacion" + e);
    } 
  }
  
  public void validarinicio() {
      
    Ventana.LABELVersion.setText("En Línea");
    String Usuario = this.TXTUsuario.getText();
    String Pass = Arrays.toString(this.TXTPass.getPassword());
    if (Usuario.equals("") || Pass.equals("")) {
      JOptionPane.showMessageDialog(null, "El Campo Usuario y Contraseno pueden estar vacíos");
    } else {
      (new Thread(new IniciodeSesion())).start();
    } 
    
  }
  
  private class IniciodeSesion implements Runnable {
    private IniciodeSesion() {}
    
    public void run() {
      jButton1.setEnabled(false);
      InicioSesion.this.IniciodeSesion();
      jButton1.setEnabled(true);
    }
  }
  
  public void IniciodeSesion() {
    Ventana V = new Ventana();
    V.CargarTablas();
    MysqlConnect AC = new MysqlConnect();
    String Usuario = this.TXTUsuario.getText();
    char[] Pass = this.TXTPass.getPassword();
    String contrasena = new String(Pass);
    Calendar c = Calendar.getInstance();
    String Segundo = Integer.toString(c.get(13));
    String Minuto = Integer.toString(c.get(12));
    String Hora = Integer.toString(c.get(11));
    String dia = Integer.toString(c.get(5));
    String mes = Integer.toString(c.get(2) + 1);
    String annio = Integer.toString(c.get(1));
    String Fecha = annio + "/" + mes + "/" + dia + " " + Hora + ":" + Minuto + ":" + Segundo;
    String SoloFecha = dia + "/" + mes + "/" + annio;
    System.out.println("Fecha: " + Fecha);
    try {
      Statement stmt = AC.conn().createStatement();
      ResultSet rs = stmt.executeQuery("SELECT id, nick, nombres, tipousuario, pass FROM UsuariosSistema "
              + "WHERE nick='" + Usuario + "'");
      if (rs.next()) {
        long idUsuarios = rs.getLong("id");
        System.out.println("nick: " + idUsuarios);
        String Contrasena = rs.getString("pass");
        String TipoUsuario = rs.getString("tipousuario");
        V.NumUsuario = rs.getLong("id");
        V.TipoUsuario = TipoUsuario;
        V.Modo = "Internet";
        V.ValidarUsuario();
        
        if (contrasena.equals(Contrasena)) {
          V.setLocationRelativeTo(null);
          V.setVisible(true);
          dispose();
        } else if (contrasena != Contrasena) {
          JOptionPane.showMessageDialog(null, "Contraseña Incorrecta", "Contraseña Incorrecta", 2);
        } 
      } else {
        JOptionPane.showMessageDialog(null, "El Usuario no Existe", "Error de Usuario", 2);
      } 
      AC.desconectar();
    } catch (SQLException ex) {
      System.out.println("Error");
      Logger.getLogger(InicioSesion.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
  }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        InternetCHECK = new javax.swing.JCheckBox();
        jButton2 = new javax.swing.JButton();
        VersionLABEL = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        TXTUsuario = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        TXTPass = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        InternetCHECK.setBackground(new java.awt.Color(255, 255, 255));
        InternetCHECK.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        InternetCHECK.setText("Modo Sin Internet");
        InternetCHECK.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                InternetCHECKPropertyChange(evt);
            }
        });

        jButton2.setText("Salir");
        jButton2.setContentAreaFilled(false);
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        VersionLABEL.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        VersionLABEL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        VersionLABEL.setText("¿Olvidó su contraseña?");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(850, 500));
        setResizable(false);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Usuario");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Contraseña");

        TXTPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TXTPassActionPerformed(evt);
            }
        });

        jButton1.setText("Iniciar Ahora");
        jButton1.setContentAreaFilled(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/walls/ic_launcher.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(TXTUsuario)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(TXTPass))
                .addContainerGap())
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TXTUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TXTPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(143, 143, 143))
        );

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/walls/wallwhite.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 692, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        validarinicio();
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void TXTPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TXTPassActionPerformed
        validarinicio();        //validarinicio();
    }//GEN-LAST:event_TXTPassActionPerformed

    private void InternetCHECKPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_InternetCHECKPropertyChange
//        validarinicio();
    }//GEN-LAST:event_InternetCHECKPropertyChange

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
System.exit(0);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(InicioSesion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InicioSesion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InicioSesion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InicioSesion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InicioSesion().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox InternetCHECK;
    private javax.swing.JPasswordField TXTPass;
    private javax.swing.JTextField TXTUsuario;
    private javax.swing.JLabel VersionLABEL;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
