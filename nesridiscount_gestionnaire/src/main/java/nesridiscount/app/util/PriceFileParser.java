package nesridiscount.app.util;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * parseur de fichier
 */
public class PriceFileParser extends Thread{
    private Action toDoOnSuccess;
    private Action toDoOnFail;

    private File toParse;

    private String columnsToParse;
    private String columnsFormula;
    private String csvContent = "";

    public PriceFileParser(File toParse,String columnsToParse,String columnsFormula) throws Exception{
        this.toParse = toParse;
        this.columnsFormula = columnsFormula.replaceAll("\\s","");
        this.columnsToParse = columnsToParse.replaceAll("\\s","");

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
            if(this.toParse.getName().endsWith("csv") )
                this.parseCsv();
            else
                this.parseXlxs();   

            this.toDoOnSuccess.doAction();
        }
        catch(Exception e){
            try{
                this.toDoOnFail.doAction();
            }
            catch(Exception exception){}
        }
    }

    public File getToParse() {
        return this.toParse;
    }

    public String getCsvContent() {
        return this.csvContent;
    }

    /**
     * parse le fichier par csv
     * @throws Exception en cas d'échec
     */
    private void parseCsv() throws Exception{
        Scanner scanner = null;

        try{
            scanner = new Scanner(this.toParse,StandardCharsets.UTF_8);

            ArrayList<Integer> columnsToGet = this.getIndexesToRead();
            HashMap<Integer,String> formulasMap = this.getColumnsFormula();

            String headerCols[] = null;

            // récupération de l'en tête
            while(scanner.hasNextLine() ){
                String line = scanner.nextLine();

                headerCols = line.split(",");

                if(headerCols.length == 0) continue;

                break;
            }

            if(headerCols == null || headerCols.length == 0) throw new Exception();

            // création de l'en tête du csv
            for(Integer index : columnsToGet){
                if(index + 1 > headerCols.length) throw new Exception();

                this.csvContent += headerCols[index] + ",";
            }

            this.csvContent = this.csvContent.substring(0,this.csvContent.length() - 1) + "\n";

            // récupération et parse des lignes
            while(scanner.hasNextLine() ) this.addLine(scanner.nextLine().split(","),columnsToGet,formulasMap);
        }
        catch(Exception e){
            throw e;
        }
        finally{
            if(scanner != null) scanner.close();
        }
    }

    /**
     * parse le fichier par xlxs
     * @throws Exception en cas d'échec
     */
    private void parseXlxs() throws Exception{

    }

    /**
     *
     * @return la liste des index à récupérer
     */
    private ArrayList<Integer> getIndexesToRead(){
        String indexes[] = this.columnsToParse.split(":");

        ArrayList<Integer> indexesToRead = new ArrayList<>(indexes.length);

        for(String index : indexes){
            Integer intIndex = Integer.parseInt(index);

            if(intIndex > 0) indexesToRead.add(intIndex - 1);
        }

        return indexesToRead;
    }

    /**
     * 
     * @return la liste des formules liés au colonnes indéxés par l'index donnée
     */
    private HashMap<Integer,String> getColumnsFormula(){
        HashMap<Integer,String> formulasMap = new HashMap<>();

        /* 
            [numero_colonne] * + () /
            format : colonne:format_formule

        */ 
        for(String columnFormula : this.columnsFormula.split(",") ){
            String[] formulaData = columnFormula.split(":");

            if(formulaData.length == 2){
                try{
                    // enregistrement de la formule avec comme clé l'indice de la colonne
                    formulasMap.put(Integer.parseInt(formulaData[0]) - 1,formulaData[1].replaceAll("\\s","") );
                }
                catch(Exception e){}
            }
        }

        return formulasMap;
    }

    /**
     * ajoute la ligne au résultat csv en changeant les valeurs
     * @param lineDatas
     * @param columnsToGet
     * @param formulasMap
     */
    private void addLine(String[] lineDatas,ArrayList<Integer> columnsToGet,HashMap<Integer,String> formulasMap){
        String toGet[] = new String[columnsToGet.size()];

        

        this.csvContent += String.join(",",toGet) + "\n";
    }
}
