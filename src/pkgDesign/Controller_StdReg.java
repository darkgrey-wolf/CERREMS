package pkgDesign;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.TooManyListenersException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import pkgCore.Configuration;
import pkgCore.ConnectionHandler;
import pkgCore.SerialReader;
import pkgExceptions.NoInputException;
import pkgUtility.DialogInfo;

/**
 * FXML Controller class
 *
 * @author Retaliation
 */
public class Controller_StdReg extends Controller_Menu implements Initializable, SerialPortEventListener {
    
    @FXML
    private TextField txfID;
    @FXML
    private TextField txfNameLast;
    @FXML
    private TextField txfNameFirst;
    @FXML
    private TextField txfMI;
    @FXML
    private TextField txfRFID;
    @FXML
    private Pane btnCancel;
    @FXML
    private Pane btnReg;
    
    
    
    //NON-FXML members  
    private SerialReader reader;
    private Long iUID;
    // CUSTOM FUNCTIONS
    @Override
    public void setContainer(Pane container){
        this.parent=container;
        runReader();
    }
    @Override
    public void clean(){
        this.txfID.setText("");
        this.txfNameLast.setText("");
        this.txfNameFirst.setText("");
        this.txfMI.setText("");
        this.txfRFID.setText("");
        if(this.parent!=null) this.parent.getChildren().clear();
        this.btnAccessor.setStyle(".button1");
        if(reader!=null) this.reader.close();
        if(this.iUID!=null) this.iUID = 0L;
    }
    public void runReader(){
        boolean bHasError = false;
        String message = "";
        
        //INITIALIZING ARDUINO READER
        try {
            Configuration config = new Configuration();
            reader = new SerialReader(config.getComPort()); // "COM5"
            reader.addEventListener(this);
        } catch (NoSuchPortException ex) {
            bHasError = true;
            message = "Port not found. "+ex.getMessage();
            //Logger.getLogger(Controller_StdReg.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PortInUseException ex) {
            bHasError = true;
            message = "Port currently in use. "+ex.getMessage();
            //Logger.getLogger(Controller_StdReg.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedCommOperationException ex) {
            bHasError = true;
            message = "Unknown operation. "+ex.getMessage();
            //Logger.getLogger(Controller_StdReg.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            bHasError = true;
            message = "Unable to send/receive data. "+ex.getMessage();
            //Logger.getLogger(Controller_StdReg.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TooManyListenersException ex) {
            bHasError = true;
            message = "Arduino unable to connect. "+ex.getMessage();
            //Logger.getLogger(Controller_StdReg.class.getName()).log(Level.SEVERE, null, ex);
        } catch(Exception e){
            bHasError = true;
            message = "Unknown error caught on action. " + e.getMessage();
        }
        finally {
            if(bHasError){
                DialogInfo<Controller_DialogInfo> dg = new DialogInfo(this.stgOwner,message);
                dg.showAndWait();
            }
        }
    }
    
    
    //FXML EVENT HANDLING
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.iUID = 0L;
        
        
        
    }    
    
    
    @FXML
    private void action_Cancel(MouseEvent event) {
        clean();
    }

    @FXML
    private void action_Reg(MouseEvent event) {
        boolean bHasError = false;
        String message = "";
        
        int id;
        String strID = this.txfID.getText().trim();
        String nameFirst = this.txfNameFirst.getText().trim();
        String nameLast = this.txfNameLast.getText().trim();
        String nameMI = this.txfMI.getText().trim();
        
        String strQ = "INSERT INTO table_student (colUIDInt,colStudentId,colNameLast,colNameFirst,colNameMI) "
                    + "VALUES(?,?,?,?,?) ON DUPLICATE KEY UPDATE "
                    + "colUIDInt=VALUES(colUIDInt), "
                    + "colStudentId=VALUES(colStudentId), "
                    + "colNameLast=VALUES(colNameLast), "
                    + "colNameFirst=VALUES(colNameFirst), "
                    + "colNameMI=VALUES(colNameMI)";
        
        Connection connection;
        PreparedStatement prepSt;
        
        try {
            id = Integer.parseInt(strID);
            if(nameFirst.equals("")){
                throw new NoInputException("No first name");
            }
            if(nameLast.equals("")){
                throw new NoInputException("No last name");
            }
            if(nameMI.equals("")){
                throw new NoInputException("No middle initial");
            }
            if(this.iUID==0){
                throw new NoInputException("RFID not loaded.");
            }
            
            connection = new ConnectionHandler(false).getConnection();
            prepSt = connection.prepareStatement(strQ);
            //prepSt.
            prepSt.setLong(1, this.iUID);
            prepSt.setInt(2,id);
            prepSt.setString(3,nameLast);
            prepSt.setString(4,nameFirst);
            prepSt.setString(5,nameMI);
            prepSt.execute();
            
            connection.commit();
            connection.close();
            this.reader.close(); 
            
            
        }catch(NumberFormatException nf){
            bHasError = true;
            message = "Invalid Student ID"; 
        }catch (NoInputException ni){
            bHasError = true;
            message = ni.getMessage();
        } catch (SQLException ex) {
            bHasError = true;
            message = "Registration failed:" + ex.getMessage();
        } catch (ClassNotFoundException ex) {
            bHasError = true;
            message = ex.getMessage();
        } catch (IOException ex) {
            bHasError = true;
            message = ex.getMessage();
        }
        finally {
            if(bHasError){
               DialogInfo<Controller_DialogInfo> dg = new DialogInfo(this.stgOwner,message);
               dg.showAndWait(); 
               
            }
            else {
               DialogInfo<Controller_DialogInfo> dg = new DialogInfo(this.stgOwner,"Upload Successful!");
               dg.showAndWait();
               clean();
            }
            
        }
        
        
        

    }

    @Override
    public synchronized void serialEvent(SerialPortEvent spe) {;
        boolean bHasError = false;
        
        String message = "";
        String input;
        if(spe.getEventType()==SerialPortEvent.DATA_AVAILABLE){
           try {
            input = this.reader.readLine();
            this.iUID = Long.parseLong(input);
            this.txfRFID.setText("UID: " + Long.toHexString(this.iUID).toUpperCase());
            } catch (IOException ex) {
                bHasError = true;
                message += ex.getMessage();
            } catch (NumberFormatException nfe){
                bHasError = true;
                message += "UID format error.";
            }
            finally {
                if(bHasError){
                    System.out.println("Error in serial reading: " + message);
                }
            } 
        }
        else {
            System.out.println("Unknown serial event:" + spe.getEventType() + " vs " + SerialPortEvent.DATA_AVAILABLE);
        }
        
    }
    
}
