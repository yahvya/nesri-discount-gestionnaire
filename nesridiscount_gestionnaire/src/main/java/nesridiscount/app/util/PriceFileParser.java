package nesridiscount.app.util;

import java.io.File;

/**
 * parseur de fichier
 */
public class PriceFileParser extends Thread{
    private Action toDoOnSuccess;
    private Action toDoOnFail;
    
    private File toParse;
    
    private String csvContent = "";

    public PriceFileParser(File toParse) throws Exception{
        this.toParse = toParse;
        
        this.setDaemon(true);
    }

    public void setToDoOnSuccess(Action toDoOnSuccess) {
        this.toDoOnSuccess = toDoOnSuccess;
    }

    public void setToDoOnFail(Action toDoOnFail) {
        this.toDoOnFail = toDoOnFail;
    }


    @Override
    public void run(){
        try{
            this.toDoOnSuccess.doAction();
        }
        catch(Exception e){}
    }

    public File getToParse() {
        return this.toParse;
    }

    public String getCsvContent() {
        return this.csvContent;
    }
    
}
