package pkgDesign;

import gnu.io.CommPortIdentifier;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pkgCore.Configuration;
import pkgExceptions.NoInputException;
import pkgUtility.DialogInfo;

/**
 * FXML Controller class
 *
 * @author GreyWolf
 */
public class Controller_Configuration implements Initializable {

    @FXML
    private TextField txfIp;
    @FXML
    private TextField txfDir;
    @FXML
    private TextField txfPort;
    @FXML
    private TextField txfDbName;
    @FXML
    private TextField txfUserName;
    @FXML
    private PasswordField txfPassword;
    @FXML
    private CheckBox chkSSL;
    @FXML
    private ComboBox<String> cmbComPort;
    @FXML
    private AnchorPane btnBrowse;
    @FXML
    private AnchorPane btnDiscard;
    @FXML
    private AnchorPane btnSave;
    
    
    //NON FXML VARIABLES
    private Stage stgStage;
    private Configuration config;
    //CUSTOM FUNCTIONS
    public void initStage(Stage value){
        this.stgStage = value;
    }
    
    //FXML EVENT HANDLES
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            config = new Configuration();
            //POPULATE TEXTFIELD
            this.txfIp.setText(config.getServerIp());
            this.txfDir.setText(config.getServerDir());
            this.txfPort.setText(Integer.toString(config.getServerPort()));
            this.txfDbName.setText(config.getDbName());
            this.txfUserName.setText(config.getUserName());
            this.chkSSL.setSelected(config.isUseSSL());
            
            CommPortIdentifier tempPort;
            Enumeration ports = CommPortIdentifier.getPortIdentifiers();
            while(ports.hasMoreElements()){
                tempPort = (CommPortIdentifier) ports.nextElement();
                this.cmbComPort.getItems().add(tempPort.getName());
            }
            this.cmbComPort.getSelectionModel().selectFirst();
        } catch (IOException ex) {
            System.out.println("IO Error at Controller_Configuration: " + ex.getMessage());
        }
        
    }    
    @FXML
    public void action_save(MouseEvent e){
       e.consume();
       boolean bHasError = false;
       String message = "";
        
       String serverIp = "";
       String serverDir = "";
       String databaseName = "";
       String userName = "";
       String password = "";
       String comPort = "";
       int serverPort = 0;
       boolean isSSL = false;
       try {
          // FETCH AND FILTER INPUTS
          
          serverIp = this.txfIp.getText().trim();
          if(serverIp.equals("")){
              throw new NoInputException("Please input server ip.");
          }
          serverDir = this.txfDir.getText().trim();
          if(serverDir.equals("")){
              throw new NoInputException("Please input server directory");
          }
          
          databaseName = this.txfDbName.getText().trim();
          if(databaseName.equals("")){
              throw new NoInputException("Please input database name.");
          }
          
          userName = this.txfUserName.getText().trim();
          if(userName.equals("")){
              throw new NoInputException("Please input user name.");
          }
          password = this.txfPassword.getText().trim();
          if(password.equals("")){
              throw new NoInputException("Please input password.");
          }
          
          comPort = this.cmbComPort.getSelectionModel().getSelectedItem();
          if(comPort==null) comPort = config.getComPort();
          
          try { serverPort = Integer.parseInt(this.txfPort.getText().trim()); }
          catch(NumberFormatException nfe){
              throw new NoInputException("Please input valid server port.");
          }
          
          isSSL = this.chkSSL.isSelected();
          
          //FEED TO THE PROPERTIES
          config.setServerIp(serverIp);
          config.setServerDir(serverDir);
          config.setServerPort(serverPort);
          config.setDbName(databaseName);
          config.setUserName(userName);
          config.setPassword(password);
          config.setComPort(comPort);
          config.useSSL(isSSL);
          
          config.save();
       } 
       catch(NullPointerException ne){
           //System.out.println("Nullpointer: " + ne.getMessage());
           bHasError = true;
           message = "NullPointer: " + ne.getMessage();
       } catch (NoInputException ex) {
           bHasError = true;
           message = ex.getMessage();
            //Logger.getLogger(Controller_Configuration.class.getName()).log(Level.SEVERE, null, ex);
       } catch (IOException ex) {
           bHasError = true;
           message = "Unable to store settings: " + ex.getMessage();
        }
       finally {
           if(bHasError){
               DialogInfo<Controller_DialogInfo> dg = new DialogInfo(this.stgStage,message);
               dg.showAndWait();
           }
           else {
               DialogInfo<Controller_DialogInfo> dg = new DialogInfo(this.stgStage,"Settings stored!");
               dg.showAndWait();
               this.stgStage.close();
           }
       }
    }
    @FXML
    public void action_discard(MouseEvent e){
        e.consume();
        this.stgStage.close();
    }
//    @FXML
//    public void action_browse(MouseEvent e){
//        FileChooser fc = new FileChooser();
//        ExtensionFilter ef = new ExtensionFilter("dir");
//        fc.setSelectedExtensionFilter(ef);
//        File file = fc.showOpenDialog(this.stgStage.getOwner());
//        if(file==null) return;
//        if(file.isDirectory()){
//            this.txfDir.setText(file.getAbsolutePath());
//        }
//    }
}
