
package pkgCore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author GreyWolf
 */
public class Client {
//    protected int id;
//    protected String strNameFull;
//    protected String strPassword;
    protected IntegerProperty id = new SimpleIntegerProperty(this,"id",0);
    public final IntegerProperty idProperty(){
        return this.id;
    }
    public final Integer getId(){
        return this.id.get();
    }
    public final void setId(Integer value){
        this.id.set(value);
    }
    
    protected StringProperty strNameFull = new SimpleStringProperty(this,"strNameFull","");
    public final StringProperty nameFullProperty(){
        return this.strNameFull;
    }
    public final String getNameFull(){
        return this.strNameFull.get();
    }
    public final void setNameFull(String value){
        this.strNameFull.setValue(value);
    } 
    protected StringProperty strPassword = new SimpleStringProperty(this,"strPassword","");
    public final StringProperty passwordProperty(){
        return this.strPassword;
    }
    public final String getPassword(){
        return this.strPassword.get();
    }
    public final void setPassword(String value){
        this.strPassword.setValue(value);
    }
    public Client(){
        this.id.set(-1);
    }
    public final boolean lookUp(Connection connection){

        String strQ = "SELECT COUNT(colAdminId) AS total FROM table_admin "
                     +"WHERE colAdminName=(?) AND colAdminPassword=(?)";
        String strQ2 = "SELECT * FROM table_admin "
                      +"WHERE colAdminName=(?) AND colAdminPassword=(?)";
        PreparedStatement prepSt;
        ResultSet rs1;
        try {
            prepSt = connection.prepareStatement(strQ);
            try {
                prepSt.setString(1,this.strNameFull.getValue());
                prepSt.setString(2,this.strPassword.getValue());
                rs1 = prepSt.executeQuery();
                rs1.next();
                if(rs1.getInt("total")>=1){
                    
                    prepSt = connection.prepareStatement(strQ2);
                    prepSt.setString(1,this.strNameFull.getValue());
                    prepSt.setString(2,this.strPassword.getValue());
                    rs1 = prepSt.executeQuery();
                    rs1.next();
                    this.id.setValue(rs1.getInt("colAdminId"));
                    
                    connection.commit();
                    connection.close();
                    return true;
                }
             
            }
            catch(SQLException ex){
                connection.close();
                System.out.println(ex.getMessage());
            }
            
        } catch (SQLException ex) {
         //Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
           System.out.println(ex.getMessage());
           return false;
          
        } catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
        
        return false;
    }
}
