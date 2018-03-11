/*
 * NOTE: USE LOOPS LATER ON
 */
package pkgCore;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

/**
 *
 * @author GreyWolf
 */
public class DataPollService extends ScheduledService< HashMap<Integer,Integer> > {
    
    protected IntegerProperty countAll = new SimpleIntegerProperty(this,"countAll",0);
    protected IntegerProperty countCPE = new SimpleIntegerProperty(this,"countCPE",0);
    protected IntegerProperty countECE = new SimpleIntegerProperty(this,"countECE",0);
    protected IntegerProperty countEE = new SimpleIntegerProperty(this,"countEE",0);
    protected IntegerProperty countME = new SimpleIntegerProperty(this,"countME",0);
    protected IntegerProperty countCE = new SimpleIntegerProperty(this,"countCE",0);
    protected IntegerProperty countCHE = new SimpleIntegerProperty(this,"countCHE",0);
    protected IntegerProperty countMonthly = new SimpleIntegerProperty(this,"countMonthly",0);
    protected IntegerProperty countDaily = new SimpleIntegerProperty(this,"countDaily",0);
    
    protected IntegerProperty countY1 = new SimpleIntegerProperty(this,"countY1",0);
    protected IntegerProperty countY2 = new SimpleIntegerProperty(this,"countY2",0);
    protected IntegerProperty countY3 = new SimpleIntegerProperty(this,"countY3",0);
    protected IntegerProperty countC1 = new SimpleIntegerProperty(this,"countC1",0);
    protected IntegerProperty countC2 = new SimpleIntegerProperty(this,"countC2",0);
    protected IntegerProperty countC3 = new SimpleIntegerProperty(this,"countC3",0);
    protected StringProperty conMessage = new SimpleStringProperty(this,"conMessage","");
    
    public final IntegerProperty countAllProperty(){ return this.countAll;  }
    public final IntegerProperty countCPEProperty(){ return this.countCPE;  }
    public final IntegerProperty countECEProperty(){ return this.countECE;  }
    public final IntegerProperty countEEProperty(){ return this.countEE;  }
    public final IntegerProperty countMEProperty(){ return this.countME;  }
    public final IntegerProperty countCEProperty(){ return this.countCE;  }
    public final IntegerProperty countCHEProperty(){ return this.countCHE;  }
    public final IntegerProperty countMonthlyProperty() { return this.countMonthly; }
    public final IntegerProperty countDailyProperty() { return this.countDaily; }
    public final IntegerProperty countY1Property() {return this.countY1;}
    public final IntegerProperty countY2Property() {return this.countY2;}
    public final IntegerProperty countY3Property() {return this.countY3;}
    public final IntegerProperty countC1Property(){ return this.countC1;}   
    public final IntegerProperty countC2Property(){ return this.countC2;}
    public final IntegerProperty countC3Property(){ return this.countC3;}
    public final StringProperty conMessageProperty() { return this.conMessage; }
    public DataPollService(){
        
       this.countAll.set(0);
       this.countCPE.set(0);
       this.countECE.set(0);
       this.countEE.set(0);
       this.countME.set(0);
       this.countCE.set(0);
       this.countCHE.set(0);
       this.conMessage.set("");
       
       this.setOnSucceeded(e->populateItems());
       this.setOnFailed(ex->handleFailure());
    
    }
    private void populateItems(){
       HashMap<Integer,Integer> hm = this.getValue();

        this.conMessage.set("");
        this.countAll.set(hm.get(1));
        this.countCPE.set(hm.get(2));
        this.countECE.set(hm.get(3));
        this.countEE.set(hm.get(4));
        this.countME.set(hm.get(5));
        this.countCE.set(hm.get(6));
        this.countCHE.set(hm.get(7)); 

        this.countY1.set(hm.get(8));
        this.countY2.set(hm.get(8)-1);
        this.countY3.set(hm.get(8)-2);

        this.countC1.set(hm.get(9));
        this.countC2.set(hm.get(10));
        this.countC3.set(hm.get(11));

        this.countMonthly.set(hm.get(12));
        this.countDaily.set(hm.get(13));
    }
    private void handleFailure(){
        //System.out.println("DataPollService Failed.");
        this.conMessage.set("Connection Error");
    }
    @Override
    protected Task<HashMap<Integer,Integer> > createTask() {
        return new Task<HashMap<Integer,Integer> >() {
        Connection connection;
        String strQ;
        PreparedStatement prepSt;
        ResultSet rs;
        HashMap<Integer,Integer> hm = new HashMap<>();
        
        @Override
        protected HashMap<Integer,Integer> call() throws Exception{
            try {
                // open connection
                connection = new ConnectionHandler(false).getConnection();
                //Fetch number of manuscripts

                strQ="SELECT COUNT(colEntryId) AS total FROM table_entry";
                prepSt = connection.prepareStatement(strQ);
                rs = prepSt.executeQuery();
                rs.next();
                hm.put(1,rs.getInt("total"));
                
                //Fetch number of manuscripts per program
                strQ="SELECT COUNT(colEntryId) AS total FROM table_entry WHERE colProgram=(?)";
                prepSt = connection.prepareStatement(strQ);
                for(int k=0;k<6;++k){      
                    switch(k){
                        case 0: prepSt.setString(1,Manuscript.PROGRAM_CPE); break;
                        case 1: prepSt.setString(1,Manuscript.PROGRAM_ECE); break;
                        case 2: prepSt.setString(1,Manuscript.PROGRAM_EE); break;
                        case 3: prepSt.setString(1,Manuscript.PROGRAM_ME); break;
                        case 4: prepSt.setString(1,Manuscript.PROGRAM_CE); break;
                        case 5: prepSt.setString(1,Manuscript.PROGRAM_CHE); break;
                    }
                    
                    
                    rs = prepSt.executeQuery();
                    rs.next();
                    hm.put(k+2,rs.getInt("total"));
                }
                
                
                //fetch all manuscripts 3 years before the current year
                //fetch the latest year
                strQ = "SELECT MAX(colSYEnd) AS colSYEnd FROM table_entry";
                prepSt = connection.prepareStatement(strQ);
                rs = prepSt.executeQuery();
                rs.next();
                hm.put(8,rs.getInt("colSYEnd"));
                
                strQ = "SELECT COUNT(colEntryId) AS total FROM table_entry WHERE colSYEnd = "
                     + " ( SELECT MAX(colSYEnd) AS colSYEnd FROM table_entry )";
                prepSt = connection.prepareStatement(strQ);
                rs = prepSt.executeQuery();
                rs.next();
                hm.put(9,rs.getInt("total"));
                
                strQ = "SELECT COUNT(colEntryId) AS total FROM table_entry WHERE colSYEnd = "
                     + " ( SELECT MAX(colSYEnd) AS colSYEnd FROM table_entry ) - 1 ";
                prepSt = connection.prepareStatement(strQ);
                rs = prepSt.executeQuery();
                rs.next();
                hm.put(10,rs.getInt("total"));
                
                strQ = "SELECT COUNT(colEntryId) AS total FROM table_entry WHERE colSYEnd = "
                     + " ( SELECT MAX(colSYEnd) AS colSYEnd FROM table_entry ) - 2";
                prepSt = connection.prepareStatement(strQ);
                rs = prepSt.executeQuery();
                rs.next();
                hm.put(11,rs.getInt("total"));
                
                //fetch number of client access per month 
                strQ = "SELECT COUNT(colId) AS total FROM table_access WHERE MONTH(colAccessDate) = MONTH(CURDATE())";
                prepSt = connection.prepareStatement(strQ);
                rs = prepSt.executeQuery();
                rs.next();
                hm.put(12,rs.getInt("total"));
                
                //fetch number of client access per week
                strQ = "SELECT COUNT(colId) AS total FROM table_access WHERE DAY(colAccessDate) = DAY(CURDATE())";
                prepSt= connection.prepareStatement(strQ);
                rs = prepSt.executeQuery();
                rs.next();
                hm.put(13,rs.getInt("total"));
                
                connection.commit();
                connection.close();
                return hm;
            } catch(NullPointerException | SQLException|ClassNotFoundException | IOException e){
                throw new Exception("Error at Task: "+e.getMessage());
            }
        }
        
        };
        
    }
    
}
