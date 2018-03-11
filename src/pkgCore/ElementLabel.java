package pkgCore;

import javafx.scene.control.Label;

/**
 *
 * @author GreyWolf
 * @param <T>
 */
public class ElementLabel<T extends Element> extends Label {
    private T data;
    public ElementLabel(T in){
        super(in.toString());
        this.data=in;    
    }
    public T getData(){
        return data;
    }
}
