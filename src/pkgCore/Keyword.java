//handle your own upload
package pkgCore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author GreyWolf
 */
public class Keyword extends Element{
    private final String strKeyWord;
    
    public Keyword(String key){
        super();
        this.strKeyWord = key;
        this.COLUMN_NAME = "colKeyWordId";
        this.TABLE_NAME = "keywords";
    }
    
    private String getKeyWord(){
        return this.strKeyWord;
    }
    @Override
    public String toString(){
        return getKeyWord();
    }
    @Override
    public boolean upload(Connection connection) throws SQLException{
        ResultSet rs;
        PreparedStatement prepSt = connection.prepareStatement(
        "INSERT INTO table_keywords (colKeyWordText) "
       +"VALUES(?) ON DUPLICATE KEY UPDATE "
       +"colKeyWordText=VALUES(colKeyWordText)");
        prepSt.setString(1,this.strKeyWord);
        if(prepSt.execute()){
            throw new SQLException("Failed to upload keyword: "+this.strKeyWord);
        }
        prepSt = connection.prepareStatement(
        "SELECT colKeyWordId FROM table_keywords WHERE colKeyWordText=(?)");
        prepSt.setString(1,this.strKeyWord);
        rs = prepSt.executeQuery();
        if(rs.next()){
          this.id = rs.getInt("colKeyWordId");  
        }
        else {
            throw new SQLException("Unable to load Keyword Id: "+this.strKeyWord);
        }
        return true;
    }
    
}
