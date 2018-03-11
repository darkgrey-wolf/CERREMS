/*
 * Description:
 * Author is a subclass of Element which contains basic data about an author.
 * Author types include Student or Adviser.
 * Author class needs Connection class to upload to database
 */
package pkgCore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author GreyWolf
 */
public class Author extends Element{
    private String strLastName;
    private String strFirstName;
    private String strMI;
    private String strType;
    
    
    
    public Author(String sL, String sF, String sM, String sT){
        super();
        this.strLastName=sL;
        this.strFirstName=sF;
        this.strMI = sM;
        this.strType = sT;
        this.id=-1;
        this.COLUMN_NAME = "colAuthorId";
        this.TABLE_NAME = "author";
    }
    public String getLastName(){
        return this.strLastName;
    }
    public String getFirstName(){
        return this.strFirstName;
    }
    public String getMI(){
        return this.strMI;
    }
    public String getFullName(){
        return this.strLastName + ", " + this.strFirstName + ", " + this.strMI;
    }
    public String getType(){
        String temp= "";
        switch(this.strType){
            case STUDENT: temp= "Student"; break;
            case ADVISER: temp= "Adviser"; break;
        }
        return temp;
    }
    
    @Override
    public String toString(){
        return getFullName() + " [Type: " + this.getType() + "] ";
    }
    @Override
    public boolean upload(Connection connection) throws SQLException{
        ResultSet rs;
        PreparedStatement prepSt = connection.prepareStatement(
        "INSERT INTO table_author (colNameLast,colNameFirst,colNameMI,colNameFull,colType) "
       +"VALUES(?,?,?,?,?) ON DUPLICATE KEY UPDATE "
       +"colNameLast=VALUES(colNameLast), "
       +"colNameFirst=VALUES(colNameFirst), "
       +"colNameMI=VALUES(colNameMI), "
       +"colNameFull=VALUES(colNameFull), "
       +"colType=VALUES(colType)");
        prepSt.setString(1,this.strLastName);
        prepSt.setString(2, this.strFirstName);
        prepSt.setString(3, this.strMI);
        prepSt.setString(4, this.strLastName + ", " + this.strFirstName + ", " + this.strMI);
        prepSt.setString(5, this.strType);
        if(prepSt.execute()){
           throw new SQLException("Failed to update author:" + this.strLastName); 
        }
        
        prepSt = connection.prepareStatement(
        "SELECT * FROM table_author WHERE colNameFull=(?)");
        prepSt.setString(1, this.strLastName + ", " + this.strFirstName + ", " + this.strMI);
        rs = prepSt.executeQuery();
        if(rs.next()){
           this.id = rs.getInt("colAuthorId"); 
        }
        else {
            throw new SQLException("Unable to load Author id:"+this.getLastName());
        }
        return true;
    }
    //CONSTANT
    public final static String STUDENT = "std";
    public final static String ADVISER = "adv";
}
