package nesridiscount.app.util;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

            System.out.println(this.csvContent);

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
        try{
            String toGet[] = new String[columnsToGet.size()];

            // construction de la chaine en parsant les colonnes
            for(int index = 0; index < toGet.length; index++){
                // récupération de la valeur de la colonne
                int indexToGet = columnsToGet.get(index);

                String colValue = indexToGet < lineDatas.length ? lineDatas[indexToGet] : null;

                // colonne vide
                if(colValue == null){
                    toGet[index] = "";

                    continue;
                }

                String formula = formulasMap.get(indexToGet);

                // sans formule on conserve la valeur de base
                if(formula == null){
                    toGet[index] = colValue;
                    
                    continue;
                }

                // colonne avec formule à appliquer
                toGet[index] = this.parseFormula(lineDatas,formula);
            }

            this.csvContent += String.join(",",toGet) + "\n";
        }
        catch(Exception e){}
    }

    /**
     * parse la formule et 
     * @param linesData
     * @param formula
     * @return la chaine contenant le résultat de la formule
     * @throws Exception en cas d'erreur lors du parse
     */
    private String parseFormula(String[] linesData,String formula) throws Exception{
        // ajout de parenthèses autour du calcul pour l'algorithme parenthèse
        formula = "(" + formula + ")";

        // recherche et remplacement des valeurs de colonne dans la formule
        Matcher matcher = Pattern.compile("\\[[0-9]+\\]").matcher(formula);
        
        while(matcher.find() ){
            String match = matcher.group();

            Integer index = Integer.parseInt(match.replace("[","").replace("]","") ) - 1;

            if(index < linesData.length) 
                formula = formula.replace(match,linesData[index]);
            else
                continue;
        }

        return Double.toString(Calculator.calculate(formula) );
    }
}
