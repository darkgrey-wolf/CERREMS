/*
 *Note: Dialog call not yet centralized
*/
package pkgDesign;
import pkgCore.*;
import pkgExceptions.*;
import java.io.*;
import java.util.*;
import javafx.fxml.*;
import pkgUtility.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author GreyWolf
 */
public class Controller_Upload extends Controller_Menu implements Initializable {

    @FXML
    private GridPane paneUpload;
    @FXML
    private TextField txfEntryTitle;
    @FXML
    private TextField txfSYEnd;
    @FXML
    private ComboBox<String> cmbProgram;
    @FXML
    private ListView< ElementLabel<Author> > listViewAuthors;
    @FXML
    private Button btnAuthorAdd;
    @FXML
    private Button btnAuthorRem;
    @FXML
    private TextArea txaAbstract;
    @FXML
    private TextArea txaKeyWords;
    @FXML
    private Pane btnCancel;
    @FXML
    private Pane btnSubmit;
    @FXML
    private Pane btnFileChoose;
    @FXML
    private Label tagFileName;
    
    //NON FXML MEMBERS 
    private File file;
    
    
    //CUSTOM FUNCTIONS
    public LinkedList<Keyword> parseKeywords(){
        LinkedList<Keyword> k = new LinkedList<>();
        String in = this.txaKeyWords.getText();
        String temp;
        StringTokenizer tk = new StringTokenizer(in,";",false);
        while(tk.hasMoreTokens()){
          temp = tk.nextToken().trim();
          if(!temp.equals("")){
             k.add(new Keyword(temp));
          }
        }
        return k;
    }

    /**
     *
     */
    @Override
    protected void clean(){
        //clear GUI
        this.txfEntryTitle.setText("");
        this.txaAbstract.setText("");
        this.txaKeyWords.setText("");
        this.txfSYEnd.setText("");
        if(this.parent!=null)this.parent.getChildren().clear();
        this.btnAccessor.setStyle(".button1");
        this.tagFileName.setText("File Loaded:");
        
        // clear data
        this.listViewAuthors.getItems().clear();
        this.lstKeywords.clear();
        this.file = null;
    }
    
    
    
    //FXML EVENT HANDLES
    private LinkedList<Keyword> lstKeywords;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.cmbProgram.getItems().addAll("CpE","ECE","EE","ME","CE","ChE");
        this.lstKeywords = new LinkedList<>();
        this.file=null;
    }    
    
    @FXML
    private void action_cancel(MouseEvent event){
      clean();
    }
    
    @FXML
    private void action_submit(MouseEvent event){
        
        int iSyLast;
        String program="";
        Author a;
        Manuscript manuscript = new Manuscript();
        LinkedList<Author> lstA = new LinkedList<>();
        
        // FETCHING INPUTS
        
        //fetch title
        String strTitle = this.txfEntryTitle.getText();
        //fetch abstract
        String strAbstract = this.txaAbstract.getText();
        //fetch keywords
        this.lstKeywords = parseKeywords();
        // fetch authors
        ListIterator li = this.listViewAuthors.getItems().listIterator();
           while(li.hasNext()){
               a = (Author) ((ElementLabel) li.next()).getData();
               lstA.add(a);
           }
        
        
        
        try { 
           
            
           // VERIFY INPUTS
           if(strTitle.equals("")){
              throw new NoInputException("Please input manuscript title");
           }
           if(lstA.isEmpty()){
               throw new NoInputException("Please input at least one researcher");
           }
           if(this.file==null){
               throw new NoInputException("Please choose a file");
           }
           try {

              iSyLast = Integer.parseInt(this.txfSYEnd.getText());  
              if(iSyLast<1946||iSyLast> new GregorianCalendar().get(GregorianCalendar.YEAR)){
                  throw new NumberFormatException("Out of range year.");
              }
           } catch(NumberFormatException e){
               throw new NoInputException("Invalid year.");
           }
           
           if(strAbstract==null||strAbstract.equals("")){
               throw new NoInputException("Please input abstract");
           }
           if(this.lstKeywords==null||this.lstKeywords.isEmpty()){
               throw new NoInputException("Please input at least one keyword");
           }
           
           
           try {
               switch(cmbProgram.getValue()){
                case "CpE": program = Manuscript.PROGRAM_CPE;
                   break;
                case "ECE": program = Manuscript.PROGRAM_ECE;
                   break;
                case "EE": program = Manuscript.PROGRAM_EE;
                   break;
                case "ME": program = Manuscript.PROGRAM_ME;
                   break;
                case "CE": program = Manuscript.PROGRAM_CE;
                   break;
                case "ChE": program = Manuscript.PROGRAM_CHE;
                   break;
                }
           }
           catch(NullPointerException e){
               throw new NoInputException("Please choose a program");
           }
           
           if(program.equals("")){
               throw new NoInputException("Please choose a program");
           }
           
           //SET TITLE
           manuscript.setTitle(strTitle);
           //SET ABSTRACT
           manuscript.setAbstract(strAbstract);
           //SET YEAR
           manuscript.setYear(iSyLast);
           //SET FILE
           manuscript.setFile(this.file);
           //SET URL
           manuscript.setURL("/data/"+this.file.getName());
           //SET AUTHORS
           manuscript.addAuthors(lstA);
           //SET KEYWORD
           manuscript.addKeyWords(this.lstKeywords);
           // SET PROGRAM
           manuscript.setProgram(program);
           
           
           // SET UP CONNECTION AND UPLOAD
           manuscript.upload(new ConnectionHandler(false).getConnection());
           DialogInfo<Controller_DialogInfo> dg = new DialogInfo<>(this.stgOwner,"Upload successfull!");
           dg.showAndWait();
           clean();         
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (NoInputException ex) {
            DialogInfo<Controller_DialogInfo> dg = new DialogInfo<>(this.stgOwner,ex.getMessage());
            dg.showAndWait();
        } catch (IOException ex) {
            //Logger.getLogger(Controller_Upload.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            DialogInfo<Controller_DialogInfo> dg = new DialogInfo<>(this.stgOwner,ex.getMessage());
            dg.showAndWait();
            //Logger.getLogger(Controller_Upload.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            System.gc();
        }
       
    }
    @FXML
    private void action_fileChoose(MouseEvent event){
        FileChooser fc = new FileChooser();
        this.file = fc.showOpenDialog(this.stgOwner);
        if(this.file==null) return;
        String str = new String();
        this.tagFileName.setText("File Loaded: "+this.file.getName());
    }
    @FXML
    private void action_addAuthor(MouseEvent event){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pkgDesign/FXML_AddAuthor.fxml"));
        Controller_AddAuthor controller;
        Author temp;
        ElementLabel<Author> temp2;
        try {
            DialogAddAuthor dg = new DialogAddAuthor(this.stgOwner,loader);
            controller = dg.<Controller_AddAuthor>getController();
            controller.initStage(dg.getStage());
            dg.showAndWait();
            if(controller.isOk()){
                temp = new Author(controller.getLastName(),controller.getFirstName(),controller.getMI(),controller.getType());
                temp2 = new ElementLabel<>(temp);
                this.listViewAuthors.getItems().add(temp2);
            }
        } catch (IOException ex) {
            System.out.println("Unable to load add author: "+ex.getMessage());
        }
    }  
    @FXML
    private void action_removeAuthor(MouseEvent event){
        ElementLabel ev = this.listViewAuthors.getSelectionModel().getSelectedItem();
        this.listViewAuthors.getItems().remove(ev);
    }
    
    

}
