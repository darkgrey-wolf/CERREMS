/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgDesign;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author GreyWolf
 */
public class Controller_Menu {
    protected Pane parent;
    protected Pane btnAccessor;
    protected Stage stgOwner; 
    public Controller_Menu(){
        //does nothing;
    }
    public void setContainer(Pane container){
        this.parent=container;
    }
    public void setAccessButton(Pane button){
        this.btnAccessor = button;
    }
    public void setStageOwner(Stage stgOwner){
        this.stgOwner=stgOwner;
    }
    protected void clean(){
        
        if(this.parent!=null) this.parent.getChildren().clear();
        this.btnAccessor.setStyle(".button1");
    }
}
