/*
 *
 * General purpose dialog to display information quickly
*/
package pkgUtility;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pkgDesign.Controller_DialogInfo;

/**
 *
 * @author Retaliation
 * @param <T>
 * 
 */
public class DialogInfo<T extends Controller_DialogInfo> {
    String strMessage;
    Stage stage;
    T controller;
    public DialogInfo(Stage owner,String in){
        try {
            this.strMessage=in;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pkgDesign/FXML_DialogInfo.fxml"));
            //controller = loader.getController();
            this.stage = new Stage();
            Scene scene = new Scene(loader.load());
            this.stage.setScene(scene);
            this.stage.initModality(Modality.APPLICATION_MODAL);
            //this.stage.setIconified(false);
            this.stage.initOwner(owner);
            this.stage.initStyle(StageStyle.UNDECORATED);
            
            
            this.controller = loader.<T>getController();
            this.controller.init(stage,in);
        } catch (IOException ex) {
            System.out.println("Unable to load dialog");
        }
    }
    public void showAndWait(){
        this.stage.showAndWait();
    }
}
