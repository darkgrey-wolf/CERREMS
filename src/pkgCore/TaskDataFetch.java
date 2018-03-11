package pkgCore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicReference;
import javafx.application.Platform;
import static javafx.application.Platform.runLater;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;

/**
 *
 * @author GreyWolf
 * @param <V>
 */
public class TaskDataFetch<V> extends Task{
    Connection connection;
    
    private long lTaskDelay = 3000;
    private AtomicReference messageUpdate1 = new AtomicReference();
    private AtomicReference messageUpdate2 = new AtomicReference();
    private AtomicReference messageUpdate3 = new AtomicReference();
    private AtomicReference messageUpdate4 = new AtomicReference();
    private AtomicReference messageUpdate5 = new AtomicReference();
    private AtomicReference messageUpdate6 = new AtomicReference();
    private AtomicReference messageUpdate7 = new AtomicReference();
    private AtomicReference messageUpdate8 = new AtomicReference();
    private AtomicReference messageUpdate9 = new AtomicReference();
    
    protected StringProperty countAll = new SimpleStringProperty(this,"countAll","");
    protected StringProperty countCPE = new SimpleStringProperty(this,"countCPE","");
    protected StringProperty countECE = new SimpleStringProperty(this,"countECE","");
    protected StringProperty countEE = new SimpleStringProperty(this,"countEE","");
    protected StringProperty countME = new SimpleStringProperty(this,"countME","");
    protected StringProperty countCE = new SimpleStringProperty(this,"countCE","");
    protected StringProperty countCHE = new SimpleStringProperty(this,"countCHE","");
    protected StringProperty countMonthly = new SimpleStringProperty(this,"countMonthly","");
    protected StringProperty countDaily = new SimpleStringProperty(this,"countDaily","");
    
    public final StringProperty countAllProperty(){ return this.countAll;  }
    public final StringProperty countCPEProperty(){ return this.countCPE;  }
    public final StringProperty countECEProperty(){ return this.countECE;  }
    public final StringProperty countEEProperty(){ return this.countEE;  }
    public final StringProperty countMEProperty(){ return this.countME;  }
    public final StringProperty countCEProperty(){ return this.countCE;  }
    public final StringProperty countCHEProperty(){ return this.countCHE;  }
    public final StringProperty countMonthlyProperty() { return this.countMonthly; }
    public final StringProperty countDailyProperty() { return this.countDaily; }
    
    
    @Override
    protected V call() throws Exception {
        String strQ;
        PreparedStatement prepSt1,prepSt2,prepSt3,prepSt4,prepSt5,prepSt6,prepSt7;
        PreparedStatement prepSt8,prepSt9;
        ResultSet rs1,rs2,rs3,rs4,rs5,rs6,rs7,rs8,rs9;
        try {
            
            // open connection
            connection = new ConnectionHandler(false).getConnection();

            //Fetch number of manuscripts

            strQ="SELECT COUNT(colEntryId) AS total FROM table_entry";
            prepSt1 = connection.prepareStatement(strQ);
            rs1 = prepSt1.executeQuery();
            //Fetch number of manuscripts per program
            strQ="SELECT COUNT(colEntryId) AS total FROM table_entry WHERE colProgram=(?)";

            prepSt2 = connection.prepareStatement(strQ);
            prepSt2.setString(1,Manuscript.PROGRAM_CPE);
            rs2 = prepSt2.executeQuery();
            prepSt3 = connection.prepareStatement(strQ);
            prepSt3.setString(1,Manuscript.PROGRAM_ECE);
            rs3 = prepSt3.executeQuery();
            prepSt4 = connection.prepareStatement(strQ);
            prepSt4.setString(1,Manuscript.PROGRAM_EE);
            rs4 = prepSt4.executeQuery();
            prepSt5 = connection.prepareStatement(strQ);
            prepSt5.setString(1,Manuscript.PROGRAM_ME);
            rs5 = prepSt5.executeQuery();
            prepSt6 = connection.prepareStatement(strQ);
            prepSt6.setString(1,Manuscript.PROGRAM_CE);
            rs6 = prepSt6.executeQuery();
            prepSt7 = connection.prepareStatement(strQ);
            prepSt7.setString(1,Manuscript.PROGRAM_CHE);
            rs7 = prepSt7.executeQuery();
            //fetch number of client access per month
            //fetch number of client access per week
            //fetch all manuscripts 3 years before the current year
            //close connection
            rs1.next();
            rs2.next();
            rs3.next();
            rs4.next();
            rs5.next();
            rs6.next();
            rs7.next();

            this.updateAll(""+rs1.getInt("total"), this.messageUpdate1,this.countAll);
            this.updateAll(""+rs2.getInt("total"), this.messageUpdate2,this.countCPE);
            this.updateAll(""+rs3.getInt("total"), this.messageUpdate3, this.countECE);
            this.updateAll(""+rs4.getInt("total"), this.messageUpdate4, this.countEE);
            this.updateAll(""+rs5.getInt("total"), this.messageUpdate5, this.countME);
            this.updateAll(""+rs6.getInt("total"),this.messageUpdate6,this.countCE);
            this.updateAll(""+rs7.getInt("total"), this.messageUpdate7, this.countCHE);
            
        }  catch(NullPointerException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    private void updateAll(String value1,AtomicReference ms,StringProperty p){
        //messageUpdate = new AtomicReference();
        if(Platform.isFxApplicationThread()){
            this.countAll.set(value1);
            //this.countCPE.setValue(value2);
        }
        else {
            if (ms.getAndSet(value1) == null) {
                runLater(new Runnable() {
                    @Override
                    public void run() {
                        final String value1 = (String) ms.getAndSet(null);
                        //p.countAll.set(value1);
                        p.set(value1);
                    }
                });
            }
            
        }
    }
}
