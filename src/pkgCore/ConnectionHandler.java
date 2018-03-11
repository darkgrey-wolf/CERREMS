/*
 *This class is instantiated to connect to database
 */
package pkgCore;
import java.io.IOException;
import java.sql.*;
import java.util.Formatter;

/**
 *
 * @author GreyWolf
 */
public class ConnectionHandler {
    private Connection connection;
    public ConnectionHandler(boolean in) throws SQLException, ClassNotFoundException, IOException{
        Configuration config = new Configuration();
        Class.forName("com.mysql.jdbc.Driver");
        Formatter frm = new Formatter();
        String strCon;
        frm.format("jdbc:mysql://%s:%s/%s?useSSL=%b",config.getServerIp(),
                                                     config.getServerPort(),
                                                     config.getDbName(),
                                                     config.isUseSSL());
        strCon = frm.toString();
        connection = DriverManager.getConnection(strCon,config.getUserName(),config.getPassword());
        connection.setAutoCommit(in);
    }
    public Connection getConnection(){
        return this.connection;
    }
}
