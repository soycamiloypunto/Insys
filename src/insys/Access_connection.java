package insys;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Access_connection {
  Connection conn = null;
  static String TipoConexion;
  static String servidor="jdbc:mysql://funeralescasasagrada.com:3306/funeral1_insys";
  //String user = "funeral1_usuario";
  //String pass = "Usuario16";
  String user = "funeral1_insysuser";
  String pass = "insysuser2021";
  public static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Access_connection.class);
  
  public Access_connection() {
    if (TipoConexion.equals("Internet")) {
      try {
        //Class.forName(this.driver);
        this.conn = DriverManager.getConnection(servidor, this.user, this.pass);
      } catch (SQLException e) {
        log.warn("No FuPosible conectarse a MYSQL: "+e);
      } 
    } else {
      try {
        Class.forName("org.sqlite.JDBC");
        this.conn = DriverManager.getConnection(servidor, this.user, this.pass);
      } catch (SQLException ex) {
        log.warn("No Fué Posible conectarse a sqlite: "+ex);
      } catch (ClassNotFoundException ex) { 
          log.warn("No Fué Posible conectarse a sqlite: "+ex);
            
        } 
    } 
  }
  
  public synchronized Connection getConnection() {
    return this.conn;
  }
  
  public void desconectar() {
    try {
      this.conn.close();
      log.warn("Se ha Cerrado la Conexicon MYSQLSERVER");
    } catch (SQLException ex) {
      log.warn("No FuPosible Cerrar la Conexicon MYSQLSERVER");
      Logger.getLogger(Access_connection.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
  }
}