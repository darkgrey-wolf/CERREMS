
package pkgDesign;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pkgUtility.MouseCommandUtility;

/**
 * FXML Controller class
 *
 * @author Retaliation
 */
public class Controller_DialogInfo implements Initializable {

    @FXML
    private Label tagMessage;
    @FXML
    private Pane btnOk;
    
    @FXML
    private AnchorPane paneRoot;
    
    private Stage stgThis;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    public void init(Stage stage,String in){
        this.stgThis=stage;
        this.tagMessage.setText(in);
        MouseCommandUtility.setAsDraggable(this.stgThis,this.paneRoot);
    }
    @FXML
    private void action_ok(MouseEvent event) {
        this.stgThis.close();
    }
    
}
