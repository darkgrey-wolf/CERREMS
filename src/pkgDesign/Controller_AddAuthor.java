
package pkgDesign;

/*
 * Allows empty Middle Initial
 */
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pkgCore.Author;

/**
 * FXML Controller class
 *
 * @author Retaliation
 */
public class Controller_AddAuthor implements Initializable {

    @FXML
    private TextField txfLastName;
    @FXML
    private TextField txfFirstName;
    @FXML
    private TextField txfMI;
    @FXML
    private ComboBox<String> cmbType;
    @FXML
    private Pane btnCancel;
    @FXML
    private Pane btnOk;
    
    private Stage stgThis;
    
    // NON-GUI variables
    private boolean bSubmitted;
    private String strLastName;
    private String strFirstName;
    private String strMI;
    private String strType;
    private String strTypeFull;
    
    
    public void initStage(Stage owner){
        this.stgThis=owner;
    }
    public boolean isOk(){
        return this.bSubmitted==true;
    }
    public String getLastName(){
        return this.strLastName;
    }
    public String getFirstName(){
        return this.strFirstName;
    }
    public String getMI(){
        return this.strMI;
    }
    public String getType(){
        return this.strType;
    }
    public String getTypeFull(){
        return this.strTypeFull;
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cmbType.getItems().addAll("Researcher","Adviser");
        bSubmitted = false;
    }    
    
    @FXML
    private void action_cancel(MouseEvent event) {
        bSubmitted = false;
        this.stgThis.close();
    }

    @FXML
    private void action_ok(MouseEvent event) {
        String sel;
        this.strLastName = this.txfLastName.getText().trim();
        this.strFirstName = this.txfFirstName.getText().trim();
        this.strMI = this.txfMI.getText().trim();
        //Allow empty middle initial
        if(this.strMI==null) this.strMI = " ";
        if(this.strMI.equals("")) this.strMI = " ";
        
        if(!"".equals(this.strLastName)&&!"".equals(this.strFirstName)){
            sel = cmbType.getSelectionModel().getSelectedItem(); 
            if(sel==null) {
                this.bSubmitted = false;
                return;
            }
            switch(sel){
                case "Researcher": 
                    this.strTypeFull = "Student";
                    this.strType = Author.STUDENT;
                    this.bSubmitted = true;
                    this.stgThis.close();
                    break;
                case "Adviser":
                    this.strTypeFull = "Adviser";
                    this.strType = Author.ADVISER;
                    this.bSubmitted = true;
                    this.stgThis.close();
                    break;
                default:
                    this.bSubmitted = false;
                    
            }
        }
        
    }
    
}
