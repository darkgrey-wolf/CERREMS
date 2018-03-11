
package pkgDesign;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import pkgCore.ConnectionHandler;
import pkgExceptions.NoInputException;
import pkgExceptions.PasswordMismatchException;
import pkgUtility.DialogInfo;

/**
 * FXML Controller class
 *
 * @author GreyWolf
 */
public class Controller_RegAdmin extends Controller_Menu implements Initializable {

    @FXML
    private TextArea txfNameFull;
    @FXML
    private PasswordField txfPass1;
    @FXML
    private PasswordField txfPass2;
    @FXML
    private AnchorPane btn_Submit;
    @FXML
    private TextArea txfNameFull2;
    @FXML
    private PasswordField txfPass3;
    @FXML
    private AnchorPane btn_Delete;
    
    
    
    
    //CUSTOM FUNCTIONS
    @Override
    public void clean(){
        if(this.parent!=null) this.parent.getChildren().clear();
        this.btnAccessor.setStyle(".button1");
        this.txfNameFull.setText("");
        this.txfNameFull2.setText("");
        this.txfPass1.setText("");
        this.txfPass2.setText("");
        this.txfPass3.setText("");
    }
    
    
    //FXML EVENT HANDLERS
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    


    @FXML
    private void action_Submit(MouseEvent event) {
        boolean bIsError = false;
        String message = "";
        String strPass1 = txfPass1.getText().trim();
        String strPass2 = txfPass2.getText().trim();
        String strName = txfNameFull.getText().trim();
        String strQ;
        Connection connection;
        PreparedStatement prepSt;
        try {
            connection = new ConnectionHandler(false).getConnection();
            try {
                //Filter errors
                if(strName.equals("")){
                    throw new NoInputException("Please input name.");
                }
                if(strPass1.equals("")){
                    throw new NoInputException("Please input password.");
                }
                if(strPass2.equals("")){
                    throw new NoInputException("Please confirm password.");
                }
                if(!strPass1.equals(strPass2)){
                    throw new PasswordMismatchException("Password not matched.");
                }

                strQ = "INSERT INTO table_admin (colAdminName,colAdminPassword) "
                     + "VALUES(?,?) ";

                prepSt = connection.prepareStatement(strQ);
                prepSt.setString(1,strName);
                prepSt.setString(2, strPass1);
                prepSt.executeUpdate();

                connection.commit();
                connection.close(); 
            }
            catch(NoInputException e){
            bIsError = true;
            message = e.getMessage();
            } 
            catch (PasswordMismatchException ex) {
                //Logger.getLogger(Controller_RegAdmin.class.getName()).log(Level.SEVERE, null, ex);
                bIsError = true;
                message = ex.getMessage();
                txfPass1.setText("");
                txfPass2.setText("");
            } catch (SQLException ex) {
                bIsError = true;
                message = ex.getMessage();
                connection.commit();
                connection.close();
                //Logger.getLogger(Controller_RegAdmin.class.getName()).log(Level.SEVERE, null, ex);
            } 
            finally {
                if(bIsError){
                    DialogInfo<Controller_DialogInfo> dg = new DialogInfo(this.stgOwner,"Register Failed: " + message);
                    dg.showAndWait();
                }
                else {
                    DialogInfo<Controller_DialogInfo> dg = new DialogInfo(this.stgOwner,
                            "New administrator successfully registered!");
                    dg.showAndWait();
                    clean();
                }
            }    
        }
        catch (SQLException | ClassNotFoundException|IOException ex) {
            DialogInfo<Controller_DialogInfo> dg = new DialogInfo(this.stgOwner,
            "Connection not established.");
            dg.showAndWait();
        }
        
    }

    @FXML
    private void action_Del(MouseEvent event) {
        boolean bIsError = false;
        int count;
        String message = "";
        String strName = txfNameFull2.getText().trim();
        String strPass = txfPass3.getText().trim();
        String strQ;
        Connection connection;
        PreparedStatement prepSt;
        
        try {
            connection = new ConnectionHandler(false).getConnection();
            
            try {
                 if(strName.equals("")){
                     throw new NoInputException("Please input name.");
                 }
                 if(strPass.equals("")){
                     throw new NoInputException("Please input password.");
                 }

                 strQ = "DELETE FROM table_admin WHERE colAdminName=(?) AND colAdminPassword = (?)";
                 prepSt = connection.prepareStatement(strQ);
                 prepSt.setString(1, strName);
                 prepSt.setString(2, strPass);
                 count = prepSt.executeUpdate();
                 if(count==0){
                     connection.commit();
                     connection.close();
                     throw new PasswordMismatchException("Adminstrator/Password mistach");
                 }
                 connection.commit();
                 connection.close(); 
            }catch (SQLException ex) {
            bIsError = true;
            message = ex.getMessage();
            //Logger.getLogger(Controller_RegAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }catch (NoInputException ex) {
            bIsError = true;
            message = ex.getMessage();
            //Logger.getLogger(Controller_RegAdmin.class.getName()).log(Level.SEVERE, null, ex);
            } catch (PasswordMismatchException ex) {
            bIsError = true;
            message = ex.getMessage();
            //Logger.getLogger(Controller_RegAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally {
                if(bIsError){
                   DialogInfo<Controller_DialogInfo> dg = new DialogInfo(this.stgOwner,"Admin not deleted: " + message);
                   dg.showAndWait(); 
                }
                else {
                    DialogInfo<Controller_DialogInfo> dg = new DialogInfo(this.stgOwner,
                            "Adminstrator successfully deleted!");
                    dg.showAndWait();
                    clean();
                }
            }
            
        }catch (SQLException |ClassNotFoundException|IOException ex) {
            DialogInfo<Controller_DialogInfo> dg = new DialogInfo(this.stgOwner,
                            "Connection not established.");
            dg.showAndWait();
            //Logger.getLogger(Controller_RegAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Logger.getLogger(Controller_RegAdmin.class.getName()).log(Level.SEVERE, null, ex);
        
        
    }
    
}
