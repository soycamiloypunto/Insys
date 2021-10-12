package insys;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Access_connection_SQLITE //Conexion a Base Local
{
    Connection conn = null;
    
    //Base de Pruebas: alistami_pruebas
    //Base Pública: alistami_database
    
    //String servidor="jdbc:mysql://grupoinnovate.co:3306/grupoin2_ico";
    
    static String servidor;
    String user= "grupoin2_innofort"; //Nombre de Usuario
    String pass="INNOFort2018"; //Clave de Usuario
    String driver= "com.mysql.jdbc.Driver";
    public static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Access_connection_SQLITE.class);
    
    
    public Access_connection_SQLITE(){
       //Verifico que haya conexión con la Base de Datos y si no existe reviento la conexión.
        
        try{
            Class.forName("org.sqlite.JDBC");
                conn=DriverManager.getConnection(servidor);//establezco la conexión
            }catch(SQLException ex){
                //Autogenerado
                log.warn("No FuPosible conectarse a sqlite: "+ex);
            } catch (ClassNotFoundException ex) {
                log.warn("No FuPosible conectarse a sqlite: "+ex);
        }
        
    }
  
    public synchronized Connection getConnection(){
        return this.conn;
    }

    public void desconectar(){
        try {
            conn.close();
        } catch (SQLException ex) {
            log.warn("No FuPosible cerrar la conexión sqlite: "+ex);
        }
    } 
  
}