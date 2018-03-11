/*
 * 
 */
package pkgDesign;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import pkgCore.Client;
import pkgCore.DataPollService;
import pkgUtility.DialogFXML;
import pkgUtility.MouseCommandUtility;

/**
 * FXML Controller class
 *
 * @author Grey_Wolf
 */
public class Controller_Console implements Initializable {

    @FXML
    private AnchorPane paneRoot;
    @FXML
    private Pane btnAbout;
    @FXML
    private Pane btnSettings;
    @FXML
    private Pane btnAccount;
    @FXML
    private Pane btnExit;
    @FXML
    private Pane btnUpload;
    @FXML
    private Pane btnUpdate;
    @FXML
    private Pane btnStudentReg;
    @FXML
    private Pane btnAdminReg;
    @FXML
    private VBox btnFree;
    @FXML
    private AnchorPane paneFreeSpace;
    @FXML
    private Label tagConStatus;
    @FXML
    private Label tagAllCount;
    @FXML
    private Label tagCpECount;
    @FXML
    private Label tagECECount;
    @FXML
    private Label tagEECount;
    @FXML
    private Label tagChECount;
    @FXML
    private Label tagMECount;
    @FXML
    private Label tagCECount;
    @FXML
    private Label tagYear1;
    @FXML
    private Label tagYear1Count;
    @FXML
    private Label tagYear2;
    @FXML
    private Label tagYear2Count;
    @FXML
    private Label tagYear3;
    @FXML
    private Label tagYear3Count;
    @FXML
    private Label tagAccessDay;
    @FXML
    private Label tagAccessMonth;
    @FXML
    private Label tagAdminName;
    @FXML
    private Pane paneWorkSpace;
    @FXML
    private HBox paneDragBar;
    
    // NON FXML variables;
    private Stage mainWindow;
    private AnchorPane formSubmit;
    private AnchorPane formStdReg;
    private AnchorPane formAdReg;
    private AnchorPane formCover;
    private Controller_Upload uploadController;
    private Controller_StdReg stdRegController;
    private Controller_RegAdmin adRegController;
    private Controller_Cover coverController;
    //private DataMonitor<Void> monitoring;
    private DataPollService monitoring;
    private Client admin;
    //CUSTOM FUNCTIONS
    public void setStage(Stage stage){
        MouseCommandUtility.setAsDraggable(stage,this.paneDragBar);
        this.mainWindow = stage;
    }
    public void setClient(Client value){
        this.admin = value;
        //System.out.println(this.tagAdminName.);
        this.tagAdminName.textProperty().bind(admin.nameFullProperty());
    }
    
    //FXML EVENTS
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Loaders for FXML
        FXMLLoader formUploadLoader = new FXMLLoader(getClass().getResource("/pkgDesign/FXML_Upload.fxml"));
        FXMLLoader formStdRegLoader = new FXMLLoader(getClass().getResource("/pkgDesign/FXML_StudentReg.fxml"));
        FXMLLoader formAdRegLoader = new FXMLLoader(getClass().getResource("/pkgDesign/FXML_RegAdmin.fxml"));
        FXMLLoader formCoverController = new FXMLLoader(getClass().getResource("/pkgDesign/FXML_Cover.fxml"));
        try {
            // load cover
            this.formCover = formCoverController.load();
            this.coverController = formCoverController.<Controller_Cover>getController();
            this.coverController.setStageOwner(this.mainWindow);
            this.coverController.setAccessButton(this.btnFree);
            this.paneWorkSpace.getChildren().add(this.formCover);
            this.coverController.setContainer(this.paneWorkSpace);
            
            // load upload fxml;
            this.formSubmit = formUploadLoader.load();
            this.uploadController = formUploadLoader.<Controller_Upload>getController();
            this.uploadController.setStageOwner(this.mainWindow);
            this.uploadController.setAccessButton(this.btnUpload);
            
            // load student reg
            this.formStdReg = formStdRegLoader.load();
            this.stdRegController = formStdRegLoader.<Controller_StdReg>getController();
            this.stdRegController.setStageOwner(this.mainWindow);
            this.stdRegController.setAccessButton(this.btnStudentReg);
            
            //load admin reg
            this.formAdReg = formAdRegLoader.load();
            this.adRegController = formAdRegLoader.<Controller_RegAdmin>getController();
            this.adRegController.setStageOwner(this.mainWindow);
            this.adRegController.setAccessButton(this.btnAdminReg);
            
        } catch (IOException ex) {
            Logger.getLogger(Controller_Console.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Load Monitoring
        this.monitoring = new DataPollService();
        this.tagAllCount.textProperty().bind(this.monitoring.countAllProperty().asString());
        this.tagCpECount.textProperty().bind(this.monitoring.countCPEProperty().asString());
        this.tagECECount.textProperty().bind(this.monitoring.countECEProperty().asString());
        this.tagEECount.textProperty().bind(this.monitoring.countEEProperty().asString());
        this.tagMECount.textProperty().bind(this.monitoring.countMEProperty().asString());
        this.tagCECount.textProperty().bind(this.monitoring.countCEProperty().asString());
        this.tagChECount.textProperty().bind(this.monitoring.countCHEProperty().asString());
        this.tagAccessMonth.textProperty().bind(this.monitoring.countMonthlyProperty().asString());
        this.tagAccessDay.textProperty().bind(this.monitoring.countDailyProperty().asString());
        this.tagYear1.textProperty().bind(this.monitoring.countY1Property().asString());
        this.tagYear2.textProperty().bind(this.monitoring.countY2Property().asString());
        this.tagYear3.textProperty().bind(this.monitoring.countY3Property().asString());
        this.tagYear1Count.textProperty().bind(this.monitoring.countC1Property().asString());
        this.tagYear2Count.textProperty().bind(this.monitoring.countC2Property().asString());
        this.tagYear3Count.textProperty().bind(this.monitoring.countC3Property().asString());
        this.tagAccessMonth.textProperty().bind(this.monitoring.countMonthlyProperty().asString());
        this.tagAccessDay.textProperty().bind(this.monitoring.countDailyProperty().asString());
        this.tagConStatus.textProperty().bind(this.monitoring.conMessageProperty());
        this.monitoring.setDelay(Duration.seconds(4));
        this.monitoring.start();
    }    
    
    @FXML
    private void action_exit(MouseEvent event) {
        this.uploadController.clean();
        this.stdRegController.clean();
        this.adRegController.clean();
        Platform.exit();
    }
    @FXML
    private void action_dispForm1(MouseEvent event){
        //func1();
        this.stdRegController.clean();
        this.adRegController.clean();
        
        this.btnUpload.setStyle("-fx-background-color: rgb(50,50,50);");
        this.paneWorkSpace.getChildren().clear();
        this.paneWorkSpace.getChildren().add(this.formSubmit);
        this.uploadController.setContainer(this.paneWorkSpace);
        
    }
    
    @FXML
    private void action_dispForm2(MouseEvent event){
        //func1();
        this.adRegController.clean();
        this.stdRegController.clean();
        this.uploadController.clean();
        
        this.btnUpdate.setStyle("-fx-background-color: rgb(50,50,50);");
        this.paneWorkSpace.getChildren().clear();
        
    }
 
    @FXML
    private void action_dispForm3(MouseEvent event){
        //func1();
        this.adRegController.clean();
        this.uploadController.clean();
        
        this.btnStudentReg.setStyle("-fx-background-color: rgb(50,50,50);");
        this.paneWorkSpace.getChildren().clear();
        this.paneWorkSpace.getChildren().add(this.formStdReg);
        this.stdRegController.setContainer(this.paneWorkSpace);
        
    }
    @FXML
    private void action_dispForm4(MouseEvent event){
       // spawns adminstrator registry
       //func1();
       this.stdRegController.clean();
       this.uploadController.clean();
       
       this.btnAdminReg.setStyle("-fx-background-color: rgb(50,50,50);");
       this.paneWorkSpace.getChildren().clear();
       this.paneWorkSpace.getChildren().add(this.formAdReg);
       this.adRegController.setContainer(this.paneWorkSpace);
       
       
    }
    @FXML
    private void action_freeAll(MouseEvent event){
        //func1();
        this.paneWorkSpace.getChildren().clear();
        this.adRegController.clean();
        this.uploadController.clean();
        this.stdRegController.clean();
        this.paneWorkSpace.getChildren().add(this.formCover);
        this.coverController.setContainer(this.paneWorkSpace);
        //Configuration conf = new Configuration(); //Logger.getLogger(Controller_Console.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    
    @FXML
    private void action_config(MouseEvent e){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pkgDesign/FXML_Configuration.fxml"));
        Controller_Configuration config;
        try {
            DialogFXML dg = new DialogFXML(loader,
                    "",
                    this.mainWindow,
                    Modality.APPLICATION_MODAL,
                    StageStyle.UNDECORATED);
            config = dg.<Controller_Configuration>getController();
            config.initStage(dg.getStage());
;
            dg.showAndWait();
            
        } catch (IOException ex) {
            //Logger.getLogger(Controller_Console.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Action_config error: "+ ex.getMessage());
        }
    }
    private void func1(){
        this.btnUpload.setStyle(".button1");
        this.btnUpdate.setStyle(".button1");
        this.btnStudentReg.setStyle(".button1");
        this.btnAdminReg.setStyle(".button1");
    }
    
}
