/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package insys;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import org.jfree.util.Log;
import org.jdesktop.swingx.prompt.PromptSupport;

/**
 *
 * @author cktv
 */
public class Ventana extends javax.swing.JFrame {

    public static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Ventana.class);
    SimpleDateFormat Formato = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MMM/yyyy");
    int x;
    int y;
    boolean b = true;
    long NumUsuario = 0L;
    public static String TipoUsuario;
    String HOME = System.getProperty("user.home");
    String Ruta;//Direccion donde se va a guardar el excel
    String Modo;
    private Vector rowData;
    public SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MMM/yyyy");
    public SimpleDateFormat FormatoFecha = new SimpleDateFormat("dd/MM/yyyy");
    public SimpleDateFormat FormatoFecha2 = new SimpleDateFormat("yyyy-MM-dd");
    Color encabezadostabla = new Color(59, 79, 133);
    long NumAfiliado=0;
    String NomAfiliado = "";
    int NumPago = 0;
    Object BusquedaDatos;
    int IdVendedorSeleccionado;
    int OrdenServicio;
    
    
    /**
     * Creates new form Ventana
     */
    public Ventana() {
        initComponents();
        setIcon();
        PromptSupport.setPrompt("Buscar...", jTextField1);
        new Thread(new VerificarVersion()).start();  
        
    }

    //Administración de la app
    
  private void setIcon() {
    setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
    Busqueda.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
    Afiliados.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
    Ordenes.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
  }
  
  public void ValidarUsuario(){
      if(TipoUsuario.equals("Administrador")){
          jButton21.setEnabled(true);
          jButton22.setEnabled(true);
          jButton23.setEnabled(true);
          jButton24.setEnabled(true);
          jButton25.setEnabled(true);
          
          jButton19.setEnabled(true);
          jButton20.setEnabled(true);
          jMenuItem2.setEnabled(true);
          BTNRegistrarPago.setEnabled(true);
          
          jButton1.setEnabled(true);
          jButton2.setEnabled(true);
          jButton9.setEnabled(true);
          jButton10.setEnabled(true);
          jButton15.setEnabled(true);
          jButton3.setEnabled(true);
          
          TABLAUsuariosSistema.setEnabled(true);
          TABLAFuneraria.setEnabled(true);
          TABLAParentescos.setEnabled(true);
          TABLACiudades.setEnabled(true);
          TABLACobradores.setEnabled(true);
          TABLAVendedores.setEnabled(true);
      }else if(TipoUsuario.equals("Cobrador")){
          jButton21.setEnabled(false);
          jButton22.setEnabled(false);
          jButton23.setEnabled(false);
          jButton24.setEnabled(false);
          jButton25.setEnabled(false);
          
          jButton19.setEnabled(false);
          jButton20.setEnabled(false);
          jMenuItem2.setEnabled(false);
          BTNRegistrarPago.setEnabled(true);
          
          jButton1.setEnabled(false);
          jButton2.setEnabled(false);
          jButton9.setEnabled(false);
          jButton10.setEnabled(false);
          jButton15.setEnabled(false);
          jButton3.setEnabled(false);
          
          TABLAUsuariosSistema.setEnabled(false);
          TABLAFuneraria.setEnabled(false);
          TABLAParentescos.setEnabled(false);
          TABLACiudades.setEnabled(false);
          TABLACobradores.setEnabled(false);
          TABLAVendedores.setEnabled(false);
      }else{
          jButton21.setEnabled(false);
          jButton22.setEnabled(false);
          jButton23.setEnabled(false);
          jButton24.setEnabled(false);
          jButton25.setEnabled(false);
          
          jButton19.setEnabled(false);
          jButton20.setEnabled(false);
          jMenuItem2.setEnabled(false);
          BTNRegistrarPago.setEnabled(false);
          
          jButton1.setEnabled(false);
          jButton2.setEnabled(false);
          jButton9.setEnabled(false);
          jButton10.setEnabled(false);
          jButton15.setEnabled(false);
          jButton3.setEnabled(false);
          
          TABLAUsuariosSistema.setEnabled(false);
          TABLAFuneraria.setEnabled(false);
          TABLAParentescos.setEnabled(false);
          TABLACiudades.setEnabled(false);
          TABLACobradores.setEnabled(false);
          TABLAVendedores.setEnabled(false);
          
      }
  }
  
  public void OcultarColumnaIDTabla(){
        
        JTableHeader header = TABLAAfiliados.getTableHeader();
        header.setBackground(encabezadostabla);
        header.setForeground(Color.white);
        TABLAAfiliados.getColumnModel().getColumn(0).setMaxWidth(0);
        TABLAAfiliados.getColumnModel().getColumn(0).setMinWidth(0);
        TABLAAfiliados.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        TABLAAfiliados.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
        
        
  }
  
  public void CargarCiudades() {
    this.COMBOCiudadVendedores.removeAllItems();
    this.COMBOCiudadCobrador.removeAllItems();
    Access_connection AC = new Access_connection();
    try {
      Statement stmt = AC.conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT Ciudad FROM Ciudades ORDER BY CodigoCiudad ASC");
      int i = 0;
      while (rs.next()) {
        String Ciudad = rs.getString("Ciudad");
        this.COMBOCiudadVendedores.addItem(Ciudad);
        this.COMBOCiudadCobrador.addItem(Ciudad);
        i++;
      } 
      AC.desconectar();
    } catch (SQLException ex) {
      System.out.println("Error COnsulta Cargar Ciudades");
      log.warn("Hubo en error al X por que: "+ex);
    } 
  }
  
  
  
  public void CargarVentanaVendedores() {
    DefaultTableModel modelo = (DefaultTableModel)this.TABLAVendedores.getModel();
    Access_connection AC = new Access_connection();
    int NumFilas = modelo.getRowCount();
    for (int i = 0; NumFilas > i; i++)
      modelo.removeRow(0); 
    try {
      Statement stmt = AC.conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT Id_Vendedor, Nombres_Vendedor,Direccion_Vendedor, Telefono_Vendedor, ciudad "
              + "FROM Vendedores "
              + "INNER JOIN Ciudades ON Ciudades.codigociudad=Vendedores.Id_Ciudad_Vendedor "
              + "WHERE Id_Vendedor <> 0 ORDER BY Nombres_Vendedor");
      int j = 0;
      while (rs.next()) {
        modelo.addRow(this.rowData);
        String Id_Vendedor = rs.getString("Id_Vendedor");
        String Nombres_Vendedor = rs.getString("Nombres_Vendedor");
        String Direccion_Vendedor = rs.getString("Direccion_Vendedor");
        String Telefono_Vendedor = rs.getString("Telefono_Vendedor");
        String Id_Ciudad_Vendedor = rs.getString("ciudad");
        this.TABLAVendedores.setValueAt(Id_Vendedor, j, 0);
        this.TABLAVendedores.setValueAt(Nombres_Vendedor, j, 1);
        this.TABLAVendedores.setValueAt(Direccion_Vendedor, j, 2);
        this.TABLAVendedores.setValueAt(Telefono_Vendedor, j, 3);
        this.TABLAVendedores.setValueAt(Id_Ciudad_Vendedor, j, 4);
        this.TABLAVendedores.setValueAt(j, j, 5);
        j++;
      } 
      AC.desconectar();
    } catch (SQLException ex) {
      System.out.println("Error COnsulta Cargar Nombres_Vendedor");
      log.warn("Hubo en error al X por que: "+ex);
    } 
    
    
    
    JTableHeader header = TABLAVendedores.getTableHeader();
    header.setBackground(encabezadostabla);
    header.setForeground(Color.white);
        
    Action edite = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
            JTable table = (JTable)e.getSource();
            if(TABLAVendedores.getSelectedRowCount()!=0){
                IdVendedorSeleccionado=Integer.parseInt(TABLAVendedores.getValueAt(TABLAVendedores.getSelectedRow(), 5).toString());
                Editar_Vendedores();
            }else{
                JOptionPane.showMessageDialog(null, "Seleccione una fila primero!");
            }  
        }
    };

    BotonenTablaEditar buttonColumn2 = new BotonenTablaEditar(TABLAVendedores, edite, 5);
    buttonColumn2.setMnemonic(KeyEvent.VK_D);
        
  }
  
  private void Editar_Vendedores(){
      System.err.println("IdSelecionado: "+IdVendedorSeleccionado);
      IdVendedor.setText(TABLAVendedores.getValueAt(IdVendedorSeleccionado, 0).toString());
      NombresVendedor.setText(TABLAVendedores.getValueAt(IdVendedorSeleccionado, 1).toString());
      jTextField3.setText(TABLAVendedores.getValueAt(IdVendedorSeleccionado, 2).toString());
      TelefonoVendedor.setText(TABLAVendedores.getValueAt(IdVendedorSeleccionado, 3).toString());
      COMBOCiudadVendedores.setSelectedItem(TABLAVendedores.getValueAt(IdVendedorSeleccionado, 4).toString());
      
  }
  
  
  public void CargarVentanaCobradores() {
    DefaultTableModel modelo = (DefaultTableModel)this.TABLACobradores.getModel();
    Access_connection AC = new Access_connection();
    int NumFilas = modelo.getRowCount();
    for (int i = 0; NumFilas > i; i++)
      modelo.removeRow(0); 
    try {
      Statement stmt = AC.conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT Id_Cobrador, Nombres_Cobrador,Direccion_Cobrador, Telefono_Cobrador, ciudad "
              + "FROM Cobradores "
              + "INNER JOIN Ciudades ON Ciudades.codigociudad=Cobradores.Id_Ciudad_Cobrador "
              + "WHERE Id_Cobrador <> 0 ORDER BY Nombres_Cobrador ASC");
      int j = 0;
      while (rs.next()) {
        modelo.addRow(this.rowData);
        String Id_Cobrador = rs.getString("Id_Cobrador");
        String Nombres_Cobrador = rs.getString("Nombres_Cobrador");
        String Direccion_Cobrador = rs.getString("Direccion_Cobrador");
        String Telefono_Cobrador = rs.getString("Telefono_Cobrador");
        String Id_Ciudad_Cobrador = rs.getString("ciudad");
        this.TABLACobradores.setValueAt(Id_Cobrador, j, 0);
        this.TABLACobradores.setValueAt(Nombres_Cobrador, j, 1);
        this.TABLACobradores.setValueAt(Direccion_Cobrador, j, 2);
        this.TABLACobradores.setValueAt(Telefono_Cobrador, j, 3);
        this.TABLACobradores.setValueAt(Id_Ciudad_Cobrador, j, 4);
        this.TABLACobradores.setValueAt(j, j, 5);
        j++;
      } 
      AC.desconectar();
    } catch (SQLException ex) {
      log.warn("Hubo en error al COnsulta Cargar Nombres_CObradores por que: "+ex);
    } 
    
    JTableHeader header = TABLACobradores.getTableHeader();
    header.setBackground(encabezadostabla);
    header.setForeground(Color.white);
        
    Action edite = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
            JTable table = (JTable)e.getSource();
            if(TABLACobradores.getSelectedRowCount()!=0){
                IdVendedorSeleccionado=Integer.parseInt(TABLACobradores.getValueAt(TABLACobradores.getSelectedRow(), 5).toString());
                Editar_Cobradores();
            }else{
                JOptionPane.showMessageDialog(null, "Seleccione una fila primero!");
            }  
        }
    };

    BotonenTablaEditar buttonColumn2 = new BotonenTablaEditar(TABLACobradores, edite, 5);
    buttonColumn2.setMnemonic(KeyEvent.VK_D);
    
  }
  
   private void Editar_Cobradores(){
      System.err.println("IdSelecionado: "+IdVendedorSeleccionado);
      IdCobrador.setText(TABLACobradores.getValueAt(IdVendedorSeleccionado, 0).toString());
      NombresCobrador.setText(TABLACobradores.getValueAt(IdVendedorSeleccionado, 1).toString());
      DireccionCobrador.setText(TABLACobradores.getValueAt(IdVendedorSeleccionado, 2).toString());
      TelefonoCobrador.setText(TABLACobradores.getValueAt(IdVendedorSeleccionado, 3).toString());
      COMBOCiudadCobrador.setSelectedItem(TABLACobradores.getValueAt(IdVendedorSeleccionado, 4).toString());
      
  }
  
  public void CargarVentanaCiudades() {
    DefaultTableModel modelo = (DefaultTableModel)this.TABLACiudades.getModel();
    Access_connection AC = new Access_connection();
    int NumFilas = modelo.getRowCount();
    for (int i = 0; NumFilas > i; i++)
      modelo.removeRow(0); 
    try {
      Statement stmt = AC.conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT CodigoCiudad, Ciudad FROM Ciudades WHERE CodigoCiudad <> 0 ORDER BY Ciudad ASC");
      int j = 0;
      while (rs.next()) {
        modelo.addRow(this.rowData);
        String CodigoCiudad = rs.getString("CodigoCiudad");
        String Ciudad = rs.getString("Ciudad");
        modelo.setValueAt(CodigoCiudad, j, 0);
        modelo.setValueAt(Ciudad, j, 1);
        modelo.setValueAt(j, j, 2);
        j++;
      } 
      AC.desconectar();
    } catch (SQLException ex) {
      
      log.warn("Hubo en error al COnsulta Cargar Nombres_Vendedor por que: "+ex);
    } 
    
    
    JTableHeader header = TABLACiudades.getTableHeader();
    header.setBackground(encabezadostabla);
    header.setForeground(Color.white);
        
    Action edite = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
            JTable table = (JTable)e.getSource();
            if(TABLACiudades.getSelectedRowCount()!=0){
                IdVendedorSeleccionado=Integer.parseInt(TABLACiudades.getValueAt(TABLACiudades.getSelectedRow(), 2).toString());
                Editar_Ciudades();
            }else{
                JOptionPane.showMessageDialog(null, "Seleccione una fila primero!");
            }  
        }
    };

    BotonenTablaEditar buttonColumn2 = new BotonenTablaEditar(TABLACiudades, edite, 2);
    buttonColumn2.setMnemonic(KeyEvent.VK_D);
    
  }
  
  private void Editar_Ciudades(){
      System.err.println("IdSelecionado: "+IdVendedorSeleccionado);
      CodCiudad.setText(TABLAVendedores.getValueAt(IdVendedorSeleccionado, 0).toString());
      NombreCiudad.setText(TABLAVendedores.getValueAt(IdVendedorSeleccionado, 1).toString());
      
  }
  
  
  public void CargarVentanaParentescos() {
    DefaultTableModel modelo = (DefaultTableModel)this.TABLAParentescos.getModel();
    Access_connection AC = new Access_connection();
    int NumFilas = modelo.getRowCount();
    for (int i = 0; NumFilas > i; i++)
      modelo.removeRow(0); 
    try {
      Statement stmt = AC.conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT Id, Parentezco FROM Parentezcos "
              + "WHERE Id <> 0 ORDER BY Parentezco ASC");
      int j = 0;
      while (rs.next()) {
        modelo.addRow(this.rowData);
        String Id = rs.getString("Id");
        String Parentezco = rs.getString("Parentezco");
        modelo.setValueAt(Id, j, 0);
        modelo.setValueAt(Parentezco, j, 1);
        modelo.setValueAt(j, j, 2);
        j++;
      } 
      AC.desconectar();
    } catch (SQLException ex) {
      
      log.warn("Hubo en error al COnsulta Cargar Parentesco por que: "+ex);
    } 
    
    JTableHeader header = TABLAParentescos.getTableHeader();
    header.setBackground(encabezadostabla);
    header.setForeground(Color.white);
        
    Action edite = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
            JTable table = (JTable)e.getSource();
            if(TABLAParentescos.getSelectedRowCount()!=0){
                IdVendedorSeleccionado=Integer.parseInt(TABLAParentescos.getValueAt(TABLAParentescos.getSelectedRow(), 2).toString());
                Editar_Parentezcos();
            }else{
                JOptionPane.showMessageDialog(null, "Seleccione una fila primero!");
            }  
        }
    };

    BotonenTablaEditar buttonColumn2 = new BotonenTablaEditar(TABLAParentescos, edite, 2);
    buttonColumn2.setMnemonic(KeyEvent.VK_D);
    
  }
  
  private void Editar_Parentezcos(){
      System.err.println("IdSelecionado: "+IdVendedorSeleccionado);
      TXTCodParentesco.setText(TABLAParentescos.getValueAt(IdVendedorSeleccionado, 0).toString());
      TXTParentesco.setText(TABLAParentescos.getValueAt(IdVendedorSeleccionado, 1).toString());
  }
  
  public void GuardarVentanaVendedores() {
    Access_connection AC = new Access_connection();
    int Campo1 = Integer.parseInt(this.IdVendedor.getText());
    String Campo2 = this.NombresVendedor.getText();
    String Campo3 = this.jTextField3.getText();
    long Campo4 = Long.parseLong(this.TelefonoVendedor.getText());
    int Campo5 = this.COMBOCiudadVendedores.getSelectedIndex();
    try {
      Statement stmt = AC.conn.createStatement();
      stmt.executeUpdate("INSERT INTO Vendedores VALUES (" + Campo1 + ",'" + Campo2 + "','" + Campo3 + "'," + Campo4 + "," + Campo5 + ")");
      AC.conn.close();
      this.IdVendedor.setText("");
      this.NombresVendedor.setText("");
      this.jTextField3.setText("");
      this.TelefonoVendedor.setText("");
      this.COMBOCiudadVendedores.setSelectedIndex(0);
      AC.desconectar();
    } catch (SQLException ex) {
      System.out.println("El Vendedor ya Existe se procede a actualizar");
      try {
        Statement stmt = AC.conn.createStatement();
        stmt.executeUpdate("UPDATE Vendedores SET Nombres_Vendedor='" + Campo2 + "', Direccion_Vendedor='" + Campo3 + "', Telefono_Vendedor=" + Campo4 + ", Id_Ciudad_Vendedor=" + Campo5 + " WHERE Id_Vendedor=" + Campo1 + "");
        this.IdVendedor.setText("");
        this.NombresVendedor.setText("");
        this.jTextField3.setText("");
        this.TelefonoVendedor.setText("");
        this.COMBOCiudadVendedores.setSelectedIndex(0);
        AC.desconectar();
      } catch (SQLException e) {
        System.out.println(e);
      } 
    } 
    CargarVentanaVendedores();
  }
  
  public void GuardarVentanaCobradores() {
    Access_connection AC = new Access_connection();
    String campo1 = this.IdCobrador.getText();
    int Campo1 = Integer.parseInt(campo1);
    String Campo2 = this.NombresCobrador.getText();
    String Campo3 = this.DireccionCobrador.getText();
    String Campo4 = this.TelefonoCobrador.getText();
    int Campo5 = this.COMBOCiudadCobrador.getSelectedIndex();
    try {
      Statement stmt = AC.conn.createStatement();
      stmt.executeUpdate("INSERT INTO Cobradores VALUES (" + Campo1 + ",'" + Campo2 + "','" + Campo3 + "','" + Campo4 + "'," + Campo5 + ")");
      this.IdCobrador.setText("");
      this.NombresCobrador.setText("");
      this.DireccionCobrador.setText("");
      this.TelefonoCobrador.setText("");
      this.COMBOCiudadCobrador.setSelectedIndex(0);
      AC.desconectar();
    } catch (SQLException ex) {
      System.out.println("El Cobrador ya Existe se procede a actualizar");
      try {
        Statement stmt = AC.conn.createStatement();
        stmt.executeUpdate("UPDATE Cobradores SET Nombres_Cobrador='" + Campo2 + "', Direccion_Cobrador='" + Campo3 + "', Telefono_Cobrador='" + Campo4 + "', Id_Ciudad_Cobrador=" + Campo5 + " WHERE Id_Cobrador=" + Campo1 + "");
        this.IdCobrador.setText("");
        this.NombresCobrador.setText("");
        this.DireccionCobrador.setText("");
        this.TelefonoCobrador.setText("");
        this.COMBOCiudadCobrador.setSelectedIndex(0);
        AC.desconectar();
      } catch (SQLException e) {
        System.out.println(e);
      } 
    } 
    CargarVentanaCobradores();
  }
  
  public void GuardarVentanaCiudades() {
    Access_connection AC = new Access_connection();
    String campo1 = this.CodCiudad.getText();
    int Campo1 = Integer.parseInt(campo1);
    String Campo2 = this.NombreCiudad.getText();
    try {
      Statement stmt = AC.conn.createStatement();
      stmt.executeUpdate("INSERT INTO Ciudades VALUES (" + Campo1 + ",'" + Campo2 + "')");
      AC.conn.close();
      this.CodCiudad.setText("");
      this.NombreCiudad.setText("");
      AC.desconectar();
    } catch (SQLException ex) {
      System.out.println("La Ciudad ya Existe se procede a actualizar");
      try {
        Statement stmt = AC.conn.createStatement();
        stmt.executeUpdate("UPDATE Ciudades SET Ciudad='" + Campo2 + "' WHERE CodigoCiudad=" + Campo1 + " ");
        this.CodCiudad.setText("");
        this.NombreCiudad.setText("");
        AC.desconectar();
      } catch (SQLException e) {
        System.out.println(e);
      } 
    } 
    CargarVentanaCiudades();
  }
  
  public void GuardarVentanaParentesco() {
    Access_connection AC = new Access_connection();
    String campo1 = this.TXTCodParentesco.getText();
    int Campo1 = Integer.parseInt(campo1);
    String Campo2 = this.TXTParentesco.getText();
    try {
      Statement stmt = AC.conn.createStatement();
      stmt.executeUpdate("INSERT INTO Parentezcos VALUES (" + Campo1 + ",'" + Campo2 + "')");
      AC.conn.close();
      this.TXTCodParentesco.setText("");
      this.TXTParentesco.setText("");
      AC.desconectar();
    } catch (SQLException ex) {
      System.out.println("El Parentesco ya Existe se procede a actualizar");
      try {
        Statement stmt = AC.conn.createStatement();
        stmt.executeUpdate("UPDATE Parentezcos SET Parentezco='" + Campo2 + "' WHERE Id=" + Campo1 + " ");
        this.TXTCodParentesco.setText("");
        this.TXTParentesco.setText("");
        AC.desconectar();
      } catch (SQLException e) {
        System.out.println(e);
      } 
    } 
    CargarVentanaParentescos();
  }
  
  public void GuardarFuneraria() {
    Access_connection AC = new Access_connection();
    int Campo1 = Integer.parseInt(this.TXTCodFuneraria.getText());
    String Campo2 = this.TXTFuneraria.getText();
    try {
      Statement stmt = AC.conn.createStatement();
      stmt.executeUpdate("INSERT INTO Funerarias VALUES (" + Campo1 + ",'" + Campo2 + "')");
      AC.conn.close();
      this.TXTCodFuneraria.setText("");
      this.TXTFuneraria.setText("");
      AC.desconectar();
    } catch (SQLException ex) {
      System.out.println("La funeraria ya Existe se procede a actualizar");
      try {
        Statement stmt = AC.conn.createStatement();
        stmt.executeUpdate("UPDATE Funerarias SET Nombre='" + Campo2 + "' WHERE Id=" + Campo1 + " ");
        this.TXTCodFuneraria.setText("");
        this.TXTFuneraria.setText("");
        AC.desconectar();
      } catch (SQLException e) {
        System.out.println(e);
      } 
    } 
    CargarVentanaFuneraria();
  }
  
  public void CargarVentanaFuneraria() {
    DefaultTableModel modelo = (DefaultTableModel)this.TABLAFuneraria.getModel();
    Access_connection AC = new Access_connection();
    int NumFilas = modelo.getRowCount();
    for (int i = 0; NumFilas > i; i++)
      modelo.removeRow(0); 
    try {
      Statement stmt = AC.conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT Id, Nombre FROM Funerarias WHERE Id <> 0 ORDER BY Nombre ASC");
      int j = 0;
      while (rs.next()) {
        modelo.addRow(this.rowData);
        String Id = rs.getString("Id");
        String Parentezco = rs.getString("Nombre");
        modelo.setValueAt(Id, j, 0);
        modelo.setValueAt(Parentezco, j, 1);
        modelo.setValueAt(j, j, 2);
        j++;
      } 
      AC.desconectar();
    } catch (SQLException ex) {
      log.warn("Error Consulta Cargar Funeraria"+ex);
    } 
    
    JTableHeader header = TABLAFuneraria.getTableHeader();
    header.setBackground(encabezadostabla);
    header.setForeground(Color.white);
        
    Action edite = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
            JTable table = (JTable)e.getSource();
            if(TABLAFuneraria.getSelectedRowCount()!=0){
                IdVendedorSeleccionado=Integer.parseInt(TABLAFuneraria.getValueAt(TABLAFuneraria.getSelectedRow(), 2).toString());
                Editar_Funeraria();
            }else{
                JOptionPane.showMessageDialog(null, "Seleccione una fila primero!");
            }  
        }
    };

    BotonenTablaEditar buttonColumn2 = new BotonenTablaEditar(TABLAFuneraria, edite, 2);
    buttonColumn2.setMnemonic(KeyEvent.VK_D);
    
  }
  
  private void Editar_Funeraria(){
      System.err.println("IdSelecionado: "+IdVendedorSeleccionado);
      TXTCodFuneraria.setText(TABLAFuneraria.getValueAt(IdVendedorSeleccionado, 0).toString());
      TXTFuneraria.setText(TABLAFuneraria.getValueAt(IdVendedorSeleccionado, 1).toString());
  }
  
  public void EliminarFuneraria() {
    Access_connection AC = new Access_connection();
    try {
      Statement stmt = AC.conn.createStatement();
      stmt.executeUpdate("DELETE FROM Funerarias WHERE Id=" + this.TABLAFuneraria.getValueAt(this.TABLAFuneraria.getSelectedRow(), 0) + "");
      System.out.println("Se ha eliminado correctamente la funeraria");
      AC.desconectar();
      CargarVentanaFuneraria();
    } catch (Exception e) {
        log.warn("falló borrado de la funeraria"+e);
      
    } 
  }
  
  public void CargarVentanaUsuarios() {
    DefaultTableModel modelo = (DefaultTableModel)this.TABLAUsuariosSistema.getModel();
    Access_connection AC = new Access_connection();
    int NumFilas = modelo.getRowCount();
    for (int i = 0; NumFilas > i; i++)
      modelo.removeRow(0); 
    try {
      Statement stmt = AC.conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT Id, Nombres,TipoUsuario, Nick, Pass  FROM UsuariosSistema "
              + "WHERE Nombres<>'Admin' ORDER BY Nombres ASC");
      int j = 0;
      while (rs.next()) {
        modelo.addRow(this.rowData);
        String Id = rs.getString("Id");
        String Nombres = rs.getString("Nombres");
        String TipoUsuario = rs.getString("TipoUsuario");
        String Nick = rs.getString("Nick");
        String Pass = "*****";
        modelo.setValueAt(Id, j, 0);
        modelo.setValueAt(Nombres, j, 1);
        modelo.setValueAt(TipoUsuario, j, 2);
        modelo.setValueAt(Nick, j, 3);
        modelo.setValueAt(Pass, j, 4);
        modelo.setValueAt(j, j, 5);
        j++;
      } 
      AC.desconectar();
    } catch (SQLException ex) {
    
      log.warn("Hubo en error al COnsulta Cargar UsuariosSistema por que: "+ex);
    } 
    
    JTableHeader header = TABLAUsuariosSistema.getTableHeader();
    header.setBackground(encabezadostabla);
    header.setForeground(Color.white);
        
    Action edite = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
            JTable table = (JTable)e.getSource();
            if(TABLAUsuariosSistema.getSelectedRowCount()!=0){
                IdVendedorSeleccionado=Integer.parseInt(TABLAUsuariosSistema.getValueAt(TABLAUsuariosSistema.getSelectedRow(), 5).toString());
                Editar_Usuarios();
            }else{
                JOptionPane.showMessageDialog(null, "Seleccione una fila primero!");
            }  
        }
    };

    BotonenTablaEditar buttonColumn2 = new BotonenTablaEditar(TABLAUsuariosSistema, edite, 5);
    buttonColumn2.setMnemonic(KeyEvent.VK_D);
    
  }
  
   private void Editar_Usuarios(){
      System.err.println("IdSelecionado: "+IdVendedorSeleccionado);
      IdUsuario.setText(TABLAUsuariosSistema.getValueAt(IdVendedorSeleccionado, 0).toString());
      NombresUsuarios.setText(TABLAUsuariosSistema.getValueAt(IdVendedorSeleccionado, 1).toString());
      COMBOTipoUsuario.setSelectedItem(TABLAUsuariosSistema.getValueAt(IdVendedorSeleccionado, 2).toString());
      NickUsuario.setText(TABLAUsuariosSistema.getValueAt(IdVendedorSeleccionado, 3).toString());
      PassUsuario.setText(TABLAUsuariosSistema.getValueAt(IdVendedorSeleccionado, 4).toString());
      
      
  }
  
  public void GuardarVentanaUsuarios() {
    Access_connection AC = new Access_connection();
    String campo1 = this.IdUsuario.getText();
    int Campo1 = Integer.parseInt(campo1);
    String Campo2 = this.NombresUsuarios.getText();
    String Campo3 = this.COMBOTipoUsuario.getSelectedItem().toString();
    String Campo4 = this.NickUsuario.getText();
    String Campo5 = this.PassUsuario.getText();
    try {
      Statement stmt = AC.conn.createStatement();
      stmt.executeUpdate("INSERT INTO UsuariosSistema VALUES (" + Campo1 + ",'" + Campo2 + "','" + Campo3 + "','" + Campo4 + "','" + Campo5 + "')");
      AC.desconectar();
      this.IdUsuario.setText("");
      this.NombresUsuarios.setText("");
      this.NickUsuario.setText("");
      this.PassUsuario.setText("");
    } catch (SQLException ex) {
      System.out.println("El Usuario ya Existe se procede a actualizar");
      try {
        Statement stmt = AC.conn.createStatement();
        stmt.executeUpdate("UPDATE UsuariosSistema SET Nombres='" + Campo2 + "', TipoUsuario='" + Campo3 + "',Nick='" + Campo4 + "', Pass='" + Campo5 + "'WHERE Id=" + Campo1 + "");
        this.IdUsuario.setText("");
        this.NombresUsuarios.setText("");
        this.NickUsuario.setText("");
        this.PassUsuario.setText("");
        AC.desconectar();
      } catch (SQLException e) {
        System.out.println(e);
      } 
    } 
    CargarVentanaUsuarios();
  }
  
  public void CargarTablas(){
      (new Thread(new CargarTablas())).start();
  }
  
  private class CargarTablas implements Runnable {
    private CargarTablas() {}
    
    public void run() {
        FechActual();
        
        jTextField1.setEnabled(false);
        jButton19.setEnabled(false);
        
        CargarDesplegableCiudades();
        CargarDesplegableParentezcos();
        CargarDesplegableVendedores();
        CargarDesplegableFormasdePago();
        CargarDesplegableCobradores();
        TXTCuotasNulas.setText("0");
        
        jTextField1.setEnabled(true);
        jTextField1.requestFocus();
    }
  }
  
  private class GuardarAdministracion_Vendedores implements Runnable {
    private GuardarAdministracion_Vendedores() {}
    
    public void run() {
        jButton1.setEnabled(false);
        jButton1.setText("Guardando...");
        
        GuardarVentanaVendedores();
        
        jButton1.setEnabled(true);
        jButton1.setText("Guardar");
    }
  }
  
  
  private class CargarAdministracion_Vendedores implements Runnable {
    private CargarAdministracion_Vendedores() {}
    
    public void run() {
        CargarVentanaVendedores();
    }
  }
  
  private class GuardarAdministracion_Cobradores implements Runnable {
    private GuardarAdministracion_Cobradores() {}
    
    public void run() {
        jButton2.setEnabled(false);
        jButton2.setText("Guardando...");
        GuardarVentanaCobradores();
        jButton2.setEnabled(true);
        jButton2.setText("Guardar");
    }
  }
  
  private class CargarAdministracion_Cobradores implements Runnable {
    private CargarAdministracion_Cobradores() {}
    
    public void run() {
        CargarVentanaCobradores();
    }
  }
  
  private class GuardarAdministracion_Ciudades implements Runnable {
    private GuardarAdministracion_Ciudades() {}
    
    public void run() {
        jButton9.setEnabled(false);
        jButton9.setText("Guardando...");
        GuardarVentanaCiudades();
        jButton9.setEnabled(true);
        jButton9.setText("Guardar");
        
    }
  }
  
  private class CargarAdministracion_Ciudades implements Runnable {
    private CargarAdministracion_Ciudades() {}
    
    public void run() {
        CargarVentanaCiudades();
    }
  }
  
  private class GuardarAdministracion_Parentezcos implements Runnable {
    private GuardarAdministracion_Parentezcos() {}
    
    public void run() {
        jButton10.setEnabled(false);
        jButton10.setText("Guardando...");
        GuardarVentanaParentesco();
        jButton10.setEnabled(true);
        jButton10.setText("Guardar");
    }
  }
  
  private class CargarAdministracion_Parentezcos implements Runnable {
    private CargarAdministracion_Parentezcos() {}
    
    public void run() {
        CargarVentanaParentescos();
    }
  }
  
  private class GuardarAdministracion_Funerarias implements Runnable {
    private GuardarAdministracion_Funerarias() {}
    
    public void run() {
        jButton15.setEnabled(false);
        jButton15.setText("Guardando...");
        GuardarFuneraria();
        jButton15.setEnabled(false);
        jButton15.setText("Guardar");
    }
  }
  
  private class CargarAdministracion_Funerarias implements Runnable {
    private CargarAdministracion_Funerarias() {}
    
    public void run() {
        CargarVentanaFuneraria();
    }
  }
  
    private class GuardarAdministracion_UsuariosSistema implements Runnable {
    private GuardarAdministracion_UsuariosSistema() {}
    
    public void run() {
        jButton3.setEnabled(false);
        jButton3.setText("Guardando...");
        GuardarVentanaUsuarios();
        jButton3.setEnabled(true);
        jButton3.setText("Guardar");
        
    }
  }
  
  
    private class GenerarOrdenServicio implements Runnable {
    private GenerarOrdenServicio() {}
    
    public void run() {
        ValidarAfiliadoExistente();
        TXTCodAfiliado.setText(String.valueOf(NumAfiliado));
        Cargar_OrdendeServicio();
        
    }
  }
            
    
  private class CargarAdministracion_UsuariosSistema implements Runnable {
    private CargarAdministracion_UsuariosSistema() {}
    
    public void run() {
        CargarVentanaUsuarios();
    }
  }
  
  //Centro de Búsqueda
  private class FiltrarBusqueda implements Runnable {
    private FiltrarBusqueda() {}
    
    public void run() {
      FiltrarBusqueda();
    }
  }
  
  public void FiltrarBusqueda() {
    DefaultTableModel modelo = (DefaultTableModel)this.TABLABusClientes.getModel();
    if (this.TXTBusqueda.getText().equals("")) {
      JOptionPane.showMessageDialog(null, "Antes de Presionar Buscar Verifique que el Campo de Busqueda est\nDiligenciado Correctamente :D");
    } else {
        
        int NumFilas = modelo.getRowCount();
        for (int i = 0; NumFilas > i; i++){
          modelo.removeRow(0);   
        }
          
        BusquedaDatos = this.TXTBusqueda.getText();
        
        new Thread(new BuscarClientesCodCliente()).start();  
        new Thread(new BuscarIdentificacionCliente()).start();  
        new Thread(new BuscarNombresCliente()).start();  
        new Thread(new BuscarClientesNContrato()).start();  
        
      
        this.TABLABusClientes.requestFocus(true);
        this.TABLABusClientes.getSelectionModel().setSelectionInterval(0, 0);
    } 
  }
  
  public void LimpiarTablasAfiliados() {
    DefaultTableModel modeloAfiliados = (DefaultTableModel)this.TABLAAfiliados.getModel();
    
    int NumFilasAfiliados = modeloAfiliados.getRowCount();
    for (int i = 0; NumFilasAfiliados > i; i++)
      modeloAfiliados.removeRow(0); 
  }
  
  
  
  private class BuscarClientes implements Runnable {
    private BuscarClientes() {}
    
    public void run() {
      BuscarClientes();
    }
  }
  
  public void BuscarClientes() {
    Access_connection AC = new Access_connection();
    DefaultTableModel modelo = (DefaultTableModel)this.TABLABusClientes.getModel();
    int NumFilas = modelo.getRowCount();
    for (int i = 0; NumFilas > i; i++)
      modelo.removeRow(0); 
    try {
      Statement stmt = AC.conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT Num_Contrato, Cod_Cliente, Nombres, Cedula FROM Contratos INNER JOIN Afiliados ON Contratos.Cod_Cliente = Afiliados.NContratovsAfiliados WHERE Num_Afiliado = 1");
      int j = 0;
      while (rs.next()) {
        modelo.addRow(this.rowData);
        int NumContrato = rs.getInt("Num_Contrato");
        int CodCliente = rs.getInt("Cod_Cliente");
        String NombresApellidos = rs.getString("Nombres");
        int Cedula = rs.getInt("Cedula");
        this.TABLABusClientes.setValueAt(Integer.valueOf(NumContrato), j, 0);
        this.TABLABusClientes.setValueAt(Integer.valueOf(CodCliente), j, 1);
        this.TABLABusClientes.setValueAt(NombresApellidos, j, 2);
        this.TABLABusClientes.setValueAt(Integer.valueOf(Cedula), j, 3);
        j++;
      } 
      AC.desconectar();
    } catch (SQLException ex) {
      System.out.println("Hubo Un Problema al Cargar los Clientes en la Tabla"+ex);
    } 
  }
  
  private class BuscarClientesNContrato implements Runnable {
        private BuscarClientesNContrato() {}

        public void run(){
            try {
                BuscarClientesNContrato();
            } catch (Exception e) {
            }
            
        
        }
    }
  
  public void BuscarClientesNContrato() {
    Access_connection AC = new Access_connection();
    DefaultTableModel modelo = (DefaultTableModel)this.TABLABusClientes.getModel();
    
    
    try {
      Statement stmt = AC.conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT Num_Contrato, Cod_Cliente, Nombres, Cedula "
              + "FROM Contratos LEFT JOIN Afiliados ON Contratos.Cod_Cliente = Afiliados.NContratovsAfiliados "
              + "WHERE Num_Contrato=" + BusquedaDatos + "");
      int j = TABLABusClientes.getRowCount();
      while (rs.next()) {
        modelo.addRow(this.rowData);
        String NumContrato = rs.getString("Num_Contrato");
        String CodCliente = rs.getString("Cod_Cliente");
        String NombresApellidos = rs.getString("Nombres");
        Long CC = Long.valueOf(rs.getLong("Cedula"));
        String Cedula = Long.toString(CC.longValue());
        this.TABLABusClientes.setValueAt(NumContrato, j, 0);
        this.TABLABusClientes.setValueAt(CodCliente, j, 1);
        this.TABLABusClientes.setValueAt(NombresApellidos, j, 2);
        this.TABLABusClientes.setValueAt(Cedula, j, 3);
        j++;
      } 
      AC.desconectar();
    } catch (SQLException ex) {
      log.warn("Hubo en error al Buscar por Num de COntrato por que: "+ex);
    } 
  }
  
  private class BuscarClientesCodCliente implements Runnable {
        private BuscarClientesCodCliente() {}

        public void run(){
            try {
                BuscarClientesCodCliente();
            } catch (Exception e) {
            }
            
        
        }
    }
  
  public void BuscarClientesCodCliente() {
    Access_connection AC = new Access_connection();
    DefaultTableModel modelo = (DefaultTableModel)this.TABLABusClientes.getModel();
    
    try {
      Statement stmt = AC.conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT Num_Contrato, Cod_Cliente, Nombres, Cedula "
              + "FROM Contratos "
              + "LEFT JOIN Afiliados ON Contratos.Cod_Cliente = Afiliados.NContratovsAfiliados "
              + "WHERE Cod_Cliente=" + BusquedaDatos + "");
      int j = TABLABusClientes.getRowCount();
      while (rs.next()) {
        modelo.addRow(this.rowData);
        String NumContrato = rs.getString("Num_Contrato");
        String CodCliente = rs.getString("Cod_Cliente");
        String NombresApellidos = rs.getString("Nombres");
        String Cedula = rs.getString("Cedula");
        this.TABLABusClientes.setValueAt(NumContrato, j, 0);
        this.TABLABusClientes.setValueAt(CodCliente, j, 1);
        this.TABLABusClientes.setValueAt(NombresApellidos, j, 2);
        this.TABLABusClientes.setValueAt(Cedula, j, 3);
        j++;
      } 
      AC.desconectar();
    } catch (SQLException ex) {
      log.warn("Hubo en error al escribir en el cuadro de búsqueda por que: "+ex);
    } 
  }
  
  private class BuscarNombresCliente implements Runnable {
        private BuscarNombresCliente() {}

        public void run(){
            try {
                BuscarNombresCliente();
            } catch (Exception e) {
            }
            
        
        }
    }
  
  public void BuscarNombresCliente() {
    Access_connection AC = new Access_connection();
    DefaultTableModel modelo = (DefaultTableModel)this.TABLABusClientes.getModel();
    String Busqueda = this.TXTBusqueda.getText();
//    int NumFilas = modelo.getRowCount();
//    for (int i = 0; NumFilas > i; i++)
//      modelo.removeRow(0); 
    try {
      Statement stmt = AC.conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT Num_Contrato, Cod_Cliente, Nombres, Cedula "
              + "FROM Contratos LEFT JOIN Afiliados ON Contratos.Cod_Cliente = Afiliados.NContratovsAfiliados "
              + "WHERE Nombres LIKE '%" + BusquedaDatos + "%' ORDER BY Nombres ASC");
      int j = TABLABusClientes.getRowCount();
      while (rs.next()) {
        modelo.addRow(this.rowData);
        String NumContrato = rs.getString("Num_Contrato");
        String CodCliente = rs.getString("Cod_Cliente");
        String NombresApellidos = rs.getString("Nombres");
        Long CC = Long.valueOf(rs.getLong("Cedula"));
        String Cedula = Long.toString(CC.longValue());
        this.TABLABusClientes.setValueAt(NumContrato, j, 0);
        this.TABLABusClientes.setValueAt(CodCliente, j, 1);
        this.TABLABusClientes.setValueAt(NombresApellidos, j, 2);
        this.TABLABusClientes.setValueAt(Cedula, j, 3);
        j++;
      } 
      AC.desconectar();
    } catch (SQLException ex) {
      log.warn("Hubo en error al buscar por nombres por que: "+ex);
    } 
  }
  
  private class BuscarIdentificacionCliente implements Runnable {
        private BuscarIdentificacionCliente() {}

        public void run(){
            try {
                BuscarIdentificacionCliente();
            } catch (Exception e) {
            }
            
        
        }
    }
  
  public void BuscarIdentificacionCliente() {
    Access_connection AC = new Access_connection();
    DefaultTableModel modelo = (DefaultTableModel)this.TABLABusClientes.getModel();
    
    
//    int NumFilas = modelo.getRowCount();
//    for (int i = 0; NumFilas > i; i++)
//      modelo.removeRow(0); 
    try {
      Statement stmt = AC.conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT Num_Contrato, Cod_Cliente, Nombres, Cedula "
              + "FROM Contratos LEFT JOIN Afiliados ON Contratos.Cod_Cliente = Afiliados.NContratovsAfiliados "
              + "WHERE Cedula=" + BusquedaDatos + "");
      int j = TABLABusClientes.getRowCount();
      while (rs.next()) {
        modelo.addRow(this.rowData);
        String NumContrato = rs.getString("Num_Contrato");
        String CodCliente = rs.getString("Cod_Cliente");
        String NombresApellidos = rs.getString("Nombres_Apellidos");
        Long CC = Long.valueOf(rs.getLong("Cedula"));
        String Cedula = Long.toString(CC.longValue());
        this.TABLABusClientes.setValueAt(NumContrato, j, 0);
        this.TABLABusClientes.setValueAt(CodCliente, j, 1);
        this.TABLABusClientes.setValueAt(NombresApellidos, j, 2);
        this.TABLABusClientes.setValueAt(Cedula, j, 3);
        j++;
      } 
      AC.desconectar();
    } catch (SQLException ex) {
      log.warn("Hubo en error al buscar por cédula por que: "+ex);
    } 
  }
  
  private class CargarCliente implements Runnable {
    private CargarCliente() {}
    
    public void run() {
      if ((TABLABusClientes.getSelectedRows()).length > 0) {
        Busqueda.dispose();
        CargarCliente();
        (new Thread(new CargarPagos())).start();
        (new Thread(new CargarAfiliados())).start();
        
        Busqueda.dispose();
      } else {
        JOptionPane.showMessageDialog(null, "Primero Seleccione un Cliente");
      } 
    }
  }
  
  private void FechActual() {
    Calendar c1 = Calendar.getInstance();
    String Dia = Integer.toString(c1.get(5));
    String Mes = Integer.toString(c1.get(2) + 1);
    String Anio = Integer.toString(c1.get(1));
    String FechaActual = "" + Dia + "/" + Mes + "/" + Anio + "";
    System.out.println(FechaActual);
    SimpleDateFormat Formato = new SimpleDateFormat("dd/MM/yyyy");
    try {
      Date FActual = Formato.parse(FechaActual);
      this.FECHAFechaContrato.setDate(FActual);
      this.FECHAInactividadContrato.setDate(FActual);
      this.FECHARegistroPago.setDate(FActual);
      System.out.println("Cargada Fecha Actual Contratos");
    } catch (ParseException ex) {
      log.warn("Hubo en error al X por que: "+ex);
    } 
  }
  
  public class CargarDesplegableCiudades implements Runnable {
    public void run() {
      CargarDesplegableCiudades();
    }
  }
  
  public void CargarDesplegableCiudades() {
    this.COMBOCiudadResidencia.removeAllItems();
    COMBOCiudadVendedores.removeAllItems();
    COMBOCiudadCobrador.removeAllItems();
    this.COMBOBarrio.removeAllItems();
    Access_connection AC = new Access_connection();
    try {
      Statement stmt = AC.conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT Ciudad FROM Ciudades ORDER BY CodigoCiudad ASC");
      int i = 0;
      while (rs.next()) {
        String Ciudad = rs.getString("Ciudad");
        this.COMBOCiudadResidencia.addItem(Ciudad);
        COMBOCiudadVendedores.addItem(Ciudad);
        this.COMBOBarrio.addItem(Ciudad);
        this.COMBOCiudadCobrador.addItem(Ciudad);
        i++;
      } 
      System.out.println("Cargada Ciudades Contratos");
      AC.desconectar();
    } catch (SQLException ex) {
      System.out.println("Error COnsulta Cargar Ciudades");
      log.warn("Hubo en error al X por que: "+ex);
    } 
    this.COMBOCiudadResidencia.repaint();
    COMBOCiudadVendedores.repaint();
    this.COMBOBarrio.repaint();
    CargarDesplegableBarrios();
  }
  
  public class CargarDesplegableBarrios implements Runnable {
    public void run() {
      CargarDesplegableBarrios();
    }
  }
  
  private void CargarDesplegableBarrios() {
    this.COMBOBarrio.removeAllItems();
    int CodCiudad = this.COMBOCiudadResidencia.getSelectedIndex();
    this.COMBOBarrio.addItem("Ninguno");
    Access_connection AC = new Access_connection();
    try {
      Statement stmt = AC.conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT Barrio FROM Barrios WHERE CodCiudad=" + CodCiudad + " ORDER BY Barrio ASC");
      int i = 0;
      while (rs.next()) {
        this.COMBOBarrio.addItem(rs.getString("Barrio"));
        i++;
      } 
      AC.desconectar();
    } catch (SQLException ex) {
      System.out.println("Error COnsulta Cargar Barrios");
      log.warn("Hubo en error al X por que: "+ex);
    } 
  }
  
  
  public class CargarDesplegableParentezcos implements Runnable {
    public void run() {
      CargarDesplegableParentezcos();
    }
  }
  
  private void CargarDesplegableParentezcos() {
    this.COMBOParentescoAfiliado.removeAllItems();
    
    Access_connection AC = new Access_connection();
    try {
      Statement stmt = AC.conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT Parentezco FROM Parentezcos");
      int i = 0;
      while (rs.next()) {
        String Parentezco = rs.getString("Parentezco");
        this.COMBOParentescoAfiliado.addItem(Parentezco);
        i++;
      } 
      System.out.println("Cargada Parentezcos Contratos");
      AC.desconectar();
    } catch (SQLException ex) {
      System.out.println("Error COnsulta Cargar Parentescos");
      log.warn("Hubo en error al X por que: "+ex);
    } 
  }
  
  private void CargarDesplegableVendedores() {
    this.COMBOVendedor.removeAllItems();
    Access_connection AC = new Access_connection();
    try {
      Statement stmt = AC.conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT Nombres_Vendedor FROM Vendedores ORDER BY Id_Vendedor ASC");
      int i = 0;
      while (rs.next()) {
        String Nombres_Vendedor = rs.getString("Nombres_Vendedor");
        this.COMBOVendedor.addItem(Nombres_Vendedor);
        i++;
      } 
      System.out.println("Cargada Vendedores COntratos");
      AC.desconectar();
    } catch (SQLException ex) {
      
      log.warn("Hubo en error al Cargar Nombres_Vendedor por que: "+ex);
    } 
    CargarDesplegableBarrios();
  }
  
  public class CargarDesplegableFormasdePago implements Runnable {
    public void run() {
      CargarDesplegableFormasdePago();
    }
  }
  
  private void CargarDesplegableFormasdePago() {
    this.COMBOFormadePago.removeAllItems();
    Access_connection AC = new Access_connection();
    try {
      Statement stmt = AC.conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT Forma_de_Pago FROM FormasPago ORDER BY Id ASC");
      int i = 0;
      while (rs.next()) {
        String Forma_de_Pago = rs.getString("Forma_de_Pago");
        this.COMBOFormadePago.addItem(Forma_de_Pago);
        i++;
      } 
      System.out.println("Cargada Formas de Pago Contratos");
      AC.desconectar();
    } catch (SQLException ex) {
    
      log.warn("Hubo en error al Cargar Forma_de_Pago por que: "+ex);
    } 
  }
  
  //Contratos
  
  private void CargarCliente() {
    
    int FilaSeleccionada = this.TABLABusClientes.getSelectedRow();
    System.out.println(FilaSeleccionada);
    Object NCSeleccionado = this.TABLABusClientes.getValueAt(FilaSeleccionada, 1);
    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
    
    
    Access_connection AC = new Access_connection();
    
    
    
    try {
      Statement stmt = AC.conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT Num_Contrato,Cod_Cliente,Fecha_Contrato,Id_Ciudad,"
              + "Direccion,Barrio,Telefono,Id_Vendedor,Forma_Pago,Pago,Observaciones,ContratoActivo,"
              + "FechaInactividadContrato FROM Contratos WHERE Cod_Cliente= " + NCSeleccionado + "");
      while (rs.next()) {
        int Num_Contrato = rs.getInt("Num_Contrato");
        String NContrato = Integer.toString(Num_Contrato);
        int CodCliente = rs.getInt("Cod_Cliente");
        String Cod_Cliente = Integer.toString(CodCliente);
        try {
          this.FECHAFechaContrato.setDate(rs.getDate("Fecha_Contrato"));
        } catch (SQLException e) {
          try {
            this.FECHAFechaContrato.setDate(this.FormatoFecha2.parse(rs.getString("Fecha_Contrato")));
          } catch (ParseException ex) {
            log.warn("Hubo en error al parsear fecha de cotnrato por que: "+ex);
          } 
        } 
        int Id_Ciudad = rs.getInt("Id_Ciudad");
        String TDireccion = rs.getString("Direccion");
        String Barrio = rs.getString("Barrio");
        String Telefono = rs.getString("Telefono");
        int Id_Vendedor = rs.getInt("Id_Vendedor");
        int Forma_Pago = rs.getInt("Forma_Pago");
        String Pago = rs.getString("Pago");
        String TObservaciones = rs.getString("Observaciones");
        String contratoactivo = rs.getString("ContratoActivo");
        try {
          this.FECHAInactividadContrato.setDate(rs.getDate("FechaInactividadContrato"));
        } catch (Exception e) {
          try {
            this.FECHAInactividadContrato.setDate(this.FormatoFecha2.parse(rs.getString("FechaInactividadContrato")));
          } catch (ParseException ex) {
            log.warn("Hubo en error al parsear la fecha inactividad de cotnrato por que: "+ex);
          } 
        } 
        this.TXTNumContrato.setText(NContrato);
        this.TXTCodCliente.setText(Cod_Cliente);
        this.COMBOCiudadResidencia.setSelectedIndex(Id_Ciudad);
        this.TXTDireccion.setText(TDireccion);
        this.COMBOBarrio.setSelectedItem(Barrio);
        this.TXTTelefono.setText(Telefono);
        this.COMBOVendedor.setSelectedIndex(Id_Vendedor);
        this.COMBOFormadePago.setSelectedIndex(Forma_Pago);
        this.TXTValordePago.setText(Pago);
        TXTValorPagar.setText(Pago);
        this.TXTObservaciones.setText(TObservaciones);
        this.ContratoActivo.setSelectedItem(contratoactivo);
      } 
      AC.desconectar();
    } catch (SQLException ex) {
      System.out.println("Error al cargar Contrato por que: " + ex);
    } 
  }
  
  private void ValidarExisteCodCliente(){
      Access_connection AC = new Access_connection();
      int CodCliente=Integer.parseInt(TXTCodCliente.getText());
      int Cuenta=0;
      
      try {
          Statement stmt1 = AC.conn.createStatement();
          ResultSet rs1 = stmt1.executeQuery("SELECT COUNT(cod_cliente) AS Cuenta FROM Contratos WHERE "
                  + "cod_cliente=" + CodCliente + " ");
          while (rs1.next()) {
                Cuenta=rs1.getInt("Cuenta");
          } 
        } catch (SQLException exe) {
            log.warn("Algo falló al cargar los encabezados de derechos, traslados en contrato a partir de afiliados: "+exe);
    } 
      
      if(Cuenta!=0){
          JOptionPane.showMessageDialog(null, "Este Código de Cliente ya Existe, \n"
                  + "Verifique de nuevo el Número para continuar ");
          TXTCodCliente.setText("");
          TXTCodCliente.requestFocus();
      }
  }
  
  private void ValidarContratoExistente(){
      
      System.out.println("El Cod de Afiliado es: "+NumAfiliado);
      if(ContratoActivo.getSelectedIndex()==2&&FECHAInactividadContrato.getDate()==null){
          JOptionPane.showConfirmDialog(null, "Primero Defina la fecha de Inactividad antes de guardar");
          
      }else{
            if(TXTNumContrato.getText().equals("")&&TXTCodCliente.getText().equals("")
              &&COMBOFormadePago.getSelectedIndex()==0&& FECHAFechaContrato.getDate()==null
              &&COMBOVendedor.getSelectedIndex()==0&&COMBOCiudadResidencia.getSelectedIndex()==0
              &&ContratoActivo.getSelectedIndex()==0&&TXTValordePago.getText().equals("")){
          JOptionPane.showConfirmDialog(null, "Todos los Campos son Obligatorios");
      }else{
            Access_connection AC = new Access_connection();
            int CodCliente=Integer.parseInt(TXTCodCliente.getText());
            int Cuenta=0;

            try {
                Statement stmt1 = AC.conn.createStatement();
                ResultSet rs1 = stmt1.executeQuery("SELECT COUNT(cod_cliente) AS Cuenta FROM Contratos WHERE "
                        + "cod_cliente=" + CodCliente + " ");
                while (rs1.next()) {
                      Cuenta=rs1.getInt("Cuenta");
                } 
              } catch (SQLException exe) {
                  log.warn("Algo falló al Validar Contrato Existente: "+exe);
              } 

            if(Cuenta==0){
//                  int reply = JOptionPane.showConfirmDialog(null, "Almacenamiento de Datos\n"
//                                  + "Presione 'Aceptar' para Guardar el contrato\n", 
//                                          "Guardar Contrato", JOptionPane.OK_CANCEL_OPTION);
//                  switch (reply) {
//                      case JOptionPane.YES_OPTION:
                      Guardar_Contrato();
//                      case JOptionPane.CLOSED_OPTION:
//                          System.out.println("A cancelado la actualización");
//                          break;
//                      default:
//                          System.out.println("A cancelado la actualización");
//                          break;
//                  }


            }else{
//                int reply = JOptionPane.showConfirmDialog(null, "Actualización de Datos \n"
//                                  + "Presione 'Aceptar' para Actualizar el Viejo Contrato\n", 
//                                              "Actualización de Contrato", JOptionPane.OK_CANCEL_OPTION);
//              switch (reply) {
//                      case JOptionPane.YES_OPTION:
                      Editar_Contrato();
//                      case JOptionPane.CLOSED_OPTION:
//                          System.out.println("A cancelado la actualización");
//                          break;
//                      default:
//                          System.out.println("A cancelado la actualización");
//                          break;
//                  }



            }
          
        }
      }
      
  }
  
  private void Guardar_Contrato() {
    Access_connection AC = new Access_connection();
    SimpleDateFormat Formato = new SimpleDateFormat("yyyy-MM-dd");
    int Campo1 = Integer.parseInt(this.TXTCodCliente.getText());
    int Campo2 = Integer.parseInt(this.TXTNumContrato.getText());
    String Campo3 = Formato.format(this.FECHAFechaContrato.getDate());
    int Campo4 = this.COMBOCiudadResidencia.getSelectedIndex();
    String Campo5 = this.TXTDireccion.getText();
    String Campo6 = this.COMBOBarrio.getSelectedItem().toString();
    String Campo7 = this.TXTTelefono.getText();
    int Campo8 = this.COMBOFormadePago.getSelectedIndex();
    String Campo9 = this.TXTValordePago.getText();
    int Campo10 = this.COMBOVendedor.getSelectedIndex();
    String Campo11 = this.TXTObservaciones.getText();
    String Campo12 = this.ContratoActivo.getSelectedItem().toString();
    
    
    String Dia1="01";
    String Mes1="01";
    String Anio1= "1910";

    String Fecha1 = Anio1+"-"+Mes1+"-"+Dia1;

    try {
        Date FechaVieja=Formato.parse(Fecha1);
        if(FECHAInactividadContrato.getDate()==null){
            FECHAInactividadContrato.setDate(FechaVieja);
        }else{
            FECHAInactividadContrato.getDate();
        }
    } catch (ParseException ex) {
        Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
    }
   
    
    String Campo13 = Formato.format(this.FECHAInactividadContrato.getDate());
    
    try {
      Statement stmt = AC.conn.createStatement();
      stmt.executeUpdate("INSERT INTO Contratos VALUES (" + Campo1 + "," + Campo2 + ","
              + "'" + Campo3 + "'," + Campo4 + ",'" + Campo5 + "','" + Campo6 + "','" + Campo7 + "',"
                      + "" + Campo8 + ",'" + Campo9 + "'," + Campo10 + ",'" + Campo11 + "','" + Campo12 + "',"
                              + "'" + Campo13 + "')");
        //LimpiarTablasAfiliadosyPagos();
      AC.desconectar();
      JOptionPane.showMessageDialog(null, "Se ha Creado un Nuevo Contrato Correctamente :-D");
    } catch (SQLException ex) {
      System.out.println("El Cliente ya Existe Se Procede A Actualizar el Cliente: "+ex);
    } 
  }
  
  private void Editar_Contrato() {
    Access_connection AC = new Access_connection();
    SimpleDateFormat Formato = new SimpleDateFormat("yyyy-MM-dd");
    int Campo1 = Integer.parseInt(this.TXTCodCliente.getText());
    int Campo2 = Integer.parseInt(this.TXTNumContrato.getText());
    String Campo3 = Formato.format(this.FECHAFechaContrato.getDate());
    int Campo4 = this.COMBOCiudadResidencia.getSelectedIndex();
    String Campo5 = this.TXTDireccion.getText();
    String Campo6 = this.COMBOBarrio.getSelectedItem().toString();
    String Campo7 = this.TXTTelefono.getText();
    int Campo8 = this.COMBOFormadePago.getSelectedIndex();
    String Campo9 = this.TXTValordePago.getText();
    int Campo10 = this.COMBOVendedor.getSelectedIndex();
    String Campo11 = this.TXTObservaciones.getText();
    
    if(ContratoActivo.getSelectedItem() == null){
        ContratoActivo.setSelectedIndex(0);
    }
    
    String Campo12 = this.ContratoActivo.getSelectedItem().toString();
    
    String Dia1="01";
    String Mes1="01";
    String Anio1= "1910";

    String Fecha1 = Anio1+"-"+Mes1+"-"+Dia1;

    try {
        Date FechaVieja=Formato.parse(Fecha1);
        if(FECHAInactividadContrato.getDate()==null){
            FECHAInactividadContrato.setDate(FechaVieja);
        }
    } catch (ParseException ex) {
        Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    String Campo13 = Formato.format(this.FECHAInactividadContrato.getDate());
    
      try {
        Statement stmt = AC.conn.createStatement();
        stmt.executeUpdate("UPDATE Contratos SET Num_Contrato= " + Campo2 + ",Fecha_Contrato='" + Campo3 + "', "
                + "Id_Ciudad=" + Campo4 + ",Direccion='" + Campo5 + "',Barrio='" + Campo6 + "',"
                        + "Telefono='" + Campo7 + "',Forma_Pago=" + Campo8 + ",Pago=" + Campo9 + ","
                                + "Id_Vendedor=" + Campo10 + ",Observaciones='" + Campo11 + "',"
                                        + "ContratoActivo='" + Campo12 + "',FechaInactividadContrato='" + Campo13 + "'"
                                                + "WHERE Cod_Cliente=" + Campo1 + "");
        AC.desconectar();
        //LimpiarTablasAfiliadosyPagos();
      } catch (SQLException e) {
        System.out.println("Fallo La Actualizacion del Contrato el error es \n" + e);
        
      } 
   
  }
  
  
  private class CargarAfiliados implements Runnable {
    private CargarAfiliados() {}
    
    public void run() {
        CargarAfiliados();
    }
  }
  
  private class CargarTodosAfiliados implements Runnable {
    private CargarTodosAfiliados() {}
    
    public void run() {
        CargarTodosAfiliados();
    }
  }
  
  private void CargarAfiliados() {
    
        LimpiarCampoAfiliados();
        LimpiarTablasAfiliados();
      
        Access_connection AC = new Access_connection();
        int FilaSeleccionada = this.TABLABusClientes.getSelectedRow();
        String NumContrato = this.TXTCodCliente.getText();
        DefaultTableModel modelo = (DefaultTableModel)this.TABLAAfiliados.getModel();
        try {
          Statement stmt = AC.conn.createStatement();
          ResultSet rs = stmt.executeQuery("SELECT IdAfiliado, Nombres, EdadAfiliado,Parentezco, "
                  + "Fallecido, Activo, Seguro_de_Vida, Fecha_Derechos,TipoUsuario, Traslados, "
                  + "Traslado_a_partir_de, Traslado_de "
                  + "FROM  Afiliados "
                  + "INNER JOIN Parentezcos ON Afiliados.Id_Parentesco=Parentezcos.Id "
                  + "INNER JOIN TipoUsuario ON TipoUsuario.Id = Afiliados.Titular_Beneficiario "
                  + "WHERE NContratovsAfiliados=" + NumContrato + " ORDER BY Titular_Beneficiario ASC ");
          int i = 0;
          while (rs.next()) {
            modelo.addRow(this.rowData);
            NumAfiliado = rs.getInt("IdAfiliado");
            
            Date FechaDeerechos=(rs.getDate("Fecha_Derechos"));
            
            try {
                TABLAAfiliados.setValueAt(this.FormatoFecha2.format(FechaDeerechos), i, 4);
            } catch (Exception e) {
              this.TABLAAfiliados.setValueAt(rs.getString("Fecha_Derechos"), i, 4);
            } 
            
            this.TABLAAfiliados.setValueAt(NumAfiliado, i, 0);
            this.TABLAAfiliados.setValueAt(rs.getString("Nombres"), i, 1);
            this.TABLAAfiliados.setValueAt(rs.getString("EdadAfiliado"), i, 2);
            this.TABLAAfiliados.setValueAt(rs.getString("Parentezco"), i, 3);
            this.TABLAAfiliados.setValueAt(rs.getString("Fallecido"), i, 5);
            this.TABLAAfiliados.setValueAt(rs.getString("Activo"), i, 6);
            this.TABLAAfiliados.setValueAt(rs.getString("Seguro_de_Vida"), i, 7);
            this.TABLAAfiliados.setValueAt(rs.getString("TipoUsuario"), i, 8);
            
            i++;
            
            
            if(rs.getString("TipoUsuario").equals("Titular")){
                FECHADerechosAfiliado2.setText(FormatoFecha2.format(FechaDeerechos));
                FECHATrasladoAfiliados2.setDate((rs.getDate("Traslado_a_partir_de")));
                ContratosTraslados.setText(rs.getString("Traslado_de"));
            }
            
            
          } 
        } catch (SQLException ex) {
          log.warn("Hubo en error al cargar la tabla afiliados por que: "+ex);
        } 
    
    JTableHeader header = TABLAAfiliados.getTableHeader();
    header.setBackground(encabezadostabla);
    header.setForeground(Color.white);
        
        
        Action delete = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                JTable table = (JTable)e.getSource();
                if(TABLAAfiliados.getSelectedRowCount()!=0){
                    NumAfiliado=Long.parseLong(TABLAAfiliados.getValueAt(TABLAAfiliados.getSelectedRow(), 0).toString());
                    NomAfiliado=TABLAAfiliados.getValueAt(TABLAAfiliados.getSelectedRow(), 1).toString();
                     int reply = JOptionPane.showConfirmDialog(null, "No podrá recuperar los datos", "Continuar", JOptionPane.YES_NO_OPTION);
                        if (reply == JOptionPane.YES_OPTION) {
                            //EliminarBeneficiario();
                            (new Thread(new EliminarBeneficiario())).start();
                        }  
                }else{
                    JOptionPane.showMessageDialog(null, "Seleccione una fila primero!");
                }  
                       
            }
        };

        BotonenTablaEliminar buttonColumn = new BotonenTablaEliminar(TABLAAfiliados, delete, 9);
        buttonColumn.setMnemonic(KeyEvent.VK_D);
        
        Action edite = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                JTable table = (JTable)e.getSource();
                if(TABLAAfiliados.getSelectedRowCount()!=0){
                    NumAfiliado=Long.parseLong(TABLAAfiliados.getValueAt(TABLAAfiliados.getSelectedRow(), 0).toString());
                    NomAfiliado=TABLAAfiliados.getValueAt(TABLAAfiliados.getSelectedRow(), 1).toString();
                    System.out.println("------NumAfiliado: "+NumAfiliado);
                    //EditarAfiliados();
                    (new Thread(new EditarAfiliados())).start();
                    Afiliados.setVisible(true);
                    Afiliados.setLocationRelativeTo(null);
                    
                    
                }else{
                    JOptionPane.showMessageDialog(null, "Seleccione una fila primero!");
                }  
            }
        };

        BotonenTablaEditar buttonColumn2 = new BotonenTablaEditar(TABLAAfiliados, edite, 10);
        buttonColumn2.setMnemonic(KeyEvent.VK_D);
        
    TABLAAfiliados.setDefaultRenderer(Object.class, new FormatoTabla());
    TABLAAfiliados.setFocusable(false);
  }
  
  private void CargarTodosAfiliados() {
    
        LimpiarCampoAfiliados();
        LimpiarTablasAfiliados();
      
        Access_connection AC = new Access_connection();
        int FilaSeleccionada = this.TABLABusClientes.getSelectedRow();
        String NumContrato = this.TXTCodCliente.getText();
        DefaultTableModel modelo = (DefaultTableModel)this.TABLAAfiliados.getModel();
        try {
          Statement stmt = AC.conn.createStatement();
          ResultSet rs = stmt.executeQuery("SELECT IdAfiliado, Nombres, EdadAfiliado,Parentezco, "
                  + "Fallecido, Activo, Seguro_de_Vida, Fecha_Derechos,TipoUsuario, Traslados, "
                  + "Traslado_a_partir_de "
                  + "FROM  (Afiliados INNER JOIN Parentezcos ON Afiliados.Id_Parentesco=Parentezcos.Id) "
                  + "INNER JOIN TipoUsuario ON TipoUsuario.Id = Afiliados.Titular_Beneficiario "
                  + "WHERE NContratovsAfiliados=" + NumContrato + " ORDER BY Titular_Beneficiario");
          int i = 0;
          while (rs.next()) {
            modelo.addRow(this.rowData);
            NumAfiliado = rs.getInt("IdAfiliado");
            String NomAfiliado = rs.getString("Nombres");
            String EdadAfiliado = rs.getString("EdadAfiliado");
            String ParAfiliado = rs.getString("Parentezco");
            try {
              this.TABLAAfiliados.setValueAt(this.FormatoFecha2.format(rs.getDate("Fecha_Derechos")), i, 4);
            } catch (Exception e) {
              this.TABLAAfiliados.setValueAt(rs.getString("Fecha_Derechos"), i, 4);
            } 
            String FallAfiliado = rs.getString("Fallecido");
            String ActAfiliado = rs.getString("Activo");
            String SVidaAfiliado = rs.getString("Seguro_de_Vida");
            String TipoUsuario = rs.getString("TipoUsuario");
            this.TABLAAfiliados.setValueAt(NumAfiliado, i, 0);
            this.TABLAAfiliados.setValueAt(NomAfiliado, i, 1);
            this.TABLAAfiliados.setValueAt(EdadAfiliado, i, 2);
            this.TABLAAfiliados.setValueAt(ParAfiliado, i, 3);
            this.TABLAAfiliados.setValueAt(FallAfiliado, i, 5);
            this.TABLAAfiliados.setValueAt(ActAfiliado, i, 6);
            this.TABLAAfiliados.setValueAt(SVidaAfiliado, i, 7);
            this.TABLAAfiliados.setValueAt(TipoUsuario, i, 8);
            i++;
            this.TABLAAfiliados.setDefaultRenderer(Object.class, new FormatoTabla());
            this.TABLAAfiliados.setFocusable(false);
          } 
        } catch (SQLException ex) {
          log.warn("Hubo en error al cargar la tabla afiliados por que: "+ex);
        } 
        try {
          Statement stmt1 = AC.conn.createStatement();
          ResultSet rs1 = stmt1.executeQuery("SELECT Fecha_Derechos, Traslados, Traslado_a_partir_de, Traslado_de FROM Afiliados WHERE NContratovsAfiliados=" + NumContrato + " AND Num_Afiliado = 1");
          while (rs1.next()) {
            try {
              this.FECHADerechosAfiliado2.setText(this.FormatoFecha.format(rs1.getDate("Fecha_Derechos")));
              this.FECHATrasladoAfiliados2.setDate((rs1.getDate("Traslado_a_partir_de")));
            } catch (Exception e) {
              this.FECHADerechosAfiliado2.setText(rs1.getString("Fecha_Derechos"));
              this.FECHATrasladoAfiliados2.setDate(rs1.getDate("Traslado_a_partir_de"));
            } 
            String Tde = rs1.getString("Traslados")+" De: "+rs1.getString("Traslado_de");
            this.ContratosTraslados.setText(Tde);
          } 
        } catch (SQLException exe) {
            log.warn("Algo falló al cargar los encabezados de derechos, traslados en contrato a partir de afiliados: "+exe);
      
    } 
    
    
    JTableHeader header = TABLAAfiliados.getTableHeader();
    header.setBackground(encabezadostabla);
    header.setForeground(Color.white);
        
        
        Action delete = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                JTable table = (JTable)e.getSource();
                if(TABLAAfiliados.getSelectedRowCount()!=0){
                    NumAfiliado=Long.parseLong(TABLAAfiliados.getValueAt(TABLAAfiliados.getSelectedRow(), 0).toString());
                    NomAfiliado=TABLAAfiliados.getValueAt(TABLAAfiliados.getSelectedRow(), 1).toString();
                     int reply = JOptionPane.showConfirmDialog(null, "No podrá recuperar los datos", "Continuar", JOptionPane.YES_NO_OPTION);
                        if (reply == JOptionPane.YES_OPTION) {
                            //EliminarBeneficiario();
                            (new Thread(new EliminarBeneficiario())).start();
                        }  
                }else{
                    JOptionPane.showMessageDialog(null, "Seleccione una fila primero!");
                }  
                       
            }
        };

        BotonenTablaEliminar buttonColumn = new BotonenTablaEliminar(TABLAAfiliados, delete, 9);
        buttonColumn.setMnemonic(KeyEvent.VK_D);
        
        Action edite = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                JTable table = (JTable)e.getSource();
                if(TABLAAfiliados.getSelectedRowCount()!=0){
                    NumAfiliado=Long.parseLong(TABLAAfiliados.getValueAt(TABLAAfiliados.getSelectedRow(), 0).toString());
                    NomAfiliado=TABLAAfiliados.getValueAt(TABLAAfiliados.getSelectedRow(), 1).toString();
                    System.out.println("------NumAfiliado: "+NumAfiliado);
                    //EditarAfiliados();
                    (new Thread(new EditarAfiliados())).start();
                    Afiliados.setVisible(true);
                    Afiliados.setLocationRelativeTo(null);
                    
                    
                }else{
                    JOptionPane.showMessageDialog(null, "Seleccione una fila primero!");
                }  
            }
        };

        BotonenTablaEditar buttonColumn2 = new BotonenTablaEditar(TABLAAfiliados, edite, 10);
        buttonColumn2.setMnemonic(KeyEvent.VK_D);
        
    
  }
  
  
  private class EditarAfiliados implements Runnable {
    private EditarAfiliados() {}
    
    public void run() {
        EditarAfiliados();
    }
  }
  
  private void EditarAfiliados() {
    Access_connection AC = new Access_connection();
    DefaultTableModel modelo = (DefaultTableModel)this.TABLAAfiliados.getModel();
    String NCSeleccionado = this.TXTCodCliente.getText();
    int NumContrato = Integer.parseInt(NCSeleccionado);
    System.out.println(NumContrato);
    int NumFilaSeleccionada = this.TABLAAfiliados.getSelectedRow();
    Object NOrden = this.TABLAAfiliados.getValueAt(NumFilaSeleccionada, 0);
    try {
      Statement stmt = AC.conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT IdAfiliado,Num_Afiliado,Nombres,Cedula, "
              + "EdadAfiliado,Titular_Beneficiario,Id_Parentesco,Traslados,Traslado_de, "
              + "Traslado_a_partir_de, Fallecido,Fecha_Fallecimiento,Activo,FechaActivo,"
              + "Seguro_de_Vida,Valor_Seguro_de_Vida,Destino_Final,Fecha_Derechos,Observaciones "
              + "FROM Afiliados WHERE NContratovsAfiliados=" + NumContrato + " AND IdAfiliado=" + NOrden + "");
      while (rs.next()) {
        String IdAfiliado = rs.getString("IdAfiliado");
        String NContratovsAfiliados = this.TXTCodCliente.getText();
        String NumAfiliado = rs.getString("Num_Afiliado");
        String NomAfiliado = rs.getString("Nombres");
        long cedula = rs.getLong("Cedula");
        String Cedula = Long.toString(cedula);
        String EdadAfiliado = rs.getString("EdadAfiliado");
        int Titular_Beneficiario = rs.getInt("Titular_Beneficiario");
        int ParAfiliado = rs.getInt("Id_Parentesco");
        String Traslados = rs.getString("Traslados");
        String Trasladode = rs.getString("Traslado_de");
        String Fallecido = rs.getString("Fallecido");
        String Activo = rs.getString("Activo");
        String SVida = rs.getString("Seguro_de_Vida");
        String VSeguro = rs.getString("Valor_Seguro_de_Vida");
        String DFinal = rs.getString("Destino_Final");
        String Observaciones = rs.getString("Observaciones");
        if (Access_connection.TipoConexion.equals("Local")) {
          System.out.println("TIPO DE CONEXILOCAL");
          try {
            this.FECHATrasladoAfiliados.setDate(this.FormatoFecha2.parse(rs.getString("Traslado_a_partir_de")));
            this.FECHAActivo.setDate(this.FormatoFecha2.parse(rs.getString("FechaActivo")));
            this.FECHAFallecidoAfiliado.setDate(this.FormatoFecha2.parse(rs.getString("Fecha_Fallecimiento")));
            this.FECHADerechosAfiliado.setDate(this.FormatoFecha2.parse(rs.getString("Fecha_Derechos")));
          } catch (Exception e) {
            System.out.println("No se pudo cargar fechas de Afiliados Locales por que " + e);
          } 
        } else {
          System.out.println("TIPO DE CONEXIINTERNET Fecha Fallecido:" + rs.getDate("Fecha_Fallecimiento"));
          this.FECHATrasladoAfiliados.setDate(rs.getDate("Traslado_a_partir_de"));
          this.FECHAActivo.setDate(rs.getDate("FechaActivo"));
          this.FECHAFallecidoAfiliado.setDate(rs.getDate("Fecha_Fallecimiento"));
          this.FECHADerechosAfiliado.setDate(rs.getDate("Fecha_Derechos"));
        } 
        this.TXTIdAfiliado.setText(IdAfiliado);
        this.TXTCodClienteAfiliado.setText(NContratovsAfiliados);
        //this.TXTNumAfiliado.setText(NumAfiliado);
        this.TXTNombresAfiliado.setText(NomAfiliado);
        this.TXTCedula.setText(Cedula);
        this.TXTEdadAfiliado.setText(EdadAfiliado);
        this.COMBOTipoBeneficiario.setSelectedIndex(Titular_Beneficiario);
        this.COMBOParentescoAfiliado.setSelectedIndex(ParAfiliado);
        this.COMBOTrasladosAfiliado.setSelectedItem(Traslados);
        this.TXTTrasladosAfiliado.setText(Trasladode);
        this.COMBOFallecidoAfiliado.setSelectedItem(Fallecido);
        this.COMBOActivoAfiliado.setSelectedItem(Activo);
        this.COMBOSegurodeVidaAfiliado.setSelectedItem(SVida);
        this.TXTSegurodeVidaAfiliado.setText(VSeguro);
        this.COMBODestinoFinalAfiliado.setSelectedItem(DFinal);
        this.TXTAREAObservaciones.setText(Observaciones);
      } 
      AC.desconectar();
    } catch (SQLException ex) {
      this.Afiliados.dispose();
      JOptionPane.showMessageDialog(null, "Debe Seleccionar un Afiliado Primero " + ex);
      System.out.println(ex);
    } 
  }
  
  private void ValidarAfiliadoExistente(){
      
      System.out.println("El Cod de Afiliado es: "+NumAfiliado);
      if(TXTCedula.getText().equals("") || 
                    TXTEdadAfiliado.getText().equals("") || TXTNombresAfiliado.getText().equals("")){
          JOptionPane.showConfirmDialog(null, "Número de Afiliado, Cédula, Edad, Nombres Son obligatorios.");
          
      }else{
          
          if(NumAfiliado==0){
//              int reply = JOptionPane.showConfirmDialog(null, "Almacenamiento de Datos\n"
//                                  + "Presione 'Aceptar' para Guardar el Afiliado\n", 
//                                          "Guardar Afiliado", JOptionPane.OK_CANCEL_OPTION);
//                  switch (reply) {
//                      case JOptionPane.YES_OPTION:
                          Guardar_Afiliados();
                          LimpiarCampoAfiliados();
                          Afiliados.dispose();
                          CargarAfiliados();
                          
      
//                      case JOptionPane.CLOSED_OPTION:
//                          System.out.println("A cancelado la actualización");
//                          break;
//                      default:
//                          System.out.println("A cancelado la actualización");
//                          break;
//                  }
          }else{
//              int reply = JOptionPane.showConfirmDialog(null, "Actualización de Datos \n"
//                                  + "Presione 'Aceptar' para Actualizar el Viejo Contrato\n", 
//                                              "Actualización de Contrato", JOptionPane.OK_CANCEL_OPTION);
//                switch (reply) {
//                      case JOptionPane.YES_OPTION:
                      Editar_Afiliados();
                      LimpiarCampoAfiliados();
                      Afiliados.dispose();
                      CargarAfiliados();
//                      
//                      case JOptionPane.CLOSED_OPTION:
//                          System.out.println("A cancelado la actualización");
//                          break;
//                      default:
//                          System.out.println("A cancelado la actualización");
//                          break;
//                  }              
          }
        
      }
      NumAfiliado=0;
  }
  
  private void Guardar_Afiliados() {
    Access_connection AC = new Access_connection();
    SimpleDateFormat FormatoFecha = new SimpleDateFormat("yyyy-MM-dd");
    String campo1 = this.TXTCodClienteAfiliado.getText();
    int Campo1 = Integer.parseInt(campo1);
    //String campo2 = this.TXTNumAfiliado.getText();
    //int Campo2 = Integer.parseInt(campo2);
    String Campo3 = this.TXTNombresAfiliado.getText();
    String cedula = this.TXTCedula.getText();
    Long Cedula = Long.parseLong(cedula);
    String campo4 = this.TXTEdadAfiliado.getText();
    int Campo4 = Integer.parseInt(campo4);
    int TBeneficiario = this.COMBOTipoBeneficiario.getSelectedIndex();
    int Campo5 = this.COMBOParentescoAfiliado.getSelectedIndex();
    String Campo6 = this.COMBOTrasladosAfiliado.getSelectedItem().toString();
    String Campo7 = this.TXTTrasladosAfiliado.getText();

    String Dia1="01";
    String Mes1="01";
    String Anio1= "1910";

    String Fecha1 = Anio1+"-"+Mes1+"-"+Dia1;

    try {
        Date FechaVieja=FormatoFecha.parse(Fecha1);
        if(FECHATrasladoAfiliados.getDate()==null){
            FECHATrasladoAfiliados.setDate(FechaVieja);
        }
        
        if(FECHAFallecidoAfiliado.getDate()==null){
            FECHAFallecidoAfiliado.setDate(FechaVieja);
        }
        
        if(FECHAActivo.getDate()==null){
            FECHAActivo.setDate(FechaVieja);
        }
        
        if(FECHADerechosAfiliado.getDate()==null){
            FECHADerechosAfiliado.setDate(FechaVieja);
        }
    } catch (ParseException ex) {
        Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    
    Date ftraslado = this.FECHATrasladoAfiliados.getDate();
    String FTraslado = FormatoFecha.format(ftraslado);
    
    String Campo8 = this.COMBOFallecidoAfiliado.getSelectedItem().toString();
    
    Date campo9 = this.FECHAFallecidoAfiliado.getDate();
    String Campo9 = FormatoFecha.format(campo9);
    
    String Campo10 = this.COMBOActivoAfiliado.getSelectedItem().toString();
    
    Date campo10b = this.FECHAActivo.getDate();
    String Campo10b = FormatoFecha.format(campo10b);
    
    String Campo11 = this.COMBOSegurodeVidaAfiliado.getSelectedItem().toString();
    String Campo12 = this.TXTSegurodeVidaAfiliado.getText();
    String Campo13 = this.COMBODestinoFinalAfiliado.getSelectedItem().toString();
    
    Date campo14 = this.FECHADerechosAfiliado.getDate();
    String Campo14 = FormatoFecha.format(campo14);
    
    System.out.println("Fecha Derechos Afiliado" + Campo14);
    String Campo15 = this.TXTAREAObservaciones.getText();
    try {
      Statement stmt = AC.conn.createStatement();
      stmt.executeUpdate("INSERT INTO Afiliados VALUES (null," + Campo1 + "," + 0 + ",'" + Campo3 + "',"
              + "" + Cedula + "," + Campo4 + "," + TBeneficiario + "," + Campo5 + ",'" + Campo6 + "',"
                      + "'" + Campo7 + "','" + FTraslado + "','" + Campo8 + "','" + Campo9 + "','" + Campo10 + "',"
                              + "'" + Campo10b + "','" + Campo11 + "','" + Campo12 + "','" + Campo13 + "','" + Campo14 + "'"
                                      + ",'" + Campo15 + "')");
      
    } catch (SQLException ex) {
      System.out.println("No se puede guardar Afiliado: "+ex);
    } 
    
    
    AC.desconectar();
  }
  
  private void Editar_Afiliados() {
    Access_connection AC = new Access_connection();
    SimpleDateFormat FormatoFecha = new SimpleDateFormat("yyyy-MM-dd");
    String campo1 = this.TXTCodClienteAfiliado.getText();
    int Campo1 = Integer.parseInt(campo1);
    //String campo2 = this.TXTNumAfiliado.getText();
    //int Campo2 = Integer.parseInt(campo2);
    String Campo3 = this.TXTNombresAfiliado.getText();
    String cedula = this.TXTCedula.getText();
    long Cedula = Long.parseLong(cedula);
    String campo4 = this.TXTEdadAfiliado.getText();
    int Campo4 = Integer.parseInt(campo4);
    int TBeneficiario = this.COMBOTipoBeneficiario.getSelectedIndex();
    int Campo5 = this.COMBOParentescoAfiliado.getSelectedIndex();
    String Campo6 = this.COMBOTrasladosAfiliado.getSelectedItem().toString();
    String Campo7 = this.TXTTrasladosAfiliado.getText();

    String Dia1="01";
    String Mes1="01";
    String Anio1= "1910";

    String Fecha1 = Anio1+"-"+Mes1+"-"+Dia1;

    try {
        Date FechaVieja=FormatoFecha.parse(Fecha1);
        if(FECHATrasladoAfiliados.getDate()==null){
            FECHATrasladoAfiliados.setDate(FechaVieja);
        }
        
        if(FECHAFallecidoAfiliado.getDate()==null){
            FECHAFallecidoAfiliado.setDate(FechaVieja);
        }
        
        if(FECHAActivo.getDate()==null){
            FECHAActivo.setDate(FechaVieja);
        }
        
        if(FECHADerechosAfiliado.getDate()==null){
            FECHADerechosAfiliado.setDate(FechaVieja);
        }
    } catch (ParseException ex) {
        Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    
    Date ftraslado = this.FECHATrasladoAfiliados.getDate();
    String FTraslado = FormatoFecha.format(ftraslado);
    
    String Campo8 = this.COMBOFallecidoAfiliado.getSelectedItem().toString();
    
    Date campo9 = this.FECHAFallecidoAfiliado.getDate();
    String Campo9 = FormatoFecha.format(campo9);
    
    String Campo10 = this.COMBOActivoAfiliado.getSelectedItem().toString();
    
    Date campo10b = this.FECHAActivo.getDate();
    String Campo10b = FormatoFecha.format(campo10b);
    
    String Campo11 = this.COMBOSegurodeVidaAfiliado.getSelectedItem().toString();
    String Campo12 = this.TXTSegurodeVidaAfiliado.getText();
    String Campo13 = this.COMBODestinoFinalAfiliado.getSelectedItem().toString();
    
    Date campo14 = this.FECHADerechosAfiliado.getDate();
    String Campo14 = FormatoFecha.format(campo14);
    
    System.out.println("Fecha Derechos Afiliado" + Campo14);
    String Campo15 = this.TXTAREAObservaciones.getText();
    
    System.out.println("El Afiliado ya Existe Se Procede A Actualizar el Cliente ");
    try {
      Statement stmt = AC.conn.createStatement();
      stmt.executeUpdate("UPDATE Afiliados SET Nombres='" + Campo3 + "',Cedula=" + Cedula + ","
              + "EdadAfiliado=" + Campo4 + ",Titular_Beneficiario=" + TBeneficiario + ","
                      + "Id_Parentesco=" + Campo5 + ",Traslados='" + Campo6 + "',"
                              + "Traslado_de='" + Campo7 + "',Traslado_a_partir_de='" + FTraslado + "',"
                                      + "Fallecido='" + Campo8 + "',Fecha_Fallecimiento='" + Campo9 + "',"
                                              + "Activo='" + Campo10 + "',FechaActivo='" + Campo10b + "',"
                                                      + "Seguro_de_Vida='" + Campo11 + "',"
                                                      + "Valor_Seguro_de_Vida='" + Campo12 + "',"
                                                            + "Destino_Final='" + Campo13 + "',"
                                                           + "Fecha_Derechos='" + Campo14 + "',"
                                                                  + "Observaciones='" + Campo15 + "'"
                                                              + " WHERE IdAfiliado=" + NumAfiliado + "");
      
      
      
    } catch (SQLException e) {
      System.out.println("No se puede Editar Afiliado: "+e);
    } 
    
    NumAfiliado=0;
    
    AC.desconectar();
  }
  
   private class EliminarBeneficiario implements Runnable {
    private EliminarBeneficiario() {}
    
    public void run() {
      EliminarBeneficiario();
    }
  }
   
  private void EliminarBeneficiario() {
    Access_connection AC = new Access_connection();
    
    try {
      Statement stmt = AC.conn.createStatement();
      stmt.executeUpdate("DELETE FROM Afiliados WHERE IdAfiliado=" + NumAfiliado + "");
      JOptionPane.showMessageDialog(null, "El Beneficiario: " + NomAfiliado + "  se eliminó del sistema." );
      LimpiarCampoAfiliados();
      CargarAfiliados();
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, "No se pudo eliminar el Cliente por que: " + e);
    } 
    AC.desconectar();
  }
  
  private void LimpiarCampoAfiliados() {
    SimpleDateFormat Formato = new SimpleDateFormat("dd/MM/yyyy");
    String FDefault = "01/01/1000";
    this.COMBOActivoAfiliado.setSelectedIndex(0);
    this.COMBODestinoFinalAfiliado.setSelectedIndex(0);
    this.COMBOFallecidoAfiliado.setSelectedIndex(0);
    this.COMBOParentescoAfiliado.setSelectedIndex(0);
    this.COMBOSegurodeVidaAfiliado.setSelectedIndex(0);
    this.COMBOTrasladosAfiliado.setSelectedIndex(0);
    this.TXTCedula.setText("");
    this.COMBOTipoBeneficiario.setSelectedIndex(0);
    //this.TXTNumAfiliado.setText("");
    this.TXTCodClienteAfiliado.setText("");
    this.TXTNombresAfiliado.setText("");
    this.TXTSegurodeVidaAfiliado.setText("");
    this.TXTTrasladosAfiliado.setText("");
    this.TXTEdadAfiliado.setText("");
    TXTAREAObservaciones.setText("");
    try {
      Date FPredefinida = Formato.parse(FDefault);
      this.FECHADerechosAfiliado.setDate(FPredefinida);
      this.FECHAFallecidoAfiliado.setDate(FPredefinida);
      this.FECHATrasladoAfiliados2.setDate(FPredefinida);
    } catch (ParseException ex) {
      log.warn("Hubo en error al Limpiar Campos de afiliados por que: "+ex);
    } 
  }
  
  private void LimpiarCampoContratos(){
      TXTNumContrato.setText("0");
      TXTCodCliente.setText("0");
      COMBOFormadePago.setSelectedIndex(0);
      FECHAFechaContrato.setCalendar(null);
      COMBOVendedor.setSelectedIndex(0);
      COMBOCiudadResidencia.setSelectedIndex(0);
      TXTTelefono.setText("0");
      TXTDireccion.setText("");
      COMBOBarrio.setSelectedIndex(0);
      ContratoActivo.setSelectedIndex(0);
      FECHAInactividadContrato.setCalendar(null);
      FECHADerechosAfiliado2.setText("");
      ContratosTraslados.setText("");
      FECHATrasladoAfiliados2.setCalendar(null);
      TXTValordePago.setText("0");
      TXTObservaciones.setText("");
      
      
  }
  
  private void CrearNuevoContrato(){
        LimpiarCampoContratos();
        LimpiarCampoAfiliados();
        LimpiarTablasAfiliados();
        LimpiarTablasPagos();
  }
  //Pagos
  private void GuardarnuevovalordePagos() {
    String CodCliente = this.TXTCodCliente.getText();
    if (this.TXTCodCliente.getText().equals("")) {
      JOptionPane.showMessageDialog(null, "Debe Buscar un Cliente para poder modificar el valor de pago");
    } else {
      int NuevoValor = Integer.parseInt(JOptionPane.showInputDialog("Digite el nuevo valor a pagar del Cliente: " + CodCliente));
      Access_connection AC = new Access_connection();
      SimpleDateFormat Formato = new SimpleDateFormat("yyyy-MM-dd");
      try {
        Statement stmt = AC.conn.createStatement();
        stmt.executeUpdate("UPDATE Contratos SET Pago=" + NuevoValor + " WHERE Cod_Cliente=" + CodCliente + "");
        AC.desconectar();
        JOptionPane.showMessageDialog(null, "Se ha guardado un nuevo Valor de Pago");
        this.TXTValorPagar.setText("");
        //this.TXTEfectivo.setText("");
        this.TXTValorPagar.setText(Integer.toString(NuevoValor));
        //this.TXTEfectivo.setText(Integer.toString(NuevoValor));
      } catch (SQLException ex) {
        log.warn("Hubo en error al X por que: "+ex);
      } 
    } 
  }
  
  private class GuardarPago implements Runnable {
    private GuardarPago() {}
    
    public void run() {
      if (FECHARegistroPago.getDate() == null || TXTCuotasNulas.getText().equals("") || FECHAPagoDesde.getDate() == null || 
              FECHAPagoHasta.getDate() == null || TXTValorPagar.getText().equals("")) {
        JOptionPane.showMessageDialog(null, "Verifique Que los Campos:\n - Fecha Registro de Pago\n - "
                + "Cuotas Nulas\n - Fecha Pago Desde\n - Fecha Pago Hasta\n - Valor de Pago estcompletamente DILIGENCIADOS. :-|\n "
                + "Recuerde que las fechas son Obligatorias\nSe procede a Resaltar todos los campos Obligatorios ;-)");
        
      } else {
        BTNRegistrarPago.requestFocus();
        GuardarPago();
        CargarPagos();
        //new Thread(new HiloGuardarFechadeUltimoPago()).start();
        //new Thread(new HiloGuardarPago()).start();
        
      } 
    }
  }
  
  private class HiloGuardarFechadeUltimoPago implements Runnable {
    private HiloGuardarFechadeUltimoPago() {}
    
    public void run() {
        
        GuardarFechadeUltimoPago();
      
    }
  }
  
  private class VerificarVersion implements Runnable {
    private VerificarVersion() {}
    
    public void run() {
        
        VerificarVersion();
      
    }
  }
  
  public void VerificarVersion() {
        Access_connection AC = new Access_connection();
        Acercade Acerca=new Acercade();
        LABELVersion1.setText(Acerca.Version.getText());

        try {
            Statement stmt = AC.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Version "
                    + "FROM App_Version");
            while (rs.next()) {
                log.info("VersionLocal: " + LABELVersion1.getText() + " VersionActual: " + rs.getString("Version"));
                if (LABELVersion1.getText().equals(rs.getString("Version"))) {
                    //Notification.show("Verificación", "Está usando la última versión de ICO App " + jMenuItem6.getText(), Notification.NICON_LIGHT_THEME);
                    log.info("Tiene la última versión instalada");

                } else {
                    int reply = JOptionPane.showConfirmDialog(null, "Una nueva versión de la APP está disponible, \n"
                            + "Tendrá que ejecutar la instalador al finalizar la descarga.\n"
                            + "Presione 'Aceptar' para comenzar la actualización "
                            + "(Esto abrirá el navegador y descargará el paquete de actualización)\n"
                            + "Presione 'Cancelar' para posponer.", "Actualización Disponible", JOptionPane.OK_CANCEL_OPTION);
                    switch (reply) {
                        case JOptionPane.YES_OPTION:
                            new Thread(new ProcesoDescargaNuevoVersion()).start();        // TODO add your handling code here:
                            log.info("La actualización se está descargando");
                        case JOptionPane.CLOSED_OPTION:
                            log.info("A cancelado la actualización");
                            break;
                        default:
                            log.info("A cancelado la actualización");
                            break;
                    }
                    
                }

            }

            log.info("Cargado ICO_Datos_Organizacion");
            AC.desconectar();
        } catch (Exception e) {
            log.info("No Cargado ICO_Datos_Organizacion" + e);
        }

    }
  
  
  public void ProcesoDescargaNuevoVersion(){
        
        VentanaProgresoDescargaNuevaVersion.setVisible(true);
        VentanaProgresoDescargaNuevaVersion.setLocationRelativeTo(null);
        jLabel93.setEnabled(false);
        jLabel92.setText("Descargando...");
        
        
        try {
            
            String url="";
            String filename="";
            String so = System.getProperty("os.name");
            if (so.contains("Linux")) {
                url = ("https://github.com/soycamiloypunto/Insys/releases/latest/download/Insys_Installer.tar.gz");
                filename=(HOME + File.separator +"Insys_Installer.tar.gz");
            } else if (so.contains("Windows")) {
                url = ("https://github.com/soycamiloypunto/Insys/releases/latest/download/Insys_Installer.exe");
                filename=(HOME + File.separator +"Insys_Installer.exe");
            } else if (so.contains("Mac")) {
                url = ("https://github.com/soycamiloypunto/Insys/releases/latest/download/Insys_Installer.pkg");
                filename=(HOME + File.separator +"Insys_Installer.pkg");
            }
                
            Ruta=filename;
            System.out.println("Sistema Operativo: "+so);
            
            try {

                    URL urll = new URL(url);
                    HttpURLConnection httpConnection = (HttpURLConnection) (urll.openConnection());
                    long completeFileSize = httpConnection.getContentLength();

                    java.io.BufferedInputStream in = new java.io.BufferedInputStream(httpConnection.getInputStream());
                    java.io.FileOutputStream fos = new java.io.FileOutputStream(filename);
                    java.io.BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
                    
                    byte[] data = new byte[1024];
                    long downloadedFileSize = 0;
                    int x = 0;
                    while ((x = in.read(data, 0, 1024)) >= 0) {
                        downloadedFileSize += x;

                        // calculate progress
                        final int currentProgress = (int) ((((double)downloadedFileSize) / ((double)completeFileSize)) * 100d);

                        // update progress bar
                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                jProgressBar1.setValue(currentProgress);
                            }
                        });

                        bout.write(data, 0, x);
                    }
                    bout.close();
                    in.close();
                } catch (FileNotFoundException e) {
                } catch (IOException e) {
                }
            
        } catch (Exception e) {
        }
        
        
        jLabel93.setEnabled(true);
        jLabel92.setText("Progreso de Descarga");
        
        int reply = JOptionPane.showConfirmDialog(null, "La Actualización ha sido descargada."
                            + "Presione 'Aceptar' para comenzar la actualización (Esto cerrará la aplicación por completo y abrirá el asistente de instalación)\n"
                            + "Presione 'Cancelar' para posponer.", "Actualización Disponible", JOptionPane.OK_CANCEL_OPTION);
        switch (reply) {
            case JOptionPane.YES_OPTION:
                AbrirAdministradordeArchivos();
                System.exit(0);
            case JOptionPane.CLOSED_OPTION:
                log.info("A cancelado la actualización");
                break;
            default:
                log.info("A cancelado la actualización");
                break;
        }

        
    }
  
  private void AbrirAdministradordeArchivos(){
        
        String so = System.getProperty("os.name");
        System.out.println("Sistema Operativo: "+so);
        System.out.println("Ruta de Descarga: "+Ruta+"");
        
        if(so.contains("Linux")){
            try {
                Runtime runtime = Runtime.getRuntime();
                Process process = runtime.exec("xdg-open "+Ruta+"");
            } catch (IOException ex) {
                Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else if(so.contains("Windows")){
            try {
                    Runtime runtime = Runtime.getRuntime();
                    Process process = runtime.exec("Explorer "+Ruta+"");
            } catch (IOException exe) {
                Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, exe);
            }
        }else if(so.contains("Mac")){
            try {
                Runtime runtime = Runtime.getRuntime();
                Process process = runtime.exec("open "+Ruta+"");
            } catch (IOException ex) {
                Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
  
  private class HiloBajarNuevaVersion implements Runnable {
    private HiloBajarNuevaVersion() {}
    
    public void run() {
        
        VerificarVersion();
      
    }
  }
  
  
  private class ProcesoDescargaNuevoVersion implements Runnable {
    private ProcesoDescargaNuevoVersion() {}
    
    public void run() {
        
        ProcesoDescargaNuevoVersion();
      
    }
  }
  
  
  
  
   private class HiloGuardarPago implements Runnable {
    private HiloGuardarPago() {}
    
    public void run() {
        
        GuardarPago();
        CargarPagos();
      
    }
  }
  private class HiloCargarPagos implements Runnable {
    private HiloCargarPagos() {}
    
    public void run() {
        
        CargarPagos();
      
    }
  }
  
  
  private class EliminarPago implements Runnable {
    private EliminarPago() {}
    
    public void run() {
      int dialogResult = JOptionPane.showConfirmDialog(null, "seguro que quiere Eliminar este pago? \n "
              + "Recuerde que si elimina este registro no podrdeshacer el Cambio", "Peligro", 0);
      if (dialogResult == 0) {
        EliminarPago();
        CargarPagos();
        //CargarNumCodigoPago();
        //GuardarUltimaFechadeUltimoPago();
        //new Thread(new HiloCargarPagos()).start();
      } else {
        System.out.println("Se ha cancelado la Acción");
      } 
    }
  }
  
  public void CargarNumCodigoPago() {
    Access_connection AC = new Access_connection();
    try {
      Statement stmtPago = AC.conn.createStatement();
      ResultSet rsPago = stmtPago.executeQuery("SELECT MAX(CodPago) as totalcuotas from Pagos");
      while (rsPago.next()) {
        String TCuotas = rsPago.getString("totalcuotas");
        int Totalcuotas = Integer.parseInt(TCuotas);
        int TotalC = Totalcuotas + 1;
        String TotalCuotas = Integer.toString(TotalC);
        this.TXTNumPago.setText(TotalCuotas);
      } 
      AC.desconectar();
    } catch (SQLException ex) {
      log.warn("Hubo en error al X por que: "+ex);
    } 
  }
  
  public void GuardarPago() {
    Access_connection AC = new Access_connection();
    
    String Campo2 = Formato.format(this.FECHARegistroPago.getDate());
    int DesdeDia = this.FECHARegistroPago.getCalendar().get(5);
    int DesdeMes = this.FECHARegistroPago.getCalendar().get(2);
    int DesdeAno = this.FECHARegistroPago.getCalendar().get(1);
    Long Campo3 = Long.parseLong(this.TXTCodCliente.getText());
    String Campo4 = this.COMBOCobrador.getSelectedItem().toString();
    int Campo5 = Integer.parseInt(this.TXTCuotasNulas.getText());
    String Campo6 = Formato.format(this.FECHAPagoDesde.getDate());
    String Campo7 = Formato.format(this.FECHAPagoHasta.getDate());
    int Campo8 = Integer.parseInt(this.TXTValorPagar.getText());
    try {
      Statement stmt = AC.conn.createStatement();
      stmt.executeUpdate("INSERT INTO Pagos VALUES (null,'" + Campo2 + "'," + Campo3 + ",'" + Campo4 + "',"
              + "" + Campo5 + ",'" + Campo6 + "','" + Campo7 + "'," + Campo8 + "," + DesdeDia + ","
                      + "" + DesdeMes + "," + DesdeAno + ")");
      
      
    } catch (SQLException ex) {
      log.warn("Hubo en error al X por que: "+ex);
      JOptionPane.showMessageDialog(null, "Falló Guardar el Registro de Pago. Verifique:  \n - Revise que todos los campos estCompletamente Diligenciados\n- Verifique que el Nde Pago sea y no haya registro con ese C\nEn Caso que haya un registro repetido, elimine el registro e intentelo de nuevo");
    } 
    
    AC.desconectar();
    TXTValorPagar.requestFocus();
  }
  
  private void GuardarFechadeUltimoPago() {
    Access_connection AC = new Access_connection();
    long CodCliente = Long.parseLong(this.TXTCodCliente.getText());
    String FechaUltimoPago = FormatoFecha.format(this.FECHAPagoHasta.getDate());
    try {
      Statement stmt = AC.conn.createStatement();
      stmt.executeUpdate("INSERT INTO Contratos_FechaUltimoPago VALUES (" + CodCliente + ", '" + FechaUltimoPago + "')");
      AC.desconectar();
      System.out.println("Se ha Ingresado un Nuevo Cliente que ha pagado");
    } catch (SQLException ex) {
      System.out.println("El Cliente ya ha pagado se procede a actualizar fecha de Pago");
      try {
        Statement stmt = AC.conn.createStatement();
        stmt.executeUpdate("UPDATE Contratos_FechaUltimoPago SET FechaUltimoPago='" + FechaUltimoPago + "' WHERE CodContratovsFechaUltimoPago=" + CodCliente + "");
        AC.desconectar();
        System.out.println("Se ha actualizado el Campo Fecha Ultimo Pago");
      } catch (Exception e) {
        System.out.println("Algo fallal actualizar la fecha del ultimo pago");
        Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, (String)null, e);
      } 
    } 
  }
  
  public void LimpiarTablasPagos() {
    DefaultTableModel modeloPagos = (DefaultTableModel)this.TABLAPagos.getModel();
    
    int NumFilasPagos = modeloPagos.getRowCount();
    for (int j = 0; NumFilasPagos > j; j++)
      modeloPagos.removeRow(0); 
  }
  
  public void CargarPagos() {
    Access_connection AC = new Access_connection();
    DefaultTableModel modelo = (DefaultTableModel)this.TABLAPagos.getModel();
    LimpiarTablasPagos();
    int NCSeleccionado = Integer.parseInt(this.TXTCodCliente.getText());
    
    try {
      
      Statement stmtPago = AC.conn.createStatement();
      ResultSet rsPago = stmtPago.executeQuery("SELECT CodPago,FRegistro, Depositante,Cuotas_Nulas,Desde,"
              + "Hasta,Valor_a_Pagar "
              + "FROM Pagos WHERE CodContratovsPagos= " + NCSeleccionado + " "
               + " ORDER BY CodPago DESC LIMIT 5 ");
      int j = 0;
      while (rsPago.next()) {
        modelo.addRow(this.rowData);
        
        this.TABLAPagos.setValueAt(rsPago.getString("CodPago"), j, 0);
        this.TABLAPagos.setValueAt(formatoDeFecha.format(rsPago.getDate("FRegistro")), j, 1);
        this.TABLAPagos.setValueAt(rsPago.getString("Depositante"), j, 2);
        this.TABLAPagos.setValueAt(rsPago.getString("Cuotas_Nulas"), j, 3);
        this.TABLAPagos.setValueAt(formatoDeFecha.format(rsPago.getDate("Desde")), j, 4);
        this.TABLAPagos.setValueAt(formatoDeFecha.format(rsPago.getDate("Hasta")), j, 5);
        this.TABLAPagos.setValueAt(rsPago.getString("Valor_a_Pagar"), j, 6);
        
        if(j==0){
            FECHAPagoDesde.setDate((rsPago.getDate("Hasta")));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((rsPago.getDate("Hasta")));
            calendar.add(Calendar.MONTH, 1);
            FECHAPagoHasta.setDate(calendar.getTime());
            
        }
        
        j++;
        
      } 
    } catch (SQLException ex) {
      Log.warn("No se pudo cargar Pagos: "+ex);
    } 
    
    JTableHeader header = TABLAPagos.getTableHeader();
    header.setBackground(encabezadostabla);
    header.setForeground(Color.white);
        
        
        Action delete = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                JTable table = (JTable)e.getSource();
                if(TABLAPagos.getSelectedRowCount()!=0){
                    NumPago=Integer.parseInt(TABLAPagos.getValueAt(TABLAPagos.getSelectedRow(), 0).toString());
                     int reply = JOptionPane.showConfirmDialog(null, "No podrá recuperar los datos", "Continuar", JOptionPane.YES_NO_OPTION);
                        if (reply == JOptionPane.YES_OPTION) {
                            //EliminarBeneficiario();
                            (new Thread(new EliminarPago())).start();
                        }  
                }else{
                    JOptionPane.showMessageDialog(null, "Seleccione una fila primero!");
                }  
                       
            }
        };

        BotonenTablaEliminar buttonColumn = new BotonenTablaEliminar(TABLAPagos, delete, 7);
        buttonColumn.setMnemonic(KeyEvent.VK_D);
        
        OcultarTotalesPagos();
        
    
  }
  
  public void OcultarTotalesPagos(){
      jLabel1.setVisible(false);
      TXTNumPago.setVisible(false);
      jLabel31.setVisible(false);
      TXTTotalPagos.setVisible(false);
      
      
  }
  
   public void MostrarTotalesPagos(){
      jLabel1.setVisible(true);
      TXTNumPago.setVisible(true);
      jLabel31.setVisible(true);
      TXTTotalPagos.setVisible(true);
      
      
  }
  
  public void CargarTodosPagos() {
    Access_connection AC = new Access_connection();
    LimpiarTablasPagos();
    int NCSeleccionado = Integer.parseInt(this.TXTCodCliente.getText());
    try {
      DefaultTableModel modelo = (DefaultTableModel)this.TABLAPagos.getModel();
      SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MMM/yyyy");
      int NumFilas = modelo.getRowCount();
      for (int i = 0; NumFilas > i; i++)
        modelo.removeRow(0); 
      Statement stmtPago = AC.conn.createStatement();
      ResultSet rsPago = stmtPago.executeQuery("SELECT CodPago,FRegistro, Depositante,Cuotas_Nulas,Desde,"
              + "Hasta,Valor_a_Pagar "
              + "FROM Pagos WHERE CodContratovsPagos= " + NCSeleccionado + " "
               + " ORDER BY CodPago");
      int j = 0;
      while (rsPago.next()) {
        modelo.addRow(this.rowData);
        String CodPago = rsPago.getString("CodPago");
        Date fregistro = rsPago.getDate("FRegistro");
        String FRegistro = formatoDeFecha.format(fregistro);
        String Depositante = rsPago.getString("Depositante");
        String Cuotas_Nulas = rsPago.getString("Cuotas_Nulas");
        Date desde = rsPago.getDate("Desde");
        String Desde = formatoDeFecha.format(desde);
        Date hasta = rsPago.getDate("Hasta");
        String Hasta = formatoDeFecha.format(hasta);
        String Valor_a_Pagar = rsPago.getString("Valor_a_Pagar");
        this.TABLAPagos.setValueAt(CodPago, j, 0);
        this.TABLAPagos.setValueAt(FRegistro, j, 1);
        this.TABLAPagos.setValueAt(Depositante, j, 2);
        this.TABLAPagos.setValueAt(Cuotas_Nulas, j, 3);
        this.TABLAPagos.setValueAt(Desde, j, 4);
        this.TABLAPagos.setValueAt(Hasta, j, 5);
        this.TABLAPagos.setValueAt(Valor_a_Pagar, j, 6);
        j++;
      } 
    } catch (SQLException ex) {
      Log.warn("No se pudo cargar Pagos: "+ex);
    } 
    try {
      Statement stmtPago = AC.conn.createStatement();
      ResultSet rsPago = stmtPago.executeQuery("SELECT count(CodPago) as totalcuotas, sum(Valor_a_Pagar) as suma from Pagos WHERE CodContratovsPagos=" + NCSeleccionado + "");
      while (rsPago.next()) {
        String TotalCuotas = rsPago.getString("totalcuotas");
        long Suma = rsPago.getLong("suma");
        String SumaPagos = Long.toString(Suma);
        this.TXTNumPago.setText(TotalCuotas);
        this.TXTTotalPagos.setText(SumaPagos);
      } 
    } catch (SQLException ex) {
      Log.warn("No se pudo obtener los totales de pagos y cuotas: "+ex);
    } 
    this.TXTCuotasNulas.setText("0");
    try {
      int Dias = 30;
      Statement stmfecha = AC.conn.createStatement();
      ResultSet rsFecha = stmfecha.executeQuery("SELECT MAX(Hasta) as FechaHasta from Pagos WHERE CodContratovsPagos= " + NCSeleccionado + "");
      while (rsFecha.next()) {
        Date FechaDesde = rsFecha.getDate("FechaHasta");
        System.out.println(FechaDesde);
        this.FECHAPagoDesde.setDate(FechaDesde);
        Calendar cal = new GregorianCalendar();
        cal.setLenient(false);
        cal.setTime(FechaDesde);
        cal.add(2, 1);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");
        String FechaH = formatter.format(cal.getTime());
        try {
          Date FechaHasta = formatoDeFecha.parse(FechaH);
          System.out.println(FechaHasta);
          this.FECHAPagoHasta.setDate(FechaHasta);
        } catch (ParseException ex) {
            log.warn("No se pudo Cargar Fechas de Pagos por que: "+ex);
        } 
      } 
      AC.desconectar();
    } catch (SQLException ex) {
      log.warn("No se pudo Cargar Encabezado de pagos por que: "+ex);
    } 
    
    JTableHeader header = TABLAPagos.getTableHeader();
    header.setBackground(encabezadostabla);
    header.setForeground(Color.white);
        
        
        Action delete = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                JTable table = (JTable)e.getSource();
                if(TABLAPagos.getSelectedRowCount()!=0){
                    NumPago=Integer.parseInt(TABLAPagos.getValueAt(TABLAPagos.getSelectedRow(), 0).toString());
                     int reply = JOptionPane.showConfirmDialog(null, "No podrá recuperar los datos", "Continuar", JOptionPane.YES_NO_OPTION);
                        if (reply == JOptionPane.YES_OPTION) {
                            //EliminarBeneficiario();
                            (new Thread(new EliminarPago())).start();
                        }  
                }else{
                    JOptionPane.showMessageDialog(null, "Seleccione una fila primero!");
                }  
                       
            }
        };

        BotonenTablaEliminar buttonColumn = new BotonenTablaEliminar(TABLAPagos, delete, 7);
        buttonColumn.setMnemonic(KeyEvent.VK_D);
        
        
        MostrarTotalesPagos();
    
  }
  
  
  private void GuardarUltimaFechadeUltimoPago() {
    Access_connection AC = new Access_connection();
    SimpleDateFormat Formato = new SimpleDateFormat("yyyy/MM/dd");
    long CodCliente = Long.parseLong(this.TXTCodCliente.getText());
    String FechaUltimoPago = Formato.format(this.FECHAPagoDesde.getDate());
    try {
      Statement stmt = AC.conn.createStatement();
      stmt.executeUpdate("INSERT INTO Contratos_FechaUltimoPago VALUES (" + CodCliente + ", '" + FechaUltimoPago + "')");
      AC.desconectar();
      System.out.println("Se ha Ingresado un Nuevo Cliente que ha pagado");
    } catch (SQLException ex) {
      System.out.println("El Cliente ya ha pagado se procede a actualizar fecha de Pago");
      try {
        Statement stmt = AC.conn.createStatement();
        stmt.executeUpdate("UPDATE Contratos_FechaUltimoPago SET FechaUltimoPago='" + FechaUltimoPago + "' WHERE CodContratovsFechaUltimoPago=" + CodCliente + "");
        AC.desconectar();
        System.out.println("Se ha actualizado el Campo Fecha Ultimo Pago");
      } catch (Exception e) {
        System.out.println("Algo fallal actualizar la fecha del ultimo pago");
        Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, (String)null, e);
      } 
    } 
  }
  
  public void EliminarPago() {
    Access_connection AC = new Access_connection();
    if ((this.TABLAPagos.getSelectedRows()).length > 0) {
      int FilaSeleccionada = this.TABLAPagos.getSelectedRow();
      String NPago = this.TABLAPagos.getValueAt(FilaSeleccionada, 0).toString();
      NumPago = Integer.parseInt(NPago);
      try {
        Statement stmt = AC.conn.createStatement();
        stmt.executeUpdate("DELETE FROM Pagos WHERE CodPago = " + NumPago + "");
        AC.desconectar();
//        try {
//          Thread.sleep(100L);
//          //CargarCliente2();
//        } catch (InterruptedException ex) {
//          Logger.getLogger(Pagos.class.getName()).log(Level.SEVERE, (String)null, ex);
//        } 
      } catch (SQLException ex) {
        log.warn("Hubo en error al X por que: "+ex);
        JOptionPane.showMessageDialog(null, "No se LogrBorrar el Pago Seleccionado");
      } 
    } else {
      JOptionPane.showMessageDialog(null, "Primero Seleccione un Vendedor antes de Editar");
    } 
  }
  
  private class CargarPagos implements Runnable {
    private CargarPagos() {}
    
    public void run() {
        CargarPagos();
        TXTValorPagar.requestFocus();
    }
  }
  
  private class CargarTodosPagos implements Runnable {
    private CargarTodosPagos() {}
    
    public void run() {
        CargarTodosPagos();
        TXTValorPagar.requestFocus();
    }
  }
  
  
  public void CargarDesplegableCobradores() {
    this.COMBOCobrador.removeAllItems();
    Access_connection AC = new Access_connection();
    try {
      Statement stmt = AC.conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT Nombres_Cobrador FROM Cobradores ORDER BY Nombres_Cobrador ASC");
      int i = 0;
      while (rs.next()) {
        String Nombres_Cobrador = rs.getString("Nombres_Cobrador");
        this.COMBOCobrador.addItem(Nombres_Cobrador);
        i++;
      } 
      AC.desconectar();
    } catch (SQLException ex) {
      
      log.warn("Error COnsulta Cargar Cobradores "+ex);
    } 
  }
  
  
  public void PresionarBuscar(){
        Busqueda.setVisible(true);
        Busqueda.setLocationRelativeTo(null);  
      
        String Mayuscula = this.TXTBusqueda.getText().toUpperCase();
        this.TXTBusqueda.setText(Mayuscula);
        (new Thread(new FiltrarBusqueda())).start();      
      
  }
  
  private class Cargar_OrdendeServicio implements Runnable {
    private Cargar_OrdendeServicio() {}
    
    public void run() {
        Cargar_OrdendeServicio();
    }
  }
  
  
  public void Cargar_OrdendeServicio(){
      Ordenes.setVisible(true);
      Ordenes.setLocationRelativeTo(null);
      TXTCodAfiliado.setText(String.valueOf(NumAfiliado));
      
      Access_connection AC=new Access_connection();
      
        try {
            Statement stmt = AC.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT CodigoAfiliado, LugarD, Direccion, TMuerte, "
                    + "Velacion, TablasN, Iglesia, FechaH, Observaciones, Etapas, Traslado, TrasladoDe, "
                    + "TrasladoA, Cremacion, RealizaTraslado, Conductor, Tanatologo, Cementerio, "
                    + "CementerioA, Tipo, Ciudad, Numero, Cortejo, Arreglo, CafeteriaD, CafeteriaN "
                    + "FROM Servicios WHERE CodigoAfiliado="+NumAfiliado+"");
            while (rs.next()) {
                
                TXTCodAfiliado.setText(String.valueOf(NumAfiliado));
                LDefuncion.setText(rs.getString("LugarD"));
                Direccion.setText(rs.getString("Direccion"));
                TMuerte.setSelectedItem(rs.getString("TMuerte"));
                Velacion.setText(rs.getString("Velacion"));
                TablasN.setText(rs.getString("TablasN"));
                Iglesia.setText(rs.getString("Iglesia"));
                FechaH.setText(rs.getString("FechaH"));
                Observaciones.setText(rs.getString("Observaciones"));
                EtapasS.setSelectedItem(rs.getString("Etapas"));
                Traslado.setSelectedItem(rs.getString("Traslado"));
                TrasladoDe.setText(rs.getString("TrasladoDe"));
                TrasladoA.setText(rs.getString("TrasladoA"));
                Cremacion.setSelectedItem(rs.getString("Cremacion"));
                RealizaTraslado.setText(rs.getString("RealizaTraslado"));
                Conductor.setText(rs.getString("Conductor"));
                Tanatologo.setText(rs.getString("Tanatologo"));
                Cementerio.setSelectedItem(rs.getString("Cementerio"));
                CementerioDe.setText(rs.getString("CementerioA"));
                Tipo.setSelectedItem(rs.getString("Tipo"));
                Ciudad.setText(rs.getString("Ciudad"));
                Numero.setText(rs.getString("Numero"));
                Cortejo.setText(rs.getString("Cortejo"));
                Arreglo.setText(rs.getString("Arreglo"));
                CafeteriaD.setText(rs.getString("CafeteriaD"));
                CafeteriaN.setText(rs.getString("CafeteriaN"));
            } 
      
        } catch (SQLException ex) {
          System.out.println("Error al cargar ordenservicio: "+ex);
        } 
        AC.desconectar();
  }
  
  private class Guardar_OrdendeServicio implements Runnable {
    private Guardar_OrdendeServicio() {}
    
    public void run() {
        jButton16.setEnabled(false);
        jButton16.setText("Guardando...");
        Guardar_OrdendeServicio();
        jButton16.setEnabled(true);
        jButton16.setText("Guardar");
    }
  }
  
  public void Guardar_OrdendeServicio(){
        Access_connection AC=new Access_connection();
        
        
        String Campo2=TXTCodAfiliado.getText();
        String Campo3=LDefuncion.getText();
        String Campo4=Direccion.getText();
        String Campo5=TMuerte.getSelectedItem().toString();
        String Campo6=Velacion.getText();
        String Campo7=TablasN.getText();
        String Campo8=Iglesia.getText();
        String Campo9=FechaH.getText();
        String Campo10=Observaciones.getText();
        String Campo11=EtapasS.getSelectedItem().toString();
        String Campo12=Traslado.getSelectedItem().toString();
        String Campo13=TrasladoDe.getText();
        String Campo14=TrasladoA.getText();
        String Campo15=Cremacion.getSelectedItem().toString();
        String Campo16=RealizaTraslado.getText();
        String Campo17=Conductor.getText();
        String Campo18=Tanatologo.getText();
        String Campo19=Cementerio.getSelectedItem().toString();
        String Campo20=CementerioDe.getText();
        String Campo21=Tipo.getSelectedItem().toString();
        String Campo22=Ciudad.getText();
        String Campo23=Numero.getText();
        String Campo24=Cortejo.getText();
        String Campo25=Arreglo.getText();
        String Campo26=CafeteriaD.getText();
        String Campo27=CafeteriaN.getText();
        

        try {
            Statement stmt = AC.conn.createStatement();
            stmt.executeUpdate("INSERT INTO Servicios VALUES (" + Campo2 + ",'" + Campo3 + "',"
                    + "'" + Campo4 + "','" + Campo5 + "','" + Campo6 + "','" + Campo7 + "','" + Campo8 + "'"
                        + ",'" + Campo9 + "','" + Campo10 + "','" + Campo11 + "','" + Campo12 + "','" + Campo13 + "'"
                            + ",'" + Campo14 + "','" + Campo15 + "','" + Campo16 + "','" + Campo17 + "','" + Campo18 + "'"
                                + ",'" + Campo19 + "','" + Campo20 + "','" + Campo21 + "','" + Campo22 + "'"
                                + ",'" + Campo23 + "','" + Campo24 + "','" + Campo25 + "','" + Campo26 + "',"
                                    + "'" + Campo27 + "')");
            JOptionPane.showMessageDialog(null, "Se han almacenado los datos correctamente");
        } catch (SQLException ex) {
            System.out.println("no se puede guardar ordenservicio: "+ex+" Se actualiza la orden de servicio");
            try {
                Statement stmt = AC.conn.createStatement();
                stmt.executeUpdate("UPDATE Servicios SET "
                        + "LugarD='" + Campo3 + "', "
                        + "Direccion='" + Campo4 + "', TMuerte='" + Campo5 + "', "
                        + "Velacion='" + Campo6 + "', TablasN='" + Campo7 + "', "
                        + "Iglesia='" + Campo8 + "', FechaH='" + Campo9 + "', "
                        + "Observaciones='" + Campo10 + "', Etapas='" + Campo11 + "', "
                        + "Traslado='" + Campo12 + "', TrasladoDe='" + Campo13 + "', "
                        + "TrasladoA='" + Campo14 + "', Cremacion='" + Campo15 + "', "
                        + "RealizaTraslado='" + Campo16 + "', Conductor='" + Campo17 + "', "
                        + "Tanatologo='" + Campo18 + "', Cementerio='" + Campo19 + "', "
                        + "CementerioA='" + Campo20 + "', Tipo='" + Campo21 + "', "
                        + "Ciudad='" + Campo22 + "', Numero='" + Campo23 + "', "
                        + "Cortejo='" + Campo24 + "', Arreglo='" + Campo25 + "', "
                        + "CafeteriaD='" + Campo26 + "', CafeteriaN='" + Campo27 + "' "
                        + "WHERE CodigoAfiliado=" + Campo2 + "");
                JOptionPane.showMessageDialog(null, "Se han editado los datos correctamente");
            } catch (SQLException e) {
                System.out.println("no se puede editar ordenservicio: "+e);
            } 
        } 
      AC.desconectar();
  }
  
  
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Busqueda = new javax.swing.JFrame();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TABLABusClientes = new javax.swing.JTable();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        Afiliados = new javax.swing.JFrame();
        jPanel16 = new javax.swing.JPanel();
        jButton14 = new javax.swing.JButton();
        jLabel45 = new javax.swing.JLabel();
        COMBOSegurodeVidaAfiliado = new javax.swing.JComboBox<>();
        TXTSegurodeVidaAfiliado = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        COMBODestinoFinalAfiliado = new javax.swing.JComboBox<>();
        COMBOTrasladosAfiliado = new javax.swing.JComboBox<>();
        jLabel44 = new javax.swing.JLabel();
        TXTTrasladosAfiliado = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        FECHATrasladoAfiliados = new com.toedter.calendar.JDateChooser();
        FECHAFallecidoAfiliado = new com.toedter.calendar.JDateChooser();
        jLabel47 = new javax.swing.JLabel();
        TXTNombresAfiliado = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        TXTCodClienteAfiliado = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        TXTCedula = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        COMBOParentescoAfiliado = new javax.swing.JComboBox<>();
        jLabel51 = new javax.swing.JLabel();
        TXTEdadAfiliado = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        COMBOTipoBeneficiario = new javax.swing.JComboBox<>();
        jLabel53 = new javax.swing.JLabel();
        COMBOFallecidoAfiliado = new javax.swing.JComboBox<>();
        jButton18 = new javax.swing.JButton();
        jLabel54 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        TXTAREAObservaciones = new javax.swing.JTextArea();
        jPanel20 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        COMBOActivoAfiliado = new javax.swing.JComboBox<>();
        jLabel43 = new javax.swing.JLabel();
        FECHAActivo = new com.toedter.calendar.JDateChooser();
        jLabel40 = new javax.swing.JLabel();
        FECHADerechosAfiliado = new com.toedter.calendar.JDateChooser();
        TXTIdAfiliado = new javax.swing.JTextField();
        TXTBusqueda = new javax.swing.JTextField();
        jButton11 = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        Ordenes = new javax.swing.JFrame();
        jPanel6 = new javax.swing.JPanel();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        LDefuncion = new javax.swing.JTextField();
        Direccion = new javax.swing.JTextField();
        TMuerte = new javax.swing.JComboBox<>();
        jLabel70 = new javax.swing.JLabel();
        Velacion = new javax.swing.JTextField();
        jLabel71 = new javax.swing.JLabel();
        TablasN = new javax.swing.JTextField();
        jLabel72 = new javax.swing.JLabel();
        Iglesia = new javax.swing.JTextField();
        jLabel73 = new javax.swing.JLabel();
        FechaH = new javax.swing.JTextField();
        jLabel74 = new javax.swing.JLabel();
        jScrollPane13 = new javax.swing.JScrollPane();
        Observaciones = new javax.swing.JTextArea();
        jPanel21 = new javax.swing.JPanel();
        jLabel78 = new javax.swing.JLabel();
        Cortejo = new javax.swing.JTextField();
        CafeteriaD = new javax.swing.JTextField();
        CementerioDe = new javax.swing.JTextField();
        jLabel77 = new javax.swing.JLabel();
        Numero = new javax.swing.JTextField();
        jLabel75 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        Cementerio = new javax.swing.JComboBox<>();
        jLabel80 = new javax.swing.JLabel();
        TrasladoDe = new javax.swing.JTextField();
        EtapasS = new javax.swing.JComboBox<>();
        jLabel88 = new javax.swing.JLabel();
        CafeteriaN = new javax.swing.JTextField();
        jLabel84 = new javax.swing.JLabel();
        jLabel90 = new javax.swing.JLabel();
        Arreglo = new javax.swing.JTextField();
        jLabel82 = new javax.swing.JLabel();
        Tipo = new javax.swing.JComboBox<>();
        TrasladoA = new javax.swing.JTextField();
        jLabel86 = new javax.swing.JLabel();
        jLabel89 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        RealizaTraslado = new javax.swing.JTextField();
        Tanatologo = new javax.swing.JTextField();
        jLabel91 = new javax.swing.JLabel();
        Ciudad = new javax.swing.JTextField();
        jLabel81 = new javax.swing.JLabel();
        Cremacion = new javax.swing.JComboBox<>();
        Traslado = new javax.swing.JComboBox<>();
        Conductor = new javax.swing.JTextField();
        jLabel85 = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        TXTCodAfiliado = new javax.swing.JTextField();
        jButton16 = new javax.swing.JButton();
        VentanaProgresoDescargaNuevaVersion = new javax.swing.JFrame();
        jPanel22 = new javax.swing.JPanel();
        jLabel87 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel92 = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        TXTNumContrato = new javax.swing.JTextField();
        TXTCodCliente = new javax.swing.JTextField();
        FECHAFechaContrato = new com.toedter.calendar.JDateChooser();
        COMBOCiudadResidencia = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        TXTDireccion = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        TXTTelefono = new javax.swing.JTextField();
        COMBOVendedor = new javax.swing.JComboBox<>();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        TXTValordePago = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        COMBOBarrio = new javax.swing.JComboBox<>();
        COMBOFormadePago = new javax.swing.JComboBox<>();
        jLabel36 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        ContratoActivo = new javax.swing.JComboBox<>();
        jLabel57 = new javax.swing.JLabel();
        FECHAInactividadContrato = new com.toedter.calendar.JDateChooser();
        jScrollPane11 = new javax.swing.JScrollPane();
        TXTObservaciones = new javax.swing.JTextArea();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        FECHADerechosAfiliado2 = new javax.swing.JTextField();
        jLabel64 = new javax.swing.JLabel();
        ContratosTraslados = new javax.swing.JTextField();
        FECHATrasladoAfiliados2 = new com.toedter.calendar.JDateChooser();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        TABLAAfiliados = new javax.swing.JTable();
        jButton20 = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        TABLAPagos = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        TXTNumPago = new javax.swing.JTextField();
        TXTTotalPagos = new javax.swing.JTextField();
        FECHARegistroPago = new com.toedter.calendar.JDateChooser();
        jLabel58 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        TXTCuotasNulas = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        COMBOCobrador = new javax.swing.JComboBox<>();
        jLabel60 = new javax.swing.JLabel();
        FECHAPagoDesde = new com.toedter.calendar.JDateChooser();
        FECHAPagoHasta = new com.toedter.calendar.JDateChooser();
        jLabel61 = new javax.swing.JLabel();
        BTNRegistrarPago = new javax.swing.JButton();
        jLabel65 = new javax.swing.JLabel();
        TXTValorPagar = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        IdVendedor = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        NombresVendedor = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        TelefonoVendedor = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        COMBOCiudadVendedores = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        TABLAVendedores = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        IdCobrador = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        NombresCobrador = new javax.swing.JTextField();
        TelefonoCobrador = new javax.swing.JTextField();
        COMBOCiudadCobrador = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        DireccionCobrador = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TABLACobradores = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        CodCiudad = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        NombreCiudad = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        TABLACiudades = new javax.swing.JTable();
        jButton9 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        TXTCodParentesco = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        TXTParentesco = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        TABLAParentescos = new javax.swing.JTable();
        jButton10 = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        TXTCodFuneraria = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        TXTFuneraria = new javax.swing.JTextField();
        jScrollPane6 = new javax.swing.JScrollPane();
        TABLAFuneraria = new javax.swing.JTable();
        jButton15 = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        IdUsuario = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        NombresUsuarios = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        NickUsuario = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        PassUsuario = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        COMBOTipoUsuario = new javax.swing.JComboBox<>();
        jScrollPane7 = new javax.swing.JScrollPane();
        TABLAUsuariosSistema = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jButton21 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jButton19 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

        Busqueda.setMinimumSize(new java.awt.Dimension(700, 400));

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));

        TABLABusClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Núm Contrato", "Cód. Cliente", "Nombres y Apellidos", "Núm. Identificación"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TABLABusClientes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TABLABusClientesKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TABLABusClientesKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(TABLABusClientes);
        if (TABLABusClientes.getColumnModel().getColumnCount() > 0) {
            TABLABusClientes.getColumnModel().getColumn(2).setMinWidth(400);
            TABLABusClientes.getColumnModel().getColumn(2).setPreferredWidth(400);
        }

        jButton12.setText("Mostrar todos");
        jButton12.setContentAreaFilled(false);
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setText("Cargar Cliente");
        jButton13.setContentAreaFilled(false);
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jButton12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton13)))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton12)
                    .addComponent(jButton13))
                .addContainerGap())
        );

        javax.swing.GroupLayout BusquedaLayout = new javax.swing.GroupLayout(Busqueda.getContentPane());
        Busqueda.getContentPane().setLayout(BusquedaLayout);
        BusquedaLayout.setHorizontalGroup(
            BusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        BusquedaLayout.setVerticalGroup(
            BusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        Afiliados.setMinimumSize(new java.awt.Dimension(1100, 404));

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));

        jButton14.setText("Guardar");
        jButton14.setContentAreaFilled(false);
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jLabel45.setText("Seguro de Vida");

        COMBOSegurodeVidaAfiliado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No", "Si" }));
        COMBOSegurodeVidaAfiliado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                COMBOSegurodeVidaAfiliadoActionPerformed(evt);
            }
        });

        jLabel46.setText("Destino Final");

        COMBODestinoFinalAfiliado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No", "Si" }));

        COMBOTrasladosAfiliado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No", "Si" }));
        COMBOTrasladosAfiliado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                COMBOTrasladosAfiliadoActionPerformed(evt);
            }
        });

        jLabel44.setText("Traslado");

        jLabel41.setText("Fecha Traslados");

        jLabel47.setText("Nombres y Apellidos");

        jLabel48.setText("Cod. Cliente");

        TXTCodClienteAfiliado.setEditable(false);

        jLabel49.setText("Número de Identificación");

        jLabel50.setText("Parentezco");

        jLabel51.setText("Edad");

        jLabel52.setText("Tipo de Beneficiario");

        COMBOTipoBeneficiario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-", "Titular", "Beneficiario" }));

        jLabel53.setText("Fallecido");

        COMBOFallecidoAfiliado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No", "Si" }));
        COMBOFallecidoAfiliado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                COMBOFallecidoAfiliadoActionPerformed(evt);
            }
        });

        jButton18.setText("Crear/Editar Orden");
        jButton18.setContentAreaFilled(false);
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel54.setText("Observaciones");

        TXTAREAObservaciones.setColumns(20);
        TXTAREAObservaciones.setLineWrap(true);
        TXTAREAObservaciones.setRows(5);
        TXTAREAObservaciones.setWrapStyleWord(true);
        jScrollPane10.setViewportView(TXTAREAObservaciones);

        jLabel42.setText("Activo");

        COMBOActivoAfiliado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No", "Si" }));
        COMBOActivoAfiliado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                COMBOActivoAfiliadoActionPerformed(evt);
            }
        });

        jLabel43.setText("Fecha Inactivo");

        jLabel40.setText("Fecha Derechos");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addComponent(jLabel42)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(COMBOActivoAfiliado, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel40)
                            .addComponent(jLabel43))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(FECHAActivo, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                            .addComponent(FECHADerechosAfiliado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(COMBOActivoAfiliado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel43)
                    .addComponent(FECHAActivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel40)
                    .addComponent(FECHADerechosAfiliado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel48)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TXTCodClienteAfiliado, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel49)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(TXTCedula)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel51)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TXTEdadAfiliado, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel47)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(TXTNombresAfiliado))
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel50)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(COMBOParentescoAfiliado, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel52)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(COMBOTipoBeneficiario, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel54, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane10, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel44)
                                .addGap(18, 18, 18)
                                .addComponent(COMBOTrasladosAfiliado, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TXTTrasladosAfiliado, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel41)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(FECHATrasladoAfiliados, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel45)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(COMBOSegurodeVidaAfiliado, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(TXTSegurodeVidaAfiliado, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE))
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel46)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(COMBODestinoFinalAfiliado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel53)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(COMBOFallecidoAfiliado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(FECHAFallecidoAfiliado, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel48)
                            .addComponent(TXTCodClienteAfiliado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel49)
                            .addComponent(TXTCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel51)
                            .addComponent(TXTEdadAfiliado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel47)
                            .addComponent(TXTNombresAfiliado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel50)
                            .addComponent(COMBOParentescoAfiliado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel52)
                            .addComponent(COMBOTipoBeneficiario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel44)
                        .addComponent(COMBOTrasladosAfiliado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(TXTTrasladosAfiliado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel41))
                    .addComponent(FECHATrasladoAfiliados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel45)
                        .addComponent(COMBOSegurodeVidaAfiliado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(TXTSegurodeVidaAfiliado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel46)
                        .addComponent(COMBODestinoFinalAfiliado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel53)
                            .addComponent(COMBOFallecidoAfiliado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(FECHAFallecidoAfiliado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jButton18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel54)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton14)
                .addContainerGap())
        );

        javax.swing.GroupLayout AfiliadosLayout = new javax.swing.GroupLayout(Afiliados.getContentPane());
        Afiliados.getContentPane().setLayout(AfiliadosLayout);
        AfiliadosLayout.setHorizontalGroup(
            AfiliadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        AfiliadosLayout.setVerticalGroup(
            AfiliadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        TXTIdAfiliado.setText("jTextField5");

        TXTBusqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TXTBusquedaActionPerformed(evt);
            }
        });

        jButton11.setText("Buscar");
        jButton11.setContentAreaFilled(false);
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton5.setText("Ajustar edades a la fecha actual");

        jButton6.setText("Ajustar edades a la fecha de fallecimiento");

        jLabel20.setText("Año de último ajuste realizado");

        jLabel21.setText("Cambio Realizado a: ");

        jLabel22.setText("2000");

        jLabel23.setText("2000");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton5)
                    .addComponent(jButton6)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel22))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel23)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(jLabel23))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Ordenes.setTitle("Orden de Servicio");
        Ordenes.setMinimumSize(new java.awt.Dimension(950, 650));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel66.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel66.setText("Orden de Servicio");

        jLabel67.setText("Lugar de Defunción");

        jLabel68.setText("Dirección");

        jLabel69.setText("Tipo de Muerte");

        Direccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DireccionActionPerformed(evt);
            }
        });

        TMuerte.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-", "Natural", "Violenta" }));

        jLabel70.setText("Velación");

        jLabel71.setText("Tablas No.");

        jLabel72.setText("Iglesia");

        jLabel73.setText("Fecha y Hora: (DD/MM/AAAA HH:MM AM/PM)");

        jLabel74.setText("Observaciones");

        Observaciones.setColumns(20);
        Observaciones.setLineWrap(true);
        Observaciones.setRows(5);
        Observaciones.setWrapStyleWord(true);
        jScrollPane13.setViewportView(Observaciones);

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));

        jLabel78.setText("A");

        Cortejo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CortejoActionPerformed(evt);
            }
        });

        CafeteriaD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CafeteriaDActionPerformed(evt);
            }
        });

        jLabel77.setText("De");

        jLabel75.setText("Etapas del servicio");

        jLabel79.setText("Cremación");

        Cementerio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-", "Jardín", "Boveda" }));

        jLabel80.setText("Quién Realiza el Traslado");

        EtapasS.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-", "Inciales", "Finales", "Completo" }));

        jLabel88.setText("Cortejo");

        CafeteriaN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CafeteriaNActionPerformed(evt);
            }
        });

        jLabel84.setText("Tipo");

        jLabel90.setText("Cafetería/Noche");

        Arreglo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ArregloActionPerformed(evt);
            }
        });

        jLabel82.setText("Tanatólogo");

        Tipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-", "Notaría", "Registraduría" }));

        jLabel86.setText("Número");

        jLabel89.setText("Cafetería/Día");

        jLabel76.setText("Traslado");

        jLabel91.setText("Arreglo Floral");

        Ciudad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CiudadActionPerformed(evt);
            }
        });

        jLabel81.setText("Conductor");

        Cremacion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-", "No", "Si" }));

        Traslado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-", "No", "Si" }));

        Conductor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConductorActionPerformed(evt);
            }
        });

        jLabel85.setText("Ciudad");

        jLabel83.setText("Cementerio");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Tipo, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel84))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Ciudad, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel85))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addComponent(jLabel86)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(Numero)))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addComponent(Cementerio, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(CementerioDe))
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(EtapasS, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel75))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Traslado, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel76))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(TrasladoDe)
                                    .addComponent(jLabel77))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(TrasladoA)
                                    .addComponent(jLabel78))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel79)
                                    .addComponent(Cremacion, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel83)
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(RealizaTraslado)
                                    .addComponent(jLabel80))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Conductor)
                                    .addComponent(jLabel81))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel82)
                                    .addComponent(Tanatologo))))
                        .addGap(10, 10, 10))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(Cortejo, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Arreglo))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(CafeteriaD, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel89))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(CafeteriaN, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel90))
                        .addGap(0, 374, Short.MAX_VALUE))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(jLabel88)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel91)
                        .addGap(315, 315, 315)))
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel75)
                            .addComponent(jLabel76)
                            .addComponent(jLabel77)
                            .addComponent(jLabel78)
                            .addComponent(jLabel79))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(EtapasS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Traslado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TrasladoDe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TrasladoA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Cremacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel80)
                            .addComponent(jLabel81)
                            .addComponent(jLabel82))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(RealizaTraslado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Conductor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Tanatologo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addComponent(jLabel83)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(Cementerio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(CementerioDe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel84)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel85)
                                    .addComponent(jLabel86))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(Ciudad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Numero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel88)
                            .addComponent(jLabel91))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Cortejo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Arreglo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(jLabel89)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CafeteriaD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(jLabel90)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CafeteriaN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        TXTCodAfiliado.setEditable(false);

        jButton16.setText("Guardar");
        jButton16.setContentAreaFilled(false);
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel69)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(TMuerte, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jLabel70)
                                .addGap(18, 18, 18)
                                .addComponent(Velacion)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel71)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TablasN, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel72)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Iglesia)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel73)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(FechaH))
                            .addComponent(jScrollPane13)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel74)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel66, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TXTCodAfiliado, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel67)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(LDefuncion, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel68)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Direccion))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jButton16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel66)
                    .addComponent(TXTCodAfiliado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel67)
                    .addComponent(LDefuncion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel68)
                    .addComponent(Direccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel69)
                    .addComponent(TMuerte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel70)
                    .addComponent(Velacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel71)
                    .addComponent(TablasN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel73)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel72)
                        .addComponent(Iglesia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(FechaH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel74)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton16)
                .addContainerGap())
        );

        javax.swing.GroupLayout OrdenesLayout = new javax.swing.GroupLayout(Ordenes.getContentPane());
        Ordenes.getContentPane().setLayout(OrdenesLayout);
        OrdenesLayout.setHorizontalGroup(
            OrdenesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        OrdenesLayout.setVerticalGroup(
            OrdenesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        VentanaProgresoDescargaNuevaVersion.setMinimumSize(new java.awt.Dimension(500, 300));
        VentanaProgresoDescargaNuevaVersion.setResizable(false);

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));

        jLabel87.setText("Descargar Última Versión Publicada");

        jProgressBar1.setStringPainted(true);

        jLabel92.setText("Progreso de Descarga");

        jLabel93.setText("Descargar Ahora");
        jLabel93.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jLabel93ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel93, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel87)
                            .addComponent(jLabel92))
                        .addGap(0, 313, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel87)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel93, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 178, Short.MAX_VALUE)
                .addComponent(jLabel92)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout VentanaProgresoDescargaNuevaVersionLayout = new javax.swing.GroupLayout(VentanaProgresoDescargaNuevaVersion.getContentPane());
        VentanaProgresoDescargaNuevaVersion.getContentPane().setLayout(VentanaProgresoDescargaNuevaVersionLayout);
        VentanaProgresoDescargaNuevaVersionLayout.setHorizontalGroup(
            VentanaProgresoDescargaNuevaVersionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        VentanaProgresoDescargaNuevaVersionLayout.setVerticalGroup(
            VentanaProgresoDescargaNuevaVersionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setText("Número de Contrato");

        jLabel4.setText("Código de Cliente");

        jLabel33.setText("Fecha Contrato");

        jLabel35.setText("Ciudad");

        TXTCodCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                TXTCodClienteFocusLost(evt);
            }
        });

        COMBOCiudadResidencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                COMBOCiudadResidenciaActionPerformed(evt);
            }
        });

        jLabel3.setText("Dirección");

        jLabel5.setText("Barrio");

        jLabel34.setText("Teléfono");

        jLabel37.setText("Forma de pago");

        jLabel38.setText("Valor Pago Actual");

        jLabel39.setText("Observaciones");

        jLabel36.setText("Vendedor");

        jLabel56.setText("Activo");

        ContratoActivo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-", "Activo", "Inactivo" }));
        ContratoActivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ContratoActivoActionPerformed(evt);
            }
        });

        jLabel57.setText("Fecha Inactividad");

        TXTObservaciones.setColumns(20);
        TXTObservaciones.setLineWrap(true);
        TXTObservaciones.setRows(3);
        TXTObservaciones.setTabSize(1);
        TXTObservaciones.setWrapStyleWord(true);
        jScrollPane11.setViewportView(TXTObservaciones);

        jLabel62.setText("Fecha de Derechos");
        jLabel62.setEnabled(false);

        jLabel63.setText("Fecha Traslados");
        jLabel63.setEnabled(false);

        FECHADerechosAfiliado2.setEnabled(false);

        jLabel64.setText("Traslado");
        jLabel64.setEnabled(false);

        ContratosTraslados.setEnabled(false);

        FECHATrasladoAfiliados2.setEnabled(false);

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel39)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(jLabel62)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(FECHADerechosAfiliado2, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel64)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ContratosTraslados, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel63)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(FECHATrasladoAfiliados2, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel38)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TXTValordePago, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TXTNumContrato, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TXTCodCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel37)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(COMBOFormadePago, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(16, 16, 16)
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(FECHAFechaContrato, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel36)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel19Layout.createSequentialGroup()
                                .addComponent(COMBOVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel35)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(COMBOCiudadResidencia, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel34)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TXTTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel19Layout.createSequentialGroup()
                                .addComponent(TXTDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(COMBOBarrio, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel56)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ContratoActivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel57)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(FECHAInactividadContrato, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(TXTNumContrato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)
                        .addComponent(TXTCodCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel37)
                        .addComponent(COMBOFormadePago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel33))
                    .addComponent(FECHAFechaContrato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(COMBOVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel36))
                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel35)
                        .addComponent(COMBOCiudadResidencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel34)
                        .addComponent(TXTTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(TXTDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(COMBOBarrio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel56)
                        .addComponent(ContratoActivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel57))
                    .addComponent(FECHAInactividadContrato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel38)
                        .addComponent(TXTValordePago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel62)
                        .addComponent(FECHADerechosAfiliado2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel64)
                        .addComponent(ContratosTraslados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel63))
                    .addComponent(FECHATrasladoAfiliados2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addComponent(jLabel39)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane11)
                .addContainerGap())
        );

        jTabbedPane3.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));

        TABLAAfiliados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Nombres", "Edad", "Parentezco", "Derechos", "Fallecido", "Activo", "Seguro de Vida", "Tipo", "Eliminar", "Editar"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TABLAAfiliados.setRowHeight(30);
        TABLAAfiliados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TABLAAfiliadosMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(TABLAAfiliados);
        if (TABLAAfiliados.getColumnModel().getColumnCount() > 0) {
            TABLAAfiliados.getColumnModel().getColumn(1).setMinWidth(400);
            TABLAAfiliados.getColumnModel().getColumn(1).setPreferredWidth(400);
        }

        jButton20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico_diseno/assets-74.png"))); // NOI18N
        jButton20.setText("     Agregar nuevo afiliado");
        jButton20.setToolTipText("Crear Contrato");
        jButton20.setContentAreaFilled(false);
        jButton20.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton20.setFocusPainted(false);
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 929, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton20)))
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("Afiliados", jPanel17);

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));

        TABLAPagos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código de Pago", "Fecha", "Depositante", "Cuotas Nulas", "Pago Desde", "Pago Hasta", "Valor", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TABLAPagos.setRowHeight(30);
        jScrollPane8.setViewportView(TABLAPagos);
        if (TABLAPagos.getColumnModel().getColumnCount() > 0) {
            TABLAPagos.getColumnModel().getColumn(0).setMinWidth(5);
            TABLAPagos.getColumnModel().getColumn(0).setPreferredWidth(5);
            TABLAPagos.getColumnModel().getColumn(1).setMinWidth(500);
            TABLAPagos.getColumnModel().getColumn(1).setPreferredWidth(500);
            TABLAPagos.getColumnModel().getColumn(2).setHeaderValue("Depositante");
            TABLAPagos.getColumnModel().getColumn(3).setHeaderValue("Cuotas Nulas");
            TABLAPagos.getColumnModel().getColumn(4).setHeaderValue("Pago Desde");
            TABLAPagos.getColumnModel().getColumn(5).setHeaderValue("Pago Hasta");
            TABLAPagos.getColumnModel().getColumn(6).setHeaderValue("Valor");
            TABLAPagos.getColumnModel().getColumn(7).setHeaderValue("");
        }

        jLabel1.setText("Total Cuotas");

        jLabel31.setText("Total Pagos");

        jLabel58.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel58.setText("Fecha Registro");

        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("Cuotas Nulas");

        jLabel59.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel59.setText("Cobrador");

        jLabel60.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel60.setText("Desde");

        jLabel61.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel61.setText("Hasta");

        BTNRegistrarPago.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico_diseno/assets-74.png"))); // NOI18N
        BTNRegistrarPago.setToolTipText("Crear Contrato");
        BTNRegistrarPago.setContentAreaFilled(false);
        BTNRegistrarPago.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BTNRegistrarPago.setFocusPainted(false);
        BTNRegistrarPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNRegistrarPagoActionPerformed(evt);
            }
        });

        jLabel65.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel65.setText("Valor Pago");

        TXTValorPagar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                TXTValorPagarFocusGained(evt);
            }
        });
        TXTValorPagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TXTValorPagarActionPerformed(evt);
            }
        });

        jButton4.setText("Mostrar todos los pagos");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 929, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TXTNumPago, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(TXTTotalPagos, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(FECHARegistroPago, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                            .addComponent(jLabel58, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TXTValorPagar, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(TXTCuotasNulas, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel59, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(COMBOCobrador, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(FECHAPagoDesde, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(FECHAPagoHasta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BTNRegistrarPago, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel18Layout.createSequentialGroup()
                            .addComponent(jLabel58)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(FECHARegistroPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel18Layout.createSequentialGroup()
                            .addComponent(jLabel60)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(FECHAPagoDesde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel18Layout.createSequentialGroup()
                            .addComponent(jLabel61)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(FECHAPagoHasta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel18Layout.createSequentialGroup()
                            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel32)
                                .addComponent(jLabel59))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(TXTCuotasNulas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(COMBOCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel18Layout.createSequentialGroup()
                            .addComponent(jLabel65)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(TXTValorPagar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(BTNRegistrarPago, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(TXTNumPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31)
                    .addComponent(TXTTotalPagos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4))
                .addContainerGap())
        );

        jTabbedPane3.addTab("Pagos", jPanel18);

        LABELVersion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LABELVersion.setText("Modo Online");

        LABELVersion1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LABELVersion1.setText("V1.2");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(LABELVersion1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LABELVersion, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane3))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jTabbedPane3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LABELVersion, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LABELVersion1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );

        jTabbedPane1.addTab("Centro de Búsqueda", jPanel4);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jTabbedPane2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane2StateChanged(evt);
            }
        });
        jTabbedPane2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane2MouseClicked(evt);
            }
        });

        jLabel6.setText("Número de Identificación");

        jLabel7.setText("Nombres y Apellidos");

        jLabel8.setText("Dirección");

        jLabel9.setText("Teléfono");

        jLabel10.setText("Ciudad");

        COMBOCiudadVendedores.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton1.setText("Guardar");
        jButton1.setContentAreaFilled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        TABLAVendedores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Identificación", "Nombres y Apellidos", "Dirección", "Teléfono", "Ciudad", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TABLAVendedores.setRowHeight(30);
        jScrollPane2.setViewportView(TABLAVendedores);
        if (TABLAVendedores.getColumnModel().getColumnCount() > 0) {
            TABLAVendedores.getColumnModel().getColumn(1).setMinWidth(500);
            TABLAVendedores.getColumnModel().getColumn(1).setPreferredWidth(500);
        }

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(IdVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(NombresVendedor))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TelefonoVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(COMBOCiudadVendedores, 0, 339, Short.MAX_VALUE))
                    .addComponent(jScrollPane2)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(IdVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(NombresVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(TelefonoVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(COMBOCiudadVendedores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Vendedores", jPanel8);

        jLabel11.setText("Número de Identificación");

        jLabel12.setText("Nombres y Apellidos");

        COMBOCiudadCobrador.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel13.setText("Ciudad");

        jLabel14.setText("Teléfono");

        jLabel15.setText("Dirección");

        TABLACobradores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Identificación", "Nombres y Apellidos", "Dirección", "Teléfono", "Ciudad", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TABLACobradores.setRowHeight(30);
        jScrollPane3.setViewportView(TABLACobradores);
        if (TABLACobradores.getColumnModel().getColumnCount() > 0) {
            TABLACobradores.getColumnModel().getColumn(1).setMinWidth(500);
            TABLACobradores.getColumnModel().getColumn(1).setPreferredWidth(500);
        }

        jButton2.setText("Guardar");
        jButton2.setContentAreaFilled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(IdCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(NombresCobrador))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(DireccionCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TelefonoCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(COMBOCiudadCobrador, 0, 339, Short.MAX_VALUE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(IdCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(NombresCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(DireccionCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(TelefonoCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(COMBOCiudadCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Cobradores", jPanel9);

        jLabel16.setText("Código de Ciudad");

        CodCiudad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CodCiudadActionPerformed(evt);
            }
        });

        jLabel17.setText("Ciudad");

        TABLACiudades.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo de Ciudad", "Ciudad", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TABLACiudades.setRowHeight(30);
        jScrollPane4.setViewportView(TABLACiudades);
        if (TABLACiudades.getColumnModel().getColumnCount() > 0) {
            TABLACiudades.getColumnModel().getColumn(1).setMinWidth(600);
            TABLACiudades.getColumnModel().getColumn(1).setPreferredWidth(600);
        }

        jButton9.setText("Guardar");
        jButton9.setContentAreaFilled(false);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CodCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(NombreCiudad))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 949, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(CodCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(NombreCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9)
                .addGap(11, 11, 11)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Ciudades", jPanel10);

        jLabel18.setText("Código de Parentezco ");

        TXTCodParentesco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TXTCodParentescoActionPerformed(evt);
            }
        });

        jLabel19.setText("Parentezco");

        TABLAParentescos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código de Parentezco ", "Parentezco", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TABLAParentescos.setRowHeight(30);
        jScrollPane5.setViewportView(TABLAParentescos);
        if (TABLAParentescos.getColumnModel().getColumnCount() > 0) {
            TABLAParentescos.getColumnModel().getColumn(1).setMinWidth(600);
            TABLAParentescos.getColumnModel().getColumn(1).setPreferredWidth(600);
        }

        jButton10.setText("Guardar");
        jButton10.setContentAreaFilled(false);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TXTCodParentesco, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(TXTParentesco))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 949, Short.MAX_VALUE)
                    .addComponent(jButton10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(TXTCodParentesco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(TXTParentesco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton10)
                .addGap(11, 11, 11)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Parentezcos", jPanel11);

        jLabel24.setText("Identificación o NIT de funeraria");

        TXTCodFuneraria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TXTCodFunerariaActionPerformed(evt);
            }
        });

        jLabel25.setText("Nombre de Funeraria");

        TABLAFuneraria.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código de Funeraria ", "Funeraria", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TABLAFuneraria.setRowHeight(30);
        jScrollPane6.setViewportView(TABLAFuneraria);
        if (TABLAFuneraria.getColumnModel().getColumnCount() > 0) {
            TABLAFuneraria.getColumnModel().getColumn(1).setMinWidth(600);
            TABLAFuneraria.getColumnModel().getColumn(1).setPreferredWidth(600);
        }

        jButton15.setText("Guardar");
        jButton15.setContentAreaFilled(false);
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TXTCodFuneraria, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(TXTFuneraria))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 949, Short.MAX_VALUE)
                    .addComponent(jButton15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(TXTCodFuneraria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25)
                    .addComponent(TXTFuneraria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton15)
                .addGap(11, 11, 11)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Funerarias", jPanel13);

        jLabel26.setText("Número de Identificación");

        jLabel27.setText("Nombres y Apellidos");

        jLabel28.setText("Usuario");

        jLabel29.setText("Contraseña");

        jLabel30.setText("Tipo de Usuario");

        COMBOTipoUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-", "Administrador", "Cobrador", "Auxiliar" }));

        TABLAUsuariosSistema.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Identificación", "Nombres y Apellidos", "TipoUsuario", "Usuario", "Contraseña", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TABLAUsuariosSistema.setRowHeight(30);
        jScrollPane7.setViewportView(TABLAUsuariosSistema);
        if (TABLAUsuariosSistema.getColumnModel().getColumnCount() > 0) {
            TABLAUsuariosSistema.getColumnModel().getColumn(1).setMinWidth(400);
            TABLAUsuariosSistema.getColumnModel().getColumn(1).setPreferredWidth(400);
        }

        jButton3.setText("Guardar");
        jButton3.setContentAreaFilled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(IdUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(NombresUsuarios))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(NickUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(PassUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(COMBOTipoUsuario, 0, 384, Short.MAX_VALUE))
                    .addComponent(jScrollPane7)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(IdUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27)
                    .addComponent(NombresUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(NickUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29)
                    .addComponent(PassUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30)
                    .addComponent(COMBOTipoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Usuarios del Sistema", jPanel14);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );

        jTabbedPane1.addTab("Administración", jPanel7);

        jButton21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico_diseno/assets-89.png"))); // NOI18N
        jButton21.setText("Contratos");
        jButton21.setToolTipText("");
        jButton21.setContentAreaFilled(false);
        jButton21.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton21.setFocusPainted(false);
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        jButton22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico_diseno/assets-89.png"))); // NOI18N
        jButton22.setText("Muertes");
        jButton22.setToolTipText("");
        jButton22.setContentAreaFilled(false);
        jButton22.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton22.setFocusPainted(false);
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        jButton23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico_diseno/assets-89.png"))); // NOI18N
        jButton23.setText("Pagos");
        jButton23.setToolTipText("");
        jButton23.setContentAreaFilled(false);
        jButton23.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton23.setFocusPainted(false);
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        jButton24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico_diseno/assets-89.png"))); // NOI18N
        jButton24.setText("Clientes Atrasados");
        jButton24.setToolTipText("");
        jButton24.setContentAreaFilled(false);
        jButton24.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton24.setFocusPainted(false);
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        jButton25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico_diseno/assets-89.png"))); // NOI18N
        jButton25.setText("Clientes Activos");
        jButton25.setToolTipText("");
        jButton25.setContentAreaFilled(false);
        jButton25.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton25.setFocusPainted(false);
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton21)
                    .addComponent(jButton22)
                    .addComponent(jButton23)
                    .addComponent(jButton24)
                    .addComponent(jButton25))
                .addContainerGap(805, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(281, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Reportes", jPanel5);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jButton19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico_diseno/assets-74.png"))); // NOI18N
        jButton19.setText("     Crear nuevo contrato");
        jButton19.setToolTipText("Crear Contrato");
        jButton19.setContentAreaFilled(false);
        jButton19.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton19.setFocusPainted(false);
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jTextField1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setToolTipText("Escriba su búsqueda y presione Enter.");
        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField1FocusGained(evt);
            }
        });
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton19)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField1)
                    .addComponent(jButton19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addGap(7, 7, 7))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jMenu1.setText("Archivo");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem1.setText("Buscar");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem2.setText("Guardar");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem3.setText("Ir A Pagos");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

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

    private void CodCiudadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CodCiudadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CodCiudadActionPerformed

    private void TXTCodParentescoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TXTCodParentescoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TXTCodParentescoActionPerformed

    private void TXTCodFunerariaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TXTCodFunerariaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TXTCodFunerariaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        new Thread(new GuardarAdministracion_Vendedores()).start();  
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void TXTBusquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TXTBusquedaActionPerformed

        PresionarBuscar();
    }//GEN-LAST:event_TXTBusquedaActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        PresionarBuscar();
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        (new Thread(new CargarCliente())).start();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        (new Thread(new BuscarClientes())).start();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton12ActionPerformed

    private void COMBOCiudadResidenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_COMBOCiudadResidenciaActionPerformed
        try {
            CargarDesplegableBarrios();
          } catch (Exception exception) {}
    }//GEN-LAST:event_COMBOCiudadResidenciaActionPerformed

    private void ContratoActivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ContratoActivoActionPerformed
        if(ContratoActivo.getSelectedIndex()==1){
            FECHAInactividadContrato.setEnabled(false);
            FECHAInactividadContrato.setCalendar(null);
        }else{
            FECHAInactividadContrato.setEnabled(true);
        }
    }//GEN-LAST:event_ContratoActivoActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        CrearNuevoContrato();
        TXTNumContrato.requestFocus();
        
    }//GEN-LAST:event_jButton19ActionPerformed

    private void TABLABusClientesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TABLABusClientesKeyPressed
   // TODO add your handling code here:
    }//GEN-LAST:event_TABLABusClientesKeyPressed

    private void TABLAAfiliadosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TABLAAfiliadosMouseClicked

        if(TABLAAfiliados.getValueAt(TABLAAfiliados.getSelectedRow(), 5).toString().equals("Si")){
            if(evt.getClickCount()==2){
                System.out.println("Se ha hecho doble click");
                NumAfiliado=Integer.parseInt(TABLAAfiliados.getValueAt(TABLAAfiliados.getSelectedRow(), 0).toString());
                Cargar_OrdendeServicio();
            }       
        }
        
    }//GEN-LAST:event_TABLAAfiliadosMouseClicked

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
            ValidarAfiliadoExistente();
    }//GEN-LAST:event_jButton14ActionPerformed

    private void BTNRegistrarPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNRegistrarPagoActionPerformed
         (new Thread(new GuardarPago())).start();
    }//GEN-LAST:event_BTNRegistrarPagoActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        this.toFront();
        jTextField1.requestFocus();
//        Busqueda.setVisible(true);
//        Busqueda.setLocationRelativeTo(null);        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void COMBOActivoAfiliadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_COMBOActivoAfiliadoActionPerformed
        if(COMBOActivoAfiliado.getSelectedIndex()==1){
            FECHAActivo.setEnabled(false);
            FECHAActivo.setCalendar(null);
            FECHADerechosAfiliado.setEnabled(true);
            
        }else{
            FECHAActivo.setEnabled(true);
            FECHADerechosAfiliado.setEnabled(false);
            FECHADerechosAfiliado.setCalendar(null);
        }
    }//GEN-LAST:event_COMBOActivoAfiliadoActionPerformed

    private void COMBOFallecidoAfiliadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_COMBOFallecidoAfiliadoActionPerformed
        if(COMBOFallecidoAfiliado.getSelectedIndex()==0){
            FECHAFallecidoAfiliado.setCalendar(null);
            FECHAFallecidoAfiliado.setEnabled(false);
            jButton18.setEnabled(false);
        }else{
            FECHAFallecidoAfiliado.setEnabled(true);
            jButton18.setEnabled(true);
        }
    }//GEN-LAST:event_COMBOFallecidoAfiliadoActionPerformed

    private void COMBOTrasladosAfiliadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_COMBOTrasladosAfiliadoActionPerformed
        if(COMBOTrasladosAfiliado.getSelectedIndex()==0){
            FECHATrasladoAfiliados.setEnabled(false);
            FECHATrasladoAfiliados.setCalendar(null);
            TXTTrasladosAfiliado.setEnabled(false);
        }else{
            FECHATrasladoAfiliados.setEnabled(true);
            TXTTrasladosAfiliado.setEnabled(true);
        }
    }//GEN-LAST:event_COMBOTrasladosAfiliadoActionPerformed

    private void COMBOSegurodeVidaAfiliadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_COMBOSegurodeVidaAfiliadoActionPerformed
        if(COMBOSegurodeVidaAfiliado.getSelectedIndex()==0){
            TXTSegurodeVidaAfiliado.setEnabled(false);
            TXTSegurodeVidaAfiliado.setText("");
        }else{
            TXTSegurodeVidaAfiliado.setEnabled(true);
        }
    }//GEN-LAST:event_COMBOSegurodeVidaAfiliadoActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
             // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            TXTBusqueda.setText(jTextField1.getText());
            PresionarBuscar();
            CrearNuevoContrato();
             
        }
    }//GEN-LAST:event_jTextField1KeyReleased

    private void TABLABusClientesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TABLABusClientesKeyReleased
      if (evt.getKeyCode() == KeyEvent.VK_ENTER){
        if ((this.TABLABusClientes.getSelectedRows()).length > 0) {
            (new Thread(new CargarCliente())).start();
          } else {
            JOptionPane.showMessageDialog(null, "Primero Seleccione un Cliente");
          }   
      }
                    // TODO add your handling code here:
    }//GEN-LAST:event_TABLABusClientesKeyReleased

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        NumAfiliado=0;//Afiliado Nuevo        
        ValidarContratoExistente();
        LimpiarCampoAfiliados();
        Afiliados.setVisible(true);
        Afiliados.setLocationRelativeTo(null);
        TXTCodClienteAfiliado.setText(TXTCodCliente.getText());
    }//GEN-LAST:event_jButton20ActionPerformed

    private void TXTCodClienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TXTCodClienteFocusLost
        try{
            ValidarExisteCodCliente();
        }catch(Exception e){
            
        }
            
        
    }//GEN-LAST:event_TXTCodClienteFocusLost

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        ValidarContratoExistente();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void TXTValorPagarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TXTValorPagarFocusGained
        TXTValorPagar.setText(TXTValordePago.getText());
    }//GEN-LAST:event_TXTValorPagarFocusGained

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
                new Thread(new GuardarAdministracion_Cobradores()).start();  
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        new Thread(new GuardarAdministracion_Ciudades()).start();  
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        new Thread(new GuardarAdministracion_Parentezcos()).start();  
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        new Thread(new GuardarAdministracion_Funerarias()).start();  
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        new Thread(new GuardarAdministracion_UsuariosSistema()).start();  
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        new Thread(new Cargar_OrdendeServicio()).start();  
    }//GEN-LAST:event_jButton18ActionPerformed

    private void DireccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DireccionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DireccionActionPerformed

    private void ConductorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConductorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ConductorActionPerformed

    private void CiudadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CiudadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CiudadActionPerformed

    private void CortejoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CortejoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CortejoActionPerformed

    private void CafeteriaDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CafeteriaDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CafeteriaDActionPerformed

    private void CafeteriaNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CafeteriaNActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CafeteriaNActionPerformed

    private void ArregloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ArregloActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ArregloActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        new Thread(new Guardar_OrdendeServicio()).start();  
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        Reportes R=new Reportes();
        R.CargarReporte_Contratos();
        
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        Reportes R=new Reportes();
        R.CargarReporte_Muertes();
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        Reportes R=new Reportes();
        R.CargarHiloReporte_Pagos();
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        int reply = JOptionPane.showConfirmDialog(null, "Se generará un reporte muy grande y tardará varios minutos,\n "
                + "¿Desea generar este reporte?","¿Está seguro de continuar?", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            Reportes R=new Reportes();
            R.CargarReporte_ClientesA();
        }
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        int reply = JOptionPane.showConfirmDialog(null, "Se generará un reporte muy grande y tardará varios minutos,\n "
                + "¿Desea generar este reporte?","¿Está seguro de continuar?", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            Reportes R=new Reportes();
            R.CargarReporte_ClientesAct();
        }
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jTabbedPane2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane2MouseClicked

    }//GEN-LAST:event_jTabbedPane2MouseClicked

    private void jTabbedPane2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane2StateChanged
        int sel = jTabbedPane2.getSelectedIndex();
        System.out.println(sel);
        
        switch (sel) {
            case 0:
                new Thread(new CargarAdministracion_Vendedores()).start();
                break;
            case 1:
                new Thread(new CargarAdministracion_Cobradores()).start();
                break;
            case 2:
                new Thread(new CargarAdministracion_Ciudades()).start();
                break;
            case 3:
                new Thread(new CargarAdministracion_Parentezcos()).start();  
                break;
            case 4:
                new Thread(new CargarAdministracion_Funerarias()).start();
                break;
            case 5:
                new Thread(new CargarAdministracion_UsuariosSistema()).start();
                break;
            default:
                break;
        }
        
    }//GEN-LAST:event_jTabbedPane2StateChanged

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        
        jTabbedPane3.setSelectedIndex(1);
        TXTValorPagar.requestFocus();
        TXTValorPagar.requestFocus();
        TXTValorPagar.requestFocus();
        
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void TXTValorPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TXTValorPagarActionPerformed
         (new Thread(new GuardarPago())).start();        // TODO add your handling code here:
    }//GEN-LAST:event_TXTValorPagarActionPerformed

    private void jLabel93ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jLabel93ActionPerformed
        new Thread(new ProcesoDescargaNuevoVersion()).start();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel93ActionPerformed

    private void jTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusGained
        jTextField1.setText("");
    }//GEN-LAST:event_jTextField1FocusGained

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        
    }//GEN-LAST:event_jTextField1KeyPressed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        (new Thread(new CargarTodosPagos())).start();
    }//GEN-LAST:event_jButton4ActionPerformed

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
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ventana().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFrame Afiliados;
    private javax.swing.JTextField Arreglo;
    private javax.swing.JButton BTNRegistrarPago;
    private javax.swing.JFrame Busqueda;
    private javax.swing.JComboBox<String> COMBOActivoAfiliado;
    private javax.swing.JComboBox<String> COMBOBarrio;
    private javax.swing.JComboBox<String> COMBOCiudadCobrador;
    private javax.swing.JComboBox<String> COMBOCiudadResidencia;
    private javax.swing.JComboBox<String> COMBOCiudadVendedores;
    private javax.swing.JComboBox<String> COMBOCobrador;
    private javax.swing.JComboBox<String> COMBODestinoFinalAfiliado;
    private javax.swing.JComboBox<String> COMBOFallecidoAfiliado;
    private javax.swing.JComboBox<String> COMBOFormadePago;
    private javax.swing.JComboBox<String> COMBOParentescoAfiliado;
    private javax.swing.JComboBox<String> COMBOSegurodeVidaAfiliado;
    private javax.swing.JComboBox<String> COMBOTipoBeneficiario;
    private javax.swing.JComboBox<String> COMBOTipoUsuario;
    private javax.swing.JComboBox<String> COMBOTrasladosAfiliado;
    private javax.swing.JComboBox<String> COMBOVendedor;
    private javax.swing.JTextField CafeteriaD;
    private javax.swing.JTextField CafeteriaN;
    private javax.swing.JComboBox<String> Cementerio;
    private javax.swing.JTextField CementerioDe;
    private javax.swing.JTextField Ciudad;
    private javax.swing.JTextField CodCiudad;
    private javax.swing.JTextField Conductor;
    private javax.swing.JComboBox<String> ContratoActivo;
    private javax.swing.JTextField ContratosTraslados;
    private javax.swing.JTextField Cortejo;
    private javax.swing.JComboBox<String> Cremacion;
    private javax.swing.JTextField Direccion;
    private javax.swing.JTextField DireccionCobrador;
    private javax.swing.JComboBox<String> EtapasS;
    private com.toedter.calendar.JDateChooser FECHAActivo;
    private com.toedter.calendar.JDateChooser FECHADerechosAfiliado;
    private javax.swing.JTextField FECHADerechosAfiliado2;
    private com.toedter.calendar.JDateChooser FECHAFallecidoAfiliado;
    private com.toedter.calendar.JDateChooser FECHAFechaContrato;
    private com.toedter.calendar.JDateChooser FECHAInactividadContrato;
    private com.toedter.calendar.JDateChooser FECHAPagoDesde;
    private com.toedter.calendar.JDateChooser FECHAPagoHasta;
    private com.toedter.calendar.JDateChooser FECHARegistroPago;
    private com.toedter.calendar.JDateChooser FECHATrasladoAfiliados;
    private com.toedter.calendar.JDateChooser FECHATrasladoAfiliados2;
    private javax.swing.JTextField FechaH;
    private javax.swing.JTextField IdCobrador;
    private javax.swing.JTextField IdUsuario;
    private javax.swing.JTextField IdVendedor;
    private javax.swing.JTextField Iglesia;
    public static final javax.swing.JLabel LABELVersion = new javax.swing.JLabel();
    public static final javax.swing.JLabel LABELVersion1 = new javax.swing.JLabel();
    private javax.swing.JTextField LDefuncion;
    private javax.swing.JTextField NickUsuario;
    private javax.swing.JTextField NombreCiudad;
    private javax.swing.JTextField NombresCobrador;
    private javax.swing.JTextField NombresUsuarios;
    private javax.swing.JTextField NombresVendedor;
    private javax.swing.JTextField Numero;
    private javax.swing.JTextArea Observaciones;
    private javax.swing.JFrame Ordenes;
    private javax.swing.JTextField PassUsuario;
    private javax.swing.JTextField RealizaTraslado;
    private javax.swing.JTable TABLAAfiliados;
    private javax.swing.JTable TABLABusClientes;
    private javax.swing.JTable TABLACiudades;
    private javax.swing.JTable TABLACobradores;
    private javax.swing.JTable TABLAFuneraria;
    private javax.swing.JTable TABLAPagos;
    private javax.swing.JTable TABLAParentescos;
    private javax.swing.JTable TABLAUsuariosSistema;
    private javax.swing.JTable TABLAVendedores;
    private javax.swing.JComboBox<String> TMuerte;
    private javax.swing.JTextArea TXTAREAObservaciones;
    private javax.swing.JTextField TXTBusqueda;
    private javax.swing.JTextField TXTCedula;
    private javax.swing.JTextField TXTCodAfiliado;
    private javax.swing.JTextField TXTCodCliente;
    private javax.swing.JTextField TXTCodClienteAfiliado;
    private javax.swing.JTextField TXTCodFuneraria;
    private javax.swing.JTextField TXTCodParentesco;
    private javax.swing.JTextField TXTCuotasNulas;
    private javax.swing.JTextField TXTDireccion;
    private javax.swing.JTextField TXTEdadAfiliado;
    private javax.swing.JTextField TXTFuneraria;
    private javax.swing.JTextField TXTIdAfiliado;
    private javax.swing.JTextField TXTNombresAfiliado;
    private javax.swing.JTextField TXTNumContrato;
    private javax.swing.JTextField TXTNumPago;
    private javax.swing.JTextArea TXTObservaciones;
    private javax.swing.JTextField TXTParentesco;
    private javax.swing.JTextField TXTSegurodeVidaAfiliado;
    private javax.swing.JTextField TXTTelefono;
    private javax.swing.JTextField TXTTotalPagos;
    private javax.swing.JTextField TXTTrasladosAfiliado;
    private javax.swing.JTextField TXTValorPagar;
    private javax.swing.JTextField TXTValordePago;
    private javax.swing.JTextField TablasN;
    private javax.swing.JTextField Tanatologo;
    private javax.swing.JTextField TelefonoCobrador;
    private javax.swing.JTextField TelefonoVendedor;
    private javax.swing.JComboBox<String> Tipo;
    private javax.swing.JComboBox<String> Traslado;
    private javax.swing.JTextField TrasladoA;
    private javax.swing.JTextField TrasladoDe;
    private javax.swing.JTextField Velacion;
    private javax.swing.JFrame VentanaProgresoDescargaNuevaVersion;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JButton jLabel93;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
