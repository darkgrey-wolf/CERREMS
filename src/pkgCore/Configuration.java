package pkgCore;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author GreyWolf
 */
public class Configuration {
    
    private Properties p;
    private String strServerIp;
    private String strServerDir;
    private int iPort;
    private String strDbName;
    private String strUserName;
    private String strPassword;
    private boolean bUseSSL;
    private String strComPort;

    public Configuration() throws IOException {
        p = new Properties();
        FileInputStream fin;
        try {
            fin = new FileInputStream("config.xml");         
            //File already exist           
            p.loadFromXML(fin);
            this.strServerIp = p.getProperty(Configuration.KEY_SERVER_IP);
            this.strServerDir = p.getProperty(Configuration.KEY_SERVER_DIR);
            this.iPort = Integer.parseInt(p.getProperty(Configuration.KEY_SERVERPORT));//uncaught number format execption
            this.strDbName = p.getProperty(Configuration.KEY_DBNAME);
            this.strUserName = p.getProperty(Configuration.KEY_SERVERUSER);
            this.strPassword = p.getProperty(Configuration.KEY_PASSWORD);
            this.bUseSSL = Boolean.parseBoolean(p.getProperty(Configuration.KEY_USESSL));
            this.strComPort = p.getProperty(Configuration.KEY_COMPORT);

            fin.close();
        } catch (FileNotFoundException ex) {
            
            //File does not exist use default
            this.strServerIp = Configuration.DEFAULT_SERVER_IP;
            this.strServerDir = Configuration.DEFAULT_SERVER_DIR;
            this.iPort = Configuration.DEFAULT_SPORT;
            this.strDbName = Configuration.DEFAULT_DBNAME;
            this.strUserName = Configuration.DEFAULT_USERNAME;
            this.strPassword = Configuration.DEFAULT_PASSWORD;
            this.bUseSSL = Configuration.DEFAULT_SSL;
            this.strComPort = Configuration.DEFAULT_COM;
            save();
        } catch (IOException ex) {
           throw new IOException("Unable to load property: " + ex.getMessage());
        }
        
        
    }
    
    public final void setServerIp(String value){
        this.strServerIp = value;
    }
    public final String getServerIp(){
        return this.strServerIp;
    }
    public final void setServerDir(String value){
        this.strServerDir = value;
    }
    public final String getServerDir(){
        return this.strServerDir;
    }
    public final void setServerPort(int value){
        this.iPort = value;
    }
    public final int getServerPort(){
        return this.iPort;
    }
    public final void setDbName(String value){
        this.strDbName = value;
    }
    public final String getDbName(){
        return this.strDbName;
    }
    public final void setUserName(String value){
        this.strUserName = value;
    }
    public final String getUserName(){
        return this.strUserName;
    }
    public final void setPassword(String value){
        this.strPassword = value;
    }
    public final String getPassword(){
        return this.strPassword;
    }
    public final void useSSL(boolean value){
        this.bUseSSL = value;
    }
    public final boolean isUseSSL(){
        return this.bUseSSL;
    }
    public final void setComPort(String value){
        this.strComPort = value;
    }
    public final String getComPort(){
        return this.strComPort;
    }
    public final void save() throws IOException{
        try {
            FileOutputStream fout = new FileOutputStream("config.xml");
            p.setProperty(Configuration.KEY_SERVER_IP,this.strServerIp);
            p.setProperty(Configuration.KEY_SERVER_DIR, this.strServerDir);
            p.setProperty(Configuration.KEY_SERVERPORT,Integer.toString(this.iPort));
            p.setProperty(Configuration.KEY_SERVERUSER,this.strUserName);
            p.setProperty(Configuration.KEY_DBNAME,this.strDbName);
            p.setProperty(Configuration.KEY_COMPORT, this.strComPort);
            p.setProperty(Configuration.KEY_PASSWORD,this.strPassword);
            p.setProperty(Configuration.KEY_USESSL,Boolean.toString(this.bUseSSL));
            p.storeToXML(fout,"Configuration");
            fout.close();
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException("Unable to create file: " + ex.getMessage());
        } catch (IOException ex) {
            throw new IOException("Unable to store configuration settings: " + ex.getMessage());
        }
    }
    
    private final static String DEFAULT_SERVER_IP = "localhost";
    private final static String DEFAULT_SERVER_DIR = "C:\\wamp64\\www\\ravenfx\\data\\";
    private final static String DEFAULT_DBNAME = "db_raven";
    private final static String DEFAULT_USERNAME = "user_admin";
    private final static String DEFAULT_PASSWORD = "admin";
    private final static String DEFAULT_COM = "COM6";
    private final static int DEFAULT_SPORT = 3306;
    private final static boolean DEFAULT_SSL = false;
    
    
    public final static String KEY_SERVER_IP = "SERVER_IP";
    public final static String KEY_SERVER_DIR = "SERVER_DIR";
    public final static String KEY_SERVERPORT = "SPORT";
    public final static String KEY_SERVERUSER = "USER";
    public final static String KEY_DBNAME = "DBNAME";
    public final static String KEY_COMPORT = "CPORT";
    public final static String KEY_PASSWORD = "PASSWORD"; 
    public final static String KEY_USESSL = "SSL";
}
