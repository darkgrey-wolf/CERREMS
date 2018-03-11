/*
 * Description
 * Element class represents the fundamental data within the database
 * Its subclasses includes the Manuscript, Author and Keyword.
 * It contains inheritable data: id, TABLE_NAME and COLUMN_NAME.
 * 
*/
package pkgCore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 *
 * @author GreyWolf
 */
public class Element {
    protected int id;
    protected String TABLE_NAME = "";
    protected String COLUMN_NAME = "";
    public Element(){
        this.id = -1;
    }
    public int getId(){
        return this.id;
    }
    public String getColName(){
        return this.COLUMN_NAME;
    }
    public String getTableName(){
        return this.TABLE_NAME;
    }
    public boolean upload(Connection connection) throws Exception {
        // do nothing;
        return true;
    }
    @Override
    public String toString(){
        return "Element ID: " + this.id; 
    }
    public static void relateElement(Connection connection,Element in,LinkedList<Element> in2) throws SQLException{
        PreparedStatement prepSt;
        try {
            ListIterator li = in2.listIterator();
            Element tempEl = new Element();
            if(li.hasNext()) tempEl = (Element) li.next();
            li.previous();
            prepSt = connection.prepareStatement(
            "INSERT INTO table_"+in.getTableName()+"_"+tempEl.getTableName()+" ("+in.getColName()+","+tempEl.getColName()+") VALUES (?,?)");

            while(li.hasNext()){
                tempEl = (Element) li.next();
                prepSt.setInt(1,in.getId());
                prepSt.setInt(2,tempEl.getId());
                prepSt.addBatch();
            }
            prepSt.executeBatch();
        }
        catch(NullPointerException e){
            throw new NullPointerException("Failed at relate elements.");
        }
    }
}
