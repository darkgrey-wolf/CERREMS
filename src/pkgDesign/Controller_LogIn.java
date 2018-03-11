
package pkgDesign;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pkgCore.Client;
import pkgCore.ConnectionHandler;
import pkgUtility.DialogFXML;
import pkgUtility.DialogInfo;
import pkgUtility.MouseCommandUtility;

/**
 * FXML Controller class
 *
 * @author GreyWolf
 */
public class Controller_LogIn implements Initializable {

    @FXML
    private TextField txfName;
    @FXML
    private PasswordField txfPassword;
    @FXML
    private Pane btnSettings;
    @FXML
    private Pane btnCancel;
    @FXML
    private Pane btnLogin;
    @FXML
    private AnchorPane paneSpace;
    @FXML
    private Pane paneDrag1;
    @FXML
    private Pane paneDrag2;
    @FXML
    private Pane paneDrag3;
    
    private Stage stgThis;
    private boolean bPassed;
    private Client admin;
    
    //CUSTOM FUNCTIONS
    public final void setStage(Stage value){
        this.stgThis=value;
        MouseCommandUtility.setAsDraggable(this.stgThis,this.paneDrag1);
        MouseCommandUtility.setAsDraggable(this.stgThis,this.paneDrag2);
        MouseCommandUtility.setAsDraggable(this.stgThis,this.paneDrag3);
    }
    public final boolean isPassed(){
        return this.bPassed;
    }
    public final Client getClient(){
        return this.admin;
    }
    //EVENT HANDLING FUNCTIONS
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.bPassed=false;
        admin = new Client();
        
        System.out.println("Login Loaded");
    }    
    @FXML
    public void action_login(MouseEvent event){
        admin.setNameFull(txfName.getText());
        admin.setPassword(txfPassword.getText());
        try {
            this.bPassed = admin.lookUp(new ConnectionHandler(false).getConnection());
            if(this.bPassed) this.stgThis.close();
            else {
                DialogInfo<Controller_DialogInfo> dg = new DialogInfo<>(this.stgThis,"Invalid name or password");
                dg.showAndWait();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Controller_LogIn.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Controller_LogIn.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            //Logger.getLogger(Controller_LogIn.class.getName()).log(Level.SEVERE, null, ex);
        }
        //this.bPassed=true;
        
    }
    @FXML
    public void action_cancel(MouseEvent event){
        this.bPassed=false;
        this.stgThis.close();
        
    }
    @FXML
    public void action_setting(MouseEvent event){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pkgDesign/FXML_Configuration.fxml"));
        Controller_Configuration config;
        try {
            DialogFXML dg = new DialogFXML(loader,
                    "",
                    this.stgThis,
                    Modality.APPLICATION_MODAL,
                    StageStyle.UNDECORATED);
            config = dg.<Controller_Configuration>getController();
            config.initStage(dg.getStage());
            dg.showAndWait();
            
        } catch (IOException ex) {
            //Logger.getLogger(Controller_Console.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Action_config error: "+ ex.getMessage());
        }
    }
}
