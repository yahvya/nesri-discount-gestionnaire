package nesridiscount.app.util;

import java.io.File;

/**
 * parseur de fichier
 */
public class PriceFileParser extends Thread{
    private Action toDoOnSuccess;
    private Action toDoOnFail;
    
    private File toParse;
    /**
     * contient les données du fichier généré
     */
    private File dstFile;

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

    }

    public File getToParse() {
        return this.toParse;
    }

    public File getDstFile() {
        return this.dstFile;
    }
    
}
