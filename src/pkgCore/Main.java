/*
 *Notes:
 *  
 *  The class Manuscript handles general uploading
 *  Parameterize connection Strings into a persistent configuration file via Properties
 *  FILENAMES MUST HAVE NO SPACES, USE DASH
 *  executeBatch not useful
 *  QUERY ROLLBACK NOT WORKING - solved
 *  LOGIN CURRENTLY BY PASSED
 *  SERVER DIRECTORY: c:\wamp64\www\ravenfx
 *  SERVER SUBDIRECTORY: \data\
 */

/*
 * 
 * 
 */

package pkgCore;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pkgDesign.Controller_Console;
import pkgDesign.Controller_LogIn;

/**
 *
 * @author Grey_Wolf
 */
public class Main extends Application {
    Stage stgThis;
    DataPollService monitoring;
    @Override
    public void start(Stage stage) throws Exception {
        this.stgThis = stage;
        Stage login = new Stage();
        Scene scene;
        // Set up login
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pkgDesign/FXML_LogIn.fxml"));
        scene = new Scene(loader.load());
        setUpStage(login,scene);
        Controller_LogIn controllerLogin = loader.<Controller_LogIn>getController();
        controllerLogin.setStage(login);
        //login bypassed
        //controllerLogin = null;
        login.showAndWait(); 
        if(controllerLogin.isPassed()){
            loader = new FXMLLoader(getClass().getResource("/pkgDesign/FXML_Console.fxml"));
            scene = new Scene(loader.load());
            setUpStage(this.stgThis,scene);
            Controller_Console controllerConsole = loader.<Controller_Console>getController();
            controllerConsole.setStage(this.stgThis);
            controllerConsole.setClient(controllerLogin.getClient());
            this.stgThis.show();
            System.gc();
        } else {
            System.gc();
        }

    }

    private void setUpStage(Stage stage,Scene value){
        stage.setScene(value);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.centerOnScreen();
        stage.setResizable(false);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
