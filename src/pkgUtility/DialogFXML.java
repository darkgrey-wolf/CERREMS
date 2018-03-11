/*
 * General Purpose Dialog that loads FXML and its controller
 * Some Problems:
 * Exceptions not catched inside initialize
*/
package pkgUtility;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author GreyWolf
 */
public class DialogFXML {
    private Stage stgStage;
    private FXMLLoader loader;
    public DialogFXML(FXMLLoader loader,String title,Stage owner,Modality modality,StageStyle style) throws IOException{
 
        try {
            this.loader = loader;
            this.stgStage = new Stage();
            Scene scene = new Scene(this.loader.load());
            this.stgStage.setScene(scene);
            this.stgStage.initOwner(owner);
            this.stgStage.initModality(modality);
            this.stgStage.initStyle(style);
            this.stgStage.setTitle(title);
            MouseCommandUtility.setAsDraggable(stgStage,(Node) this.loader.getRoot());
        } catch (IOException ex) {
            throw new IOException("From dialogFXML: " + ex.getMessage());
        }
        
    }
    //Returns the controller
    public <T> T getController(){
        return this.loader.getController();
    }
    //SHOWS the stage
    public void show(){
        this.stgStage.show();
        //return this.loader.getController();
    }
    public void showAndWait(){
        this.stgStage.showAndWait();
    }
    public Stage getStage(){
        return this.stgStage;
    }
}
