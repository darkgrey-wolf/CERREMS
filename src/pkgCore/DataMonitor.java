package pkgCore;

import java.awt.Point;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author GreyWolf
 * @param <V>
 */
public class DataMonitor<V> extends Service{
    
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
    
    private TaskDataFetch<Point> myTask;
    public DataMonitor(){
        this.myTask = new TaskDataFetch();
        this.countAll.bind(this.myTask.countAllProperty());
        this.countCPE.bind(this.myTask.countCPEProperty());
        this.countECE.bind(this.myTask.countECEProperty());
        this.countEE.bind(this.myTask.countEEProperty());
        this.countME.bind(this.myTask.countMEProperty());
        this.countCE.bind(this.myTask.countCEProperty());
        this.countCHE.bind(this.myTask.countCHEProperty());
        //this.countMonthly.bind(this.myTask.countMonthlyProperty());
        //this.countDaily.bind(this.myTask.countDailyProperty());
    }
    
    @Override
    protected Task<V> createTask() {
        return this.myTask;
    }
    
}
