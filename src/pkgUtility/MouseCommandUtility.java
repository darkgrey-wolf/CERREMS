package pkgUtility;

import pkgUtility.Coord;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author Grey_Wolf
 */
public class MouseCommandUtility {
    public static void setAsDraggable(Stage stage,Node node){
        final Coord dragPoint = new Coord();
        node.setOnMouseEntered((MouseEvent event) -> {
            node.setCursor(Cursor.OPEN_HAND);
        });
        node.setOnMousePressed((MouseEvent event)->{
            dragPoint.x = stage.getX() - event.getScreenX();
            dragPoint.y = stage.getY() - event.getScreenY();
        });
        node.setOnMouseDragged((MouseEvent event)->{
            stage.setX(event.getScreenX()+dragPoint.x);
            stage.setY(event.getScreenY()+dragPoint.y);
        });
    }
    
    private static Coord point;
}
