
package pkgUtility;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author GreyWolf
 */
public class DialogAddAuthor {
    private Stage stgOwner;
    private Stage stgThis;
    private Parent root;
    private FXMLLoader loader;
    
    public DialogAddAuthor(Stage owner,FXMLLoader loader) throws IOException{
        this.stgOwner=owner;
        this.loader=loader;
        this.root = loader.load();
        this.stgThis = new Stage();
        Scene scene = new Scene(this.root);
        this.stgThis.setScene(scene);
        this.stgThis.initModality(Modality.APPLICATION_MODAL);
        this.stgThis.initOwner(this.stgOwner);
        this.stgThis.initStyle(StageStyle.UNDECORATED);
    }
    public <T> T getController(){
        return this.loader.getController();
    }
    public Stage getStage(){
        return this.stgThis;
    }
    public void showAndWait() {
        this.stgThis.showAndWait();
    }
}
