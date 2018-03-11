package pkgCore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 *
 * @author GreyWolf
 */
public class Manuscript extends Element{
    private String strTitle;
    private String strAbstract;
    private String strProgram;
    private String strURL;
    private int iSYEnd;
    private LinkedList<Element> lstAuthors;
    private LinkedList<Element> lstKeyWords;
    private File file;
    
    public Manuscript(){
        super();
        this.lstAuthors = new LinkedList<>();
        this.lstKeyWords = new LinkedList<>();
        this.COLUMN_NAME = "colEntryId";
        this.TABLE_NAME = "entry";
    }
    
    
    public void setAbstract(String strIn){
        this.strAbstract = strIn;
    }
    public void addAuthor(Author in){
        this.lstAuthors.add(in);
    }
    public void addAuthors(LinkedList<Author> in){
        this.lstAuthors.addAll(in);
    }
    public void setFile(File in){
        this.file = in;
    }
    public void addKeyWord(Keyword in){
        this.lstKeyWords.add(in);
    }
    public void addKeyWords(LinkedList<Keyword> in){
        this.lstKeyWords.addAll(in);
    }
    public void setProgram(String strIn){
        this.strProgram=strIn;
    }
    public void setTitle(String strIn){
        this.strTitle = strIn;
    }
    public void setYear(int in){
        this.iSYEnd = in;
    }
    public void setURL(String strIn){
        this.strURL = strIn;
    }
    public void clearAuthors(){
        lstAuthors.clear();
    }
    public void clearKeyWords(){
        lstKeyWords.clear();
    }
    @Override
    public int getId(){
        return this.id;
    }
    @Override
    public boolean upload(Connection connection) throws Exception {
        ListIterator<Element> liA = lstAuthors.listIterator();
        ListIterator<Element> liK = lstKeyWords.listIterator();
        ResultSet rs;
        try {
            connection = new ConnectionHandler(false).getConnection();
            PreparedStatement prepSt;

            String qEntry = "INSERT INTO table_entry (colTitle,colEntryAbstract,colURL,colProgram,colSYEnd) "
                    + "VALUES(?,?,?,?,?)";
            
            
            String qEntry2 = "SELECT * FROM table_entry WHERE colTitle=(?)";
            
            
            try {
                
                // UPLOAD ENTRY
                prepSt = connection.prepareStatement(qEntry);
                prepSt.setString(1, this.strTitle);
                prepSt.setString(2, this.strAbstract);
                prepSt.setString(3, this.strURL);
                prepSt.setString(4, this.strProgram);
                prepSt.setInt(5,this.iSYEnd);
                if(prepSt.execute()){
                    throw new SQLException("Failed to upload entry: " + this.strTitle);
                }
                // *get id
                prepSt = connection.prepareStatement(qEntry2);
                prepSt.setString(1,this.strTitle);
                rs = prepSt.executeQuery();
                if(rs.next()){
                   this.id = rs.getInt("colEntryId"); 
                }
                else {
                    throw new SQLException("Unable to load entry id");
                }
                //System.out.println("Entry successfully uploaded");
                
                 //UPLOAD EACH AUTHOR
                while(liA.hasNext()){
                    liA.next().upload(connection);
                    //System.out.println("Author loaded");
                }
                
                // UPLOAD EACH KEYWORD
                while(liK.hasNext()){
                    liK.next().upload(connection);
                    //System.out.println("Keyword loaded");
                }
                
                //RELATE ENTRY AND AUTHORS
                Element.relateElement(connection, this,lstAuthors);
                //System.out.println("Entry Author relation successful");
                
                //RELATE ENTRY AND KEYWORDS
                Element.relateElement(connection, this, lstKeyWords);
                //System.out.println("Entry Keywords Relation successfull");
               
                //UPLOAD FILE TO SERVER DIRECTORY
                Configuration config = new Configuration();
                File fileOut = new File(config.getServerDir()+this.file.getName());  
                FileOutputStream fout = new FileOutputStream(fileOut);
                FileInputStream fin = new FileInputStream(this.file);
                byte[] b = new byte[25000000];
                fin.read(b);
                fout.write(b);
                System.out.println("File transfered: "+ this.file.getName());
                connection.commit();
                connection.close();
                return true;
            } catch(SQLException exs){    
                connection.rollback();
                connection.close();
               throw new SQLException("Rollback happened- "+exs.getMessage());
            }  catch(IOException exs){
                connection.rollback();
                connection.close();
                throw new IOException("Rollback happened- "+exs.getMessage());
            }  catch(Exception e){
                connection.rollback();
                connection.close();
                throw new IOException("Some items not uploaded-"+e.getMessage());
            }
        } catch (SQLException|ClassNotFoundException|IOException ex1) {
            throw new Exception("Upload Error: " + ex1.getMessage());
        } 
    }
    //STATIC
    public final static String PROGRAM_CPE = "CPE";
    public final static String PROGRAM_ME = "ME";
    public final static String PROGRAM_ECE = "ECE";
    public final static String PROGRAM_EE = "EE";
    public final static String PROGRAM_CE = "CE";
    public final static String PROGRAM_CHE = "CHE";
    
}
