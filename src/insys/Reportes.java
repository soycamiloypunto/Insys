/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package insys;

import static insys.Ventana.log;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author cktv
 */
public class Reportes extends javax.swing.JFrame {

    private Vector rowData;
    public SimpleDateFormat FormatoFecha = new SimpleDateFormat("yyyy-MM-dd");
    String Ruta;
    Calendar c1 = Calendar.getInstance();
    String Dia = Integer.toString(c1.get(5));
    String Mes = Integer.toString(c1.get(2) + 1);
    String Anio = Integer.toString(c1.get(1));
    String InicioAnio = Integer.toString(c1.get(1)-1);
    String FechaActual = "" + Anio + "-" + Mes + "-" + Dia + "";
    String FechaInicioAnio = "" + InicioAnio + "-" + Mes + "-" + Dia + "";
    java.util.Date FechaA;

    
    /**
     * Creates new form Reportes
     */
    public Reportes() {
        initComponents();
        setIcon();
        FechActual();
    }
    
    private void setIcon() {
    setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
    Contratos.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
    Muertes.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
    Pagos.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
    ClientesActivos.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
    ClientesAtrasados.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
  }
    
    public void CargarReporte_Contratos(){
    Access_connection AC=new Access_connection();
    Contratos.setVisible(true);
    Contratos.setLocationRelativeTo(null);
    
    DefaultTableModel modelo = (DefaultTableModel) TablaContrato.getModel();
    
    int NumFilas = modelo.getRowCount();
    for (int i = 0; NumFilas > i; i++)
      modelo.removeRow(0); 
    
    int Suma=0;
    int i=0;
        try {
            Statement stmt = AC.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT C.cod_cliente AS CCliente,  "
                    + "A.Nombres AS NomAfiliado, C.fecha_contrato AS FechaRegistro FROM Contratos C "
                    + "INNER JOIN Afiliados A ON A.ncontratovsafiliados=C.cod_cliente "
                    + "WHERE C.fecha_contrato>='"+FormatoFecha.format(Inicial.getDate())+"' "
                            + "AND C.fecha_contrato<='"+FormatoFecha.format(Final.getDate())+"' AND "
                            + "A.titular_beneficiario=1");
            while (rs.next()) {
                modelo.addRow(rowData);
                TablaContrato.setValueAt(rs.getString("CCliente"), i, 0);
                TablaContrato.setValueAt(rs.getString("NomAfiliado"), i, 1);
                TablaContrato.setValueAt(rs.getString("FechaRegistro"), i, 2);
                
                i++;
                Suma++;
            } 
      
        } catch (SQLException ex) {
          System.out.println("Error al cargar ordenservicio: "+ex);
        } 
        AC.desconectar();
        
        LabelTotal.setText(String.valueOf(Suma));
    }
    
    public void CargarReporte_Muertes(){
    Access_connection AC=new Access_connection();
    Muertes.setVisible(true);
    Muertes.setLocationRelativeTo(null);
    
    DefaultTableModel modelo = (DefaultTableModel) TablaContrato1.getModel();
    
    int NumFilas = modelo.getRowCount();
    for (int i = 0; NumFilas > i; i++)
      modelo.removeRow(0); 
    
    int Suma=0;
    int i=0;
        try {
            Statement stmt = AC.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT C.cod_cliente AS CCliente,  "
                    + "A.Nombres AS NomAfiliado, C.fecha_contrato AS FechaRegistro, A.fecha_fallecimiento AS "
                    + "FFallecimiento FROM Contratos C "
                    + "INNER JOIN Afiliados A ON A.ncontratovsafiliados=C.cod_cliente "
                    + "WHERE A.fecha_fallecimiento>='"+FormatoFecha.format(Inicial1.getDate())+"' "
                    + "AND A.fecha_fallecimiento<='"+FormatoFecha.format(Final1.getDate())+"' AND "
                    + "A.fallecido='Si'");
            while (rs.next()) {
                modelo.addRow(rowData);
                TablaContrato1.setValueAt(rs.getString("CCliente"), i, 0);
                TablaContrato1.setValueAt(rs.getString("NomAfiliado"), i, 1);
                TablaContrato1.setValueAt(rs.getString("FechaRegistro"), i, 2);
                TablaContrato1.setValueAt(rs.getString("FFallecimiento"), i, 3);
                
                i++;
                Suma++;
            } 
      
        } catch (SQLException ex) {
          System.out.println("Error al cargar ordenservicio: "+ex);
        } 
        AC.desconectar();
        
        LabelTotal1.setText(String.valueOf(Suma));
    }

    public void CargarReporte_Pagos(){
    Access_connection AC=new Access_connection();
    Pagos.setVisible(true);
    Pagos.setLocationRelativeTo(null);
    CargarDesplegableCobradores();
    
    DefaultTableModel modelo = (DefaultTableModel) TablaContrato2.getModel();
    
    int NumFilas = modelo.getRowCount();
    for (int i = 0; NumFilas > i; i++)
      modelo.removeRow(0); 
    
    int Suma=0;
    int i=0;
        try {
            Statement stmt = AC.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT P.codcontratovspagos AS CCLiente, A.Nombres AS Titular, "
                    + "SUM(P.valor_a_pagar) AS VPagar, MAX(P.hasta) AS FHasta FROM Pagos P "
                    + "INNER JOIN Contratos C ON C.cod_cliente=P.codcontratovspagos "
                    + "LEFT JOIN Afiliados A ON A.ncontratovsafiliados=P.codcontratovspagos "
                    + "WHERE P.fregistro>='"+FormatoFecha.format(Inicial2.getDate())+"' "
                            + "AND P.fregistro<='"+FormatoFecha.format(Final2.getDate())+"' "
                    + "AND P.depositante='"+COMBOCobrador.getSelectedItem().toString()+"' "
                            + "AND A.titular_beneficiario=1  "
                    + "GROUP BY P.codcontratovspagos ");
            while (rs.next()) {
                modelo.addRow(rowData);
                TablaContrato2.setValueAt(rs.getInt("CCLiente"), i, 0);
                TablaContrato2.setValueAt(rs.getString("Titular"), i, 1);
                TablaContrato2.setValueAt(rs.getInt("VPagar"), i, 2);
                TablaContrato2.setValueAt(rs.getDate("FHasta"), i, 3);
                
                i++;
                Suma+=rs.getInt("VPagar");
            } 
      
        } catch (SQLException ex) {
          System.out.println("Error al cargar ordenservicio: "+ex);
        } 
        AC.desconectar();
        
        LabelTotal2.setText(String.valueOf(Suma));
    }
    
    public void CargarReporte_ClientesAtrasados(){
      
    Access_connection AC=new Access_connection();
    
    
    DefaultTableModel modelo = (DefaultTableModel) TablaContrato3.getModel();
    
    int NumFilas = modelo.getRowCount();
    for (int i = 0; NumFilas > i; i++)
      modelo.removeRow(0); 
    
    int Suma=0;
    int i=0;
        try {
            Statement stmt = AC.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT C.cod_cliente AS CCliente,  A.Nombres AS NomAfiliado, "
                    + "C.fecha_contrato AS FechaRegistro, MAX(P.Hasta) AS PHasta, "
                    + "TIMESTAMPDIFF(MONTH, MAX(P.Hasta), NOW()) AS NumMeses "
                    + "FROM Contratos C "
                    + "INNER JOIN Afiliados A ON A.ncontratovsafiliados=C.cod_cliente "
                    + "INNER JOIN Pagos P ON P.codcontratovspagos=C.cod_cliente WHERE A.titular_beneficiario=1 "
                    + "AND C.contratoactivo='Activo' AND P.Hasta <=NOW() GROUP BY C.cod_cliente ");
            while (rs.next()) {
                modelo.addRow(rowData);
                TablaContrato3.setValueAt(rs.getInt("CCLiente"), i, 0);
                TablaContrato3.setValueAt(rs.getString("NomAfiliado"), i, 1);
                TablaContrato3.setValueAt(rs.getDate("FechaRegistro"), i, 2);
                TablaContrato3.setValueAt(rs.getDate("PHasta"), i, 3);
                TablaContrato3.setValueAt(rs.getInt("NumMeses"), i, 4);
                
                i++;
                Suma+=1;
            } 
      
        } catch (SQLException ex) {
          System.out.println("Error al cargar Reporte Atrasados: "+ex);
        } 
        AC.desconectar();
        
        LabelTotal3.setText(String.valueOf(Suma));
    }
    
    public void CargarReporte_ClientesActivos(){
        
        Access_connection AC=new Access_connection();
    
    
        DefaultTableModel modelo = (DefaultTableModel) TablaContrato4.getModel();

        int NumFilas = modelo.getRowCount();
        for (int i = 0; NumFilas > i; i++)
          modelo.removeRow(0); 

        int Suma=0;
        int i=0;
            try {
                Statement stmt = AC.conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT C.cod_cliente AS CCliente,  A.Nombres AS NomAfiliado, "
                        + "C.contratoactivo AS CActivo FROM Contratos C "
                        + "INNER JOIN Afiliados A ON A.ncontratovsafiliados=C.cod_cliente "
                        + "WHERE A.titular_beneficiario=1 AND C.contratoactivo='Activo' "
                        + "GROUP BY C.cod_cliente ");
                while (rs.next()) {
                    
                    
                    modelo.addRow(rowData);
                    TablaContrato4.setValueAt(rs.getInt("CCLiente"), i, 0);
                    TablaContrato4.setValueAt(rs.getString("NomAfiliado"), i, 1);
                    TablaContrato4.setValueAt(rs.getString("CActivo"), i, 2);

                    i++;
                    Suma+=1;
                } 

            } catch (SQLException ex) {
              System.out.println("Error al cargar ordenservicio: "+ex);
            } 
            AC.desconectar();

            LabelTotal4.setText(String.valueOf(Suma));
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
    
    private void ExportaraExcel_Contratos(){
        
        try {
            //Agrego 3 filas con los totales de los puntajes
            
            java.util.List<JTable> tb = new ArrayList<JTable>();
            java.util.List<Integer> NumC = new ArrayList<Integer>();
            tb.add(TablaContrato);
            NumC.add(2);
            
            //-------------------
            export_excel1 excelExporter = new export_excel1(tb, new File(Ruta+".xls"), NumC);
            if (excelExporter.export()) {
                JOptionPane.showMessageDialog(null, "Se ha guardado el reporte");
                AbrirAdministradordeArchivos();
            }
        } catch (Exception ex) {
            System.out.println("Algo Falló al guardar el excel: "+ex);
        }
        
    }
    
    
    private void FechActual() {
    Calendar c1 = Calendar.getInstance();
    String Dia = Integer.toString(c1.get(5));
    String Mes = Integer.toString(c1.get(2) + 1);
    String Anio = Integer.toString(c1.get(1));
    String FechaActual = "" + Dia + "/" + Mes + "/" + Anio + "";
    System.out.println(FechaActual);
    SimpleDateFormat Formato = new SimpleDateFormat("yyyy-MM-dd");
    try {
      FechaA = Formato.parse(FechaActual);
      System.out.println("Cargada Fecha Actual Contratos");
    } catch (ParseException ex) {
      log.warn("Hubo en error al X por que: "+ex);
    } 
  }
  
    public void CargarHiloReporte_Pagos(){
        Pagos.setVisible(true);
        Pagos.setLocationRelativeTo(null);
        CargarDesplegableCobradores();
        
    }
    
    private class CargarReporte_Pagos implements Runnable {
    private CargarReporte_Pagos() {}
    
    public void run() {
            BTNCargarReporte4.setEnabled(false);
            BTNCargarReporte4.setText("Generando Información, Por favor espere...");
            
            CargarReporte_Pagos();
            
            BTNCargarReporte4.setEnabled(true);
            BTNCargarReporte4.setText("Cargar Reporte");
        }
      }
    
    public void CargarReporte_ClientesA(){
        new Thread(new CargarReporte_ClientesAtrasados()).start();  
    }
    
    private class CargarReporte_ClientesAtrasados implements Runnable {
    private CargarReporte_ClientesAtrasados() {}
    
    public void run() {
            jLabel8.setEnabled(false);
            jLabel8.setText("Generando Información el volúmen de datos es muy alto, puede tardar varios minutos. \n"
                    + "Minimice esta ventana mientras carga y no cierre la aplicación\n, Por favor espere...");
            
            ClientesAtrasados.setVisible(true);
            ClientesAtrasados.setLocationRelativeTo(null);
            
            CargarReporte_ClientesAtrasados();
            
            jLabel8.setEnabled(true);
            jLabel8.setText("Cargar Reporte");
        }
      }
    
    public void CargarReporte_ClientesAct(){
        new Thread(new CargarReporte_ClientesActivos()).start();  
    }
    
    private class CargarReporte_ClientesActivos implements Runnable {
    private CargarReporte_ClientesActivos() {}
    
    public void run() {
            jLabel9.setEnabled(false);
            jLabel9.setText("Generando Información el volúmen de datos es muy alto, puede tardar varios minutos. \n"
                    + "Minimice esta ventana mientras carga y no cierre la aplicación\n, Por favor espere...");
            
            ClientesActivos.setVisible(true);
            ClientesActivos.setLocationRelativeTo(null);
            
            CargarReporte_ClientesActivos();
            
            jLabel9.setEnabled(true);
            jLabel9.setText("Cargar Reporte");
        }
      }
    
    
    
    private void ExportaraExcel_Muertes(){
        
        try {
            //Agrego 3 filas con los totales de los puntajes
            
            java.util.List<JTable> tb = new ArrayList<JTable>();
            java.util.List<Integer> NumC = new ArrayList<Integer>();
            tb.add(TablaContrato1);
            NumC.add(3);
            
            //-------------------
            export_excel1 excelExporter = new export_excel1(tb, new File(Ruta+".xls"), NumC);
            if (excelExporter.export()) {
                JOptionPane.showMessageDialog(null, "Se ha guardado el reporte");
                AbrirAdministradordeArchivos();
            }
        } catch (Exception ex) {
            System.out.println("Algo Falló al guardar el excel: "+ex);
        }
        
    }
    
    private void ExportaraExcel_Pagos(){
        
        try {
            //Agrego 3 filas con los totales de los puntajes
            
            java.util.List<JTable> tb = new ArrayList<JTable>();
            java.util.List<Integer> NumC = new ArrayList<Integer>();
            tb.add(TablaContrato1);
            NumC.add(3);
            
            //-------------------
            export_excel1 excelExporter = new export_excel1(tb, new File(Ruta+".xls"), NumC);
            if (excelExporter.export()) {
                JOptionPane.showMessageDialog(null, "Se ha guardado el reporte");
                AbrirAdministradordeArchivos();
            }
        } catch (Exception ex) {
            System.out.println("Algo Falló al guardar el excel: "+ex);
        }
        
    }
    
    private void ExportaraExcel_Cobradores(){
        
        try {
            //Agrego 3 filas con los totales de los puntajes
            
            java.util.List<JTable> tb = new ArrayList<JTable>();
            java.util.List<Integer> NumC = new ArrayList<Integer>();
            tb.add(TablaContrato2);
            NumC.add(3);
            
            //-------------------
            export_excel1 excelExporter = new export_excel1(tb, new File(Ruta+".xls"), NumC);
            if (excelExporter.export()) {
                JOptionPane.showMessageDialog(null, "Se ha guardado el reporte");
                AbrirAdministradordeArchivos();
            }
        } catch (Exception ex) {
            System.out.println("Algo Falló al guardar el excel: "+ex);
        }
        
    }
    
    private void ExportaraExcel_Atrasados(){
        
        try {
            //Agrego 3 filas con los totales de los puntajes
            
            java.util.List<JTable> tb = new ArrayList<JTable>();
            java.util.List<Integer> NumC = new ArrayList<Integer>();
            tb.add(TablaContrato3);
            NumC.add(4);
            
            //-------------------
            export_excel1 excelExporter = new export_excel1(tb, new File(Ruta+".xls"), NumC);
            if (excelExporter.export()) {
                JOptionPane.showMessageDialog(null, "Se ha guardado el reporte");
                AbrirAdministradordeArchivos();
            }
        } catch (Exception ex) {
            System.out.println("Algo Falló al guardar el excel: "+ex);
        }
        
    }
    
    private void ExportaraExcel_Activos(){
        
        try {
            //Agrego 3 filas con los totales de los puntajes
            
            java.util.List<JTable> tb = new ArrayList<JTable>();
            java.util.List<Integer> NumC = new ArrayList<Integer>();
            tb.add(TablaContrato4);
            NumC.add(2);
            
            //-------------------
            export_excel1 excelExporter = new export_excel1(tb, new File(Ruta+".xls"), NumC);
            if (excelExporter.export()) {
                JOptionPane.showMessageDialog(null, "Se ha guardado el reporte");
                AbrirAdministradordeArchivos();
            }
        } catch (Exception ex) {
            System.out.println("Algo Falló al guardar el excel: "+ex);
        }
        
    }
    
    private void AbrirAdministradordeArchivos(){
        
        String so = System.getProperty("os.name");
        System.out.println("Sistema Operativo: "+so);
        System.out.println("Ruta de Descarga: "+Ruta+".xls");
        
        if(so.contains("Linux")){
            try {
                Runtime runtime = Runtime.getRuntime();
                Process process = runtime.exec("dde-file-manager "+Ruta+".xls");
            } catch (IOException ex) {
                
            }
        }else if(so.contains("Windows")){
            try {
                Runtime runtime = Runtime.getRuntime();
                Process process = runtime.exec("Explorer "+Ruta+".xls");
            } catch (IOException ex) {
                
            }
        }else if(so.contains("Mac")){
            try {
                Runtime runtime = Runtime.getRuntime();
                Process process = runtime.exec("open "+Ruta+".xls");
            } catch (IOException ex) {
                
            }
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

        Contratos = new javax.swing.JFrame();
        jPanel1 = new javax.swing.JPanel();
        Inicial = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        Final = new com.toedter.calendar.JDateChooser();
        BTNCargarReporte = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TablaContrato = new javax.swing.JTable();
        LabelTotal = new javax.swing.JLabel();
        BTNCargarReporte1 = new javax.swing.JButton();
        Muertes = new javax.swing.JFrame();
        jPanel2 = new javax.swing.JPanel();
        Inicial1 = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        Final1 = new com.toedter.calendar.JDateChooser();
        BTNCargarReporte2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        TablaContrato1 = new javax.swing.JTable();
        LabelTotal1 = new javax.swing.JLabel();
        BTNCargarReporte3 = new javax.swing.JButton();
        Pagos = new javax.swing.JFrame();
        jPanel3 = new javax.swing.JPanel();
        Inicial2 = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        Final2 = new com.toedter.calendar.JDateChooser();
        BTNCargarReporte4 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        TablaContrato2 = new javax.swing.JTable();
        LabelTotal2 = new javax.swing.JLabel();
        BTNCargarReporte5 = new javax.swing.JButton();
        COMBOCobrador = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        ClientesAtrasados = new javax.swing.JFrame();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        TablaContrato3 = new javax.swing.JTable();
        LabelTotal3 = new javax.swing.JLabel();
        BTNCargarReporte7 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        BTNCargarReporte6 = new javax.swing.JButton();
        ClientesActivos = new javax.swing.JFrame();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        TablaContrato4 = new javax.swing.JTable();
        LabelTotal4 = new javax.swing.JLabel();
        BTNCargarReporte8 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();

        Contratos.setMinimumSize(new java.awt.Dimension(776, 500));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("Mes Inicial");

        jLabel2.setText("Mes Final");

        BTNCargarReporte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico_diseno/assets-92.png"))); // NOI18N
        BTNCargarReporte.setText("Cargar Reporte");
        BTNCargarReporte.setToolTipText("");
        BTNCargarReporte.setContentAreaFilled(false);
        BTNCargarReporte.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BTNCargarReporte.setFocusPainted(false);
        BTNCargarReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNCargarReporteActionPerformed(evt);
            }
        });

        TablaContrato.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cód. Cliente", "Titular", "Fecha Registro"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(TablaContrato);
        if (TablaContrato.getColumnModel().getColumnCount() > 0) {
            TablaContrato.getColumnModel().getColumn(1).setMinWidth(400);
            TablaContrato.getColumnModel().getColumn(1).setPreferredWidth(400);
        }

        LabelTotal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        LabelTotal.setText("TOTAL");

        BTNCargarReporte1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico_diseno/assets-83.png"))); // NOI18N
        BTNCargarReporte1.setText("Exportar a Excel");
        BTNCargarReporte1.setToolTipText("");
        BTNCargarReporte1.setContentAreaFilled(false);
        BTNCargarReporte1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BTNCargarReporte1.setFocusPainted(false);
        BTNCargarReporte1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNCargarReporte1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(Inicial, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(Final, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(BTNCargarReporte, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(BTNCargarReporte1, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(LabelTotal)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(Final, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(Inicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(BTNCargarReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LabelTotal, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(BTNCargarReporte1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        javax.swing.GroupLayout ContratosLayout = new javax.swing.GroupLayout(Contratos.getContentPane());
        Contratos.getContentPane().setLayout(ContratosLayout);
        ContratosLayout.setHorizontalGroup(
            ContratosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        ContratosLayout.setVerticalGroup(
            ContratosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        Muertes.setMinimumSize(new java.awt.Dimension(800, 650));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setText("Mes Inicial");

        jLabel4.setText("Mes Final");

        BTNCargarReporte2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico_diseno/assets-92.png"))); // NOI18N
        BTNCargarReporte2.setText("Cargar Reporte");
        BTNCargarReporte2.setToolTipText("");
        BTNCargarReporte2.setContentAreaFilled(false);
        BTNCargarReporte2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BTNCargarReporte2.setFocusPainted(false);
        BTNCargarReporte2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNCargarReporte2ActionPerformed(evt);
            }
        });

        TablaContrato1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cód. Cliente", "Fallecido", "Fecha Contrato", "Fecha Fallecimiento"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(TablaContrato1);

        LabelTotal1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        LabelTotal1.setText("TOTAL");

        BTNCargarReporte3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico_diseno/assets-83.png"))); // NOI18N
        BTNCargarReporte3.setText("Exportar a Excel");
        BTNCargarReporte3.setToolTipText("");
        BTNCargarReporte3.setContentAreaFilled(false);
        BTNCargarReporte3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BTNCargarReporte3.setFocusPainted(false);
        BTNCargarReporte3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNCargarReporte3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(Inicial1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(Final1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(BTNCargarReporte2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(BTNCargarReporte3, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(LabelTotal1)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel4)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(Final1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(Inicial1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(BTNCargarReporte2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LabelTotal1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(BTNCargarReporte3, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        javax.swing.GroupLayout MuertesLayout = new javax.swing.GroupLayout(Muertes.getContentPane());
        Muertes.getContentPane().setLayout(MuertesLayout);
        MuertesLayout.setHorizontalGroup(
            MuertesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        MuertesLayout.setVerticalGroup(
            MuertesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        Pagos.setMinimumSize(new java.awt.Dimension(800, 650));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setText("Mes Inicial");

        jLabel6.setText("Mes Final");

        BTNCargarReporte4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico_diseno/assets-92.png"))); // NOI18N
        BTNCargarReporte4.setText("Cargar Reporte");
        BTNCargarReporte4.setToolTipText("");
        BTNCargarReporte4.setContentAreaFilled(false);
        BTNCargarReporte4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BTNCargarReporte4.setFocusPainted(false);
        BTNCargarReporte4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNCargarReporte4ActionPerformed(evt);
            }
        });

        TablaContrato2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cód. Cliente", "Titular", "Pago", "Hasta"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(TablaContrato2);

        LabelTotal2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        LabelTotal2.setText("TOTAL");

        BTNCargarReporte5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico_diseno/assets-83.png"))); // NOI18N
        BTNCargarReporte5.setText("Exportar a Excel");
        BTNCargarReporte5.setToolTipText("");
        BTNCargarReporte5.setContentAreaFilled(false);
        BTNCargarReporte5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BTNCargarReporte5.setFocusPainted(false);
        BTNCargarReporte5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNCargarReporte5ActionPerformed(evt);
            }
        });

        COMBOCobrador.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel7.setText("Cobrador");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(Inicial2, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Final2, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(COMBOCobrador, 0, 278, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BTNCargarReporte4, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(BTNCargarReporte5, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(LabelTotal2)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel6)
                                .addComponent(jLabel7))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(Final2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(Inicial2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(COMBOCobrador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(BTNCargarReporte4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LabelTotal2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(BTNCargarReporte5, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        javax.swing.GroupLayout PagosLayout = new javax.swing.GroupLayout(Pagos.getContentPane());
        Pagos.getContentPane().setLayout(PagosLayout);
        PagosLayout.setHorizontalGroup(
            PagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        PagosLayout.setVerticalGroup(
            PagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        ClientesAtrasados.setMinimumSize(new java.awt.Dimension(990, 650));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        TablaContrato3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cód. Cliente", "Titular", "Fecha Contrato", "Último Pago", "Número de Meses en Mora"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(TablaContrato3);
        if (TablaContrato3.getColumnModel().getColumnCount() > 0) {
            TablaContrato3.getColumnModel().getColumn(1).setMinWidth(400);
            TablaContrato3.getColumnModel().getColumn(1).setPreferredWidth(400);
        }

        LabelTotal3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        LabelTotal3.setText("TOTAL");

        BTNCargarReporte7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico_diseno/assets-83.png"))); // NOI18N
        BTNCargarReporte7.setText("Exportar a Excel");
        BTNCargarReporte7.setToolTipText("");
        BTNCargarReporte7.setContentAreaFilled(false);
        BTNCargarReporte7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BTNCargarReporte7.setFocusPainted(false);
        BTNCargarReporte7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNCargarReporte7ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Clientes Atrasados");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(BTNCargarReporte7, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(LabelTotal3))
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LabelTotal3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(BTNCargarReporte7, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        javax.swing.GroupLayout ClientesAtrasadosLayout = new javax.swing.GroupLayout(ClientesAtrasados.getContentPane());
        ClientesAtrasados.getContentPane().setLayout(ClientesAtrasadosLayout);
        ClientesAtrasadosLayout.setHorizontalGroup(
            ClientesAtrasadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        ClientesAtrasadosLayout.setVerticalGroup(
            ClientesAtrasadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        BTNCargarReporte6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico_diseno/assets-92.png"))); // NOI18N
        BTNCargarReporte6.setText("Cargar Reporte");
        BTNCargarReporte6.setToolTipText("");
        BTNCargarReporte6.setContentAreaFilled(false);
        BTNCargarReporte6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BTNCargarReporte6.setFocusPainted(false);
        BTNCargarReporte6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNCargarReporte6ActionPerformed(evt);
            }
        });

        ClientesActivos.setMinimumSize(new java.awt.Dimension(1000, 500));
        ClientesActivos.setPreferredSize(new java.awt.Dimension(1000, 500));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        TablaContrato4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cód. Cliente", "Titular", "Activo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(TablaContrato4);
        if (TablaContrato4.getColumnModel().getColumnCount() > 0) {
            TablaContrato4.getColumnModel().getColumn(1).setMinWidth(400);
            TablaContrato4.getColumnModel().getColumn(1).setPreferredWidth(400);
        }

        LabelTotal4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        LabelTotal4.setText("TOTAL");

        BTNCargarReporte8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ico_diseno/assets-83.png"))); // NOI18N
        BTNCargarReporte8.setText("Exportar a Excel");
        BTNCargarReporte8.setToolTipText("");
        BTNCargarReporte8.setContentAreaFilled(false);
        BTNCargarReporte8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BTNCargarReporte8.setFocusPainted(false);
        BTNCargarReporte8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNCargarReporte8ActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Clientes Activos - Inactivos");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(BTNCargarReporte8, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(LabelTotal4))
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LabelTotal4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(BTNCargarReporte8, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        javax.swing.GroupLayout ClientesActivosLayout = new javax.swing.GroupLayout(ClientesActivos.getContentPane());
        ClientesActivos.getContentPane().setLayout(ClientesActivosLayout);
        ClientesActivosLayout.setHorizontalGroup(
            ClientesActivosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        ClientesActivosLayout.setVerticalGroup(
            ClientesActivosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 776, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BTNCargarReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNCargarReporteActionPerformed
        if(Inicial.getDate()==null || Final.getDate()==null){
            JOptionPane.showMessageDialog(null, "Seleccione las fechas a calcular");
        }else{
            try {
                CargarReporte_Contratos();
            } catch (java.lang.NullPointerException e) {
            }
            
        }
    }//GEN-LAST:event_BTNCargarReporteActionPerformed

    private void BTNCargarReporte1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNCargarReporte1ActionPerformed
        JFileChooser dialog = new JFileChooser();
        int opcion = dialog.showSaveDialog(this);

        if(opcion == JFileChooser.APPROVE_OPTION){

            File dir = dialog.getSelectedFile();
            Ruta = dir.toString();

            if(Ruta.equals("")){
                JOptionPane.showMessageDialog(null, "Primero Seleccione donde desea guardar el reporte");
            }else{

                ExportaraExcel_Contratos();        
                
            }
        }        // TODO add your handling code here:
    }//GEN-LAST:event_BTNCargarReporte1ActionPerformed

    private void BTNCargarReporte2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNCargarReporte2ActionPerformed
        CargarReporte_Muertes();
    }//GEN-LAST:event_BTNCargarReporte2ActionPerformed

    private void BTNCargarReporte3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNCargarReporte3ActionPerformed
        JFileChooser dialog = new JFileChooser();
        int opcion = dialog.showSaveDialog(this);

        if(opcion == JFileChooser.APPROVE_OPTION){

            File dir = dialog.getSelectedFile();
            Ruta = dir.toString();

            if(Ruta.equals("")){
                JOptionPane.showMessageDialog(null, "Primero Seleccione donde desea guardar el reporte");
            }else{

                ExportaraExcel_Muertes();        
                
            }
        }      
    }//GEN-LAST:event_BTNCargarReporte3ActionPerformed

    private void BTNCargarReporte4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNCargarReporte4ActionPerformed
        
        new Thread(new CargarReporte_Pagos()).start();  
    }//GEN-LAST:event_BTNCargarReporte4ActionPerformed

    private void BTNCargarReporte5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNCargarReporte5ActionPerformed
        JFileChooser dialog = new JFileChooser();
        int opcion = dialog.showSaveDialog(this);

        if(opcion == JFileChooser.APPROVE_OPTION){

            File dir = dialog.getSelectedFile();
            Ruta = dir.toString();

            if(Ruta.equals("")){
                JOptionPane.showMessageDialog(null, "Primero Seleccione donde desea guardar el reporte");
            }else{

                ExportaraExcel_Cobradores();        
                
            }
        }  
    }//GEN-LAST:event_BTNCargarReporte5ActionPerformed

    private void BTNCargarReporte6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNCargarReporte6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BTNCargarReporte6ActionPerformed

    private void BTNCargarReporte7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNCargarReporte7ActionPerformed
        JFileChooser dialog = new JFileChooser();
        int opcion = dialog.showSaveDialog(this);

        if(opcion == JFileChooser.APPROVE_OPTION){

            File dir = dialog.getSelectedFile();
            Ruta = dir.toString();

            if(Ruta.equals("")){
                JOptionPane.showMessageDialog(null, "Primero Seleccione donde desea guardar el reporte");
            }else{

                ExportaraExcel_Atrasados();      
                
            }
        }          // TODO add your handling code here:
    }//GEN-LAST:event_BTNCargarReporte7ActionPerformed

    private void BTNCargarReporte8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNCargarReporte8ActionPerformed
        JFileChooser dialog = new JFileChooser();
        int opcion = dialog.showSaveDialog(this);

        if(opcion == JFileChooser.APPROVE_OPTION){

            File dir = dialog.getSelectedFile();
            Ruta = dir.toString();

            if(Ruta.equals("")){
                JOptionPane.showMessageDialog(null, "Primero Seleccione donde desea guardar el reporte");
            }else{

                ExportaraExcel_Activos();      
                
            }
        }          // TODO add your handling code here:        // TODO add your handling code here:
    }//GEN-LAST:event_BTNCargarReporte8ActionPerformed

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
            java.util.logging.Logger.getLogger(Reportes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Reportes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Reportes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Reportes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Reportes().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTNCargarReporte;
    private javax.swing.JButton BTNCargarReporte1;
    private javax.swing.JButton BTNCargarReporte2;
    private javax.swing.JButton BTNCargarReporte3;
    private javax.swing.JButton BTNCargarReporte4;
    private javax.swing.JButton BTNCargarReporte5;
    private javax.swing.JButton BTNCargarReporte6;
    private javax.swing.JButton BTNCargarReporte7;
    private javax.swing.JButton BTNCargarReporte8;
    private javax.swing.JComboBox<String> COMBOCobrador;
    private javax.swing.JFrame ClientesActivos;
    private javax.swing.JFrame ClientesAtrasados;
    private javax.swing.JFrame Contratos;
    private com.toedter.calendar.JDateChooser Final;
    private com.toedter.calendar.JDateChooser Final1;
    private com.toedter.calendar.JDateChooser Final2;
    private com.toedter.calendar.JDateChooser Inicial;
    private com.toedter.calendar.JDateChooser Inicial1;
    private com.toedter.calendar.JDateChooser Inicial2;
    private javax.swing.JLabel LabelTotal;
    private javax.swing.JLabel LabelTotal1;
    private javax.swing.JLabel LabelTotal2;
    private javax.swing.JLabel LabelTotal3;
    private javax.swing.JLabel LabelTotal4;
    private javax.swing.JFrame Muertes;
    private javax.swing.JFrame Pagos;
    private javax.swing.JTable TablaContrato;
    private javax.swing.JTable TablaContrato1;
    private javax.swing.JTable TablaContrato2;
    private javax.swing.JTable TablaContrato3;
    private javax.swing.JTable TablaContrato4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    // End of variables declaration//GEN-END:variables
}
